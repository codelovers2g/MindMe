package com.mindmesolo.mindme.ContactAndLists;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ankit Chhabra on 1/29/2016.
 */
public class GroupsFragment extends Fragment {

    ListView list, list2;

    SqliteDataBaseHelper dBhelper;

    String[] names = {"All", "Leads", "Trials", "Customers", "Personal", "Inactive", "Cancelled"};

    int[] length = new int[8];

    String[] names2 = {"Favorites", "Birthdays", "Interests", "Tags"};

    int[] value2 = new int[4];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.mylist, container, false);

        list = (ListView) rootView.findViewById(R.id.listView);

        list2 = (ListView) rootView.findViewById(R.id.list_view2);

        dBhelper = new SqliteDataBaseHelper(getContext());

        LoadListView();
        return rootView;
    }

    private void LoadListView() {
        length = new int[]{
                dBhelper.numberOfRowsContact(),
                dBhelper.getContactTypelen("LEAD"),
                dBhelper.getContactTypelen("TRIAL"),
                dBhelper.getContactTypelen("CUSTOMER"),
                dBhelper.getContactTypelen("PERSONAL"),
                dBhelper.getContactTypelen("INACTIVE"),
                dBhelper.getContactTypelen("CANCELLED")};

        List<HashMap<String, String>> alist = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 7; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("name", "" + names[i]);
            hm.put("values", "" + length[i]);
            alist.add(hm);
        }

        String[] from = {"name", "values"};

        int[] to = {R.id.txt, R.id.textnum};

        SimpleAdapter adapter = new SimpleAdapter(getContext(), alist, R.layout.contactlistlayout, from, to);

        list.setAdapter(adapter);

        DataHelper.getInstance().setListViewHeightBasedOnItems(list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.textnum);
                String abc = textView.getText().toString();
                if (Integer.parseInt(abc) == 0) {
                    list.getChildAt(position).setEnabled(false);
                } else {
                    Intent intent = new Intent(getContext(), ContactOptions.class);
                    intent.putExtra("ContactCode", names[position]);
                    startActivity(intent);
                }
            }
        });
        value2 = new int[]{dBhelper.getFavlen(),
                dBhelper.getAllBirthdaylen(),
                dBhelper.getNumOfRowsInterests(),
                dBhelper.numberOfRowsTags()};

        List<HashMap<String, String>> alist2 = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 4; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("name", "" + names2[i]);
            hm.put("values", "" + value2[i]);
            alist2.add(hm);
        }

        String[] from2 = {"name", "values"};

        int[] to2 = {R.id.txt, R.id.textnum};

        SimpleAdapter adapter2 = new SimpleAdapter(getContext(), alist2, R.layout.contactlistlayout, from2, to2);
        list2.setAdapter(adapter2);
        DataHelper.getInstance().setListViewHeightBasedOnItems(list2);
        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.textnum);
                String abc = textView.getText().toString();
                if (Integer.parseInt(abc) == 0) {
                    list.getChildAt(position).setEnabled(false);
                } else {
                    if (position == 0) {
                        startActivity(new Intent(getContext(), FavContact.class));
                    } else if (position == 1) {
                        startActivity(new Intent(getContext(), Birthday.class));
                    } else if (position == 2) {
                        startActivity(new Intent(getContext(), Interest.class));
                    } else if (position == 3) {
                        startActivity(new Intent(getContext(), Tags.class));
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadListView();
    }
}