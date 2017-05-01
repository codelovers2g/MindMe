package com.mindmesolo.mindme.GettingStarted;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mindmesolo.mindme.helper.DataHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by enest_09 on 9/27/2016.
 */
public class VolleyService {

    private final String TAG = "VolleyService";

    DataHelper mydatahelper = new DataHelper();

    VolleyResponseResult mResultCallback = null;

    Context mContext;

    public VolleyService(VolleyResponseResult resultCallback, Context context) {
        mResultCallback = resultCallback;
        mContext = context;
    }

    public void volleyGetJsonObjectRequest(final String requestType, String WebUrl) {
        try {
            final String token = GetApiAccess();
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, WebUrl,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            if (mResultCallback != null)
                                mResultCallback.objectResponce(requestType, response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String json = null;
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                if (mResultCallback != null)
                                    mResultCallback.notifyError(requestType, error);
                                switch (response.statusCode) {
                                    case 400:
                                        json = new String(response.data);
                                        json = mydatahelper.trimMessage(json, "message");
                                        if (json != null) mydatahelper.displayMessage(json);
                                        break;
                                }
                                //Additional cases
                            }
                        }
                    }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
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
        } catch (Exception e) {

        }
    }

    public void volleyGetJsonArrayRequest(final String requestType, String WebUrl) {
        try {
            final String token = GetApiAccess();
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, WebUrl,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            if (mResultCallback != null)
                                mResultCallback.ArrayResponce(requestType, response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String json = null;
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                if (mResultCallback != null)
                                    mResultCallback.notifyError(requestType, error);
                                switch (response.statusCode) {
                                    case 400:
                                        json = new String(response.data);
                                        json = mydatahelper.trimMessage(json, "message");
                                        if (json != null) mydatahelper.displayMessage(json);
                                        break;
                                }
                                //Additional cases
                            }
                        }
                    }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
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
        } catch (Exception e) {

        }
    }

    public void jsonObjectRequestTypePutWithAuth(final String requestType, String url, String data) {

        Log.i(TAG, url);

        try {

            final String jsonbodydata = data;

            final String token = GetApiAccess();

            RequestQueue requestQueue = Volley.newRequestQueue(mContext);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("VolleyService", "Update Success");
                            if (mResultCallback != null)
                                mResultCallback.objectResponce(requestType, response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String json = null;
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                if (mResultCallback != null)
                                    mResultCallback.notifyError(requestType, error);
                                switch (response.statusCode) {
                                    case 400:
                                        json = new String(response.data);
                                        json = mydatahelper.trimMessage(json, "message");
                                        if (json != null) mydatahelper.displayMessage(json);
                                        break;
                                }
                                //Additional cases
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
                        return jsonbodydata == null ? null : jsonbodydata.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                jsonbodydata, "utf-8");
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
        } catch (Exception e) {
        }
    }

    public void JsonObjectRequestTypePost(final String requestType, String url, String data) {

        Log.i(TAG, url);

        try {

            final String jsonbodydata = data;

            final String token = GetApiAccess();

            RequestQueue requestQueue = Volley.newRequestQueue(mContext);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            if (mResultCallback != null)
                                mResultCallback.objectResponce(requestType, response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String json = null;
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                if (mResultCallback != null)
                                    mResultCallback.notifyError(requestType, error);
                                switch (response.statusCode) {
                                    case 400:
                                        json = new String(response.data);
                                        json = mydatahelper.trimMessage(json, "message");
                                        if (json != null) mydatahelper.displayMessage(json);
                                        break;
                                }
                                //Additional cases
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
                        return jsonbodydata == null ? null : jsonbodydata.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                jsonbodydata, "utf-8");
                        return null;
                    }
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
        }
    }

    public void JsonObjectRequestTypePostWithOuth(final String requestType, String url, String data) {

        Log.i(TAG, url);

        try {

            final String jsonbodydata = data;

            final String token = GetApiAccess();

            RequestQueue requestQueue = Volley.newRequestQueue(mContext);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (mResultCallback != null)
                                mResultCallback.objectResponce(requestType, response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String json = null;
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                if (mResultCallback != null)
                                    mResultCallback.notifyError(requestType, error);
                                switch (response.statusCode) {
                                    case 400:
                                        json = new String(response.data);
                                        json = mydatahelper.trimMessage(json, "message");
                                        if (json != null) mydatahelper.displayMessage(json);
                                        break;
                                }
                                //Additional cases
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
                        return jsonbodydata == null ? null : jsonbodydata.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                jsonbodydata, "utf-8");
                        return null;
                    }
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("Authorization", "Basic " + token);
                    return headers;
                }
            };
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
        }
    }


    public void JsonObjectRequestTypeDelete(final String requestType, String url) {
        try {

            final String token = GetApiAccess();

            RequestQueue requestQueue = Volley.newRequestQueue(mContext);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("VolleyService", "Delete Success");
                            if (mResultCallback != null) {
                                mResultCallback.objectResponce(requestType, response);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String json = null;
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                if (mResultCallback != null)
                                    mResultCallback.notifyError(requestType, error);
                                switch (response.statusCode) {
                                    case 400:
                                        json = new String(response.data);
                                        json = mydatahelper.trimMessage(json, "message");
                                        if (json != null) mydatahelper.displayMessage(json);
                                        break;
                                }
                                //Additional cases
                            }
                        }
                    }) {

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
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
        } catch (Exception e) {
        }
    }

    public String GetApiAccess() {
        SharedPreferences pref1 = mContext().getSharedPreferences("UserLogin", Context.MODE_PRIVATE);
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

    private Context mContext() {
        return mContext;
    }

}