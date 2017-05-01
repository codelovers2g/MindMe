package com.mindmesolo.mindme.googlePushNotifcation;


import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by enest_09 on 11/22/2016.
 */
public class MyAndroidFirebaseInstanceIDService extends FirebaseInstanceIdService {


    private static final String TAG = "MyAndroidFCMIIDService";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log.d(TAG, "Refreshed token: " + refreshedToken);
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }


    private void sendRegistrationToServer(String token) {
        //Implement this method if you want to store the token on your server
    }
}
