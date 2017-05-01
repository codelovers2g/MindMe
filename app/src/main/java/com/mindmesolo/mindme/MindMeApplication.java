package com.mindmesolo.mindme;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.adobe.creativesdk.foundation.AdobeCSDKFoundation;
import com.adobe.creativesdk.foundation.auth.IAdobeAuthClientCredentials;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

/**
 * Created by enest_09 on 1/25/2017.
 */

//Don't remove this class adobe library depends on it.
public class MindMeApplication extends Application implements IAdobeAuthClientCredentials {

    public static final String TAG = MindMeApplication.class.getSimpleName();
    /* Be sure to fill in the two strings below. */
    private static final String CREATIVE_SDK_CLIENT_ID = "aa8f900b72624af6b8fcf236449cc360";
    private static final String CREATIVE_SDK_CLIENT_SECRET = "9072e2b0-2f07-493a-aa89-edeb57ba11a3";
    private static MindMeApplication mInstance;
    private static Context mContext;
    private RequestQueue mRequestQueue;
    private CookieManager cookieManager;

    public static synchronized MindMeApplication getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AdobeCSDKFoundation.initializeCSDKFoundation(getApplicationContext());

        mInstance = this; //return the singleton
        mContext = getApplicationContext();

    }

    @Override
    public String getClientID() {
        return CREATIVE_SDK_CLIENT_ID;
    }

    @Override
    public String getClientSecret() {
        return CREATIVE_SDK_CLIENT_SECRET;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public CookieManager getCookieManager() {
        return cookieManager;
    }

    /***
     * VOLLEY REQUEST MANAGER
     ***/
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            //set the server cookies, essential if your server require authentication
            cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
            CookieHandler.setDefault(cookieManager);
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
