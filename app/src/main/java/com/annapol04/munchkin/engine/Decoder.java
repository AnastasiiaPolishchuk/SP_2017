package com.annapol04.munchkin.engine;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * Decodes following structure.
 *
 * # Event structure
 * |  previous hash  |   hash   | scope  |  action id |  message id  |  data type |  data   |
 * |-----------------|----------|--------|------------|--------------|------------|---------|
 * |    16 bytes     | 16 bytes | 1 Byte |   4 Byte   |   4 Byte     |   1 Byte   | n Bytes |
 */
@Singleton
public class Decoder {
    private List<Event> decodedEvents;

    private static final int HEADER_LENGTH = 42;

    @Inject
    public Decoder() { }

    public List<Event> decode(byte[] data, int pos, int length) {
        final int end = pos + length;

        decodedEvents = new ArrayList<>();

        while (pos < end)
            pos = decodeEvent(data, pos, end);

        return decodedEvents;
    }

    public EventData decodeEventData(byte[] data) {
        if (data.length < 4)
            throw new IllegalArgumentException("Illegal event data format: " + Arrays.toString(data));

        ByteBuffer buffer = ByteBuffer.wrap(data);

        switch (DataType.fromId(buffer.getInt())) {
            case EMPTY:
                return new EventData();
            case INTEGER:
                if (data.length < 8)
                    throw new IllegalArgumentException("Illegal event data format: " + Arrays.toString(data));

                return new EventData(buffer.getInt());
            case STRING:
                if (data.length < 5)
                    throw new IllegalArgumentException("Illegal event data format: " + Arrays.toString(data));

                byte[] stringBuffer = Arrays.copyOfRange(buffer.array(), buffer.position(), 0);
                String string = new String(stringBuffer, StandardCharsets.UTF_8);
                return new EventData(new String(stringBuffer, StandardCharsets.UTF_8));
        }

        return null;
    }

    private int parseInteger(byte[] data, int pos) {
        return data[pos] << 24
            | (data[pos + 1] & 0xFF) << 16
            | (data[pos + 2] & 0xFF) << 8
            | (data[pos + 3] & 0xFF);
    }

    private byte[] parseHash(byte[] data, int pos) {
        byte[] hash = new byte[16];

        System.arraycopy(data, pos, hash, 0, 16);

        return hash;
    }

    private int strlen(byte[] data, int pos, int end) {
        int length = 0;

        while (pos + length < end) {
            if (data[pos + length] == (byte)0)
                return length;

            length++;
        }

        return -1;
    }

    private int decodeEvent(byte[] data, int pos, int end) {
        int endOfheader = pos + HEADER_LENGTH;

        if (endOfheader <= end) {
            try {
                final byte[] previousHash = parseHash(data, pos);
                final byte[] hash = parseHash(data, pos + 16);
                final int scopeId = (int) data[pos + 32];
                final int actionId = parseInteger(data, pos + 33);
                final int messageId = parseInteger(data, pos + 37);
                final int dataTypeId = (int) data[pos + 41];

                final Scope scope = Scope.fromId(scopeId);
                final Action action = Action.fromId(actionId);
                final DataType dataType = DataType.fromId(dataTypeId);

                if (dataType == DataType.EMPTY)
                    decodedEvents.add(new Event(scope, action, messageId, previousHash, hash));
                else if (dataType == DataType.STRING) {
                    int length = strlen(data, endOfheader, end);

                    if (length < 0)
                        throw new IllegalStateException("Unknown string format");
                    else {
                        byte[] stringBuffer = new byte[length];
                        System.arraycopy(data, endOfheader, stringBuffer, 0, length);
                        String string = new String(stringBuffer, StandardCharsets.UTF_8);
                        decodedEvents.add(new Event(scope, action, messageId, new EventData(string), previousHash, hash));
                        endOfheader += length + 1;
                    }
                } else if (dataType == DataType.INTEGER) {
                    if (endOfheader + 4 > end)
                        throw new IllegalStateException("Unknown integer format");
                    else
                        decodedEvents.add(new Event(scope, action, messageId,
                                new EventData(parseInteger(data, endOfheader)), previousHash, hash));
                } else
                    throw new IllegalStateException("Unknown event data type " + dataTypeId);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            return endOfheader;
        }

        return end;
    }

}
