package com.annapol04.munchkin.di;

import android.app.Application;
import android.util.Log;

import com.annapol04.munchkin.data.EventRepository;
import com.annapol04.munchkin.engine.Desk;
import com.annapol04.munchkin.engine.PlayClient;
import com.annapol04.munchkin.engine.Player;
import com.annapol04.munchkin.network.GooglePlayClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;

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
    public Player provideMyself(Application app, Desk desk, EventRepository repository) {
        Player p = new Player(new Random().nextInt(), desk, repository);
        p.rename(GoogleSignIn.getLastSignedInAccount(app).getDisplayName());
        Log.d(AppModule.class.getSimpleName(), "rename myself to " + p.getName());
        return p;
    }
}
