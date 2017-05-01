package com.mindmesolo.mindme.ContactAndLists;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by User1 on 6/24/2016.
 */
public class ContactsAndListsPagerAdapter extends FragmentStatePagerAdapter {
    public ContactsAndListsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                RecentFragment recentFragment = new RecentFragment();
                return recentFragment;
            case 1:
                GroupsFragment groupsFragment = new GroupsFragment();
                return groupsFragment;
            case 2:
                ListFragment listFragment = new ListFragment();
                return listFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
