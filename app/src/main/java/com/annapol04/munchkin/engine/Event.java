package com.annapol04.munchkin.engine;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.nio.ByteBuffer;


@Entity(tableName = "events")
public class Event {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private final Scope scope;
    private final Action action;
    private final int messageId;
    private final EventData data;

    @Ignore
    public Event(Scope scope, Action action, int messageId) {
        this(scope, action, messageId, new EventData());
    }

    @Ignore
    public Event(Scope scope, Action action, int messageId, int data) {
        this(scope, action, messageId, new EventData(data));
    }

    @Ignore
    public Event(Scope scope, Action action, int messageId, String data) {
        this(scope, action, messageId, new EventData(data));
    }

    public Event(Scope scope, Action action, int messageId, EventData data) {
        this.scope = scope;
        this.action = action;
        this.messageId = messageId;
        this.data = data;
    }

    public void execute(Game game) {
        action.execute(game, data.getData());
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
        return data.getType();
    }

    public int getInteger() {
        return data.getInteger();
    }

    public String getString() {
        return data.getString();
    }

    public int size() {
        return 9 + data.size();
    }

    public byte[] getBytes() {
        return ByteBuffer.allocate(size())
                .put((byte)scope.ordinal())
                .putInt(action.getId())
                .putInt(messageId)
                .put(data.getBytes())
                .array();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public EventData getData() {
        return data;
    }
}
