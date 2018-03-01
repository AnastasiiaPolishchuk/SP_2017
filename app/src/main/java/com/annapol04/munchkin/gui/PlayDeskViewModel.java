package com.annapol04.munchkin.gui;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.annapol04.munchkin.data.EventRepository;
import com.annapol04.munchkin.engine.Action;
import com.annapol04.munchkin.engine.Card;
import com.annapol04.munchkin.engine.Event;
import com.annapol04.munchkin.engine.Executor;
import com.annapol04.munchkin.engine.Game;
import com.annapol04.munchkin.engine.Match;
import com.annapol04.munchkin.engine.Player;
import com.annapol04.munchkin.engine.PlayClient;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class PlayDeskViewModel extends AndroidViewModel implements PlayClient.OnMatchStateChangedListener {
    private static final String TAG = PlayDeskViewModel.class.getName();

    private LiveData<String> playerName;
    private LiveData<Integer> playerLevel;
    private LiveData<List<Card>> playerHand;
    private LiveData<List<Card>> playedCards;

    private LiveData<Boolean> canPlayBigEquipment;
    private LiveData<Boolean> canPlayHeadgeer;
    private LiveData<Boolean> canPlayArmor;
    private LiveData<Boolean> canPlayShoes;
    private LiveData<Boolean> canPlayOneHander;
    private LiveData<Boolean> canPlayTwoHander;

    private LiveData<Boolean> isHeadgearEquiped;
    private LiveData<Boolean> isArmorEquiped;
    private LiveData<Boolean> areShoesEquiped;
    private LiveData<Boolean> isRightHandEquiped;
    private LiveData<Boolean> isLeftHandEquiped;

    private LiveData<Boolean> isMyself;
    private MutableLiveData<Boolean> isStarted = new MutableLiveData<>();

    private boolean isStartingAlready = false;
    private Player myself;
    private MutableLiveData<Player> visiblePlayer = new MutableLiveData<>();
    private PlayClient client;
    private Game game;
    private Match match;
    private EventRepository eventRepository;
    private Executor executor;

    @Inject
    public PlayDeskViewModel(@NonNull Application application,
                             @Named("myself") Player myself,
                             PlayClient client,
                             Match match,
                             Game game,
                             EventRepository eventRepository,
                             Executor executor) {
        super(application);
        this.myself = myself;

        this.client = client;
        this.game = game;
        this.match = match;
        this.eventRepository = eventRepository;
        this.executor = executor;

        client.setMatchStateChangedListener(this);
        isStarted.setValue(false);

        visiblePlayer.setValue(myself);

        isMyself = Transformations.map(visiblePlayer, player -> {
            Log.d("Executor", " " + player + " == " + myself); return player == myself;
        });
        playerName = Transformations.map(visiblePlayer, Player::getName);
        playerLevel = Transformations.switchMap(visiblePlayer, Player::getLevel);
        playerHand = Transformations.switchMap(visiblePlayer, Player::getHandCards);
        playedCards = Transformations.switchMap(visiblePlayer, Player::getPlayedCards);

        canPlayBigEquipment = Transformations.switchMap(visiblePlayer, Player::getCanPlayBigEquipment);
        canPlayHeadgeer = Transformations.switchMap(visiblePlayer, Player::getCanPlayHeadgeer);
        canPlayArmor = Transformations.switchMap(visiblePlayer, Player::getCanPlayArmor);
        canPlayShoes = Transformations.switchMap(visiblePlayer, Player::getCanPlayShoes);
        canPlayOneHander = Transformations.switchMap(visiblePlayer, Player::getCanPlayOneHander);
        canPlayTwoHander = Transformations.switchMap(visiblePlayer, Player::getCanPlayTwoHander);

        isHeadgearEquiped = Transformations.switchMap(visiblePlayer, Player::getIsHeadgearEquiped);
        isArmorEquiped = Transformations.switchMap(visiblePlayer, Player::getIsArmorEquiped);
        areShoesEquiped = Transformations.switchMap(visiblePlayer, Player::getAreShoesEquiped);
        isLeftHandEquiped = Transformations.switchMap(visiblePlayer, Player::getIsLeftHandEquiped);
        isRightHandEquiped = Transformations.switchMap(visiblePlayer, Player::getIsRightHandEquiped);
    }

    @Override
    protected void onCleared() {
        client.setMatchStateChangedListener(null);
        super.onCleared();
    }

    public void processActivityResults(int requestCode, int resultCode, Intent data) {
        client.processActivityResults(requestCode, resultCode, data);
    }

    public LiveData<Boolean> getIsHeadgearEquiped() {
        return isHeadgearEquiped;
    }

    public LiveData<Boolean> getIsArmorEquiped() {
        return isArmorEquiped;
    }

    public LiveData<Boolean> getAreShoesEquiped() {
        return areShoesEquiped;
    }

    public LiveData<Boolean> getIsRightHandEquiped() {
        return isRightHandEquiped;
    }

    public LiveData<Boolean> getIsLeftHandEquiped() {
        return isLeftHandEquiped;
    }

    public LiveData<String> getLog() {
        return match.getLog();
    }

    public Player getPlayer(int position){
        return match.getPlayer(position);
    }

    public LiveData<Boolean> isMyself() {
        return isMyself;
    }

    public LiveData<String> getPlayerName() {
        return playerName;
    }

    public LiveData<Integer> getPlayerLevel() {
        return playerLevel;
    }

    public LiveData<List<Card>> getPlayerHand() {
        return playerHand;
    }

    public LiveData<List<Card>> getPlayedCards() {
        return playedCards;
    }

    public LiveData<List<Card>> getPlayedCards(int playerNr) {
        return getPlayer(playerNr).getPlayedCards();
    }

    public LiveData<List<Card>> getDeskCards() {
        return game.getDeskCards();
    }

    public LiveData<Boolean> getGameStarted() {
        return isStarted;
    }

    public LiveData<Boolean> getGameFinished() { return game.getGameFinished(); }

    public LiveData<List<Player>> getPlayers(){
        return match.getPlayers();
    }

    public LiveData<String> getMyName() {
        MutableLiveData<String> myName = new MutableLiveData<>();
        myName.setValue(myself.getName());
        return myName;
    }

    public LiveData<Integer> getMyLevel() {
        return myself.getLevel();
    }

    public LiveData<Boolean> isMyLevelGreater(){
        // vergleichen MyLevel+ bonus mit Monster Level

        MutableLiveData<Boolean> isGreater = new MutableLiveData<>();
         isGreater.setValue(true);
         return isGreater;
    }


    public void quitGame() {
    //    throw new UnsupportedOperationException("Not implemented");
    }

    public void resume(Activity fromActivity) {
        client.setActivity(fromActivity);

        if (!client.isLoggedIn())
            client.login();
        else
            startGameIfPossible();
    }

    public void pause() {
        client.setActivity(null);
    }

    private void startGameIfPossible() {
        if (!getGameStarted().getValue() && !isStartingAlready) {
            isStartingAlready = true;

            client.startQuickGame();
        }
    }

    @Override
    public void onMatchStateChanged(PlayClient.MatchState state) {
        switch (state) {
            case LOGGED_IN:
                startGameIfPossible();
                break;
            case STARTED:
                isStarted.setValue(true);
                match.start(client.getAmountOfPlayers());
                isStartingAlready = false;
                break;
            case ABORTED:
                isStarted.setValue(false);
                isStartingAlready = false;
                break;
            case DISCONNECTED:
                isStarted.setValue(false);
                isStartingAlready = false;
                break;
        }
    }

    public void drawDoorCard() {
        match.emitDrawDoorCard(visiblePlayer.getValue().getScope());
    }

    public void drawTreasureCard() {
        visiblePlayer.getValue().emitDrawTreasureCard();
    }

    public void playCard(Card card) {
        match.emitPlayCard(visiblePlayer.getValue().getScope(), card);
    }

    public void pickupCard(Card card) {
        visiblePlayer.getValue().emitPickupCard(card);
    }

    public void runAwayFromMonster() {
        match.emitRunAway(visiblePlayer.getValue().getScope());
    }

    public void fightMonster() {
        match.emitFightMonster(visiblePlayer.getValue().getScope());
    }

    public void moveToPlayDesk(Card selected) {
        //hier wird die Karte aus der Hand auf PlayDesk verschoben
    }

    public void displayPlayer(int id) {
        visiblePlayer.setValue(getPlayer(id));
    }
}
