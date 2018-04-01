package com.annapol04.munchkin.engine;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Arrays;

import okio.ByteString;


@Entity(tableName = "events")
public class Event {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private final Scope scope;
    private final Action action;
    private final int messageId;
    private final EventData data;
    private byte[] previousHash;
    private byte[] hash;

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

    @Ignore
    public Event(Scope scope, Action action, int messageId, byte[] previousHash, byte[] hash) {
        this(scope, action, messageId, new EventData(), previousHash, hash);
    }

    public Event(Scope scope, Action action, int messageId, EventData data, byte[] previousHash, byte[] hash) {
        this.scope = scope;
        this.action = action;
        this.messageId = messageId;
        this.data = data;
        this.previousHash = previousHash;
        this.hash = hash;
    }

    public void execute(Match match, Game game) throws IllegalEngineStateException {
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
        return 41 + data.size();
    }

    public byte[] getBytes() {
        return ByteBuffer.allocate(size())
                .put(previousHash)
                .put(hash)
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

    public byte[] getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(byte[] previousHash) {
        if (action == Action.JOIN_PLAYER) {
            this.previousHash = previousHash;

            this.hash = this.previousHash;
        } else {
            this.previousHash = previousHash;

            hash = HashUtil.applyMD5(
                    ByteBuffer.allocate(size())
                    .put(previousHash)
                    .put((byte)scope.ordinal())
                    .putInt(action.getId())
                    .putInt(messageId)
                    .put(data.getBytes())
                    .array());
        }
    }

    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public EventData getData() {
        return data;
    }

    public String getMessage(MessageBook messageBook, Player player, boolean anonymized) {
        return messageBook.build(this, player, anonymized);
    }

    @Override
    public String toString() {
        return "(" +
                scope.toString() +
                ", " +
                action.toString() +
                ", " +
                data.toString() +
                ", " +
                messageId +
                ")";
    }

    public String toString(MessageBook messageBook, Player player, boolean anonymized) {
        return "(" +
                scope.toString() +
                ", " +
                action.toString() +
                ", " +
                data.toString() +
                ", \"" +
                getMessage(messageBook, player, anonymized) +
                "\")";
    }
}
