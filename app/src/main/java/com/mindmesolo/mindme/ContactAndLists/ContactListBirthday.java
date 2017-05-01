package com.mindmesolo.mindme.ContactAndLists;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mindmesolo.mindme.ContactAndLists.Adapters.ContactViewAdapter;
import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.Contacts;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import java.util.ArrayList;

/**
 * Created by User1 on 4/20/2017.
 */
public class ContactListBirthday extends MainActivity {

    ListView listView;

    SqliteDataBaseHelper dBhelper;
    ContactViewAdapter contactsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.contactlistview, frameLayout, false);
        drawer.addView(contentView, 0);

        dBhelper = new SqliteDataBaseHelper(this);

        toggleButtonContact.setChecked(true);

        EditText searchView = (EditText) findViewById(R.id.searchview);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contactsAdapter.filter(searchView.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        TextView textView = (TextView) findViewById(R.id.toolbartext);

        String title = getIntent().getStringExtra("ContactType");

        if (title != null) {

            textView.setText(title);


        }
        DisplayListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        toggleButtonContact.setChecked(true);
        DisplayListView();
    }

    private void DisplayListView() {

        String title = getIntent().getStringExtra("ContactType");

        listView = (ListView) findViewById(R.id.listView);

        ArrayList<Contacts> array_list = dBhelper.getBirthdayContactsBetweenTime(title);

        if (array_list.size() > 0) {
            contactsAdapter = new ContactViewAdapter(getBaseContext(), R.layout.recentlistlayout, array_list);
            listView.setAdapter(contactsAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle dataBundle = new Bundle();
                    dataBundle.putString("contactid", contactsAdapter.contactsList.get(position).getId());
                    Intent intent = new Intent(getBaseContext(), Summary.class);
                    intent.putExtras(dataBundle);
                    startActivity(intent);
                }
            });
        }
    }
}
