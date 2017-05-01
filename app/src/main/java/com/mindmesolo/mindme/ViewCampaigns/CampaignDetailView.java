package com.mindmesolo.mindme.ViewCampaigns;

/**
 * Created by eNest on 7/28/2016.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.mindmesolo.mindme.CampaignHelper.CampaignDetailAdapter;
import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import java.util.ArrayList;


public class CampaignDetailView extends MainActivity {

    SqliteDataBaseHelper mydbhelper;
    private CampaignDetailAdapter adapter;
    private TabLayout tabLayout;
    private ProgressDialog nDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.campaign_deatail_view, frameLayout, false);
        drawer.addView(contentView, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        // new code
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        // changes

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        toggleButtonCampaing.setChecked(true);

        ArrayList<String> tabs = new ArrayList<>();
        tabs.add("Last Campaign");
        tabs.add("View All");

        tabLayout = (TabLayout) findViewById(R.id.tab_layout2);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        adapter = new CampaignDetailAdapter(getSupportFragmentManager(), tabs);

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        mydbhelper = new SqliteDataBaseHelper(this);
    }

    @Override
    public void onAttachFragment(android.support.v4.app.Fragment fragment) {
        super.onAttachFragment(fragment);
    }


}

