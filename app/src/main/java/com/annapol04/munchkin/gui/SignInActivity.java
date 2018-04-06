package com.annapol04.munchkin.gui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.annapol04.munchkin.R;
import com.annapol04.munchkin.di.ViewModelFactory;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class SignInActivity extends AppCompatActivity {


    @Inject
    ViewModelFactory viewModelFactory;
    private SignInViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SignInViewModel.class);

        viewModel.getLoggedIn().observe(this, isLoggedIn -> {
            if (isLoggedIn != null && isLoggedIn)
                startMainActivity();
        });
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();

        viewModel.login(this);
    }

    @Override
    protected void onPause() {
    //    viewModel.logout();

        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        viewModel.processActivityResults(requestCode, resultCode, data);
    }
}
