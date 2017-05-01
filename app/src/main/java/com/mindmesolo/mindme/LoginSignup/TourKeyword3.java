package com.mindmesolo.mindme.LoginSignup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mindmesolo.mindme.R;

/**
 * Created by User1 on 19-05-2016.
 */
public class TourKeyword3 extends AppCompatActivity {
    public static Activity TourMobile3;
    String response, password, email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tourkeyword3);
        TourMobile3 = this;
        Bundle extras = getIntent().getExtras();
        response = extras.getString("data");
        email = extras.getString("email");
        password = extras.getString("password");
    }

    public void TourEmail4(View v) {
        Bundle dataBundle = new Bundle();
        dataBundle.putString("data", response);
        dataBundle.putString("email", email);
        dataBundle.putString("password", password);
        Intent intent = new Intent(getBaseContext(), TourEmail4.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtras(dataBundle);
        startActivity(intent);
    }
}
