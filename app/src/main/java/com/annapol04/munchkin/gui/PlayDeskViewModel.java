package com.annapol04.munchkin.gui;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.annapol04.munchkin.data.EventRepository;
import com.annapol04.munchkin.engine.Card;
import com.annapol04.munchkin.engine.Executor;
import com.annapol04.munchkin.engine.Desk;
import com.annapol04.munchkin.engine.Match;
import com.annapol04.munchkin.engine.MatchResult;
import com.annapol04.munchkin.engine.MessageBook;
import com.annapol04.munchkin.engine.Player;
import com.annapol04.munchkin.engine.PlayClient;
import com.annapol04.munchkin.util.NonNullLiveData;
import com.annapol04.munchkin.util.NonNullMutableLiveData;
import com.annapol04.munchkin.util.Transformations;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class PlayDeskViewModel extends AndroidViewModel implements PlayClient.OnMatchStateChangedListener {
    public interface OnAbortedListener {
        void onAborted();
    }

    private static final String TAG = PlayDeskViewModel.class.getName();

    private LiveData<String> playerName;
    private LiveData<Integer> playerLevel;
    private LiveData<Integer> playerFightLevel;
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

    private NonNullLiveData<Boolean> canStartCombat;
    private NonNullLiveData<Boolean> canFinishRound;
    private LiveData<Boolean> canDrawDoorCard;
    private LiveData<Boolean> canDrawTreasureCard;
    private LiveData<Boolean> canDropCard;

    private LiveData<MatchResult> matchResult;

    private LiveData<Boolean> isMyself;
    private MutableLiveData<Boolean> isStarted = new MutableLiveData<>();
    private MutableLiveData<Boolean> isMyRound = new MutableLiveData<>();

    private boolean isStartingAlready = false;
    private Player myself;
    private NonNullMutableLiveData<Player> visiblePlayer;
    private PlayClient client;
    private Desk desk;
    private Match match;
    private EventRepository eventRepository;
    private Executor executor;
    private MessageBook messageBook;

    private OnAbortedListener abortedListener = null;

    @Inject
    public PlayDeskViewModel(@NonNull Application application,
                             @Named("myself") Player myself,
                             PlayClient client,
                             Match match,
                             Desk desk,
                             EventRepository eventRepository,
                             Executor executor,
                             MessageBook messageBook) {
        super(application);
        this.myself = myself;

        this.client = client;
        this.desk = desk;
        this.match = match;
        this.eventRepository = eventRepository;
        this.executor = executor;
        this.messageBook = messageBook;

        client.setMatchStateChangedListener(this);
        isStarted.setValue(false);

        visiblePlayer = new NonNullMutableLiveData<>(myself);

        match.getCurrentPlayer().observeForever(player -> {
            isMyRound.setValue(player == myself);
        });

        isMyself = Transformations.map(visiblePlayer, player -> player == myself);
        playerName = Transformations.switchMap(visiblePlayer, Player::getName);
        playerLevel = Transformations.switchMap(visiblePlayer, Player::getLevel);
        playerFightLevel = Transformations.switchMap(visiblePlayer, Player::getFightLevel);
        playerHand = Transformations.switchMap(visiblePlayer, Player::getHandCards);
        playedCards = Transformations.switchMap(visiblePlayer, Player::getPlayedCards);

        canPlayBigEquipment = Transformations.switchMap(visiblePlayer, Player::getCanPlayBigEquipment);
        canPlayHeadgeer = Transformations.switchMap(visiblePlayer, Player::getCanPlayHeadgeer);
        canPlayArmor = Transformations.switchMap(visiblePlayer, Player::getCanPlayArmor);
        canPlayShoes = Transformations.switchMap(visiblePlayer, Player::getCanPlayShoes);
        canPlayOneHander = Transformations.switchMap(visiblePlayer, Player::getCanPlayOneHander);
        canPlayTwoHander = Transformations.switchMap(visiblePlayer, Player::getCanPlayTwoHander);
        canDrawDoorCard = Transformations.switchMap(visiblePlayer, Player::isAllowedToDrawDoorCard);
        canDrawTreasureCard = Transformations.switchMap(visiblePlayer, Player::isAllowedToDrawTreasureCard);
        canDropCard = Transformations.switchMap(visiblePlayer, Player::isAllowedToDropCard);

        canStartCombat = match.getCanStartCombat();
        canFinishRound = match.getCanFinishRound();

        matchResult = match.getResult();

        isHeadgearEquiped = Transformations.switchMap(visiblePlayer, Player::getIsHeadgearEquiped);
        isArmorEquiped = Transformations.switchMap(visiblePlayer, Player::getIsArmorEquiped);
        areShoesEquiped = Transformations.switchMap(visiblePlayer, Player::getAreShoesEquiped);
        isLeftHandEquiped = Transformations.switchMap(visiblePlayer, Player::getIsLeftHandEquiped);
        isRightHandEquiped = Transformations.switchMap(visiblePlayer, Player::getIsRightHandEquiped);
    }

    @Override
    protected void onCleared() {
        client.setMatchStateChangedListener(null);
        this.abortedListener = null;
        super.onCleared();
    }

    public void processActivityResults(int requestCode, int resultCode, Intent data) {
        client.processActivityResults(requestCode, resultCode, data);
    }

    public LiveData<Boolean> getIsMyRound() {
        return isMyRound;
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

    public LiveData<Integer> getPlayerFightLevel() {
        return playerFightLevel;
    }

    public LiveData<List<Card>> getPlayerHand() {
        return playerHand;
    }

    public LiveData<List<Card>> getPlayedCards() {
        return playedCards;
    }

    public LiveData<Boolean> getCanDrawDoorCard() {
        return canDrawDoorCard;
    }

    public LiveData<Boolean> getCanDrawTreasureCard() {
        return canDrawTreasureCard;
    }

    public LiveData<Boolean> getCanDropCard() {
        return canDropCard;
    }

    public NonNullLiveData<Boolean> getCanStartCombat() {
        return canStartCombat;
    }

    public NonNullLiveData<Boolean> getCanFinishRound() {
        return canFinishRound;
    }

    public LiveData<MatchResult> getMatchResult() {
        return matchResult;
    }

    public LiveData<List<Card>> getDeskCards() {
        return desk.getDeskCards();
    }

    public LiveData<Boolean> getGameStarted() {
        return isStarted;
    }

    public LiveData<List<Player>> getPlayers(){
        return match.getPlayers();
    }

    public LiveData<String> getMyName() {
        return myself.getName();
    }

    public LiveData<Integer> getMyLevel() {
        return myself.getLevel();
    }

    public boolean canFightMonster() {
        return match.canPlayerFightMonster(visiblePlayer.getValue());
    }

    public void quitGame() {
        match.reset();
        client.leaveGame();
    }

    public void resume(Activity fromActivity, OnAbortedListener abortedListener) {
        client.setActivity(fromActivity);
        this.abortedListener = abortedListener;

        if (!client.isLoggedIn())
            client.login();
        else
            startGameIfPossible();
    }

    public void pause() {
        client.setActivity(null);
    }

    public void reset() {
        match.reset();
    }

    private void startGameIfPossible() {
        if (!getGameStarted().getValue() && !isStartingAlready) {
            isStartingAlready = true;

            client.startQuickGame();
        }
    }

    @Override
    public void onMatchStateChanged(PlayClient.ClientState state) {
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

                if (abortedListener != null)
                    abortedListener.onAborted();
                break;
            case DISCONNECTED:
                match.stop(messageBook.find(client.getErrorReason()));
                isStarted.setValue(false);
                isStartingAlready = false;
                break;
        }
    }

    public void drawDoorCard() {
        match.emitDrawDoorCard(visiblePlayer.getValue().getScope());
    }

    public void drawTreasureCard() {
        match.emitDrawTreasureCard(visiblePlayer.getValue().getScope());
    }

    public void playCard(Card card) {
        match.emitPlayCard(visiblePlayer.getValue().getScope(), card);
    }

    public void dropCard(Card card) {
        match.emitDropCard(visiblePlayer.getValue().getScope(), card);
    }

    public void pickupCard(Card card) {
        visiblePlayer.getValue().emitPickupCard(card);
    }

    public void startCombat() {
        match.startCombat();
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

    public void finishRound() {
        match.finishRound();
    }
}
