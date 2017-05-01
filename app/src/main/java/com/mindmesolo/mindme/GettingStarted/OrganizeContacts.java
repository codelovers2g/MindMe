package com.mindmesolo.mindme.GettingStarted;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mindmesolo.mindme.ContactAndLists.AddInterest;
import com.mindmesolo.mindme.ContactAndLists.AddLists;
import com.mindmesolo.mindme.ContactAndLists.AddTag;
import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.OrganizationModel;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.Services.VolleyApi;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.ToastShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by eNest on 6/8/2016.
 */
public class OrganizeContacts extends AppCompatActivity implements View.OnClickListener {

    TextView textView3;

    Button ImportContactnew;

    ImageButton fab;

    String SELECTED_CONTACTS_DATA;
    SqliteDataBaseHelper dBhelper = new SqliteDataBaseHelper(this);
    DataHelper dataHelper = new DataHelper();
    Dialog popupDialog;
    String count;
    SqliteDataBaseHelper sqliteDataBaseHelper;
    VolleyResponseResult mResultCallback = null;
    VolleyService mVolleyService;
    OrganizeContactsPagerAdapter organizeContactsPagerAdapter;
    private ProgressDialog pDialog;
    private String TAG = "OrganizeContacts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organize_contact);
        mVolleyService = new VolleyService(mResultCallback, this);
        sqliteDataBaseHelper = new SqliteDataBaseHelper(this);
        SetUpUi();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void SetUpUi() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        textView3 = (TextView) findViewById(R.id.textView3);

        ImportContactnew = (Button) findViewById(R.id.ImportContactnew);

        ImportContactnew.setOnClickListener(this);

        fab = (ImageButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        tabLayout.addTab(tabLayout.newTab().setText("Lists"));

        tabLayout.addTab(tabLayout.newTab().setText("Interests"));

        tabLayout.addTab(tabLayout.newTab().setText("Tags"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        organizeContactsPagerAdapter = new OrganizeContactsPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(organizeContactsPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        Intent intent = getIntent();

        SELECTED_CONTACTS_DATA = intent.getStringExtra("myjsonarray");
        try {
            JSONArray jsonArray = new JSONArray(SELECTED_CONTACTS_DATA);
            count = String.valueOf(jsonArray.length());
            textView3.setText(count);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayList List = dBhelper.getAllListNames();
        ArrayList Tag = dBhelper.getAllTagNames();
        ArrayList Interests = dBhelper.getAllInterestNames();
        if ((List.size() == 0) && (Tag.size() == 0) && (Interests.size() == 0)) {
            ToastShow.setText(getBaseContext(), "You need to add a List, Interest, or Tag to organize your contacts. Create one now.", Toast.LENGTH_LONG);
        }
    }

    // add list interest tags.
    private void PopupForAddingNewListItems() {

        popupDialog = new Dialog(this);

        popupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        popupDialog.setContentView(R.layout.organize_contact_dialog);

        TextView newList = (TextView) popupDialog.findViewById(R.id.newList);
        newList.setOnClickListener(this);

        TextView newInterest = (TextView) popupDialog.findViewById(R.id.newInterest);
        newInterest.setOnClickListener(this);

        TextView newTag = (TextView) popupDialog.findViewById(R.id.newTag);
        newTag.setOnClickListener(this);

        TextView cancel = (TextView) popupDialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        popupDialog.show();
    }

    // default method for on click listener
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ImportContactnew:
                ImportContactnew.setEnabled(false);
                checkSelectedGroup();
                break;
            case R.id.fab:
                PopupForAddingNewListItems();
                break;
            case R.id.cancel:
                popupDialog.dismiss();
                break;
            case R.id.newTag:
                Intent InsertTag = new Intent(getApplicationContext(), AddTag.class);
                startActivity(InsertTag);
                popupDialog.dismiss();
                break;
            case R.id.newInterest:
                Intent insertInterest = new Intent(getApplicationContext(), AddInterest.class);
                startActivity(insertInterest);
                popupDialog.dismiss();
                break;
            case R.id.newList:
                Intent insertList = new Intent(getApplicationContext(), AddLists.class);
                startActivity(insertList);
                popupDialog.dismiss();
                break;
        }

    }

    private void checkSelectedGroup() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Importcontact", MODE_PRIVATE);
        String List = pref.getString("ListArray", null);//GET ALL SELECTED LIST FROM LIST TAB
        String Tag = pref.getString("ListTag", null);//GET ALL TAG FROM TAG TAB
        String Interest = pref.getString("InterestsArray", null);//GET ALL INTERESTS FROM INTEREST TAB
        if ((List == null) && (Tag == null) && (Interest == null)) {
            ImportContactnew.setEnabled(true);
            ToastShow.setText(getBaseContext(), "Please select group first", Toast.LENGTH_LONG);
        } else {
            ImportContacts();
        }
    }

    private void ImportContacts() {

        showProgressDialog("Importing Contacts.");

        final String org_id = dBhelper.getOrganizationId();

        final String jsonStr = finalObjectForImportContacts();

        Log.i(TAG, jsonStr.toString());

        String REGISTER_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + org_id + "/contacts/import";

        VolleyApi.getInstance().postJsonObject(getBaseContext(), REGISTER_URL, jsonStr, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                ImportContactnew.setEnabled(true);
                if (result.success && result.dataIsObject()) {
                    pDialog.dismiss();
                    JSONObject response = result.getDataAsObject();
                    Log.i(TAG, "Contact Imported");
                    // sync contacts with local db
                    handleContactsResponse(response);
                    SharedPreferences DASHBOARD = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
                    String GettingStartData = DASHBOARD.getString(MainActivity.PREFS_KEY_GETTING_START, null);
                    if (GettingStartData != null) {
                        if (!GettingStartData.contains("Import Contacts")) {
                            UpdateOrganization();
                        }
                    } else {
                        //UpdateOrganization();
                    }
                    // sync contacts with local db
                    new ImportContacts(getBaseContext(), response.toString()).execute();
                    // clear sharedPreferences data
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("Importcontact", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    // show success alert
                    successAlert(count);
                } else {
                    pDialog.dismiss();
                    if (result.volleyError instanceof TimeoutError || result.volleyError instanceof NoConnectionError)
                        ToastShow.setText(getBaseContext(), "Please Check your internet Connection", Toast.LENGTH_LONG);

                    else if (result.volleyError instanceof ServerError)
                        ToastShow.setText(getBaseContext(), "oops something went wrong. try again", Toast.LENGTH_LONG);
                }
            }
        });
    }

