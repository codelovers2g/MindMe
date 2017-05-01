package com.mindmesolo.mindme.ContactAndLists;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.mindmesolo.mindme.ContactAndLists.Adapters.ContactViewAdapter;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.Contacts;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import java.util.ArrayList;

/**
 * Created by Ankit Chhabra on 1/29/2016.
 */
public class RecentFragment extends Fragment {
    ListView list;
    SqliteDataBaseHelper dBhelper;
    EditText searchView;
    ContactViewAdapter dataAdapter = null;
    DataHelper dataHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recent, container, false);

        dBhelper = new SqliteDataBaseHelper(getContext());

        list = (ListView) rootView.findViewById(R.id.listView);
        searchView = (EditText) rootView.findViewById(R.id.searchview);
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
        displayListView();
        return rootView;
    }

    private void displayListView() {
        ArrayList<Contacts> List = dBhelper.getRecentContacts();
        dataAdapter = new ContactViewAdapter(getContext(), R.layout.recentlistlayout, List);
        list.setAdapter(dataAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Contactid = String.valueOf(dataAdapter.contactsList.get(position).getId());
                Bundle dataBundle = new Bundle();
                dataBundle.putString("contactid", Contactid);
                Intent intent = new Intent(getContext(), Summary.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        displayListView();
        searchView.setText("");
        searchView.clearFocus();
    }
}
