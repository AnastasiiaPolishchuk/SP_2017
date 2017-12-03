package com.annapol04.munchkin.gui;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.annapol04.munchkin.R;
import com.annapol04.munchkin.engine.Card;
import com.annapol04.munchkin.engine.CardDeck;
import com.annapol04.munchkin.engine.Game;
import com.annapol04.munchkin.engine.Player;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesCallbackStatusCodes;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.OnRealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateCallback;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.annapol04.munchkin.R.layout.activity_play_desk;


public class PlayDeskActivity extends AppCompatActivity {
    private final static String TAG = PlayDeskActivity.class.getSimpleName();
    private static final int RC_WAITING_ROOM = 9007;

    List<Player> playerList;
    private Game game;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private View mRootView;

    private GoogleSignInAccount mAccount;
    private RoomConfig mJoinedRoomConfig;
    private String mMyParticipantId;

    private ToastManager toastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_play_desk);

        mRootView = findViewById(R.id.root_view);

        mAccount = GoogleSignIn.getLastSignedInAccount(this);

        toastManager = new ToastManager(this);

        ArrayList<String> playerNames = new ArrayList<>(1);
        playerNames.add(mAccount.getDisplayName());

        game = new Game(playerNames.stream()
                .map(playerName -> new Player(playerName, 1))
                .collect(Collectors.toList()));

        playerList = game.getPlayers();
        CardDeck deck = new CardDeck();

        game.getDeck().schuffleCards();
        game.initializePlayersHand(game.getDeck());


        showForCurrentPlayer(game.getCurrentPlayer());

        startQuickGame();
    }

    @Override
    protected void onPause() {
        toastManager.pause();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        toastManager.resume(this);
    }

    private void showWaitingRoom(Room room, int maxPlayersToStartGame) {
        Games.getRealTimeMultiplayerClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .getWaitingRoomIntent(room, maxPlayersToStartGame)
                .addOnSuccessListener(intent -> startActivityForResult(intent, RC_WAITING_ROOM));
    }


    boolean mWaitingRoomFinishedFromCode = false;

    private void onStartGameMessageReceived() {
        mWaitingRoomFinishedFromCode = true;
        finishActivity(RC_WAITING_ROOM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_WAITING_ROOM) {

            // Look for finishing the waiting room from code, for example if a
            // "start game" message is received.  In this case, ignore the result.
            if (mWaitingRoomFinishedFromCode) {
                return;
            }

            if (resultCode == Activity.RESULT_OK) {
                // Start the game
                mRootView.setVisibility(View.VISIBLE);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Waiting room was dismissed with the back button. The meaning of this
                // action is up to the game. You may choose to leave the room and cancel the
                // match, or do something else like minimize the waiting room and
                // continue to connect in the background.

                // in this example, we take the simple approach and just leave the room:
                leaveRoom();

                finish();
            } else if (resultCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
                // player wants to leave the room.
                leaveRoom();

                finish();
            }
        }
    }

    private RoomUpdateCallback mRoomUpdateCallback = new RoomUpdateCallback() {
        @Override
        public void onRoomCreated(int code, @Nullable Room room) {
            // Update UI and internal state based on room updates.
            if (code == GamesCallbackStatusCodes.OK && room != null) {
                Log.d(TAG, "Room " + room.getRoomId() + " created.");
                showWaitingRoom(room, MIN_PLAYERS);
            } else {
                Log.w(TAG, "Error creating room: " + code);
                // let screen go to sleep
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            }
        }

        @Override
        public void onJoinedRoom(int code, @Nullable Room room) {
            // Update UI and internal state based on room updates.
            if (code == GamesCallbackStatusCodes.OK && room != null) {
                Log.d(TAG, "Room " + room.getRoomId() + " joined.");
            } else {
                Log.w(TAG, "Error joining room: " + code);
                // let screen go to sleep
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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
                // let screen go to sleep
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            }
        }
    };

    // are we already playing?
    boolean mPlaying = false;

    // at least 2 players required for our game
    final static int MIN_PLAYERS = 2;

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

    private Activity thisActivity = this;
    private Room mRoom;
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
            Games.getPlayersClient(thisActivity, GoogleSignIn.getLastSignedInAccount(thisActivity))
                    .getCurrentPlayerId().addOnSuccessListener(playerId -> {
                mMyParticipantId = mRoom.getParticipantId(playerId);
            });
        }

        @Override
        public void onDisconnectedFromRoom(@Nullable Room room) {
            // This usually happens due to a network error, leave the game.
            leaveRoom();
            // show error message and return to main screen

            finish();
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
                leaveRoom();
                finish();
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

    void sendToAllReliably(byte[] message) {
        for (String participantId : mRoom.getParticipantIds()) {
            if (!participantId.equals(mMyParticipantId)) {
                Task<Integer> task = Games.
                        getRealTimeMultiplayerClient(this, GoogleSignIn.getLastSignedInAccount(this))
                        .sendReliableMessage(message, mRoom.getRoomId(), participantId,
                                handleMessageSentCallback).addOnCompleteListener(task1 -> {
                                    // Keep track of which messages are sent, if desired.
                               //     recordMessageToken(task.getResult());
                                });
            }
        }
    }

    private void startQuickGame() {
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

        // prevent screen from sleeping during handshake
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Save the roomConfig so we can use it if we call leave().
        mJoinedRoomConfig = roomConfig;

        // create room:
        Games.getRealTimeMultiplayerClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .create(roomConfig);
    }

    private void showForCurrentPlayer(Player player) {
        TextView nameOfPlayer = (TextView) findViewById(R.id.name_of_player);
        nameOfPlayer.setText(player.getName());

        TextView levelOfPlayer = (TextView) findViewById(R.id.level_of_player);
        levelOfPlayer.setText("Level : " + player.getLevel());


        RecyclerView handOfPlayer = (RecyclerView) findViewById(R.id.recycler_view_hand_of_player);
        RecyclerView.LayoutManager handLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        handOfPlayer.setLayoutManager(handLayoutManager);
        CardAdapter handAdapter = new CardAdapter(game.getCurrentPlayer().getHand());
        handOfPlayer.setAdapter(handAdapter);

        mRecyclerView = (RecyclerView) findViewById((R.id.recycler_view_cards));
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CardAdapter(game.getCurrentPlayer().getHand());
        mRecyclerView.setAdapter(mAdapter);

        List<Card> desk = new ArrayList<>();
        RecyclerView playDesk = (RecyclerView) findViewById(R.id.recycler_view_desk);
        RecyclerView.LayoutManager deskLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        playDesk.setLayoutManager(deskLayoutManager);
        desk.add((Card) game.getDeck().getDoorCardDeck().getFirst());
        desk.add((Card) game.getDeck().getDoorCardDeck().getLast());

        CardAdapter.MyDeskAdapter deskAdapter = new CardAdapter.MyDeskAdapter(desk);
        playDesk.setAdapter(deskAdapter);


        addListenerOnButton();

    }

    public void showNextPlayer(View view) {

        showForCurrentPlayer(game.getNextPlayer());
    }

    private OnRealTimeMessageReceivedListener mMessageReceivedHandler =
            new OnRealTimeMessageReceivedListener() {
                @Override
                public void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage) {
                    // Handle messages received here.
                    byte[] message = realTimeMessage.getMessageData();

                    if (message[0] == (byte)'T') {
                        toastManager.show("treasure is clicked!");
                    } else if (message[0] == (byte)'D') {
                        toastManager.show("doors is clicked!");
                    }
                }
            };

    public void addListenerOnButton() {
        ImageButton doorButton = (ImageButton) findViewById(R.id.deck_doors);
        doorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToAllReliably(new byte[]{(byte)'D'});
                toastManager.show( "doors is clicked!");
            }
        });

        ImageButton treasureButton = (ImageButton) findViewById(R.id.deck_treasure);
        treasureButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToAllReliably(new byte[]{(byte)'T'});
                toastManager.show("treasure is clicked!");
            }
        }));

        ImageButton exitButton = (ImageButton) findViewById(R.id.munchkin_exit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveRoom();
                finish();
            }
        });

        ImageButton infoButton = (ImageButton) findViewById(R.id.munchkin_info);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastManager.show("hier wird INFO!");
            }
        });
    }

    private void leaveRoom() {
        if (mRoom != null && mJoinedRoomConfig != null) {
            Games.getRealTimeMultiplayerClient(thisActivity,
                    GoogleSignIn.getLastSignedInAccount(thisActivity))
                    .leave(mJoinedRoomConfig, mRoom.getRoomId());
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            mRoom = null;
            mJoinedRoomConfig = null;
        }
    }

    @Override
    public void onBackPressed() {
        leaveRoom();

        super.onBackPressed();
    }
}
