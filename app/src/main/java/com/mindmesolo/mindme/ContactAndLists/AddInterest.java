package com.mindmesolo.mindme.ContactAndLists;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.ToastShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * last Updated  by srn  on 1/09/2016.
 */
public class AddInterest extends Activity implements View.OnClickListener {

    private static final String TAG = "AddInterest";

    EditText editText;

    Button buttonAdd, buttoncancel;
    SqliteDataBaseHelper dBhelper = new SqliteDataBaseHelper(this);
    DataHelper mydatahelper = new DataHelper();
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        editText = (EditText) findViewById(R.id.editText);
        editText.setHint("Add new interest");

        TextView textView = (TextView) findViewById(R.id.toolbartxt);
        textView.setText("Add New Interest");

        TextView TV_simple_text_message = (TextView) findViewById(R.id.TV_simple_text_message);

        TV_simple_text_message.setText("Use interests to represent what you offer, and capture the needs of your contacts.");

        buttonAdd = (Button) findViewById(R.id.Add);
        buttonAdd.setOnClickListener(this);
        buttoncancel = (Button) findViewById(R.id.cancel);
        buttoncancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Add:
                checkListData();
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }

    private void checkListData() {
        if (editText.getText().toString().trim().length() <= 0) {
            ToastShow.setText(getBaseContext(), "Please enter Interest name.", Toast.LENGTH_LONG);
        } else if (checkABoolean(editText.getText().toString().trim())) {
            ToastShow.setText(getBaseContext(), "Interest " + editText.getText().toString() + " already exist. Try another name", Toast.LENGTH_LONG);
        } else {
            AddInterest();
        }
    }

    public void AddInterest() {

        showProgressDialog("Adding New Interest..");

        final String finalToken = GetApiAccess();

        final String org_id = dBhelper.getOrganizationId();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", editText.getText().toString());
            jsonObject.put("id", "");
            jsonObject.put("name", editText.getText().toString());
            jsonObject.put("status", "ACTIVE");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String jsonStr = jsonObject.toString();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, OrganizationModel.getApiBaseUrl() + org_id + "/interests",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        SharedPreferences DASHBOARD = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
                        String GettingStartData = DASHBOARD.getString(MainActivity.PREFS_KEY_GETTING_START, null);
                        if (GettingStartData != null) {
                            // update Orginization
                            if (!GettingStartData.contains("Interests")) {
                                UpdateOrganization();
                            } else {
                                ToastShow.setText(getBaseContext(), "Interest Name " + editText.getText().toString() + " Created successfully", Toast.LENGTH_LONG);
                                finish();
                            }
                        } else {
                            UpdateOrganization();
                        }
                        handleInterestResponce(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        ToastShow.setText(getBaseContext(), "oops something went wrong. try again", Toast.LENGTH_LONG);
                        String json = null;
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 400:
                                    json = new String(response.data);
                                    json = mydatahelper.trimMessage(json, "message");
                                    if (json != null) mydatahelper.displayMessage(json);
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
                    return jsonStr == null ? null : jsonStr.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            jsonStr, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Basic " + finalToken);
                return headers;
            }
        };

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonArrayRequest);
    }

    public void UpdateOrganization() {

        final String org_id = dBhelper.getOrganizationId();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id", org_id);
            jsonObject.put("properties",
                    new JSONArray().put(
                            new JSONObject().put("name", "Greeting Started")
                                    .put("value", getPropertiesObject())));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String jsonStr = jsonObject.toString();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.PUT, OrganizationModel.getApiBaseUrl() + org_id,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ToastShow.setText(getBaseContext(), "Interest Name " + editText.getText().toString() + " Created successfully", Toast.LENGTH_LONG);
                        try {
                            String GettingStartData = response.getJSONArray("properties").getJSONObject(0).getString("value");
                            SharedPreferences DASHBOARD = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = DASHBOARD.edit();
                            editor.putString(MainActivity.PREFS_KEY_GETTING_START, GettingStartData);
                            editor.commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, e.toString());
                        }

                        finish();
                        pDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        ToastShow.setText(getBaseContext(), "oops something went wrong. try again", Toast.LENGTH_LONG);
                        String json = null;
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 400:
                                    json = new String(response.data);
                                    json = mydatahelper.trimMessage(json, "message");
                                    if (json != null) mydatahelper.displayMessage(json);
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
                    return jsonStr == null ? null : jsonStr.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            jsonStr, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Basic " + GetApiAccess());
                return headers;
            }
        };

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonArrayRequest);
    }

    private void handleInterestResponce(JSONObject jsonObject) {
        try {
            String Interestid = jsonObject.getString("id");
            String Interestcode = jsonObject.getString("code");
            String Interestname = jsonObject.getString("name");
            String Intereststatus = jsonObject.getString("status");
            JSONArray jsonArray1 = jsonObject.getJSONArray("contactIds");
            final String jsonstr = jsonArray1.toString();
            int len = jsonArray1.length();
            dBhelper.insertInterests(Interestid, Interestcode, Interestname, Intereststatus, len, jsonstr);
        } catch (Exception e) {
        }
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

    private boolean checkABoolean(String test) {
        ArrayList<String> data = dBhelper.getAllInterestNames();
        if (data.contains(test)) {
            return true;
        } else {
            return false;
        }
    }

    ;

    private String getPropertiesObject() {
        SharedPreferences DASHBOARD = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        String GettingStartData = DASHBOARD.getString(MainActivity.PREFS_KEY_GETTING_START, null);
        StringBuilder stringBuilder;
        if (GettingStartData != null) {
            stringBuilder = new StringBuilder(GettingStartData);
            if (!GettingStartData.contains("Interests")) {
                stringBuilder.append("," + "Interests");
            }
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Interests");
        }
        return String.valueOf(stringBuilder);
    }
}
