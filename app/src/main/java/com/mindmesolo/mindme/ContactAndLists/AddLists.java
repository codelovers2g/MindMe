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

public class AddLists extends Activity implements View.OnClickListener {
    final String TAG = "AddLists";

    EditText editText;
    String jsonStr;

    SqliteDataBaseHelper dBhelper = new SqliteDataBaseHelper(this);

    DataHelper mydatahelper = new DataHelper();
    Button buttonAdd, buttoncancel;
    TextView TV_simple_text_message;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.add);

        editText = (EditText) findViewById(R.id.editText);

        editText.setHint("Add new list");

        TextView textView = (TextView) findViewById(R.id.toolbartxt);
        textView.setText("Add New List");

        buttonAdd = (Button) findViewById(R.id.Add);
        buttonAdd.setOnClickListener(this);

        TV_simple_text_message = (TextView) findViewById(R.id.TV_simple_text_message);

        TV_simple_text_message.setText("Use lists to organize your contacts into groups.");

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
            ToastShow.setText(getBaseContext(), "Please enter List name.", Toast.LENGTH_LONG);
        } else if (checkABoolean(editText.getText().toString().trim())) {
            ToastShow.setText(getBaseContext(), "List " + editText.getText().toString() + " already exist. Try another name", Toast.LENGTH_LONG);
        } else {
            AddNewList();
        }
    }

    public void AddNewList() {
        showProgressDialog("Adding New List..");

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

        jsonStr = jsonObject.toString();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final String ADD_List_URL = OrganizationModel.getApiBaseUrl() + org_id + "/lists";
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, ADD_List_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Response", response.toString());
                        handleListResponce(response);
                        pDialog.dismiss();
                        finish();
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

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonArrayRequest);
    }

    private void handleListResponce(JSONObject jsonObject) {
        try {
            String listid = jsonObject.getString("id");
            String listcode = jsonObject.getString("code");
            String listname = jsonObject.getString("name");
            String liststatus = jsonObject.getString("status");
            JSONArray jsonArray1 = jsonObject.getJSONArray("contactIds");
            final String jsonstr = jsonArray1.toString();
            int len = jsonArray1.length();
            dBhelper.insertNewList(listid, listcode, listname, liststatus, len, jsonstr);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
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
        ArrayList<String> data = dBhelper.getAllListNames();
        if (data.contains(test)) {
            return true;
        } else {
            return false;
        }
    }

    ;

}
