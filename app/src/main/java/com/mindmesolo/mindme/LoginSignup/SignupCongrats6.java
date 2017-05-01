package com.mindmesolo.mindme.LoginSignup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mindmesolo.mindme.R;

/**
 * Created by User1 on 18-05-2016.
 */
public class SignupCongrats6 extends AppCompatActivity {
    public static Activity SignupSocial6;
    String response, password, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupcongrats6);
        SignupSocial6 = this;
        Bundle extras = getIntent().getExtras();
        response = extras.getString("data");
        email = extras.getString("email");
        password = extras.getString("password");
    }

    public void TourMobile1(View v) {
        Bundle dataBundle = new Bundle();
        dataBundle.putString("data", response);
        dataBundle.putString("email", email);
        dataBundle.putString("password", password);
        Intent intent = new Intent(getBaseContext(), TourMobile1.class);
        intent.putExtras(dataBundle);
        startActivity(intent);
    }

}
