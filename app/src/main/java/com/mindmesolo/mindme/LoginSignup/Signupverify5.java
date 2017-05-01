package com.mindmesolo.mindme.LoginSignup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.mindmesolo.mindme.GettingStarted.VolleyResponseResult;
import com.mindmesolo.mindme.GettingStarted.VolleyService;
import com.mindmesolo.mindme.OrganizationModel;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.ToastShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User1 on 18-05-2016.
 */
public class Signupverify5 extends AppCompatActivity {

    private static final String TAG = "SignUpVerify5";
    public static Activity SignupSocial5;
    static String ConfirmationCode;
    TextView textViewVerifyCode;
    String response, password, email, mobile;
    VolleyResponseResult mResultCallback = null;
    VolleyService mVolleyService;
    SqliteDataBaseHelper sqliteDataBaseHelper;
    private Dialog dialogue_custom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.signupverify5);

        SignupSocial5 = this;

        sqliteDataBaseHelper = new SqliteDataBaseHelper(this);

        textViewVerifyCode = (TextView) findViewById(R.id.editcode);

        Bundle extras = getIntent().getExtras();
        response = extras.getString("data");
        email = extras.getString("email");
        password = extras.getString("password");

        mobile = extras.getString("mobile");

        initVolleyCallback();

        mVolleyService = new VolleyService(mResultCallback, this);

        ConfirmationCode = generatePIN();

        Button signin = (Button) findViewById(R.id.signin);


        TextView tv_resendCode = (TextView) findViewById(R.id.TV_ResendCode);
        tv_resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmationCode = generatePIN();
            }
        });


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignupCongrats6();
            }
        });

    }

    public void SignupCongrats6() {

        String code = textViewVerifyCode.getText().toString();

        if (code.equals(ConfirmationCode)) {
            Bundle dataBundle = new Bundle();
            dataBundle.putString("data", response);
            dataBundle.putString("email", email);
            dataBundle.putString("password", password);
            Intent intent = new Intent(getBaseContext(), SignupCongrats6.class);
            intent.putExtras(dataBundle);
            startActivity(intent);

        } else if (code.length() == 0 || code.isEmpty()) {
            DialogBox("Please enter Verification code");
        } else {
            getCustomDialogue("Your verification code is incorrect.", "Please try again.");
        }

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


    void initVolleyCallback() {
        mResultCallback = new VolleyResponseResult() {
            @Override
            public void objectResponce(String requestType, JSONObject response) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + response);

            }

            @Override
            public void ArrayResponce(String requestType, JSONArray response) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + response);
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d(TAG, "Volley requester  :" + requestType);
                Log.d(TAG, "Volley JSON post  :" + error.toString());
            }
        };
    }

    public String generatePIN() {
        //generate a 4 digit integer 1000 <10000
        int randomPIN = (int) (Math.random() * 9000) + 1000;
        JSONObject code = new JSONObject();

        try {
            code.put("message", "your MindMe verification code is : " + String.valueOf(randomPIN));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Log.d(TAG, "code :" + code.toString());

        ToastShow.setText(getBaseContext(), "A text message with your code has been sent to " + mobile, Toast.LENGTH_LONG);

        mVolleyService.JsonObjectRequestTypePost("confirmation", OrganizationModel.getHomeUrl() + "/mindmemobile-web/api/v1/public/sms/" + mobile + "/confirmation/code", code.toString());

        return String.valueOf(randomPIN);
    }


    public void getCustomDialogue(String AlertTitle, String AlertMessage) {

        dialogue_custom = new Dialog(Signupverify5.this);

        dialogue_custom.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialogue_custom.setContentView(R.layout.campaign_dialog_layout);

        Button btn_ok_dialogue = (Button) dialogue_custom.findViewById(R.id.btn_ok_dialogue);

        TextView title = (TextView) dialogue_custom.findViewById(R.id.title);

        TextView message = (TextView) dialogue_custom.findViewById(R.id.message);

        TextView message2 = (TextView) dialogue_custom.findViewById(R.id.message2);

        message.setText(AlertTitle);

        message2.setText(AlertMessage);

        btn_ok_dialogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogue_custom.dismiss();
            }
        });

        dialogue_custom.show();
    }


}