    //update list counts
    private void handleContactsResponse(JSONObject data) {
        JSONArray contacts = new JSONArray();
        try {
            contacts = data.getJSONArray("contactIds");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Importcontact", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String List = pref.getString("ListArray", null);//GET ALL SELECTED LIST FROM LIST TAB
        String Tag = pref.getString("ListTag", null);//GET ALL TAG FROM TAG TAB
        String Interest = pref.getString("InterestsArray", null);//GET ALL INTERESTS FROM INTEREST TAB
        if (List != null) {
            ArrayList<String> ListArray = dataHelper.stringToArrayList(List);
            if (ListArray.size() > 0) {
                for (String ListId : ListArray) {
                    String ListContacts = dBhelper.getAllContactsFromList(ListId);

                    try {
                        JSONArray listContacts = new JSONArray(ListContacts);
                        for (int i = 0; i < contacts.length(); i++) {
                            listContacts.put(contacts.get(i));
                        }
                        dBhelper.updateListContactsAndLength(ListId, listContacts.toString(), listContacts.length());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (Tag != null) {
            ArrayList<String> TagArray = dataHelper.stringToArrayList(Tag);
            if (TagArray.size() > 0) {
                for (String TagId : TagArray) {
                    String TagContacts = dBhelper.getAllContactsFromTag(TagId);

                    try {
                        JSONArray tagContacts = new JSONArray(TagContacts);
                        for (int i = 0; i < contacts.length(); i++) {
                            tagContacts.put(contacts.get(i));
                        }
                        dBhelper.updateTagContactsAndLength(TagId, tagContacts.toString(), tagContacts.length());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (Interest != null) {
            ArrayList<String> InterestArray = dataHelper.stringToArrayList(Interest);
            if (InterestArray.size() > 0) {
                for (String InterestId : InterestArray) {
                    String InterestContacts = dBhelper.getAllContactsFromInterests(InterestId);
                    try {
                        JSONArray tagContacts = new JSONArray(InterestContacts);
                        for (int i = 0; i < contacts.length(); i++) {
                            tagContacts.put(contacts.get(i));
                        }
                        dBhelper.updateInterestContactsAndLength(InterestId, tagContacts.toString(), tagContacts.length());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        editor.clear();
        editor.commit();
    }

    public void UpdateOrganization() {

        final String org_id = dBhelper.getOrganizationId();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", org_id);
            jsonObject.put("properties",
                    new JSONArray().put(
                            new JSONObject().put("name", "Greeting Started")
                                    .put("value", getPropertiesObject())));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jsonStr = jsonObject.toString();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.PUT, OrganizationModel.getApiBaseUrl() + org_id,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Organization Update Success");
                        try {

                            String GettingStartData = response.getJSONArray("properties").getJSONObject(0).getString("value");
                            SharedPreferences DASHBOARD = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = DASHBOARD.edit();
                            editor.putString(MainActivity.PREFS_KEY_GETTING_START, GettingStartData);
                            editor.commit();

                            sqliteDataBaseHelper.DeleteLists();
                            sqliteDataBaseHelper.insertIntoLists(response);
                            sqliteDataBaseHelper.DeleteInterests();
                            sqliteDataBaseHelper.insertInterestsJsonObj(response);
                            sqliteDataBaseHelper.DeleteTags();
                            sqliteDataBaseHelper.insertNewTag(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        ToastShow.setText(getBaseContext(), "oops something went wrong. try again", Toast.LENGTH_LONG);
                        String json = null;
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 400:
                                    DataHelper mydatahelper = new DataHelper();
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
                headers.put("Authorization", "Basic " + GetApiAccess());
                return headers;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonArrayRequest);
    }

    private String getPropertiesObject() {

        SharedPreferences DASHBOARD = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);

        String GettingStartData = DASHBOARD.getString(MainActivity.PREFS_KEY_GETTING_START, null);

        StringBuilder stringBuilder = null;

        if (GettingStartData != null) {
            stringBuilder = new StringBuilder(GettingStartData);
            if (!GettingStartData.contains("Import Contacts")) {
                stringBuilder.append("," + "Import Contacts");
            }
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Import Contacts");
        }
        return String.valueOf(stringBuilder);
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

    private void showProgressDialog(String Message) {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(Message);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private String finalObjectForImportContacts() {

        JSONObject jsonObject1 = new JSONObject();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Importcontact", MODE_PRIVATE);
        String List = pref.getString("ListArray", null);//GET ALL SELECTED LIST FROM LIST TAB
        String Tag = pref.getString("ListTag", null);//GET ALL TAG FROM TAG TAB
        String Interest = pref.getString("InterestsArray", null);//GET ALL INTERESTS FROM INTEREST TAB

        JSONArray Contacts = null;

        JSONArray Lists = null;

        JSONArray Tags = null;

        JSONArray Interests = null;
        try {
            Contacts = new JSONArray(SELECTED_CONTACTS_DATA);
            if (List != null) {
                Lists = new JSONArray(List);
            }
            if (Tag != null) {
                Tags = new JSONArray(Tag);
            }
            if (Interest != null) {
                Interests = new JSONArray(Interest);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonObject1.put("contacts", Contacts);
            jsonObject1.put("listIds", Lists);
            jsonObject1.put("tagIds", Tags);
            jsonObject1.put("interestIds", Interests);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject1.toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Importcontact", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    private void successAlert(String count) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrganizeContacts.this);
        //alertDialog.setTitle("Congratulation");
        alertDialog.setMessage("Congratulations! You've just imported " + count + " contact.");
        alertDialog.setPositiveButton("Import More", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Searchcontact.FinishCallBack.finish();
                finish();
            }
        });
        alertDialog.setNegativeButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ImportContactType.ImportContactsCallBack.finish();
                Searchcontact.FinishCallBack.finish();
                finish();
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    public class ImportContacts extends AsyncTask<Void, String, String> {
        Context context;
        String data;

        public ImportContacts(Context context, String data) {
            this.context = context;
            this.data = data;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {

                JSONObject contacts = new JSONObject(data);

                JSONArray jsonArray = contacts.getJSONArray("contactIds");

                for (int i = 0; i < jsonArray.length(); i++) {

                    String Contactid = jsonArray.getString(i);

                    final String org_id = dBhelper.getOrganizationId();

                    final String finalToken = GetApiAccess();

                    String REGISTER_URL = OrganizationModel.getApiBaseUrl() + org_id + "/contacts/" + Contactid + "";

                    RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, REGISTER_URL,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    dBhelper.insertNewContact(response);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e(TAG, "Error while getting contacts from server");
                                    String json = null;
                                    NetworkResponse response = error.networkResponse;
                                    if (response != null && response.data != null) {
                                        switch (response.statusCode) {
                                            case 400:
                                                json = new String(response.data);
                                                json = dataHelper.trimMessage(json, "message");
                                                if (json != null) dataHelper.displayMessage(json);
                                                break;
                                        }
                                        //Additional cases
                                    }
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

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "You are at PostExecute";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }
}