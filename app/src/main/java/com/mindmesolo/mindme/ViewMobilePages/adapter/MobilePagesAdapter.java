package com.mindmesolo.mindme.ViewMobilePages.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.mindmesolo.mindme.ViewMobilePages.Fragments.MobilePageDetail1;
import com.mindmesolo.mindme.ViewMobilePages.Fragments.MobilePageDetail2;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by pc-14 on 3/17/2017.
 */

public class MobilePagesAdapter extends FragmentStatePagerAdapter {

    private final SparseArray<WeakReference<Fragment>> instantiatedFragments = new SparseArray<>();
    private ArrayList<String> mTabHeader;


    public MobilePagesAdapter(FragmentManager fm, ArrayList<String> tabHeader) {
        super(fm);
        this.mTabHeader = tabHeader;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MobilePageDetail1();
            case 1:
                return new MobilePageDetail2();
        }
        return null;
    }


    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final Fragment fragment = (Fragment) super.instantiateItem(container, position);
        instantiatedFragments.put(position, new WeakReference<>(fragment));
        return fragment;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        instantiatedFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Nullable
    public Fragment getFragment(final int position) {
        final WeakReference<Fragment> wr = instantiatedFragments.get(position);
        if (wr != null) {
            return wr.get();
        } else {
            return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabHeader.get(position);
    }

    @Override
    public int getCount() {
        return mTabHeader.size();
    }

    @Override
    public int getItemPosition(Object object) {
        // POSITION_NONE makes it possible to reload the PagerAdapter
        return POSITION_NONE;
    }

}
