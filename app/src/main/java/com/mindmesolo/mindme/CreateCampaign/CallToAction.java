package com.mindmesolo.mindme.CreateCampaign;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mindmesolo.mindme.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User1 on 7/8/2016.
 */

public class CallToAction extends AppCompatActivity {

    private static final String TAG = "CallToActionModel";
    ToggleButton toggleButtonresponse, toggleButtonpoll, toggleButtonfeedback, toggleButtonlinkcall, toggleButtonleft, toggleButtoncenter, toggleButtonright;

    ToggleButton feedbackLeft, feedbackCenter, feedbackRight, toggleLink, toggleCall, toggleEmail, linkLeft, linkCenter, linkRight;

    LinearLayout linearLayoutresponseoutput, linearLayoutresponse, linearLayoutPollOutput, linearLayoutPoll, linearLayoutFeedbackOutput, linearLayoutfeedback;

    LinearLayout linearLayoutOutputLink, linearLayoutLink;

    SwitchCompat switchNo, switchMaybe;

    Button buttonYes, buttonNo, buttonMaybe, button;

    EditText editText, editTextTitle, editTextAction;

    EditText editTextPollText, editTextOption1, editTextOption2, editTextOption3, editTextFeedback;

    TextView textViewResponse, textViewPollText, option1Text, option2Text, option3Text, feedbackText;

    TextView textViewResponseToggleText, textViewPollToggleText, textViewFeedbackToggleText, textViewCallToggleText;

    SeekBar seekBarBorderCorner, seekBarBorderCornerLink, SeekBarButtonSize;

    String cta;

    int fontSize = 20, fontWeight = 250, borderRadius = 5;

    JSONObject jsonObjectPollitems = new JSONObject();

    JSONObject jsonObjectLinkCall = new JSONObject();

    JSONArray jsonArrayPollItems = new JSONArray();

    JSONArray jsonArrayLinkCallProperties = new JSONArray();

    JSONObject jsonObjectLinkCallProperties = new JSONObject();

    String alignment = "center";

    JSONArray jsonArrayButtons = new JSONArray();

    JSONObject jsonObjectComponent = new JSONObject();

