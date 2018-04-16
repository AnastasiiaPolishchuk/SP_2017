package com.annapol04.munchkin.engine

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

import java.nio.ByteBuffer

@Entity(tableName = "events")
class Event(@PrimaryKey(autoGenerate = true)
            val id: Long? = null,
            val scope: Scope,
            val action: Action,
            val messageId: Int,
            val data: EventData,
            private var previousHash: ByteArray,
            var hash: ByteArray) {

    @Ignore
    constructor(scope: Scope, action: Action, messageId: Int, data: EventData, prevHash: ByteArray, hash: ByteArray)
            : this(null, scope, action, messageId, data, prevHash, hash)


    @Ignore
    constructor(scope: Scope, action: Action, messageId: Int, data: Int)
            : this(scope, action, messageId, EventData(data))

    @Ignore
    constructor(scope: Scope, action: Action, messageId: Int, data: String)
            : this(scope, action, messageId, EventData(data))

    @Ignore
    @JvmOverloads constructor(scope: Scope, action: Action, messageId: Int, data: EventData = EventData())
            : this(0, scope, action, messageId, data, ByteArray(16), ByteArray(16))

    @Ignore
    constructor(scope: Scope, action: Action, messageId: Int, previousHash: ByteArray, hash: ByteArray)
            : this(0, scope, action, messageId, EventData(), previousHash, hash)

    @Throws(IllegalEngineStateException::class)
    fun execute(match: Match, desk: Desk) {
        action.invoke(match, desk, this)
    }

    fun size(): Int {
        return 16 + 16 + 1 + 4 + 4 + data.size()
    }

    fun getDataType() = data.type
    fun getInteger() = data.getInteger()
    fun getString() = data.getString()

    fun getBytes(): ByteArray {
        return ByteBuffer.allocate(size())
                .put(previousHash)
                .put(hash)
                .put(scope.ordinal.toByte())
                .putInt(action.id)
                .putInt(messageId)
                .put(data.getBytes())
                .array()
    }

    fun getPreviousHash() = previousHash
    fun setPreviousHash(previousHash: ByteArray) {
        if (action == Action.JOIN_PLAYER) {
            this.previousHash = previousHash

            this.hash = this.previousHash
        } else {
            this.previousHash = previousHash

            hash = HashUtil.applyMD5(
                    ByteBuffer.allocate(size())
                            .put(previousHash)
                            .put(scope.ordinal.toByte())
                            .putInt(action.id)
                            .putInt(messageId)
                            .put(data.getBytes())
                            .array())
        }
    }

    fun getMessage(messageBook: MessageBook, player: Player?, anonymized: Boolean): String {
        return messageBook.build(this, player, anonymized)
    }

    override fun toString(): String {
        return "(" +
                scope.toString() +
                ", " +
                action.toString() +
                ", " +
                data.toString() +
                ", " +
                messageId +
                ")"
    }

    fun toString(messageBook: MessageBook, player: Player?, anonymized: Boolean): String {
        return "(" +
                scope.toString() +
                ", " +
                action.toString() +
                ", " +
                data.toString() +
                ", \"" +
                getMessage(messageBook, player, anonymized) +
                "\")"
    }
}
