package com.mindmesolo.mindme.ContactAndLists;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import java.util.ArrayList;

/**
 * Created by Ankit Chhabra on 1/29/2016.
 */
public class ListFragment extends Fragment {

    private static final String TAG = "ListFragment";

    MyCustomAdapter dataAdapter = null;

    String listContactid;

    ListView list;

    String StoredEmail, Storedpassword = null;

    SqliteDataBaseHelper dBhelper;

    String name, contactid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.list, container, false);

        list = (ListView) rootView.findViewById(R.id.listView);

        dBhelper = new SqliteDataBaseHelper(getContext());

        SharedPreferences pref = getContext().getSharedPreferences("UserLogin", Context.MODE_PRIVATE);
        StoredEmail = pref.getString("Email", null);
        Storedpassword = pref.getString("Password", null);

        displayListView();
        //getcontent();
        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        displayListView();

    }

    private void displayListView() {
        ArrayList<ListItemsModel> List = new ArrayList<ListItemsModel>();
        final ArrayList<String> array_listName = dBhelper.getAllListNames();
        final ArrayList<String> arrayListid = dBhelper.getAllListIds();
        final ArrayList<String> arrayListLength = dBhelper.getNameLength();
        for (int i = 0; i < array_listName.size(); i++) {
            name = array_listName.get(i);
            int len = Integer.parseInt(arrayListLength.get(i));
            ListItemsModel listsDB = new ListItemsModel(name, len);
            List.add(listsDB);
        }
        dataAdapter = new MyCustomAdapter(getContext(), R.layout.contactlistlayout, List);
        // Assign adapter to ListView
        list.setAdapter(dataAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView textNum = (TextView) view.findViewById(R.id.textnum);
                String abc = textNum.getText().toString();
                if (Integer.parseInt(abc) == 0) {
                    //list.getChildAt(position).setEnabled(false);
                } else {
                    TextView textView = (TextView) view.findViewById(R.id.txt);
                    String itemName = textView.getText().toString();
                    String listsId = arrayListid.get(position);
                    listContactid = dBhelper.getListContactsById(listsId);
                    Bundle dataBundle = new Bundle();
                    dataBundle.putString("Contactid", listContactid);
                    dataBundle.putString("Name", itemName);
                    dataBundle.putString("Type", "lists");
                    Intent intent = new Intent(getContext(), ContactView.class);
                    intent.putExtras(dataBundle);
                    startActivity(intent);

                }
            }
        });
    }

    //---------------------CUSTOM ADAPTER FOR LIST VIEW ----------//
    private class MyCustomAdapter extends ArrayAdapter<ListItemsModel> {
        private ArrayList<ListItemsModel> ListD;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<ListItemsModel> List) {
            super(context, textViewResourceId, List);
            this.ListD = new ArrayList<ListItemsModel>();
            this.ListD.addAll(List);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.contactlistlayout, null);

                holder = new ViewHolder();

                holder.name = (TextView) convertView.findViewById(R.id.txt);
                holder.len = (TextView) convertView.findViewById(R.id.textnum);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ListItemsModel listsDB = ListD.get(position);
            holder.name.setText(listsDB.getName());
            holder.len.setText(Integer.toString(listsDB.getlen()));
            return convertView;

        }

        private class ViewHolder {
            TextView name, len;

        }
    }
}
