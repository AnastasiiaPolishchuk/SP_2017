package com.annapol04.munchkin.di;

import com.annapol04.munchkin.App;
import com.annapol04.munchkin.engine.PlayClient;

import javax.inject.Singleton;

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

        AppComponent build();
    }

    void inject(App app);
}