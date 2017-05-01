package com.mindmesolo.mindme.googlePushNotifcation;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mindmesolo.mindme.Dashboard;
import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.OrganizationModel;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by enest_09 on 11/22/2016.
 */
public class MyAndroidFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyAndroidFCMService";


    SqliteDataBaseHelper sqliteDataBaseHelper;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        sqliteDataBaseHelper = new SqliteDataBaseHelper(getApplicationContext());

        //create notification
        createNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));

        setupDashBoardCampaign();

        getContacts();

        getNotification();
        // use this to start and trigger a service
        // Intent i= new Intent(getApplicationContext(), SyncContacts.class);
        // startService(i);
    }

    private void createNotification(String title, String messageBody) {

        Intent intent = new Intent(this, Dashboard.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent resultIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
//                .setContentText(messageBody)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, mNotificationBuilder.build());
    }

    // get dashboard data
    private void setupDashBoardCampaign() {

        final String token = GetApiAccess();

        String org_id = getOrgId();

        String REGISTER_URL = OrganizationModel.getApiBaseUrl() + org_id + "/dashboard/info";

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, REGISTER_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        SharedPreferences DASHBOARD = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = DASHBOARD.edit();
                        editor.putString(MainActivity.MAIN_DASHBOARD_DATA, response.toString());
                        editor.commit();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "VOLLEY  contacts:---->" + error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Basic " + token);
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
    }


    // get dashboard data
    private void getContacts() {

        final String token = GetApiAccess();

        String org_id = getOrgId();

        String REGISTER_URL = OrganizationModel.getApiBaseUrl() + getOrgId() + "/contacts?code=ACTIVE&size=0&sortBy=date&asc=false";

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, REGISTER_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            for (int j = 0; j < response.length(); j++) {
                                if (response.length() == sqliteDataBaseHelper.numberOfRowsContact()) {
                                    break;
                                } else {
                                    sqliteDataBaseHelper.insertNewContact(response.getJSONObject(j));
                                }
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "VOLLEY  contacts:---->" + e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "VOLLEY  contacts:---->" + error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Basic " + token);
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
    }


    // get dashboard data
    private void getNotification() {

        final String token = GetApiAccess();

        String org_id = getOrgId();

        String REGISTER_URL = OrganizationModel.getApiBaseUrl() + getOrgId() + "/notifications/info";

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, REGISTER_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int j = 0; j < response.length(); j++) {
                                if (response.length() == sqliteDataBaseHelper.getNotifications().size()) {
                                    break;
                                }
                                sqliteDataBaseHelper.insertNotification(response.getJSONObject(j));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "Notification inserting:---->" + e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "VOLLEY  contacts:---->" + error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Basic " + token);
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
    }


    public String GetApiAccess() {
        SharedPreferences pref1 = getBaseContext().getSharedPreferences("UserLogin", Context.MODE_PRIVATE);
        String email = pref1.getString("Email", null);
        String password = pref1.getString("Password", null);
        String token = email + ":" + password;
        byte[] data = null;
        try {
            data = token.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        final String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        final String finalToken = base64.replace("\n", "");
        return finalToken;
    }

    private String getOrgId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Organization", Context.MODE_PRIVATE);
        String StoredOrgid = pref.getString("OrgId", null);
        return StoredOrgid;
    }
}