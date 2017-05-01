package com.mindmesolo.mindme.CreateCampaign;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.ToastShow;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.view.View.SCALE_X;
import static android.view.View.SCALE_Y;

/**
 * Created by enest_09 on 11/11/2016.
 */

public class CallToActionElements extends AppCompatActivity implements CallToActionInterface, View.OnClickListener {

    private static final String TAG = "CallToActionElements";

    static JSONObject finalObject;

    private static int currentFragment;

    ToggleButton yes_no_maybe_btn, poll_btn, feedback_btn, Link_Call_btn;

    LinearLayout responseOutputLayout, pollOutputLayout, feedbackOutputLayout, linkOutputLayout;

    TextView responseToggleText, pollToggleText, feedbackToggleText, callToggleText;

    Button btn_LinkCall;

    TextView option1Text, option2Text, option3Text, option4Text, option5Text;

    LinearLayout optionOneLayout, optionTwoLayout, optionThreeLayout, optionFourLayout, optionFiveLayout;

    TextView pollText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.calltoaction_elements);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        InitializeLayouts();

        String MediaType = getIntent().getStringExtra("MediaType");

        handleActivity(MediaType);
    }

    // Fragment Replace Methods
    private void setCurrentTabFragment(int position) {
        currentFragment = position;
        switch (position) {
            case 0:
                replaceFragment(new YesNoMaybeFragment());
                break;
            case 1:
                replaceFragment(new PollFragment());
                break;
            case 2:
                replaceFragment(new FeedBackFragment());
                break;
            case 3:
                replaceFragment(new LinkCallFragment());
                break;
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    private void handleActivity(String type) {
        switch (type) {
            case "Response":
            case "Call to Action":
                setCurrentTabFragment(0);
                handleButtons(0);
                String YesNoMaybeExtraData = getIntent().getStringExtra("ExtraData");
                if (YesNoMaybeExtraData != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(YesNoMaybeExtraData);
                        yesNoMaybe(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case "Poll":
                setCurrentTabFragment(1);
                handleButtons(1);
                String PollExtraData = getIntent().getStringExtra("ExtraData");
                if (PollExtraData != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(PollExtraData);
                        pollItems(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "Feedback":
                setCurrentTabFragment(2);
                handleButtons(2);
                String FeedbackData = getIntent().getStringExtra("ExtraData");
                if (FeedbackData != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(FeedbackData);
                        feedBack(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case "Link/Call":
                setCurrentTabFragment(3);
                handleButtons(3);
                String ExtraData = getIntent().getStringExtra("ExtraData");
                if (ExtraData != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(ExtraData);
                        callToAction(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yes_no_maybe_btn:
                setCurrentTabFragment(0);
                poll_btn.setChecked(false);
                feedback_btn.setChecked(false);
                Link_Call_btn.setChecked(false);
                yes_no_maybe_btn.setChecked(true);
                handleButtons(0);
                break;

            case R.id.poll_btn:
                poll_btn.setChecked(true);
                feedback_btn.setChecked(false);
                Link_Call_btn.setChecked(false);
                yes_no_maybe_btn.setChecked(false);
                handleButtons(1);
                setCurrentTabFragment(1);
                break;

            case R.id.feedback_btn:
                poll_btn.setChecked(false);
                feedback_btn.setChecked(true);
                Link_Call_btn.setChecked(false);
                yes_no_maybe_btn.setChecked(false);
                setCurrentTabFragment(2);
                handleButtons(2);
                break;

            case R.id.Link_Call_btn:
                poll_btn.setChecked(false);
                feedback_btn.setChecked(false);
                Link_Call_btn.setChecked(true);
                yes_no_maybe_btn.setChecked(false);
                setCurrentTabFragment(3);
                handleButtons(3);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.done) {
            if (currentFragment == 1) {
                checkPollItems();
            } else {
                if (finalObject != null) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", finalObject.toString());
                    returnIntent.putExtra("Title", getCurrentFragmentTitle());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    ToastShow.setText(getBaseContext(), "Please add some elements.", Toast.LENGTH_LONG);
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getCurrentFragmentTitle() {
        String Title = null;
        switch (currentFragment) {
            case 0:
                Title = MediaTypes.YESNOMAYBE;
                break;
            case 1:
                Title = MediaTypes.POLL;
                break;
            case 2:
                Title = MediaTypes.FEEDBACK;
                break;
            case 3:
                Title = MediaTypes.LINKCALL;
                break;
        }
        return Title;
    }

    //Poll items validation.
    private void checkPollItems() {
        ArrayList pollItems = new ArrayList();
        String one = option1Text.getText().toString().trim();
        String two = option2Text.getText().toString().trim();
        String three = option3Text.getText().toString().trim();
        String four = option4Text.getText().toString().trim();
        String five = option5Text.getText().toString().trim();
        if (StringUtils.isNotBlank(one)) {
            pollItems.add(one);
        }
        if (StringUtils.isNotBlank(two)) {
            pollItems.add(two);
        }
        if (StringUtils.isNotBlank(three)) {
            pollItems.add(three);
        }
        if (StringUtils.isNotBlank(four)) {
            pollItems.add(four);
        }
        if (StringUtils.isNotBlank(five)) {
            pollItems.add(five);
        }
        if (pollItems.size() >= 2) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", finalObject.toString());
            returnIntent.putExtra("Title", getCurrentFragmentTitle());
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else {
            ToastShow.setText(this, "Poll must have 2 or more elements.", Snackbar.LENGTH_LONG);
        }
    }

    // all methods for interface between all  child fragments
    @Override
    public void yesNoMaybe(JSONObject jsonObject) {

        finalObject = new JSONObject();

        finalObject = jsonObject;

        Button Yes = (Button) findViewById(R.id.yes);

        Button NoButton = (Button) findViewById(R.id.no);

        NoButton.setVisibility(View.GONE);

        Button MaybeButton = (Button) findViewById(R.id.maybe);
        MaybeButton.setVisibility(View.GONE);

        TextView MessageStatus = (TextView) findViewById(R.id.yes_no_maybe_text);

        LinearLayout responseOutputLayout = (LinearLayout) findViewById(R.id.yes_no_maybe_Layout);

        responseOutputLayout.setVisibility(View.VISIBLE);

        try {

            MessageStatus.setText(jsonObject.getString("message"));

            JSONObject callToAction = jsonObject.getJSONObject("callToAction");

            JSONObject yesNoMaybe = callToAction.getJSONObject("yesNoMaybe");

            JSONArray jsonArrayBtn = yesNoMaybe.getJSONArray("buttons");

            for (int i = 0; i < jsonArrayBtn.length(); i++) {
                if (jsonArrayBtn.getString(i).equalsIgnoreCase("no")) {
                    NoButton.setVisibility(View.VISIBLE);
                } else if (jsonArrayBtn.getString(i).equalsIgnoreCase("maybe")) {
                    MaybeButton.setVisibility(View.VISIBLE);
                }
            }

            JSONArray propertiesArray = jsonObject.getJSONArray("properties");

            switch (propertiesArray.getJSONObject(2).getString("value")) {
                case "center":
                    responseOutputLayout.setGravity(Gravity.CENTER);
                    break;
                case "left":
                    responseOutputLayout.setGravity(Gravity.LEFT);
                    break;
                case "right":
                    responseOutputLayout.setGravity(Gravity.RIGHT);
                    break;
            }

            //int border = propertiesArray.getJSONObject(3).getInt("value");

            GradientDrawable gd = new GradientDrawable();

            GradientDrawable gd1 = new GradientDrawable();

            GradientDrawable gd2 = new GradientDrawable();

            gd.setColor(Color.parseColor("#57ac2d"));
            gd1.setColor(Color.parseColor("#B22222"));
            gd2.setColor(Color.parseColor("#FFA500"));
            gd.setCornerRadius(8);
            gd1.setCornerRadius(8);
            gd2.setCornerRadius(8);

            Yes.setBackgroundDrawable(gd);
            NoButton.setBackgroundDrawable(gd1);
            MaybeButton.setBackgroundDrawable(gd2);

            YesNoMaybeFragment fragment = new YesNoMaybeFragment();
            if (fragment != null) {

                fragment.fragmentCommunication(jsonObject.toString());

            } else {
                Log.i(TAG, "Fragment 2 is not initialized");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pollItems(JSONObject jsonObject) {

        finalObject = new JSONObject();

        finalObject = jsonObject;

        optionOneLayout.setVisibility(View.GONE);

        optionTwoLayout.setVisibility(View.GONE);

        optionThreeLayout.setVisibility(View.GONE);

        optionFourLayout.setVisibility(View.GONE);

        optionFiveLayout.setVisibility(View.GONE);

        try {

            pollText.setText(jsonObject.getString("message"));

            JSONObject jsonObject1 = jsonObject.getJSONObject("callToAction");

            JSONArray jsonArray = jsonObject1.getJSONArray("pollItems");

            option1Text.setText("");
            option2Text.setText("");
            option3Text.setText("");
            option4Text.setText("");
            option5Text.setText("");

            for (int i = 0; i < jsonArray.length(); i++) {

                String data = jsonArray.getJSONObject(i).getString("label");

                switch (i) {
                    case 0:
                        if (data.length() > 0) {
                            optionOneLayout.setVisibility(View.VISIBLE);
                            option1Text.setText(data);
                        }
                        break;
                    case 1:
                        if (data.length() > 0) {
                            optionTwoLayout.setVisibility(View.VISIBLE);
                            option2Text.setText(data);

                        }
                        break;
                    case 2:
                        if (data.length() > 0) {
                            optionThreeLayout.setVisibility(View.VISIBLE);
                            option3Text.setText(data);
                        }
                        break;

                    case 3:
                        if (data.length() > 0) {
                            optionFourLayout.setVisibility(View.VISIBLE);
                            option4Text.setText(data);
                        }
                        break;
                    case 4:
                        if (data.length() > 0) {
                            optionFiveLayout.setVisibility(View.VISIBLE);
                            option5Text.setText(data);
                        }
                        break;
                }
            }
            PollFragment fragment = new PollFragment();
            if (fragment != null) {
                fragment.fragmentCommunication(jsonObject.toString());
            } else {
                Log.i(TAG, "PollFragment is not initialized");
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void feedBack(JSONObject jsonObject) {
        finalObject = new JSONObject();
        finalObject = jsonObject;

        TextView feedbackText = (TextView) findViewById(R.id.feedbackText);

        RatingBar feedbackRating = (RatingBar) findViewById(R.id.RatingBar);

        try {
            String message = jsonObject.getString("message");
            if (message != null) {
                feedbackText.setText(message);
            }

            JSONArray properties = jsonObject.getJSONArray("properties");

            switch (properties.getJSONObject(2).getString("value")) {
                case "center":
                    ((LinearLayout.LayoutParams) feedbackOutputLayout.getLayoutParams()).gravity = Gravity.CENTER;
                    feedbackOutputLayout.setGravity(Gravity.CENTER);
                    feedbackText.setGravity(Gravity.CENTER);
                    break;
                case "left":
                    ((LinearLayout.LayoutParams) feedbackOutputLayout.getLayoutParams()).gravity = Gravity.LEFT;
                    feedbackOutputLayout.setGravity(Gravity.LEFT);
                    feedbackText.setGravity(Gravity.LEFT);
                    break;
                case "right":
                    ((LinearLayout.LayoutParams) feedbackOutputLayout.getLayoutParams()).gravity = Gravity.RIGHT;
                    feedbackOutputLayout.setGravity(Gravity.RIGHT);
                    feedbackText.setGravity(Gravity.RIGHT);
                    break;
            }
            int ElementsSize = properties.getJSONObject(3).getInt("value");
            if (ElementsSize <= 35) {
                SCALE_X.set(feedbackRating, (float) 0.3);
                SCALE_Y.set(feedbackRating, (float) 0.3);
            }
            if (ElementsSize > 35 || ElementsSize == 0) {
                SCALE_X.set(feedbackRating, (float) 0.7);
                SCALE_Y.set(feedbackRating, (float) 0.7);
            }
            if (ElementsSize > 75) {
                SCALE_X.set(feedbackRating, (float) 1);
                SCALE_Y.set(feedbackRating, (float) 1);
            }
            FeedBackFragment fragment = new FeedBackFragment();
            if (fragment != null) {
                fragment.fragmentCommunication(jsonObject.toString());
            } else {
                Log.i(TAG, "PollFragment is not initialized");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callToAction(JSONObject jsonObject) {

        finalObject = new JSONObject();

        finalObject = jsonObject;

        try {

            JSONObject linkCall = jsonObject.getJSONObject("callToAction").getJSONObject("linkCall");

            JSONArray LinkCallProperties = linkCall.getJSONArray("properties");

            String Title = LinkCallProperties.getJSONObject(1).getString("value");

            if (Title != null) {
                btn_LinkCall.setText(Title);
            }

            JSONArray properties = jsonObject.getJSONArray("properties");

            switch (properties.getJSONObject(2).getString("value")) {
                case "center":
                    ((LinearLayout.LayoutParams) linkOutputLayout.getLayoutParams()).gravity = Gravity.CENTER;
                    linkOutputLayout.setGravity(Gravity.CENTER);
                    break;
                case "left":
                    ((LinearLayout.LayoutParams) linkOutputLayout.getLayoutParams()).gravity = Gravity.LEFT;
                    linkOutputLayout.setGravity(Gravity.LEFT);
                    break;
                case "right":
                    ((LinearLayout.LayoutParams) responseOutputLayout.getLayoutParams()).gravity = Gravity.RIGHT;
                    linkOutputLayout.setGravity(Gravity.RIGHT);
                    break;
            }

            //int buttonCorner = properties.getJSONObject(3).getInt("value");

            //int buttonSize = properties.getJSONObject(4).getInt("value");

            GradientDrawable gd = new GradientDrawable();

            gd.setColor(Color.parseColor("#FFA500"));

            gd.setCornerRadius(8);

            btn_LinkCall.setBackgroundDrawable(gd);
//
//
//            Log.i("ButtonSize", String.valueOf(buttonSize));
//
//            if (buttonSize > 120) {
//                btn_LinkCall.setLayoutParams(new LinearLayout.LayoutParams(buttonSize, buttonSize / 3));
//            } else {
//                btn_LinkCall.setLayoutParams(new LinearLayout.LayoutParams(200, 200 / 3));
//            }

            LinkCallFragment fragment = new LinkCallFragment();
            if (fragment != null) {

                Log.i(TAG, jsonObject.toString());

                fragment.fragmentCommunication(jsonObject.toString());
            } else {
                Log.i(TAG, "PollFragment is not initialized");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //null validation to handle null pointer exception
    private boolean isNull(String data) {
        if (!data.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    // Method for  Initialize all UI Layouts
    private void InitializeLayouts() {

        //All toggle yesNoMaybe
        yes_no_maybe_btn = (ToggleButton) findViewById(R.id.yes_no_maybe_btn);
        yes_no_maybe_btn.setOnClickListener(this);

        poll_btn = (ToggleButton) findViewById(R.id.poll_btn);
        poll_btn.setOnClickListener(this);

        feedback_btn = (ToggleButton) findViewById(R.id.feedback_btn);
        feedback_btn.setOnClickListener(this);

        Link_Call_btn = (ToggleButton) findViewById(R.id.Link_Call_btn);
        Link_Call_btn.setOnClickListener(this);

        responseToggleText = (TextView) findViewById(R.id.responseToggleText);

        pollToggleText = (TextView) findViewById(R.id.pollToggleText);

        feedbackToggleText = (TextView) findViewById(R.id.feedbackToggleText);

        callToggleText = (TextView) findViewById(R.id.callToggleText);

        //top layouts for handle texts
        responseOutputLayout = (LinearLayout) findViewById(R.id.yes_no_maybe_Layout);

        pollOutputLayout = (LinearLayout) findViewById(R.id.pollOutputLayout);

        feedbackOutputLayout = (LinearLayout) findViewById(R.id.feedbackOutputLayout);

        linkOutputLayout = (LinearLayout) findViewById(R.id.linkOutputLayout);


        //poll items
        pollText = (TextView) findViewById(R.id.pollText);

        option1Text = (TextView) findViewById(R.id.option1Text);

        option2Text = (TextView) findViewById(R.id.option2Text);

        option3Text = (TextView) findViewById(R.id.option3Text);

        option4Text = (TextView) findViewById(R.id.option4Text);

        option5Text = (TextView) findViewById(R.id.option5Text);

        optionOneLayout = (LinearLayout) findViewById(R.id.optionOneLayout);

        optionTwoLayout = (LinearLayout) findViewById(R.id.optionTwoLayout);

        optionThreeLayout = (LinearLayout) findViewById(R.id.optionThreeLayout);
        optionFourLayout = (LinearLayout) findViewById(R.id.optionFourLayout);
        optionFiveLayout = (LinearLayout) findViewById(R.id.optionFiveLayout);

        btn_LinkCall = (Button) findViewById(R.id.button);

    }

    // Handle Fragments state with toggle buttons
    private void handleButtons(int id) {
        finalObject = new JSONObject();
        switch (id) {
            //yes no maybe
            case 0:
                pollOutputLayout.setVisibility(View.GONE);
                feedbackOutputLayout.setVisibility(View.GONE);
                linkOutputLayout.setVisibility(View.GONE);

                feedbackToggleText.setTextColor(Color.parseColor("#D3D3D3"));
                pollToggleText.setTextColor(Color.parseColor("#D3D3D3"));
                callToggleText.setTextColor(Color.parseColor("#D3D3D3"));

                responseToggleText.setTextColor(Color.parseColor("#57ac2d"));
                responseOutputLayout.setVisibility(View.VISIBLE);
                yes_no_maybe_btn.setChecked(true);
                break;
            //poll items
            case 1:
                responseOutputLayout.setVisibility(View.GONE);
                feedbackOutputLayout.setVisibility(View.GONE);
                linkOutputLayout.setVisibility(View.GONE);

                responseToggleText.setTextColor(Color.parseColor("#D3D3D3"));
                feedbackToggleText.setTextColor(Color.parseColor("#D3D3D3"));
                pollToggleText.setTextColor(Color.parseColor("#57ac2d"));
                callToggleText.setTextColor(Color.parseColor("#D3D3D3"));

                pollOutputLayout.setVisibility(View.VISIBLE);
                poll_btn.setChecked(true);
                break;
            //feedback star rating
            case 2:
                responseOutputLayout.setVisibility(View.GONE);
                pollOutputLayout.setVisibility(View.GONE);
                linkOutputLayout.setVisibility(View.GONE);

                responseToggleText.setTextColor(Color.parseColor("#D3D3D3"));
                pollToggleText.setTextColor(Color.parseColor("#D3D3D3"));
                feedbackToggleText.setTextColor(Color.parseColor("#57ac2d"));
                callToggleText.setTextColor(Color.parseColor("#D3D3D3"));

                feedbackOutputLayout.setVisibility(View.VISIBLE);
                feedback_btn.setChecked(true);
                break;
            //link call
            case 3:
                responseOutputLayout.setVisibility(View.GONE);
                pollOutputLayout.setVisibility(View.GONE);
                feedbackOutputLayout.setVisibility(View.GONE);

                responseToggleText.setTextColor(Color.parseColor("#D3D3D3"));
                pollToggleText.setTextColor(Color.parseColor("#D3D3D3"));
                feedbackToggleText.setTextColor(Color.parseColor("#D3D3D3"));
                callToggleText.setTextColor(Color.parseColor("#57ac2d"));

                linkOutputLayout.setVisibility(View.VISIBLE);
                Link_Call_btn.setChecked(true);
                break;
        }
    }
}


