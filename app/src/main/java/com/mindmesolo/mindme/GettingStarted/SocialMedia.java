package com.mindmesolo.mindme.GettingStarted;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.OrganizationModel;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.ToastShow;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by eNest on 30-08-2016.
 */

public class SocialMedia extends AppCompatActivity {

    private static final String TAG = "Social Media";

    public static SqliteDataBaseHelper dBhelper;

    public static DataHelper dataHelper = new DataHelper();
    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    public static String text = "userId",
            text2 = "userid",
            text3 = "userid",
            text4 = "userid",
            text5 = "userid",
            text6 = "userid",
            text7 = "userid";
    static boolean userChangeProfile = false;
    ProgressDialog pDialog;
    SocialMediaAdapter myCustomAdapter;
    ArrayList<SocialMediaModel> items;
    ListView SocialListView;
    Button savebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.socialmedia);
        preferences = getPreferences(MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Social Media");

        dBhelper = new SqliteDataBaseHelper(this);

        savebutton = (Button) findViewById(R.id.savebutton);

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog("Updating SocialMedia..");
                updateSocialMedia();
            }
        });

        prepareSocialUiComponents();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.greeting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            updateSocialMedia();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void prepareSocialUiComponents() {
        items = new ArrayList<SocialMediaModel>();
        items.add(new SocialMediaModel("Linkedln", dBhelper.getSocialId("Linkedln"), "www.linkedln.com/", R.drawable.linkedin));
        items.add(new SocialMediaModel("Facebook", dBhelper.getSocialId("Facebook"), "www.facebook.com/", R.drawable.facebook));
        items.add(new SocialMediaModel("Twitter", dBhelper.getSocialId("Twitter"), "www.twitter.com/", R.drawable.twitter));
        items.add(new SocialMediaModel("Google+", dBhelper.getSocialId("Google+"), "www.googleplus.com/", R.drawable.googleplus));
        items.add(new SocialMediaModel("Youtube", dBhelper.getSocialId("Youtube"), "www.youtube.com/", R.drawable.youtubeplay));
        items.add(new SocialMediaModel("Instagram", dBhelper.getSocialId("Instagram"), "www.instagram.com/", R.drawable.instagram));
        items.add(new SocialMediaModel("Pinterest", dBhelper.getSocialId("Pinterest"), "www.pinterest.com/", R.drawable.pinterest));

        SocialListView = (ListView) findViewById(R.id.SocialListView);

        myCustomAdapter = new SocialMediaAdapter(getBaseContext(), R.layout.social_media_row, items);

        SocialListView.setAdapter(myCustomAdapter);

        DataHelper.getInstance().setListViewHeightBasedOnItems(SocialListView);
    }

    // update social media links
    private void updateSocialMedia() {
        if (dBhelper.getSocialCount() < 6) {
            dBhelper.InsertIntoSocialMedia();
        } else {
            dBhelper.UpdateSocialMedia("Linkedln", SocialMedia.preferences.getString("gpref", SocialMedia.text));
            dBhelper.UpdateSocialMedia("Facebook", SocialMedia.preferences.getString("gpref2", SocialMedia.text));
            dBhelper.UpdateSocialMedia("Twitter", SocialMedia.preferences.getString("gpref3", SocialMedia.text));
            dBhelper.UpdateSocialMedia("Google+", SocialMedia.preferences.getString("gpref4", SocialMedia.text));
            dBhelper.UpdateSocialMedia("Youtube", SocialMedia.preferences.getString("gpref5", SocialMedia.text));
            dBhelper.UpdateSocialMedia("Instagram", SocialMedia.preferences.getString("gpref6", SocialMedia.text));
            dBhelper.UpdateSocialMedia("Pinterest", SocialMedia.preferences.getString("gpref7", SocialMedia.text));
            updateOrganization();
        }
    }


    // Main method for updating te social media
    public void updateOrganization() {
        startDialog("Updating Profile ...");
        try {

            final String jsonbodydata = getSocialMediaObject();

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, OrganizationModel.getApiBaseUrl() + dBhelper.getOrganizationId(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i(TAG, "Organization Update Success");
                            prepareSocialUiComponents();
                            try {
                                String GettingStartData = response.getJSONArray("properties").getJSONObject(0).getString("value");
                                SharedPreferences DASHBOARD = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = DASHBOARD.edit();
                                editor.putString(MainActivity.PREFS_KEY_GETTING_START, GettingStartData);
                                editor.commit();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e(TAG, e.toString());
                            }
                            pDialog.dismiss();
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(TAG, error.toString());
                            pDialog.dismiss();
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                ToastShow.setText(getBaseContext(), "Please Check your internet Connection", Toast.LENGTH_LONG);
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
                        return jsonbodydata == null ? null : jsonbodydata.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                jsonbodydata, "utf-8");
                        return null;
                    }
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", "Basic " + GetApiAccess());
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    9000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
        }
    }

    // get JOSN object of the social media items
    public String getSocialMediaObject() {
        final String[] SocialmediaItems = new String[]{"Linkedln", "Facebook", "Twitter", "Google+", "Youtube", "Instagram", "Pinterest"};
        final String[] SocialmediaUrls = new String[]
                {"www.linkedln.com", "www.facebook.com", "www.twitter.com", "www.googleplus.com", "www.youtube.com", "www.instagram.com", "www.pinterest.com"};
        JSONObject Orgnization = new JSONObject();
        JSONArray Social = new JSONArray();
        for (int i = 0; i < SocialmediaItems.length; i++) {
            if (dBhelper.getSocialId(SocialmediaItems[i]).length() > 2) {
                JSONObject socialMediaItem = new JSONObject();
                try {
                    socialMediaItem.put("name", SocialmediaItems[i]);
                    socialMediaItem.put("url", SocialmediaUrls[i]);
                    socialMediaItem.put("id", dBhelper.getSocialId(SocialmediaItems[i]));
                    Social.put(socialMediaItem);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            Orgnization.put("socials", Social);
            if (isSocialItemUpdated(Social) == true) {
                Orgnization.put("properties",
                        new JSONArray().put(
                                new JSONObject().put("name", "Greeting Started")
                                        .put("value", getPropertiesObject())));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Orgnization.toString();
    }

    // check if social items updated then update greeting data
    private boolean isSocialItemUpdated(JSONArray social) {
        ArrayList<String> List = new ArrayList<>();
        for (int i = 0; i < social.length(); i++) {
            try {
                String Item = social.getJSONObject(i).getString("id");
                if (StringUtils.isNotBlank(Item) || Item != null) {
                    List.add(Item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (List.size() > 0)
            return true;
        else return false;
    }

    // update getting started data
    private String getPropertiesObject() {

        SharedPreferences DASHBOARD = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);

        String GettingStartData = DASHBOARD.getString(MainActivity.PREFS_KEY_GETTING_START, null);

        StringBuilder stringBuilder;

        if (GettingStartData != null) {
            stringBuilder = new StringBuilder(GettingStartData);
            if (!GettingStartData.contains("Social Media")) {
                stringBuilder.append("," + "Social Media");
            }
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Social Media");
        }
        return String.valueOf(stringBuilder);
    }

    // progress dialog
    private void startDialog(String Message) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(true);
        pDialog.setMessage(Message);
        pDialog.show();
    }

    //api access token
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






