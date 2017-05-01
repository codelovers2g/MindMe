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
public class SignupSocial2 extends AppCompatActivity {

    public static Activity SignupSocial2;
    String storedEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup2);
        SignupSocial2 = this;
        Bundle extras = getIntent().getExtras();
        storedEmail = extras.getString("email");
    }

    public void SignupPassword3(View v) {
        Bundle bundle = new Bundle();
        bundle.putString("email", storedEmail);
        Intent intent = new Intent(getBaseContext(), SignupPassword3.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }
}

