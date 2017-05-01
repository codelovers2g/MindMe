package com.mindmesolo.mindme.LoginSignup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mindmesolo.mindme.OrganizationModel;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.Services.VolleyApi;
import com.mindmesolo.mindme.helper.ToastShow;

import org.json.JSONObject;

/**
 * Created by User1 on 18-05-2016.
 */
public class ActivitySignup extends AppCompatActivity {
    public static Activity Signup;
    EditText editTextEmail, editTextInvitationCode;
    TextView textViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        Signup = this;
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextInvitationCode = (EditText) findViewById(R.id.editTextInvitationcode);
        textViewLogin = (TextView) findViewById(R.id.loginPage);
        textViewLogin.setOnClickListener((View v) -> {
            startActivity(new Intent(getBaseContext(), login.class));
        });

        findViewById(R.id.signin).setOnClickListener((View v) -> {
            SignUpSocial(v);
        });

    }

    public void SignUpSocial(View v) {
        final String InvitationCode = editTextInvitationCode.getText().toString();
        if (InvitationCode.length() == 0) {
            DialogBox("Please Enter Invitation Code");
        } else if (isNetworkAvailable()) {
            matchCodeOnServer(InvitationCode);
        } else {
            ToastShow.setText(getBaseContext(), "Please Check your internet Connection is active!", Toast.LENGTH_LONG);
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void matchCodeOnServer(String InvitationCode) {
        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please wait.");
        pDialog.show();
        String REGISTER_URL = "/mindmemobile-web/api/v1/public/promocode/" + InvitationCode;
        VolleyApi.getInstance().getJsonObjectPublic(getBaseContext(), REGISTER_URL, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                pDialog.dismiss();
                if (result.success && result.dataIsObject()) {
                    JSONObject response = result.getDataAsObject();
                    Bundle bundle = new Bundle();
                    bundle.putString("email", "");
                    //Intent intent = new Intent(getBaseContext(), SignupSocial2.class);
                    Intent intent = new Intent(getBaseContext(), SignupPassword3.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    DialogBox("This code is invalid. Please enter another code");
                }
            }
        });
    }

    public void Weblinkclick(View view) {
        String url = OrganizationModel.getMindMeReferral();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void DialogBox(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
