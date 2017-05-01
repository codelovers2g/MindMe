package com.mindmesolo.mindme.ContactAndLists;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.OrganizationModel;
import com.mindmesolo.mindme.R;
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
 * Created by User1 on 6/2/2016.
 */
public class Interest extends MainActivity {
    private static final String TAG = "Interest";
    ListView list;
    CheckBox ch1;
    String StoredContactid;
    SqliteDataBaseHelper dBhelper;
    String StoredEmail, Storedpassword, StoredOrgid = null;
    List<HashMap<String, String>> InterestLists;
    TextView textView;
    String Interestid, Interestcode, Interestname, Intereststatus, InterestContid;
    int Interestlength;
    String name, contactid;
    MyCustomAdapter dataAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = layoutInflater.inflate(R.layout.interest, frameLayout, false);
        drawer.addView(contentView, 0);
        dBhelper = new SqliteDataBaseHelper(this);
        toggleButtonContact.setChecked(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
        textView = (TextView) findViewById(R.id.textView);
        list = (ListView) findViewById(R.id.list_view_interest);
        // listView1 = (ListView) findViewById(R.id.list_view_interest1);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserLogin", MODE_PRIVATE);
        StoredEmail = sharedPreferences.getString("Email", null);
        Storedpassword = sharedPreferences.getString("Password", null);
        displayListView();
    }


    private void displayListView() {
        ArrayList<Interestdb> interest = new ArrayList<Interestdb>();
        final ArrayList<String> array_InterestName = dBhelper.getAllInterestNames();
        final ArrayList<String> arrayInteresttid = dBhelper.getAllInterestId();
        final ArrayList<String> arrayInterestLength = dBhelper.getAllInterestLength();
        for (int i = 0; i < array_InterestName.size(); i++) {
            name = array_InterestName.get(i);
            int len = Integer.parseInt(arrayInterestLength.get(i));
            Interestdb interestdb = new Interestdb(name, len);
            interest.add(interestdb);
        }
        dataAdapter = new MyCustomAdapter(getBaseContext(), R.layout.contactlistlayout, interest);
        // Assign adapter to ListView
        list.setAdapter(dataAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textNum = (TextView) view.findViewById(R.id.textnum);
                String abc = textNum.getText().toString();
                if (Integer.parseInt(abc) == 0) {
                    list.getChildAt(position).setEnabled(false);
                } else {
                    TextView textView = (TextView) view.findViewById(R.id.txt);
                    String itemName = textView.getText().toString();
                    //  int interestId = position + 1;
                    String interestId = arrayInteresttid.get(position);
                    InterestContid = dBhelper.getInterestsContacts(interestId);
                    Bundle dataBundle = new Bundle();
                    dataBundle.putString("Contactid", InterestContid);
                    dataBundle.putString("Name", itemName);
                    dataBundle.putString("Type", "interests");
                    Intent intent = new Intent(getBaseContext(), ContactView.class);
                    intent.putExtras(dataBundle);
                    startActivity(intent);

                }
            }
        });
    }

    private void getcontent() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Organization", Context.MODE_PRIVATE);
        StoredOrgid = pref.getString("OrgId", null);
        String orgid = StoredOrgid;
        String email = StoredEmail;
        String password = Storedpassword;
        String token = email + ":" + password;
        byte[] data = null;
        try {
            data = token.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        final String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        final String finalToken = base64.replace("\n", "");
        String Fetch_URL = OrganizationModel.getApiBaseUrl() + orgid + "";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Fetch_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        final String responcedata = response.toString();
                        handleresponce(responcedata);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "VOLLEY------>" + error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Basic " + finalToken);
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void handleresponce(String responcedata) {
        InterestLists = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(responcedata);
            for (int i = 0; i < 1; i++) {
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("interests");
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1 = jsonArray.getJSONObject(j);
                        Interestid = jsonObject1.getString("id");
                        Interestcode = jsonObject1.getString("code");
                        Interestname = jsonObject1.getString("name");
                        Intereststatus = jsonObject1.getString("status");
                        JSONArray jsonArray1 = jsonObject1.getJSONArray("contactIds");
                        int len = jsonArray1.length();
                        //employeeList.add(rawdata);
                        for (int k = 0; k < 1; k++) {
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("name", "" + Interestname);
                            hm.put("length", "" + len);
                            InterestLists.add(hm);
                        }
                        String[] from = {"name", "length"};

                        int[] to = {R.id.txt, R.id.textnum};


                        SimpleAdapter simpleadapter = new SimpleAdapter(getApplicationContext(), InterestLists, R.layout.contactlistlayout, from, to);
                        list.setAdapter(simpleadapter);
                    }
                } catch (Exception e) {
                    Log.i(TAG, e.toString());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // getcontent();
    }

    //---------------------CUSTOM ADAPTER FOR LIST VIEW ----------//
    private class MyCustomAdapter extends ArrayAdapter<Interestdb> {
        private ArrayList<Interestdb> interestd;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Interestdb> interest) {
            super(context, textViewResourceId, interest);
            this.interestd = new ArrayList<Interestdb>();
            this.interestd.addAll(interest);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.contactlistlayout, null);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.txt);
                holder.len = (TextView) convertView.findViewById(R.id.textnum);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Interestdb interestdb = interestd.get(position);
            holder.name.setText(interestdb.getName());
            holder.len.setText(Integer.toString(interestdb.getlen()));
            return convertView;

        }

        private class ViewHolder {
            TextView name, len;

        }
    }
}

