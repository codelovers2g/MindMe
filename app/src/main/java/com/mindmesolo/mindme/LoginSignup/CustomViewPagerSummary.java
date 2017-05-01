package com.mindmesolo.mindme.LoginSignup;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.mindmesolo.mindme.ContactAndLists.ContactDetail;
import com.mindmesolo.mindme.ContactAndLists.ContactSummary;
import com.mindmesolo.mindme.ContactAndLists.Contactcatagory;

/**
 * Created by User1 on 6/8/2016.
 */
public class CustomViewPagerSummary extends FragmentStatePagerAdapter {

    final int PAGE_COUNT = 300;
    final int REAL_PAGE_COUNT = 3;

    private SparseArray<String> mFragmentTags;

    private FragmentManager mFragmentManager;

    public CustomViewPagerSummary(FragmentManager fragmentManager) {
        super(fragmentManager);
        mFragmentManager = fragmentManager;
        mFragmentTags = new SparseArray<String>();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        if (obj instanceof Fragment) {
            // record the fragment tag here.
            Fragment f = (Fragment) obj;
            String tag = f.getTag();
            mFragmentTags.put(position, tag);
        }
        return obj;
    }


    @Override
    public Fragment getItem(int position) {

        while (position > REAL_PAGE_COUNT - 1) {
            position = position - REAL_PAGE_COUNT;
        }
        switch (position) {
            case 0:
                return new ContactSummary();

            case 1:
                return new ContactDetail();

            case 2:
                return new Contactcatagory();
        }
        return null;
    }


    @Override
    public int getCount() {
        return PAGE_COUNT;
    }


    // Don't remove we can use it later
    public Fragment getFragment(int position) {
        String tag = mFragmentTags.get(position);
        if (tag == null)
            return null;
        return mFragmentManager.findFragmentByTag(tag);
    }

    // Don't remove we can use it later
    public FragmentManager getFragmentManager() {
        return mFragmentManager;
    }

}
