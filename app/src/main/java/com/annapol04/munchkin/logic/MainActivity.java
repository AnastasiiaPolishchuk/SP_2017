package com.annapol04.munchkin.logic;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Falco on 26.10.2017.
 */
public class MainActivity extends AppCompatActivity {

    //TextView console = (TextView)findViewById(R.id.console);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //console.setText("My Awesome Text");
    }
}
