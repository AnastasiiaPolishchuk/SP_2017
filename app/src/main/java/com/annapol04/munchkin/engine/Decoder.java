package com.annapol04.munchkin.engine;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;


/**
 * Decodes following structure.
 *
 * # Event structure
 * |  scope |  action id |  message id  |  data type |  data   |
 * |--------|------------|--------------|------------|---------|
 * | 1 Byte |   4 Byte   |   4 Byte     |   1 Byte   | n Bytes |
 */
public class Decoder {
    private List<Event> decodedEvents = new ArrayList<>();

    private static final int HEADER_LENGTH = 10;

    @Inject
    public Decoder() { }

    public List<Event> decode(byte[] data, int pos, int length) {
        final int end = pos + length;

        while (pos < end)
            pos = decodeEvent(data, pos, end);

        return decodedEvents;
    }

    private int parseInteger(byte[] data, int pos) {
        return data[pos] << 24
            | (data[pos + 1] & 0xFF) << 16
            | (data[pos + 2] & 0xFF) << 8
            | (data[pos + 3] & 0xFF);
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
                final int scopeId = (int) data[pos];
                final int actionId = parseInteger(data, pos + 1);
                final int messageId = parseInteger(data, pos + 5);
                final int dataTypeId = (int) data[pos + 9];

                final Scope scope = Scope.fromId(scopeId);
                final Action action = Action.fromId(actionId);
                final DataType dataType = DataType.fromId(dataTypeId);

                if (dataType == DataType.EMPTY)
                    decodedEvents.add(new Event(scope, action, messageId));
                else if (dataType == DataType.STRING) {
                    int length = strlen(data, endOfheader, end);

                    if (length < 0)
                        throw new IllegalStateException("Unknown string format");
                    else {
                        byte[] stringBuffer = new byte[length];
                        System.arraycopy(data, endOfheader, stringBuffer, 0, length);
                        String string = new String(stringBuffer, StandardCharsets.UTF_8);
                        decodedEvents.add(new Event(scope, action, messageId, string));
                        endOfheader += length + 1;
                    }
                } else if (dataType == DataType.INTEGER) {
                    if (endOfheader + 4 > end)
                        throw new IllegalStateException("Unknown integer format");
                    else
                        decodedEvents.add(new Event(scope, action, messageId, parseInteger(data, endOfheader)));
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
