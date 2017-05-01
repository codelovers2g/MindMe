package com.mindmesolo.mindme.ContactAndLists;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mindmesolo.mindme.OrganizationModel;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User1 on 6/8/2016.
 */
public class ContactSummary extends Fragment implements View.OnClickListener {

    private static final String TAG = "ContactSummary";
    String StoredContactId;
    TextView TV_ContactEmail, TV_ContactPhone, TV_phoneType;
    ImageView Img_view_Email, Img_view_Phone_Call, Img_view_text_message;
    EditText ET_Notes;
    LinearLayout linearLayoutEmail, linearLayoutHome;
    TextView TV_list_count, TV_interest_count, TV_tag_count, TV_totalCamp, TV_contactResponse;
    SqliteDataBaseHelper sqliteDataBaseHelper;
    DataHelper dataHelper = new DataHelper();
    ArrayList<String> contactArray;

    IFragmentToActivity2 mCallback;

    LinearLayout List_tag_interests;

    ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.contactsummary, container, false);
        sqliteDataBaseHelper = new SqliteDataBaseHelper(getContext());

        Img_view_Email = (ImageView) rootView.findViewById(R.id.Img_view_Email);
        Img_view_Email.setOnClickListener(this);

        Img_view_Phone_Call = (ImageView) rootView.findViewById(R.id.Img_view_Phone_Call);
        Img_view_Phone_Call.setOnClickListener(this);

        Img_view_text_message = (ImageView) rootView.findViewById(R.id.Img_view_text_message);
        Img_view_text_message.setOnClickListener(this);

        TV_list_count = (TextView) rootView.findViewById(R.id.listCount);

        TV_totalCamp = (TextView) rootView.findViewById(R.id.totalCamp);

        TV_contactResponse = (TextView) rootView.findViewById(R.id.responsePercent);

        TV_interest_count = (TextView) rootView.findViewById(R.id.interestCount);

        TV_tag_count = (TextView) rootView.findViewById(R.id.tagCount);

        TV_phoneType = (TextView) rootView.findViewById(R.id.type);

        TV_ContactEmail = (TextView) rootView.findViewById(R.id.Contactemail);

        TV_ContactPhone = (TextView) rootView.findViewById(R.id.Contactphone);

        ET_Notes = (EditText) rootView.findViewById(R.id.notes);

        ColorStateList oldColors = TV_ContactPhone.getTextColors();

        ET_Notes.setTextColor(oldColors);

        linearLayoutEmail = (LinearLayout) rootView.findViewById(R.id.linearLayoutEmail);

        linearLayoutHome = (LinearLayout) rootView.findViewById(R.id.linearLayoutHome);

        // if user click on List/Interests/tags then Open Types fragment.
        List_tag_interests = (LinearLayout) rootView.findViewById(R.id.List_tag_interests);
        List_tag_interests.setOnClickListener(this);

        Bundle extras = getActivity().getIntent().getExtras();

        StoredContactId = extras.getString("contactid");

        getCampaignResponse(StoredContactId);

        getContactData(StoredContactId);

        getListInterestTagsCount(StoredContactId);

        return rootView;
    }

    private void getListInterestTagsCount(String StoredContactId) {
        TV_list_count.setText(String.valueOf(sqliteDataBaseHelper.getNumbersOfRowsInListByListId(StoredContactId)));
        TV_interest_count.setText(String.valueOf(sqliteDataBaseHelper.numberOfRowsInterestContactId(StoredContactId)));
        TV_tag_count.setText(String.valueOf(sqliteDataBaseHelper.numberOfRowsTagsContactId(StoredContactId)));
    }

    private void getContactData(final String StoredContactId) {

        contactArray = sqliteDataBaseHelper.getContactInformation(StoredContactId);

        TV_ContactEmail.setText(sqliteDataBaseHelper.getContactemail(StoredContactId));

        TV_ContactPhone.setText(sqliteDataBaseHelper.getContactphone(StoredContactId));

        String input = sqliteDataBaseHelper.getContactsPhoneType(StoredContactId);

        if (input != null) {
            String output = input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
            TV_phoneType.setText(output);
        }

        if (TV_ContactEmail.length() == 0 || TV_ContactEmail.getText().toString().toLowerCase().equals("null")) {
            linearLayoutEmail.setVisibility(View.GONE);
        }

        if (TV_ContactPhone.length() == 0 || TV_ContactPhone.getText().toString().toLowerCase().equals("null")) {
            linearLayoutHome.setVisibility(View.GONE);
        }

        String s = contactArray.get(18);
        if (s != null || s.length() > 0 || !s.equalsIgnoreCase("null"))
            ET_Notes.setText(s);

//        ET_Notes.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    if (v.getText().toString().isEmpty() || v.getText().length() == 0) {
//                        ToastShow.setText(getContext(), "please enter Some text", Snackbar.LENGTH_LONG);
//                    } else {
//                        saveNotes(v.getText().toString());
//                    }
//                }
//                return false;
//            }
//        });

        ET_Notes.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                saveNotes(ET_Notes.getText().toString());
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });


    }

    private void getCampaignResponse(final String StoredContactId) {

        String REGISTER_URL = OrganizationModel.getApiBaseUrl() + sqliteDataBaseHelper.getOrganizationId() + "/contacts/" + StoredContactId + "";

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, REGISTER_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        setContactCampaignResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Basic " + dataHelper.getApiAccess(getContext()));
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void setContactCampaignResponse(JSONObject responseData) {
        try {
            JSONArray receivedCampaignIds = responseData.getJSONArray("receivedCampaignIds");

            JSONArray respondedCampaignIds = responseData.getJSONArray("respondedCampaignIds");
            int resper;
            int total = receivedCampaignIds.length();
            int resp = respondedCampaignIds.length();
            if (total == 0) {
                resper = 0;
            } else {
                double test = (double) resp / total * 100;
                resper = (int) test;
                if (resper > 100) {
                    resper = 100;
                }
            }

            if (total > 1) {
                TV_totalCamp.setText("Responses from " + total + " Campaigns");
            } else {
                TV_totalCamp.setText("Responses from 0 Campaign");
            }
            TV_contactResponse.setText(resper + "%");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void saveNotes(String Notes) {
        sqliteDataBaseHelper.updateContactNotes(StoredContactId, Notes);
    }

    @Override
    public void onResume() {
        super.onResume();
        getContactData(StoredContactId);
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (IFragmentToActivity2) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement IFragmentToActivity");
        }
    }

    private void startDialog(String Message) {
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(true);
        pDialog.setMessage(Message);
        pDialog.show();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.Img_view_Email:
                intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + TV_ContactEmail.getText()));
                startActivity(Intent.createChooser(intent, "Send Email"));
                break;
            case R.id.Img_view_Phone_Call:
                intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + TV_ContactPhone.getText()));
                startActivity(intent);
                break;
            case R.id.Img_view_text_message:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("vnd.android-dir/mms-sms");
                intent.setData(Uri.parse("smsto:" + TV_ContactPhone.getText())); // This ensures only SMS apps respond
                startActivity(intent);
                break;
            case R.id.List_tag_interests:
                mCallback.setCurrentItem(50);
                break;
        }

    }
}
