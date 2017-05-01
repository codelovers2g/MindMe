package com.mindmesolo.mindme.ContactAndLists;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by srn on 4/20/2017.
 */
public class ContactOptions extends MainActivity {

    ListView listView;

    String[] names = {"All", "Last 7 Days", "Last 30 Days", "Last 60 Days", "Last 90 Days", "Over 90 Days", "Favorites"};

    SqliteDataBaseHelper dBhelper;

    DataHelper dataHelper = new DataHelper();

    String ContactCode;

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

        ContactCode = getIntent().getStringExtra("ContactCode");

        TextView textView = (TextView) findViewById(R.id.toolbartext);
        textView.setText(ContactCode);

        setupListView();
    }

    private void setupListView() {

        // Group filter filter contact Please don't remove it
        ContactCode = dataHelper.GroupsFilter(ContactCode);

        List<HashMap<String, String>> alist = new ArrayList<>();

        for (int i = 0; i < names.length; i++) {
            HashMap<String, String> hm = new HashMap<>();
            hm.put("name", "" + names[i]);
            hm.put("length", "" + dBhelper.getContactsBetweenTimeWithContactCode(ContactCode.toUpperCase(), names[i]).size());
            alist.add(hm);
        }

        String[] from = {"name", "length"};

        int[] to = {R.id.txt, R.id.textnum};

        SimpleAdapter simpleadapter = new SimpleAdapter(getBaseContext(), alist, R.layout.contactlistlayout, from, to);
        listView.setAdapter(simpleadapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.textnum);
                String abc = textView.getText().toString();
                if (Integer.parseInt(abc) == 0) {
                    listView.getChildAt(position).setEnabled(false);
                } else {
                    String value2 = names[position];
                    Bundle dataBundle = new Bundle();
                    dataBundle.putString("ContactCode", ContactCode);
                    dataBundle.putString("ContactCreatedBetween", value2);
                    Intent intent = new Intent(getBaseContext(), ContactLists.class);
                    intent.putExtras(dataBundle);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        toggleButtonContact.setChecked(true);
        setupListView();
    }
}
