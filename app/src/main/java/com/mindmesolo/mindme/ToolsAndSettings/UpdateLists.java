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
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.ToastShow;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by eNest on 8/30/2016.
 */
public class UpdateLists extends Activity implements View.OnClickListener {

    EditText editText;

    TextView ActivityTitle;

    SqliteDataBaseHelper dBhelper = new SqliteDataBaseHelper(this);

    Button UpdateData, FinishActivity;

    String ListId = null, ListName;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.add);

        ActivityTitle = (TextView) findViewById(R.id.toolbartxt);
        ActivityTitle.setText("Update List");

        editText = (EditText) findViewById(R.id.editText);

        UpdateData = (Button) findViewById(R.id.Add);

        UpdateData.setText("Update");

        TextView TV_simple_text_message = (TextView) findViewById(R.id.TV_simple_text_message);

        TV_simple_text_message.setText("Use lists to organize your contacts into groups.");

        UpdateData.setOnClickListener(this);

        FinishActivity = (Button) findViewById(R.id.cancel);

        FinishActivity.setOnClickListener(this);

        Intent intent = getIntent();

        ListId = intent.getStringExtra("ListId");

        ListName = intent.getStringExtra("ListName");

        editText.setText(ListName);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Add:
                UpdateList();
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

    public void UpdateList() {
        if (editText.getText().length() > 0 && StringUtils.isNotBlank(editText.getText().toString())) {
            showProgressDialog();

            String REGISTER_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + dBhelper.getOrganizationId() + "/lists/" + ListId;

            VolleyApi.getInstance().putJsonObject(UpdateLists.this, REGISTER_URL, getJsonStr(ListId).toString(), new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
                @Override
                public void onCompletion(VolleyApi.ApiResult result) {
                    pDialog.dismiss();
                    if (result.success && result.dataIsObject()) {
                        JSONObject response = result.getDataAsObject();
                        try {
                            String ListId = response.getString("id");
                            String ListName = response.getString("name");
                            dBhelper.updateListById(ListId, ListName);
                        } catch (Exception e) {

                        }
                        ToastShow.setText(UpdateLists.this, "Update Successful.", Toast.LENGTH_LONG);
                        finish();
                    } else {
                        ToastShow.setText(getBaseContext(), "oops something went wrong. try again", Toast.LENGTH_LONG);
                    }
                }
            });
        } else {
            ToastShow.setText(getBaseContext(), "Please enter list name.", Toast.LENGTH_SHORT);
        }
    }

    public String getJsonStr(String id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", editText.getText().toString());
            jsonObject.put("id", id);
            jsonObject.put("name", editText.getText().toString());
            jsonObject.put("status", "ACTIVE");
            JSONArray contactIds = new JSONArray(dBhelper.getListContactsById(id));
            jsonObject.put("contactIds", contactIds);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
