package com.mindmesolo.mindme.LeadRoutes;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.Services.VolleyApi;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.ToastShow;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User1 on 8/9/2016.
 */
public class PhoneRoute extends MainActivity {

    SwitchCompat switchPush, switchEmail, switchSms, switchNewLeads;

    SqliteDataBaseHelper dBhelper;

    TextView phone;

    ProgressDialog pDialog;
    private View.OnClickListener myClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pushNotification:
                case R.id.emailNotification:
                case R.id.smsNotification:
                case R.id.newCallerLead:
                    saveRouteData();
                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.phoneroute, frameLayout, false);
        drawer.addView(contentView, 0);
        dBhelper = new SqliteDataBaseHelper(this);

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
        toggleButtonLead.setChecked(true);

        phone = (TextView) findViewById(R.id.phone);
        phone.setText(dBhelper.getPhone());

        switchPush = (SwitchCompat) findViewById(R.id.pushNotification);
        switchPush.setOnClickListener(myClickListener);

        switchEmail = (SwitchCompat) findViewById(R.id.emailNotification);
        switchEmail.setOnClickListener(myClickListener);

        switchSms = (SwitchCompat) findViewById(R.id.smsNotification);
        switchSms.setOnClickListener(myClickListener);

        switchNewLeads = (SwitchCompat) findViewById(R.id.newCallerLead);
        switchNewLeads.setOnClickListener(myClickListener);

        initialize();
    }

    private void initialize() {
        String phoneRoute = dBhelper.getPhoneRoute();
        try {
            JSONObject jsonObject = new JSONObject(phoneRoute);
            switchPush.setChecked(jsonObject.getBoolean("sendPushNotification"));
            switchNewLeads.setChecked(jsonObject.getBoolean("designateNewCallerAsLead"));
            switchEmail.setChecked(jsonObject.getBoolean("sendEmailNotification"));
            switchSms.setChecked(jsonObject.getBoolean("sendSmsNotification"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void saveRouteData() {
        pDialog = new ProgressDialog(PhoneRoute.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please wait.");
        pDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("leadCapture", new JSONObject()
                    .put("emailLeadRoute", new JSONObject(dBhelper.getEmailRoute()))
                    .put("textLeadRoute", new JSONObject(dBhelper.getTextRoute()))
                    .put("phoneLeadRoute", new JSONObject(dBhelper.getPhoneRoute())
                            .put("sendPushNotification", switchPush.isChecked())
                            .put("sendEmailNotification", switchEmail.isChecked())
                            .put("sendSmsNotification", switchSms.isChecked())
                            .put("designateNewCallerAsLead", switchNewLeads.isChecked())
                            .put("enable", true)
                    )
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String REGISTER_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + dBhelper.getOrganizationId() + "";

        VolleyApi.getInstance().putJsonObject(PhoneRoute.this, REGISTER_URL, jsonObject.toString(), new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                if (result.success && result.dataIsObject()) {
                    JSONObject response = result.getDataAsObject();
                    try {
                        dBhelper.updateEmailRoute(response.getJSONObject("leadCapture").getJSONObject("emailLeadRoute").toString());
                        dBhelper.updateTextRoute(response.getJSONObject("leadCapture").getJSONObject("textLeadRoute").toString());
                        dBhelper.updatePhoneRoute(response.getJSONObject("leadCapture").getJSONObject("phoneLeadRoute").toString());
                        ToastShow.setText(PhoneRoute.this, "Update Successful.", Toast.LENGTH_LONG);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastShow.setText(getBaseContext(), "oops something went wrong. try again", Toast.LENGTH_LONG);
                }
            }
        });

    }
}
