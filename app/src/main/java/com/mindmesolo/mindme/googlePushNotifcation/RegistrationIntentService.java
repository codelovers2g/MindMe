package com.mindmesolo.mindme.googlePushNotifcation;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by enest_09 on 11/22/2016.
 */

public class RegistrationIntentService extends IntentService {

    public static final String PREFS_NAME = "DEVICE_TOKEN_PREFS";
    public static final String PREFS_KEY = "DEVICE_TOKEN_STRING";
    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String token = FirebaseInstanceId.getInstance().getToken();
            //Log.e(TAG,"Token :"+token);
            SharedPreferences settings;
            SharedPreferences.Editor editor;
            settings = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            editor = settings.edit();
            editor.clear();
            editor.commit();
            editor.putString(PREFS_KEY, token);
            editor.commit();
        } catch (Exception e) {
            Log.e(TAG, "Fail to Generate firebase token ");
        }
    }
}
