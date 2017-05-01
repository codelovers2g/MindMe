package com.mindmesolo.mindme.LoginSignup;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by User1 on 6/1/2016.
 */
public class CustomViewPagerLaunch extends FragmentStatePagerAdapter {
    final int PAGE_COUNT = 300;
    final int REAL_PAGE_COUNT = 5;

    public CustomViewPagerLaunch(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        while (position > REAL_PAGE_COUNT - 1) {
            position = position - REAL_PAGE_COUNT;
        }
        switch (position) {
            case 0:
                return new Launch1();
            case 1:
                return new Launch2();
            case 2:
                return new Launch3();
            case 3:
                return new Launch4();
            case 4:
                return new Launch5();
        }
        return null;
    }


    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}