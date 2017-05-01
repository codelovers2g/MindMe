package com.mindmesolo.mindme.ToolsAndSettings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mindmesolo.mindme.OrganizationModel;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by eNest on 5/19/2016.
 */

public class RestrictionsToolsSettings extends AppCompatActivity {

    private static final String TAG = "Restrictions";

    DataHelper dataHelper = new DataHelper();

    SqliteDataBaseHelper dBhelper = new SqliteDataBaseHelper(this);

    SwitchCompat callSwitch, textSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restrictions_in_tools_settings);
        callSwitch = (SwitchCompat) findViewById(R.id.callSwitch);
        textSwitch = (SwitchCompat) findViewById(R.id.textSwitch);
        setUpUserInterface();

    }

    private void setUpUserInterface() {
        try {
            JSONObject restraction = new JSONObject(dBhelper.getRestriction());

            String callingSwitch = restraction.getString("callDuringBusinessHours");

            String smsSwitch = restraction.getString("smsDuringBusinessHours");

            callSwitch.setChecked(Boolean.parseBoolean(callingSwitch));

            textSwitch.setChecked(Boolean.parseBoolean(smsSwitch));

        } catch (JSONException e) {
            e.printStackTrace();
            callSwitch.setChecked(false);
            textSwitch.setChecked(false);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new DownloadFilesTask(this).execute();
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private String getJsonData() {
        JSONObject restrictionObject = new JSONObject();
        JSONObject restriction = new JSONObject();
        try {

            String callswitch = String.valueOf(callSwitch.isChecked());

            String textswitch = String.valueOf(textSwitch.isChecked());

            restriction.put("callDuringBusinessHours", callswitch);

            restriction.put("smsDuringBusinessHours", textswitch);

            restrictionObject.put("restriction", restriction);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return restrictionObject.toString();
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

    private class DownloadFilesTask extends AsyncTask<String, String, String> {

        Context context;

        public DownloadFilesTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {

            final String token = GetApiAccess();

            String org_id = dBhelper.getOrganizationId();

            final String jsonBodyData = getJsonData();

            RequestQueue requestQueue = Volley.newRequestQueue(RestrictionsToolsSettings.this);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, OrganizationModel.getApiBaseUrl() + org_id + "/settings",
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String restriction = response.getString("restriction");
                                Log.i(TAG, "Data Updated Success !  :" + restriction);
                                dBhelper.updateRestriction(restriction);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String json = null;
                            Log.e(TAG, "Volley updating contacts types error !");
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
                    headers.put("Authorization", "Basic " + token);
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };
            requestQueue.add(jsonObjectRequest);
            return null;
        }

        @Override
        protected void onProgressUpdate(String... progress) {
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }


}
