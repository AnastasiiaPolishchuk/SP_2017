package com.annapol04.munchkin.di;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.util.Log;

import com.annapol04.munchkin.data.EventRepository;
import com.annapol04.munchkin.db.AppDb;
import com.annapol04.munchkin.db.EventDao;
import com.annapol04.munchkin.engine.Card;
import com.annapol04.munchkin.engine.Game;
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

@Module
public class AppModuleFlavor {

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
}
