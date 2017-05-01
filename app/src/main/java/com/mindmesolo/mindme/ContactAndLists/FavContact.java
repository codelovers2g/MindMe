package com.mindmesolo.mindme.ContactAndLists;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.Contacts;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import java.util.ArrayList;

/**
 * Created by User1 on 6/24/2016.
 */
public class FavContact extends MainActivity {

    ListView listView;

    SqliteDataBaseHelper dBhelper;

    ContactsAdapter dataAdapter = null;

    DataHelper dataHelper = new DataHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.contactlayout, frameLayout, false);
        drawer.addView(contentView, 0);
        toggleButtonContact.setChecked(true);
        listView = (ListView) findViewById(R.id.listView);
        dBhelper = new SqliteDataBaseHelper(this);
        TextView textView = (TextView) findViewById(R.id.toolbartext);
        textView.setText("Favorites");
        DisplayListview();
    }

    private void DisplayListview() {

        ArrayList<Contacts> List = dBhelper.getFavoriteContacts();
        dataAdapter = new ContactsAdapter(getBaseContext(), R.layout.recentlistlayout, List);
        listView.setAdapter(dataAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Contactid = String.valueOf(dataAdapter.contactsList.get(position).getId());
                Bundle dataBundle = new Bundle();
                dataBundle.putString("contactid", Contactid);
                Intent intent = new Intent(getBaseContext(), Summary.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        DisplayListview();
    }
}
