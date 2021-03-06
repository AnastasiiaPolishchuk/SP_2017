package com.annapol04.munchkin.network

import android.app.Activity
import android.content.Intent

import com.annapol04.munchkin.engine.PlayClient

class PlayClientDummy : PlayClient() {
    override var isLoggedIn = false
        private set

    override val amountOfPlayers: Int
        get() = 3

    override fun setActivity(activity: Activity?) {

    }

    override fun login() {
        matchState = ClientState.LOGGED_IN
    }

    override fun processActivityResults(requestCode: Int, resultCode: Int, data: Intent?) {

    }

    override fun startQuickGame() {
        matchState = ClientState.STARTED
    }

    override fun leaveGame() {

    }

    override fun sendToAll(message: ByteArray) {
        messageReceived(message)
    }
}
