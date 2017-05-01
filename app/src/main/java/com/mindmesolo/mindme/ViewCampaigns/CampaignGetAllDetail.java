package com.mindmesolo.mindme.ViewCampaigns;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.Services.VolleyApi;
import com.mindmesolo.mindme.helper.CircleImageView;
import com.mindmesolo.mindme.helper.ConnectionDetector;
import com.mindmesolo.mindme.helper.Contacts;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.TagLayout;
import com.mindmesolo.mindme.helper.ToastShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;

/**
 * Created by eNest on 8/2/2016.
 */
public class CampaignGetAllDetail extends MainActivity {

    private static final String TAG = "CampaignGetAllDetail";

    SqliteDataBaseHelper mydbhelper;

    DataHelper dataHelper = new DataHelper();

    ProgressDialog nDialog = null;

    String CampaignId, CampaignName;

    TextView TV_campaign_title, TV_campaign_create, TV_campaign_credits, TV_RecipientsCount, TV_ResponseCount, TV_Recipients2;

    TagLayout Mytags;

    ProgressBar progressBarPlay, progressBarOpens;

    TextView TV_playper, TV_openper, TV_responseStatus;

    ListView listViewMedia;

    ListView List_view_for_CTA;

    CircleImageView circleImageView;

    RelativeLayout MediaLayout, response_layout;

    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mydbhelper = new SqliteDataBaseHelper(getBaseContext());

        Bundle bundle = getIntent().getExtras();

        CampaignId = bundle.getString("CampaignId");

