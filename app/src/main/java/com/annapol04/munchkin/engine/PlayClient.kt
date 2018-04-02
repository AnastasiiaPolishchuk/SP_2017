package com.annapol04.munchkin.engine

import android.app.Activity
import android.content.Intent
import android.util.Log

import java.util.Arrays

abstract class PlayClient {

    var matchStateChangedListener: OnMatchStateChangedListener? = null
    var messageReceivedListener: OnMessageReceivedListener? = null

    abstract val isLoggedIn: Boolean

    abstract val amountOfPlayers: Int

    enum class MatchState {
        LOGGED_OUT,
        LOGGED_IN,
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

    protected fun changeMatchState(state: MatchState) {
        if (matchStateChangedListener != null)
            matchStateChangedListener!!.onMatchStateChanged(state)
    }

    protected fun messageReceived(data: ByteArray) {
        if (messageReceivedListener != null)
            messageReceivedListener!!.onMessageReceived(data)
    }

    abstract fun setActivity(activity: Activity)

    abstract fun login()

    abstract fun processActivityResults(requestCode: Int, resultCode: Int, data: Intent)

    abstract fun startQuickGame()

    abstract fun sendToAll(message: ByteArray)
}
