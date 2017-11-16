package com.annapol04.munchkin.gui;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.annapol04.munchkin.R;
import com.annapol04.munchkin.databinding.ActivityHighscoreBinding;

public class HighscoreActivity extends AppCompatActivity {
    private HighscoreViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHighscoreBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_highscore);
        viewModel = ViewModelProviders.of(this).get(HighscoreViewModel.class);

        binding.setVm(viewModel);

        viewModel.getEntries().observe(this, entries -> {
            // update UI
        });
    }
}
