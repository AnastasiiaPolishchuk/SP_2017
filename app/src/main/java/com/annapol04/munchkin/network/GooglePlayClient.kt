package com.annapol04.munchkin.network

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.util.Log
import com.annapol04.munchkin.R
import com.annapol04.munchkin.engine.MessageBook

import javax.inject.Inject
import javax.inject.Singleton

import com.annapol04.munchkin.engine.PlayClient
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.games.Games
import com.google.android.gms.games.GamesActivityResultCodes
import com.google.android.gms.games.GamesCallbackStatusCodes
import com.google.android.gms.games.multiplayer.realtime.*


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
                in arrayOf(ClientState.ABORTED, ClientState.LOGGED_OUT, ClientState.DISCONNECTED) -> {
                    if (value != null)
                        matchState = ClientState.LOGGED_IN
                }
                in arrayOf(ClientState.LOGGED_IN, ClientState.STARTED, ClientState.MATCHMAKING) -> {
                    if (value == null)
                        matchState = ClientState.LOGGED_OUT
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
                leaveRoom(ClientState.DISCONNECTED, R.string.abort_reason_can_not_create_room)
            }
        }

        override fun onJoinedRoom(code: Int, room: Room?) {
            // Update UI and internal state based on room updates.
            if (code == GamesCallbackStatusCodes.OK && room != null) {
                Log.d(TAG, "Room " + room.roomId + " joined.")
            } else {
                Log.w(TAG, "Error joining room: $code")
                leaveRoom(ClientState.DISCONNECTED, R.string.abort_reason_can_not_create_room)
            }
        }

        override fun onLeftRoom(code: Int, roomId: String) {
            Log.d(TAG, "Left room$roomId")

   //         leaveRoom(ClientState.ABORTED, R.string.abort)
        }

        override fun onRoomConnected(code: Int, room: Room?) {
            if (code == GamesCallbackStatusCodes.OK && room != null) {
                Log.d(TAG, "Room " + room.roomId + " connected.")
            } else {
                Log.w(TAG, "Error connecting to room: $code")
                leaveRoom(ClientState.DISCONNECTED, R.string.abort_reason_can_not_create_room)
            }
        }
    }

    private val mRoomStatusCallbackHandler = object : RoomStatusUpdateCallback() {
        override fun onRoomConnecting(room: Room?) {
            // Update the UI status since we are in the process of connecting to a specific room.
            Log.i(TAG, "Room is connecting")
        }

        override fun onRoomAutoMatching(room: Room?) {
            // Update the UI status since we are in the process of matching other players.
            Log.i(TAG, "Room ist auto matching")
        }

        override fun onPeerInvitedToRoom(room: Room?, list: List<String>) {
            // Update the UI status since we are in the process of matching other players.
            Log.i(TAG, "Peer invited to room")
        }

        override fun onPeerDeclined(room: Room?, list: List<String>) {
            Log.i(TAG, "Peer declined invitation")
            // Peer declined invitation, see if desk should be canceled
            leaveRoom(ClientState.DISCONNECTED, R.string.abort_reason_opponent_lost_connection)
        }

        override fun onPeerJoined(room: Room?, list: List<String>) {
            Log.i(TAG, "Peer joined")
        }

        override fun onPeerLeft(room: Room?, list: List<String>) {
            // Peer left, see if desk should be canceled.
            Log.i(TAG, "Peer left")
            leaveRoom(ClientState.DISCONNECTED, R.string.abort_reason_player_left)
        }

        override fun onConnectedToRoom(room: Room?) {
            // Connected to room, record the room Id.
            Log.i(TAG, "Connected to room")

            mRoom = room
            Games.getPlayersClient(application, GoogleSignIn.getLastSignedInAccount(application)!!)
                    .currentPlayerId.addOnSuccessListener { playerId -> mMyParticipantId = mRoom!!.getParticipantId(playerId) }
        }

        override fun onDisconnectedFromRoom(room: Room?) {
            // This usually happens due to a network error, leave the desk
            // show error message and return to main screen
            Log.i(TAG, "Disconnected from room")

       //     leaveRoom(ClientState.DISCONNECTED, R.string.abort_reason_lost_connection)
        }

        override fun onPeersConnected(room: Room?, list: List<String>) {
            Log.i(TAG, "Peer connected")
        }

        override fun onPeersDisconnected(room: Room?, list: List<String>) {
            Log.i(TAG, "Peer disconnected")

            leaveRoom(ClientState.DISCONNECTED, R.string.abort_reason_opponent_lost_connection)
        }

        override fun onP2PConnected(participantId: String) {
            // Update status due to new peer to peer connection.
            Log.i(TAG, "P2P connected")
        }

        override fun onP2PDisconnected(participantId: String) {
            // Update status due to  peer to peer connection being disconnected.
            Log.i(TAG, "P2P disconnected")

            leaveRoom(ClientState.DISCONNECTED, R.string.abort_reason_opponent_lost_connection)
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
                matchState = ClientState.STARTED
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Waiting room was dismissed with the back button. The meaning of this
                // action is up to the desk. You may choose to leave the room and cancel the
                // match, or do something else like minimize the waiting room and
                // continue to connect in the background.
                leaveRoom(ClientState.ABORTED, R.string.ev_empty)
            } else if (resultCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
                // player wants to leave the room.
                leaveRoom(ClientState.ABORTED, R.string.ev_empty)
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
        matchState = ClientState.MATCHMAKING

        Games.getRealTimeMultiplayerClient(application, account!!)
                .getWaitingRoomIntent(room!!, maxPlayersToStartGame)
                .addOnSuccessListener { intent -> activity?.startActivityForResult(intent, RC_WAITING_ROOM) }
    }

    override fun startQuickGame() {
        if (mRoom != null) {
            Games.getRealTimeMultiplayerClient(application, account!!)
                    .leave(mJoinedRoomConfig!!, mRoom!!.roomId)

            mRoom = null
            mJoinedRoomConfig = null
        }

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

    override fun leaveGame() {
        leaveRoom(ClientState.ABORTED, R.string.ev_empty)
    }

    override fun sendToAll(message: ByteArray) {
        if (account != null)
            Games.getRealTimeMultiplayerClient(application, account!!)
                    .sendUnreliableMessageToOthers(message, mRoom!!.roomId)
                    .addOnCompleteListener { _ ->
                        messageReceived(message)
                    }
    }

    private fun leaveRoom(matchState: ClientState, reason: Int) {
        if (mRoom != null && mJoinedRoomConfig != null) {
            Games.getRealTimeMultiplayerClient(application, account!!)
                    .leave(mJoinedRoomConfig!!, mRoom!!.roomId)
            //     getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            mRoom = null
            mJoinedRoomConfig = null
        }

        this.errorReason = reason
        this.matchState = matchState
    }

    companion object {
        private val TAG = GooglePlayClient::class.java.simpleName
        private val RC_SIGN_IN = 9001
        private val RC_WAITING_ROOM = 9007

        // at least 2 players required for our desk
        internal val MIN_PLAYERS = 1
    }
}
