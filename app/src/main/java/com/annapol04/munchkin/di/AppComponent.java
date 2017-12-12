package com.annapol04.munchkin.di;

import android.app.Application;

import com.annapol04.munchkin.gui.GameDetailActivity;
import com.annapol04.munchkin.gui.HighscoreActivity;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class
})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        Builder appModule(AppModule appModule);

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(HighscoreActivity activity);
    void inject(GameDetailActivity activity);
}