package com.annapol04.munchkin;

import android.app.Application;

import com.annapol04.munchkin.di.AppComponent;
import com.annapol04.munchkin.di.DaggerAppComponent;

public class MunchkinApp extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                // list of modules that are part of this component need to be created here too
                .application(this) // This also corresponds to the name of your module: %component_name%Module
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
