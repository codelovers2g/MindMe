package com.mindmesolo.mindme.GettingStarted;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * Created by User1 on 19-05-2016.
 */
public class Forwarding extends Fragment implements View.OnClickListener {

    final String TAG = "Forwarding";
    TextView mobileno;
    SwitchCompat forwarding;
    SqliteDataBaseHelper dBhelper;
    ArrayList<String> forwardingData = new ArrayList<String>();
    View rootView;
    private GreetingAndForwarding mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.forwarding, container, false);

        dBhelper = new SqliteDataBaseHelper(getContext());

        setupComponents();

        return rootView;
    }

    private void setupComponents() {

        forwardingData = dBhelper.getPhoneGreetings();

        mobileno = (TextView) rootView.findViewById(R.id.mobileno);

        forwarding = (SwitchCompat) rootView.findViewById(R.id.forwarding);
        forwarding.setOnClickListener(this);

        if (forwardingData.size() > 1) {
            try {

                boolean switchchecked = Boolean.parseBoolean(forwardingData.get(5));

                forwarding.setChecked(switchchecked);

                String MobileNumber = forwardingData.get(7);

                if (MobileNumber == null) {
                    ArrayList<String> contacts = dBhelper.getOrganizationContacts();
                    MobileNumber = contacts.get(0);
                }

                mCallback.forwardingDataFromFragment(MobileNumber, switchchecked);

                mobileno.setText(MobileNumber);
            } catch (Exception e) {
                Log.e(TAG, "Unable to get data from greetingdata maybe null ");
            }
        }
        mobileno.setOnClickListener((View v) -> {
            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.popup_list_menu);
            ListView lv = (ListView) dialog.findViewById(R.id.listView);
            ArrayList<String> contacts = dBhelper.getOrganizationContacts();
            if (contacts != null && contacts.size() > 0) {
                ArrayList<String> tempList = new ArrayList<String>();
                for (String name : contacts) {
                    if (StringUtils.isNotEmpty(name) && StringUtils.isNotBlank(name)) {
                        tempList.add(name);
                    }
                }
                contacts.clear();
                contacts.addAll(tempList);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, contacts);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mobileno.setText(contacts.get(position));
                        mCallback.forwardingDataFromFragment(contacts.get(position), forwarding.isChecked());
                        dialog.dismiss();
                    }
                });
                dialog.setCancelable(true);
                dialog.setTitle("Mobile Phones");
                dialog.show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forwarding:
//                if (forwarding.isChecked()) {
//                    dBhelper.updatePhoneFarwordings("true");
//                } else {
//                    dBhelper.updatePhoneFarwordings("false");
//                }
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (GreetingAndForwarding) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IFragmentToActivity");
        }
    }

}
