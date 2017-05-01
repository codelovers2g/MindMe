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
public class Tags extends MainActivity {

    private static final String TAG = "Tags";

    ListView listView;

    String Storedemail, Storedpassword, StoredOrgid = null;

    SqliteDataBaseHelper dBhelper;

    String StoredContactid, TagContid;

    int Taglength;

    List<HashMap<String, String>> TagsLists;

    String name, contactid;

    MyCustomAdapter dataAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = layoutInflater.inflate(R.layout.tags, frameLayout, false);
        drawer.addView(contentView, 0);
        toggleButtonContact.setChecked(true);
        dBhelper = new SqliteDataBaseHelper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
        toggleButtonContact.setChecked(true);
        listView = (ListView) findViewById(R.id.list_view_tags);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserLogin", MODE_PRIVATE);
        Storedemail = sharedPreferences.getString("Email", null);
        Storedpassword = sharedPreferences.getString("Password", null);
        displayListView();
        //  getcontent();
        // TabclickListener();
    }

    private void displayListView() {
        ArrayList<TagDb> List = new ArrayList<TagDb>();
        final ArrayList<String> array_TagName = dBhelper.getAllTagNames();
        final ArrayList<String> arrayTagtid = dBhelper.getAllTagId();
        final ArrayList<String> arrayTagLength = dBhelper.getAllTagLength();
        for (int i = 0; i < array_TagName.size(); i++) {
            name = array_TagName.get(i);
            int len = Integer.parseInt(arrayTagLength.get(i));

            TagDb tagDb = new TagDb(name, len);
            List.add(tagDb);
        }
        dataAdapter = new MyCustomAdapter(getBaseContext(), R.layout.contactlistlayout, List);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textNum = (TextView) view.findViewById(R.id.textnum);
                String abc = textNum.getText().toString();
                if (Integer.parseInt(abc) == 0) {
                    listView.getChildAt(position).setEnabled(false);
                } else {
                    TextView textView = (TextView) view.findViewById(R.id.txt);
                    String itemName = textView.getText().toString();
                    // int TagId = position + 1;
                    String tagId = arrayTagtid.get(position);
                    TagContid = dBhelper.getAllContactsFromTag(tagId);
                    Bundle dataBundle = new Bundle();
                    dataBundle.putString("Contactid", TagContid);
                    dataBundle.putString("Name", itemName);
                    dataBundle.putString("Type", "tags");
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
        String email = Storedemail;
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
                        Log.e("VOLLEY", error.toString());
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
        TagsLists = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(responcedata);
            for (int i = 0; i < 1; i++) {
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("tags");
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1 = jsonArray.getJSONObject(j);
                        String Name = jsonObject1.getString("name");
                        JSONArray jsonArray1 = jsonObject1.getJSONArray("contactIds");
                        int len = jsonArray1.length();
                        //employeeList.add(rawdata);
                        for (int k = 0; k < 1; k++) {
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("name", "" + Name);
                            hm.put("length", "" + len);
                            TagsLists.add(hm);
                        }
                        String[] from = {"name", "length"};

                        int[] to = {R.id.txt, R.id.textnum};
                        SimpleAdapter simpleadapter = new SimpleAdapter(getApplicationContext(), TagsLists, R.layout.contactlistlayout, from, to);
                        listView.setAdapter(simpleadapter);
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //---------------------CUSTOM ADAPTER FOR LIST VIEW ----------//
    private class MyCustomAdapter extends ArrayAdapter<TagDb> {
        private ArrayList<TagDb> TagD;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<TagDb> Tag) {
            super(context, textViewResourceId, Tag);
            //super(context, textViewResourceId, List);
            this.TagD = new ArrayList<TagDb>();
            this.TagD.addAll(Tag);
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
            TagDb TagDb = TagD.get(position);
            holder.name.setText(TagDb.getName());
            holder.len.setText(Integer.toString(TagDb.getlen()));
            // holder.id.setText(contact.getid());
            return convertView;

        }

        private class ViewHolder {
            TextView name, len;
        }
    }
}
