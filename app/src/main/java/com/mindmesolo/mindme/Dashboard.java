package com.mindmesolo.mindme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mindmesolo.mindme.FeedBack.FeedBackMain;
import com.mindmesolo.mindme.GettingStarted.GettingStart;
import com.mindmesolo.mindme.GettingStarted.TrainingAndSupport;
import com.mindmesolo.mindme.LeadRoutes.NewLeads;
import com.mindmesolo.mindme.LoginSignup.LaunchScreen;
import com.mindmesolo.mindme.Services.VolleyApi;
import com.mindmesolo.mindme.ViewCampaigns.CampaignDetailView;
import com.mindmesolo.mindme.helper.CircleImageView;
import com.mindmesolo.mindme.helper.Contacts;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by User1 on 24-05-2016.
 */

public class Dashboard extends MainActivity implements View.OnClickListener {

    private static final String TAG = "DashBoard";
    static int Campaign = 0;
    static int newLeadCount = 0;
    RelativeLayout callBackGettingStart;
    LinearLayout feedbackLink, LastCampaign;
    TextView campaignTitle, campaignDate, RecipientsCount, ResponseCount, gettingStart, leadCount;
    SqliteDataBaseHelper sqliteDataBaseHelper;
    String userInformation;
    DataHelper dataHelper = new DataHelper();
    CircleImageView CompanyLogo;
    ProgressBar progressBarPlay, progressBarOpens;
    TextView playper, openper;
    LinearLayout linearLayoutLead;
    View dashboardView;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.dashboard, frameLayout, false);

        drawer.addView(contentView, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sqliteDataBaseHelper = new SqliteDataBaseHelper(this);

        initializeUserInterface();

        toggleButtonDashboard.setChecked(true);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

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
        SessionLogin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleDashBoardData();
        getDashBoard();
        toggleButtonDashboard.setChecked(true);
        setupLogo();
        getProgressBarStatus();
        HandleStartUpInformation();
        updateDeviceToken();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trainingandsupport, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, TrainingAndSupport.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeUserInterface() {

        CompanyLogo = (CircleImageView) findViewById(R.id.companylogo);

        leadCount = (TextView) findViewById(R.id.leadcapturecount);

        gettingStart = (TextView) findViewById(R.id.gettingstart);

        campaignTitle = (TextView) findViewById(R.id.lastCampaignTitle);

        campaignDate = (TextView) findViewById(R.id.campaigndate);

        RecipientsCount = (TextView) findViewById(R.id.RecipientsCount);

        ResponseCount = (TextView) findViewById(R.id.ResponceCount);

        progressBarOpens = (ProgressBar) findViewById(R.id.progress_bar_opens);

        progressBarPlay = (ProgressBar) findViewById(R.id.progress_bar_plays);

        playper = (TextView) findViewById(R.id.playPercentage);

        openper = (TextView) findViewById(R.id.openPercentage);

        callBackGettingStart = (RelativeLayout) findViewById(R.id.callbackgettingstart);

        callBackGettingStart.setOnClickListener(this);

        LastCampaign = (LinearLayout) findViewById(R.id.LastCampaign);

        LastCampaign.setOnClickListener(this);

        feedbackLink = (LinearLayout) findViewById(R.id.feedbackLink);

        feedbackLink.setOnClickListener(this);

        dashboardView = (View) findViewById(R.id.dashboardView);

        notificationCount = (TextView) findViewById(R.id.notificationCount);

        linearLayoutLead = (LinearLayout) findViewById(R.id.leadCaptureLayout);

        linearLayoutLead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences DASHBOARD = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                String responce = DASHBOARD.getString(MAIN_DASHBOARD_DATA, null);
                if (responce != null) {
                    try {
                        JSONObject response = new JSONObject(responce);
                        String newLeadCount = response.getString("newLeadCount");
                        if (StringUtils.isNotBlank(newLeadCount)) {
                            //int LeadCount = Integer.parseInt(newLeadCount);

                            ArrayList<Contacts> LeadContacts = dBhelper.getContactsDetailDayWise("LEAD", 7);
                            if (LeadContacts.size() > 0) {
                                int newLeadCount1 = Integer.parseInt(String.valueOf(newLeadCount));
                                Intent intent = new Intent(getBaseContext(), NewLeads.class);
                                intent.putExtra("LeadContactsCount", String.valueOf(newLeadCount1));
                                startActivity(intent);
                            }
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    private void SessionLogin() {
        try {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("UserLogin", MODE_PRIVATE);
            //get username from session
            String email = pref.getString("Email", null);
            //get password from session
            String password = pref.getString("Password", null);
            //if session username or password is null then do written in if if not null then do written in else
            if (email == null || password == null) {
                Intent intent = new Intent(getBaseContext(), LaunchScreen.class);
                startActivity(intent);
                finish();
            } else {
                //Update fireBase device token
                updateDeviceToken();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getOrganizationData();
                    }
                }, 200);

                final Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getOrganizationUser();
                    }
                }, 200);

                final Handler handler3 = new Handler();
                handler3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getOrganizationContacts();
                    }
                }, 200);


                final Handler handler4 = new Handler();
                handler4.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //setup organization ViewMobilePages Lead Capture Data
                        getOrganizationNotification();
                    }
                }, 200);

                //setup if Logo is not null then setup logo
                setupLogo();
            }
        } catch (Exception e) {
            Log.e(TAG, "Something went wrong. in SessionLogin");
        }
    }

    // get dashboard data
    private void getDashBoard() {
        String REGISTER_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + getOrgId() + "/dashboard/info";
        VolleyApi.getInstance().getJsonObject(getBaseContext(), REGISTER_URL, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                if (result.success && result.dataIsObject()) {
                    JSONObject response = result.getDataAsObject();
                    //getting start profile data.
                    SharedPreferences DASHBOARD = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = DASHBOARD.edit();
                    editor.putString(MAIN_DASHBOARD_DATA, response.toString());
                    editor.commit();
                    handleDashBoardData();
                }
            }
        });
    }

    // get Update dashboard
    public void handleDashBoardData() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        SharedPreferences DASHBOARD = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String responseData = DASHBOARD.getString(MAIN_DASHBOARD_DATA, null);

        if (responseData != null) {
            try {
                JSONObject response = new JSONObject(responseData);
                Campaign = 1;
                String name = response.getString("campaignName");
                String date = response.getString("date");
                String recipientsCount = DataHelper.getInstance().getValidData(response.getString("recipientsCount"));
                String responsesCount = DataHelper.getInstance().getValidData(response.getString("responsesCount"));
                String playsCount = DataHelper.getInstance().getValidData(response.getString("playsCount"));
                String openCount = DataHelper.getInstance().getValidData(response.getString("openCount"));

                RecipientsCount.setText(recipientsCount);

                ResponseCount.setText(responsesCount);

                //String newLeadCounts = response.getString("newLeadCount");
                String newLeadCounts = String.valueOf(dBhelper.getContactsDetailDayWise("LEAD", 7).size());

                String unreadNotificationCount = response.getString("unreadNotificationCount");

                if (StringUtils.isNotBlank(name) && !name.equalsIgnoreCase("null")) {
                    campaignTitle.setText(name);
                }
                if (StringUtils.isNotBlank(date) && !date.equalsIgnoreCase("null")) {
                    long campaignDate = Long.parseLong(date);
                    try {
                        calendar.setTimeInMillis(campaignDate);
                        this.campaignDate.setText(formatter.format(calendar.getTime()));
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }

                int perOpen = dataHelper.CalculatePercentage(
                        Integer.parseInt(String.valueOf(openCount)),
                        Integer.parseInt(String.valueOf(recipientsCount)));

                int perPlay = dataHelper.CalculatePercentage(
                        Integer.parseInt(String.valueOf(playsCount)),
                        Integer.parseInt(String.valueOf(recipientsCount)));

                if (perOpen <= 100) {
                    openper.setVisibility(View.VISIBLE);
                    openper.setText(String.valueOf(perOpen + "%"));
                    progressBarOpens.setProgress(perOpen);
                } else if (perOpen > 100) {
                    openper.setVisibility(View.VISIBLE);
                    openper.setText(String.valueOf("100%"));
                    progressBarOpens.setProgress(100);
                }
                if (perPlay <= 100) {
                    playper.setVisibility(View.VISIBLE);
                    playper.setText(String.valueOf(perPlay + "%"));
                    progressBarPlay.setProgress(perPlay);
                } else if (perPlay > 100) {
                    playper.setVisibility(View.VISIBLE);
                    playper.setText(String.valueOf("100%"));
                    progressBarPlay.setProgress(100);
                }

                if (StringUtils.isNotBlank(unreadNotificationCount)) {
                    //handel unread notifications count
                    int notification = Integer.parseInt(String.valueOf(unreadNotificationCount));
                    handleNotifications(notification);
                }
                if (StringUtils.isNotBlank(newLeadCounts)) {
                    int newLeadCount1 = Integer.parseInt(String.valueOf(newLeadCounts));
                    newLeadCount = newLeadCount1;
                    leadCount.setText(String.valueOf(newLeadCount1));
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }

    // setup getting start data
    private void getProgressBarStatus() {
        String Fetch_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + getOrgId() + "/startup/info";
        VolleyApi.getInstance().getJsonArray(getBaseContext(), Fetch_URL, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                if (result.success && result.dataIsArray()) {
                    JSONArray response = result.getDataAsArray();
                    Log.i(TAG, "VOLLEY startup Data : ----->" + response.toString());
                    try {
                        SharedPreferences DASHBOARD = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = DASHBOARD.edit();
                        editor.putString(PREFS_KEY_GETTING_START, response.getString(0));
                        editor.commit();
                        HandleStartUpInformation();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    // Update device token for  FCM
    private void updateDeviceToken() {

        SharedPreferences settings = getBaseContext().getSharedPreferences("DEVICE_TOKEN_PREFS", Context.MODE_PRIVATE);

        final String DeviceToken = settings.getString("DEVICE_TOKEN_STRING", null);

        if (sqliteDataBaseHelper.getUserID() != null) {

            String URL = OrganizationModel.getUserBaseUrl() + sqliteDataBaseHelper.getUserID() + "/fcm";

            try {

                RequestQueue requestQueue = Volley.newRequestQueue(this);

                StringRequest jsonObjectRequest = new StringRequest(Request.Method.PUT, URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i(TAG, "Device token Update Success");
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, "VOLLEY Update Token Data : ----->" + error.toString());
                                String json = null;
                                NetworkResponse response = error.networkResponse;
                                if (response != null && response.data != null) {
                                    switch (response.statusCode) {
                                        case 400:
                                            json = new String(response.data);
                                            json = new DataHelper().trimMessage(json, "message");
                                            if (json != null) new DataHelper().displayMessage(json);
                                            break;
                                    }
                                }
                            }
                        }) {

                    @Override
                    public String getBodyContentType() {
                        return "application/text; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() {
                        try {
                            return DeviceToken == null ? null : DeviceToken.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                    DeviceToken, "utf-8");
                            return null;
                        }
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Authorization", "Basic " + dataHelper.getApiAccess(getBaseContext()));
                        return headers;
                    }
                };
                requestQueue.add(jsonObjectRequest);
            } catch (Exception e) {
            }
        }
    }

    //setup dashboard
    private void HandleStartUpInformation() {

        TextView progressbarStatus = (TextView) findViewById(R.id.progressbarStatus);

        ProgressBar progressStatus = (ProgressBar) findViewById(R.id.progressStatus);

        SharedPreferences DASHBOARD = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        String GettingStartData = DASHBOARD.getString(PREFS_KEY_GETTING_START, null);

        if (GettingStartData != null) {

            try {
                List<String> el = Arrays.asList(GettingStartData.split(","));

                System.out.println(el);

                int count = el.size() * 100 / 6;

                progressbarStatus.setText(String.valueOf(count) + "%");

                progressStatus.setProgress(count);

                if (count == 100) {
                    callBackGettingStart.setVisibility(View.GONE);
                    dashboardView.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            callBackGettingStart.setVisibility(View.VISIBLE);
            dashboardView.setVisibility(View.VISIBLE);
        }

    }

    // update UI if new Notification come
    private void handleNotifications(int notification) {

        if (notification > 0) {

            notificationCount.setVisibility(View.VISIBLE);

            notificationCount.setText(String.valueOf(notification));

        } else {
            notificationCount.setVisibility(View.GONE);
        }
    }

    // Sync Notifications on Local Db
    private void getOrganizationNotification() {

        String Fetch_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + getOrgId() + "/notifications/info";

        VolleyApi.getInstance().getJsonArray(getBaseContext(), Fetch_URL, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
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
                    }
                }
            }
        });
    }

    // Get Organization user data from Server
    private void getOrganizationUser() {
        String Fetch_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + getOrgId() + "/users";
        VolleyApi.getInstance().getJsonArray(getBaseContext(), Fetch_URL, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                if (result.success && result.dataIsArray()) {
                    JSONArray response = result.getDataAsArray();
                    userInformation = response.toString();
                    sqliteDataBaseHelper.updateAppUserData(response);
                }
            }
        });
    }

    // Get Organization Contacts from Server
    private void getOrganizationContacts() {

        String Fetch_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + getOrgId() + "/contacts?code=ACTIVE&size=500&sortBy=date&asc=false";

        VolleyApi.getInstance().getJsonArray(getBaseContext(), Fetch_URL, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                if (result.success && result.dataIsArray()) {
                    JSONArray response = result.getDataAsArray();
                    try {
                        for (int j = 0; j < response.length(); j++) {
                            if (response.length() == sqliteDataBaseHelper.numberOfRowsContact()) {
                                break;
                            } else {
                                sqliteDataBaseHelper.insertNewContact(response.getJSONObject(j));
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "VOLLEY  contacts:---->" + e.toString());
                    }
                }
            }
        });
    }

    // Get Organization from Server
    private void getOrganizationData() {
        String REGISTER_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + getOrgId() + "";
        VolleyApi.getInstance().getJsonObject(getBaseContext(), REGISTER_URL, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                if (result.success && result.dataIsObject()) {
                    JSONObject response = result.getDataAsObject();
                    if (sqliteDataBaseHelper.getSocialMediaArray().size() <= 0) {
                        sqliteDataBaseHelper.InsertIntoSocialMedia(response);
                    }
                    if (sqliteDataBaseHelper.getUserProfileData().size() <= 0) {
                        sqliteDataBaseHelper.updateAppUserData(response);
                    }
                    if (sqliteDataBaseHelper.getPhoneGreetings().size() <= 0) {
                        sqliteDataBaseHelper.InsertIntoPhoneGreeting(response);
                    }
                    if (sqliteDataBaseHelper.getBusinessSingleDay(0).size() <= 0) {
                        sqliteDataBaseHelper.insertBusinessHours(response);
                    }
                    sqliteDataBaseHelper.DeleteLists();
                    sqliteDataBaseHelper.insertIntoLists(response);
                    sqliteDataBaseHelper.DeleteInterests();
                    sqliteDataBaseHelper.insertInterestsJsonObj(response);
                    sqliteDataBaseHelper.DeleteTags();
                    sqliteDataBaseHelper.insertNewTag(response);
                    handleUserResponse(response);
                    setupLogo();
                }
            }
        });
    }

    // Sync user data on Local Db
    private void handleUserResponse(JSONObject jsonobject) {
        try {
            String name = jsonobject.getString("name");
            String legalName = jsonobject.getString("legalName");
            String email = jsonobject.getString("email");
            String contactTypes = jsonobject.getString("contactTypes");
            String originalEmail = jsonobject.getString("originalEmail");
            String organizationType = jsonobject.getString("organizationType");
            String organizationEntityType = jsonobject.getString("organizationEntityType");
            String industryType = jsonobject.getString("industryType");
            String registrationNumber = jsonobject.getString("registrationNumber");
            String taxNumber = jsonobject.getString("taxNumber");
            String siteUrl = jsonobject.getString("siteUrl");
            String siteProvider = jsonobject.getString("siteProvider");
            String facebookUrl = jsonobject.getString("facebookUrl");
            String linkedInUrl = jsonobject.getString("linkedInUrl");
            String twitterUrl = jsonobject.getString("twitterUrl");
            String facebookId = jsonobject.getString("facebookId");
            String linkedInId = jsonobject.getString("linkedInId");
            String twitterId = jsonobject.getString("twitterId");
            String id = jsonobject.getString("id");
            String planId = jsonobject.getString("planId");
            String apiKey = jsonobject.getString("apiKey");
            String twilioAccountSid = jsonobject.getString("twilioAccountSid");
            String twilioAuthToken = jsonobject.getString("twilioAuthToken");
            String googleAnalyticsKey = jsonobject.getString("googleAnalyticsKey");
            String demoCompany = jsonobject.getString("demoCompany");
            String restriction = jsonobject.getString("restriction");
            String status = jsonobject.getString("status");
            String mobile = null, emailRoute = null, textRoute = null, phoneRoute = null;
            JSONArray jsonArray = jsonobject.getJSONArray("phones");
            for (int k = 0; k < jsonArray.length(); k++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(k);
                mobile = jsonObject1.get("phoneNumber").toString();
            }
            JSONObject jsonObjectLeadCapture = jsonobject.getJSONObject("leadCapture");
            emailRoute = jsonObjectLeadCapture.get("emailLeadRoute").toString();
            textRoute = jsonObjectLeadCapture.get("textLeadRoute").toString();
            phoneRoute = jsonObjectLeadCapture.get("phoneLeadRoute").toString();
            sqliteDataBaseHelper.insertuserTable(name, legalName, email, originalEmail, organizationType, organizationEntityType, industryType, registrationNumber, taxNumber, siteUrl, siteProvider, facebookUrl, linkedInUrl, twitterUrl, facebookId, linkedInId, twitterId, id, planId, apiKey, twilioAccountSid, twilioAuthToken, googleAnalyticsKey, demoCompany, mobile, emailRoute, textRoute, phoneRoute, userInformation, contactTypes, restriction, status);
        } catch (Exception e) {
            Log.e(TAG, "Error while inserting Orgnization user");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.callbackgettingstart:
                Intent intent = new Intent(getBaseContext(), GettingStart.class);
                startActivity(intent);
                break;
            case R.id.feedbackLink:
                Intent intent1 = new Intent(getBaseContext(), FeedBackMain.class);
                startActivity(intent1);
                break;
            case R.id.LastCampaign:
                if (Campaign != 0) {
                    Intent intent2 = new Intent(getBaseContext(), CampaignDetailView.class);
                    startActivity(intent2);
                }
                break;
        }
    }

    private String getOrgId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Organization", Context.MODE_PRIVATE);
        String StoredOrgid = pref.getString("OrgId", null);
        return StoredOrgid;
    }

    private void setupLogo() {
        String Logo = sqliteDataBaseHelper.getUserLogo();
        if (Logo != null && Logo.length() > 7) {
            Bitmap myBitmapAgain = dataHelper.decodeBase64(Logo);
            CompanyLogo.setImageBitmap(myBitmapAgain);
        } else {
            CompanyLogo.setImageResource(R.drawable.checked);
        }
    }
}


