package com.mindmesolo.mindme.FeedBack;

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
import android.widget.RatingBar;
import android.widget.ToggleButton;

import com.mindmesolo.mindme.R;

/**
 * Created by eNest on 8/6/2016.
 */
public class GiveFeedBack extends AppCompatActivity {

    ToggleButton toggleyes, toggleno;
    RatingBar ratingBar;
    private WebView webViewAndroid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = "http://survey.mindmesolo.com/s3/feedback";
        setContentView(R.layout.give_feedback);
        webViewAndroid = (WebView) findViewById(R.id.webview);
        webViewAndroid.getSettings().setLoadsImagesAutomatically(true);
        webViewAndroid.getSettings().setJavaScriptEnabled(true);
        webViewAndroid.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if (isNetworkAvailable()) {
            webViewAndroid.loadUrl(url);
        } else {
            webViewAndroid.loadUrl("about:blank");
//            ToastShow.setText(getBaseContext(), "Please enable internet connection or try again", Toast.LENGTH_LONG);
            AlertDialog alertDialog = new AlertDialog.Builder(GiveFeedBack.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Please enable internet connection and try again.");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Try Again", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    startActivity(getIntent());
                }
            });
            alertDialog.show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
