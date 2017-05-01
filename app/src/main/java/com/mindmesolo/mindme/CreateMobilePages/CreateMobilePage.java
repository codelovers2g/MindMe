package com.mindmesolo.mindme.CreateMobilePages;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mindmesolo.mindme.CreateCampaign.CreateEmailOption;
import com.mindmesolo.mindme.CreateCampaign.MediaTypes;
import com.mindmesolo.mindme.CreateMobilePages.Adapters.WelComePageBuilderAdapter;
import com.mindmesolo.mindme.CreateMobilePages.models.BusinessHours;
import com.mindmesolo.mindme.CreateMobilePages.models.MapModel;
import com.mindmesolo.mindme.CreateMobilePages.models.SpecialOffer;
import com.mindmesolo.mindme.GettingStarted.SocialMediaModel;
import com.mindmesolo.mindme.Models.CommonModel;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.Services.VolleyApi;
import com.mindmesolo.mindme.ViewCampaigns.CampaignData;
import com.mindmesolo.mindme.helper.CampaignAndMobilePageHelper;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.OnStartDragListener;
import com.mindmesolo.mindme.helper.RuntimePermissionsActivity;
import com.mindmesolo.mindme.helper.SimpleItemTouchHelperCallback;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.ToastShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by enest_09 on 11/2/2016.
 */

public class CreateMobilePage extends RuntimePermissionsActivity implements View.OnClickListener, OnStartDragListener {

