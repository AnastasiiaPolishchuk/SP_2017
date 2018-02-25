package com.annapol04.munchkin.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.annapol04.munchkin.data.AndroidMessageBook;
import com.annapol04.munchkin.data.EventRepository;
import com.annapol04.munchkin.db.AppDb;
import com.annapol04.munchkin.db.EventDao;
import com.annapol04.munchkin.engine.FakeMatch;
import com.annapol04.munchkin.engine.Game;
import com.annapol04.munchkin.engine.Match;
import com.annapol04.munchkin.engine.MessageBook;
import com.annapol04.munchkin.engine.PlayClient;
import com.annapol04.munchkin.engine.Player;
import com.annapol04.munchkin.network.PlayClientDummy;

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
        return Room.databaseBuilder(app, AppDb.class, "munchkin_app.db").build();
    }

    @Singleton
    @Provides
    public EventDao provideEventDao(AppDb db) {
        return db.eventDao();
    }

    @Provides
    public MessageBook providesMessageBook(Application application) {
        return new AndroidMessageBook(application);
    }
}
