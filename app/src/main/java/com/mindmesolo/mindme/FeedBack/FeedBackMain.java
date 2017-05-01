package com.mindmesolo.mindme.FeedBack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.mindmesolo.mindme.R;

/**
 * Created by eNest on 8/5/2016.
 */
public class FeedBackMain extends AppCompatActivity implements View.OnClickListener {

    LinearLayout ReportABug, GiveFeedBack, ReferAFriend;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.feed_feedback_main);

        ReportABug = (LinearLayout) findViewById(R.id.ReportABug);
        ReportABug.setOnClickListener(this);

        GiveFeedBack = (LinearLayout) findViewById(R.id.GiveFeedBack);
        GiveFeedBack.setOnClickListener(this);

        ReferAFriend = (LinearLayout) findViewById(R.id.ReferAFriend);
        ReferAFriend.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ReportABug:

                Intent intent = new Intent(getBaseContext(), ReportABug.class);
                startActivity(intent);
                break;
            case R.id.GiveFeedBack:
                Intent intent1 = new Intent(getBaseContext(), GiveFeedBack.class);
                startActivity(intent1);
                break;
            case R.id.ReferAFriend:
                Intent intent2 = new Intent(getBaseContext(), ReferAFriend.class);
                startActivity(intent2);

                break;

        }

    }
}
