package com.mindmesolo.mindme.GettingStarted;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mindmesolo.mindme.OrganizationModel;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by enest_09 on 10/12/2016.
 */

public class ImportContacts extends AsyncTask<Void, String, String> {
    private static final String TAG = "ImportContacts";
    SqliteDataBaseHelper dBhelper;
    DataHelper dataHelper = new DataHelper();
    Context context;
    String data;

    public ImportContacts(Context context, String data) {
        this.context = context;
        this.data = data;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {

            Log.i(TAG, "ImportContacts Working");

            JSONObject contacts = new JSONObject(data);

            JSONArray jsonArray = contacts.getJSONArray("contactIds");

            for (int i = 0; i < jsonArray.length(); i++) {

                String Contactid = jsonArray.getString(i);

                final String org_id = dBhelper.getOrganizationId();

                final String finalToken = GetApiAccess();

                String REGISTER_URL = OrganizationModel.getApiBaseUrl() + org_id + "/contacts/" + Contactid + "";

                RequestQueue requestQueue = Volley.newRequestQueue(context);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, REGISTER_URL,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                dBhelper.insertNewContact(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // ToastShow("oops something went wrong. try again");
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
                                    //Additional cases
                                }
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Authorization", "Basic " + finalToken);
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                    }
                };
                requestQueue.add(jsonObjectRequest);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "You are at PostExecute";
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

    public String GetApiAccess() {
        SharedPreferences pref1 = context.getSharedPreferences("UserLogin", Context.MODE_PRIVATE);
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


}
