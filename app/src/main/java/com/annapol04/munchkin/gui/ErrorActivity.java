package com.annapol04.munchkin.gui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.annapol04.munchkin.R;

public class ErrorActivity extends AppCompatActivity {
    public static final String EXTRA_STACK_TRACE = "EXTRA_STACK_TRACE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        TextView text = findViewById(R.id.text_error);
        text.setText(getIntent().getStringExtra(EXTRA_STACK_TRACE));
    }

    public void onErrorDismissClicked(View view) {
        finish();
    }
}
