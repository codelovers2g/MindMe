package com.mindmesolo.mindme.ContactAndLists;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mindmesolo.mindme.ContactAndLists.Models.SimpleContact;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by User1 on 6/8/2016.
 */
public class ContactDetail extends Fragment {

    private static final String TAG = "ContactDetail";

    String StoredContactid;

    View rootView;

    TextView ContactCompany2, ContactType2, ContactEmail, ContactPhone, ContactBirthday, ContactAddress1, ContactAddress2, ContactHome;

    TextView ContactType1, createdOn;

    LinearLayout linearLayoutType, linearLayoutCompany, linearLayoutHome, linearLayoutEmail, linearLayoutBirthday, linearLayoutAddress, linearLayoutHomepage;

    SqliteDataBaseHelper sqliteDataBaseHelper;

    ArrayList<String> contactArray;

    boolean _areFragmentLoaded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.contactdetail, container, false);
        ContactType1 = (TextView) rootView.findViewById(R.id.phoneType);
        createdOn = (TextView) rootView.findViewById(R.id.createdOn);
        ContactEmail = (TextView) rootView.findViewById(R.id.Contactemail);
        ContactCompany2 = (TextView) rootView.findViewById(R.id.Contactcompany);
        ContactType2 = (TextView) rootView.findViewById(R.id.Contacttype);
        ContactPhone = (TextView) rootView.findViewById(R.id.Contactphone);
        ContactBirthday = (TextView) rootView.findViewById(R.id.contactbirthday);
        ContactAddress1 = (TextView) rootView.findViewById(R.id.contactaddress1);
        ContactAddress2 = (TextView) rootView.findViewById(R.id.contactaddress2);
        ContactHome = (TextView) rootView.findViewById(R.id.contacthomepage);

        sqliteDataBaseHelper = new SqliteDataBaseHelper(getContext());

        Bundle extras = getActivity().getIntent().getExtras();

        StoredContactid = extras.getString("contactid");

        initLinearlayout();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        _areFragmentLoaded = false;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !_areFragmentLoaded) {
            getData();
            _areFragmentLoaded = true;
        }
    }


    private void getData() {

        contactArray = sqliteDataBaseHelper.getContactInformation(StoredContactid);

        ContactCompany2.setText(sqliteDataBaseHelper.getContactsCompanyName(StoredContactid));

        String date = sqliteDataBaseHelper.getContactCreatedon(StoredContactid);

        if (!isNullOrEmpty(date)) {
            SimpleDateFormat formatter1 = new SimpleDateFormat("MMM d, yyyy");

            try {

                Calendar calendar1 = Calendar.getInstance();

                calendar1.setTimeInMillis(Long.parseLong(date));

                createdOn.setText(formatter1.format(calendar1.getTime()));

            } catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }

        if (ContactCompany2.length() == 0 || ContactCompany2.getText().toString().equalsIgnoreCase("null")) {
            linearLayoutCompany.setVisibility(View.GONE);
        }
        ContactType2.setText(sqliteDataBaseHelper.getContactCode(StoredContactid));
        if (isNullOrEmpty(ContactType2.getText().toString())) {
            linearLayoutType.setVisibility(View.GONE);
        }

        ContactEmail.setText(sqliteDataBaseHelper.getContactemail(StoredContactid));

        if (isNullOrEmpty(ContactEmail.getText().toString())) {
            linearLayoutEmail.setVisibility(View.GONE);
        }
        if (!isNullOrEmpty(sqliteDataBaseHelper.getContactsDateOfBirth(StoredContactid))) {

            SimpleDateFormat formatter = new SimpleDateFormat("dd -MM -yyyy ");

            Calendar calendar = Calendar.getInstance();

            linearLayoutBirthday.setVisibility(View.VISIBLE);

            calendar.setTimeInMillis(Long.parseLong(sqliteDataBaseHelper.getContactsDateOfBirth(StoredContactid)));

            ContactBirthday.setText(formatter.format(calendar.getTime()));
        }

        ContactHome.setText(sqliteDataBaseHelper.getContactsSiteurl(StoredContactid));

        if (isNullOrEmpty(ContactHome.getText().toString())) {
            linearLayoutHomepage.setVisibility(View.GONE);
        }

        ContactPhone.setText(sqliteDataBaseHelper.getContactphone(StoredContactid));

        String input = sqliteDataBaseHelper.getContactsPhoneType(StoredContactid);

        if (input != null) {

            String output = input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();

            ContactType1.setText(getContactType(output));

        }
        if (ContactPhone.length() == 0 || ContactPhone.getText().toString().toLowerCase().equals("null")) {

            linearLayoutHome.setVisibility(View.GONE);
        }

        contactArray = sqliteDataBaseHelper.getContactInformation(StoredContactid);

        ContactAddress1.setText(contactArray.get(8));

        ContactAddress2.setText(contactArray.get(9));

        if (ContactAddress1.length() == 0 || ContactAddress1.getText().toString().toLowerCase().equals("null") && ContactAddress2.length() == 0 || ContactAddress2.getText().toString().toLowerCase().equals("null")) {
            linearLayoutAddress.setVisibility(View.GONE);
        }
    }

    private void getContactData() {
        SimpleContact userdata = new SimpleContact();
        userdata = sqliteDataBaseHelper.getContactDetail(StoredContactid);
        if (userdata != null) {
            ContactCompany2.setText(userdata.getCompanyName());
            if (!isNullOrEmpty(userdata.getCreatedOn())) {
                SimpleDateFormat formatter1 = new SimpleDateFormat("MMM d, yyyy");
                try {
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTimeInMillis(Long.parseLong(userdata.getCreatedOn()));
                    createdOn.setText(formatter1.format(calendar1.getTime()));
                } catch (Exception e) {
                    Log.i(TAG, e.toString());
                }
            }

        }


    }

    private String getContactType(String contactsPhoneType) {
        String phonetype = "";
        switch (contactsPhoneType.toLowerCase()) {
            case "mobile":
                phonetype = "Mobile";
                break;
            case "home":
                phonetype = "Home";
                break;
            case "default":
                phonetype = "Default";
                break;
            case "work":
                phonetype = "Work";
                break;
            case "other":
                phonetype = "Other";
                break;
        }
        return phonetype;
    }

    private void initLinearlayout() {
        linearLayoutType = (LinearLayout) rootView.findViewById(R.id.linearLayoutType);
        linearLayoutCompany = (LinearLayout) rootView.findViewById(R.id.linearLayoutCompany);
        linearLayoutHome = (LinearLayout) rootView.findViewById(R.id.linearLayoutHome);
        linearLayoutEmail = (LinearLayout) rootView.findViewById(R.id.linearLayoutEmail);
        linearLayoutBirthday = (LinearLayout) rootView.findViewById(R.id.linearLayoutBirthday);
        linearLayoutAddress = (LinearLayout) rootView.findViewById(R.id.linearLayoutAddress);
        linearLayoutHomepage = (LinearLayout) rootView.findViewById(R.id.linearLayoutHomepage);
    }

    private boolean isNullOrEmpty(String name) {
        boolean data;

        if (name.trim() == null || name.trim().equalsIgnoreCase("null")) {
            data = true;
        } else {
            data = false;
        }
        return data;
    }

}
