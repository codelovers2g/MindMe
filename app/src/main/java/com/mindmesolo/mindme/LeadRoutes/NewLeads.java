package com.mindmesolo.mindme.LeadRoutes;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mindmesolo.mindme.ContactAndLists.ContactsAdapter;
import com.mindmesolo.mindme.ContactAndLists.Summary;
import com.mindmesolo.mindme.CreateCampaign.CreateCampaignChooseRoute;
import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.Contacts;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by User1 on 9/26/2016.
 */

public class NewLeads extends MainActivity {

    private static final String TAG = "NewLeads";

    ListView list;

    SqliteDataBaseHelper dBhelper;

    EditText searchView;

    ContactsAdapter dataAdapter = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.new_leads_layout, frameLayout, false);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.addView(contentView, 0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.VISIBLE);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        // new code
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        // changes

        drawer.addDrawerListener(toggle);
        toggle.syncState();

//      LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//      lp.setMargins(0, 0, 0, 50);

        list = (ListView) findViewById(R.id.listView);
        //list.setLayoutParams(lp);

        searchView = (EditText) findViewById(R.id.searchview);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataAdapter.filter(searchView.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        toggleButtonDashboard.setChecked(true);
        dBhelper = new SqliteDataBaseHelper(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            extras.getString("LeadContactsCount");
        }
        displayListView();
    }


    private void displayListView() {

        ArrayList<Contacts> LeadContacts = dBhelper.getContactsDetailDayWise("LEAD", 7);

        Collections.sort(LeadContacts, new CustomComparator());

        Collections.reverse(LeadContacts);

        dataAdapter = new ContactsAdapter(getBaseContext(), R.layout.recentlistlayout, LeadContacts);

        list.setAdapter(dataAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ContactId = String.valueOf(dataAdapter.contactsList.get(position).getId());
                Bundle dataBundle = new Bundle();
                dataBundle.putString("contactid", ContactId);
                Intent intent = new Intent(getBaseContext(), Summary.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contactaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            int number = dataAdapter.contactsList.size();

            if (number > 0) {

                ArrayList SelectedContacts = new ArrayList<>();
                for (Contacts Contact : dataAdapter.contactsList) {
                    SelectedContacts.add(Contact.getId());
                }
                // custom dialog
                final Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.campaigndialog);
                TextView sendCamp = (TextView) dialog.findViewById(R.id.sendCampaign);

                if (number == 1) {
                    sendCamp.setText("Send Campaign to " + number + " Contact");
                } else {
                    sendCamp.setText("Send Campaign to " + number + " Contacts");
                }
                sendCamp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getBaseContext(), CreateCampaignChooseRoute.class);
                        intent.putCharSequenceArrayListExtra("filterContacts", SelectedContacts);
                        intent.putExtra("recipientKeyNames", getRecipientKey());
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
                TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        searchView.setText("");
        searchView.clearFocus();
        displayListView();
    }

    private String getRecipientKey() {
        JSONObject ValuesObj = new JSONObject();
        try {
            ValuesObj.put("custom", new JSONArray().put("LEAD"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ValuesObj.toString();
    }

    public String GetApiAccess() {
        SharedPreferences pref1 = getBaseContext().getSharedPreferences("UserLogin", Context.MODE_PRIVATE);
        String email = pref1.getString("Email", null);
        String password = pref1.getString("Password", null);
        String token = email + ":" + password;
        byte[] data = null;
        try {
            data = token.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        final String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        final String finalToken = base64.replace("\n", "");
        return finalToken;
    }


    public class CustomComparator implements Comparator<Contacts> {
        @Override
        public int compare(Contacts o1, Contacts o2) {
            return o1.getCreateON().compareTo(o2.getCreateON());
        }
    }

}

