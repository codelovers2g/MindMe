package com.mindmesolo.mindme.ContactAndLists;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mindmesolo.mindme.GettingStarted.VolleyResponseResult;
import com.mindmesolo.mindme.GettingStarted.VolleyService;
import com.mindmesolo.mindme.LoginSignup.CustomViewPagerSummary;
import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.OrganizationModel;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.Services.VolleyApi;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.ToastShow;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User1 on 6/7/2016.
 */

public class Summary extends MainActivity implements IFragmentToActivity2, ApiCallsInterface {

    private static final String TAG = "Summary";
    public static Activity KillMe;
    static String StoredContactid;
    static int SubItems = 0;
    protected TextView ContactType1;
    DataHelper dataHelper = new DataHelper();
    ViewPager viewPager;
    DataHelper datahelper = new DataHelper();
    SqliteDataBaseHelper sqliteDataBaseHelper;
    Button _btn1, _btn2, _btn3;
    TextView textView;
    TextView ContactName1, ContactCompany1;
    ToggleButton toggleButtonFav;
    ImageView imageViewBirthday, imageViewContact;

    MenuItem editMenu;

    MenuItem addMenu;

    VolleyResponseResult mResultCallback = null;

    VolleyService mVolleyService;

    CustomViewPagerSummary customViewPagerSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        KillMe = this;

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.summary, frameLayout, false);

        drawer.addView(contentView, 0);

        sqliteDataBaseHelper = new SqliteDataBaseHelper(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Volley Api Calls
        initVolleyCallback();

        mVolleyService = new VolleyService(mResultCallback, this);

        //Contacts and List Tab bar Button .
        toggleButtonContact.setChecked(true);

        textView = (TextView) findViewById(R.id.toolbartext);
        frameLayout = (FrameLayout) findViewById(R.id.framelayout);


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

        viewPager = (ViewPager) findViewById(R.id.customviewpager);

        ContactName1 = (TextView) findViewById(R.id.Contactname);

        ContactCompany1 = (TextView) findViewById(R.id.companyname);

        ContactType1 = (TextView) findViewById(R.id.type);

        toggleButtonFav = (ToggleButton) findViewById(R.id.favoriteBtn);

        imageViewBirthday = (ImageView) findViewById(R.id.birthday);

        imageViewContact = (ImageView) findViewById(R.id.contactImage);

        setAdapter(48);

        Bundle extras = getIntent().getExtras();

        StoredContactid = extras.getString("contactid");

        String ContactId = sqliteDataBaseHelper.getContactCode(StoredContactid);

        if (ContactId.trim().length() > 0) {
            //setTab();
            _btn1 = (Button) findViewById(R.id.btn1);
            _btn1.setBackgroundResource(R.drawable.summarydotindicatorselected);

            _btn2 = (Button) findViewById(R.id.btn2);
            _btn3 = (Button) findViewById(R.id.btn3);
            getData(StoredContactid);
        }
        long time = System.currentTimeMillis();

        sqliteDataBaseHelper.changeContactUpdatedON(StoredContactid, String.valueOf(time));

        handleDashBoardNotifications();
    }

    private void setAdapter(int position) {
        customViewPagerSummary = new CustomViewPagerSummary(getSupportFragmentManager());
        viewPager.setAdapter(customViewPagerSummary);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setCurrentItem(position);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setupMenuItems();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    // change view of option menu according to the fragment of the page.
    private void setupMenuItems() {

        int position = 0;

        Fragment f = customViewPagerSummary.getItem(viewPager.getCurrentItem());

        if (f.getClass().equals(new ContactSummary().getClass())) {

            position = 0;

        } else if (f.getClass().equals(new ContactDetail().getClass())) {

            position = 1;

        } else if (f.getClass().equals(new Contactcatagory().getClass())) {

            position = 2;

        }

        btnAction(position);

        switch (position) {
            case 0:
            case 1:
                if ((addMenu != null) && (editMenu != null)) {
                    addMenu.setVisible(false);
                    editMenu.setVisible(true);
                }
                break;
            case 2:
                if ((addMenu != null) && (editMenu != null)) {
                    addMenu.setVisible(true);
                    editMenu.setVisible(false);
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.add_menu, menu);

        editMenu = menu.findItem(R.id.action_settings);

        addMenu = menu.findItem(R.id.action_settings_add);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Bundle dataBundle = new Bundle();
                dataBundle.putString("contactids", StoredContactid);
                Intent intent = new Intent(getBaseContext(), EditContact.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
                break;
            case R.id.action_settings_add:
                getNewListItems();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        setupMenuItems();
        return true;
    }

    private void getData(final String StoredContactId) {

        imageViewBirthday.setVisibility(View.GONE);

        String Name = sqliteDataBaseHelper.getContactFname(StoredContactId);

        String CompanyName = sqliteDataBaseHelper.getContactsCompanyName(StoredContactId);

        String ContactType = sqliteDataBaseHelper.getContactCode(StoredContactId);

        ContactName1.setText(Name);

        if (CompanyName != null && CompanyName.length() > 2 && !CompanyName.equalsIgnoreCase("null")) {
            ContactCompany1.setVisibility(View.VISIBLE);
            ContactCompany1.setText(CompanyName);
        }

        if (ContactType != null && ContactType.length() > 2 && !ContactType.equalsIgnoreCase("null")) {
            ContactType1.setVisibility(View.VISIBLE);
            ContactType1.setText(ContactType);
        }

        boolean fav = sqliteDataBaseHelper.getContactFavorite(StoredContactId);

        if (fav) {
            toggleButtonFav.setBackgroundResource(R.drawable.star);
            toggleButtonFav.setChecked(true);
        } else {
            toggleButtonFav.setBackgroundResource(R.drawable.star_unselected);
            toggleButtonFav.setChecked(false);
        }
        String Birthday = sqliteDataBaseHelper.getContactsDateOfBirth(StoredContactId);
        Log.i("Time", "birthday " + String.valueOf(Birthday));

        if (StringUtils.isNotBlank(Birthday) && StringUtils.isNotEmpty(Birthday) && !Birthday.equalsIgnoreCase("null")) {
            Calendar todayDate = Calendar.getInstance();
            Calendar endDate = (Calendar) todayDate.clone();

            todayDate.add(Calendar.DAY_OF_MONTH, -1);
            endDate.add(Calendar.DAY_OF_MONTH, +30);

            long startTime = todayDate.getTimeInMillis();

            long endTime = endDate.getTimeInMillis();

            Log.i("Time", "Today " + String.valueOf(startTime));
            Log.i("Time", "End Time " + String.valueOf(endTime));

            long BirthDay = Long.valueOf(Birthday);

            if (BirthDay > startTime && BirthDay < endTime) {
                imageViewBirthday.setVisibility(View.VISIBLE);
            }
        }
        toggleButtonFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    toggleButtonFav.setBackgroundResource(R.drawable.star);

                    sqliteDataBaseHelper.changeContactFavorite(StoredContactId, "true");

                    updateContact(true);

                } else {
                    toggleButtonFav.setBackgroundResource(R.drawable.star_unselected);
                    sqliteDataBaseHelper.changeContactFavorite(StoredContactId, "false");
                    updateContact(false);
                }
            }
        });

        String image = sqliteDataBaseHelper.getContactsPhoto(StoredContactId);
        if (image.length() == 0 || image.toLowerCase().equals("null")) {
            imageViewContact.setImageResource(R.drawable.contactsicon);
        } else {
            imageViewContact.setImageBitmap(dataHelper.decodeBase64(image));
        }
    }

    private void updateContact(boolean fav) {
        //startDialog("Please wait.");
        JSONObject jsonObject = sqliteDataBaseHelper.getContactObject(StoredContactid);
        try {
            jsonObject.remove("favorite");
            jsonObject.put("favorite", fav);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jsonStr = jsonObject.toString();

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());

        final String URL = OrganizationModel.getApiBaseUrl() + sqliteDataBaseHelper.getOrganizationId() + "/contacts/" + StoredContactid + "";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Contact Notes Updates Successfully.");
                        sqliteDataBaseHelper.deleteContact(StoredContactid);
                        sqliteDataBaseHelper.insertNewContact(response);
                        //pDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //pDialog.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            ToastShow.setText(getBaseContext(), "Please try again.", Toast.LENGTH_LONG);
                        }
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
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Basic " + dataHelper.getApiAccess(getBaseContext()));
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    private void unSelectedButton(Button btn) {
        btn.setBackgroundResource(R.drawable.summarydotindicator);
    }

    private void btnAction(int position) {
        switch (position) {
            case 0:
                selectedButton(_btn1);
                unSelectedButton(_btn2);
                unSelectedButton(_btn3);
                textView.setText("Summary");
                break;

            case 1:
                selectedButton(_btn2);
                unSelectedButton(_btn1);
                unSelectedButton(_btn3);
                textView.setText("Details");
                break;

            case 2:
                selectedButton(_btn3);
                unSelectedButton(_btn2);
                unSelectedButton(_btn1);
                textView.setText("Categorization");
                break;

        }

    }

    private void selectedButton(Button btn) {
        btn.setBackgroundResource(R.drawable.summarydotindicatorselected);
    }

    @Override
    public void setCurrentItem(int data) {
        setAdapter(50);
        setupMenuItems();
    }

    @Override
    public void setSubItem(int Position) {
        SubItems = Position;
        if (Position == 3) {
            addMenu.setVisible(false);
            editMenu.setVisible(false);
        } else {
            addMenu.setVisible(true);
        }
    }

    @Override
    public void changeContactType(String data) {
        ContactType1.setText(data.toUpperCase());
        sqliteDataBaseHelper.changeContactCode(StoredContactid, data.toUpperCase());
    }

    // if user
    private void getNewListItems() {
        Intent intent = null;
        switch (SubItems) {
            case 0:
                intent = new Intent(getBaseContext(), AddLists.class);
                break;
            case 1:
                intent = new Intent(getBaseContext(), AddInterest.class);
                break;
            case 2:
                intent = new Intent(getBaseContext(), AddTag.class);
                break;
            default:
                intent = new Intent(getBaseContext(), AddLists.class);
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    // get all update requests from the child fragments
    @Override
    public JSONObject AddContact_In_List_Interest_Tags(JSONObject data, String Type) {
        try {
            String ListId = data.getString("id");

            String URL = OrganizationModel.getApiBaseUrl() + sqliteDataBaseHelper.getOrganizationId() + "/" + Type + "/" + ListId;

            mVolleyService.jsonObjectRequestTypePutWithAuth("UpdateItems", URL, data.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    // get all Remove requests from the child fragments
    @Override
    public JSONObject Remove_Contact_From_List_Interest_Tags(JSONObject data, String Type) {
        try {
            String ListId = data.getString("id");

            String URL = OrganizationModel.getApiBaseUrl() + sqliteDataBaseHelper.getOrganizationId() + "/" + Type + "/" + ListId;

            mVolleyService.jsonObjectRequestTypePutWithAuth("UpdateItems", URL, data.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    void initVolleyCallback() {
        mResultCallback = new VolleyResponseResult() {
            @Override
            public void objectResponce(String requestType, JSONObject response) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + response);
            }

            @Override
            public void ArrayResponce(String requestType, JSONArray response) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + response);
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d(TAG, "Volley requester  :" + requestType);
                Log.d(TAG, "Volley JSON post  :" + error.toString());
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(StoredContactid);
        getDashBoard();
    }

    // get dashboard data
    private void getDashBoard() {
        SqliteDataBaseHelper sqliteDataBaseHelper = new SqliteDataBaseHelper(Summary.this);
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
}
