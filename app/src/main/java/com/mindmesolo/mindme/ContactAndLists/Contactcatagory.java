package com.mindmesolo.mindme.ContactAndLists;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mindmesolo.mindme.R;


/**
 * Created by User1 on 6/10/2016.
 */
public class Contactcatagory extends Fragment {

    String TAG = "Contactcatagory";

    TabLayout allTabs;

    IFragmentToActivity2 mCallback;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.contactcatagories, container, false);

        allTabs = (TabLayout) rootView.findViewById(R.id.catagoriestab_layout);

        allTabs.addTab(allTabs.newTab().setText("Lists"), true);
        allTabs.addTab(allTabs.newTab().setText("Interests"));
        allTabs.addTab(allTabs.newTab().setText("Tags"));
        allTabs.addTab(allTabs.newTab().setText("Type"));
        setCurrentTabFragment(0);
        allTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
                mCallback.setSubItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return rootView;
    }

    private void setCurrentTabFragment(int position) {
        switch (position) {
            case 0:
                replaceFragment(new ListsCatagories());
                break;
            case 1:
                replaceFragment(new InterestCatagories());
                break;
            case 2:
                replaceFragment(new TagCatagories());
                break;
            case 3:
                replaceFragment(new TypeCatagories());
                break;
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (IFragmentToActivity2) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement IFragmentToActivity");
        }
    }

}
