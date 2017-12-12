package com.annapol04.munchkin.engine;

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
public class EventDecoder {
    private List<Event> decodedEvents = new ArrayList<>();

    private static final int HEADER_LENGTH = 10;

    @Inject
    public EventDecoder() { }

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

    private int decodeEvent(byte[] data, int pos, int end) {
        final int endOfheader = pos + HEADER_LENGTH;

        if (endOfheader <= end) {
            try {
                final int scopeId = (int) data[pos];
                final int actionId = parseInteger(data, pos + 1);
                final int messageId = parseInteger(data, pos + 4);
                final int dataTypeId = parseInteger(data, pos + 9);

                final EventScope scope = EventScope.fromId(scopeId);
                final EventAction action = EventAction.fromId(actionId);
                final EventMessage message = EventMessage.fromId(messageId);
                final EventDataType dataType = EventDataType.fromId(dataTypeId);

                decodedEvents.add(new Event(scope, action, message, dataType));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            return endOfheader;
        }

        return end;
    }

}
