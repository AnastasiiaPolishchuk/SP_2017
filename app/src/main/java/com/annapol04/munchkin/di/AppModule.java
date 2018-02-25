package com.annapol04.munchkin.di;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.util.Log;

import com.annapol04.munchkin.data.AndroidMessageBook;
import com.annapol04.munchkin.data.EventRepository;
import com.annapol04.munchkin.db.AppDb;
import com.annapol04.munchkin.db.EventDao;
import com.annapol04.munchkin.engine.Card;
import com.annapol04.munchkin.engine.Game;
import com.annapol04.munchkin.engine.Match;
import com.annapol04.munchkin.engine.FakeMatch;
import com.annapol04.munchkin.engine.MessageBook;
import com.annapol04.munchkin.engine.PlayClient;
import com.annapol04.munchkin.engine.Player;
import com.annapol04.munchkin.network.GooglePlayClient;
import com.annapol04.munchkin.network.PlayClientDummy;
import com.google.android.gms.auth.api.signin.GoogleSignIn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {
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

    @Singleton
    @Provides
    public PlayClient providePlayClient(Application app) {
        return new GooglePlayClient(app);
    }

    @Singleton
    @Provides
    @Named("myself")
    public Player provideMyself(Application app, Game game, EventRepository repository) {
        Player p = new Player(new Random().nextInt(), game, repository);
        p.rename(GoogleSignIn.getLastSignedInAccount(app).getDisplayName());
        Log.d(AppModule.class.getSimpleName(), "rename myself to " + p.getName());
        return p;
    }
/*
    @Singleton
    @Provides
    public PlayClient providePlayClient() {
        return new PlayClientDummy();
    }

    @Singleton
    @Provides
    @Named("myself")
    public Player provideMyself(Application app, Game game, EventRepository repository) {
        Player p = new Player(Integer.MAX_VALUE, game, repository);
        p.rename("Marvin");
        return p;
    }

    @Singleton
    @Provides
    public Match providesMatch(Game game, @Named("myself") Player player, EventRepository repository) {
        return new FakeMatch(game, player, repository);
    }
*/
    @Provides
    public MessageBook providesMessageBook(Application application) {
        return new AndroidMessageBook(application);
    }
}
