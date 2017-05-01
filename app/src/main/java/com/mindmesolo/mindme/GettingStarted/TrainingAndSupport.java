package com.mindmesolo.mindme.GettingStarted;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mindmesolo.mindme.R;

/**
 * Created by User1 on 9/19/2016.
 */
public class TrainingAndSupport extends AppCompatActivity {
    String url = "http://support.mindmesolo.com/";
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainingandsupport);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.getString("URL");
        }
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:document.body.style.margin=\"8%\"; void 0");
            }
        });

        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        if (isNetworkAvailable()) {
            webView.loadUrl(url);
        } else {
            webView.loadUrl("about:blank");
            AlertDialog alertDialog = new AlertDialog.Builder(TrainingAndSupport.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Please enable internet connection and try again.");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Try Again", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    startActivity(getIntent());
                }
            });
            alertDialog.show();
            //Toast.makeText(getBaseContext(), "Please enable internet connection or try again", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
