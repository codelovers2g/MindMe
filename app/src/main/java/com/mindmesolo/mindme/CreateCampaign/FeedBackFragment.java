package com.mindmesolo.mindme.CreateCampaign;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by enest_09 on 11/11/2016.
 */

public class FeedBackFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "FeedBackFragment";
    private static String FeedBackData;
    SqliteDataBaseHelper dBhelper;

    View view;

    DataHelper dataHelper = new DataHelper();

    TextView feedbackEditText;

    SeekBar elementSizeSeekbar;

    String alignment = "center";

    ToggleButton rightfeedback, leftfeedback, centerfeedback;

    //String ElementsSize = "small";
    int ElementsSize;

    boolean _areLecturesLoaded = false;
    private CallToActionInterface mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.feedback_fragemnt, container, false);

        dBhelper = new SqliteDataBaseHelper(getContext());

        feedbackEditText = (TextView) view.findViewById(R.id.feedbackEditText);

        elementSizeSeekbar = (SeekBar) view.findViewById(R.id.elementSizeSeekbar);

        elementSizeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ElementsSize = progress;
                sendDataToActivity();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });

        feedbackEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sendDataToActivity();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        leftfeedback = (ToggleButton) view.findViewById(R.id.leftfeedback);
        leftfeedback.setOnClickListener(this);

        centerfeedback = (ToggleButton) view.findViewById(R.id.centerfeedback);
        centerfeedback.setOnClickListener(this);

        rightfeedback = (ToggleButton) view.findViewById(R.id.rightfeedback);
        rightfeedback.setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leftfeedback:
                leftfeedback.setChecked(true);
                centerfeedback.setChecked(false);
                rightfeedback.setChecked(false);
                alignment = "left";
                sendDataToActivity();
                break;
            case R.id.centerfeedback:
                leftfeedback.setChecked(false);
                rightfeedback.setChecked(false);
                centerfeedback.setChecked(true);
                alignment = "center";
                sendDataToActivity();

                break;
            case R.id.rightfeedback:
                leftfeedback.setChecked(false);
                centerfeedback.setChecked(false);
                rightfeedback.setChecked(true);
                alignment = "right";
                sendDataToActivity();
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
    }

    //-------------- GET SELECTED ITEMS ROM LIST VIEW -------AND RETURAN A JSON ARRAY OF SELECTED ITEMS-------//
    @Override
    public void onDetach() {
        mCallback = null;
        FeedBackData = null;
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromActivity();
        sendDataToActivity();
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

    private void sendDataToActivity() {
        JSONObject componentObject = new JSONObject();
        JSONArray properties = new JSONArray();

        JSONObject callToAction = new JSONObject();
        try {
            properties.put(new JSONObject().put("name", "fontSize").put("value", "28px"));
            properties.put(new JSONObject().put("name", "fontWeight").put("value", 300));
            properties.put(new JSONObject().put("name", "alignment").put("value", alignment));
            properties.put(new JSONObject().put("name", "elementSize").put("value", ElementsSize));

            callToAction.put("feedback", new JSONObject().put("scale", "5"));
            callToAction.put("type", "FEEDBACK");
            callToAction.put("message", feedbackEditText.getText().toString());
            componentObject.put("message", feedbackEditText.getText().toString());
            componentObject.put("type", "CALLTOACTION");
            componentObject.put("callToAction", callToAction);
            componentObject.put("properties", properties);

            mCallback.feedBack(componentObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void fragmentCommunication(String feedBackData) {
        FeedBackData = feedBackData;
    }

    private void getDataFromActivity() {

        if (FeedBackData != null) {

            centerfeedback.setChecked(false);

            try {

                JSONObject jsonObject = new JSONObject(FeedBackData);

                String message = jsonObject.getString("message");

                if (message != null) {

                    feedbackEditText.setText(jsonObject.getString("message"));

                }

                JSONArray properties = jsonObject.getJSONArray("properties");

                switch (properties.getJSONObject(2).getString("value")) {
                    case "center":
                        centerfeedback.setChecked(true);
                        alignment = "center";
                        break;
                    case "left":
                        leftfeedback.setChecked(true);
                        alignment = "left";
                        break;
                    case "right":
                        rightfeedback.setChecked(true);
                        alignment = "right";
                        break;
                }

                int Progress = properties.getJSONObject(3).getInt("value");
                elementSizeSeekbar.setProgress(Progress);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
