package com.mindmesolo.mindme.ContactAndLists;

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
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by eNest on 6/8/2016.
 */
public class InterestCat extends Fragment {
    ListView InterestsList;
    String id;
    MyCustomAdapter dataAdapter = null;
    String contactCode;
    SqliteDataBaseHelper mydbhelper;
    ArrayList<Contacts> newarray = new ArrayList<Contacts>();
    View view;
    String listname, Listid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.organize_contact_lists, container, false);
        mydbhelper = new SqliteDataBaseHelper(getContext());
        ArrayList<Contacts> countryList = new ArrayList<Contacts>();
        ArrayList name = mydbhelper.getAllInterestNames();
        ArrayList lid = mydbhelper.getAllInterestId();
        for (int i = 0; i < name.size(); i++) {
            listname = String.valueOf(name.get(i));
            Listid = String.valueOf(lid.get(i));
            Contacts country = new Contacts(Listid, listname, id, "demo", false);
            countryList.add(country);
        }
        dataAdapter = new MyCustomAdapter(getContext(), R.layout.layout_contact_row, countryList);
        ListView listView = (ListView) view.findViewById(R.id.orgnize_list_view);
        listView.setAdapter(dataAdapter);
        return view;
    }

    //-------------- GET SELECTED ITEMS ROM LIST VIEW -------AND RETURAN A JSON ARRAY OF SELECTED ITEMS-------//
    public JSONArray getselecteditems() {
        super.onPause();// Always call the superclass method first
        String idsss = null;
        ArrayList idss;
        ArrayList<String> arraylist = new ArrayList<String>();
        JSONArray jsonArray = new JSONArray();
        ArrayList<Contacts> countryList = dataAdapter.contactsList;
        for (int i = 0; i < countryList.size(); i++) {
            Contacts country = countryList.get(i);
            if (country.isSelected()) {
                String data = country.getCode().toString();
                jsonArray.put(data);
            }
        }
        return jsonArray;
    }

    //-------------------ADDAPTER FOR GENRATING THE LIST VIEW FOR TAGS------//
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
                convertView = vi.inflate(R.layout.orgnize_contact_row, null);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.listview_item_title);
                holder.id = (CheckBox) convertView.findViewById(R.id.CheckBox01);
                convertView.setTag(holder);
                holder.id.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        Contacts contacts = (Contacts) cb.getTag();
                        contacts.setSelected(cb.isChecked());
                        JSONArray arrayList = getselecteditems();
                        SharedPreferences pref = getContext().getSharedPreferences("Importcontact", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("InterestsArray", arrayList.toString());
                        editor.commit();
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Contacts contact = contactsList.get(position);
            holder.name.setText(contact.getName());
            holder.id.setChecked(contact.isSelected());
            holder.id.setTag(contact);
            return convertView;
        }

        private class ViewHolder {
            TextView name;
            CheckBox id;
        }
    }
}
