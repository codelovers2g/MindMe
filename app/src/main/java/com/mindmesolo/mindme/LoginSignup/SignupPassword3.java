package com.mindmesolo.mindme.LoginSignup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mindmesolo.mindme.R;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by User1 on 18-05-2016.
 */

public class SignupPassword3 extends AppCompatActivity {

    public static Activity SignupSocial3;

    EditText editTextEmail, editTextPassword, editTextRePassword;

    String storedEmail;

    AppCompatCheckBox checkbox;

    TextView textViewTermsConditions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signuppassword3);
        SignupSocial3 = this;
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextRePassword = (EditText) findViewById(R.id.editTextConfirmPassword);
        Bundle extras = getIntent().getExtras();
        storedEmail = extras.getString("email");
        editTextEmail.setText(storedEmail);
        checkbox = (AppCompatCheckBox) findViewById(R.id.checkbox);
        textViewTermsConditions = (TextView) findViewById(R.id.textViewTermsConditions);
        textViewTermsConditions.setOnClickListener((View v) -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("http://mindmesolo.com/terms-conditions/"));
            startActivity(i);
        });
    }

    public void SignupCompleteProfile4(View v) {

        final String email = editTextEmail.getText().toString();

        final String pass = editTextPassword.getText().toString();

        final String rePass = editTextRePassword.getText().toString();

        if (StringUtils.isEmpty(email)) {

            DialogBox("Please Enter Email");

        } else if (!isValidEmail(email)) {

            DialogBox("Invalid Email");

        } else if (StringUtils.isEmpty(pass)) {

            DialogBox("Please enter Password");

        } else if (pass.length() < 7) {

            DialogBox("Password must be of 7 or more Characters");

        } else if (rePass.length() == 0) {

            DialogBox("Re-enter Password");

        } else if (!pass.equals(rePass)) {
            DialogBox("Password do not match");

        } else if (!checkbox.isChecked()) {

            DialogBox("You must agree with the Terms and Conditions");

        } else {
            Bundle dataBundle = new Bundle();
            dataBundle.putString("email", email);
            dataBundle.putString("password", pass);
            Intent intent = new Intent(getBaseContext(), SignupCompleteProfile4.class);
            intent.putExtras(dataBundle);
            startActivity(intent);
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

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
