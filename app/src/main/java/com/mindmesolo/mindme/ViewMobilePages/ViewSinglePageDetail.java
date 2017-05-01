package com.mindmesolo.mindme.ViewMobilePages;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.mindmesolo.mindme.CreateMobilePages.CreateMobilePage;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.ViewMobilePages.Fragments.MobilePageDetail1;
import com.mindmesolo.mindme.ViewMobilePages.Fragments.MobilePageDetail2;
import com.mindmesolo.mindme.ViewMobilePages.Helper.ViewMobilePagesModel;
import com.mindmesolo.mindme.ViewMobilePages.adapter.MobilePagesAdapter;

import java.util.ArrayList;

/**
 * Created by pc-14 on 3/17/2017.
 */
public class ViewSinglePageDetail extends AppCompatActivity {

    private static final String TAG = "ViewSinglePageDetail";
    private static String pageId;
    MobilePagesAdapter adapter;
    TextView textViewActivityTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.greeting_and_forwarding);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        textViewActivityTitle = (TextView) findViewById(R.id.textViewActivityTitle);
        textViewActivityTitle.setText("Mobile page");

        TabLayout tablayout = (TabLayout) findViewById(R.id.Greetingtab_layout);

        ArrayList<String> tabs = new ArrayList<>();

        tabs.add("Preview");

        tabs.add("Details");

        final ViewPager viewPager = (ViewPager) findViewById(R.id.Greetingpager);

        adapter = new MobilePagesAdapter(getSupportFragmentManager(), tabs);

        viewPager.setAdapter(adapter);

        tablayout.setupWithViewPager(viewPager);

        ViewMobilePagesModel viewMobilePagesModel = getIntent().getParcelableExtra("PageModel");
        if (viewMobilePagesModel != null) {
            textViewActivityTitle.setText(viewMobilePagesModel.getMobilePageName());
            pageId = viewMobilePagesModel.getMobilePageId();

            sendDataToFragments(viewMobilePagesModel);
        }
    }

    private void sendDataToFragments(ViewMobilePagesModel viewMobilePagesModel) {
        MobilePageDetail1 mobilePageDetail1 = new MobilePageDetail1();
        if (mobilePageDetail1 != null) {
            mobilePageDetail1.setData(viewMobilePagesModel);
        } else {
            Log.i(TAG, "mobilePageDetail1 --->not available");
        }
        MobilePageDetail2 mobilePageDetail2 = new MobilePageDetail2();
        if (mobilePageDetail2 != null) {
            mobilePageDetail2.setData(viewMobilePagesModel);
        } else {
            Log.i(TAG, "mobilePageDetail2 --->not available");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.routemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                startActivity(new Intent(ViewSinglePageDetail.this, CreateMobilePage.class).putExtra("EditMobilePageId", pageId));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
