package com.mindmesolo.mindme.LoginSignup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mindmesolo.mindme.R;

/**
 * Created by User1 on 20-05-2016.
 */
public class Launch4 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.launch4, container, false);

        TextView Organize = (TextView) rootView.findViewById(R.id.OrganizeFooter);
        Organize.setText("Manage Your Contacts & Lists");
        return rootView;
    }
}