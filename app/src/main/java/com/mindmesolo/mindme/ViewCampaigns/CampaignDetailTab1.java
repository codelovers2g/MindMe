package com.mindmesolo.mindme.ViewCampaigns;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
 * Created by eNest on 8/5/2016.
 */

public class CampaignDetailTab1 extends Fragment {

    private static final String TAG = "CampaignDetailTab1";

    View view;

    SqliteDataBaseHelper dBhelper;

    ProgressDialog nDialog;

    DataHelper dataHelper = new DataHelper();

    TextView TV_campaign_title, TV_campaign_create, TV_campaign_credits,
            TV_RecipientsCount, TV_ResponseCount, TV_Recipients2;

    TagLayout Mytags;

    RelativeLayout MediaLayout, response_layout;

    ProgressBar progressBarPlay, progressBarOpens;

    TextView TV_playper, TV_openper, TV_responseStatus;

    ListView listViewMedia;

    ListView List_view_for_CTA;

    CircleImageView circleImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.single_campaign_detail, container, false);

        dBhelper = new SqliteDataBaseHelper(getContext());

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);

        SetupUI();

        return view;
    }

    private void SetupUI() {

        TV_campaign_title = (TextView) view.findViewById(R.id.campaign_title);
        TV_campaign_create = (TextView) view.findViewById(R.id.campaign_create);
        TV_campaign_credits = (TextView) view.findViewById(R.id.campaign_credits);
        TV_RecipientsCount = (TextView) view.findViewById(R.id.RecipientsCount);
        TV_Recipients2 = (TextView) view.findViewById(R.id.Recipients2);
        TV_ResponseCount = (TextView) view.findViewById(R.id.ResponceCount);
        Mytags = (TagLayout) view.findViewById(R.id.Recipientsitems);

        TV_responseStatus = (TextView) view.findViewById(R.id.responcestatus);
        progressBarOpens = (ProgressBar) view.findViewById(R.id.progress_bar_opens);
        progressBarPlay = (ProgressBar) view.findViewById(R.id.progress_bar_plays);

        TV_playper = (TextView) view.findViewById(R.id.playPercentage);
        TV_openper = (TextView) view.findViewById(R.id.openPercentage);

        List_view_for_CTA = (ListView) view.findViewById(R.id.List_view_for_CTA);
        listViewMedia = (ListView) view.findViewById(R.id.listviewMedia);
        circleImageView = (CircleImageView) view.findViewById(R.id.campaign_status);
        MediaLayout = (RelativeLayout) view.findViewById(R.id.MediaLayout);
        response_layout = (RelativeLayout) view.findViewById(R.id.response_layout);

        if (new ConnectionDetector(getContext()).isConnectingToInternet()) {

            getOrganizationCampaigns();

        } else {
            ToastShow.setText(getContext(), "please connect to internet", Toast.LENGTH_LONG);
        }
    }

    private void getOrganizationCampaigns() {
        nDialog = new ProgressDialog(getContext());
        nDialog.setMessage("Retrieving latest Campaign data");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();
        String org_id = new SqliteDataBaseHelper(getContext()).getOrganizationId();
        String Fetch_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + org_id + "/campaigns/from/to";
        VolleyApi.getInstance().getJsonArray(getContext(), Fetch_URL, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                nDialog.dismiss();
                if (result.success && result.dataIsArray()) {
                    JSONArray response = result.getDataAsArray();
                    try {
                        SetupCampaignDetails(response.getJSONObject(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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


            TV_RecipientsCount.setText(String.valueOf(CampaignObj.getJSONArray("contacts").length()));

            TV_Recipients2.setText(String.valueOf(CampaignObj.getJSONArray("contacts").length()));

            switch (CampaignObj.getString("status")) {
                case "SENT":
                    calendar.setTimeInMillis(CampaignObj.getLong("createdOn"));
                    TV_campaign_create.setText(formatter.format(calendar.getTime()));
                    circleImageView.setVisibility(View.VISIBLE);
                    break;
                case "SCHEDULED":
                    Log.i("SchduleTime", CampaignObj.getString("scheduleStartDate"));
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

                                        //progress = (Contacts.size() / TotalContacts.size()) * 100;
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

                        CampaignViewAdapter campaignViewAdapter2 = new CampaignViewAdapter(getContext(), R.layout.contactlistlayout, PlayedContactsList);

                        listViewMedia.setAdapter(campaignViewAdapter2);
                        dataHelper.setListViewHeightBasedOnItems(listViewMedia);
                        listViewMedia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Contacts contact = campaignViewAdapter2.contactsList.get(position);
                                ArrayList Contacts = contact.getContacts();
                                ArrayList<Contacts> List = dBhelper.getAllContactsBYContactIds(Contacts);
                                if (List.size() > 0) {
                                    Bundle dataBundle = new Bundle();
                                    dataBundle.putStringArrayList("Contactids", contact.getContacts());
                                    dataBundle.putString("tag", contact.getName());
                                    dataBundle.putString("recipientKeyNames", recipientKeyNames.toString());
                                    Intent intent = new Intent(getContext(), CampaignContactView.class);
                                    intent.putExtras(dataBundle);
                                    startActivity(intent);
                                }
                            }
                        });
                        break;
                }
            }
            CampaignViewAdapter campaignViewAdapter = new CampaignViewAdapter(getContext(), R.layout.contactlistlayout, CTAContactsList);
            if (CTAContactsList.size() > 0) {
                List_view_for_CTA.setVisibility(View.VISIBLE);
                response_layout.setVisibility(View.VISIBLE);
                List_view_for_CTA.setAdapter(campaignViewAdapter);
                dataHelper.setListViewHeightBasedOnItems(List_view_for_CTA);
                List_view_for_CTA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Contacts contact = campaignViewAdapter.contactsList.get(position);

                        ArrayList<String> Contacts = contact.getContacts();

                        ArrayList<Contacts> List = dBhelper.getAllContactsBYContactIds(Contacts);

                        if (List.size() > 0) {
                            Bundle dataBundle = new Bundle();
                            dataBundle.putStringArrayList("Contactids", Contacts);
                            dataBundle.putString("tag", contact.getName());
                            dataBundle.putString("recipientKeyNames", recipientKeyNames.toString());
                            Intent intent = new Intent(getContext(), CampaignContactView.class);
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
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        String[] Tags = {"custom", "lists", "interests", "tags"};
        for (int i = 0; i < Tags.length; i++) {
            try {
                JSONArray tagsdata = obj.getJSONArray(Tags[i]);
                if (tagsdata.length() > 0) {
                    for (int j = 0; j <= tagsdata.length(); j++) {
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
}
