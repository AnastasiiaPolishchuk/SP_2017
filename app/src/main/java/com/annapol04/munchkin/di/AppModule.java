package com.annapol04.munchkin.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.annapol04.munchkin.data.EventRepository;
import com.annapol04.munchkin.db.AppDb;
import com.annapol04.munchkin.db.GameDetailsDao;
import com.annapol04.munchkin.db.HighscoreEntryDao;
import com.annapol04.munchkin.engine.Executor;
import com.annapol04.munchkin.engine.Game;
import com.annapol04.munchkin.engine.Player;
import com.annapol04.munchkin.network.Webservice;
import com.google.android.gms.auth.api.signin.GoogleSignIn;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
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
    public Webservice provideWebservice() {
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Webservice.class);
    }

    @Singleton
    @Provides
    public AppDb provideDb(Application app) {
        return Room.databaseBuilder(app, AppDb.class, "munchkin_app.db").build();
    }

    @Singleton
    @Provides
    public HighscoreEntryDao provideHighscoreDoa(AppDb db) {
        return db.highscoreEntryDao();
    }

    @Singleton
    @Provides
    public GameDetailsDao provideGameDetailsDao(AppDb db) {
        return db.gameDetailsDao();
    }

    @Singleton
    @Provides
    public EventRepository eventRepository() {
        return new EventRepository();
    }

    @Singleton
    @Provides
    public Executor provideEventExecutor(Game game, EventRepository repository) {
        return new Executor(game, repository);
    }

    @Singleton
    @Provides
    @Named("myself")
    public Player provideMyself(Application app) {
        return new Player(GoogleSignIn.getLastSignedInAccount(app).getDisplayName());
    }
}
