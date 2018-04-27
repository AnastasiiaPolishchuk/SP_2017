package com.annapol04.munchkin.engine

import android.app.Activity
import android.content.Intent
import android.util.Log

abstract class PlayClient {
    val TAG = PlayClient::class.java.simpleName

    var matchStateChangedListener: OnMatchStateChangedListener? = null
    var messageReceivedListener: OnMessageReceivedListener? = null

    var matchState = ClientState.LOGGED_OUT
        protected set(value) {
            if (field != value) {
                field = value

                Log.i(TAG, "MATCH_STATE=" + value.toString())

                matchStateChangedListener?.onMatchStateChanged(value)
            }
        }

    var errorReason: Int? = null

    abstract val isLoggedIn: Boolean

    abstract val amountOfPlayers: Int

    enum class ClientState {
        LOGGED_OUT,
        LOGGED_IN,
        MATCHMAKING,
        STARTED,
        ABORTED,
        DISCONNECTED
    }

    interface OnMatchStateChangedListener {
        fun onMatchStateChanged(state: ClientState)
    }

    interface OnMessageReceivedListener {
        fun onMessageReceived(data: ByteArray)
    }

    protected fun messageReceived(data: ByteArray) {
        messageReceivedListener?.onMessageReceived(data)
    }

    abstract fun setActivity(activity: Activity?)

    abstract fun login()

    abstract fun processActivityResults(requestCode: Int, resultCode: Int, data: Intent?)

    abstract fun startQuickGame()

    abstract fun leaveGame()

    abstract fun sendToAll(message: ByteArray)
}
