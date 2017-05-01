package com.mindmesolo.mindme.ContactAndLists;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.mindmesolo.mindme.CreateCampaign.CreateCampaignChooseRoute;
import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.Contacts;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by User1 on 7/15/2016.
 */
public class ContactView extends MainActivity {

    ListView listView;

    SqliteDataBaseHelper dBhelper;

    String name, contactid, type;

    EditText searchView;

    DataHelper dataHelper;

    ContactViewAdapter dataAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.contactlistview, frameLayout, false);
        drawer.addView(contentView, 0);

        toggleButtonContact.setChecked(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        dataHelper = new DataHelper();

        listView = (ListView) findViewById(R.id.listView);

        dBhelper = new SqliteDataBaseHelper(this);

        Bundle extras = getIntent().getExtras();

        type = extras.getString("Type");

        name = extras.getString("Name");

        contactid = extras.getString("Contactid");

        TextView textView = (TextView) findViewById(R.id.toolbartext);

        textView.setText(name);

        DisplayListView();

    }

    private void DisplayListView() {

        ArrayList<Contacts> List = dBhelper.getAllContactsBYContactIds(getArrayListOfContacts(contactid));

        dataAdapter = new ContactViewAdapter(getBaseContext(), R.layout.recentlistlayout, List);

        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    private ArrayList<String> getArrayListOfContacts(String Data) {
        ArrayList<String> Contacts = new ArrayList();
        try {
            JSONArray jsonArray = new JSONArray(Data);
            for (int i = 0; i < jsonArray.length(); i++) {
                Contacts.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Contacts;
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

    private String getRecipientKey() {
        JSONObject ValuesObj = new JSONObject();
        try {
            JSONArray ListJsonArray = new JSONArray();
            ListJsonArray.put(name.toUpperCase());
            ValuesObj.put(type, ListJsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ValuesObj.toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DisplayListView();
        searchView.setText("");
        searchView.clearFocus();
    }

}

