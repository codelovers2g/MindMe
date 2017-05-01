package com.mindmesolo.mindme.LoginSignup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

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
import com.mindmesolo.mindme.Services.VolleyApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User1 on 18-05-2016.
 */
public class SignupCompleteProfile4 extends AppCompatActivity {
    private static final String TAG = "SignupCompleteProfile4";
    public static Activity Signup4;
    EditText editTextFname, editTextLname, editTextBname, editTextPhonenumber;
    Spinner spinnerIndustry, spinnerState;
    String fName, lName, bName, mobile, email, password;
    int[] areaCode = new int[]{205, 907, 480, 479, 209, 303, 203, 202, 302, 239, 229, 808, 208, 217, 219, 319, 316, 270, 225, 207, 240, 339, 231, 218, 228, 314, 406, 308
            , 702, 603, 201, 505, 212, 252, 701, 216, 405, 458, 215, 401, 803, 605, 423, 210, 385, 802, 276, 206, 304, 262, 307};
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupcompleteprofile4);
        Signup4 = this;
        editTextFname = (EditText) findViewById(R.id.editTextFname);
        editTextLname = (EditText) findViewById(R.id.editTextLname);
        editTextBname = (EditText) findViewById(R.id.editTextBname);
        editTextPhonenumber = (EditText) findViewById(R.id.editTextMPN);
        spinnerIndustry = (Spinner) findViewById(R.id.industry);
        spinnerState = (Spinner) findViewById(R.id.state);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.industry_array, R.layout.spiner_items);
        spinnerIndustry.setAdapter(adapter);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.State_arrays, R.layout.spiner_items);
        spinnerState.setAdapter(adapter1);

        Bundle extras = getIntent().getExtras();
        email = extras.getString("email");
        password = extras.getString("password");
    }

    private void showProgressDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Authenticating...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void hideProgressDialog() {
        pDialog.dismiss();
    }

    public void SignUpVerify5(View v) {
        fName = editTextFname.getText().toString();
        lName = editTextLname.getText().toString();
        bName = editTextBname.getText().toString();
        mobile = editTextPhonenumber.getText().toString();
        if (fName.length() == 0) {
            DialogBox("Please enter First name");
        } else if (lName.length() == 0) {
            DialogBox("Please enter Last name");
        } else if (bName.length() == 0) {
            DialogBox("Please enter Business name");
        } else if (mobile.length() == 0) {
            DialogBox("Please enter Mobile phone number");
        } else if (mobile.length() < 10) {
            DialogBox("Please enter your correct 10 digit mobile phone number");
        } else if (spinnerIndustry.getSelectedItem().toString().equals("Industry")) {
            DialogBox("Please select Industry type");
        } else if (spinnerState.getSelectedItem().toString().equals("State")) {
            DialogBox("Please select state");
        } else {
            showProgressDialog();
            getTwillioNumber();

        }

    }

    private void getTwillioNumber() {

        String abc = spinnerState.getSelectedItem().toString();

        String state = abc.replace(" ", "%20");

        int index = spinnerState.getSelectedItemPosition();

        int code = areaCode[index - 1];

        String Fetch_URL = "/mindmemobile-web/api/v1/public/twilio/us/lookup?areaCode=" + code + "&state=" + state + "";

        VolleyApi.getInstance().getJsonArrayWithoutAuth(getBaseContext(), Fetch_URL, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                if (result.success && result.dataIsArray()) {
                    JSONArray response = result.getDataAsArray();
                    if (response.length() == 0) {
                        hideProgressDialog();
                        DialogBox("Please select different City");
                    } else {
                        registerUser(response);
                    }
                }
            }
        });
    }

    private void registerUser(JSONArray response) {

        final String jsonStr = getUserObject(response);

        String registerUrl = OrganizationModel.getHomeUrl() + "/mindmemobile-web/api/v1/public/register";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, registerUrl,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideProgressDialog();

                        handleResponse(response);

                        Bundle dataBundle = new Bundle();
                        dataBundle.putString("data", response.toString());
                        dataBundle.putString("email", email);
                        dataBundle.putString("password", password);
                        dataBundle.putString("mobile", mobile);

                        try {

                            String orgId = response.getString("id");

                            SharedPreferences pref = getApplicationContext().getSharedPreferences("Organization", MODE_PRIVATE);

                            SharedPreferences.Editor editor = pref.edit();

                            editor.putString("OrgId", orgId);

                            editor.commit();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        Intent intent = new Intent(getBaseContext(), Signupverify5.class);
                        intent.putExtras(dataBundle);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        String json = null;
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 500:
                                    DialogBox("Mindme User Already Exists");
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
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
    }

    private String getUserObject(JSONArray jsonArray) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObjectUser = new JSONObject();
        JSONArray jsonArrayUser = new JSONArray();
        JSONArray jsonArrayphone = new JSONArray();
        JSONArray jsonArraytwilliophone = new JSONArray();
        try {
            String phone = jsonArray.getJSONObject(0).getString("phoneNumber");
            JSONObject list2 = new JSONObject();
            list2.put("phoneType", "DEFAULT");
            list2.put("phoneNumber", phone);
            jsonArraytwilliophone.put(list2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONObject list2 = new JSONObject();
            list2.put("phoneType", "MOBILE");
            list2.put("phoneNumber", mobile);
            jsonArrayphone.put(list2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        byte[] data = null;
        try {
            data = password.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        try {
            jsonObjectUser.put("createdOn", "");
            jsonObjectUser.put("updatedOn", "");
            jsonObjectUser.put("firstName", fName);
            jsonObjectUser.put("lastName", lName);
            jsonObjectUser.put("emailAddress", email);
            jsonObjectUser.put("phones", jsonArrayphone);
            jsonObjectUser.put("status", "ACTIVE");
            jsonObjectUser.put("passwordHash", base64.replace("\n", ""));
            jsonObjectUser.put("username", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonArrayUser.put(jsonObjectUser);

        try {
            jsonObject.put("createdOn", "");
            jsonObject.put("updatedOn", "");
            jsonObject.put("originalEmail", email);
            jsonObject.put("users", jsonArrayUser);
            jsonObject.put("name", bName);
            jsonObject.put("phones", jsonArraytwilliophone);
            jsonObject.put("legalName", bName);
            jsonObject.put("industryType", spinnerIndustry.getSelectedItem().toString().toUpperCase().replace(" ", "_"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }


    public void handleResponse(JSONObject jsonobject) {
        try {
            String orgid = jsonobject.getString("id");
            String name = jsonobject.getString("name");
            String mindMeEmail = jsonobject.getString("email");
            String phone = jsonobject.getJSONArray("phones").getJSONObject(0).getString("phoneNumber");
            StoreNameSession(name, mindMeEmail, phone);
            StoreOrganizationidSession(orgid);
            StoreEmailSession();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void StoreEmailSession() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("UserLogin", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Email", email);
        editor.putString("Password", password);// password from bundle
        editor.commit();
    }


    private void StoreOrganizationidSession(String orgid) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Organization", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("OrgId", orgid);
        editor.putString("Password", password);
        editor.commit();
    }

    private void StoreNameSession(String name, String mindMeEmail, String phone) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Name", name);
        editor.putString("mindMeEmail", mindMeEmail);
        editor.putString("phone", phone);
        editor.commit();

    }


    public void DialogBox(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

}