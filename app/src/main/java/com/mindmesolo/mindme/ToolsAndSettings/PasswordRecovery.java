package com.mindmesolo.mindme.ToolsAndSettings;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.ToastShow;

import org.apache.commons.lang3.StringUtils;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by User1 on 9/8/2016.
 */
public class PasswordRecovery extends AppCompatActivity {
    private static final String TAG = "PasswordRecovery";
    TextView textViewEmail;
    Button buttonSend;
    private ProgressDialog pDialog;

    private static SSLSocketFactory createSslSocketFactory() {
        TrustManager[] byPassTrustManagers = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }
        }};

        SSLContext sslContext = null;
        SSLSocketFactory sslSocketFactory = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, byPassTrustManagers, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.e(TAG, StringUtils.EMPTY, e);
        } catch (KeyManagementException e) {
            Log.e(TAG, StringUtils.EMPTY, e);
        }

        return sslSocketFactory;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passwordrecovery);
        textViewEmail = (TextView) findViewById(R.id.email);
        buttonSend = (Button) findViewById(R.id.btnSend);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textViewEmail.getText().toString().length() == 0 || textViewEmail.getText().toString().isEmpty()) {
                    ToastShow.setText(PasswordRecovery.this, "Please enter username", Toast.LENGTH_SHORT);
                } else {
                    sendRecovery();
                }
            }
        });

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

    private void sendRecovery() {

        String email = textViewEmail.getText().toString();

        String Recovery_Url = "https://app.mindmesolo.com/mindmemobile-web/api/v1/public/password_recovery?userName=" + email + "";

        HurlStack stack = new HurlStack(null, createSslSocketFactory());

        RequestQueue requestQueue = Volley.newRequestQueue(this, stack);

        showProgressDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Recovery_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("\"OK\"")) {

                            ToastShow.setText(PasswordRecovery.this, "Success! We have emailed your login information", Toast.LENGTH_SHORT);
                            finish();
                        } else {
                            ToastShow.setText(PasswordRecovery.this, "Sorry, the email address you've entered does not match a MindMe account. Please try again.", Toast.LENGTH_SHORT);
                        }
                        hideProgressDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "VOLLEY--->" + error.toString());
                        ToastShow.setText(getBaseContext(), "Check your internet connection", Toast.LENGTH_LONG);
                        hideProgressDialog();
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5 * DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));

        requestQueue.add(stringRequest);
    }
}
