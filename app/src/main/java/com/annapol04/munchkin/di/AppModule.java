package com.annapol04.munchkin.di;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;

import com.annapol04.munchkin.db.AppDb;
import com.annapol04.munchkin.db.EventDao;
import com.annapol04.munchkin.engine.BonusWear;
import com.annapol04.munchkin.engine.Deck;
import com.annapol04.munchkin.engine.DeckKt;
import com.annapol04.munchkin.engine.DoorCards;
import com.annapol04.munchkin.engine.Event;
import com.annapol04.munchkin.engine.MessageBook;
import com.annapol04.munchkin.engine.Monster;
import com.annapol04.munchkin.engine.TreasureCards;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {
        AppModuleFlavor.class,
        ViewModelModule.class,
        SignInActivityModule.class,
        MainActivityModule.class,
        PlayDeskActivityModule.class,
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
        return Room.databaseBuilder(app, AppDb.class, "app.db").build();
    }

    @Singleton
    @Provides
    public EventDao provideEventDao(AppDb db) {
        return db.eventDao();
    }

    @Singleton
    @Provides
    @Named("treasure")
    public Deck provideTreasureDeck() {
        String[] cardTypes = {BonusWear.class.getSimpleName() };
        return DeckKt.build(TreasureCards.class, Arrays.asList(cardTypes));
    }

    @Singleton
    @Provides
    @Named("door")
    public Deck provideDoorDeck() {
        String[] cardTypes = {Monster.class.getSimpleName()};
        return DeckKt.build(DoorCards.class, Arrays.asList(cardTypes));
    }
}
