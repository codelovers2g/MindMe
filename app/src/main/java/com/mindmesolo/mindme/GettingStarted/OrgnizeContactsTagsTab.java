package com.mindmesolo.mindme.GettingStarted;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.Contacts;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import org.json.JSONArray;

import java.util.ArrayList;
/**
 * Created by eNest on 6/8/2016.
 */

/**
 * LastUpdate by eNest on 12/9/2016.
 */

public class OrgnizeContactsTagsTab extends Fragment {

    MyCustomAdapter dataAdapter = null;

    SqliteDataBaseHelper mydbhelper;

    DataHelper dataHelper = new DataHelper();

    View view;

    Bundle holddata;

    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.organize_contact_lists, container, false);

        listView = (ListView) view.findViewById(R.id.orgnize_list_view);

        mydbhelper = new SqliteDataBaseHelper(getContext());

        holddata = savedInstanceState;

        ArrayList<Contacts> countryList = ListViewItems();

        dataAdapter = new MyCustomAdapter(getContext(), R.layout.layout_contact_row, countryList);

        listView.setAdapter(dataAdapter);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle state) {

        ArrayList<Contacts> holditems = dataAdapter.contactsList;

        super.onSaveInstanceState(state);

        state.putParcelableArrayList("savedList", holditems);
    }


    @Override
    public void onResume() {
        super.onResume();
        setupUiData(holddata);
    }


    public void setupUiData(Bundle savedInstanceState) {

        if (savedInstanceState != null) {

            ArrayList<Contacts> list = savedInstanceState.getParcelableArrayList("savedList");

            if (mydbhelper.getAllTagNames().size() > list.size()) {

                list.add(getNewListItem());

            }

            dataAdapter = new MyCustomAdapter(getContext(), R.layout.layout_contact_row, list);

            listView.setAdapter(dataAdapter);

            dataAdapter.notifyDataSetChanged();

        } else {

            ArrayList<Contacts> countryList = ListViewItems();

            dataAdapter = new MyCustomAdapter(getContext(), R.layout.layout_contact_row, countryList);

            listView.setAdapter(dataAdapter);
        }

    }

    public JSONArray getSlectedItems() {
        JSONArray jsonArray = new JSONArray();
        ArrayList<Contacts> countryList = dataAdapter.contactsList;
        for (int i = 0; i < countryList.size(); i++) {
            Contacts country = countryList.get(i);
            if (country.isSelected()) {
                String data = country.getId().toString();
                jsonArray.put(data);
            }
        }
        return jsonArray;
    }

    public ArrayList<Contacts> ListViewItems() {
        ArrayList<Contacts> countryList = new ArrayList<Contacts>();
        ArrayList name = mydbhelper.getAllTagNames();
        ArrayList lid = mydbhelper.getAllTagId();
        for (int i = 0; i < name.size(); i++) {
            String Tagname = String.valueOf(name.get(i));
            String Tagid = String.valueOf(lid.get(i));
            String allcontacts = mydbhelper.getAllContactsFromTag(Tagid);
            String count = dataHelper.getStringLength(allcontacts);
            Contacts country = new Contacts(Tagid, Tagname, count, false);
            countryList.add(country);
        }
        return countryList;

    }

    public Contacts getNewListItem() {

        ArrayList name = mydbhelper.getAllTagNames();

        ArrayList Tid = mydbhelper.getAllTagId();

        String Tagname = String.valueOf(name.get(name.size() - 1));

        String Tagid = String.valueOf(Tid.get(name.size() - 1));

        String allcontacts = mydbhelper.getAllContactsFromList(Tagid);

        String length = dataHelper.getStringLength(allcontacts);

        Contacts country = new Contacts(Tagid, Tagname, length, false);

        return country;
    }

    private class MyCustomAdapter extends ArrayAdapter<Contacts> {
        private ArrayList<Contacts> contactsList;

        public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<Contacts> countryList) {
            super(context, textViewResourceId, countryList);
            this.contactsList = new ArrayList<Contacts>();
            this.contactsList.addAll(countryList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.layout_contact_row, null);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.count = (TextView) convertView.findViewById(R.id.count);
                holder.id = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);
                holder.id.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        Contacts contacts = (Contacts) cb.getTag();
                        contacts.setSelected(cb.isChecked());
                        JSONArray arrayList = getSlectedItems();
                        SharedPreferences pref = getContext().getSharedPreferences("Importcontact", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("ListTag", arrayList.toString());
                        editor.commit();
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Contacts contact = contactsList.get(position);
            holder.name.setText(contact.getName());
            holder.count.setText(contact.getCount());
            holder.id.setChecked(contact.isSelected());
            holder.id.setTag(contact);
            return convertView;
        }

        private class ViewHolder {
            TextView name;
            TextView count;
            CheckBox id;
        }
    }
}
