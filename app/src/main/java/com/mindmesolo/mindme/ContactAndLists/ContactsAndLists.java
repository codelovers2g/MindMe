package com.mindmesolo.mindme.ContactAndLists;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mindmesolo.mindme.GettingStarted.ImportContactType;
import com.mindmesolo.mindme.GettingStarted.TrainingAndSupport;
import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.OrganizationModel;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.Services.VolleyApi;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User1 on 6/24/2016.
 */
public class ContactsAndLists extends MainActivity {

    public static final int RESULT_PICK_CONTACT = 255;
    public static final int SELECT_CONTACT_TYPE = 256;
    private static final int REQUEST_PERMISSIONS = 20;
    private static final String TAG = "ContactsAndLists";
    static String ContactType;
    ContactsAndListsPagerAdapter contactsAndListsPagerAdapter;

    SqliteDataBaseHelper mydbhelper;

    DataHelper myDataHelper = new DataHelper();
    int index;
    ViewPager viewPager;
    TabLayout tablayout;
    private SparseIntArray mErrorString;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        pDialog = new ProgressDialog(this);

        mydbhelper = new SqliteDataBaseHelper(this);

        View contentView = inflater.inflate(R.layout.contactsandlists, frameLayout, false);
        drawer.addView(contentView, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        // new code
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        // changes


        mErrorString = new SparseIntArray();

        toggle.syncState();

        viewPager = (ViewPager) findViewById(R.id.contactViewPager);

        toggleButtonContact.setChecked(true);

        tablayout = (TabLayout) findViewById(R.id.contactTablayout);

        tablayout.addTab(tablayout.newTab().setText("Recent"));

        tablayout.addTab(tablayout.newTab().setText("Groups"));

        tablayout.addTab(tablayout.newTab().setText("Lists"));

        viewPager = (ViewPager) findViewById(R.id.contactViewPager);

        getOrganizationContacts();

        loadViewPager();
    }

