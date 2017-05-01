package com.mindmesolo.mindme.CreateCampaign;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
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

/**
 * Created by enest_09 on 11/11/2016.
 */

public class YesNoMaybeFragment extends Fragment implements View.OnClickListener {

    private static String yesNoMayData = null;
    SqliteDataBaseHelper dBhelper;

    View view;

    DataHelper dataHelper = new DataHelper();

    EditText response;

    SeekBar buttonCornerSeekbar;

    SwitchCompat nobtn, maybebtn;

    ToggleButton left, center, right;

    String alignment = "center";

    boolean _areLecturesLoaded = false;
    private CallToActionInterface mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.yes_no_maybe_fragment, container, false);

        dBhelper = new SqliteDataBaseHelper(getContext());
        initializeView();
        return view;
    }

    private void initializeView() {

        response = (EditText) view.findViewById(R.id.response);

        buttonCornerSeekbar = (SeekBar) view.findViewById(R.id.buttonCornerSeekbar);

        left = (ToggleButton) view.findViewById(R.id.left);
        left.setOnClickListener(this);

        center = (ToggleButton) view.findViewById(R.id.center);
        center.setOnClickListener(this);
        center.setChecked(true);

        right = (ToggleButton) view.findViewById(R.id.right);
        right.setOnClickListener(this);

        nobtn = (SwitchCompat) view.findViewById(R.id.nobtn);
        nobtn.setOnClickListener(this);
        nobtn.setChecked(true);

        maybebtn = (SwitchCompat) view.findViewById(R.id.maybebtn);
        maybebtn.setOnClickListener(this);

        response.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                passJsonObjectToActivity();
            }
        });

        buttonCornerSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                passJsonObjectToActivity();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !_areLecturesLoaded) {
            getDataFromActivity();
            passJsonObjectToActivity();
            _areLecturesLoaded = true;
        }
    }

    //-------------- GET SELECTED ITEMS ROM LIST VIEW -------AND Return A JSON ARRAY OF SELECTED ITEMS-------//

    @Override
    public void onDetach() {
        mCallback = null;
        yesNoMayData = null;
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromActivity();
        passJsonObjectToActivity();
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
            case R.id.left:
                alignment = "left";
                left.setChecked(true);
                right.setChecked(false);
                center.setChecked(false);
                passJsonObjectToActivity();
                break;
            case R.id.center:
                alignment = "center";
                left.setChecked(false);
                right.setChecked(false);
                center.setChecked(true);
                passJsonObjectToActivity();
                break;
            case R.id.right:
                alignment = "right";
                left.setChecked(false);
                right.setChecked(true);
                center.setChecked(false);
                passJsonObjectToActivity();
                break;
            case R.id.maybebtn:
                passJsonObjectToActivity();
                break;
            case R.id.nobtn:
                passJsonObjectToActivity();
                break;
        }
    }

    private void passJsonObjectToActivity() {

        JSONObject componentObject = new JSONObject();

        JSONArray properties = new JSONArray();

        JSONObject callToAction = new JSONObject();

        JSONObject yesNoMaybe = new JSONObject();

        JSONArray buttons = new JSONArray();

        try {

            buttons.put("yes");
            if (nobtn.isChecked()) {
                buttons.put("no");
            }
            if (maybebtn.isChecked()) {
                buttons.put("maybe");
            }

            yesNoMaybe.put("buttons", buttons);

            callToAction.put("yesNoMaybe", yesNoMaybe);

            callToAction.put("type", "YESNOMAYBE");

            callToAction.put("message", response.getText().toString());

            properties.put(new JSONObject().put("name", "fontSize").put("value", "28px"));
            properties.put(new JSONObject().put("name", "fontWeight").put("value", 300));
            properties.put(new JSONObject().put("name", "alignment").put("value", alignment));
            properties.put(new JSONObject().put("name", "buttonCorners").put("value", "8px"));
            //properties.put(new JSONObject().put("name", "buttonCorners").put("value", buttonCornerSeekbar.getProgress()));
            componentObject.put("message", response.getText().toString());
            componentObject.put("type", "CALLTOACTION");
            componentObject.put("callToAction", callToAction);
            componentObject.put("properties", properties);

            mCallback.yesNoMaybe(componentObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getDataFromActivity() {

        if (yesNoMayData != null) {

            Log.i("YESNO", yesNoMayData.toString());

            try {

                center.setChecked(false);

                JSONObject jsonObject = new JSONObject(yesNoMayData);

                response.setText(jsonObject.getString("message"));

                JSONObject callToAction = jsonObject.getJSONObject("callToAction");

                JSONObject yesNoMaybe = callToAction.getJSONObject("yesNoMaybe");

                JSONArray jsonArrayBtn = yesNoMaybe.getJSONArray("buttons");

                for (int i = 0; i < jsonArrayBtn.length(); i++) {
                    switch (jsonArrayBtn.getString(i)) {
                        case "no":
                            nobtn.setChecked(true);
                            break;
                        case "maybe":
                            maybebtn.setChecked(true);
                    }
                }

                JSONArray propertiesArray = jsonObject.getJSONArray("properties");
                switch (propertiesArray.getJSONObject(2).getString("value")) {
                    case "center":
                        center.setChecked(true);
                        alignment = "center";
                        break;
                    case "left":
                        left.setChecked(true);
                        alignment = "left";
                        break;
                    case "right":
                        right.setChecked(true);
                        alignment = "right";
                        break;
                }

//                int border = propertiesArray.getJSONObject(3).getInt("value");
//
//                buttonCornerSeekbar.setProgress(border);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void fragmentCommunication(String Data) {
        yesNoMayData = Data;
    }

}
