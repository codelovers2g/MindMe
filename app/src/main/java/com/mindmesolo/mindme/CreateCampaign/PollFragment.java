package com.mindmesolo.mindme.CreateCampaign;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.ToastShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by enest_09 on 11/11/2016.
 */

public class PollFragment extends Fragment {

    private static final String TAG = "PollFragment";
    private static String PollData;
    SqliteDataBaseHelper dBhelper;

    ArrayList HoldItems;

    View view;

    DataHelper dataHelper = new DataHelper();

    EditText pollEditText, Edit_Option1, Edit_Option2, Edit_Option3, Edit_Option4, Edit_Option5;

    boolean _areLecturesLoaded = false;
    private CallToActionInterface mCallback;
    TextWatcher myTestWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            SendDataToActivity();
        }
    };
    //    @Override
//    public void onSaveInstanceState(Bundle state) {
//        super.onSaveInstanceState(state);
//        state.putStringArrayList("savedList", HoldItems);
//    }
    //-------------- GET SELECTED ITEMS ROM LIST VIEW -------AND RETURAN A JSON ARRAY OF SELECTED ITEMS-------//
    TextWatcher myTestWatcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            String result = s.toString().replaceAll(" ", "");
            if (!s.toString().equals(result)) {
                Edit_Option1.setText(result);
                Edit_Option1.setSelection(result.length());

                ToastShow.setText(getContext(), "Cannot add a space.", Toast.LENGTH_LONG);
                // alert the user
            }
            SendDataToActivity();
        }
    };
    TextWatcher myTestWatcher2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            String result = s.toString().replaceAll(" ", "");
            if (!s.toString().equals(result)) {
                Edit_Option2.setText(result);
                Edit_Option2.setSelection(result.length());
                ToastShow.setText(getContext(), "Cannot add a space.", Toast.LENGTH_LONG);
                // alert the user
            }

            SendDataToActivity();
        }
    };
    TextWatcher myTestWatcher3 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            String result = s.toString().replaceAll(" ", "");
            if (!s.toString().equals(result)) {
                Edit_Option3.setText(result);
                Edit_Option3.setSelection(result.length());
                ToastShow.setText(getContext(), "Cannot add a space.", Toast.LENGTH_LONG);
                // alert the user
            }
            SendDataToActivity();
        }
    };
    TextWatcher myTestWatcher4 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            String result = s.toString().replaceAll(" ", "");
            if (!s.toString().equals(result)) {
                Edit_Option4.setText(result);
                Edit_Option4.setSelection(result.length());
                ToastShow.setText(getContext(), "Cannot add a space.", Toast.LENGTH_LONG);
                // alert the user
            }
            SendDataToActivity();
        }
    };
    TextWatcher myTestWatcher5 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            String result = s.toString().replaceAll(" ", "");
            if (!s.toString().equals(result)) {
                Edit_Option5.setText(result);
                Edit_Option5.setSelection(result.length());
                ToastShow.setText(getContext(), "Cannot add a space.", Toast.LENGTH_LONG);
                // alert the user
            }
            SendDataToActivity();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.poll_fragment, container, false);

        pollEditText = (EditText) view.findViewById(R.id.pollEditText);
        pollEditText.addTextChangedListener(myTestWatcher);

        Edit_Option1 = (EditText) view.findViewById(R.id.option1EditText);
        Edit_Option1.addTextChangedListener(myTestWatcher1);

        Edit_Option2 = (EditText) view.findViewById(R.id.option2EditText);
        Edit_Option2.addTextChangedListener(myTestWatcher2);

        Edit_Option3 = (EditText) view.findViewById(R.id.option3EditText);
        Edit_Option3.addTextChangedListener(myTestWatcher3);

        Edit_Option4 = (EditText) view.findViewById(R.id.option4EditText);
        Edit_Option4.addTextChangedListener(myTestWatcher4);

        Edit_Option5 = (EditText) view.findViewById(R.id.option5EditText);
        Edit_Option5.addTextChangedListener(myTestWatcher5);

        dBhelper = new SqliteDataBaseHelper(getContext());

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !_areLecturesLoaded) {
            getDataFromActivity();
            SendDataToActivity();
            _areLecturesLoaded = true;
        }
    }

    @Override
    public void onDetach() {
        mCallback = null;
        PollData = null;
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromActivity();
        SendDataToActivity();
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

    private void getDataFromActivity() {
        if (PollData != null) {
            try {

                JSONObject jsonObject = new JSONObject(PollData);

                pollEditText.setText(jsonObject.getString("message"));

                JSONObject jsonObject1 = jsonObject.getJSONObject("callToAction");

                JSONArray jsonArray = jsonObject1.getJSONArray("pollItems");

                for (int i = 0; i < jsonArray.length(); i++) {
                    String data = jsonArray.getJSONObject(i).getString("label");
                    switch (i) {
                        case 0:
                            Edit_Option1.setText(data);
                            break;
                        case 1:
                            Edit_Option2.setText(data);
                            break;
                        case 2:
                            Edit_Option3.setText(data);
                            break;
                        case 3:
                            Edit_Option4.setText(data);
                            break;
                        case 4:
                            Edit_Option5.setText(data);
                            break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void fragmentCommunication(String Data) {
        PollData = Data;
    }

    private void SendDataToActivity() {
        JSONObject componentObject = new JSONObject();
        JSONArray properties = new JSONArray();
        JSONObject callToAction = new JSONObject();
        JSONArray pollItems = new JSONArray();
        try {
            pollItems.put(new JSONObject().put("label", Edit_Option1.getText().toString().trim()));
            pollItems.put(new JSONObject().put("label", Edit_Option2.getText().toString().trim()));
            pollItems.put(new JSONObject().put("label", Edit_Option3.getText().toString().trim()));
            pollItems.put(new JSONObject().put("label", Edit_Option4.getText().toString().trim()));
            pollItems.put(new JSONObject().put("label", Edit_Option5.getText().toString().trim()));

            callToAction.put("type", "POLL");
            callToAction.put("pollItems", pollItems);
            callToAction.put("message", pollEditText.getText().toString().trim());
            callToAction.put("properties", new JSONArray().put(new JSONObject().put("name", "color").put("value", "#000000")));
            properties.put(new JSONObject().put("name", "fontSize").put("value", "28px"));
            properties.put(new JSONObject().put("name", "fontWeight").put("value", 300));
            properties.put(new JSONObject().put("name", "elementSize").put("value", "small"));
            properties.put(new JSONObject().put("name", "alignment").put("value", "center"));
            //properties.put(new JSONObject().put("name", "buttonCorners").put("value",22));

            componentObject.put("message", pollEditText.getText().toString().trim());
            componentObject.put("type", "CALLTOACTION");
            componentObject.put("properties", properties);
            componentObject.put("callToAction", callToAction);

            mCallback.pollItems(componentObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
