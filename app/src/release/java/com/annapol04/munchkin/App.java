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

public class App extends AppBase implements Thread.UncaughtExceptionHandler {

    @Override
    public void onCreate() {
        super.onCreate();

        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace(System.err); // not all Android versions will print the stack trace automatically

        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        final String stackTrace = result.toString();

        Intent intent = new Intent();
        intent.setAction("com.annapol04.munchkin.SHOW_ERROR"); // see step 5.
        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application
        intent.putExtra(ErrorActivity.EXTRA_STACK_TRACE, stackTrace);
        startActivity (intent);

        System.exit(1); // kill off the crashed app
    }
}
