package com.mindmesolo.mindme.ToolsAndSettings;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.Services.VolleyApi;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.ToastShow;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by eNest on 8/24/2016.
 */
public class UpdateInterests extends Activity implements View.OnClickListener {

    DataHelper dataHelper = new DataHelper();

    EditText editText;

    TextView ActivityTitle;

    SqliteDataBaseHelper dBhelper = new SqliteDataBaseHelper(this);
    Button UpdateData, FinishActivity;
    String InterestId = null, InterestName;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);


        ActivityTitle = (TextView) findViewById(R.id.toolbartxt);
        ActivityTitle.setText("Update Interest");

        editText = (EditText) findViewById(R.id.editText);

        UpdateData = (Button) findViewById(R.id.Add);

        UpdateData.setText("Update");

        TextView TV_simple_text_message = (TextView) findViewById(R.id.TV_simple_text_message);

        TV_simple_text_message.setText("Use interests to represent what you offer, and capture the needs of your contacts.");

        UpdateData.setOnClickListener(this);

        FinishActivity = (Button) findViewById(R.id.cancel);

        FinishActivity.setOnClickListener(this);

        Intent intent = getIntent();

        InterestId = intent.getStringExtra("InterestId");

        InterestName = intent.getStringExtra("InterestName");

        editText.setText(InterestName);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Add:
                UpdateInterest();
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }

    private void showProgressDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Updating...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void UpdateInterest() {
        if (editText.getText().length() > 0 && StringUtils.isNotBlank(editText.getText().toString())) {
            showProgressDialog();

            String REGISTER_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + dBhelper.getOrganizationId() + "/interests/" + InterestId;

            VolleyApi.getInstance().putJsonObject(UpdateInterests.this, REGISTER_URL, getJsonStr(InterestId).toString(), new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
                @Override
                public void onCompletion(VolleyApi.ApiResult result) {
                    pDialog.dismiss();
                    if (result.success && result.dataIsObject()) {
                        JSONObject response = result.getDataAsObject();
                        try {
                            String InterestId = response.getString("id");
                            String InterestName = response.getString("name");
                            dBhelper.updateInterestById(InterestId, InterestName);
                        } catch (Exception e) {
                        }
                        ToastShow.setText(UpdateInterests.this, "Update Successful.", Toast.LENGTH_LONG);
                        finish();
                    } else {
                        ToastShow.setText(getBaseContext(), "oops something went wrong. try again", Toast.LENGTH_LONG);
                    }
                }
            });
        } else {
            ToastShow.setText(getBaseContext(), "Please enter interest name.", Toast.LENGTH_SHORT);
        }
    }


    public String getJsonStr(String id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", editText.getText().toString());
            jsonObject.put("id", id);
            jsonObject.put("name", editText.getText().toString());
            jsonObject.put("status", "ACTIVE");
            JSONArray contactIds = new JSONArray(dBhelper.getInterestsContactsById(id));
            jsonObject.put("contactIds", contactIds);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

}

