package com.mindmesolo.mindme.ToolsAndSettings;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mindmesolo.mindme.OrganizationModel;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.ToastShow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User1 on 19-05-2016.
 */
public class ResetPassword2 extends AppCompatActivity {
    private static final String TAG = "ResetPassword2";
    EditText editTextPassword, editTextRePassword;
    SqliteDataBaseHelper dBhelper;
    String jsonStr, pass;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resetpassword2);
        dBhelper = new SqliteDataBaseHelper(this);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextRePassword = (EditText) findViewById(R.id.editTextConfirmPassword);
    }

    public void LoginActivity(View view) {
        pass = editTextPassword.getText().toString();
        String rePass = editTextRePassword.getText().toString();
        if (pass.length() == 0) {
            ToastShow.setText(ResetPassword2.this, "Please enter Password", Toast.LENGTH_SHORT);
        } else if (pass.length() < 7) {
            ToastShow.setText(ResetPassword2.this, "Password must be of 7 or more Characters", Toast.LENGTH_SHORT);
        } else if (rePass.length() == 0) {
            ToastShow.setText(ResetPassword2.this, "Re-enter Password", Toast.LENGTH_SHORT);
        } else if (pass.equals(rePass)) {
            changePassword();
        } else {
            ToastShow.setText(ResetPassword2.this, "Password do not match", Toast.LENGTH_SHORT);
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

    private void changePassword() {
        pDialog = new ProgressDialog(ResetPassword2.this);
        pDialog.setMessage("Authenticating...");
        pDialog.setCancelable(false);
        pDialog.show();

        final SharedPreferences pref = getBaseContext().getSharedPreferences("UserLogin", Context.MODE_PRIVATE);

        String user = dBhelper.getUserData();

        final String token = GetApiAccess();

        String UserID = null;

        byte[] data = null;

        try {

            data = pass.getBytes("UTF-8");

        } catch (UnsupportedEncodingException e1) {

            e1.printStackTrace();
        }

        String base64 = Base64.encodeToString(data, Base64.DEFAULT);

        try {

            JSONObject jsonObject = new JSONObject(user);

            jsonObject.remove("passwordHash");

            UserID = jsonObject.getString("id");

            jsonObject.put("passwordHash", base64.replace("\n", ""));

            jsonStr = jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, OrganizationModel.getUserBaseUrl() + UserID,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        final String responcedata = response.toString();
                        dBhelper.updateUserData(responcedata);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("Password", pass);
                        editor.apply();
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        ToastShow.setText(ResetPassword2.this, "Password changed successfully", Toast.LENGTH_SHORT);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        Log.e(TAG, "VOLLEY" + error.toString());
                        ToastShow.setText(getBaseContext(), "Please Check your internet Connection is active!", Toast.LENGTH_LONG);
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
}
