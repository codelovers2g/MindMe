package com.mindmesolo.mindme.ViewCampaigns;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mindmesolo.mindme.ContactAndLists.Adapters.ContactViewAdapter;
import com.mindmesolo.mindme.ContactAndLists.Summary;
import com.mindmesolo.mindme.CreateCampaign.CreateCampaignChooseRoute;
import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.Contacts;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by User1 on 9/30/2016.
 */
public class CampaignContactView extends MainActivity {

    ListView list;

    String recipientKey;

    SqliteDataBaseHelper dBhelper;

    EditText searchView;

    ContactViewAdapter dataAdapter = null;

    ArrayList<String> Contactids;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.recent, frameLayout, false);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.addView(contentView, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        String type = extras.getString("tag");

        Contactids = extras.getStringArrayList("Contactids");
        recipientKey = extras.getString("recipientKeyNames");

        toolbar.setVisibility(View.VISIBLE);

        final TextView textView = (TextView) findViewById(R.id.toolbarText);
        textView.setText(type);

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

        list = (ListView) findViewById(R.id.listView);

        toggleButtonCampaing.setChecked(true);

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

        dBhelper = new SqliteDataBaseHelper(this);

        displayListView();
    }

    private void displayListView() {
        ArrayList<Contacts> List = dBhelper.getAllContactsBYContactIds(Contactids);
        dataAdapter = new ContactViewAdapter(getBaseContext(), R.layout.recentlistlayout, List);
        list.setAdapter(dataAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle dataBundle = new Bundle();
                dataBundle.putString("contactid", dataAdapter.contactsList.get(position).getId());
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
                        intent.putExtra("recipientKeyNames", getRecipientKey(recipientKey));
                        startActivity(intent);
                        searchView.setText("");
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

    private String getRecipientKey(String recipientKey) {
        JSONObject ValuesObj = null;
        try {
            JSONArray valuesArray = new JSONArray(recipientKey);

            if (valuesArray.length() > 0) {

                JSONObject recipientKeyObj = new JSONObject(String.valueOf(valuesArray.getJSONObject(0)));

                ValuesObj = new JSONObject(recipientKeyObj.getString("value"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ValuesObj.toString();
    }

    @Override
    public void onResume() {
        super.onResume();
        displayListView();
        searchView.setText("");
        searchView.clearFocus();
        toggleButtonCampaing.setChecked(true);
    }
}
