package com.mindmesolo.mindme.Conversation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.mindmesolo.mindme.ContactAndLists.Summary;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.Contacts;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import java.util.ArrayList;

/**
 * Created by enest_09 on 11/1/2016.
 */

public class Leads extends Fragment implements View.OnClickListener {

    final String TAG = "Conversation Leads";
    DataHelper dataHelper = new DataHelper();
    SqliteDataBaseHelper sqliteDataBaseHelper;

    View rootView;

    EditText searchView;

    ListView list;

    ChatListViewAdapter dataAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.recent, container, false);

        list = (ListView) rootView.findViewById(R.id.listView);

        searchView = (EditText) rootView.findViewById(R.id.searchview);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //dataAdapter.filter(searchView.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sqliteDataBaseHelper = new SqliteDataBaseHelper(getContext());

        displayListView();

        return rootView;
    }

    private void displayListView() {
        ArrayList<Contacts> List = new ArrayList<Contacts>();
        final ArrayList<String> array_list = sqliteDataBaseHelper.getfirstnamecontact();
        final ArrayList<String> arrayListcontid = sqliteDataBaseHelper.getcontactid();
        final ArrayList<String> arrayListImages = sqliteDataBaseHelper.getContactImage();
        for (int i = 0; i < array_list.size(); i++) {
            String name = array_list.get(i);
            String image = arrayListImages.get(i);
            Bitmap bitmap = dataHelper.decodeBase64(image);
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.contactsicon);
            }

            Contacts contacts = new Contacts(name, bitmap);
            List.add(contacts);
        }
        // dataAdapter = new ChatListViewAdapter(getContext(), R.layout.conversation_chat_row, List);
        list.setAdapter(dataAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Contactid = String.valueOf(arrayListcontid.get(position));
                Bundle dataBundle = new Bundle();
                dataBundle.putString("contactid", Contactid);
                Intent intent = new Intent(getContext(), Summary.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {


    }

    @Override
    public void onResume() {
        super.onResume();
        searchView.setText("");
        searchView.clearFocus();
    }
}