        CampaignName = bundle.getString("CampaignName");


        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.single_campaign_detial_new, frameLayout, false);

        TextView CampaignTitle = (TextView) contentView.findViewById(R.id.CampaignTitle);
        CampaignTitle.setText("Campaign Details");

        drawer.addView(contentView, 0);

        toggleButtonCampaing.setChecked(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);

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

        toggle.syncState();
        mydbhelper = new SqliteDataBaseHelper(this);
        SetupUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        toggleButtonCampaing.setChecked(true);
    }

    private void SetupUI() {
        TV_campaign_title = (TextView) findViewById(R.id.campaign_title);
        TV_campaign_create = (TextView) findViewById(R.id.campaign_create);
        TV_campaign_credits = (TextView) findViewById(R.id.campaign_credits);
        TV_RecipientsCount = (TextView) findViewById(R.id.RecipientsCount);
        TV_Recipients2 = (TextView) findViewById(R.id.Recipients2);
        TV_ResponseCount = (TextView) findViewById(R.id.ResponceCount);
        Mytags = (TagLayout) findViewById(R.id.Recipientsitems);
        TV_responseStatus = (TextView) findViewById(R.id.responcestatus);
        progressBarOpens = (ProgressBar) findViewById(R.id.progress_bar_opens);
        progressBarPlay = (ProgressBar) findViewById(R.id.progress_bar_plays);
        TV_playper = (TextView) findViewById(R.id.playPercentage);
        TV_openper = (TextView) findViewById(R.id.openPercentage);

        List_view_for_CTA = (ListView) findViewById(R.id.List_view_for_CTA);

        listViewMedia = (ListView) findViewById(R.id.listviewMedia);

        circleImageView = (CircleImageView) findViewById(R.id.campaign_status);

        MediaLayout = (RelativeLayout) findViewById(R.id.MediaLayout);
        response_layout = (RelativeLayout) findViewById(R.id.response_layout);

        if (new ConnectionDetector(getBaseContext()).isConnectingToInternet()) {
            getOrganizationCampaigns();
        } else {
            ToastShow.setText(getBaseContext(), "please connect to internet", Toast.LENGTH_LONG);
        }
    }

    private void getOrganizationCampaigns() {
        this.nDialog = new ProgressDialog(this);
        nDialog.setMessage("Retrieving Campaign data");
        nDialog.setCancelable(true);
        nDialog.show();
        String org_id = new SqliteDataBaseHelper(getBaseContext()).getOrganizationId();
        String Fetch_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + org_id + "/campaigns/" + CampaignId + "";
        VolleyApi.getInstance().getJsonObject(getBaseContext(), Fetch_URL, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                nDialog.dismiss();
                if (result.success && result.dataIsObject()) {
                    JSONObject response = result.getDataAsObject();
                    SetupCampaignDetails(response);
                }
            }
        });
    }

    private void SetupCampaignDetails(JSONObject CampaignObj) {

        int responseCount = 0, openedCount = 0, playCount = 0;

        ArrayList<String> totalContactList;

        int totalContact;

        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM dd  yyyy @ hh:mm a");

        Calendar calendar = Calendar.getInstance();

        try {
            totalContactList = getTotalContacts(CampaignObj.getJSONArray("contacts"));

            totalContact = totalContactList.size();


            JSONArray jsonArrayComponents = CampaignObj.getJSONArray("components");

            JSONArray jsonArrayActivities = CampaignObj.getJSONArray("activities");

            JSONArray jsonArrayProperties = CampaignObj.getJSONArray("properties");

            //  ----------setup ListView For All Items Of CTA Elements---------//

            for (int j = 0; j < jsonArrayActivities.length(); j++) {
                String type = jsonArrayActivities.getJSONObject(j).getString("type");
                switch (type) {
                    case "RESPONDED":
                        responseCount++;
                        break;
                    case "OPENED":
                        openedCount++;
                        break;
                    case "PLAYED":
                        playCount++;
                        break;
                }
            }

            setupAllCTAResponse(jsonArrayComponents, jsonArrayActivities, jsonArrayProperties, playCount, totalContactList);

            TV_ResponseCount.setText(String.valueOf(responseCount));

            TV_responseStatus.setText(String.valueOf(responseCount));

            int perOpen = dataHelper.CalculatePercentage(openedCount, totalContact);

            int perPlay = dataHelper.CalculatePercentage(playCount, totalContact);

            if (perOpen <= 100) {
                TV_openper.setVisibility(View.VISIBLE);
                TV_openper.setText(String.valueOf(perOpen + "%"));
                progressBarOpens.setProgress(perOpen);
            } else if (perOpen > 100) {
                TV_openper.setVisibility(View.VISIBLE);
                TV_openper.setText(String.valueOf("100%"));
                progressBarOpens.setProgress(100);
            }
            if (perPlay <= 100) {
                TV_playper.setVisibility(View.VISIBLE);
                TV_playper.setText(String.valueOf(perPlay + "%"));
                progressBarPlay.setProgress(perPlay);
            } else if (perPlay > 100) {
                TV_playper.setVisibility(View.VISIBLE);
                TV_playper.setText(String.valueOf("100%"));
                progressBarPlay.setProgress(100);
            }

            TV_campaign_title.setText(CampaignObj.getString("name"));

            TV_campaign_credits.setText(CampaignObj.getString("credits") + " credits");

            calendar.setTimeInMillis(Long.parseLong(CampaignObj.getString("createdOn")));

            TV_campaign_create.setText(formatter.format(calendar.getTime()));

            TV_RecipientsCount.setText(String.valueOf(CampaignObj.getJSONArray("contacts").length()));

            TV_Recipients2.setText(String.valueOf(CampaignObj.getJSONArray("contacts").length()));
//            if (CampaignObj.getString("status").equalsIgnoreCase("SENT")) {
//                circleImageView.setVisibility(View.VISIBLE);
//            } else {
//                circleImageView.setVisibility(View.GONE);
//            }

            switch (CampaignObj.getString("status")) {
                case "SENT":
                    calendar.setTimeInMillis(CampaignObj.getLong("createdOn"));
                    TV_campaign_create.setText(formatter.format(calendar.getTime()));
                    circleImageView.setVisibility(View.VISIBLE);
                    break;
                case "SCHEDULED":
                    calendar.setTimeInMillis(CampaignObj.getLong("scheduleStartDate"));
                    TV_campaign_create.setText(formatter.format(calendar.getTime()));
                    circleImageView.setVisibility(View.VISIBLE);
                    circleImageView.setImageResource(R.drawable.ic_schdule);
                    break;
            }

            //---------------------------------get Campaign Recipients--------------//
            setTags(new JSONObject(CampaignObj.getJSONArray("properties").getJSONObject(0).getString("value")));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> getTotalContacts(JSONArray contacts) {
        ArrayList<String> TotalContacts = new ArrayList<>();
        for (int i = 0; i < contacts.length(); i++) {
            try {
                TotalContacts.add(contacts.getJSONObject(i).getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return TotalContacts;
    }

    private void setupAllCTAResponse(JSONArray jsonArrayComponents, JSONArray jsonArrayActivities, final JSONArray recipientKeyNames, final int playCount, final ArrayList<String> TotalContacts) {

        ArrayList<Contacts> CTAContactsList = new ArrayList<>();

        try {
            ArrayList<String> AllResponseContacts = new ArrayList<>();
            for (int i = 0; i < jsonArrayComponents.length(); i++) {
                switch (jsonArrayComponents.getJSONObject(i).getString("type")) {
                    case "CALLTOACTION":
                        JSONObject jsonObjectCallToAction = jsonArrayComponents.getJSONObject(i).getJSONObject("callToAction");
                        // Check media type Like YESNOMAYBE,POLL  or FEEDBACK TYPE;
                        switch (jsonObjectCallToAction.getString("type")) {
                            case "YESNOMAYBE":
                                JSONArray Buttons = jsonObjectCallToAction.getJSONObject("yesNoMaybe").getJSONArray("buttons");
                                for (int j = 0; j < Buttons.length(); j++) {
                                    String ButtonName = Buttons.getString(j);
                                    String Code = jsonObjectCallToAction.getJSONObject("yesNoMaybe").getString(ButtonName + "Code");
                                    ArrayList<String> Contacts = getContacts(jsonArrayActivities, Code);
                                    int progress = 0;
                                    if (Contacts.size() > 0) {

                                        AllResponseContacts.addAll(Contacts);

                                        progress = getProgress(Contacts.size(), TotalContacts.size());

                                    }
                                    String cap = ButtonName.substring(0, 1).toUpperCase() + ButtonName.substring(1);
                                    CTAContactsList.add(new Contacts(cap, String.valueOf(Contacts.size()), Contacts, progress));
                                }
                                // test Code
                                // Contacts that not response for the campaign.
                                addNoneContacts(AllResponseContacts, TotalContacts, CTAContactsList);
                                AllResponseContacts.clear();
                                break;
                            case "POLL":
                                JSONArray jsonArrayPoll = jsonObjectCallToAction.getJSONArray("pollItems");
                                for (int j = 0; j < jsonArrayPoll.length(); j++) {
                                    String PollID = jsonArrayPoll.getJSONObject(j).getString("id");
                                    String PollLabel = jsonArrayPoll.getJSONObject(j).getString("label");
                                    String cap = PollLabel.substring(0, 1).toUpperCase() + PollLabel.substring(1);
                                    ArrayList<String> Contacts = getContacts(jsonArrayActivities, PollID);
                                    int progress = 0;

                                    if (Contacts.size() > 0) {
                                        AllResponseContacts.addAll(Contacts);
                                        progress = getProgress(Contacts.size(), TotalContacts.size());
                                    }
                                    CTAContactsList.add(new Contacts(cap, String.valueOf(Contacts.size()), Contacts, progress));
                                }
                                // test Code
                                // Contacts that not response for the campaign.
                                addNoneContacts(AllResponseContacts, TotalContacts, CTAContactsList);
                                AllResponseContacts.clear();
                                break;
                            case "FEEDBACK":
                                final String[] val = new String[]{"1 Star", "2 Stars", "3 Stars", "4 Stars", "5 Stars"};
                                for (int i1 = 0; i1 < val.length; i1++) {
                                    String Name = val[i1];
                                    ArrayList<String> Contacts = getContacts(jsonArrayActivities, String.valueOf(i1 + 1));
                                    int progress = 0;

                                    if (Contacts.size() > 0) {
                                        AllResponseContacts.addAll(Contacts);
                                        progress = getProgress(Contacts.size(), TotalContacts.size());
                                    }
                                    CTAContactsList.add(new Contacts(Name, String.valueOf(Contacts.size()), Contacts, progress));
                                }
                                // test Code
                                // Contacts that not response for the campaign.
                                addNoneContacts(AllResponseContacts, TotalContacts, CTAContactsList);
                                AllResponseContacts.clear();
                                break;
                        }

                        break;
//------------------In case Media is available in Components------------//
                    case "VOICERECORDING":
                    case "VIDEO":
                        //--------------------if Play count >0 then viewPlayListView --------------//
                        MediaLayout.setVisibility(View.VISIBLE);

                        listViewMedia.setVisibility(View.VISIBLE);

                        ArrayList<Contacts> PlayedContactsList = new ArrayList<>();

                        ArrayList<String> Contacts = getContactsWhoPlayMedia(jsonArrayActivities, "PLAYED");

                        int progress = 0;

                        if (Contacts.size() > 0) {
                            progress = getProgress(Contacts.size(), TotalContacts.size());
                        }

                        PlayedContactsList.add(new Contacts("Played", String.valueOf(playCount), Contacts, progress));

                        CampaignViewAdapter campaignViewAdapter2 = new CampaignViewAdapter(getBaseContext(), R.layout.contactlistlayout, PlayedContactsList);

                        listViewMedia.setAdapter(campaignViewAdapter2);
                        dataHelper.setListViewHeightBasedOnItems(listViewMedia);
                        listViewMedia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                Contacts contact = campaignViewAdapter2.contactsList.get(position);

                                ArrayList Contacts = contact.getContacts();

                                ArrayList<Contacts> List = mydbhelper.getAllContactsBYContactIds(Contacts);

                                if (List.size() > 0) {
                                    Bundle dataBundle = new Bundle();
                                    dataBundle.putStringArrayList("Contactids", contact.getContacts());
                                    dataBundle.putString("tag", contact.getName());
                                    dataBundle.putString("recipientKeyNames", recipientKeyNames.toString());
                                    Intent intent = new Intent(getBaseContext(), CampaignContactView.class);
                                    intent.putExtras(dataBundle);
                                    startActivity(intent);
                                }
                            }
                        });
                        break;
                }
            }

            CampaignViewAdapter campaignViewAdapter = new CampaignViewAdapter(getBaseContext(), R.layout.contactlistlayout, CTAContactsList);

            if (CTAContactsList.size() > 0) {
                response_layout.setVisibility(View.VISIBLE);
                List_view_for_CTA.setVisibility(View.VISIBLE);
                List_view_for_CTA.setAdapter(campaignViewAdapter);
                DataHelper.getInstance().setListViewHeightBasedOnItems(List_view_for_CTA);
                List_view_for_CTA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Contacts contact = campaignViewAdapter.contactsList.get(position);

                        ArrayList Contacts = contact.getContacts();

                        ArrayList<Contacts> List = mydbhelper.getAllContactsBYContactIds(Contacts);

                        if (List.size() > 0) {
                            Bundle dataBundle = new Bundle();
                            dataBundle.putStringArrayList("Contactids", contact.getContacts());
                            dataBundle.putString("tag", contact.getName());
                            dataBundle.putString("recipientKeyNames", recipientKeyNames.toString());
                            Intent intent = new Intent(getBaseContext(), CampaignContactView.class);
                            intent.putExtras(dataBundle);
                            startActivity(intent);
                        }
                    }
                });
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int getProgress(int i, int i1) {

        float contact = (float) i;

        float contacts = (float) i1;

        float data = ((contact / contacts) * 100);

        return (int) data;
    }

    private void addNoneContacts(ArrayList<String> allResponseContacts, ArrayList<String> totalContacts, ArrayList<Contacts> ctaContactsList) {
        ArrayList<String> nonResponseContacts = new ArrayList<>();
        nonResponseContacts.addAll(totalContacts);
        Log.i("addNoneContacts", "TotalContacts : " + String.valueOf(totalContacts.size()));

        nonResponseContacts.removeAll(allResponseContacts);

        Log.i("addNoneContacts", "addNoneContacts : " + String.valueOf(nonResponseContacts.size()));

        //:LinkedHashSet is used for filter the array list for unique values in Array.
        LinkedHashSet<String> lhs = new LinkedHashSet<>();
        lhs.addAll(nonResponseContacts);
        nonResponseContacts.clear();
        nonResponseContacts.addAll(lhs);
        int NoneProgress = 0;
        if (nonResponseContacts.size() > 0) {
            //NoneProgress = (nonResponseContacts.size() / totalContacts.size()) * 100;

            NoneProgress = getProgress(nonResponseContacts.size(), totalContacts.size());

            ctaContactsList.add(new Contacts("None", String.valueOf(nonResponseContacts.size()), nonResponseContacts, NoneProgress));
        }
    }

    private ArrayList<String> getContactsWhoPlayMedia(JSONArray jsonArrayActivities, String type) {
        ArrayList<String> arrayList = new ArrayList<String>();
        try {
            for (int j = 0; j < jsonArrayActivities.length(); j++) {
                String type1 = null;
                type1 = jsonArrayActivities.getJSONObject(j).getString("type");
                if (type1.equals("PLAYED")) {
                    String contactId = jsonArrayActivities.getJSONObject(j).getString("contactId");
                    arrayList.add(contactId);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;

    }

    private ArrayList<String> getContacts(JSONArray jsonArrayActivities, String id) {
        ArrayList<String> arrayList = new ArrayList<String>();
        try {
            for (int j = 0; j < jsonArrayActivities.length(); j++) {
                String ResponseType = jsonArrayActivities.getJSONObject(j).getString("type");
                if (ResponseType.equals("RESPONDED")) {
                    String responseCode = jsonArrayActivities.getJSONObject(j).getString("responseCode");
                    if (responseCode.equals(id)) {
                        String contactId = jsonArrayActivities.getJSONObject(j).getString("contactId");
                        arrayList.add(contactId);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return arrayList;
    }

    private void setTags(JSONObject obj) {
        LayoutInflater layoutInflater = getLayoutInflater();
        String[] Tags = {"custom", "lists", "interests", "tags"};
        for (int i = 0; i < Tags.length; i++) {
            try {
                JSONArray tagsdata = obj.getJSONArray(Tags[i]);
                if (tagsdata.length() > 0) {
                    for (int j = 0; j < tagsdata.length(); j++) {
                        View tagView = layoutInflater.inflate(R.layout.lead_tag_layout, null, false);
                        TextView tagTextView = (TextView) tagView.findViewById(R.id.tagTextView);
                        switch (Tags[i]) {
                            case "custom":
                                tagTextView.setBackgroundResource(R.drawable.custom_shape);
                                break;
                            case "lists":
                                tagTextView.setBackgroundResource(R.drawable.lead_shape);
                                break;
                            case "interests":
                                tagTextView.setBackgroundResource(R.drawable.intrest_shape);
                                break;
                            case "tags":
                                tagTextView.setBackgroundResource(R.drawable.tag_shape);
                                break;
                        }
                        tagTextView.setText(String.valueOf(tagsdata.get(j)));
                        Mytags.addView(tagView);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            String all = obj.getString("All");
            if (all.equalsIgnoreCase("All")) {
                View tagView = layoutInflater.inflate(R.layout.lead_tag_layout, null, false);
                TextView tagTextView = (TextView) tagView.findViewById(R.id.tagTextView);
                tagTextView.setBackgroundResource(R.drawable.custom_shape);
                tagTextView.setText(String.valueOf("ALL"));
                Mytags.addView(tagView);
            }
        } catch (Exception e) {
            Log.i(TAG, "All tags not showing");
        }
    }
}
