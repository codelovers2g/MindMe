package com.mindmesolo.mindme.GettingStarted;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by eNest on 6/8/2016.
 */
public class OrganizeContactsPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public OrganizeContactsPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                OrgnizeContactsListTab tab1 = new OrgnizeContactsListTab();
                return tab1;
            case 1:
                OrgnizeContactsInterestsTab tab2 = new OrgnizeContactsInterestsTab();
                return tab2;
            case 2:
                OrgnizeContactsTagsTab tab3 = new OrgnizeContactsTagsTab();
                return tab3;
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}