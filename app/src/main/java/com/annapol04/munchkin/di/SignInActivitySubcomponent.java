package com.annapol04.munchkin.di;

import com.annapol04.munchkin.gui.SignInActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent
public interface SignInActivitySubcomponent extends AndroidInjector<SignInActivity> {
    @Subcomponent.Builder
    public abstract class Builder extends AndroidInjector.Builder<SignInActivity> {}
}