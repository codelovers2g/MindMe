package com.mindmesolo.mindme.LoginSignup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mindmesolo.mindme.Dashboard;
import com.mindmesolo.mindme.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User1 on 19-05-2016.
 */
public class TourEmail4 extends AppCompatActivity {
    private static final String TAG = "TourEmail4";
    String mindMeEmail, orgid, name, phone, response, password, email;
    TextView textViewEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.touremail4);
        textViewEmail = (TextView) findViewById(R.id.mindmeEmail);
        Bundle extras = getIntent().getExtras();
        response = extras.getString("data");
        email = extras.getString("email");
        password = extras.getString("password");
        setupUpEmail();
    }

    private void setupUpEmail() {
        try {
            JSONObject jsonObject = new JSONObject(response);
            orgid = jsonObject.getString("id");
            name = jsonObject.getString("name");
            mindMeEmail = jsonObject.getString("email");
            phone = jsonObject.getJSONArray("phones").getJSONObject(0).getString("phoneNumber");
            textViewEmail.setText(mindMeEmail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void MainActivity(View v) {
        try {
            ActivitySignup.Signup.finish();
            SignupSocial2.SignupSocial2.finish();
            SignupPassword3.SignupSocial3.finish();
            SignupCompleteProfile4.Signup4.finish();
            Signupverify5.SignupSocial5.finish();
            SignupCongrats6.SignupSocial6.finish();
            TourMobile1.TourMobile1.finish();
            TourAreacode2.TourMobile2.finish();
            TourKeyword3.TourMobile3.finish();
        } catch (Exception e) {
            Log.e(TAG, "Error While Finishing the activity");
        }
        StoreNameSession(name, mindMeEmail, phone);
        StoreOrganizationidSession(orgid);
        StoreEmailSession();
        Intent intent = new Intent(this, Dashboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void StoreOrganizationidSession(String orgid) {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Organization", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("OrgId", orgid);
        editor.putString("Password", password);
        editor.commit();
    }

    private void StoreEmailSession() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("UserLogin", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Email", email);
        editor.putString("Password", password);
        editor.commit();
    }

    private void StoreNameSession(String name, String mindMeEmail, String phone) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Name", name);
        editor.putString("mindMeEmail", mindMeEmail);
        editor.putString("phone", phone);
        editor.commit();
    }
}
