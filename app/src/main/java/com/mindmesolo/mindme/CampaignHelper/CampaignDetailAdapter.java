package com.mindmesolo.mindme.CampaignHelper;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.mindmesolo.mindme.ViewCampaigns.CampaignDetailTab1;
import com.mindmesolo.mindme.ViewCampaigns.CampaignDetailTab2;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by eNest on 7/28/2016.
 */
public class CampaignDetailAdapter extends FragmentStatePagerAdapter {
    private final SparseArray<WeakReference<Fragment>> instantiatedFragments = new SparseArray<>();
    private ArrayList<String> mTabHeader;

    public CampaignDetailAdapter(FragmentManager fm, ArrayList<String> tabHeader) {
        super(fm);
        this.mTabHeader = tabHeader;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                CampaignDetailTab1 tab1 = new CampaignDetailTab1();
                return tab1;
            case 1:
                CampaignDetailTab2 tab2 = new CampaignDetailTab2();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mTabHeader.size();
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
}