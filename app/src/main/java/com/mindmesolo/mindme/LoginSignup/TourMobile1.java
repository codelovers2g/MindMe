package com.mindmesolo.mindme.LoginSignup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.mindmesolo.mindme.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User1 on 19-05-2016.
 */
public class TourMobile1 extends AppCompatActivity {
    public static Activity TourMobile1;
    String response, password, email, phone;
    TextView textViewMobile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tourmobileno1);
        TourMobile1 = this;
        textViewMobile = (TextView) findViewById(R.id.mindmeMobile);
        Bundle extras = getIntent().getExtras();
        response = extras.getString("data");
        email = extras.getString("email");
        password = extras.getString("password");
        setUpMobile();
    }

    private void setUpMobile() {
        try {
            JSONObject jsonObject = new JSONObject(response);
            phone = jsonObject.getJSONArray("phones").getJSONObject(0).getString("phoneNumber");
            textViewMobile.setText(String.valueOf(phone));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void TourKeyword3(View view) {
        Bundle dataBundle = new Bundle();
        dataBundle.putString("data", response);
        dataBundle.putString("email", email);
        dataBundle.putString("password", password);
        Intent intent = new Intent(getBaseContext(), TourKeyword3.class);
        intent.putExtras(dataBundle);
        startActivity(intent);

    }

    public void TourAreacode2(View view) {
        Intent intent = new Intent(getBaseContext(), TourAreacode2.class);
        startActivity(intent);
    }
}
