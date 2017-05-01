package com.mindmesolo.mindme.CreateCampaign;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by enest_09 on 11/11/2016.
 */

public class CallToActionAdapter extends FragmentStatePagerAdapter {

    public CallToActionAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment tabs = null;

        switch (position) {
            case 0:
                tabs = new YesNoMaybeFragment();
                break;
            case 1:
                tabs = new PollFragment();
                break;
            case 2:
                tabs = new FeedBackFragment();
                break;
            case 3:
                tabs = new LinkCallFragment();
                break;
        }
        return tabs;
    }

    @Override
    public int getCount() {
        return 4;
    }

    public int getCurrentItem() {
        return 4;
    }
}
