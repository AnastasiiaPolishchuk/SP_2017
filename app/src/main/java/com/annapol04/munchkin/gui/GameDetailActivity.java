package com.annapol04.munchkin.gui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.annapol04.munchkin.MunchkinApp;
import com.annapol04.munchkin.R;
import com.annapol04.munchkin.databinding.ActivityGameDetailBinding;

import javax.inject.Inject;

public class GameDetailActivity extends AppCompatActivity {
    public static final String INTENT_ARG_GAME_ID = "GAME_ID";

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private GameDetailViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGameDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_game_detail);
        ((MunchkinApp) getApplication()).getAppComponent().inject(this);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameDetailViewModel.class);
    }
}
