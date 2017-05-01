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
public class OrgnizeContactsInterestsTab extends Fragment {

    MyCustomAdapter dataAdapter = null;

    String contactCode;

    SqliteDataBaseHelper mydbhelper;

    DataHelper dataHelper = new DataHelper();

    View view;

    Bundle holddata;

    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.organize_contact_lists, container, false);

        mydbhelper = new SqliteDataBaseHelper(getContext());

        listView = (ListView) view.findViewById(R.id.orgnize_list_view);

        holddata = savedInstanceState;

        ArrayList<Contacts> countryList = ListViewItems();
//        if (countryList.size()<=0) {
//            ToastShow.setText(getContext(),"There is no Interests add One By Clicking on + Icon" , Toast.LENGTH_LONG);
//        }
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

            if (mydbhelper.getAllInterestNames().size() > savedInstanceState.size()) {

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

    private ArrayList<Contacts> ListViewItems() {
        ArrayList<Contacts> countryList = new ArrayList<Contacts>();
        ArrayList<String> name = mydbhelper.getAllInterestNames();
        ArrayList<String> Id = mydbhelper.getAllInterestId();
        for (int i = 0; i < name.size(); i++) {
            String interestName = name.get(i);
            String interestId = Id.get(i);
            String count = dataHelper.getStringLength(mydbhelper.getAllContactsFromInterests(interestId));
            Contacts country = new Contacts(interestId, interestName, count, false);
            countryList.add(country);
        }
        return countryList;
    }

    public Contacts getNewListItem() {
        ArrayList name = mydbhelper.getAllInterestNames();
        ArrayList ITid = mydbhelper.getAllInterestId();
        String IntrestName = String.valueOf(name.get(name.size() - 1));
        String InterestId = String.valueOf(ITid.get(ITid.size() - 1));
        String length = dataHelper.getStringLength(mydbhelper.getAllContactsFromInterests(InterestId));
        Contacts country = new Contacts(InterestId, IntrestName, length, false);
        return country;
    }

    ;

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
                        JSONArray arrayList = getSelectedItems();
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
