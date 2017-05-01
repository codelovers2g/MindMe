package com.mindmesolo.mindme.LoginSignup;

/**
 * Created by eNest on 5/24/2016.
 */

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.mindmesolo.mindme.Dashboard;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.Services.VolleyApi;
import com.mindmesolo.mindme.ToolsAndSettings.PasswordRecovery;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.ToastShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class login extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "login";

    String Responcedata = null;

    String name, email, password, orgid, mindMeEmail, phone;

    EditText Edit_text_username, Edit_text_password;

    Button signin;

    ImageButton imagebtnfacebook, imagebtnlinkedin;

    TextView textViewResetPassword, textViewSignup;

    Dialog dialogue_custom;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        signin = (Button) findViewById(R.id.signin);
        signin.setOnClickListener(this);

        imagebtnfacebook = (ImageButton) findViewById(R.id.imagebtnfacebook);
        imagebtnfacebook.setOnClickListener(this);

        imagebtnlinkedin = (ImageButton) findViewById(R.id.imagebtnlinkedin);
        imagebtnlinkedin.setOnClickListener(this);

        Edit_text_username = (EditText) findViewById(R.id.editTextEmail);
        Edit_text_password = (EditText) findViewById(R.id.editTextPassword);

        textViewSignup = (TextView) findViewById(R.id.signup);
        textViewSignup.setOnClickListener(this);

        textViewResetPassword = (TextView) findViewById(R.id.resetpassword);
        textViewResetPassword.setOnClickListener(this);
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

    public void userLogin() {
        String API_URL = "/mindmemobile-web/api/v1/public/login";
        String JsonBodyData = getJsonObject();

        // Show Progress till response execute background
        showProgressDialog();
        // Instance of Volley REST client for making Api Calls
        VolleyApi.getInstance().postJsonArrayLogin(getBaseContext(),
                API_URL, JsonBodyData.toString(),
                new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
                    @Override
                    public void onCompletion(VolleyApi.ApiResult result) {
                        // Hide Progress dialog after task executed .
                        hideProgressDialog();
                        //If result success then  execute .
                        if (result.success && result.dataIsArray()) {
                            //Get Response data as json object
                            JSONArray response = result.getDataAsArray();
                            //Pass json object to method for setup user interface for user
                            handleResponse(response.toString());
                        } else {
                            if (result.volleyError instanceof TimeoutError || result.volleyError instanceof NoConnectionError)
                                ToastShow.setText(getBaseContext(), "Please Check your internet Connection", Toast.LENGTH_LONG);

                            else if (result.volleyError instanceof AuthFailureError)
                                getCustomDialogue("Failure", "Username or Password not valid.");

                            else if (result.volleyError instanceof ServerError)
                                getCustomDialogue("Failure", "No user exists for user id " + Edit_text_username.getText().toString().trim());
                        }
                    }
                });
    }

    private String getJsonObject() {
        String Password = Edit_text_password.getText().toString().trim();
        String UserName = Edit_text_username.getText().toString().trim();

        byte[] data = null;
        try {
            data = Password.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        JSONObject JsonBodyData = new JSONObject();
        try {
            JsonBodyData.put("passwordHash", base64.replace("\n", ""));
            JsonBodyData.put("username", UserName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return JsonBodyData.toString();
    }

    public void handleResponse(String data) {
        Responcedata = data;
        JSONArray jsonarray = null;
        try {
            jsonarray = new JSONArray(Responcedata);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                orgid = jsonobject.getString("id");
                name = jsonobject.getString("name");
                mindMeEmail = jsonobject.getString("email");
                phone = jsonobject.getJSONArray("phones").getJSONObject(0).getString("phoneNumber");
                StoreNameSession(name, mindMeEmail, phone);
                StoreOrganizationidSession(orgid);
                StoreEmailSession();

                Intent intent = new Intent(getBaseContext(), Dashboard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void StoreOrganizationidSession(String orgid) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Organization", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("OrgId", orgid);
        editor.putString("Password", password);
        editor.commit();
    }

    private void StoreEmailSession() {
        password = Edit_text_password.getText().toString().trim();
        email = Edit_text_username.getText().toString().trim();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("UserLogin", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Email", email);
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

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 6) {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin:
                final String email = Edit_text_username.getText().toString();
                final String pass = Edit_text_password.getText().toString();
                if (!DataHelper.getInstance().isValidEmail(email)) {
                    Edit_text_username.setError("Invalid Email");
                } else if (!isValidPassword(pass)) {
                    Edit_text_password.setError("Minimum 6 character Required");
                } else {
                    userLogin();
                }
                break;
            case R.id.imagebtnfacebook:
//                Intent intent = new Intent(getBaseContext(), LoginWithfacebook.class);
//                startActivity(intent);
                break;

            case R.id.imagebtnlinkedin:
//                Intent intent2 = new Intent(getBaseContext(), LoginWithLinkdeIn.class);
//                startActivity(intent2);
                break;
            case R.id.signup:
                String url = "http://try.mindmesolo.com/app/?sourc=MindMe&referral=App";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;

            case R.id.resetpassword:
                Intent intent = new Intent(getBaseContext(), PasswordRecovery.class);
                startActivity(intent);
                break;

        }
    }

    public void getCustomDialogue(String AlertTitle, String AlertMessage) {

        dialogue_custom = new Dialog(login.this);

        dialogue_custom.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialogue_custom.setContentView(R.layout.campaign_dialog_layout);

        Button btn_ok_dialogue = (Button) dialogue_custom.findViewById(R.id.btn_ok_dialogue);

        TextView title = (TextView) dialogue_custom.findViewById(R.id.title);

        title.setText(AlertTitle);

        TextView message = (TextView) dialogue_custom.findViewById(R.id.message);
        message.setText(AlertMessage);

        btn_ok_dialogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogue_custom.dismiss();
            }
        });
        dialogue_custom.show();
    }
}
