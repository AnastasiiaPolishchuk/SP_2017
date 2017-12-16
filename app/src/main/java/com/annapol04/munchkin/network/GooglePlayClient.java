package com.annapol04.munchkin.network;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesCallbackStatusCodes;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.OnRealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateCallback;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;
import com.google.android.gms.tasks.Task;

import java.util.List;

@Singleton
public class GooglePlayClient {
    private static final String TAG = GooglePlayClient.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;
    private static final int RC_WAITING_ROOM = 9007;

    private Application application;
    private Activity activity;
    private GoogleSignInAccount account;
    private RoomConfig mJoinedRoomConfig;
    private String mMyParticipantId;
    private Room mRoom;
    private OnMatchStateChangedListener matchStateChangedListener;

    public interface OnLoggedInListener {
        void onLoggedIn();
    }

    public enum MatchState {
        LOGGED_OUT,
        LOGGED_IN,
        STARTED,
        ABORTED,
        DISCONNECTED,
    }

    public interface OnMatchStateChangedListener {
        void onMatchStateChanged(MatchState state);
    }

    @Inject
    public GooglePlayClient(Application application) {
        this.application = application;

        account = GoogleSignIn.getLastSignedInAccount(application);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    private void loggedIn() {
        changeMatchState(MatchState.LOGGED_IN);
    }

    public void setMatchStateChangedListener(OnMatchStateChangedListener matchStateChangedListener) {
        this.matchStateChangedListener = matchStateChangedListener;
    }

    private void changeMatchState(MatchState state) {
        if (matchStateChangedListener != null)
            matchStateChangedListener.onMatchStateChanged(state);
    }

    public boolean isLoggedIn() {
        return account != null;
    }

    public void login() {
        if (isLoggedIn())
            loggedIn();
        else
            signInSilently();
    }

    public void processActivityResults(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess())
                loggedIn();

        } else if (requestCode == RC_WAITING_ROOM) {

            // Look for finishing the waiting room from code, for example if a
            // "start game" message is received.  In this case, ignore the result.
            if (mWaitingRoomFinishedFromCode) {
                return;
            }

            if (resultCode == Activity.RESULT_OK) {
                changeMatchState(MatchState.STARTED);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Waiting room was dismissed with the back button. The meaning of this
                // action is up to the game. You may choose to leave the room and cancel the
                // match, or do something else like minimize the waiting room and
                // continue to connect in the background.
                changeMatchState(MatchState.ABORTED);
            } else if (resultCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
                // player wants to leave the room.
                changeMatchState(MatchState.ABORTED);
            }
        }
    }

    private GoogleSignInOptions buildSignInOptions() {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                .requestProfile()
                .build();
    }

    private void signInSilently() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(activity, buildSignInOptions());
        signInClient.silentSignIn().addOnCompleteListener(activity,
                task -> {
                    if (task.isSuccessful())
                        loggedIn();
                    else
                        startSignInIntent();
                });
    }

    private void startSignInIntent() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(activity, buildSignInOptions());
        Intent intent = signInClient.getSignInIntent();
        activity.startActivityForResult(intent, RC_SIGN_IN);
    }

    private void showWaitingRoom(Room room, int maxPlayersToStartGame) {
        Games.getRealTimeMultiplayerClient(application, account)
                .getWaitingRoomIntent(room, maxPlayersToStartGame)
                .addOnSuccessListener(intent -> activity.startActivityForResult(intent, RC_WAITING_ROOM));
    }

    public void startQuickGame() {
        if (mJoinedRoomConfig != null)
            throw new IllegalStateException("There is already a game started");

        // auto-match criteria to invite one random automatch opponent.
        // You can also specify more opponents (up to 3).
        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(1, 1, 0);

        // build the room config:
        RoomConfig roomConfig =
                RoomConfig.builder(mRoomUpdateCallback)
                        .setOnMessageReceivedListener(mMessageReceivedHandler)
                        .setRoomStatusUpdateCallback(mRoomStatusCallbackHandler)
                        .setAutoMatchCriteria(autoMatchCriteria)
                        .build();

        // Save the roomConfig so we can use it if we call leave().
        mJoinedRoomConfig = roomConfig;

        // create room:
        Games.getRealTimeMultiplayerClient(application, account)
                .create(roomConfig);
    }

    void sendToAllReliably(byte[] message) {
        for (String participantId : mRoom.getParticipantIds()) {
            if (!participantId.equals(mMyParticipantId)) {
                Task<Integer> task = Games.
                        getRealTimeMultiplayerClient(application, account)
                        .sendReliableMessage(message, mRoom.getRoomId(), participantId,
                                handleMessageSentCallback).addOnCompleteListener(task1 -> {
                            // Keep track of which messages are sent, if desired.
                            //     recordMessageToken(task.getResult());
                        });
            }
        }
    }

    private RealTimeMultiplayerClient.ReliableMessageSentCallback handleMessageSentCallback =
            new RealTimeMultiplayerClient.ReliableMessageSentCallback() {
                @Override
                public void onRealTimeMessageSent(int statusCode, int tokenId, String recipientId) {
                    // handle the message being sent.
                    synchronized (this) {
                        //  pendingMessageSet.remove(tokenId);
                        Log.d(TAG, "[SENDING PACKET]");
                    }
                }
            };

    private OnRealTimeMessageReceivedListener mMessageReceivedHandler =
            realTimeMessage -> {
                // Handle messages received here.
                byte[] message = realTimeMessage.getMessageData();

                if (message[0] == (byte)'T') {
                   //toastManager.show("treasure is clicked!");
                } else if (message[0] == (byte)'D') {
                   // toastManager.show("doors is clicked!");
                }
            };

    private boolean mWaitingRoomFinishedFromCode = false;

