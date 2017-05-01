package com.mindmesolo.mindme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mindmesolo.mindme.ContactAndLists.ContactsAndLists;
import com.mindmesolo.mindme.CreateCampaign.CreateNewCampaign;
import com.mindmesolo.mindme.GettingStarted.TrainingAndSupport;
import com.mindmesolo.mindme.LeadRoutes.LeadCapture;
import com.mindmesolo.mindme.LoginSignup.LaunchScreen;
import com.mindmesolo.mindme.Notifcations.Notification;
import com.mindmesolo.mindme.ToolsAndSettings.ToolsAndSettings;
import com.mindmesolo.mindme.ViewCampaigns.CampaignDetailView;
import com.mindmesolo.mindme.ViewMobilePages.ViewMobilePages;
import com.mindmesolo.mindme.googlePushNotifcation.RegistrationIntentService;
import com.mindmesolo.mindme.helper.CircleImageView;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String PREFS_NAME = "DASHBOARD_PREFS";
    public static final String USER_DATA_PREF = "UserData";
    public static final String MAIN_DASHBOARD_DATA = "DASHBOARD";
    public static final String PREFS_KEY_GETTING_START = "GETTINGSTART";
    private static final String TAG = "MainActivity";
    public boolean MainActivityObject = false;
    protected TextView notificationCount;

    protected ToggleButton toggleButtonDashboard, toggleButtonContact, toggleButtonCampaing, toggleButtonLead, toggleButtonNotification;

    protected FrameLayout frameLayout;

    protected DrawerLayout drawer;
    TextView name, email, phone;
    ListView list;
    Button signout;
    SqliteDataBaseHelper dBhelper;
    DataHelper dataHelper = new DataHelper();
    CircleImageView companylogo;
    List<HashMap<String, String>> alist;
    TextView versionCode;
    String[] Items = {"Dashboard",
            "Notifications",
            "Campaigns",
//            "Conversations",
            "Lead Routes",
//            "Mobile Pages",
            "Contacts & Lists",
            "Training & Support",
            "Settings",
            //    "Mobile Pages"
    };
    int[] images = {R.drawable.dashboardblue,
            R.drawable.notificationbell,
            R.drawable.lastcampaignicon,
//            R.drawable.conversationsicon,
            R.drawable.leadcaptureicon,
//            R.drawable.mobile_pages,
            R.drawable.contactsicon,
            R.drawable.trainingblue,
            R.drawable.settings,
            //  R.drawable.mobile_pages
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        frameLayout = (FrameLayout) findViewById(R.id.framelayout);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        // new code change nav icon for
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

        //old code
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dBhelper = new SqliteDataBaseHelper(this);

        toggleButtonDashboard = (ToggleButton) findViewById(R.id.home);
        toggleButtonContact = (ToggleButton) findViewById(R.id.contact);
        toggleButtonCampaing = (ToggleButton) findViewById(R.id.campaign);
        toggleButtonLead = (ToggleButton) findViewById(R.id.leadroutes);
        toggleButtonNotification = (ToggleButton) findViewById(R.id.notification);

        notificationCount = (TextView) findViewById(R.id.notificationCount);

        list = (ListView) findViewById(R.id.listView);

        View headerView = View.inflate(this, R.layout.nav_header_main, null);
        list.addHeaderView(headerView);

        name = (TextView) headerView.findViewById(R.id.name);
        phone = (TextView) headerView.findViewById(R.id.phone);
        email = (TextView) headerView.findViewById(R.id.Email);

        companylogo = (CircleImageView) headerView.findViewById(R.id.HeaderLogo);

        // get device token for fcm
        startService(new Intent(this, RegistrationIntentService.class));

        setupLogoMain();

        setupHeaders();

        View footerView = View.inflate(this, R.layout.footer, null);


        list.addFooterView(footerView);
        signout = (Button) footerView.findViewById(R.id.buttonsignout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signout();
            }
        });

        alist = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 7; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("car", "" + Items[i]);
            hm.put("images", Integer.toString(images[i]));
            alist.add(hm);
        }

        String[] from = {"car", "images"};
        int[] to = {R.id.txt, R.id.images};
        SimpleAdapter simpleadapter = new SimpleAdapter(getApplicationContext(), alist, R.layout.menulayout, from, to);
        list.setAdapter(simpleadapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        Intent intent = new Intent(getBaseContext(), Dashboard.class);
                        startActivity(intent);
                        break;
                    case 2:
                        Intent intent2 = new Intent(getBaseContext(), Notification.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(getBaseContext(), CampaignDetailView.class);
                        startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(getBaseContext(), LeadCapture.class);
                        startActivity(intent4);
                        break;
                    case 5:
                        Intent intent5 = new Intent(getBaseContext(), ContactsAndLists.class);
                        startActivity(intent5);
                        break;
                    case 6:
                        Intent intent6 = new Intent(getBaseContext(), TrainingAndSupport.class);
                        startActivity(intent6);
                        break;
                    case 7:
                        Intent intent7 = new Intent(getBaseContext(), ToolsAndSettings.class);
                        startActivity(intent7);
                        break;
                    case 8:
                        Intent intent8 = new Intent(getBaseContext(), ViewMobilePages.class);
                        startActivity(intent8);
                        break;
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
        tabsclick();
        tabClickListrner();
        handleDashBoardNotifications();
    }

    private void setupHeaders() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences(USER_DATA_PREF, MODE_PRIVATE);

        String StoredName = pref.getString("Name", null);

        String mindmeEmail = pref.getString("mindMeEmail", null);

        String mindmeNumber = pref.getString("phone", null);

        name.setText(StoredName);

        email.setText(mindmeEmail);

        phone.setText(mindmeNumber);

    }

    public void handleDashBoardNotifications() {
        SharedPreferences DASHBOARD = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String response = DASHBOARD.getString(MAIN_DASHBOARD_DATA, null);
        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String unreadNotificationCount = jsonObject.getString("unreadNotificationCount");
                if (isNullOrEmpty(unreadNotificationCount)) {
                    //handel unread notifications count
                    int notification = Integer.parseInt(String.valueOf(unreadNotificationCount));

                    if (notification > 0 && MainActivityObject == false) {
                        notificationCount.setVisibility(View.VISIBLE);
                        notificationCount.setText(String.valueOf(notification));
                    } else {
                        notificationCount.setVisibility(View.GONE);
                    }
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }

    private boolean isNullOrEmpty(String data) {
        if (data.trim() != null && !data.equalsIgnoreCase("null")) {
            return true;
        } else {
            return false;
        }
    }

    private void tabClickListrner() {
        toggleButtonDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleButtonDashboard.isChecked()) {
                    Intent intent = new Intent(MainActivity.this, Dashboard.class);
                    //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
            }
        });
        toggleButtonContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleButtonContact.isChecked()) {
                    Intent intent = new Intent(MainActivity.this, ContactsAndLists.class);
                    //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
            }
        });
        toggleButtonCampaing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleButtonCampaing.isChecked()) {
                    Intent intent = new Intent(MainActivity.this, CreateNewCampaign.class);
                    //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
            }
        });
        toggleButtonLead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleButtonLead.isChecked()) {
                    Intent intent = new Intent(MainActivity.this, LeadCapture.class);
                    //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
            }
        });
        toggleButtonNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleButtonNotification.isChecked()) {
                    Intent intent = new Intent(MainActivity.this, Notification.class);
                    //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
            }
        });
    }

    public void tabsclick() {
        toggleButtonDashboard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleButtonDashboard.setBackgroundResource(R.drawable.homeselected);
                    toggleButtonContact.setBackgroundResource(R.drawable.contactsunselected);
                    toggleButtonCampaing.setBackgroundResource(R.drawable.airplaneunselected);
                    toggleButtonLead.setBackgroundResource(R.drawable.leadrouteunselected);
                    toggleButtonNotification.setBackgroundResource(R.drawable.notifcationunselected);
                    toggleButtonDashboard.setChecked(true);
                    toggleButtonContact.setChecked(false);
                    toggleButtonCampaing.setChecked(false);
                    toggleButtonLead.setChecked(false);
                    toggleButtonNotification.setChecked(false);
                }
            }
        });
        toggleButtonContact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleButtonDashboard.setBackgroundResource(R.drawable.homeunselected);
                    toggleButtonContact.setBackgroundResource(R.drawable.contactselected);
                    toggleButtonCampaing.setBackgroundResource(R.drawable.airplaneunselected);
                    toggleButtonLead.setBackgroundResource(R.drawable.leadrouteunselected);
                    toggleButtonNotification.setBackgroundResource(R.drawable.notifcationunselected);
                    toggleButtonDashboard.setChecked(false);
                    toggleButtonContact.setChecked(true);
                    toggleButtonCampaing.setChecked(false);
                    toggleButtonLead.setChecked(false);
                    toggleButtonNotification.setChecked(false);
                }
            }
        });
        toggleButtonCampaing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleButtonDashboard.setBackgroundResource(R.drawable.homeunselected);
                    toggleButtonContact.setBackgroundResource(R.drawable.contactsunselected);
                    toggleButtonCampaing.setBackgroundResource(R.drawable.airplanegreen);
                    toggleButtonLead.setBackgroundResource(R.drawable.leadrouteunselected);
                    toggleButtonNotification.setBackgroundResource(R.drawable.notifcationunselected);
                    toggleButtonDashboard.setChecked(false);
                    toggleButtonContact.setChecked(false);
                    toggleButtonCampaing.setChecked(true);
                    toggleButtonLead.setChecked(false);
                    toggleButtonNotification.setChecked(false);
                }
            }
        });
        toggleButtonLead.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleButtonDashboard.setBackgroundResource(R.drawable.homeunselected);
                    toggleButtonContact.setBackgroundResource(R.drawable.contactsunselected);
                    toggleButtonCampaing.setBackgroundResource(R.drawable.airplaneunselected);
                    toggleButtonLead.setBackgroundResource(R.drawable.leadrouteselected);
                    toggleButtonNotification.setBackgroundResource(R.drawable.notifcationunselected);
                    toggleButtonDashboard.setChecked(false);
                    toggleButtonContact.setChecked(false);
                    toggleButtonCampaing.setChecked(false);
                    toggleButtonLead.setChecked(true);
                    toggleButtonNotification.setChecked(false);
                }
            }
        });
        toggleButtonNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleButtonDashboard.setBackgroundResource(R.drawable.homeunselected);
                    toggleButtonContact.setBackgroundResource(R.drawable.contactsunselected);
                    toggleButtonCampaing.setBackgroundResource(R.drawable.airplaneunselected);
                    toggleButtonLead.setBackgroundResource(R.drawable.leadrouteunselected);
                    toggleButtonNotification.setBackgroundResource(R.drawable.notificationselected);
                    toggleButtonDashboard.setChecked(false);
                    toggleButtonContact.setChecked(false);
                    toggleButtonCampaing.setChecked(false);
                    toggleButtonLead.setChecked(false);
                    toggleButtonNotification.setChecked(true);
                }
            }
        });
    }

    private void Signout() {

        cleanDeviceToken();

        SharedPreferences UserLogin = getApplicationContext().getSharedPreferences("UserLogin", MODE_PRIVATE);
        SharedPreferences.Editor editor = UserLogin.edit();
        editor.clear();
        editor.commit();

        SharedPreferences Organization = getApplicationContext().getSharedPreferences("Organization", MODE_PRIVATE);
        SharedPreferences.Editor editor1 = Organization.edit();
        editor1.clear();
        editor1.commit();

        SharedPreferences UserData = getApplicationContext().getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = UserData.edit();
        editor2.clear();
        editor2.commit();

        SharedPreferences DASHBOARD = getSharedPreferences("DASHBOARD_PREFS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor3 = DASHBOARD.edit();
        editor3.clear();
        editor3.commit();

        dBhelper.DeleteContacts();
        dBhelper.DeleteInterests();
        dBhelper.DeleteTags();
        dBhelper.DeleteLists();
        dBhelper.DeleteOrganization();
        dBhelper.DeleteUserProfile();
        dBhelper.DeleteBusiness();
        dBhelper.DeleteImportContacts();
        dBhelper.DeleteCampaignView();
        dBhelper.DeletePhoneGreetingData();
        dBhelper.DeleteSocialMediaData();
        dBhelper.DeleteNotifications();
        dBhelper.DeleteMobilePages();
        File AudioPlayFile = new File(dir, "greeting.mp3");
        if (AudioPlayFile.exists()) {
            try {
                AudioPlayFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent(this, LaunchScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // Update device token for  FCM
    private void cleanDeviceToken() {

        SqliteDataBaseHelper sqliteDataBaseHelper = new SqliteDataBaseHelper(getBaseContext());

        final String DeviceToken = "";

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupLogoMain() {
        String Logo = dBhelper.getUserLogo();
        if (Logo != null && Logo.length() > 7) {
            Bitmap myBitmapAgain = dataHelper.decodeBase64(Logo);
            companylogo.setImageBitmap(myBitmapAgain);
        } else {
            companylogo.setImageResource(R.drawable.contactsicon);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupLogoMain();
        setupHeaders();
        handleDashBoardNotifications();
        MainActivityObject = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
