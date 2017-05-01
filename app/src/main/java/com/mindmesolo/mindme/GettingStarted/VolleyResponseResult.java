package com.mindmesolo.mindme.GettingStarted;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by enest_09 on 9/27/2016.
 */

public interface VolleyResponseResult {
    public void objectResponce(String requestType, JSONObject response);

    public void ArrayResponce(String requestType, JSONArray response);

    public void notifyError(String requestType, VolleyError error);
}
