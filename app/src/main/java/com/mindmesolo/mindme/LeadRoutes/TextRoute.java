package com.mindmesolo.mindme.LeadRoutes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.Services.VolleyApi;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.ToastShow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by User1 on 8/10/2016.
 */
public class TextRoute extends MainActivity {

    TextView textViewGreeting, textViewCta, simpleTextDataCharges;

    SwitchCompat switchPush, switchEmail, switchSms;

    SqliteDataBaseHelper dBhelper;

    TextView textViewCount, textViewtotalCount, textViewcreditDetail;

    TextView tv_mobile_number_with_keyword;

    RelativeLayout openLink;

    ProgressDialog pDialog;
    private View.OnClickListener myClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.openLink:
                    String URLString = textViewCta.getText().toString();
                    if (URLString.length() > 3) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse("http://" + URLString));
                        startActivity(i);
                    }
                    break;
                case R.id.pushNotification:
                case R.id.emailNotification:
                case R.id.smsNotification:
                    saveRouteData();
                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.textroute, frameLayout, false);

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
    }

    private void initialize() {

        toggleButtonLead.setChecked(true);

        simpleTextDataCharges = (TextView) findViewById(R.id.simpleTextDataCharges);
        simpleTextDataCharges.setText("Msg&Data rates may apply");

        textViewGreeting = (TextView) findViewById(R.id.greeting);

        textViewCta = (TextView) findViewById(R.id.cta);

        tv_mobile_number_with_keyword = (TextView) findViewById(R.id.tv_mobile_number_with_keyword);

        tv_mobile_number_with_keyword.setText("Text 'join' to " + dBhelper.getPhone());

        openLink = (RelativeLayout) findViewById(R.id.openLink);
        openLink.setOnClickListener(myClickListener);

        textViewCount = (TextView) findViewById(R.id.count);
        textViewtotalCount = (TextView) findViewById(R.id.totalCount);
        textViewcreditDetail = (TextView) findViewById(R.id.creditDetail);

        switchPush = (SwitchCompat) findViewById(R.id.pushNotification);
        switchPush.setOnClickListener(myClickListener);

        switchEmail = (SwitchCompat) findViewById(R.id.emailNotification);
        switchEmail.setOnClickListener(myClickListener);

        switchSms = (SwitchCompat) findViewById(R.id.smsNotification);
        switchSms.setOnClickListener(myClickListener);

        ArrayList<String> userProfileSetup = dBhelper.getUserProfileData();
        if (userProfileSetup != null) {
            textViewGreeting.setHint("Hello! Thank you for interest in " + userProfileSetup.get(0) + ". We'll be touch in shortly.");
        }


        String textRoute = dBhelper.getTextRoute();
        try {
            JSONObject jsonObject = new JSONObject(textRoute);
            textViewCta.setText(setTextString(jsonObject.getString("callToActionURL")));
            textViewGreeting.setText(setTextString(jsonObject.getString("smsGreetingMessage")));
            switchEmail.setChecked(jsonObject.getBoolean("sendEmailNotification"));
            switchPush.setChecked(jsonObject.getBoolean("sendPushNotification"));
            switchSms.setChecked(jsonObject.getBoolean("sendSmsNotification"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int textLength = textViewGreeting.length() + 62;
        if (textLength <= 160) {
            textViewCount.setText(String.valueOf(160 - textLength));
            textViewtotalCount.setText(String.valueOf(160));
            textViewcreditDetail.setTextColor(Color.parseColor("#01bfff"));
            textViewcreditDetail.setText(R.string.onecredit);
        } else {
            textViewCount.setText(String.valueOf(320 - textLength));
            textViewtotalCount.setText(String.valueOf(320));
            textViewcreditDetail.setTextColor(Color.parseColor("#ff0000"));
            textViewcreditDetail.setText(R.string.twocredit);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void saveRouteData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("leadCapture", new JSONObject()
                    .put("emailLeadRoute", new JSONObject(dBhelper.getEmailRoute()))
                    .put("phoneLeadRoute", new JSONObject(dBhelper.getPhoneRoute()))
                    .put("textLeadRoute", new JSONObject(dBhelper.getTextRoute())
                            .put("sendPushNotification", switchPush.isChecked())
                            .put("sendEmailNotification", switchEmail.isChecked())
                            .put("sendSmsNotification", switchSms.isChecked())
                            .put("enable", true)
                    )
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }


        pDialog = new ProgressDialog(TextRoute.this);
        pDialog.setCancelable(true);
        pDialog.setMessage("Please wait.");
        pDialog.show();

        String REGISTER_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + dBhelper.getOrganizationId() + "";
        VolleyApi.getInstance().putJsonObject(TextRoute.this, REGISTER_URL, jsonObject.toString(), new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
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
                        ToastShow.setText(TextRoute.this, "Update Successful.", Toast.LENGTH_LONG);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.routemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit) {
            Intent intent = new Intent(getBaseContext(), EditTextRoute.class);
            intent.putExtra("Greeting", textViewGreeting.getText().toString());
            intent.putExtra("Cta", textViewCta.getText().toString());
            startActivityForResult(intent, 2);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String setTextString(String data) {
        if ((data != null) && (!data.equalsIgnoreCase("null"))) {
            return data;
        } else return "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialize();
    }
}