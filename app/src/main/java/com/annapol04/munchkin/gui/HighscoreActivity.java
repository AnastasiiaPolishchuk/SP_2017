package com.annapol04.munchkin.gui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.annapol04.munchkin.MunchkinApp;
import com.annapol04.munchkin.R;
import com.annapol04.munchkin.databinding.ActivityHighscoreBinding;

import javax.inject.Inject;

public class HighscoreActivity extends AppCompatActivity {
    private HighscoreViewModel viewModel;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHighscoreBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_highscore);
        ((MunchkinApp)getApplication()).getAppComponent().inject(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HighscoreViewModel.class);

        binding.setVm(viewModel);

        viewModel.initialize();

        // set up the RecyclerView
        HighscoreAdapter highscoreAdapter = new HighscoreAdapter(this);
        highscoreAdapter.setEntries(viewModel.getEntries().getValue());
        // highscoreAdapter.setClickListener(this);
        binding.highscoreList.setLayoutManager(new LinearLayoutManager(this));
        binding.highscoreList.setAdapter(highscoreAdapter);

        viewModel.getEntries().observe(this,
                entries -> highscoreAdapter.setEntries(viewModel.getEntries().getValue()));
    }
}
