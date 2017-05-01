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
public class OrgnizeContactsListTab extends Fragment {

    String id;
    MyCustomAdapter dataAdapter = null;

    SqliteDataBaseHelper mydbhelper;

    View view;

    Bundle bundle;

    DataHelper dataHelper = new DataHelper();

    ListView listView;

    String data = null;

    Bundle holddata;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.organize_contact_lists, container, false);

        mydbhelper = new SqliteDataBaseHelper(getContext());

        listView = (ListView) view.findViewById(R.id.orgnize_list_view);

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

            if (mydbhelper.getAllListNames().size() > list.size()) {

                Contacts contacts = mydbhelper.getLastCreatedList();
                if (contacts != null)
                    list.add(contacts);
                //list.add(getNewListItem());
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

    public JSONArray getSelectedItems() {

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

        ArrayList name = mydbhelper.getAllListNames();

        ArrayList lid = mydbhelper.getAllListIds();

        for (int i = 0; i < name.size(); i++) {
            String ListName = String.valueOf(name.get(i));
            String Listid = String.valueOf(lid.get(i));
            String allcontacts = mydbhelper.getAllContactsFromList(Listid);
            String count = dataHelper.getStringLength(allcontacts);
            Contacts country = new Contacts(Listid, ListName, count, false);
            countryList.add(country);
        }
        return countryList;

    }

    public Contacts getNewListItem() {

        ArrayList name = mydbhelper.getAllListNames();

        ArrayList lid = mydbhelper.getAllListIds();

        String listname = String.valueOf(name.get(name.size() - 1));

        String Listid = String.valueOf(lid.get(name.size() - 1));

        String allcontacts = mydbhelper.getAllContactsFromList(Listid);

        String length = dataHelper.getStringLength(allcontacts);

        Contacts country = new Contacts(Listid, listname, length, false);

        return country;
    }

    public class MyCustomAdapter extends ArrayAdapter<Contacts> {
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
                        JSONArray arrayList = getSelectedItems();
                        SharedPreferences pref = getContext().getSharedPreferences("Importcontact", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("ListArray", arrayList.toString());
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
