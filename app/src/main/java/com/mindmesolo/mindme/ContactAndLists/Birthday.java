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
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User1 on 6/27/2016.
 */
public class Birthday extends MainActivity {

    ListView listView;

    String[] names =
            {
                    "Today",
                    "Tomorrow",
                    "Yesterday",
                    "This Week",
                    "Next Week",
                    "Last Week",
                    "This Month",
                    "Next Month",
                    "Last Month",
                    "All Birthday",
                    "No Birthday"
            };

    int[] length = null;

    SqliteDataBaseHelper dBhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dBhelper = new SqliteDataBaseHelper(this);

        View contentView = inflater.inflate(R.layout.birthday, frameLayout, false);
        drawer.addView(contentView, 0);

        TextView textView = (TextView) findViewById(R.id.toolbartext);
        textView.setText("Birthday");
        toggleButtonContact.setChecked(true);

        setupListView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupListView();
        toggleButtonContact.setChecked(true);
    }


    private void setupListView() {
        listView = (ListView) findViewById(R.id.listView);
        List<HashMap<String, String>> alist = new ArrayList<HashMap<String, String>>();
        for (int k = 0; k < 11; k++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("name", "" + names[k]);
            hm.put("length", "" + dBhelper.getBirthdayContactsBetweenTime(names[k]).size());
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
                    Intent intent = new Intent(getBaseContext(), ContactListBirthday.class);
                    intent.putExtra("ContactType", names[position]);
                    startActivity(intent);
                }
            }
        });
    }
}

