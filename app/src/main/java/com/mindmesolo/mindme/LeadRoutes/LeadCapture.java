package com.mindmesolo.mindme.LeadRoutes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mindmesolo.mindme.GettingStarted.TrainingAndSupport;
import com.mindmesolo.mindme.LeadRoutes.PieChart.PieHelper;
import com.mindmesolo.mindme.LeadRoutes.PieChart.PieView;
import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.Services.VolleyApi;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


/**
 * Created by User1 on 8/5/2016.
 */
public class LeadCapture extends MainActivity {

    private static final String TAG = "LeadCapture";

    SqliteDataBaseHelper sqliteDataBaseHelper;

    DataHelper dataHelper = new DataHelper();

    String[] name = new String[3];

    int[] length = new int[3];

    String[] date = new String[3];

    int[] image = new int[3];

    ListView listView;

    PieView pieView = null;

    ImageView trainingImage;

    LinearLayout LeadIconslayout;

    View pie_view;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.leadcapture, frameLayout, false);

        drawer.addView(contentView, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        sqliteDataBaseHelper = new SqliteDataBaseHelper(this);

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

        trainingImage = (ImageView) findViewById(R.id.training);

        pie_view = (View) findViewById(R.id.pie_view);

        LeadIconslayout = (LinearLayout) findViewById(R.id.LeadIconslayout);

        trainingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeadCapture.this, TrainingAndSupport.class);
                intent.putExtra("URL", "http://support.mindmesolo.com/#!/AddLeadForm");
                startActivity(intent);
            }
        });

        listView = (ListView) findViewById(R.id.list_view);

        String orgEmail = sqliteDataBaseHelper.getOrginalEmail();

        image = new int[]{R.drawable.phonewhite, R.drawable.smswhite, R.drawable.envelopewhite};

        name = new String[]{sqliteDataBaseHelper.getPhone(), "Join", orgEmail,};

        getNotifications();

        getOrganizationContacts();

        toggleButtonLead.setChecked(true);
    }


    private void setupUI() {

        pieView = (PieView) findViewById(R.id.pie_view);

        // new Code
        ArrayList<LeadCaptureModel> contactsList = new ArrayList<>();

        contactsList.add(new LeadCaptureModel(sqliteDataBaseHelper.getPhone(), sqliteDataBaseHelper.getPhoneTypelen(), sqliteDataBaseHelper.getPhoneLastDate(), R.drawable.phonewhite));

        contactsList.add(new LeadCaptureModel("Join", sqliteDataBaseHelper.getsmsTypelen(), sqliteDataBaseHelper.getSmsLastDate(), R.drawable.smswhite));

        contactsList.add(new LeadCaptureModel(sqliteDataBaseHelper.getOrginalEmail(), sqliteDataBaseHelper.getEmailTypelen(), sqliteDataBaseHelper.getEmailLastDate(), R.drawable.envelopewhite));

        LeadCaptureAdapter leadCaptureAdapter = new LeadCaptureAdapter(getBaseContext(), R.layout.leadcapturelayout, contactsList);

        listView.setAdapter(leadCaptureAdapter);

        DataHelper.getInstance().setListViewHeightBasedOnItems(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(getBaseContext(), PhoneRoute.class));
                        break;
                    case 1:
                        startActivity(new Intent(getBaseContext(), TextRoute.class));
                        break;
                    case 2:
                        startActivity(new Intent(getBaseContext(), EmailRoute.class));
                }
            }
        });

        int total = sqliteDataBaseHelper.getPhoneTypelen() + sqliteDataBaseHelper.getsmsTypelen() + sqliteDataBaseHelper.getEmailTypelen();

        int phoneper, smsper, emailper;

        if (total == 0) {

            phoneper = 0;

            smsper = 0;

            emailper = 0;

            trainingImage.setVisibility(View.VISIBLE);

            pie_view.setVisibility(View.GONE);

            LeadIconslayout.setVisibility(View.GONE);

        } else {

            phoneper = dataHelper.CalculatePercentage(sqliteDataBaseHelper.getPhoneTypelen(), total);

            smsper = dataHelper.CalculatePercentage(sqliteDataBaseHelper.getsmsTypelen(), total);

            emailper = dataHelper.CalculatePercentage(sqliteDataBaseHelper.getEmailTypelen(), total);

            trainingImage.setVisibility(View.GONE);

            pie_view.setVisibility(View.VISIBLE);

            LeadIconslayout.setVisibility(View.VISIBLE);
        }
        setupPieChart(pieView, phoneper, smsper, emailper);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trainingandsupport, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(LeadCapture.this, TrainingAndSupport.class);
                intent.putExtra("URL", "http://support.mindmesolo.com/#!/AddLeadForm");
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showProgressDialog(String Message) {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(Message);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void setupPieChart(PieView pieView, int phone, int sms, int email) {

        ArrayList<PieHelper> pieHelperArrayList = new ArrayList<PieHelper>();

        pieHelperArrayList.add(new PieHelper(phone, "Phone", Color.parseColor("#E67E22")));

        pieHelperArrayList.add(new PieHelper(sms, "Sms", Color.parseColor("#F1C40F")));

        pieHelperArrayList.add(new PieHelper(email, "Email", Color.parseColor("#E74C3C")));

        pieView.setDate(pieHelperArrayList);

        pieView.showPercentLabel(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        toggleButtonLead.setChecked(true);
    }

    private void getNotifications() {

        showProgressDialog("Please wait.");

        String org_id = sqliteDataBaseHelper.getOrganizationId();

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
                    }
                }
                setupUI();
            }
        });
    }

    // Get Organization Contacts from Server
    private void getOrganizationContacts() {

        Log.i(TAG, "Organization Contacts from server");

        String Fetch_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + sqliteDataBaseHelper.getOrganizationId() + "/contacts?code=ACTIVE&size=10&sortBy=date&asc=false";

        VolleyApi.getInstance().getJsonArray(getBaseContext(), Fetch_URL, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                if (result.success && result.dataIsArray()) {
                    JSONArray response = result.getDataAsArray();
                    try {
                        for (int j = 0; j < response.length(); j++) {
                            sqliteDataBaseHelper.insertNewContact(response.getJSONObject(j));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "VOLLEY  contacts:---->" + e.toString());
                    }
                }
            }
        });
    }
}