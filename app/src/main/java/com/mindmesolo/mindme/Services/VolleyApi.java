package com.mindmesolo.mindme.Services;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mindmesolo.mindme.MindMeApplication;
import com.mindmesolo.mindme.helper.DataHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc-14 on 2/16/2017.
 */

public class VolleyApi {

    final static String HomeUrl = "https://solo.mindmemobile.com";
    public static String TAG = "VolleyApi";
    private static VolleyApi mInstance = new VolleyApi();

    //private SessionManager session;
    DataHelper datahelper = new DataHelper();

    //Create a singleton
    public static synchronized VolleyApi getInstance() {
        return mInstance;
    }

    //Create a get request with the url to query, and a callback
    public void getJsonArray(Context mcontext, String url, final ApiResponse<ApiResult> completion) {
        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, HomeUrl + url, (JSONArray) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ApiResult res = new ApiResult();
                        res.data = response;
                        res.success = true;
                        completion.onCompletion(res);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ApiResult res = new ApiResult();
                res.success = false;
                res.volleyError = error;
                completion.onCompletion(res);
                displayVolleyResponseError(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Basic " + datahelper.getApiAccess(mcontext));
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        MindMeApplication.getInstance().addToRequestQueue(jsonRequest);
    }


    //Create a get request with the url to query, and a callback
    public void getJsonArrayWithoutAuth(Context mcontext, String url, final ApiResponse<ApiResult> completion) {
        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, HomeUrl + url, (JSONArray) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ApiResult res = new ApiResult();
                        res.data = response;
                        res.success = true;
                        completion.onCompletion(res);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ApiResult res = new ApiResult();
                res.success = false;
                res.volleyError = error;
                completion.onCompletion(res);
                displayVolleyResponseError(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                //headers.put("Authorization", "Basic " + datahelper.getApiAccess(mcontext));
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        MindMeApplication.getInstance().addToRequestQueue(jsonRequest);
    }


    //Create a get request with the url to query, and a callback
    public void getJsonObject(Context mcontext, String url, final ApiResponse<ApiResult> completion) {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, HomeUrl + url, (JSONObject) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ApiResult res = new ApiResult();
                        res.data = response;
                        res.success = true;
                        completion.onCompletion(res);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ApiResult res = new ApiResult();
                res.success = false;
                res.volleyError = error;
                completion.onCompletion(res);
                displayVolleyResponseError(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Basic " + datahelper.getApiAccess(mcontext));
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        MindMeApplication.getInstance().addToRequestQueue(jsonRequest);
    }

    //Create a get request with the url to query, and a callback
    public void getJsonObjectPublic(Context mcontext, String url, final ApiResponse<ApiResult> completion) {
        Log.i("getJsonObjectPublic", url);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, HomeUrl + url, (JSONObject) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ApiResult res = new ApiResult();
                        res.data = response;
                        res.success = true;
                        completion.onCompletion(res);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ApiResult res = new ApiResult();
                res.success = false;
                res.volleyError = error;
                completion.onCompletion(res);
                displayVolleyResponseError(error);
            }
        });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        MindMeApplication.getInstance().addToRequestQueue(jsonRequest);
    }

    //Create a put request with the url to query, and a callback
    public void putJsonObject(Context mcontext, String url, String jsonbodydata, final ApiResponse<ApiResult> completion) {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT, HomeUrl + url, (JSONObject) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ApiResult res = new ApiResult();
                        res.data = response;
                        res.success = true;
                        completion.onCompletion(res);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ApiResult res = new ApiResult();
                res.success = false;
                res.volleyError = error;
                completion.onCompletion(res);
                displayVolleyResponseError(error);
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
                headers.put("Authorization", "Basic " + datahelper.getApiAccess(mcontext));
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        //In case the server is a bit slow and we experience timeout errors﹕ error => com.android.volley.TimeoutError
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
//         jsonRequest.setRetryPolicy(
//                new DefaultRetryPolicy(1 * 1000, 1, 1.0f));
//        //Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions.
//        //Volley does retry for you if you have specified the policy.
//        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MindMeApplication.getInstance().addToRequestQueue(jsonRequest);
    }


