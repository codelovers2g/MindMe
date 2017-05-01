package com.mindmesolo.mindme.ToolsAndSettings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.ToastShow;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by User1 on 18-05-2016.
 */
public class Resetpassword extends AppCompatActivity {
    String password;
    EditText editTextOldPassword;
    TextWatcher myTestWatcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String result = s.toString().replaceAll(" ", "");
            if (!s.toString().equals(result)) {
                editTextOldPassword.setText(result);
                editTextOldPassword.setSelection(result.length());
                ToastShow.setText(getBaseContext(), "Cannot add a space.", Toast.LENGTH_LONG);
                // alert the user
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resetpassword);
        SharedPreferences pref1 = getBaseContext().getSharedPreferences("UserLogin", Context.MODE_PRIVATE);
        password = pref1.getString("Password", null);
        editTextOldPassword = (EditText) findViewById(R.id.oldpassword);
        editTextOldPassword.addTextChangedListener(myTestWatcher1);

    }

    public void ResetPassword2(View v) {
        if (StringUtils.isNotBlank(editTextOldPassword.getText())) {
            if (editTextOldPassword.getText().toString().equals(password)) {
                Intent intent = new Intent(getBaseContext(), ResetPassword2.class);
                startActivity(intent);
                finish();
            } else {
                ToastShow.setText(Resetpassword.this, "Password Incorrect", Toast.LENGTH_SHORT);
            }

        } else {
            ToastShow.setText(Resetpassword.this, "Please enter password.", Toast.LENGTH_SHORT);
        }
    }

}
