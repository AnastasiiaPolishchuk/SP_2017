package com.annapol04.munchkin.engine;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class EventData {
    private final DataType type;
    private final Object data;

    public EventData() {
        this(DataType.EMPTY, null);
    }

    public EventData(int data) {
        this(DataType.INTEGER, data);
    }

    public EventData(String data) {
        this(DataType.STRING, data);
    }

    private EventData(DataType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public int size() {
        switch (type) {
            case STRING:
                return getString().length() + 1 + 1;
            case INTEGER:
                return 4 + 1;
            case EMPTY:
                return 1;
        }

        return 0;
    }

    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(size());
        buffer.put((byte)type.ordinal());

        switch (type) {
            case STRING:
                return buffer.put(getString().getBytes(StandardCharsets.UTF_8))
                             .put((byte)0)
                             .array();
            case INTEGER:
                return buffer.putInt(getInteger()).array();
            case EMPTY:
                return buffer.array();
        }

        return null;
    }

    public DataType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public String getString() {
        return (String)data;
    }

    public int getInteger() {
        return (int)data;
    }
}
