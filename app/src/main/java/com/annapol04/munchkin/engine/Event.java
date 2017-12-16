package com.annapol04.munchkin.engine;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import javax.annotation.Nullable;


public class Event {
    private final Scope scope;
    private final Action action;
    private final int messageId;
    private final DataType dataType;
    @Nullable
    private final Object data;

    public Event(Scope scope, Action action, int messageId) {
        this(scope, action, messageId, DataType.EMPTY, null);
    }

    public Event(Scope scope, Action action, int messageId, int data) {
        this(scope, action, messageId, DataType.INTEGER, data);
    }

    public Event(Scope scope, Action action, int messageId, String data) {
        this(scope, action, messageId, DataType.STRING, data);
    }

    private Event(Scope scope, Action action, int messageId, DataType type, Object data) {
        this.scope = scope;
        this.action = action;
        this.messageId = messageId;
        this.dataType = type;
        this.data = data;
    }

    public void execute(Game game) {
        action.execute(game, data);
    }

    public Scope getScope() {
        return scope;
    }

    public Action getAction() {
        return action;
    }

    public int getMessageId() {
        return messageId;
    }

    public DataType getDataType() {
        return dataType;
    }

    public int getInteger() {
        return (int)data;
    }

    public String getString() {
        return (String) data;
    }

    public int size() {
        int size = 10;

        if (dataType == DataType.INTEGER)
            size += 4;
        else if (dataType == DataType.STRING)
            size += ((String)data).length() + 1;
        else if (dataType != DataType.EMPTY)
            throw new IllegalStateException("Unknown data type");

        return size;
    }

    public byte[] getBytes() {
        ByteBuffer b = ByteBuffer.allocate(size())
                .put((byte)scope.ordinal())
                .putInt(action.getId())
                .putInt(messageId)
                .put((byte)dataType.ordinal());

        switch (dataType) {
            case EMPTY: break;
            case STRING:
                b.put(((String)data).getBytes(StandardCharsets.UTF_8)).put((byte)0);
                break;
            case INTEGER:
                b.putInt((int)data);
                break;
            default:
                throw new IllegalStateException("Unknown data type to serialize");
        }

        return b.array();
    }
}
