package com.mindmesolo.mindme.CreateCampaign;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

/**
 * Created by eNest on 6/8/2016.
 */
public class CreateCampaignInterestsTab extends Fragment {
    private static final String TAG = "CreateCampaignInterestsTab";

    String id;
    MyCustomAdapter dataAdapter = null;
    String TotalData;
    SqliteDataBaseHelper dBhelper;
    ArrayList holditems;
    View view;
    DataHelper dataHelper = new DataHelper();
    private IFragmentToActivity mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.organize_contact_lists, container, false);

        dBhelper = new SqliteDataBaseHelper(getContext());

        ArrayList<Contacts> ContactsList = new ArrayList<Contacts>();

        ListView listView = (ListView) view.findViewById(R.id.orgnize_list_view);

        ArrayList<String> name = dBhelper.getAllInterestNames();

        Collections.sort(name, String.CASE_INSENSITIVE_ORDER);

        for (String InterestName : name) {

            String InterestId = dBhelper.getInterestIdFromName(InterestName);

            ArrayList<String> ListContacts = dataHelper.stringToArrayList(dBhelper.getAllInterestContactIds(InterestId));

            ContactsList.add(new Contacts(String.valueOf(ListContacts.size()), InterestName, ListContacts, InterestId, false));

        }

        if (savedInstanceState != null) {
            ArrayList list = savedInstanceState.getStringArrayList("savedList");
            dataAdapter = new MyCustomAdapter(getContext(), R.layout.layout_contact_row, list);
            listView.setAdapter(dataAdapter);
        } else {
            dataAdapter = new MyCustomAdapter(getContext(), R.layout.layout_contact_row, ContactsList);
            listView.setAdapter(dataAdapter);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        holditems = dataAdapter.contactsList;
        super.onSaveInstanceState(state);
        state.putStringArrayList("savedList", holditems);
    }

    //-------------- GET SELECTED ITEMS ROM LIST VIEW -------AND RETURAN A JSON ARRAY OF SELECTED ITEMS-------//
    public ArrayList getSelectedItems() {
        ArrayList<String> FinalContacts = new ArrayList<String>();
        for (Contacts contactObj : dataAdapter.contactsList) {
            if (contactObj.isSelected()) {
                ArrayList<String> Contacts = contactObj.getContacts();
                for (String contact : Contacts) {
                    FinalContacts.add(contact);
                }
            }
        }
        //-----LinkedHashSet is used to store listItems Without Duplicate Values ---//
        LinkedHashSet<String> uniqueFiltredContacts = new LinkedHashSet<String>();

        uniqueFiltredContacts.addAll(FinalContacts);

        FinalContacts.clear();

        FinalContacts.addAll(uniqueFiltredContacts);

        return FinalContacts;
    }

    private ArrayList getSelectedItemsName() {
        ArrayList<Contacts> countryList = dataAdapter.contactsList;
        ArrayList SelectedItems = new ArrayList();
        for (Contacts country : countryList) {
            if (country.isSelected()) {
                String itemName = country.getName().toString();
                SelectedItems.add(itemName);
            }
        }
        return SelectedItems;
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (IFragmentToActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement IFragmentToActivity");
        }
    }

    @SuppressLint("LongLogTag")
    public void fragmentCommunication() {
        Log.i(TAG, "testing call from ListTab");
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
                convertView = vi.inflate(R.layout.fragmentsrow, null);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.listview_item_title);
                holder.count = (TextView) convertView.findViewById(R.id.listview_item_count);
                holder.id = (CheckBox) convertView.findViewById(R.id.CheckBox01);
                convertView.setTag(holder);
                holder.id.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        SharedPreferences pref = getContext().getSharedPreferences("CreateCampaign", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        TotalData = pref.getString("bundledata", null);
                        editor.clear();
                        CheckBox cb = (CheckBox) v;
                        Contacts contacts = (Contacts) cb.getTag();
                        contacts.setSelected(cb.isChecked());
                        if (cb.isChecked()) {

                        }
                        ArrayList finaldata = getSelectedItems();
                        ArrayList SelectedItemsNames = getSelectedItemsName();
                        mCallback.getInterestData(finaldata, SelectedItemsNames);
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Contacts contact = contactsList.get(position);
            String name = contact.getName();
            String code = contact.getCode();
            boolean status = contact.isSelected();
            holder.name.setText(name);
            holder.count.setText(code);
            holder.id.setChecked(status);
            holder.id.setTag(contact);
            if (code.equals("0")) {
                holder.name.setEnabled(false);
                holder.id.setEnabled(false);
                holder.count.setEnabled(false);
            } else {
                holder.name.setEnabled(true);
                holder.id.setEnabled(true);
                holder.count.setEnabled(true);
            }
            return convertView;
        }

        private class ViewHolder {
            TextView name;
            TextView count;
            CheckBox id;
        }
    }
}