    public static final String TAG = "CreateMobilePage";
    private static final int REQUEST_PERMISSIONS = 20;
    public static boolean CameraPermission = false;
    public ArrayList<CampaignData> List = new ArrayList<>();
    public RecyclerView recyclerView;
    public ItemTouchHelper mItemTouchHelper;
    ImageButton Add;
    Button btnContinue;
    CampaignAndMobilePageHelper campaignAndMobilePageHelper;
    SqliteDataBaseHelper sqliteDataBaseHelper;
    private WelComePageBuilderAdapter pageBuilderAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.wel_come_page_builder);

        campaignAndMobilePageHelper = new CampaignAndMobilePageHelper(getBaseContext());

        sqliteDataBaseHelper = new SqliteDataBaseHelper(this);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        Add = (ImageButton) findViewById(R.id.Add);
        Add.setOnClickListener(this);

        btnContinue = (Button) findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(this);

        if (getIntent().getExtras() != null) {
            String EditMobilePage = getIntent().getStringExtra("EditMobilePageId");
            if (EditMobilePage != null) {
                editMobilePageById(EditMobilePage);
            } else {
                prepareData(getIntent().getStringArrayListExtra("MobilePageBuilderElements"));
            }
        }
        checkPermissions();
        setListView();
    }

    private void editMobilePageById(String editMobilePage) {
        DataHelper.getInstance().startDialog(CreateMobilePage.this, "Getting page information.");
        String org_id = new SqliteDataBaseHelper(getBaseContext()).getOrganizationId();
        String REGISTER_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + org_id + "/mobilePage/" + editMobilePage;
        VolleyApi.getInstance().getJsonObject(getBaseContext(), REGISTER_URL,
                new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
                    @Override
                    public void onCompletion(VolleyApi.ApiResult result) {
                        DataHelper.getInstance().stopDialog();
                        if (result.success && result.dataIsObject()) {
                            JSONObject response = result.getDataAsObject();
                            handleResponseOfData(response);
                            Log.i("mobilePage", response.toString());
                        }
                    }
                });
    }

    // this method is used for Regenerating the views from the data comping from the server
    private void handleResponseOfData(JSONObject response) {
        List = new ArrayList<>();
        try {
            JSONArray Components = response.getJSONArray("components");
            if (Components != null && Components.length() > 0) {
                for (int i = 0; i < Components.length(); i++) {
                    JSONObject obj = Components.getJSONObject(i);
                    String type = obj.getString("type");
                    switch (type) {
                        case MediaTypes.IMAGE:
//                            AddNewItem.setTitle("Image");
//                            AddNewItem.setImage(R.drawable.camera);
//                            setImage(AddNewItem, intentData);
//                            List.add(AddNewItem);
                            break;
                        case MediaTypes.VIDEO:
//                            AddNewItem.setTitle("Video");
//                            AddNewItem.setImage(R.drawable.play);
//                            setVideoItems(AddNewItem,intentData);
//                            List.add(AddNewItem);
                            break;
                        case MediaTypes.AUDIO:
//                            AddNewItem.setTitle("Audio");
//                            AddNewItem.setImage(R.drawable.microphone);
//                            setAudioData(AddNewItem,intentData);
//                            List.add(AddNewItem);
                            break;
                        case MediaTypes.TITLE:
                            List.add(getTitleData(obj));
                            break;
                        case MediaTypes.PARAGRAPH:
                            List.add(getParaGraphData(obj));
                            break;
                        case MediaTypes.CALL_TO_ACTION_FOR_MOBILE_PAGE:
                            String Type = obj.getJSONObject("callToAction").getString("type");
                            switch (Type) {
                                case MediaTypes.YESNOMAYBE:
                                case MediaTypes.RESPONSE:
                                    CampaignData yesNoMayBe = new CampaignData();
                                    yesNoMayBe.setTitle("Response");
                                    yesNoMayBe.setImage(R.drawable.response);
                                    yesNoMayBe.setExtraData(getYesNoMayBeData(obj));
                                    List.add(yesNoMayBe);
                                    break;
                                case MediaTypes.POLL:
                                    CampaignData Poll = new CampaignData();
                                    Poll.setTitle("Poll");
                                    Poll.setImage(R.drawable.poll);
                                    Poll.setExtraData(getYesNoMayBeData(obj));
                                    List.add(Poll);
                                    break;
                                case MediaTypes.LINKCALL:
                                case MediaTypes.CALL_TO_ACTION:
                                    CampaignData LinkCall = new CampaignData();
                                    LinkCall.setTitle("Link/Call");
                                    LinkCall.setImage(R.drawable.linkcall);
                                    LinkCall.setExtraData(getYesNoMayBeData(obj));
                                    List.add(LinkCall);
                                    break;
                                case MediaTypes.FEEDBACK:
                                    CampaignData Feedback = new CampaignData();
                                    Feedback.setTitle("Feedback");
                                    Feedback.setImage(R.drawable.feedback);
                                    Feedback.setExtraData(getYesNoMayBeData(obj));
                                    List.add(Feedback);
                                    break;
                            }
                            break;
                        case MediaTypes.LEADFORM:
                            List.add(getLeadFormData(obj));
                            break;
                        case MediaTypes.SOCIAL:
//                            AddNewItem.setTitle("Social");
//                            AddNewItem.setImage(R.drawable.social_icon);
//                            AddNewItem.setSocialMediaData(intentData.getParcelableArrayListExtra("SocialMediaData"));
//                            List.add(AddNewItem);
                            break;
                        case MediaTypes.HOURS:
//                            AddNewItem.setTitle("Hours");
//                            AddNewItem.setImage(R.drawable.hour_icon);
//                            AddNewItem.setHoursSwitch(intentData.getParcelableExtra("get_switches_data"));
//                            AddNewItem.setBusinessHoursData(intentData.getParcelableArrayListExtra("get_BusinessHours"));
//                            List.add(AddNewItem);
                            break;
                        case MediaTypes.SPECIAL_OFFERS:
//                            AddNewItem.setTitle("Special Offer");
//                            AddNewItem.setImage(R.drawable.special_offer_icon);
//                            AddNewItem.setSpecialOffer(intentData.getParcelableExtra("get_specialOffer_data"));
//                            List.add(AddNewItem);
                            break;
                        case MediaTypes.MAP:
//                            AddNewItem.setTitle("Map");
//                            AddNewItem.setImage(R.drawable.map_icon);
//                            AddNewItem.setMapModel(intentData.getParcelableExtra("map_data"));
                            break;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setListView();
    }

    private CampaignData getLeadFormData(JSONObject obj) {
        CampaignData campaignData = new CampaignData();
        campaignData.setTitle("Lead Form");
        campaignData.setImage(R.drawable.lead_form_icon);
        try {
            JSONArray LeadCaptureItems = obj.getJSONObject("leadCaptureComponent").getJSONArray("leadCaptureElementTypes");
            ArrayList<CommonModel> LeadFormData = new ArrayList<>();
            ArrayList<CommonModel> LeadFormInterestsData = new ArrayList<>();
            for (int i = 0; i < LeadCaptureItems.length(); i++) {
                JSONArray properties = LeadCaptureItems.getJSONObject(i).getJSONArray("properties");
                String Title = properties.getString(0);
                Boolean Required = properties.getBoolean(2);
                LeadFormData.add(new CommonModel(Title, Required, true));
            }
            campaignData.setLeadFormData(LeadFormData);
            JSONArray Interests = obj.getJSONObject("leadCaptureComponent").getJSONArray("lists");
            if (Interests.length() > 0) {
                for (int i = 0; i < Interests.length(); i++) {
                    LeadFormInterestsData.add(new CommonModel(Interests.getString(i), true));
                }
                campaignData.setLeadFormInterestsData(LeadFormInterestsData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return campaignData;
    }

    private String getYesNoMayBeData(JSONObject obj) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("message", obj.getString("message"));
            jsonObject.put("callToAction", obj.getJSONObject("callToAction"));
            jsonObject.put("properties", obj.getJSONArray("properties"));
            jsonObject.put("type", "CALLTOACTIONFORMOBILEPAGE");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private CampaignData getParaGraphData(JSONObject obj) {
        CampaignData campaignData = new CampaignData();
        campaignData.setTitle("Paragraph");
        campaignData.setImage(R.drawable.paragraph_icon);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("message", obj.getString("message"));
            jsonObject.put("properties", obj.getJSONArray("properties"));
            jsonObject.put("type", "PARAGRAPH");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        campaignData.setExtraData(jsonObject.toString());
        return campaignData;
    }

    private CampaignData getTitleData(JSONObject obj) {
        CampaignData campaignData = new CampaignData();
        campaignData.setTitle("Title");
        campaignData.setImage(R.drawable.titleicon);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("message", obj.getString("message"));
            jsonObject.put("properties", obj.getJSONArray("properties"));
            jsonObject.put("type", "TITLE");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        campaignData.setExtraData(jsonObject.toString());
        return campaignData;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        if (requestCode == REQUEST_PERMISSIONS) {
            CameraPermission = true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void checkPermissions() {
        CreateMobilePage.super.requestAppPermissions(new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                R.string.runtime_permissions_txt
                , REQUEST_PERMISSIONS);
    }

    private void setListView() {

        pageBuilderAdapter = new WelComePageBuilderAdapter(CreateMobilePage.this, List, this);

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(pageBuilderAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(pageBuilderAdapter);

        mItemTouchHelper = new ItemTouchHelper(callback);

        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    // prepare listView according to the new items.
    private void prepareData(ArrayList<String> ListItems) {
        for (String Item : ListItems) {
            switch (Item) {
                case "Logo":
                    List.add(new CampaignData("Logo", R.drawable.logo_icon));
                    break;
                case "Title":
                    List.add(new CampaignData("Title", R.drawable.titleicon));
                    break;
                case "Media":
                    List.add(new CampaignData("Image", R.drawable.camera));
                    break;
                case "Paragraph":
                    List.add(new CampaignData("Paragraph", R.drawable.paragraph_icon));
                    break;
                case "Hours":
                    List.add(new CampaignData("Hours", R.drawable.hour_icon));
                    break;
                case "Map":
                    List.add(new CampaignData("Map", R.drawable.map_icon));
                    break;
                case "Social":
                    List.add(new CampaignData("Social", R.drawable.social_icon));
                    break;
                case "Lead Form":
                    List.add(new CampaignData("Lead Form", R.drawable.lead_form_icon));
                    break;
                case "Call to Action":
                    List.add(new CampaignData("Call to Action", R.drawable.phone_call));
                    break;
                case "Special Offer":
                    List.add(new CampaignData("Special Offer", R.drawable.special_offer_icon));
                    break;
            }
        }
    }

    // handle all incoming results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String result = data.getStringExtra("result");
            CampaignData ListItem = new CampaignData();
            if (pageBuilderAdapter.List.size() > 0) {
                ListItem = pageBuilderAdapter.List.get(pageBuilderAdapter.getPosition());
            }
            switch (requestCode) {
                case 1:
                    campaignAndMobilePageHelper.updateListView(data, pageBuilderAdapter.List);
                    break;
                case 2:
                    ListItem.setExtraData(result);
                    campaignAndMobilePageHelper.setCTAElements(ListItem, result);
                    pageBuilderAdapter.notifyDataSetChanged();
                    break;
                case 3:
                    campaignAndMobilePageHelper.imageFromCameraTest(data, ListItem);
                    break;
                case 4:
                    campaignAndMobilePageHelper.getVideo(data, ListItem);
                    campaignAndMobilePageHelper.getVideoImage(data, ListItem);
                    break;
                case 5:
                    campaignAndMobilePageHelper.getAudio(data, ListItem);
                    break;
                case 6:
                    campaignAndMobilePageHelper.imageFromAdobeSdk(data, ListItem);
                    break;
                case 7:
                    campaignAndMobilePageHelper.addBusinessHours(data, ListItem);
                    break;
                case 8:
                    campaignAndMobilePageHelper.addMap(data, ListItem);
                    break;
                case 9:
                    campaignAndMobilePageHelper.addSocial(data, ListItem);
                    break;
                case 10:
                    campaignAndMobilePageHelper.addLeadForm(data, ListItem);
                    break;
                case 11:
                    campaignAndMobilePageHelper.addSpecialOffers(data, ListItem);
                    break;
            }
            pageBuilderAdapter.notifyDataSetChanged();
        }
    }

    private JSONArray getAllComponents() {
        JSONArray jsonArrayComponent = new JSONArray();
        int i = 1;
        for (CampaignData data : pageBuilderAdapter.List) {
            switch (data.getTitle()) {
                case "Video":
                    videoUpdateData(i, data, jsonArrayComponent);
                    break;
                case "Lead Form":
                    leadFormUpdateData(i, data, jsonArrayComponent);
                    break;
                case "Social":
                    socialUpdateData(i, data, jsonArrayComponent);
                    break;
                case "Hours":
                    hoursUpdateData(i, data, jsonArrayComponent);
                    break;
                case "Special Offer":
                    specialOfferUpdateData(i, data, jsonArrayComponent);
                    break;
                case "Map":
                    mapUpdateData(i, data, jsonArrayComponent);
                    break;

            }
            if (data.getExtraData() != null) {
                try {
                    JSONObject jsonObject = new JSONObject(data.getExtraData());
                    jsonObject.put("order", i);
                    jsonObject.put("orgId", sqliteDataBaseHelper.getOrganizationId());
                    jsonArrayComponent.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            i++;
        }
        return jsonArrayComponent;
    }

    private void mapUpdateData(int i, CampaignData data, JSONArray jsonArrayComponent) {
        JSONObject mainObject = new JSONObject();
        JSONObject mapObject = new JSONObject();
        MapModel mapModel = data.getMapModel();
        if (mapModel != null) {
            try {
                mapObject.put("name", mapModel.getName());
                switch (mapModel.getType()) {
                    case "Map":
                        //mainObject.put("media", mapModel.getMapImage());
                        mapObject.put("properties", new JSONArray()
                                .put(new JSONObject().put("value", mapModel.getLatitude()).put("name", "latitude"))
                                .put(new JSONObject().put("value", mapModel.getLongitude()).put("name", "longitude")));
                        break;

                    case "Address":
                        mapObject.put("properties", new JSONArray()
                                .put(new JSONObject().put("value", mapModel.getAddress()).put("name", "address"))
                                .put(new JSONObject().put("value", mapModel.getLatitude()).put("name", "latitude"))
                                .put(new JSONObject().put("value", mapModel.getLongitude()).put("name", "longitude")));
                        break;
                    case "Map Address":
                        //mainObject.put("media", mapModel.getMapImage());
                        mapObject.put("properties", new JSONArray()
                                .put(new JSONObject().put("value", mapModel.getAddress()).put("name", "address"))
                                .put(new JSONObject().put("value", mapModel.getLatitude()).put("name", "latitude"))
                                .put(new JSONObject().put("value", mapModel.getLongitude()).put("name", "longitude")));
                        break;
                }
                mainObject.put("map", mapObject);
                mainObject.put("order", i);
                mainObject.put("type", "MAP");
                mainObject.put("orgId", sqliteDataBaseHelper.getOrganizationId());
                jsonArrayComponent.put(mainObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void specialOfferUpdateData(int i, CampaignData data, JSONArray jsonArrayComponent) {
        JSONObject mainObject = new JSONObject();
        JSONObject specialOfferObject = new JSONObject();
        SpecialOffer specialOffer = data.getSpecialOffer();
        if (specialOffer != null) {
            try {
                mainObject.put("type", "SPECIALOFFER");
                specialOfferObject.put("name", specialOffer.getName());
                specialOfferObject.put("description", specialOffer.getDescription());
                String SectionOne = specialOffer.getOfferType();
                if (SectionOne != null) {
                    switch (SectionOne) {
                        case "Free":
                            specialOfferObject.put("offer", new JSONObject()
                                    .put("type", "FREE"));
                            break;
                        case "Save":
                            specialOfferObject.put("offer", new JSONObject()
                                    .put("type", "SAVE")
                                    .put("properties", new JSONArray()
                                            .put(new JSONObject().put("value", specialOffer.getOfferAmount()).put("name", "offerAmount"))
                                            .put(new JSONObject().put("value", specialOffer.getOfferUnit()).put("name", "offerUnit"))
                                    ));
                            break;
                        case "Other":
                            specialOfferObject.put("offer", new JSONObject()
                                    .put("type", "OTHER")
                                    .put("properties", new JSONArray()
                                            .put(new JSONObject().put("value", specialOffer.getOther()).put("name", "other")))
                            );
                            break;
                    }
                }
                String SectionTwo = specialOffer.getExpireType();
                if (SectionTwo != null) {
                    //Expiration //
                    switch (SectionTwo) {
                        case "None":
                            specialOfferObject.put("expire", new JSONObject()
                                    .put("type", "NONE")
                            );
                            break;
                        case "After":
                            specialOfferObject.put("expire", new JSONObject()
                                    .put("type", "AFTER")
                                    .put("properties", new JSONArray()
                                            .put(new JSONObject().put("value", specialOffer.getExpireDate()).put("name", "date")))
                            );
                            break;
                        case "On Date":
                            specialOfferObject.put("expire", new JSONObject()
                                    .put("type", "ONDATE")
                                    .put("properties", new JSONArray()
                                            .put(new JSONObject().put("value", specialOffer.getExpireDate()).put("name", "date")))
                            );
                            break;
                    }
                }
                String SectionThree = specialOffer.getDisplayOfferOn();
                if (SectionThree != null) {
                    //Display On //
                    switch (SectionThree) {
                        case "This Page":
                            specialOfferObject.put("displayOffer", new JSONObject());
                            break;

                        case "Thank You":
                            specialOfferObject.put("displayOffer", new JSONObject());
                            break;

                        case "Both":
                            specialOfferObject.put("displayOffer", new JSONObject());
                            break;
                    }
                }
                mainObject.put("specialOffer", specialOfferObject);
                mainObject.put("order", i);
                jsonArrayComponent.put(mainObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void hoursUpdateData(int i, CampaignData data, JSONArray jsonArrayComponent) {
        try {
            ArrayList<BusinessHours> businessHoursData = data.getBusinessHoursData();
            if (businessHoursData != null) {
                JSONObject businessHoursObj = new JSONObject();
                JSONArray includeds = new JSONArray();
                JSONArray excludeds = new JSONArray();
                for (BusinessHours businessHours : businessHoursData) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("dayOfWeek", businessHours.getDayOfWeek());
                    jsonObject.put("openOnHour", businessHours.getOpenOnHour());
                    jsonObject.put("openOnMinute", businessHours.getOpenOnMinute());
                    jsonObject.put("closeOnHour", businessHours.getCloseOnHour());
                    jsonObject.put("closeOnMinute", businessHours.getCloseOnMinute());
                    jsonObject.put("byAppointmentOnly", businessHours.getByAppointmentOnly());
                    if (businessHours.getClosed()) {
                        excludeds.put(jsonObject);
                    } else {
                        includeds.put(jsonObject);
                    }
                }
                JSONObject businessHoursType = new JSONObject();
                businessHoursType.put("includeds", includeds);
                businessHoursType.put("excludeds", excludeds);
                businessHoursObj.put("businessHoursType", businessHoursType);
                businessHoursObj.put("order", i);
                businessHoursObj.put("orgId", sqliteDataBaseHelper.getOrganizationId());
                businessHoursObj.put("type", "BUSINESSHOURS");
                businessHoursObj.put("order", i);
                jsonArrayComponent.put(businessHoursObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void videoUpdateData(int i, CampaignData data, JSONArray jsonArrayComponent) {
        if (data.getVideoExtraData() != null) {
            try {
                if (data.getTitle().equalsIgnoreCase("Video")) {
                    JSONObject jsonObject = new JSONObject(data.getVideoExtraData());
                    jsonObject.put("order", i);
                    jsonObject.put("orgId", sqliteDataBaseHelper.getOrganizationId());
                    jsonArrayComponent.put(jsonObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void socialUpdateData(int i, CampaignData data, JSONArray jsonArrayComponent) {
        ArrayList<SocialMediaModel> socialMediaData = data.getSocialMediaData();
        if (socialMediaData != null) {
            try {
                JSONObject SocialMediaObject = new JSONObject();
                JSONArray properties = new JSONArray();
                for (SocialMediaModel socialMediaModel : socialMediaData) {
                    properties.put(new JSONObject().put("name", socialMediaModel.getSocialMediaName()).put("value", socialMediaModel.getSocialMediaId()));
                }
                SocialMediaObject.put("social", new JSONObject().put("properties", properties));
                SocialMediaObject.put("order", i);
                SocialMediaObject.put("orgId", sqliteDataBaseHelper.getOrganizationId());
                SocialMediaObject.put("type", "SOCIAL");
                SocialMediaObject.put("order", i);
                jsonArrayComponent.put(SocialMediaObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void leadFormUpdateData(int i, CampaignData data, JSONArray jsonArrayComponent) {
        ArrayList<CommonModel> commonModels = data.getLeadFormData();
        if (commonModels != null) {
            try {
                JSONObject leadFormObject = new JSONObject();

                JSONArray leadCaptureElementTypes = new JSONArray();
                for (CommonModel model : commonModels) {
                    JSONArray properties = new JSONArray();
                    properties.put(new JSONObject().put("name", "name").put("value", model.getTitle()));
                    properties.put(new JSONObject().put("name", "label").put("value", model.getTitle()));
                    properties.put(new JSONObject().put("name", "required").put("value", model.getRequired()));
                    properties.put(new JSONObject().put("name", "show").put("value", model.getChecked()));
                    leadCaptureElementTypes.put(new JSONObject().put("properties", properties));
                }
                leadFormObject.put("leadCaptureComponent",
                        new JSONObject().put("leadCaptureElementTypes", leadCaptureElementTypes)
                                .put("lists", getInterests(data.getLeadFormInterestsData()))
                );

                leadFormObject.put("orgId", sqliteDataBaseHelper.getOrganizationId());
                leadFormObject.put("type", "LEADCAPTURE");
                leadFormObject.put("order", i);
                jsonArrayComponent.put(leadFormObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private JSONArray getInterests(ArrayList<CommonModel> leadFormInterestsData) {
        JSONArray jsonArray = new JSONArray();
        if (leadFormInterestsData != null && leadFormInterestsData.size() > 0) {
            for (CommonModel model : leadFormInterestsData) {
                jsonArray.put(model.getTitle());
            }
        }
        return jsonArray;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void NotifyDataDelete() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnContinue:
                createMobilePageFinal();
                break;
            case R.id.Add:
                Intent intent1 = new Intent(getBaseContext(), CreateEmailOption.class);
                intent1.putExtra("Message", "MobilePage");
                for (CampaignData data : pageBuilderAdapter.List) {
                    if (data.getTitle().equalsIgnoreCase("Lead form")) {
                        intent1.putExtra("LeadForm", "LeadForm");
                        break;
                    }
                }
                intent1.putExtras(intent1);
                startActivityForResult(intent1, 1);
                break;
        }
    }

    private void createMobilePageFinal() {
        Intent intent = new Intent(getBaseContext(), CreateMobilePagePreview.class);
        JSONArray jsonArray = getAllComponents();
        if (jsonArray.length() > 0) {
            intent.putExtra("Components", getAllComponents().toString());
            startActivity(intent);
        } else {
            ToastShow.setText(CreateMobilePage.this, "No data available for creating mobile page", Toast.LENGTH_LONG);
        }
    }
}
