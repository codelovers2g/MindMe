package com.mindmesolo.mindme.GettingStarted;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.OrganizationModel;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.RuntimePermissionsActivity;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.ToastShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User1 on 19-05-2016.
 */

public class Greeting_and_forwarding extends RuntimePermissionsActivity implements GreetingAndForwarding {

    private static final int REQUEST_PERMISSIONS = 20;
    private static boolean PlayRecordIsRunning = false;
    private static boolean isRecordingDeleted = false;
    private static boolean greetingSwitch = false;
    private static String forwardingMobile = "";
    private static boolean forwardingSwitch = false;
    final String TAG = "GreetingAndForwarding";
    Greeting_PagerAdapter adapter;
    SqliteDataBaseHelper dBhelper = new SqliteDataBaseHelper(this);
    ProgressDialog pDialog;
    DataHelper dataHelper = new DataHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.greeting_and_forwarding);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        TabLayout tablayout = (TabLayout) findViewById(R.id.Greetingtab_layout);

        ArrayList<String> tabs = new ArrayList<>();

        tabs.add("Greeting");

        tabs.add("Forwarding");

        final ViewPager viewPager = (ViewPager) findViewById(R.id.Greetingpager);
        adapter = new Greeting_PagerAdapter(getSupportFragmentManager(), tabs);

        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);
    }

    //check permissions for record voice and read and write audio file on device
    @Override
    public void onPermissionsGranted(int requestCode) {
        Greeting fragment = (Greeting) adapter.getFragment(0);
        if (fragment != null) {
            if (REQUEST_PERMISSIONS == requestCode) {
                fragment.startRecordingAudio();
            } else {
                fragment.deleteRecordingAudio();
            }
        } else {
            Log.i(TAG, "Fragment 2 is not initialized");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!PlayRecordIsRunning) finish();
            else communicateToFragment2();
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.greeting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            if (!PlayRecordIsRunning) {
                makeApiCall();
            } else {
                communicateToFragment2();
                makeApiCall();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void makeApiCall() {
        File audioFile = new File(MainActivity.dir, "MindMe/Greeting/greeting.mp3");
        // if user delete the old audio file then update with new
        if (audioFile.exists() && isRecordingDeleted) {
            makeVolleyRequestWithAudio();
        } else {
            makeVolleyRequestWithoutAudio();
            Log.i(TAG, "Greeting media is empty !");
        }
    }

    private void makeVolleyRequestWithoutAudio() {
        JSONObject finalJosnObject = new JSONObject();
        try {
            ArrayList<String> jsondata = dBhelper.getPhoneGreetings();
            JSONObject greetingObject = new JSONObject();
            greetingObject.put("voiceRecordingMediaURL", jsondata.get(1));
            greetingObject.put("enabled", greetingSwitch);

            JSONObject forwardObject = new JSONObject();
            forwardObject.put("rings", jsondata.get(4));
            forwardObject.put("enabled", forwardingSwitch);

            JSONArray properties = new JSONArray();
            properties.put(new JSONObject().put("name", "phoneNumber").put("value", forwardingMobile));
            forwardObject.put("properties", properties);

            //----------*******--------///
            JSONObject phoneConcierge = new JSONObject();
            phoneConcierge.put("greeting", greetingObject);
            phoneConcierge.put("forward", forwardObject);
            finalJosnObject.put("phoneConcierge", phoneConcierge);
            updateGreeting(finalJosnObject.toString());
        } catch (Exception e) {
            Log.e(TAG, "makeVolleyRequestWithoutAudio :fail to parse  json data in volley final josn object");
        }
    }

    private String getPropertiesObject() {

        SharedPreferences DASHBOARD = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);

        String GettingStartData = DASHBOARD.getString(MainActivity.PREFS_KEY_GETTING_START, null);

        StringBuilder stringBuilder;
        if (GettingStartData != null) {
            stringBuilder = new StringBuilder(GettingStartData);
            if (!GettingStartData.contains("Phone Greeting & Forwarding")) {
                stringBuilder.append("," + "Phone Greeting & Forwarding");
            }
        } else {
            stringBuilder = new StringBuilder(GettingStartData);
            stringBuilder.append("Phone Greeting & Forwarding");
        }

        return String.valueOf(stringBuilder);
    }

    private void makeVolleyRequestWithAudio() {

        JSONObject finalObject = new JSONObject();

        File audioFile = new File(MainActivity.dir, "greeting.mp3");

        if (audioFile.exists()) {
            try {
                finalObject.put("fileName", audioFile.getName());
                finalObject.put("fileType", "AUDIO");
                finalObject.put("media", encodeAudio(audioFile));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            volleyUpdateMediaRequest(finalObject.toString());
        }
    }

    public void volleyUpdateMediaRequest(String data) {

        showProgressDialog("Updating Greeting");

        final String token = GetApiAccess();

        final String org_id = dBhelper.getOrganizationId();

        final String JsonBodyData = data;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, OrganizationModel.getApiBaseUrl() + org_id + "/media",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        handleMediaResponseData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        ToastShow.setText(getBaseContext(), "oops something went wrong. try again", Toast.LENGTH_LONG);
                        Log.e(TAG, "Error  volleyUpdateLoad greeting audio ----> No Response Coming from Server");
                        String json = null;
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 400:
                                    json = new String(response.data);
                                    json = dataHelper.trimMessage(json, "message");
                                    if (json != null) dataHelper.displayMessage(json);
                                    break;
                            }
                        }
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return JsonBodyData == null ? null : JsonBodyData.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            JsonBodyData, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Basic " + token);
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void handleMediaResponseData(JSONObject jsonObject) {

        Log.i(TAG, "Audio Request : " + jsonObject.toString());

        JSONObject finalJosnObject = new JSONObject();

        try {

            ArrayList<String> jsondata = dBhelper.getPhoneGreetings();

            JSONObject greetingObject = new JSONObject();

            greetingObject.put("voiceRecordingMediaURL", getMediaUrl(jsonObject));

            greetingObject.put("enabled", greetingSwitch);

            JSONObject forwardObject = new JSONObject();

            forwardObject.put("rings", jsondata.get(4));

            forwardObject.put("enabled", forwardingSwitch);

            forwardObject.put("properties", new JSONArray().put(new JSONObject().put("name", "phoneNumber").put("value", forwardingMobile)));

            //----------*******--------///
            JSONObject phoneConcierge = new JSONObject();
            phoneConcierge.put("greeting", greetingObject);
            phoneConcierge.put("forward", forwardObject);
            finalJosnObject.put("phoneConcierge", phoneConcierge);
            try {
                finalJosnObject.put("properties",
                        new JSONArray().put(
                                new JSONObject().put("name", "Greeting Started")
                                        .put("value", getPropertiesObject())));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            updateGreeting(finalJosnObject.toString());
        } catch (Exception e) {
            Log.e(TAG, "getVolleyJsonObject :fail to parse  json data in volley final josn object");
        }
    }

    private String getMediaUrl(JSONObject jsonObject) {
        String mediaUrl = null;
        try {
            mediaUrl = jsonObject.getString("mediaUrl");
        } catch (JSONException e) {
            mediaUrl = "";
            Log.e(TAG, "getMediaUrl :fail to fail to get media url retuning  empty");
        }

        return mediaUrl;
    }

    private void updateGreeting(final String jsonBodyData) {

        Log.i(TAG, jsonBodyData.toString());

        showProgressDialog("Updating Greeting");

        final String org_id = dBhelper.getOrganizationId();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, OrganizationModel.getApiBaseUrl() + org_id,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, " updateGreeting : Data Updating completed !! ");
                        dBhelper.DeletePhoneGreetingData();
                        dBhelper.InsertIntoPhoneGreeting(response);
                        try {
                            String GettingStartData = response.getJSONArray("properties").getJSONObject(0).getString("value");
                            SharedPreferences DASHBOARD = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = DASHBOARD.edit();
                            editor.putString(MainActivity.PREFS_KEY_GETTING_START, GettingStartData);
                            editor.commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Log.e(TAG, "Error  while making volley update request ----> No Response Coming from Server");
                        String json = null;
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 400:
                                    json = new String(response.data);
                                    json = dataHelper.trimMessage(json, "message");
                                    if (json != null) dataHelper.displayMessage(json);
                                    break;
                            }
                        }
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return jsonBodyData == null ? null : jsonBodyData.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            jsonBodyData, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Basic " + GetApiAccess());
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

    @SuppressLint("NewApi")
    private String encodeAudio(File audioFile) {
        String _audioBase64 = null;
        byte[] audioBytes;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(audioFile);
            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);
            audioBytes = baos.toByteArray();
            // Here goes the Base64 string
            _audioBase64 = Base64.encodeToString(audioBytes, Base64.DEFAULT);
        } catch (Exception e) {
            Log.e(TAG, " Unable to Handle audio file ");
        }
        return _audioBase64;
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

    private void showProgressDialog(String Message) {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(Message);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    public void checkPermissions(int permission) {
        Greeting_and_forwarding.super.requestAppPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, R.string.runtime_permissions_txt
                , permission);
        Log.i(TAG, "Fragment call working ");
    }

    @Override
    public void playPauseRecord(boolean b) {
        this.PlayRecordIsRunning = b;
    }

    @Override
    public void greetingDeleted(Boolean isRecordingDeleted) {
        this.isRecordingDeleted = isRecordingDeleted;
    }

    @Override
    public void greetingDataFromFragment(Boolean GreetingSwitch) {

        this.greetingSwitch = GreetingSwitch;

        Log.i("GreetingAndForwarding", "GreetingSwitch : " + String.valueOf(GreetingSwitch));
    }

    @Override
    public void forwardingDataFromFragment(String ForwardingPhone, Boolean ForwardingSwitch) {

        this.forwardingMobile = ForwardingPhone;

        this.forwardingSwitch = ForwardingSwitch;

        Log.i("GreetingAndForwarding", "Phone : " + ForwardingPhone);

        Log.i("GreetingAndForwarding", "Switch : " + String.valueOf(ForwardingSwitch));
    }

    public void communicateToFragment2() {
        Greeting fragment = (Greeting) adapter.getFragment(0);
        if (fragment != null) {
            fragment.fragmentCommunication();
        } else {
            Log.i(TAG, "Fragment 2 is not initialized");
        }
    }
}
