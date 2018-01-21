package com.annapol04.munchkin.data;

import android.app.Application;

import com.annapol04.munchkin.engine.MessageBook;

import javax.inject.Inject;

public class AndroidMessageBook extends MessageBook {
    private Application application;

    @Inject
    public AndroidMessageBook(Application application) {
        this.application = application;
    }

    @Override
    public String find(int id) {
        return application.getString(id);
    }
}
