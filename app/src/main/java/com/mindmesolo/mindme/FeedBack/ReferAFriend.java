package com.mindmesolo.mindme.FeedBack;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.mindmesolo.mindme.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by eNest on 8/6/2016.
 */
public class ReferAFriend extends AppCompatActivity implements View.OnClickListener {
    public static final String PACKAGE = "com.mindmesolo.mindme";

    ImageView facebook, linkdin, twitter, MindMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refer_a_friend);

        facebook = (ImageView) findViewById(R.id.facebook);
        facebook.setOnClickListener(this);

        linkdin = (ImageView) findViewById(R.id.linkdin);
        linkdin.setOnClickListener(this);

        twitter = (ImageView) findViewById(R.id.twitter);
        twitter.setOnClickListener(this);

        MindMe = (ImageView) findViewById(R.id.MindMe);
        MindMe.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.facebook:
                generateHashkey();
//                Intent intent = new Intent(getBaseContext(), LoginWithfacebook.class);
//                startActivity(intent);
                break;
            case R.id.linkdin:
                // Toast.makeText(getBaseContext(), "Linkdin Link Not Available yet", Toast.LENGTH_SHORT).show();
                break;
            case R.id.twitter:
                //Toast.makeText(getBaseContext(), "Twitter Link Not Available yet", Toast.LENGTH_SHORT).show();
                break;
            case R.id.MindMe:
                Intent intent2 = new Intent(getBaseContext(), MindMeRefer.class);
                startActivity(intent2);
        }
    }

    public void generateHashkey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    PACKAGE,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Name not found", e.getMessage(), e);

        } catch (NoSuchAlgorithmException e) {
            Log.d("Error", e.getMessage(), e);
        }
    }

}
