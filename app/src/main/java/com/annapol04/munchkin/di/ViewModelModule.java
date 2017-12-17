package com.annapol04.munchkin.di;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.annapol04.munchkin.gui.PlayDeskViewModel;
import com.annapol04.munchkin.gui.SignInActivity;
import com.annapol04.munchkin.gui.SignInViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel.class)
    abstract ViewModel bindSignInViewModel(SignInViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PlayDeskViewModel.class)
    abstract ViewModel bindPlayDeskViewModel(PlayDeskViewModel viewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}

