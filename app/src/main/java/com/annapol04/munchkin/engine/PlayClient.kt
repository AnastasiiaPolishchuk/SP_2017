package com.annapol04.munchkin.engine

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.annapol04.munchkin.network.GooglePlayClient

import java.util.Arrays

abstract class PlayClient {
    val TAG = PlayClient::class.java.simpleName

    var matchStateChangedListener: OnMatchStateChangedListener? = null
    var messageReceivedListener: OnMessageReceivedListener? = null

    var matchState = MatchState.LOGGED_OUT
        protected set(value) {
            if (field != value) {
                field = value

                Log.i(TAG, "MATCH_STATE=" + value.toString())

                matchStateChangedListener?.onMatchStateChanged(value)
            }
        }

    abstract val isLoggedIn: Boolean

    abstract val amountOfPlayers: Int

    enum class MatchState {
        LOGGED_OUT,
        LOGGED_IN,
        MATCHMAKING,
        STARTED,
        ABORTED,
        DISCONNECTED
    }

    interface OnMatchStateChangedListener {
        fun onMatchStateChanged(state: MatchState)
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

    abstract fun sendToAll(message: ByteArray)
}