    private void loadViewPager() {
        contactsAndListsPagerAdapter = new ContactsAndListsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(contactsAndListsPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                index = viewPager.getCurrentItem();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        toggleButtonContact.setChecked(true);
        handleDashBoardNotifications();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                popupMenu();
                break;
            case R.id.Training:
                Intent intent = new Intent(ContactsAndLists.this, TrainingAndSupport.class);
                intent.putExtra("URL", "http://support.mindmesolo.com/#!/ContactsLists");
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void popupMenu() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailoglayoutcontacts);
        TextView newContact = (TextView) dialog.findViewById(R.id.newContact);
        newContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(getBaseContext(), AddContact.class);
                startActivity(intent);
            }
        });
        TextView newList = (TextView) dialog.findViewById(R.id.newList);
        newList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(getBaseContext(), AddLists.class);
                startActivity(intent);
            }
        });

        TextView importContact = (TextView) dialog.findViewById(R.id.ImportContact);
        importContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(ContactsAndLists.this, ImportContactType.class);
                intent.putExtra("NextActivity", "CONTACTSANDLIST");
                startActivityForResult(intent, SELECT_CONTACT_TYPE);
            }
        });

        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
                case SELECT_CONTACT_TYPE:
                    Bundle res = data.getExtras();
                    ContactType = res.getString("ImportContactType");
                    requestAppPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, R.string.runtime_permissions_txt
                            , REQUEST_PERMISSIONS);
            }
        } else {
            Log.d(TAG, "FIRST --->result:" + "Sorry no data Available Pick Another Contact or Try Again");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            onPermissionsGranted(requestCode);
        } else {
            Snackbar.make(findViewById(android.R.id.content), mErrorString.get(requestCode),
                    Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            startActivity(intent);
                        }
                    }).show();
        }
    }

    public void requestAppPermissions(final String[] requestedPermissions,
                                      final int stringId, final int requestCode) {
        mErrorString.put(requestCode, stringId);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale) {
                Snackbar.make(findViewById(android.R.id.content), stringId,
                        Snackbar.LENGTH_INDEFINITE).setAction("GRANT",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(ContactsAndLists.this, requestedPermissions, requestCode);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
            }
        } else {
            onPermissionsGranted(requestCode);
        }
    }

    private void onPermissionsGranted(int requestCode) {
        if (requestCode == 20) {
            Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
        }
    }

    private void contactPicked(Intent data) {
        JSONObject APIFinalJsonObject = new JSONObject();
        JSONArray ContactPhoneArray = new JSONArray();
        JSONObject ContactPhoneObject = new JSONObject();
        String EmailAddr = null;
        Cursor phones = null;
        Uri uri = data.getData();
        phones = getContentResolver().query(uri, null, null, null, null);
        if (phones != null) {
            while (phones.moveToNext()) {
                String id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Cursor emailCur = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                while (emailCur.moveToNext()) {
                    String tempemail = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    if (myDataHelper.isValidEmail(tempemail)) {
                        EmailAddr = tempemail;
                    }
                }
                emailCur.close();
                try {
                    JSONObject ContactJsonObject = new JSONObject();
                    ContactJsonObject.put("createdOn", "");
                    ContactJsonObject.put("firstName", name);
                    ContactPhoneObject.put("phoneNumber", phoneNumber);
                    ContactPhoneObject.put("TV_phoneType", "MOBILE");
                    ContactPhoneArray.put(ContactPhoneObject);
                    ContactJsonObject.put("phones", ContactPhoneArray);
                    ContactJsonObject.put("emailAddress", EmailAddr);
                    ContactJsonObject.put("contactCode", ContactType);
                    ContactJsonObject.put("orgId", mydbhelper.getOrganizationId());
                    JSONArray JsonContactsArray = new JSONArray();
                    JsonContactsArray.put(ContactJsonObject);
                    APIFinalJsonObject.put("contacts", JsonContactsArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new ImportContact(getBaseContext()).execute(APIFinalJsonObject);
                EmailAddr = null;
            }
        } else {
            Log.e(TAG, "Cursor close 1----------------");
        }
        phones.close();

    }

    // Get Organization Contacts from Server
    private void getOrganizationContacts() {

        Log.i(TAG, "Organization Contacts from server");
        Log.i(TAG, mydbhelper.getOrganizationId());


        String Fetch_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + mydbhelper.getOrganizationId() + "/contacts?code=ACTIVE&size=10&sortBy=date&asc=false";

        VolleyApi.getInstance().getJsonArray(getBaseContext(), Fetch_URL, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                if (result.success && result.dataIsArray()) {
                    JSONArray response = result.getDataAsArray();
                    try {
                        for (int j = 0; j < response.length(); j++) {
                            mydbhelper.insertNewContact(response.getJSONObject(j));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "VOLLEY  contacts:---->" + e.toString());
                    }
                }
            }
        });
    }

    public class ImportContact extends AsyncTask<JSONObject, String, String> {

        DataHelper dataHelper = new DataHelper();
        String jsonStr, finalToken = null;
        Context context;

        public ImportContact(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Importing Contact...");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(JSONObject... params) {
            JSONObject object = params[0];
            finalToken = GetApiAccess();
            String org_id = mydbhelper.getOrganizationId();
            jsonStr = object.toString();
            String Fetch_URL = OrganizationModel.getApiBaseUrl() + org_id + "/contacts/import";
            RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Fetch_URL,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            StoreDatabasevalue(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
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
                    headers.put("Authorization", "Basic " + finalToken);
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    9000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
            return org_id;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
        }

        private void StoreDatabasevalue(JSONObject jsonObject) {
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("contactIds");
                for (int i = 0; i < jsonArray.length(); i++) {
                    String Contactid = jsonArray.getString(i);
                    String org_id = mydbhelper.getOrganizationId();
                    finalToken = GetApiAccess();
                    String REGISTER_URL = OrganizationModel.getApiBaseUrl() + org_id + "/contacts/" + Contactid + "";
                    RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, REGISTER_URL,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    mydbhelper.insertNewContact(response);
                                    int CurrentFragment = viewPager.getCurrentItem();
                                    loadViewPager();
                                    viewPager.setCurrentItem(CurrentFragment);
                                    pDialog.dismiss();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
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
                    jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                            9000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(jsonObjectRequest);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
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

    }
}
