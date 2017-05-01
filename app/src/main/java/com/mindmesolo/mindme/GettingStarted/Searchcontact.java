package com.mindmesolo.mindme.GettingStarted;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.Contacts;
import com.mindmesolo.mindme.helper.RuntimePermissionsActivity;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.ToastShow;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

/*
 * Created by eNest on 6/4/2016
 */
public class Searchcontact extends RuntimePermissionsActivity {

    private static final String TAG = "SearchContacts";
    private static final int REQUEST_PERMISSIONS = 20;
    public static Activity FinishCallBack;
    String id;
    MyCustomAdapter dataAdapter;
    String contactCode, Orgid;
    SqliteDataBaseHelper dBhelper = new SqliteDataBaseHelper(this);
    EditText searchView;
    AppCompatCheckBox selectAllSwitch;
    ArrayList<Contacts> countryList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchcontact);
        FinishCallBack = this;
        countryList = dBhelper.getContactsNames();
        dataAdapter = new MyCustomAdapter(Searchcontact.this, R.layout.layout_contact_row, countryList);
        SetupUi();
        checkPermissions();
    }

    private void checkPermissions() {
        Searchcontact.super.requestAppPermissions(new String[]{Manifest.permission.READ_CONTACTS}, R.string.runtime_permissions_txt
                , REQUEST_PERMISSIONS);
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        //ToastShow.setText(this, "Permissions Received.", Toast.LENGTH_LONG);
        if (countryList.size() <= 0) {
            new LongRunWork(this).execute();
        }
    }

    private void SetupUi() {

        Intent intent = getIntent();
        contactCode = intent.getStringExtra("ImportContactType");
        Orgid = dBhelper.getOrganizationId();
        ListView listView = (ListView) findViewById(R.id.listView1);
        listView.setAdapter(dataAdapter);
        Button myButton = (Button) findViewById(R.id.ImportStepOne);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSelectedContacts();
            }
        });

        selectAllSwitch = (AppCompatCheckBox) findViewById(R.id.selectAllSwitch);
        selectAllSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectAllSwitch.isChecked()) {
                    for (int i = 0; i < dataAdapter.getCount(); i++) {
                        Contacts item = dataAdapter.contactsList.get(i);
                        item.setSelected(true);
                        dataAdapter.notifyDataSetChanged();
                    }
                } else {
                    for (int i = 0; i < dataAdapter.getCount(); i++) {
                        Contacts item = dataAdapter.contactsList.get(i);
                        item.setSelected(false);
                        dataAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        searchView = (EditText) findViewById(R.id.searchview);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataAdapter.filter(searchView.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //----------FUNCTION FOR GATING ALL SELECTED CONTACTS FROM LIST VIEW---//
    private void getSelectedContacts() {
        ArrayList<Contacts> countryList = dataAdapter.contactsList;
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < countryList.size(); i++) {
            Contacts country = countryList.get(i);
            if (country.isSelected()) {
                JSONObject ContactObject = new JSONObject();
                JSONObject PhoneObject = new JSONObject();
                try {
                    String Name = country.getName();
                    String Email = country.getEmail();
                    String Phone = country.getCode();
                    String PhoneType = country.getType();
                    ;

                    if (StringUtils.isNotBlank(Phone))
                        PhoneObject.put("phoneNumber", Phone);

                    if (StringUtils.isNotBlank(PhoneType))
                        PhoneObject.put("phoneType", PhoneType);

                    if (StringUtils.isNotBlank(Name))
                        ContactObject.put("firstName", Name);

                    if (StringUtils.isNotBlank(Email))
                        ContactObject.put("emailAddress", Email);

                    ContactObject.put("contactCode", contactCode);
                    ContactObject.put("orgId", Orgid);
                    ContactObject.put("phones", new JSONArray().put(PhoneObject));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(ContactObject);
            }
        }
        if (jsonArray.length() == 0) {
            ToastShow.setText(getBaseContext(), "Please select contacts", Toast.LENGTH_LONG);

        } else {
            Intent intent = new Intent(Searchcontact.this, OrganizeContacts.class);
            intent.putExtra("myjsonarray", jsonArray.toString());
            startActivity(intent);
        }
    }

    private int getSelectedContactsCount() {
        ArrayList<Contacts> countryList = dataAdapter.contactsList;
        ArrayList<Contacts> tempContacts = new ArrayList<Contacts>();
        for (int i = 0; i < countryList.size(); i++) {
            Contacts country = countryList.get(i);
            if (country.isSelected()) {
                tempContacts.add(country);
            }
        }
        return tempContacts.size();
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchView.setText("");
        searchView.clearFocus();
    }

    /* custom Adapter to handle view */
    private class MyCustomAdapter extends ArrayAdapter<Contacts> implements Filterable {
        private ArrayList<Contacts> contactsList;
        private ArrayList<Contacts> arraylist;

        public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<Contacts> countryList) {
            super(context, textViewResourceId, countryList);
            contactsList = countryList;
            this.arraylist = new ArrayList<Contacts>();
            this.arraylist.addAll(contactsList);
        }

        @Override
        public Contacts getItem(int position) {
            return contactsList.get(position);
        }

        @Override
        public int getCount() {
            return contactsList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.layout_contact_row, null);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.id = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);
                holder.id.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        Contacts contacts = (Contacts) cb.getTag();
                        contacts.setSelected(cb.isChecked());
                        if (getCount() == getSelectedContactsCount()) {
                            selectAllSwitch.setChecked(true);
                        } else {
                            selectAllSwitch.setChecked(false);
                        }
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Contacts contact = contactsList.get(position);
            holder.name.setText(contact.getName());
            // holder.id.setText(contact.getid());
            holder.id.setChecked(contact.isSelected());
            holder.id.setTag(contact);
            return convertView;
        }

        // Filter Class
        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            contactsList.clear();
            if (charText.length() == 0) {
                contactsList.addAll(arraylist);
            } else {
                for (Contacts wp : arraylist) {
                    if (wp.getName().toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        contactsList.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }

        private class ViewHolder {
            TextView name;
            CheckBox id;
        }
    }

    //-------------Async Task------------------------------//
    public class LongRunWork extends AsyncTask<Void, String, ArrayList<Contacts>> {
        ArrayList<Contacts> countryList = new ArrayList<Contacts>();
        Context context;
        String EmailAddr;
        private ProgressDialog pDialog;

        public LongRunWork(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Searchcontact.this);
            pDialog.setMessage("Updating Contact List...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected ArrayList<Contacts> doInBackground(Void... voids) {

            String ContactType = null;

            dBhelper.DeleteImportContactData();

            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            // Get Contact list from Phone
            if (phones != null) {
                Log.e(TAG, "count" + phones.getCount());
                if (phones.getCount() == 0) {
                    return countryList;
                }
                while (phones.moveToNext()) {
                    Bitmap bit_thumb = null;
                    String id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    int phoneType = phones.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));

                    switch (phoneType) {
                        case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                            ContactType = "HOME";
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                            ContactType = "MOBILE";
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                            ContactType = "WORK";
                            break;
                    }
                    //EmailAddr = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA2));
//                    String EmailAddr = getEmail(id);
//                    String image_thumb = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
//                    try {
//                        if (image_thumb != null) {
//                            bit_thumb = MediaStore.Images.Media.getBitmap(resolver, Uri.parse(image_thumb));
//                        } else {
//                            Log.e("No Image Thumb", "--------------");
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    Cursor emailCur = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (emailCur.moveToNext()) {
                        EmailAddr = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    }
                    emailCur.close();
                    Contacts country = new Contacts(phoneNumber, ContactType, name, id, EmailAddr, false);
                    countryList.add(country);

                    //public boolean importcontacts(String IMC_id, String mobile, String name, String email, String photo, String address)

                    dBhelper.importContacts(id, phoneNumber, ContactType, name, EmailAddr, "demo", "demo");
                    EmailAddr = null;
                }
            } else {
                Log.e(TAG, "Cursor close 1----------------");
            }
            phones.close();
            return countryList;
        }

        @Override
        protected void onPostExecute(ArrayList<Contacts> result) {
            super.onPostExecute(result);
            if (result.size() == 0) {
                ToastShow.setText(context, "No contacts in your contact list.", Toast.LENGTH_LONG);
                //finish();
            }
            //  ArrayList< Contacts > countryList = sqliteDataBaseHelper.getContactsNames();
            dataAdapter = new MyCustomAdapter(Searchcontact.this, R.layout.layout_contact_row, countryList);
            ListView listView = (ListView) findViewById(R.id.listView1);
            listView.setAdapter(dataAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CheckBox cBox = (CheckBox) view.findViewById(R.id.checkBox1);
                    cBox.toggle();
                }
            });
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }
}