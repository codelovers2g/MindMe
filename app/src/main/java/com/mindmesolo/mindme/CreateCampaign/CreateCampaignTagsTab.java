package com.mindmesolo.mindme.CreateCampaign;

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
import android.widget.RelativeLayout;
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
public class CreateCampaignTagsTab extends Fragment {

    String id;
    MyCustomAdapter dataAdapter = null;
    String TotalData;
    SqliteDataBaseHelper dBhelper;
    ArrayList TempArrayList;
    View view;
    DataHelper dataHelper = new DataHelper();
    private IFragmentToActivity mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.organize_contact_lists, container, false);

        dBhelper = new SqliteDataBaseHelper(getContext());

        ListView listView = (ListView) view.findViewById(R.id.orgnize_list_view);

        ArrayList<Contacts> ContactsList = new ArrayList<Contacts>();

        ArrayList<String> name = dBhelper.getAllTagNames();

        Collections.sort(name, String.CASE_INSENSITIVE_ORDER);

        for (String TagName : name) {

            String TagId = dBhelper.getTagIdFromName(TagName);

            ArrayList<String> ListContacts = dataHelper.stringToArrayList(dBhelper.getAllContactsFromTag(TagId));

            ContactsList.add(new Contacts(String.valueOf(ListContacts.size()), TagName, ListContacts, TagId, false));
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
        TempArrayList = dataAdapter.contactsList;
        super.onSaveInstanceState(state);
        state.putStringArrayList("savedList", TempArrayList);
    }

    //-------------- GET SELECTED ITEMS ROM LIST VIEW -------AND RETURAN A JSON ARRAY OF SELECTED ITEMS-------//
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
                holder.layout = (RelativeLayout) convertView.findViewById(R.id.android_list_view_tutorial_with_example);
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
                        ArrayList SelectecItemsNames = getSelectedItemsName();
                        mCallback.getTagData(finaldata, SelectecItemsNames);
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
            holder.layout.setBackgroundColor(getResources().getColor(R.color.colorwhite));
            holder.count.setVisibility(View.VISIBLE);
            holder.id.setVisibility(View.VISIBLE);
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
            RelativeLayout layout;
            TextView name;
            TextView count;
            CheckBox id;
        }
    }

}
