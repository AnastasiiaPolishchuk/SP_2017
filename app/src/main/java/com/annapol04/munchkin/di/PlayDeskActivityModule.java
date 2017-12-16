package com.annapol04.munchkin.di;

import android.app.Activity;

import com.annapol04.munchkin.gui.PlayDeskActivity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(subcomponents = PlayDeskActivitySubcomponent.class)
abstract class PlayDeskActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(PlayDeskActivity.class)
    abstract AndroidInjector.Factory<? extends Activity>
    bindYourActivityInjectorFactory(PlayDeskActivitySubcomponent.Builder builder);
}