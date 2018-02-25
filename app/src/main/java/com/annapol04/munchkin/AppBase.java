package com.annapol04.munchkin;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.annapol04.munchkin.di.AppModule;
import com.annapol04.munchkin.di.DaggerAppComponent;
import com.annapol04.munchkin.gui.ErrorActivity;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import javax.inject.Inject;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class AppBase extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingActivityInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build()
                .inject(this);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }
}