/*    private void onStartGameMessageReceived() {
        mWaitingRoomFinishedFromCode = true;
        finishActivity(RC_WAITING_ROOM);
    }
*/
    private RoomUpdateCallback mRoomUpdateCallback = new RoomUpdateCallback() {
        @Override
        public void onRoomCreated(int code, @Nullable Room room) {
            // Update UI and internal state based on room updates.
            if (code == GamesCallbackStatusCodes.OK && room != null) {
                Log.d(TAG, "Room " + room.getRoomId() + " created.");
                showWaitingRoom(room, MIN_PLAYERS);
            } else {
                Log.w(TAG, "Error creating room: " + code);
                changeMatchState(MatchState.ABORTED);
            }
        }

        @Override
        public void onJoinedRoom(int code, @Nullable Room room) {
            // Update UI and internal state based on room updates.
            if (code == GamesCallbackStatusCodes.OK && room != null) {
                Log.d(TAG, "Room " + room.getRoomId() + " joined.");
            } else {
                Log.w(TAG, "Error joining room: " + code);
                changeMatchState(MatchState.ABORTED);
            }
        }

        @Override
        public void onLeftRoom(int code, @NonNull String roomId) {
            Log.d(TAG, "Left room" + roomId);
        }

        @Override
        public void onRoomConnected(int code, @Nullable Room room) {
            if (code == GamesCallbackStatusCodes.OK && room != null) {
                Log.d(TAG, "Room " + room.getRoomId() + " connected.");
            } else {
                Log.w(TAG, "Error connecting to room: " + code);
                changeMatchState(MatchState.ABORTED);
            }
        }
    };

    // are we already playing?
    boolean mPlaying = false;

    // at least 2 players required for our game
    final static int MIN_PLAYERS = 1;

    // returns whether there are enough players to start the game
    boolean shouldStartGame(Room room) {
        int connectedPlayers = 0;
        for (Participant p : room.getParticipants()) {
            if (p.isConnectedToRoom()) {
                ++connectedPlayers;
            }
        }
        return connectedPlayers >= MIN_PLAYERS;
    }

    // Returns whether the room is in a state where the game should be canceled.
    boolean shouldCancelGame(Room room) {
        // TODO: Your game-specific cancellation logic here. For example, you might decide to
        // cancel the game if enough people have declined the invitation or left the room.
        // You can check a participant's status with Participant.getStatus().
        // (Also, your UI should have a Cancel button that cancels the game too)
        return true;
    }

    private RoomStatusUpdateCallback mRoomStatusCallbackHandler = new RoomStatusUpdateCallback() {
        @Override
        public void onRoomConnecting(@Nullable Room room) {
            // Update the UI status since we are in the process of connecting to a specific room.
        }

        @Override
        public void onRoomAutoMatching(@Nullable Room room) {
            // Update the UI status since we are in the process of matching other players.
        }

        @Override
        public void onPeerInvitedToRoom(@Nullable Room room, @NonNull List<String> list) {
            // Update the UI status since we are in the process of matching other players.
        }

        @Override
        public void onPeerDeclined(@Nullable Room room, @NonNull List<String> list) {
            // Peer declined invitation, see if game should be canceled
            if (!mPlaying && shouldCancelGame(room))
                leaveRoom();
        }

        @Override
        public void onPeerJoined(@Nullable Room room, @NonNull List<String> list) {
            // Update UI status indicating new players have joined!
        }

        @Override
        public void onPeerLeft(@Nullable Room room, @NonNull List<String> list) {
            // Peer left, see if game should be canceled.
            if (!mPlaying && shouldCancelGame(room))
                leaveRoom();
        }

        @Override
        public void onConnectedToRoom(@Nullable Room room) {
            // Connected to room, record the room Id.
            mRoom = room;
            Games.getPlayersClient(application, GoogleSignIn.getLastSignedInAccount(application))
                    .getCurrentPlayerId().addOnSuccessListener(playerId -> {
                mMyParticipantId = mRoom.getParticipantId(playerId);
            });
        }

        @Override
        public void onDisconnectedFromRoom(@Nullable Room room) {
            // This usually happens due to a network error, leave the game
            // show error message and return to main screen
            changeMatchState(MatchState.DISCONNECTED);
        }

        @Override
        public void onPeersConnected(@Nullable Room room, @NonNull List<String> list) {
            if (mPlaying) {
                // add new player to an ongoing game
            } else if (shouldStartGame(room)) {
                // start game!
            }
        }

        @Override
        public void onPeersDisconnected(@Nullable Room room, @NonNull List<String> list) {
            if (mPlaying) {
                // do game-specific handling of this -- remove player's avatar
                // from the screen, etc. If not enough players are left for
                // the game to go on, end the game and leave the room.
            } else if (shouldCancelGame(room)) {
                // cancel the game
                changeMatchState(MatchState.ABORTED);
            }
        }

        @Override
        public void onP2PConnected(@NonNull String participantId) {
            // Update status due to new peer to peer connection.
        }

        @Override
        public void onP2PDisconnected(@NonNull String participantId) {
            // Update status due to  peer to peer connection being disconnected.
        }
    };

    private void leaveRoom() {
        if (mRoom != null && mJoinedRoomConfig != null) {
            Games.getRealTimeMultiplayerClient(application, account)
                    .leave(mJoinedRoomConfig, mRoom.getRoomId());
       //     getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            mRoom = null;
            mJoinedRoomConfig = null;
        }
    }
}
