package com.mindmesolo.mindme.LeadRoutes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.Services.VolleyApi;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.ToastShow;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by User1 on 8/10/2016.
 */
public class EditEmailRoute extends MainActivity {

    public static final String TAG = "EditEmailRoute";
    EditText editTextGreeting, editTextCta;
    ImageButton imageButton;
    DataHelper dh = new DataHelper();
    SqliteDataBaseHelper dBhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.editemailroute, frameLayout, false);
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

        Bundle bundle = getIntent().getExtras();
        String greeting = bundle.getString("Greeting");
        String Cta = bundle.getString("Cta");
        editTextGreeting = (EditText) findViewById(R.id.greeting);
        editTextCta = (EditText) findViewById(R.id.cta);

        greeting.replaceAll("\\s+", " ");
        Cta.replaceAll("\\s+", " ");

        editTextGreeting.setText(greeting);
        editTextCta.setText(dh.nullValidation(Cta));
        imageButton = (ImageButton) findViewById(R.id.checkLink);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Weblinkclick();
            }
        });

        ArrayList<String> userProfileSetup = dBhelper.getUserProfileData();
        if (userProfileSetup != null) {
            editTextGreeting.setHint("Hello! Thank you for interest in " + userProfileSetup.get(0) + ". We'll be touch in shortly.");
        }

        editTextGreeting.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public void Weblinkclick() {
        String url = "http://" + editTextCta.getText().toString();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.editroutemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save) {

            String GreetingMessage = editTextGreeting.getText().toString();

            String GreetingUrl = editTextCta.getText().toString();

            GreetingMessage.replaceAll("\\s+", " ");

            GreetingUrl.replaceAll("\\s+", " ");

            if (StringUtils.isBlank(GreetingMessage)) {
                editTextGreeting.setText("");
            }
            if (StringUtils.isBlank(GreetingUrl)) {
                editTextCta.setText("");
            }

            saveRouteData();
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveRouteData() {

        JSONObject jsonObject = new JSONObject();

        String GreetingMessage = editTextGreeting.getText().toString().trim();

        String GreetingUrl = editTextCta.getText().toString().trim();

        GreetingMessage.replaceAll("\\s+", " ");

        GreetingUrl.replaceAll("\\s+", " ");

        if (StringUtils.isBlank(GreetingMessage)) {
            GreetingMessage = editTextGreeting.getHint().toString();
        }
        if (StringUtils.isBlank(GreetingUrl)) {
            GreetingUrl = null;
        }

        try {
            jsonObject.put("leadCapture", new JSONObject()
                    .put("textLeadRoute", new JSONObject(dBhelper.getTextRoute()))
                    .put("phoneLeadRoute", new JSONObject(dBhelper.getPhoneRoute()))
                    .put("emailLeadRoute", new JSONObject(dBhelper.getEmailRoute())
                            .put("emailGreetingMessage", GreetingMessage)
                            .put("callToActionURL", GreetingUrl)
                            .put("enable", true)
                    )
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ProgressDialog pDialog = new ProgressDialog(EditEmailRoute.this);
        pDialog.setCancelable(true);
        pDialog.setMessage("Please wait.");
        pDialog.show();

        String REGISTER_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + dBhelper.getOrganizationId() + "";
        VolleyApi.getInstance().putJsonObject(EditEmailRoute.this, REGISTER_URL, jsonObject.toString(), new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
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
                        ToastShow.setText(EditEmailRoute.this, "Update Successful.", Toast.LENGTH_LONG);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "Error while updating ..." + TAG);
                    ToastShow.setText(getBaseContext(), "oops something went wrong. try again", Toast.LENGTH_LONG);
                }
            }
        });
    }
}
