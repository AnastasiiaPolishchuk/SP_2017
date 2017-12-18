package com.annapol04.munchkin.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.annapol04.munchkin.db.AppDb;
import com.annapol04.munchkin.db.EventDao;
import com.annapol04.munchkin.engine.Card;
import com.annapol04.munchkin.engine.Player;
import com.annapol04.munchkin.network.PlayClient;
import com.annapol04.munchkin.network.PlayClientDummy;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {
        ViewModelModule.class,
        SignInActivityModule.class,
        MainActivityModule.class,
        PlayDeskActivityModule.class
})
public class AppModule {
    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Singleton
    @Provides
    public Application provideApplication() {
        return application;
    }

    @Singleton
    @Provides
    public AppDb provideDb(Application app) {
        return Room.databaseBuilder(app, AppDb.class, "munchkin_app.db").build();
    }

    @Singleton
    @Provides
    public EventDao provideEventDao(AppDb db) {
        return db.eventDao();
    }

    @Singleton
    @Provides
    @Named("myself")
    public Player provideMyself(Application app) {
        return new Player("Anastasiia");//new Player(GoogleSignIn.getLastSignedInAccount(app).getDisplayName()));
    }

    @Singleton
    @Provides
    public PlayClient providesPlayClient(/*Application app*/) {
        return new PlayClientDummy();// new GooglePlayClient(app);
    }

    @Singleton
    @Provides
    @Named("doorDeck")
    public List<Card> providesDoorDeck() {
        ArrayList<Card> deck = new ArrayList<>();
        deck.add(Card.LEPERCHAUN);
        deck.add(Card.GELATINOUS_OCTAHEDRON);
        deck.add(Card.CRABS);
        deck.add(Card.BIGFOOT);
        deck.add(Card.GAZEBO);
        deck.add(Card.INSURANCE_SALESMAN);
        deck.add(Card.LAWYERS);
        deck.add(Card.ORCS);
        return deck;
    }

    @Singleton
    @Provides
    @Named("treasureDeck")
    public List<Card> providesTreasureDeck() {
        ArrayList<Card> deck = new ArrayList<>();
        deck.add(Card.TUBA_OF_CHARM);
        deck.add(Card.STAFF_OF_NAPALM);
        deck.add(Card.SNEAKY_BASTARDS_WORD);
        deck.add(Card.RAT_ON_A_STICK);
        deck.add(Card.POINTY_HAT_OF_POWER);
        deck.add(Card.PANTYHOSE_OF_GIANT_STRENGTH);
        deck.add(Card.HIRELING);
        deck.add(Card.HELM_OF_COURAGE);
        deck.add(Card.BROADSWORD);
        return deck;
    }
}
