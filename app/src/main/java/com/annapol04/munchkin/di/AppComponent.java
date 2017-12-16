package com.annapol04.munchkin.di;

import android.app.Application;

import com.annapol04.munchkin.App;
import com.annapol04.munchkin.gui.PlayDeskActivity;
import com.annapol04.munchkin.gui.SignInActivity;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        SignInActivityModule.class,
        MainActivityModule.class,
        PlayDeskActivityModule.class,
})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        Builder appModule(AppModule appModule);

        AppComponent build();
    }

    void inject(App app);
}