package com.annapol04.munchkin.engine;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.nio.ByteBuffer;
import java.util.Arrays;


@Entity(tableName = "events")
public class Event {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private final Scope scope;
    private final Action action;
    private final int messageId;
    private final EventData data;
    private String previousHash;
    private String hash;

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

    @Ignore
    public Event(Scope scope, Action action, int messageId, EventData data) {
        this(scope, action, messageId, data, null, null);
    }

    public Event(Scope scope, Action action, int messageId, EventData data, String previousHash, String hash) {
        this.scope = scope;
        this.action = action;
        this.messageId = messageId;
        this.data = data;
        this.previousHash = previousHash;
        this.hash = hash;
    }

    public void execute(Match match, Game game) {
        action.execute(match, game, this);
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

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        if (action == Action.JOIN_PLAYER) {
            this.previousHash = previousHash;

            this.hash = this.previousHash;
        } else {
            this.previousHash = previousHash;

            this.hash = HashUtil.applySha256(previousHash + toString());
        }
    }

    public String getHash() {
        return hash;
    }

    public EventData getData() {
        return data;
    }

    public String getMessage(MessageBook messageBook, Player player) {
        return messageBook.build(this, player);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("[")
                .append(scope.toString())
                .append(", ")
                .append(action.toString())
                .append(", ")
                .append(data.toString())
                .append(", ")
                .append(messageId)
                .append("]")
                .toString();
    }

    public String toString(MessageBook messageBook, Player player) {
        return new StringBuilder()
                .append("[")
                .append(scope.toString())
                .append(", ")
                .append(action.toString())
                .append(", ")
                .append(data.toString())
                .append(", \"")
                .append(getMessage(messageBook, player))
                .append("\"]")
                .toString();
    }
}
