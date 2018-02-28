package com.annapol04.munchkin.di;

import android.app.Application;

import com.annapol04.munchkin.data.EventRepository;
import com.annapol04.munchkin.engine.Game;
import com.annapol04.munchkin.engine.Match;
import com.annapol04.munchkin.engine.FakeMatch;
import com.annapol04.munchkin.engine.PlayClient;
import com.annapol04.munchkin.engine.Player;
import com.annapol04.munchkin.network.PlayClientDummy;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModuleFlavor {

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
}