    //Create a put request with the url to query, and a callback
    public void deleteJsonObject(Context mcontext, String url, String jsonbodydata, final ApiResponse<ApiResult> completion) {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.DELETE, HomeUrl + url, (JSONObject) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ApiResult res = new ApiResult();
                        res.data = response;
                        res.success = true;
                        completion.onCompletion(res);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ApiResult res = new ApiResult();
                res.success = false;
                res.volleyError = error;
                completion.onCompletion(res);
                displayVolleyResponseError(error);
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
                headers.put("Authorization", "Basic " + datahelper.getApiAccess(mcontext));
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        MindMeApplication.getInstance().addToRequestQueue(jsonRequest);
    }

    //Create a put request with the url to query, and a callback
    public void deleteJsonObject(Context mcontext, String url, final ApiResponse<ApiResult> completion) {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.DELETE, HomeUrl + url, (JSONObject) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ApiResult res = new ApiResult();
                        res.data = response;
                        res.success = true;
                        completion.onCompletion(res);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ApiResult res = new ApiResult();
                res.success = false;
                res.volleyError = error;
                completion.onCompletion(res);
                displayVolleyResponseError(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Basic " + datahelper.getApiAccess(mcontext));
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        MindMeApplication.getInstance().addToRequestQueue(jsonRequest);
    }

    //Create a post request with the url to query, and a callback
    public void postJsonObject(Context mcontext, String url, String jsonbodydata, final ApiResponse<ApiResult> completion) {

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, HomeUrl + url, (JSONObject) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ApiResult res = new ApiResult();
                        res.data = response;
                        res.success = true;
                        completion.onCompletion(res);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ApiResult res = new ApiResult();
                res.success = false;
                res.volleyError = error;
                completion.onCompletion(res);
                displayVolleyResponseError(error);
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
                headers.put("Authorization", "Basic " + datahelper.getApiAccess(mcontext));
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        MindMeApplication.getInstance().addToRequestQueue(jsonRequest);
    }

    //Create a get request with the url to query, and a callback
    public void postJsonArrayLogin(Context mcontext, String url, String jsonbodydata, final ApiResponse<ApiResult> completion) {

        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.POST, HomeUrl + url, (JSONArray) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ApiResult res = new ApiResult();
                        res.data = response;
                        res.success = true;
                        completion.onCompletion(res);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ApiResult res = new ApiResult();
                res.success = false;
                res.volleyError = error;
                completion.onCompletion(res);
                displayVolleyResponseError(error);
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
                //headers.put("Authorization", "Basic " + datahelper.getApiAccess(mcontext));
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        MindMeApplication.getInstance().addToRequestQueue(jsonRequest);
    }

    private void displayVolleyResponseError(VolleyError error) {

        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

            Log.e(TAG, "Connection TimeOut error");

        } else if (error instanceof AuthFailureError) {

            Log.e(TAG, "AuthFailureError");

        } else if (error instanceof ServerError) {

            Log.e(TAG, "ServerError");

        } else if (error instanceof NetworkError) {

            Log.e(TAG, "NetworkError");

        } else if (error instanceof ParseError) {
            Log.e(TAG, "ParseError");
        }
    }

    //create an interface with a callback method
    public interface ApiResponse<T> {
        public void onCompletion(T result);
    }

    //Create an object to return the server's response
    public class ApiResult {
        public Boolean success;
        public String message;
        public Object data;
        public VolleyError volleyError;

        //check what kind of data is returned in the json
        public boolean dataIsArray() {
            return (data != null && data instanceof JSONArray);
        }

        public boolean dataIsObject() {
            return (data != null && data instanceof JSONObject);
        }

        //return the data properly casted
        public JSONArray getDataAsArray() {
            if (this.dataIsArray()) {
                return (JSONArray) this.data;
            } else {
                return null;
            }
        }

        public JSONObject getDataAsObject() {
            if (this.dataIsObject()) {
                return (JSONObject) this.data;
            } else {
                return null;
            }
        }

        public Boolean getSuccess() {
            return success;
        }

        public Object getData() {
            return data;
        }

        public VolleyError getVolleyError() {
            return volleyError;
        }
    }

}
