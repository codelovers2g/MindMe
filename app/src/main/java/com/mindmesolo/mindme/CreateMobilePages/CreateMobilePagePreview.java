package com.mindmesolo.mindme.CreateMobilePages;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.Services.VolleyApi;
import com.mindmesolo.mindme.ViewMobilePages.ViewMobilePages;
import com.mindmesolo.mindme.helper.CampaignAndMobilePageHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.ToastShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pc-14 on 2/20/2017.
 */
public class CreateMobilePagePreview extends AppCompatActivity {

    private static final String TAG = "CreateMobilePage";

    EditText addMobilePageTitle;

    Button btn_Create_mobile_page;
    SqliteDataBaseHelper sqliteDataBaseHelper;
    JSONObject jsonObjectFinalCampaign = new JSONObject();
    JSONArray Components = new JSONArray();
    CampaignAndMobilePageHelper campaignAndMobilePageHelper;
    private Dialog dialogue_custom;
    private ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mobile_page_final);

        campaignAndMobilePageHelper = new CampaignAndMobilePageHelper(this);
        sqliteDataBaseHelper = new SqliteDataBaseHelper(this);

        addMobilePageTitle = (EditText) findViewById(R.id.addMobilePageTitle);

        btn_Create_mobile_page = (Button) findViewById(R.id.btn_Create_mobile_page);
        btn_Create_mobile_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = addMobilePageTitle.getText().toString();
                if (title.isEmpty() || title == null) {
                    getCustomDialogue("Enter title", "Enter campaign title.");
                } else {
                    createMobilePage();
                }
            }
        });

        String component = getIntent().getStringExtra("Components");
        if (component != null) {
            try {
                updateComponents(new JSONArray(component));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void updateComponents(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                String Type = jsonArray.getJSONObject(i).getString("type");
                switch (Type) {
                    case "IMAGE":
                        campaignAndMobilePageHelper.ImageObject(jsonArray.getJSONObject(i));
                        break;
                    case "VIDEO":
                        campaignAndMobilePageHelper.updateVideoObject(jsonArray.getJSONObject(i));
                        break;
                    case "VOICERECORDING":
                        campaignAndMobilePageHelper.updateAudioObject(jsonArray.getJSONObject(i));
                        break;
                    case "CALLTOACTION":
                        String CallToActionType = jsonArray.getJSONObject(i).getJSONObject("callToAction").getString("type");
                        switch (CallToActionType) {
                            case "POLL":
                                campaignAndMobilePageHelper.updatePollObject(jsonArray.getJSONObject(i));
                                break;
                            case "FEEDBACK":
                                campaignAndMobilePageHelper.updateFeedBackObject(jsonArray.getJSONObject(i));
                                break;
                            case "YESNOMAYBE":
                                campaignAndMobilePageHelper.updateCallToAction(jsonArray.getJSONObject(i));
                                break;
                            case "CALLME":
                                campaignAndMobilePageHelper.updateCallMe(jsonArray.getJSONObject(i));
                                break;
                        }
                        jsonArray.getJSONObject(i).put("type", "CALLTOACTIONFORMOBILEPAGE");
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Components = jsonArray;
    }

    private void createMobilePage() {
        showProgressDialog();
        final String OrganizationId = sqliteDataBaseHelper.getOrganizationId();
        try {
            long time = System.currentTimeMillis();
            jsonObjectFinalCampaign.put("createdOn", String.valueOf(time));
            jsonObjectFinalCampaign.put("name", addMobilePageTitle.getText().toString());
            jsonObjectFinalCampaign.put("components", Components);
            jsonObjectFinalCampaign.put("orgId", OrganizationId);
            jsonObjectFinalCampaign.put("status", "ACTIVE");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String jsonStr = jsonObjectFinalCampaign.toString();

        String REGISTER_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + OrganizationId + "/mobilepage";

        VolleyApi.getInstance().postJsonObject(getBaseContext(), REGISTER_URL, jsonStr, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                pDialog.dismiss();
                if (result.success && result.dataIsObject()) {
                    JSONObject response = result.getDataAsObject();
                    Log.i("MobilePage", response.toString());

                    ToastShow.setText(getApplicationContext(), "Great! You have Successfully Created the Mobile Page.", Toast.LENGTH_SHORT);
                    startActivity(new Intent(getApplicationContext(), ViewMobilePages.class));
                    finish();
                } else {
                    if (result.volleyError instanceof TimeoutError || result.volleyError instanceof NoConnectionError)
                        ToastShow.setText(getBaseContext(), "Please Check your internet Connection", Toast.LENGTH_LONG);

                    else if (result.volleyError instanceof ServerError)
                        ToastShow.setText(getBaseContext(), "oops something went wrong. try again", Toast.LENGTH_LONG);
                }
            }
        });
    }

    private void showProgressDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Creating mobile page.");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void getCustomDialogue(String AlertTitle, String AlertMessage) {

        dialogue_custom = new Dialog(CreateMobilePagePreview.this);

        dialogue_custom.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialogue_custom.setContentView(R.layout.campaign_dialog_layout);

        Button btn_ok_dialogue = (Button) dialogue_custom.findViewById(R.id.btn_ok_dialogue);

        TextView title = (TextView) dialogue_custom.findViewById(R.id.title);

        title.setText(AlertTitle);

        TextView message = (TextView) dialogue_custom.findViewById(R.id.message);
        message.setText(AlertMessage);
        btn_ok_dialogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogue_custom.dismiss();
            }
        });
        dialogue_custom.show();
    }
}
