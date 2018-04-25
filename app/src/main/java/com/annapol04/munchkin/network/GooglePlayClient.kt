package com.annapol04.munchkin.network

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log

import javax.inject.Inject
import javax.inject.Singleton

import com.annapol04.munchkin.engine.PlayClient
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.games.Games
import com.google.android.gms.games.GamesActivityResultCodes
import com.google.android.gms.games.GamesCallbackStatusCodes
import com.google.android.gms.games.RealTimeMultiplayerClient
import com.google.android.gms.games.multiplayer.Participant
import com.google.android.gms.games.multiplayer.realtime.*
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException



@Singleton
class GooglePlayClient @Inject
constructor(private val application: Application) : PlayClient() {
    private var activity: Activity? = null
    private var mJoinedRoomConfig: RoomConfig? = null
    private var mMyParticipantId: String? = null
    private var mRoom: Room? = null

    private var account: GoogleSignInAccount? = null
        set(value) {
            field = value

            when (matchState) {
                in arrayOf(MatchState.ABORTED, MatchState.LOGGED_OUT, MatchState.DISCONNECTED) -> {
                    if (value != null)
                        matchState = MatchState.LOGGED_IN
                }
                in arrayOf(MatchState.LOGGED_IN, MatchState.STARTED, MatchState.MATCHMAKING) -> {
                    if (value == null)
                        matchState = MatchState.LOGGED_OUT
                }
                else -> Error("Case not implemented")
            }
        }

    override val isLoggedIn: Boolean
        get() = account != null

    override val amountOfPlayers: Int
        get() = if (mRoom == null) 0 else mRoom!!.participantIds.size

    private val mMessageReceivedHandler = { realTimeMessage: RealTimeMessage ->
        messageReceived(realTimeMessage.getMessageData())
    }

    private val mWaitingRoomFinishedFromCode = false

    /*    private void onStartGameMessageReceived() {
        mWaitingRoomFinishedFromCode = true;
        finishActivity(RC_WAITING_ROOM);
    }
*/
    private val mRoomUpdateCallback = object : RoomUpdateCallback() {
        override fun onRoomCreated(code: Int, room: Room?) {
            // Update UI and internal state based on room updates.
            if (code == GamesCallbackStatusCodes.OK && room != null) {
                Log.d(TAG, "Room " + room.roomId + " created.")
                showWaitingRoom(room, MIN_PLAYERS)
            } else {
                Log.w(TAG, "Error creating room: ${CommonStatusCodes.getStatusCodeString(code)}")
                matchState = MatchState.ABORTED
            }
        }

        override fun onJoinedRoom(code: Int, room: Room?) {
            // Update UI and internal state based on room updates.
            if (code == GamesCallbackStatusCodes.OK && room != null) {
                Log.d(TAG, "Room " + room.roomId + " joined.")
            } else {
                Log.w(TAG, "Error joining room: $code")
                matchState = MatchState.ABORTED
            }
        }

        override fun onLeftRoom(code: Int, roomId: String) {
            Log.d(TAG, "Left room$roomId")
        }

        override fun onRoomConnected(code: Int, room: Room?) {
            if (code == GamesCallbackStatusCodes.OK && room != null) {
                Log.d(TAG, "Room " + room.roomId + " connected.")
            } else {
                Log.w(TAG, "Error connecting to room: $code")
                matchState = MatchState.ABORTED
            }
        }
    }

    // are we already playing?
    internal var mPlaying = false

    private val mRoomStatusCallbackHandler = object : RoomStatusUpdateCallback() {
        override fun onRoomConnecting(room: Room?) {
            // Update the UI status since we are in the process of connecting to a specific room.
        }

        override fun onRoomAutoMatching(room: Room?) {
            // Update the UI status since we are in the process of matching other players.
        }

        override fun onPeerInvitedToRoom(room: Room?, list: List<String>) {
            // Update the UI status since we are in the process of matching other players.
        }

        override fun onPeerDeclined(room: Room?, list: List<String>) {
            // Peer declined invitation, see if desk should be canceled
            if (!mPlaying && shouldCancelGame(room))
                leaveRoom()
        }

        override fun onPeerJoined(room: Room?, list: List<String>) {
            // Update UI status indicating new players have joined!
        }

        override fun onPeerLeft(room: Room?, list: List<String>) {
            // Peer left, see if desk should be canceled.
            if (!mPlaying && shouldCancelGame(room))
                leaveRoom()
        }

        override fun onConnectedToRoom(room: Room?) {
            // Connected to room, record the room Id.
            mRoom = room
            Games.getPlayersClient(application, GoogleSignIn.getLastSignedInAccount(application)!!)
                    .currentPlayerId.addOnSuccessListener { playerId -> mMyParticipantId = mRoom!!.getParticipantId(playerId) }
        }

        override fun onDisconnectedFromRoom(room: Room?) {
            // This usually happens due to a network error, leave the desk
            // show error message and return to main screen
            matchState = MatchState.DISCONNECTED
        }

        override fun onPeersConnected(room: Room?, list: List<String>) {
            if (mPlaying) {
                // add new player to an ongoing desk
            } else if (shouldStartGame(room)) {
                // start desk!
            }
        }

        override fun onPeersDisconnected(room: Room?, list: List<String>) {
            if (mPlaying) {
                // do desk-specific handling of this -- remove player's avatar
                // from the screen, etc. If not enough players are left for
                // the desk to go on, end the desk and leave the room.
            } else if (shouldCancelGame(room)) {
                // cancel the desk
                matchState = MatchState.ABORTED
            }
        }

        override fun onP2PConnected(participantId: String) {
            // Update status due to new peer to peer connection.
        }

        override fun onP2PDisconnected(participantId: String) {
            // Update status due to  peer to peer connection being disconnected.
        }
    }

    override fun setActivity(activity: Activity?) {
        this.activity = activity
    }

    override fun login() {
        account = GoogleSignIn.getLastSignedInAccount(activity!!)

        if (!isLoggedIn)
            signInSilently()
    }

    override fun processActivityResults(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)

            if (result.isSuccess)
                account = result.signInAccount
            else {
                Log.e(TAG, "Failed to log into google play games services")
                Log.i(TAG, "Retry sign in procedure");

                startSignInIntent()
            }
        } else if (requestCode == RC_WAITING_ROOM) {

            // Look for finishing the waiting room from code, for example if a
            // "start desk" message is received.  In this case, ignore the result.
            if (mWaitingRoomFinishedFromCode) {
                return
            }

            if (resultCode == Activity.RESULT_OK) {
                matchState = MatchState.STARTED
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Waiting room was dismissed with the back button. The meaning of this
                // action is up to the desk. You may choose to leave the room and cancel the
                // match, or do something else like minimize the waiting room and
                // continue to connect in the background.
                leaveRoom()
                matchState = MatchState.ABORTED
            } else if (resultCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
                // player wants to leave the room.
                matchState = MatchState.ABORTED
            }
        }
    }

    private fun buildSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                .requestProfile()
                .build()
    }

    private fun signInSilently() {
        val signInClient = GoogleSignIn.getClient(activity!!, buildSignInOptions())
        signInClient.silentSignIn().addOnCompleteListener(activity!!
        ) { task ->
            if (task.isSuccessful) {
                account = GoogleSignIn.getLastSignedInAccount(activity!!)
            }else
                startSignInIntent()
        }
    }

    private fun startSignInIntent() {
        if (activity != null) {
            val signInClient = GoogleSignIn.getClient(activity!!, buildSignInOptions())
            val intent = signInClient.signInIntent
            activity!!.startActivityForResult(intent, RC_SIGN_IN)
        }
    }

    private fun showWaitingRoom(room: Room?, maxPlayersToStartGame: Int) {
        matchState = MatchState.MATCHMAKING

        Games.getRealTimeMultiplayerClient(application, account!!)
                .getWaitingRoomIntent(room!!, maxPlayersToStartGame)
                .addOnSuccessListener { intent -> activity?.startActivityForResult(intent, RC_WAITING_ROOM) }
    }

    override fun startQuickGame() {
        if (mRoom != null)
            throw IllegalStateException("There is already a desk started")

        // auto-match criteria to invite one random automatch opponent.
        // You can also specify more opponents (up to 3).
        val autoMatchCriteria = RoomConfig.createAutoMatchCriteria(1, 1, 0)

        // build the room config:
        val roomConfig = RoomConfig.builder(mRoomUpdateCallback)
                .setOnMessageReceivedListener(mMessageReceivedHandler)
                .setRoomStatusUpdateCallback(mRoomStatusCallbackHandler)
                .setAutoMatchCriteria(autoMatchCriteria)
                .build()

        // Save the roomConfig so we can use it if we call leave().
        mJoinedRoomConfig = roomConfig

        // create room:
        Games.getRealTimeMultiplayerClient(application, account!!)
                .create(roomConfig)
    }

    override fun sendToAll(message: ByteArray) {
        Games.getRealTimeMultiplayerClient(application, account!!)
                .sendUnreliableMessageToOthers(message, mRoom!!.roomId)
                .addOnCompleteListener { _ ->
                    messageReceived(message)
                    // Keep track of which messages are sent, if desired.
                    //     recordMessageToken(task.getResult());
                }
    }

    // returns whether there are enough players to start the desk
    internal fun shouldStartGame(room: Room?): Boolean {
        var connectedPlayers = 0
        for (p in room!!.participants) {
            if (p.isConnectedToRoom) {
                ++connectedPlayers
            }
        }
        return connectedPlayers >= MIN_PLAYERS
    }

    // Returns whether the room is in a state where the desk should be canceled.
    internal fun shouldCancelGame(room: Room?): Boolean {
        // TODO: Your desk-specific cancellation logic here. For example, you might decide to
        // cancel the desk if enough people have declined the invitation or left the room.
        // You can check a participant's status with Participant.getStatus().
        // (Also, your UI should have a Cancel button that cancels the desk too)
        return true
    }

    private fun leaveRoom() {
        if (mRoom != null && mJoinedRoomConfig != null) {
            Games.getRealTimeMultiplayerClient(application, account!!)
                    .leave(mJoinedRoomConfig!!, mRoom!!.roomId)
            //     getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            mRoom = null
            mJoinedRoomConfig = null
        }
    }

    companion object {
        private val TAG = GooglePlayClient::class.java.simpleName
        private val RC_SIGN_IN = 9001
        private val RC_WAITING_ROOM = 9007

        // at least 2 players required for our desk
        internal val MIN_PLAYERS = 1
    }
}
