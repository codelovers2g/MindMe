package com.mindmesolo.mindme.ToolsAndSettings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mindmesolo.mindme.OrganizationModel;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.Contacts;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by enest_09 on 9/15/2016.
 */

public class ContactTypes extends AppCompatActivity {

    private static final String TAG = " ContactTypes";

    MyCustomAdapter listAdpater;

    TextView title, footerText;

    ListView list;

    DataHelper dataHelper = new DataHelper();

    List<HashMap<String, String>> alist;

    SqliteDataBaseHelper dBhelper = new SqliteDataBaseHelper(this);

    String[] Items = {"Leads",
            "Trials",
            "Customers",
            "Personal",
            "Inactive",
            "Cancelled"};

    DataHelper mydatahelper = new DataHelper();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.importtype);

        title = (TextView) findViewById(R.id.title);

        title.setText("Choose Contact Types");

        footerText = (TextView) findViewById(R.id.footerText);

        footerText.setVisibility(View.GONE);

        list = (ListView) findViewById(R.id.ListViewType);

        setUpListView();
    }

    private void setUpListView() {

        ArrayList<Contacts> ArrayList = new ArrayList<Contacts>();

        for (int i = 0; i < Items.length; i++) {
            ArrayList.add(new Contacts(Items[i], getContactsType(Items[i])));
        }
        listAdpater = new MyCustomAdapter(getBaseContext(), R.layout.layout_contact_row, ArrayList);
        list.setAdapter(listAdpater);
        DataHelper.getInstance().setListViewHeightBasedOnItems(list);
    }

    private boolean getContactsType(String item) {
//        ArrayList contacsTypes = new ArrayList();
//        String contactsData = dBhelper.getContactTypes();
//        if (contactsData != null) {
//            try {
//                JSONArray ContactsTypes = new JSONArray(contactsData);
//                for (int i = 0; i < ContactsTypes.length(); i++) {
//                    contacsTypes.add(ContactsTypes.get(i).toString());
//                }
//                if (contacsTypes.contains(item)) {
//                    return true;
//                } else {
//                    return false;
//                }
//            } catch (JSONException e) {
//                Log.e(TAG, "Contacts Types : Null Pointer Exception no item found from loacldb.");
//                return false;
//            }
//        } else {
//            Log.e(TAG, "Contacts Types : String is null");
//            return false;
//        }
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //new DownloadFilesTask(this).execute();
            //final String jsonbodydata = getJsonData();
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private String getJsonData() {
        ArrayList<Contacts> contactsTypes = listAdpater.contactsList;
        JSONObject ContactsTypesObject = new JSONObject();
        JSONArray ContactsTypes = new JSONArray();
        for (int i = 0; i < contactsTypes.size(); i++) {
            Contacts item = contactsTypes.get(i);
            if (item.isSelected()) {
                ContactsTypes.put(item.getName());
            }
        }
        try {
            ContactsTypesObject.put("contactTypes", ContactsTypes);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ContactsTypesObject.toString();
    }

    public String GetApiAccess() {
        SharedPreferences pref1 = getBaseContext().getSharedPreferences("UserLogin", Context.MODE_PRIVATE);
        String email = pref1.getString("Email", null);
        String password = pref1.getString("Password", null);
        String token = email + ":" + password;
        byte[] data = null;
        try {
            data = token.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        final String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        final String finalToken = base64.replace("\n", "");
        return finalToken;
    }

    /* custom Adapter to handle view */
    private class MyCustomAdapter extends ArrayAdapter<Contacts> implements Filterable {

        private ArrayList<Contacts> contactsList;

        public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<Contacts> countryList) {

            super(context, textViewResourceId, countryList);

            this.contactsList = new ArrayList<Contacts>();

            this.contactsList.addAll(countryList);
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
                holder.id.setClickable(false);
//                holder.id.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View v) {
//                        CheckBox cb = (CheckBox) v;
//                        Contacts contacts = (Contacts) cb.getTag();
//                        contacts.setSelected(cb.isChecked());
//
//                    }
//                });
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

    private class DownloadFilesTask extends AsyncTask<String, String, String> {

        Context context;

        public DownloadFilesTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {

            final String token = GetApiAccess();

            String org_id = dBhelper.getOrganizationId();

            final String jsonbodydata = getJsonData();

            RequestQueue requestQueue = Volley.newRequestQueue(ContactTypes.this);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, OrganizationModel.getApiBaseUrl() + org_id + "/settings",
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                String contactTypes = response.getString("contactTypes");
                                dBhelper.updateContactsTypes(contactTypes);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String json = null;
                            Log.e(TAG, "Volley updating contacts types error !");
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                switch (response.statusCode) {
                                    case 400:
                                        json = new String(response.data);
                                        json = mydatahelper.trimMessage(json, "message");
                                        if (json != null) mydatahelper.displayMessage(json);
                                        break;
                                }
                            }
                        }
                    }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() {
                    try {
                        return jsonbodydata == null ? null : jsonbodydata.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                jsonbodydata, "utf-8");
                        return null;
                    }
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", "Basic " + token);
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };
            requestQueue.add(jsonObjectRequest);
            return null;
        }

        @Override
        protected void onProgressUpdate(String... progress) {
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
}