    JSONObject jsonObjectCallToAction = new JSONObject();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.calltoaction);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toggleButtonresponse = (ToggleButton) findViewById(R.id.responsebtn);
        toggleButtonpoll = (ToggleButton) findViewById(R.id.pollbtn);
        toggleButtonfeedback = (ToggleButton) findViewById(R.id.feedbackbtn);
        toggleButtonlinkcall = (ToggleButton) findViewById(R.id.linkcallbtn);
        toggleButtonleft = (ToggleButton) findViewById(R.id.left);
        toggleButtoncenter = (ToggleButton) findViewById(R.id.center);
        toggleButtonright = (ToggleButton) findViewById(R.id.right);
        toggleCall = (ToggleButton) findViewById(R.id.call);
        toggleEmail = (ToggleButton) findViewById(R.id.email);
        toggleLink = (ToggleButton) findViewById(R.id.link);

        feedbackRight = (ToggleButton) findViewById(R.id.rightfeedback);
        feedbackLeft = (ToggleButton) findViewById(R.id.leftfeedback);
        feedbackCenter = (ToggleButton) findViewById(R.id.centerfeedback);

        linkRight = (ToggleButton) findViewById(R.id.rightLink);
        linkLeft = (ToggleButton) findViewById(R.id.leftLink);
        linkCenter = (ToggleButton) findViewById(R.id.centerLink);

        linearLayoutresponseoutput = (LinearLayout) findViewById(R.id.yes_no_maybe_Layout);
        linearLayoutresponse = (LinearLayout) findViewById(R.id.responelayout);
        linearLayoutPollOutput = (LinearLayout) findViewById(R.id.pollOutputLayout);
        linearLayoutPoll = (LinearLayout) findViewById(R.id.pollLayout);

        linearLayoutFeedbackOutput = (LinearLayout) findViewById(R.id.feedbackOutputLayout);
        linearLayoutfeedback = (LinearLayout) findViewById(R.id.feedbackLayout);
        linearLayoutOutputLink = (LinearLayout) findViewById(R.id.linkOutputLayout);
        linearLayoutLink = (LinearLayout) findViewById(R.id.linkLayout);

        switchNo = (SwitchCompat) findViewById(R.id.nobtn);
        switchMaybe = (SwitchCompat) findViewById(R.id.maybebtn);

        buttonYes = (Button) findViewById(R.id.yes);
        buttonNo = (Button) findViewById(R.id.no);
        buttonMaybe = (Button) findViewById(R.id.maybe);
        button = (Button) findViewById(R.id.button);

        editText = (EditText) findViewById(R.id.response);

        editTextPollText = (EditText) findViewById(R.id.pollEditText);
        editTextOption1 = (EditText) findViewById(R.id.option1EditText);
        editTextOption2 = (EditText) findViewById(R.id.option2EditText);
        editTextOption3 = (EditText) findViewById(R.id.option3EditText);
        editTextFeedback = (EditText) findViewById(R.id.feedbackEditText);

        editTextTitle = (EditText) findViewById(R.id.EditTextTitle);
        editTextAction = (EditText) findViewById(R.id.EditTextAction);
        feedbackText = (TextView) findViewById(R.id.feedbackText);
        textViewResponse = (TextView) findViewById(R.id.yes_no_maybe_text);
        textViewPollText = (TextView) findViewById(R.id.pollText);

        option1Text = (TextView) findViewById(R.id.option1Text);
        option2Text = (TextView) findViewById(R.id.option2Text);
        option3Text = (TextView) findViewById(R.id.option3Text);

        textViewResponseToggleText = (TextView) findViewById(R.id.responseToggleText);
        textViewPollToggleText = (TextView) findViewById(R.id.pollToggleText);
        textViewFeedbackToggleText = (TextView) findViewById(R.id.feedbackToggleText);
        textViewCallToggleText = (TextView) findViewById(R.id.callToggleText);
        toggleButtoncenter.setBackgroundResource(R.drawable.centeralign);
        feedbackCenter.setBackgroundResource(R.drawable.centeralign);

        seekBarBorderCorner = (SeekBar) findViewById(R.id.buttonCornerSeekbar);
        seekBarBorderCornerLink = (SeekBar) findViewById(R.id.buttonCornerSeekbarLink);
        SeekBarButtonSize = (SeekBar) findViewById(R.id.buttonSizeSeekbarLink);

        aligment();

        Nobutton();

        Maybebutton();

        responseText();

        toggleLinkCall();

        Bundle extras = getIntent().getExtras();

        cta = extras.getString("CTA");

        checkset(cta);
        seekBarBorderCorner.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                GradientDrawable gd = new GradientDrawable();
                GradientDrawable gd1 = new GradientDrawable();
                GradientDrawable gd2 = new GradientDrawable();
                gd.setColor(Color.parseColor("#81C784"));
                gd1.setColor(Color.parseColor("#B22222"));
                gd2.setColor(Color.parseColor("#FF6600"));
                gd.setCornerRadius(progress);
                gd1.setCornerRadius(progress);
                gd2.setCornerRadius(progress);
                //gd.setStroke(1, 0xFF000000);
                buttonMaybe.setBackgroundDrawable(gd2);
                buttonYes.setBackgroundDrawable(gd);
                buttonNo.setBackgroundDrawable(gd1);
                borderRadius = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarBorderCornerLink.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                GradientDrawable gd = new GradientDrawable();
                gd.setColor(Color.parseColor("#81C784"));
                gd.setCornerRadius(progress);
                button.setBackgroundDrawable(gd);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        SeekBarButtonSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // ButtonContactType.setWidth(4*progress);
                //  ButtonContactType.setHeight(progress);
                button.setLayoutParams(new LinearLayout.LayoutParams(4 * progress, progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void checkset(String cta) {
        if (cta.equals("Response")) {
            toggleButtonresponse.setChecked(true);
            jsonArrayButtons.put("yes");
            JSONObject jsonObjectYesNoMaybe = new JSONObject();
            try {
                jsonObjectYesNoMaybe.put("yesNoMaybe", jsonArrayButtons);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                jsonObjectCallToAction.put("feedback", null);
                jsonObjectCallToAction.put("yesNoMaybe", jsonObjectYesNoMaybe);
                jsonObjectCallToAction.put("pollItems", null);
                jsonObjectCallToAction.put("linkCall", null);
                jsonObjectCallToAction.put("type", "YESNOMAYBE");
                // jsonArrayCallToAction.put(jsonObjectCallToAction);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (cta.equals("Feedback")) {
            toggleButtonfeedback.setChecked(true);
            JSONObject jsonObjectFeedback = new JSONObject();
            try {
                jsonObjectFeedback.put("description", "");
                jsonObjectFeedback.put("mediumScale", 5);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                jsonObjectCallToAction.put("feedback", jsonObjectFeedback);
                jsonObjectCallToAction.put("yesNoMaybe", null);
                jsonObjectCallToAction.put("pollItems", null);
                jsonObjectCallToAction.put("linkCall", null);
                jsonObjectCallToAction.put("type", "FEEDBACK");


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (cta.equals("T")) {
            toggleButtonpoll.setChecked(true);
            try {
                jsonObjectCallToAction.put("feedback", null);
                jsonObjectCallToAction.put("yesNoMaybe", null);
                jsonObjectCallToAction.put("pollItems", jsonArrayPollItems);
                jsonObjectCallToAction.put("linkCall", null);
                jsonObjectCallToAction.put("type", "POLL");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (cta.equals("Poll")) {
            toggleButtonpoll.setChecked(true);
            try {
                jsonObjectCallToAction.put("feedback", null);
                jsonObjectCallToAction.put("yesNoMaybe", null);
                jsonObjectCallToAction.put("pollItems", jsonArrayPollItems);
                jsonObjectCallToAction.put("linkCall", null);
                jsonObjectCallToAction.put("type", "POLL");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (cta.equals("Link/Call")) {
            try {
                jsonObjectLinkCall.put("properties", jsonArrayLinkCallProperties);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            toggleButtonlinkcall.setChecked(true);
            try {

                jsonObjectCallToAction.put("feedback", null);
                jsonObjectCallToAction.put("yesNoMaybe", null);
                jsonObjectCallToAction.put("pollItems", null);
                jsonObjectCallToAction.put("linkCall", jsonObjectLinkCall);
                jsonObjectCallToAction.put("type", "CALLME");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (toggleButtonresponse.isChecked()) {
            toggleButtonresponse.setBackgroundResource(R.drawable.responseselected);
            toggleButtonpoll.setBackgroundResource(R.drawable.pollunselected);
            toggleButtonfeedback.setBackgroundResource(R.drawable.feedbackunselected);
            toggleButtonlinkcall.setBackgroundResource(R.drawable.callunselected);
            textViewResponseToggleText.setTextColor(Color.parseColor("#57ac2d"));
            linearLayoutresponseoutput.setVisibility(View.VISIBLE);
            linearLayoutresponse.setVisibility(View.VISIBLE);
            linearLayoutPollOutput.setVisibility(View.GONE);
            linearLayoutPoll.setVisibility(View.GONE);
            linearLayoutFeedbackOutput.setVisibility(View.GONE);
            linearLayoutfeedback.setVisibility(View.GONE);
            linearLayoutLink.setVisibility(View.GONE);
            linearLayoutOutputLink.setVisibility(View.GONE);
        } else if (toggleButtonpoll.isChecked()) {
            toggleButtonresponse.setBackgroundResource(R.drawable.responseunselected);
            toggleButtonpoll.setBackgroundResource(R.drawable.pollselected);
            toggleButtonfeedback.setBackgroundResource(R.drawable.feedbackunselected);
            toggleButtonlinkcall.setBackgroundResource(R.drawable.callunselected);
            linearLayoutresponseoutput.setVisibility(View.GONE);
            linearLayoutresponse.setVisibility(View.GONE);
            linearLayoutPollOutput.setVisibility(View.VISIBLE);
            linearLayoutPoll.setVisibility(View.VISIBLE);
            linearLayoutFeedbackOutput.setVisibility(View.GONE);
            linearLayoutfeedback.setVisibility(View.GONE);
            linearLayoutLink.setVisibility(View.GONE);
            linearLayoutOutputLink.setVisibility(View.GONE);
            textViewPollToggleText.setTextColor(Color.parseColor("#57ac2d"));
        } else if (toggleButtonfeedback.isChecked()) {
            toggleButtonresponse.setBackgroundResource(R.drawable.responseunselected);
            toggleButtonpoll.setBackgroundResource(R.drawable.pollunselected);
            toggleButtonfeedback.setBackgroundResource(R.drawable.feedbackselected);
            toggleButtonlinkcall.setBackgroundResource(R.drawable.callunselected);
            textViewFeedbackToggleText.setTextColor(Color.parseColor("#57ac2d"));
            linearLayoutresponseoutput.setVisibility(View.GONE);
            linearLayoutresponse.setVisibility(View.GONE);
            linearLayoutPollOutput.setVisibility(View.GONE);
            linearLayoutPoll.setVisibility(View.GONE);
            linearLayoutFeedbackOutput.setVisibility(View.VISIBLE);
            linearLayoutfeedback.setVisibility(View.VISIBLE);
            linearLayoutLink.setVisibility(View.GONE);
            linearLayoutOutputLink.setVisibility(View.GONE);
        } else if (toggleButtonlinkcall.isChecked()) {
            toggleButtonresponse.setBackgroundResource(R.drawable.responseunselected);
            toggleButtonpoll.setBackgroundResource(R.drawable.pollunselected);
            toggleButtonfeedback.setBackgroundResource(R.drawable.feedbackunselected);
            toggleButtonlinkcall.setBackgroundResource(R.drawable.callselected);
            textViewCallToggleText.setTextColor(Color.parseColor("#57ac2d"));
            linearLayoutresponseoutput.setVisibility(View.GONE);
            linearLayoutresponse.setVisibility(View.GONE);
            linearLayoutPollOutput.setVisibility(View.GONE);
            linearLayoutPoll.setVisibility(View.GONE);
            linearLayoutFeedbackOutput.setVisibility(View.GONE);
            linearLayoutfeedback.setVisibility(View.GONE);
            linearLayoutLink.setVisibility(View.VISIBLE);
            linearLayoutOutputLink.setVisibility(View.VISIBLE);
        }
    }

    private void toggleLinkCall() {
        toggleLink.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleLink.setTextColor(Color.parseColor("#75C25A"));
                    toggleEmail.setTextColor(Color.GRAY);
                    toggleCall.setTextColor(Color.GRAY);
                    button.setText("Click Here");
                    button.setTextColor(Color.WHITE);
                    editTextAction.setHint("Add URL");
                    editTextTitle.setHint("Click Here");
                    try {
                        jsonObjectLinkCall.put("phone", "");
                        jsonObjectLinkCall.put("link", editTextTitle.getText().toString());
                        jsonObjectLinkCall.put("email", "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        toggleCall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleCall.setTextColor(Color.parseColor("#75C25A"));
                    toggleEmail.setTextColor(Color.GRAY);
                    toggleLink.setTextColor(Color.GRAY);
                    button.setText("Click To Call");
                    button.setTextColor(Color.WHITE);
                    editTextAction.setHint("Add Phone");
                    editTextTitle.setHint("Click To Call");

                    try {
                        jsonObjectLinkCall.put("phone", editTextTitle.getText().toString());
                        jsonObjectLinkCall.put("link", "");
                        jsonObjectLinkCall.put("email", "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        toggleEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleEmail.setTextColor(Color.parseColor("#75C25A"));
                    toggleLink.setTextColor(Color.GRAY);
                    toggleCall.setTextColor(Color.GRAY);
                    button.setTextColor(Color.WHITE);
                    button.setText("Click To Email");
                    editTextAction.setHint("Add Email");
                    editTextTitle.setHint("Click To Email");
                    try {
                        jsonObjectLinkCall.put("phone", "");
                        jsonObjectLinkCall.put("link", "");
                        jsonObjectLinkCall.put("email", editTextTitle.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void responseText() {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String abc = editText.getText().toString();
                textViewResponse.setText(abc);
                try {
                    jsonObjectComponent.put("message", textViewResponse.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        editTextPollText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String abc = editTextPollText.getText().toString();
                textViewPollText.setText(abc);
                try {
                    jsonObjectComponent.put("message", textViewPollText.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        editTextOption1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String abc = editTextOption1.getText().toString();
                option1Text.setText(abc);

            }
        });
        editTextOption2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String abc = editTextOption2.getText().toString();
                option2Text.setText(abc);

            }
        });
        editTextOption3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String abc = editTextOption3.getText().toString();
                option3Text.setText(abc);

            }
        });
        editTextFeedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String abc = editTextFeedback.getText().toString();
                feedbackText.setText(abc);
                try {
                    jsonObjectComponent.put("message", feedbackText.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        editTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String abc = editTextTitle.getText().toString();
                button.setText(abc);
                try {
                    jsonObjectComponent.put("message", abc);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    private void Maybebutton() {

        switchMaybe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonMaybe.setVisibility(View.VISIBLE);
                    jsonArrayButtons.put("maybe");
                } else {
                    buttonMaybe.setVisibility(View.GONE);
                    int j = getJsonIndex("maybe");
                    jsonArrayButtons.remove(j);
                }
            }
        });
    }

    private void Nobutton() {

        switchNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonNo.setVisibility(View.VISIBLE);
                    jsonArrayButtons.put("no");
                } else {
                    buttonNo.setVisibility(View.GONE);
                    int j = getJsonIndex("no");
                    jsonArrayButtons.remove(j);
                }
            }
        });
    }

    private int getJsonIndex(String name) {
        for (int i = 0; i < jsonArrayButtons.length(); i++) {
            try {
                String demo = String.valueOf(jsonArrayButtons.get(i));
                if (demo.equals(name)) {
                    return i;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return Integer.parseInt(null);
    }

    ;

    public void aligment() {

        ///..................response....................///
        toggleButtonleft.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    linearLayoutresponseoutput.setGravity(Gravity.LEFT);
                    toggleButtonleft.setBackgroundResource(R.drawable.leftalign);
                    toggleButtoncenter.setBackgroundResource(R.drawable.centeralignunselected);
                    toggleButtonright.setBackgroundResource(R.drawable.rightalignunselected);
                    alignment = "left";
                }
            }
        });
        toggleButtoncenter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    linearLayoutresponseoutput.setGravity(Gravity.CENTER);
                    toggleButtoncenter.setBackgroundResource(R.drawable.centeralign);
                    toggleButtonleft.setBackgroundResource(R.drawable.leftalignunselected);
                    toggleButtonright.setBackgroundResource(R.drawable.rightalignunselected);
                    alignment = "center";
                }
            }
        });
        toggleButtonright.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    linearLayoutresponseoutput.setGravity(Gravity.RIGHT);
                    toggleButtonright.setBackgroundResource(R.drawable.rightalign);
                    toggleButtonleft.setBackgroundResource(R.drawable.leftalignunselected);
                    toggleButtoncenter.setBackgroundResource(R.drawable.centeralignunselected);
                    alignment = "right";
                }
            }
        });
        ///.....................feedback.....................///
        feedbackLeft.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    linearLayoutFeedbackOutput.setGravity(Gravity.LEFT);
                    feedbackLeft.setBackgroundResource(R.drawable.leftalign);
                    feedbackCenter.setBackgroundResource(R.drawable.centeralignunselected);
                    feedbackRight.setBackgroundResource(R.drawable.rightalignunselected);
                    alignment = "left";
                }
            }
        });
        feedbackCenter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    linearLayoutFeedbackOutput.setGravity(Gravity.CENTER);
                    feedbackCenter.setBackgroundResource(R.drawable.centeralign);
                    feedbackLeft.setBackgroundResource(R.drawable.leftalignunselected);
                    feedbackRight.setBackgroundResource(R.drawable.rightalignunselected);
                    alignment = "center";
                }
            }
        });
        feedbackRight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    linearLayoutFeedbackOutput.setGravity(Gravity.RIGHT);
                    feedbackRight.setBackgroundResource(R.drawable.rightalign);
                    feedbackLeft.setBackgroundResource(R.drawable.leftalignunselected);
                    feedbackCenter.setBackgroundResource(R.drawable.centeralignunselected);
                    alignment = "right";
                }
            }
        });
        ///.....................Link.....................///
        linkLeft.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    linearLayoutOutputLink.setGravity(Gravity.LEFT);
                    linkLeft.setBackgroundResource(R.drawable.leftalign);
                    linkCenter.setBackgroundResource(R.drawable.centeralignunselected);
                    linkRight.setBackgroundResource(R.drawable.rightalignunselected);
                    alignment = "left";
                }
            }
        });
        linkCenter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    linearLayoutOutputLink.setGravity(Gravity.CENTER);
                    linkCenter.setBackgroundResource(R.drawable.centeralign);
                    linkLeft.setBackgroundResource(R.drawable.leftalignunselected);
                    linkRight.setBackgroundResource(R.drawable.rightalignunselected);
                    alignment = "center";
                }
            }
        });
        linkRight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    linearLayoutOutputLink.setGravity(Gravity.RIGHT);
                    linkRight.setBackgroundResource(R.drawable.rightalign);
                    linkLeft.setBackgroundResource(R.drawable.leftalignunselected);
                    linkCenter.setBackgroundResource(R.drawable.centeralignunselected);
                    alignment = "right";
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.done, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up ButtonContactType, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.done) {
            try {
                jsonObjectPollitems.put("label", option1Text.getText().toString());
                jsonObjectPollitems.put("link", "");
                jsonObjectPollitems.put("description", "");
                jsonArrayPollItems.put(jsonObjectPollitems);
                jsonObjectPollitems = new JSONObject();

                jsonObjectPollitems.put("label", option2Text.getText().toString());
                jsonObjectPollitems.put("link", "");
                jsonObjectPollitems.put("description", "");
                jsonArrayPollItems.put(jsonObjectPollitems);

                jsonObjectPollitems = new JSONObject();
                jsonObjectPollitems.put("label", option3Text.getText().toString());
                jsonObjectPollitems.put("link", "");
                jsonObjectPollitems.put("description", "");

                jsonArrayPollItems.put(jsonObjectPollitems);

                jsonObjectLinkCallProperties.put("name", "color");
                jsonObjectLinkCallProperties.put("value", "#000000");

                jsonArrayLinkCallProperties.put(jsonObjectLinkCallProperties);

                jsonObjectLinkCallProperties = new JSONObject();
                jsonObjectLinkCallProperties.put("name", "title");
                jsonObjectLinkCallProperties.put("value", editTextTitle.getText().toString());

                jsonArrayLinkCallProperties.put(jsonObjectLinkCallProperties);

                jsonObjectLinkCallProperties = new JSONObject();
                jsonObjectLinkCallProperties.put("name", "action");
                jsonObjectLinkCallProperties.put("value", editTextAction.getText().toString());
                jsonArrayLinkCallProperties.put(jsonObjectLinkCallProperties);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (toggleButtonpoll.isChecked() && option1Text.getText().length() == 0 && option2Text.getText().length() == 0 && option3Text.getText().length() == 0) {
            } else {
                JSONArray jsonArrayProperties = new JSONArray();
                try {
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("name", "fontSize");
                    jsonObject1.put("value", 20);
                    jsonArrayProperties.put(jsonObject1);
                    JSONObject jsonObject2 = new JSONObject();
                    jsonObject2.put("name", "fontWeight");
                    jsonObject2.put("value", 250);
                    jsonArrayProperties.put(jsonObject2);
                    JSONObject jsonObject3 = new JSONObject();
                    jsonObject3.put("name", "alignment");
                    jsonObject3.put("value", alignment);
                    jsonArrayProperties.put(jsonObject3);
                    if (toggleButtonresponse.isChecked() || toggleButtonlinkcall.isChecked()) {
                        JSONObject jsonObject4 = new JSONObject();
                        jsonObject4.put("name", "buttonCorners");
                        jsonObject4.put("value", borderRadius);
                        jsonArrayProperties.put(jsonObject4);
                    }
                    if (toggleButtonfeedback.isChecked()) {
                        JSONObject jsonObject5 = new JSONObject();
                        jsonObject5.put("name", "elementSize");
                        jsonObject5.put("value", "small");
                        jsonArrayProperties.put(jsonObject5);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonObjectComponent.put("properties", jsonArrayProperties);
                    jsonObjectComponent.put("type", "CALLTOACTION");
                    jsonObjectComponent.put("mediaFileName", "");
                    jsonObjectComponent.put("media", "");
                    jsonObjectComponent.put("mediaExternalId", "");
                    jsonObjectComponent.put("mediaUrl", "");
                    jsonObjectComponent.put("callToAction", jsonObjectCallToAction);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", jsonObjectComponent.toString());

                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
