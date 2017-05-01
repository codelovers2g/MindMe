package com.mindmesolo.mindme.Notifcations;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mindmesolo.mindme.ContactAndLists.Summary;
import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.OrganizationModel;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.RecycleViewHelper.DividerItemDecoration;
import com.mindmesolo.mindme.RecycleViewHelper.ItemTouchHelperExtension;
import com.mindmesolo.mindme.Services.VolleyApi;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.ToastShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User1 on 8/12/2016.
 */
public class Notification extends MainActivity implements DeleteListener {

    private static final String TAG = "Notification";
    public ItemTouchHelperExtension mItemTouchHelper;
    public ItemTouchHelperExtension.Callback mCallback;
    SqliteDataBaseHelper sqliteDataBaseHelper;
    TextView notificationTextView;
    DataHelper datahelper = new DataHelper();
    private ProgressDialog pDialog;
    private RecyclerView mRecyclerView;
    private MainRecyclerAdapter mAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.notification, frameLayout, false);
        drawer.addView(contentView, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


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

        drawer.addDrawerListener(toggle);

        toggle.syncState();

        notificationTextView = (TextView) findViewById(R.id.notificationTextView);

        toggleButtonNotification.setChecked(true);

        sqliteDataBaseHelper = new SqliteDataBaseHelper(getApplication().getBaseContext());


//-------------------------RecycleViewItem----------------------------//
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mCallback = new ItemTouchHelperCallback();

        mItemTouchHelper = new ItemTouchHelperExtension(mCallback);

        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        mAdapter = new MainRecyclerAdapter(this, this, mItemTouchHelper);

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this));

        handleResponseNotification();

        getNotifications();
    }

    @Override
    protected void onResume() {
        super.onResume();
        toggleButtonNotification.setChecked(true);
        handleResponseNotification();
    }

    private void showProgressDialog(String Message) {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(Message);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void getNotifications() {

        showProgressDialog("Getting Notification.");

        String org_id = new SqliteDataBaseHelper(getBaseContext()).getOrganizationId();

        String Fetch_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + org_id + "/notifications/info";

        VolleyApi.getInstance().getJsonArray(getBaseContext(), Fetch_URL, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                if (result.success && result.dataIsArray()) {
                    JSONArray jsonArray = result.getDataAsArray();
                    try {
                        for (int j = 0; j < jsonArray.length(); j++) {
                            if (jsonArray.length() == sqliteDataBaseHelper.getNotifications().size()) {
                                break;
                            }
                            sqliteDataBaseHelper.insertNotification(jsonArray.getJSONObject(j));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Notification inserting:---->" + e.toString());
                    }
                    handleResponseNotification();
                }
            }
        });
    }

    private void handleResponseNotification() {

        ArrayList<NotificationModal> notificationLists = sqliteDataBaseHelper.getNotifications();

        if (notificationLists.size() > 0) {

            mRecyclerView.setVisibility(View.VISIBLE);

            notificationTextView.setVisibility(View.GONE);

        } else {

            mRecyclerView.setVisibility(View.GONE);

            notificationTextView.setVisibility(View.VISIBLE);
        }

        Collections.sort(notificationLists, new CustomComparator());

        Collections.reverse(notificationLists);

        mAdapter.updateData(notificationLists);
    }

    // mark notification as read
    private void markAsRead(final NotificationModal item) {
        String org_id = new SqliteDataBaseHelper(getBaseContext()).getOrganizationId();
        String Fetch_URL = OrganizationModel.getApiBaseUrl() + org_id + "/contacts/" + item.getContactId() + "/notification/" + item.getNotificationId() + "";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, Fetch_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String read = response.getString("read");
                            sqliteDataBaseHelper.updateReadMessage(read, item.getNotificationId());
                            item.setRead(true);
                            mAdapter.notifyDataSetChanged();
                            getDashBoard();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "VOLLEY --->" + error.toString());
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Log.e(TAG, "VOLLEY --->" + error.toString());
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Basic " + datahelper.getApiAccess(getBaseContext()));
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    // get dashboard data
    private void getDashBoard() {
        SqliteDataBaseHelper sqliteDataBaseHelper = new SqliteDataBaseHelper(getBaseContext());
        String getOrgId = sqliteDataBaseHelper.getOrganizationId();
        String REGISTER_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + getOrgId + "/dashboard/info";
        VolleyApi.getInstance().getJsonObject(getBaseContext(), REGISTER_URL, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                if (result.success && result.dataIsObject()) {
                    JSONObject response = result.getDataAsObject();
                    //getting start profile data.
                    SharedPreferences DASHBOARD = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = DASHBOARD.edit();
                    editor.putString(MainActivity.MAIN_DASHBOARD_DATA, response.toString());
                    editor.commit();
                    handleDashBoardNotifications();
                }
            }
        });
    }

    private void getOrganizationContacts(final NotificationModal ContactId) {

        showProgressDialog("Please wait.");

        String REGISTER_URL = OrganizationModel.getApiBaseUrl() + getOrgId() + "/contacts/" + ContactId.getContactId();

        Log.i(TAG, REGISTER_URL);

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, REGISTER_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        try {
                            String status = response.getString("status");
                            // check if contact active then store it on local db else mark as deleted contact
                            if (status.equals("ACTIVE")) {
                                sqliteDataBaseHelper.insertNewContact(response);
                                Bundle dataBundle = new Bundle();
                                dataBundle.putString("contactid", ContactId.getContactId());
                                Intent intent = new Intent(getBaseContext(), Summary.class);
                                intent.putExtras(dataBundle);
                                startActivity(intent);
                            } else {
                                deleteNotification(ContactId);
                                ToastShow.setText(getBaseContext(), "This notification points to a deleted contact.", Toast.LENGTH_LONG);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "VOLLEY  get Contact:---->" + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "VOLLEY   get Contact:---->" + error.toString());
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Basic " + datahelper.getApiAccess(getBaseContext()));
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonArrayRequest);
    }

    private String getOrgId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Organization", Context.MODE_PRIVATE);
        String StoredOrgid = pref.getString("OrgId", null);
        return StoredOrgid;
    }

    @Override
    public void NotifyDataDelete(NotificationModal item) {
        deleteNotification(item);
    }

    @Override
    public void GetDataFromServer(NotificationModal item) {
        // get item contact id not empty then ignore notification else goto next step
        if (item.getContactId().isEmpty() || item.getContactId().equals("null")) {
        } else {
            markAsRead(item);
            // Check contact in local database if exist then goto summary else get contact from server
            String Contact = new SqliteDataBaseHelper(getBaseContext()).isContactExist(item.getContactId());
            if (Contact != null) {
                Bundle dataBundle = new Bundle();
                dataBundle.putString("contactid", item.getContactId());
                Intent intent = new Intent(getBaseContext(), Summary.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
            } else {
                //get contact from server
                getOrganizationContacts(item);
            }
        }
    }

    private void deleteNotification(final NotificationModal ContactId) {

        String URL = OrganizationModel.getApiBaseUrl() + sqliteDataBaseHelper.getOrganizationId() + "/contacts/" + ContactId.getContactId() + "/notification/" + ContactId.getNotificationId() + "";

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());

        JsonObjectRequest JsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Notification from Contact Deleted Successfully");
                        sqliteDataBaseHelper.deleteNotificationById(ContactId.getNotificationId());
                        handleResponseNotification();
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
                                    DataHelper dataHelper = new DataHelper();
                                    json = new String(response.data);
                                    json = dataHelper.trimMessage(json, "message");
                                    if (json != null) dataHelper.displayMessage(json);
                                    ToastShow.setText(getBaseContext(), "oops something went wrong. try again", Toast.LENGTH_LONG);
                                    break;
                                case 404:
                                    //dBhelper.removeListById(listId);
                            }
                            pDialog.dismiss();
                        }
                    }
                }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Basic " + datahelper.getApiAccess(getBaseContext()));
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        JsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(JsonObjectRequest);
    }

    public class CustomComparator implements Comparator<NotificationModal> {
        @Override
        public int compare(NotificationModal o1, NotificationModal o2) {
            return o1.getDate().compareTo(o2.getDate());
        }
    }
}