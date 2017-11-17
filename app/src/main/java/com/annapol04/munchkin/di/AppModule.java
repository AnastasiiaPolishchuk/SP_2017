package com.annapol04.munchkin;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.annapol04.munchkin.db.AppDb;
import com.annapol04.munchkin.data.HighscoreEntryDao;
import com.annapol04.munchkin.gui.ViewModelModule;
import com.annapol04.munchkin.network.Webservice;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class AppModule {
    @Singleton @Provides
    Webservice provideWebservice() {
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Webservice.class);
    }

    @Singleton
    @Provides
    AppDb provideDb(Application app) {
        return Room.databaseBuilder(app, AppDb.class,"app.db").build();
    }

    @Singleton
    @Provides
    HighscoreEntryDao provideHighscoreDoa(AppDb db) {
        return db.highscoreEntryDao();
    }
}
