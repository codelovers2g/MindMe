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

public class CreateCampaignListTab extends Fragment {

    MyCustomAdapter dataAdapter = null;
    String TotalData;
    SqliteDataBaseHelper dBhelper;
    ArrayList HoldItems;
    View view;
    CheckBox CheckBox03;
    ListView listView;
    DataHelper dataHelper = new DataHelper();
    String[] Items = {"Lead",
            "Trial",
            "Customer",
            "Personal",
            "Inactive",
            "Cancelled"};

    //    ArrayList<String> Items = new ArrayList<String>();
    ArrayList<Contacts> ContactsList = new ArrayList<Contacts>();
    private IFragmentToActivity mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.create_campagin_lists, container, false);

        dBhelper = new SqliteDataBaseHelper(getContext());

        TextView TotalContacts = (TextView) view.findViewById(R.id.listview_items_count);

        TotalContacts.setText(String.valueOf(dBhelper.getAllContactsById().size()));

        CheckBox03 = (CheckBox) view.findViewById(R.id.CheckBox03);

        listView = (ListView) view.findViewById(R.id.campaign_list_view);

        ContactsList.add(new Contacts("1", "Contact Type", "0", "abc", false));
        //-------------------------list view data------------------//
        for (String ContactType : Items) {

            ArrayList<String> contactList = dBhelper.getContactsByItsContactType(ContactType.toUpperCase());

            ContactsList.add(new Contacts(String.valueOf(contactList.size()), ContactType, contactList, "", false));
        }
        ContactsList.add(new Contacts("1", "List", "0", "abc", false));

        ArrayList<String> AllListNames = dBhelper.getAllListNames();

        Collections.sort(AllListNames, String.CASE_INSENSITIVE_ORDER);

        for (String listName : AllListNames) {

            String ListId = dBhelper.getListIdByName(listName);

            ArrayList<String> ListContacts = dataHelper.stringToArrayList(dBhelper.getAllContactsFromList(ListId));

            ContactsList.add(new Contacts(String.valueOf(ListContacts.size()), listName, ListContacts, ListId, false));
        }

        if (savedInstanceState != null) {
            ArrayList list = savedInstanceState.getStringArrayList("savedList");
            dataAdapter = new MyCustomAdapter(getContext(), R.layout.layout_contact_row, list);
            listView.setAdapter(dataAdapter);
        } else {
            dataAdapter = new MyCustomAdapter(getContext(), R.layout.layout_contact_row, ContactsList);
            listView.setAdapter(dataAdapter);
        }

//--------------select All contacts------------------------------------//
        CheckBox03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox03.setSelected(CheckBox03.isChecked());
                if (CheckBox03.isChecked()) {
                    listView.setEnabled(false);
                    ArrayList arrayList = dBhelper.getAllContactsById();
                    mCallback.getAllContacts(arrayList);
                    for (Contacts contacts : dataAdapter.contactsList) {
                        contacts.setEnable(true);
                        contacts.setSelected(false);
                    }
                    dataAdapter.notifyDataSetChanged();
                } else {
                    listView.setEnabled(true);
                    ArrayList arrayList = new ArrayList();
                    mCallback.getAllContacts(arrayList);
                    for (Contacts contacts : dataAdapter.contactsList) {
                        contacts.setEnable(false);
                    }
                    dataAdapter.notifyDataSetChanged();
                    ArrayList finaldata = getSelectedItems();
                    ArrayList SelectedItemsName = getSelectedItemsName();
                    mCallback.getListData(finaldata, SelectedItemsName);
                }
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        HoldItems = dataAdapter.contactsList;
        super.onSaveInstanceState(state);
        state.putStringArrayList("savedList", HoldItems);
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
        ArrayList<String> AllSelectedItems = new ArrayList();
        for (Contacts Contact : countryList) {
            if (Contact.isSelected()) {
                AllSelectedItems.add(Contact.getName().toString());
            }
        }
        return AllSelectedItems;
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
                        ArrayList SelectedItemsName = getSelectedItemsName();
                        mCallback.getListData(finaldata, SelectedItemsName);
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Contacts contact = contactsList.get(position);
            String name = contact.getName();
            String code = contact.getCode();
            String email = contact.getEmail();
            boolean status = contact.isSelected();
            holder.name.setText(name);
            holder.count.setText(code);
            holder.id.setChecked(status);
            holder.id.setTag(contact);
            holder.layout.setBackgroundResource(R.color.colorwhite);
            holder.count.setVisibility(View.VISIBLE);
            holder.id.setVisibility(View.VISIBLE);
            if (code.equals("0") || contact.isEnable()) {
                holder.name.setEnabled(false);
                holder.id.setEnabled(false);
                holder.count.setEnabled(false);
            } else {
                holder.name.setEnabled(true);
                holder.id.setEnabled(true);
                holder.count.setEnabled(true);
            }
            String type = "abc";
            if ((type.equals(email))) {
                holder.name.setText(name);
                holder.count.setVisibility(View.GONE);
                holder.id.setVisibility(View.GONE);
                holder.layout.setBackgroundResource(R.color.colorgrey);
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
