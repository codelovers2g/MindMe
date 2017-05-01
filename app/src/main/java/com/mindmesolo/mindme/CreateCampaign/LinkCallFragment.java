package com.mindmesolo.mindme.CreateCampaign;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by enest_09 on 11/11/2016.
 */

public class LinkCallFragment extends Fragment implements View.OnClickListener {

    private static String LinkCallData;
    String TAG = "LinkCallFragment";
    SqliteDataBaseHelper dBhelper;

    ArrayList HoldItems;

    View view;

    DataHelper dataHelper = new DataHelper();

    ToggleButton link, call, email;

    ToggleButton leftLink, centerLink, rightLink;

    EditText EditTextTitle, EditTextAction;
    SeekBar buttonCornerSeekbarLink;
    SeekBar buttonSizeSeekbarLink;
    boolean _areLecturesLoaded = false;
    private CallToActionInterface mCallback;
    private String alignment = "center";
    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            sendDataToActivity();
        }
    };
    private String ActionUrl = "", ActionPhone = "", ActionEmail = "";
    TextWatcher myTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (link.isChecked()) {

                ActionUrl = EditTextAction.getText().toString();

            } else if (call.isChecked()) {

                ActionPhone = EditTextAction.getText().toString();

            } else if (email.isChecked()) {
                ActionEmail = EditTextAction.getText().toString();
            }
            sendDataToActivity();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.link_call_fragment, container, false);

        dBhelper = new SqliteDataBaseHelper(getContext());

        setupUI();

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !_areLecturesLoaded) {
            getDataFromActivity();
            sendDataToActivity();
            _areLecturesLoaded = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromActivity();
        sendDataToActivity();
    }

    private void setupUI() {

        link = (ToggleButton) view.findViewById(R.id.link);
        link.setOnClickListener(this);
        link.setChecked(true);

        call = (ToggleButton) view.findViewById(R.id.call);
        call.setOnClickListener(this);

        email = (ToggleButton) view.findViewById(R.id.email);
        email.setOnClickListener(this);

        //alignment Text
        leftLink = (ToggleButton) view.findViewById(R.id.leftLink);
        leftLink.setOnClickListener(this);


        centerLink = (ToggleButton) view.findViewById(R.id.centerLink);
        centerLink.setOnClickListener(this);
        centerLink.setChecked(true);

        rightLink = (ToggleButton) view.findViewById(R.id.rightLink);
        rightLink.setOnClickListener(this);

        buttonCornerSeekbarLink = (SeekBar) view.findViewById(R.id.buttonCornerSeekbarLink);
        buttonCornerSeekbarLink.setOnSeekBarChangeListener(onSeekBarChangeListener);

        buttonSizeSeekbarLink = (SeekBar) view.findViewById(R.id.buttonSizeSeekbarLink);
        buttonSizeSeekbarLink.setOnSeekBarChangeListener(onSeekBarChangeListener);

        EditTextTitle = (EditText) view.findViewById(R.id.EditTextTitle);
        EditTextTitle.addTextChangedListener(myTextWatcher);

        EditTextAction = (EditText) view.findViewById(R.id.EditTextAction);
        EditTextAction.addTextChangedListener(myTextWatcher);
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putStringArrayList("savedList", HoldItems);
    }
    //-------------- GET SELECTED ITEMS ROM LIST VIEW -------AND RETURN A JSON ARRAY OF SELECTED ITEMS-------//

    @Override
    public void onDetach() {
        mCallback = null;
        LinkCallData = null;
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (CallToActionInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement IFragmentToActivity");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.link:
                link.setChecked(true);
                call.setChecked(false);
                email.setChecked(false);
                EditTextAction.setInputType(InputType.TYPE_CLASS_TEXT);
                EditTextAction.setHint("Add URL");
                EditTextAction.setText(ActionUrl);
                EditTextTitle.setHint("Click Here");
                sendDataToActivity();
                break;

            case R.id.call:
                link.setChecked(false);
                call.setChecked(true);
                email.setChecked(false);
                EditTextAction.setInputType(InputType.TYPE_CLASS_PHONE);
                EditTextAction.setHint("Add Phone");
                EditTextAction.setText(ActionPhone);
                EditTextTitle.setHint("Click To Call");
                sendDataToActivity();
                break;

            case R.id.email:
                link.setChecked(false);
                call.setChecked(false);
                email.setChecked(true);
                EditTextAction.setInputType(InputType.TYPE_CLASS_TEXT);
                EditTextAction.setHint("Add Email");
                EditTextAction.setText(ActionEmail);
                EditTextTitle.setHint("Click To Email");
                sendDataToActivity();
                break;

            case R.id.leftLink:
                alignment = "left";
                leftLink.setChecked(true);
                centerLink.setChecked(false);
                rightLink.setChecked(false);
                sendDataToActivity();
                break;

            case R.id.centerLink:
                alignment = "center";
                leftLink.setChecked(false);
                centerLink.setChecked(true);
                rightLink.setChecked(false);
                sendDataToActivity();
                break;

            case R.id.rightLink:
                alignment = "right";
                leftLink.setChecked(false);
                centerLink.setChecked(false);
                rightLink.setChecked(true);
                sendDataToActivity();
                break;
        }
    }

    public void fragmentCommunication(String LinkCallData) {
        this.LinkCallData = LinkCallData;

        Log.i(TAG, LinkCallData.toString());
    }

    private void handleLinkCall(JSONObject linkCall, String Action) {
        try {
            if (linkCall.getString("link").length() >= 0) {
                link.setChecked(true);
                call.setChecked(false);
                email.setChecked(false);
                EditTextAction.setInputType(InputType.TYPE_CLASS_TEXT);
                EditTextAction.setHint("Add URL");
                EditTextTitle.setHint("Click Here");
                ActionUrl = Action;
                ActionPhone = null;
                ActionEmail = null;
            }
        } catch (JSONException e) {
            try {
                if (linkCall.getString("phone").length() >= 0) {
                    link.setChecked(false);
                    call.setChecked(true);
                    email.setChecked(false);
                    EditTextAction.setInputType(InputType.TYPE_CLASS_PHONE);
                    EditTextAction.setHint("Add Phone");
                    EditTextTitle.setHint("Click To Call");
                    ActionUrl = null;
                    ActionPhone = Action;
                    ActionEmail = null;
                }
            } catch (JSONException e1) {
                try {
                    if (linkCall.getString("email").length() >= 0) {
                        link.setChecked(false);
                        call.setChecked(false);
                        email.setChecked(true);
                        EditTextAction.setInputType(InputType.TYPE_CLASS_TEXT);
                        EditTextAction.setHint("Add Email");
                        EditTextTitle.setHint("Click To Email");
                        ActionUrl = null;
                        ActionPhone = null;
                        ActionEmail = Action;
                    }
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    private void getDataFromActivity() {

        if (LinkCallData != null) {

            Log.i(TAG, "DATA :" + LinkCallData.toString());

            centerLink.setChecked(false);

            try {

                JSONObject jsonObject = new JSONObject(LinkCallData);

                JSONArray properties = jsonObject.getJSONArray("properties");

//                int buttonCorner = properties.getJSONObject(3).getInt("value");
//                int buttonSize = properties.getJSONObject(4).getInt("value");
//                buttonCornerSeekbarLink.setProgress(buttonCorner);
//                buttonSizeSeekbarLink.setProgress(buttonSize);

                switch (properties.getJSONObject(2).getString("value")) {
                    case "center":
                        leftLink.setChecked(false);
                        centerLink.setChecked(true);
                        rightLink.setChecked(false);
                        alignment = "center";
                        break;
                    case "left":
                        leftLink.setChecked(true);
                        centerLink.setChecked(false);
                        rightLink.setChecked(false);
                        alignment = "left";
                        break;
                    case "right":
                        leftLink.setChecked(false);
                        centerLink.setChecked(false);
                        rightLink.setChecked(true);
                        alignment = "right";
                        break;
                }

                JSONObject linkCall = jsonObject.getJSONObject("callToAction").getJSONObject("linkCall");

                JSONArray LinkCallProperties = linkCall.getJSONArray("properties");

                String Action = LinkCallProperties.getJSONObject(0).getString("value");

                String Title = LinkCallProperties.getJSONObject(1).getString("value");

                if (Title != null) {
                    EditTextTitle.setText(Title);
                }

                if (Action != null) {
                    EditTextAction.setText(Action);
                }
                handleLinkCall(linkCall, Action);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendDataToActivity() {

        JSONObject componentObject = new JSONObject();

        JSONArray properties = new JSONArray();

        JSONArray LinkCallproperties = new JSONArray();

        JSONObject callToAction = new JSONObject();
        try {
            properties.put(new JSONObject().put("name", "fontSize").put("value", "28px"));
            properties.put(new JSONObject().put("name", "fontWeight").put("value", 300));
            properties.put(new JSONObject().put("name", "alignment").put("value", alignment));
            properties.put(new JSONObject().put("name", "buttonCorners").put("value", "4px"));
            //properties.put(new JSONObject().put("name", "buttonSize").put("value", buttonSizeSeekbarLink.getProgress()));

            LinkCallproperties.put(new JSONObject().put("name", "action").put("value", EditTextAction.getText().toString()));
            LinkCallproperties.put(new JSONObject().put("name", "title").put("value", EditTextTitle.getText().toString()));
            LinkCallproperties.put(new JSONObject().put("name", "color").put("value", "#000000"));
            callToAction.put("type", "CALLME");

            callToAction.put("linkCall", new JSONObject()
                    .put("properties", LinkCallproperties)
                    .put("phone", getCheckedButton(call))
                    .put("link", getCheckedButton(link))
                    .put("email", getCheckedButton(email))
            );

            componentObject.put("type", "CALLTOACTION");
            componentObject.put("callToAction", callToAction);
            componentObject.put("properties", properties);
            mCallback.callToAction(componentObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getCheckedButton(ToggleButton link) {
        String action = null;
        if (link.isChecked()) {
            action = EditTextAction.getText().toString();
        }
        return action;
    }
}
