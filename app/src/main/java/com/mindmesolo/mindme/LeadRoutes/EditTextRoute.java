package com.mindmesolo.mindme.LeadRoutes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.Services.VolleyApi;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.ToastShow;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by User1 on 8/10/2016.
 */
public class EditTextRoute extends MainActivity {

    private static final String TAG = "Edit text";
    EditText editTextGreeting, editTextCta;

    ImageButton imageButton;

    int textLength;

    TextView textViewCreditInfo, textViewCount, textViewtotalCount;

    TextView editTextSmsGreeting;
    View contentView;
    SqliteDataBaseHelper dBhelper;
    private ViewGroup rootLayout;
    private boolean keyboardListenersAttached = false;
    private ViewTreeObserver.OnGlobalLayoutListener keyboardLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            int heightDiff = rootLayout.getRootView().getHeight() - rootLayout.getHeight();
            int contentViewTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();

            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(EditTextRoute.this);

            if (heightDiff <= contentViewTop) {
                onHideKeyboard();
                Intent intent = new Intent("KeyboardWillHide");
                broadcastManager.sendBroadcast(intent);

            } else {
                int keyboardHeight = heightDiff - contentViewTop;
                onShowKeyboard(keyboardHeight);
                Intent intent = new Intent("KeyboardWillShow");
                intent.putExtra("KeyboardHeight", keyboardHeight);
                broadcastManager.sendBroadcast(intent);

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        contentView = inflater.inflate(R.layout.edittextroute, frameLayout, false);
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
        editTextGreeting = (EditText) findViewById(R.id.greeting);
        editTextCta = (EditText) findViewById(R.id.cta);

        editTextSmsGreeting = (TextView) findViewById(R.id.editTextSmsGreeting);

        textViewCreditInfo = (TextView) findViewById(R.id.creditDetail);
        textViewCount = (TextView) findViewById(R.id.count);
        textViewtotalCount = (TextView) findViewById(R.id.totalCount);
        imageButton = (ImageButton) findViewById(R.id.checkLink);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Weblinkclick();
            }
        });

        textLength = editTextGreeting.length() + 62;
        if (textLength <= 160) {
            textViewCount.setText(String.valueOf(160 - textLength));
            textViewtotalCount.setText(String.valueOf(160));
            textViewCreditInfo.setTextColor(Color.parseColor("#01bfff"));
            textViewCreditInfo.setText("Your message will use 1 Credit");
        } else {
            textViewCount.setText(String.valueOf(320 - textLength));
            textViewtotalCount.setText(String.valueOf(320));
            textViewCreditInfo.setTextColor(Color.parseColor("#ff0000"));
            textViewCreditInfo.setText("Your message will use 2 Credit");
        }
        editTextGreeting.setFilters(new InputFilter[]{new InputFilter.LengthFilter(258)});
        editTextGreeting.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length() + 62;
                textLength = length;
                if (length <= 160) {
                    textViewCount.setText(String.valueOf(160 - length));
                    textViewtotalCount.setText(String.valueOf(160));
                    textViewCreditInfo.setTextColor(Color.parseColor("#01bfff"));
                    textViewCreditInfo.setText("Your message will use 1 Credit");
                }
                if (length > 160 && length <= 320) {
                    textViewCount.setText(String.valueOf(320 - length));
                    textViewtotalCount.setText(String.valueOf(320));
                    textViewCreditInfo.setTextColor(Color.parseColor("#ff0000"));
                    textViewCreditInfo.setText("Your message will use 2 Credit");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

        });
        String greeting = getIntent().getStringExtra("Greeting");
        String Cta = getIntent().getStringExtra("Cta");


        ArrayList<String> userProfileSetup = dBhelper.getUserProfileData();
        if (userProfileSetup != null) {
            editTextGreeting.setHint("Hello! Thank you for interest in " + userProfileSetup.get(0) + ". We'll be touch in shortly.");
        }

        editTextGreeting.setText(greeting);
        editTextCta.setText(Cta);
        attachKeyboardListeners();
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


            saveRouteData();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onShowKeyboard(int keyboardHeight) {
    }

    protected void onHideKeyboard() {
    }

    protected void attachKeyboardListeners() {
        if (keyboardListenersAttached) {
            return;
        }
        rootLayout = (ViewGroup) findViewById(R.id.rootlayout);
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(keyboardLayoutListener);
        keyboardListenersAttached = true;
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
                    .put("emailLeadRoute", new JSONObject(dBhelper.getEmailRoute()))
                    .put("phoneLeadRoute", new JSONObject(dBhelper.getPhoneRoute()))
                    .put("textLeadRoute", new JSONObject(dBhelper.getTextRoute())
                            .put("smsGreetingMessage", GreetingMessage)
                            .put("callToActionURL", GreetingUrl)
                    )
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ProgressDialog pDialog = new ProgressDialog(EditTextRoute.this);
        pDialog.setCancelable(true);
        pDialog.setMessage("Please wait.");
        pDialog.show();

        String REGISTER_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + dBhelper.getOrganizationId() + "";
        VolleyApi.getInstance().putJsonObject(EditTextRoute.this, REGISTER_URL, jsonObject.toString(), new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
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
                        ToastShow.setText(EditTextRoute.this, "Update Successful.", Toast.LENGTH_LONG);
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
    protected void onDestroy() {
        super.onDestroy();
        if (keyboardListenersAttached) {
            rootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(keyboardLayoutListener);
        }
    }
}
