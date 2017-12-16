package com.annapol04.munchkin.di;

import com.annapol04.munchkin.gui.PlayDeskActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent
public interface PlayDeskActivitySubcomponent extends AndroidInjector<PlayDeskActivity> {
    @Subcomponent.Builder
    public abstract class Builder extends AndroidInjector.Builder<PlayDeskActivity> {}
}