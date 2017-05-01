package com.mindmesolo.mindme.ContactAndLists;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
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
 * Created by User1 on 6/15/2016.
 */
public class TypeCatagories extends Fragment {

    final String TAG = "TypeCatagories";

    ListView list;
    MyCustomAdapter dataAdapter = null;
    List<HashMap<String, String>> alist;
    String[] Items = {"Lead",
            "Trial",
            "Customer",
            "Personal",
            "Inactive",
            "Cancelled"};
    String name;
    SqliteDataBaseHelper dBhelper;
    String StoredContactid;
    ArrayList<Contacts> listNew = null;
    String Storedemail, Storedpassword, jsonStr, StoredOrgid;

    IFragmentToActivity2 mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list, container, false);

        list = (ListView) rootView.findViewById(R.id.listView);

        dBhelper = new SqliteDataBaseHelper(getContext());

        Bundle extras = getActivity().getIntent().getExtras();

        StoredContactid = extras.getString("contactid");

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserLogin", Context.MODE_PRIVATE);

        Storedemail = sharedPreferences.getString("Email", null);

        Storedpassword = sharedPreferences.getString("Password", null);

        setupListView();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (IFragmentToActivity2) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement IFragmentToActivity");
        }
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    public void setupListView() {
        ArrayList<Contacts> List = new ArrayList<Contacts>();

        for (int i = 0; i < Items.length; i++) {

            name = Items[i];

            Boolean check = false;

            String type = dBhelper.getContactCode(StoredContactid);

            if (type.equals(name.toUpperCase())) {

                check = true;
            } else {
                check = false;
            }
            Contacts type1 = new Contacts(name, check);
            List.add(type1);
        }

        dataAdapter = new MyCustomAdapter(getContext(), R.layout.layout_contact_row, List);
        list.setAdapter(dataAdapter);
    }

    private void updateContactCode(String item) {
        SharedPreferences pref = getContext().getSharedPreferences("Organization", Context.MODE_PRIVATE);
        StoredOrgid = pref.getString("OrgId", null);
        String orgid = StoredOrgid;
        String email = Storedemail;
        String password = Storedpassword;
        String contid = StoredContactid;
        String token = email + ":" + password;
        byte[] data = null;
        try {
            data = token.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        final String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        final String finalToken = base64.replace("\n", "");
        JSONArray jsonArrayAddress = new JSONArray();
        try {
            JSONObject list1 = new JSONObject();
            list1.put("addressLine1", dBhelper.getContactaddress1(StoredContactid));
            list1.put("addressLine2", dBhelper.getContactaddress2(StoredContactid));
            list1.put("addressLine3", dBhelper.getContactaddress3(StoredContactid));
            list1.put("addressLine4", dBhelper.getContactaddress4(StoredContactid));
            list1.put("city", dBhelper.getContactaddress3(StoredContactid));
            list1.put("country", dBhelper.getContactaddress4(StoredContactid));
            list1.put("postalCode", dBhelper.getContactpincode(StoredContactid));
            jsonArrayAddress.put(list1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jsonArrayphone = new JSONArray();
        try {
            JSONObject list2 = new JSONObject();
            list2.put("phoneNumber", dBhelper.getContactphone(StoredContactid));
            list2.put("TV_phoneType", dBhelper.getContactsPhoneType(StoredContactid));
            jsonArrayphone.put(list2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        try {
            long timeNow = System.currentTimeMillis();
            jsonObject.put("updatedOn", String.valueOf(timeNow));
            jsonObject.put("createdOn", dBhelper.getContactCreatedon(contid));
            jsonObject.put("photo", dBhelper.getContactImage(contid));
            jsonObject.put("addresses", jsonArrayAddress);
            jsonObject.put("companyName", dBhelper.getContactsCompanyName(contid));
            jsonObject.put("emailAddress", dBhelper.getContactemail(contid));
            jsonObject.put("firstName", dBhelper.getContactFnameOfContact(contid));
            jsonObject.put("middleName", dBhelper.getContactMname(contid));
            jsonObject.put("lastName", dBhelper.getContactLname(contid));
            jsonObject.put("siteUrl", dBhelper.getContactsSiteurl(contid));
            jsonObject.put("phones", jsonArrayphone);
            jsonObject.put("facebookUrl", "");
            jsonObject.put("linkedInUrl", "");
            jsonObject.put("twitterUrl", "");
            jsonObject.put("contactCode", item.toUpperCase());
            jsonObject.put("favorite", dBhelper.getContactFavorite(contid));
            jsonObject.put("id", contid);
            jsonObject.put("status", "ACTIVE");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonStr = jsonObject.toString();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        final String ADD_INTEREST_URL = OrganizationModel.getApiBaseUrl() + orgid + "/contacts/" + contid + "";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, ADD_INTEREST_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        final String responseData = response.toString();
                        handleresponcecontact(responseData);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY", error.toString());
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return jsonStr == null ? null : jsonStr.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            jsonStr, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Basic " + finalToken);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void handleresponcecontact(String responcedata) {

        try {
            JSONObject jsonobject = new JSONObject(responcedata);
            {
                String createdon = jsonobject.getString("createdOn");
                String updatedon = jsonobject.getString("updatedOn");
                String firstname = jsonobject.getString("firstName");
                String middlename = jsonobject.getString("middleName");
                String lastname = jsonobject.getString("lastName");
                String skypename = jsonobject.getString("skypeUserName");
                String gender = jsonobject.getString("gender");
                String dateofbirth = jsonobject.getString("dateOfBirth");
                String emailaddress = jsonobject.getString("emailAddress");
                String contactid = jsonobject.getString("id");
                String status = jsonobject.getString("status");
                String companyname = jsonobject.getString("companyName");
                String siteurl = jsonobject.getString("siteUrl");
                String fburl = jsonobject.getString("facebookUrl");
                String liurl = jsonobject.getString("linkedInUrl");
                String photo = jsonobject.getString("photo");
                String twurl = jsonobject.getString("twitterUrl");
                String contactcode = jsonobject.getString("contactCode");
                String contactoptin = jsonobject.getString("contactOptIn");
                String orgid = jsonobject.getString("orgId");
                String fav = jsonobject.getString("favorite");
                String address1 = null, address2 = null, address3 = null, address4 = null, pin = null, phone = null, phoneType = null;
                JSONArray jsonArrayNotes = jsonobject.getJSONArray("notes");
                JSONArray jsonArray1 = jsonobject.getJSONArray("addresses");
                if (jsonArray1 == null) {
                    address1 = "";
                    address2 = "";
                    address3 = "";
                    address4 = "";
                    pin = "";
                } else {
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                        address1 = jsonObject1.getString("addressLine1");
                        address2 = jsonObject1.getString("addressLine2");
                        address3 = jsonObject1.getString("addressLine3");
                        address4 = jsonObject1.getString("addressLine4");
                        pin = jsonObject1.getString("postalCode");
                    }
                }
                JSONArray jsonArray2 = jsonobject.getJSONArray("phones");
                for (int i = 0; i < jsonArray2.length(); i++) {
                    JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                    phone = jsonObject2.getString("phoneNumber");
                    phoneType = jsonObject2.getString("TV_phoneType");
                }

                dBhelper.updateContact(createdon, updatedon, firstname, middlename, lastname, skypename, gender, dateofbirth, emailaddress, contactid, status, companyname,
                        siteurl, fburl, liurl, photo, twurl, contactcode, contactoptin, orgid, fav, address1, address2, address3, address4, pin, phone, jsonArrayNotes.toString(), phoneType);


            }
        } catch (Exception error) {

            Log.e(TAG, "VOLLEY----->" + error.toString());
        }
    }

    private class MyCustomAdapter extends ArrayAdapter<Contacts> {
        private ArrayList<Contacts> contactsList;

        public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<Contacts> countryList) {
            super(context, textViewResourceId, countryList);
            this.contactsList = new ArrayList<Contacts>();
            this.contactsList.addAll(countryList);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
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
                        if (cb.isChecked()) {
                            uncheckAll();
                            notifyDataSetChanged();
                            contacts.setSelected(cb.isChecked());
                            updateContactCode(Items[position].toUpperCase());
                            mCallback.changeContactType(Items[position].toUpperCase());
                        } else {
                            cb.setChecked(true);
                        }
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

        private void uncheckAll() {
            for (int i = 0; i < contactsList.size(); i++) {
                Contacts item = contactsList.get(i);
                if (item.isSelected()) {
                    item.setSelected(false);
                }
            }
        }

        private class ViewHolder {
            TextView name;
            CheckBox id;
        }
    }
}