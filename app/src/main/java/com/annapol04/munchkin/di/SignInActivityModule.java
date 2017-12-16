package com.annapol04.munchkin.di;

import android.app.Activity;

import com.annapol04.munchkin.gui.SignInActivity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(subcomponents = SignInActivitySubcomponent.class)
abstract class SignInActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(SignInActivity.class)
    abstract AndroidInjector.Factory<? extends Activity>
    bindYourActivityInjectorFactory(SignInActivitySubcomponent.Builder builder);
}