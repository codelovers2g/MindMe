package com.mindmesolo.mindme.LoginSignup;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.mindmesolo.mindme.Dashboard;
import com.mindmesolo.mindme.R;

/**
 * Created by Ankit Chhabra on 5/11/2016.
 */
public class SplashActivity extends AppCompatActivity {

    private long SplashDelay = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(), Dashboard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }, SplashDelay);
    }
}