package com.mindmesolo.mindme.CreateCampaign;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.mindmesolo.mindme.Dashboard;
import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.Services.VolleyApi;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.TagLayout;
import com.mindmesolo.mindme.helper.ToastShow;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import id.zelory.compressor.Compressor;
import id.zelory.compressor.FileUtil;

/**
 * Created by eNest on 7/22/2016.
 */

public class CreateCampaignPreviewScreen extends AppCompatActivity {

    private static final String TAG = "CampaignPreviewScreen";

    ImageView arrowImage, arrowImage2;

    SwitchCompat ScheduleMsgSwitch, ReminderTextSwitch;

    RelativeLayout ScheduleMsg, ReminderText;

    TextView textView1, textViewMessage, textViewContacts, contactstext, messagetext;

    TagLayout tagLayout;

    TextView textViewGetDate, textViewGetTime;

    SeekBar myseekbar;

    DataHelper dataHelper = new DataHelper();

    EditText editTextCampaignTitle;

    JSONObject jsonObjectFinalCampaign = new JSONObject();

    Button buttonSend;

    String dateTime;

    int reminderTime;

    long scheduleTime;

    SqliteDataBaseHelper mySqliteDataBaseHelper;

    // Get Current Date
    Calendar myCalendar;

    SimpleDateFormat DateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.US);

    SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm:ss a", Locale.US);

    RelativeLayout layoutTextReminder;

    private ProgressDialog pDialog;

    private Dialog dialogue_custom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_campaign_preview_screen);

        editTextCampaignTitle = (EditText) findViewById(R.id.campaignTitle);

        textViewContacts = (TextView) findViewById(R.id.SelectedContactsStatus);

        textViewMessage = (TextView) findViewById(R.id.messagestatus);

        textViewGetDate = (TextView) findViewById(R.id.getDate);

        textViewGetTime = (TextView) findViewById(R.id.getTime);

        contactstext = (TextView) findViewById(R.id.contactText);

        messagetext = (TextView) findViewById(R.id.messagetext);

        layoutTextReminder = (RelativeLayout) findViewById(R.id.layoutTextReminder);

        mySqliteDataBaseHelper = new SqliteDataBaseHelper(getBaseContext());

        prepareListData();

        buttonSend = (Button) findViewById(R.id.btnSend);

        tagLayout = (TagLayout) findViewById(R.id.tagLayout);

        String finalCampaignObject = getIntent().getExtras().getString("ChooseRouteObject");

        String component = getIntent().getExtras().getString("Components");

        getCampaignComponents(component, finalCampaignObject);

        setupContactMessage(jsonObjectFinalCampaign);

        buttonSend.setOnClickListener((View v) -> {
            String title = editTextCampaignTitle.getText().toString();
            if (title.isEmpty() || title == null) {
                getCustomDialogue("Enter title", "Enter campaign title.");
            } else {
                createCampaign(jsonObjectFinalCampaign);
            }
        });
    }

    private void getCampaignComponents(String component, String finalCampaignObject) {
        try {
            JSONArray jsonArray = new JSONArray(component);
            for (int i = 0; i < jsonArray.length(); i++) {
                String Type = jsonArray.getJSONObject(i).getString("type");
                switch (Type) {
                    case "IMAGE":
                        ImageObject(jsonArray.getJSONObject(i));
                        break;
                    case "VIDEO":
                        updateVideoObject(jsonArray.getJSONObject(i));
                        break;
                    case "VOICERECORDING":
                        updateAudioObject(jsonArray.getJSONObject(i));
                        break;
                    case "CALLTOACTION":
                        String CallToActionType = jsonArray.getJSONObject(i).getJSONObject("callToAction").getString("type");
                        switch (CallToActionType) {
                            case "POLL":
                                updatePollObject(jsonArray.getJSONObject(i));
                                break;
                            case "FEEDBACK":
                                //updateFeedBackObject(jsonArray.getJSONObject(i));
                                break;
                            case "YESNOMAYBE":
                                //updateCallToAction(jsonArray.getJSONObject(i));
                                break;
                            case "CALLME":
                                //updateCallMe(jsonArray.getJSONObject(i));
                                break;
                        }
                        break;
                }
            }

            JSONArray jsonArray1 = new JSONObject(finalCampaignObject).getJSONArray("routes");
            if (jsonArray1 != null && jsonArray1.length() == 1) {
                String type = jsonArray1.getJSONObject(0).getString("type");
                if (type != null && type.equalsIgnoreCase("EMAIL")) {
                    layoutTextReminder.setVisibility(View.VISIBLE);
                }
            }
            jsonObjectFinalCampaign = new JSONObject(finalCampaignObject);
            jsonObjectFinalCampaign.put("components", jsonArray);
            JSONObject data = (JSONObject) jsonObjectFinalCampaign.getJSONArray("properties").get(0);
            JSONObject Tags = new JSONObject(data.getString("value"));
            setTags(Tags);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateCallMe(JSONObject jsonObject) {
        JSONArray poll = new JSONArray();
        try {
            JSONObject callToAction = jsonObject.getJSONObject("callToAction");
            if (callToAction.getString("type").equalsIgnoreCase("CALLME")) {
                JSONArray properties = jsonObject.getJSONArray("properties");
                for (int i = 0; i < properties.length(); i++) {
                    if (properties.getJSONObject(i).getString("name").equalsIgnoreCase("buttonCorners")) {
                        properties.getJSONObject(i).put("value", properties.getJSONObject(i).getString("value") + "px");
                    }
                }
                callToAction.put("pollItems", poll);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void updateCallToAction(JSONObject jsonObject) {

        JSONArray poll = new JSONArray();
        try {
            JSONObject callToAction = jsonObject.getJSONObject("callToAction");
            if (callToAction.getString("type").equalsIgnoreCase("YESNOMAYBE")) {
                JSONArray properties = jsonObject.getJSONArray("properties");
                for (int i = 0; i < properties.length(); i++) {
                    if (properties.getJSONObject(i).getString("name").equalsIgnoreCase("buttonCorners")) {
                        properties.getJSONObject(i).put("value", properties.getJSONObject(i).getString("value") + "px");
                    }
                }
                callToAction.put("pollItems", poll);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updatePollObject(JSONObject jsonObject) {
        JSONArray poll = new JSONArray();
        try {
            JSONObject callToAction = jsonObject.getJSONObject("callToAction");
            if (callToAction.getString("type").equalsIgnoreCase("POLL")) {
                JSONArray pollItems = callToAction.getJSONArray("pollItems");
                for (int i = 0; i < pollItems.length(); i++) {
                    String item = pollItems.getJSONObject(i).getString("label");
                    if (StringUtils.isNotBlank(item)) {
                        poll.put(new JSONObject().put("label", item));
                    }
                }
                callToAction.put("pollItems", poll);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateFeedBackObject(JSONObject jsonObject) {
        try {
            JSONArray properties = jsonObject.getJSONArray("properties");

            int ElementsSize = properties.getJSONObject(3).getInt("value");

            if (ElementsSize <= 35) {
                properties.getJSONObject(3).put("value", "small");
            }
            if (ElementsSize > 35) {
                properties.getJSONObject(3).put("value", "medium");
            }
            if (ElementsSize > 75) {
                properties.getJSONObject(3).put("value", "large");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateAudioObject(JSONObject jsonObject) {
        File AudioFile;
        try {
            Uri data = Uri.parse(jsonObject.getString("media"));
            try {
                AudioFile = new File(dataHelper.loadAudioFile(data, getBaseContext()));

            } catch (Exception e) {

                AudioFile = new File(jsonObject.getString("media"));

            }
            jsonObject.put("media", encodeAudio(AudioFile));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ImageObject(JSONObject media) {
        try {
            Bitmap bitmap = null;
            Uri data = Uri.parse(media.getString("media"));
            try {
                File actualImage = FileUtil.from(this, data);
                bitmap = Compressor.getDefault(this).compressToBitmap(actualImage);
                //bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data);
                media.put("media", dataHelper.encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateVideoObject(JSONObject media) {
        try {

            Uri selectedVideoUri = Uri.parse(media.getString("media"));

            if (selectedVideoUri != null) {

                // File VideoFile = new File(dataHelper.loadVideoFile(selectedVideoUri, getBaseContext()));

                File VideoFile = null;
                try {
                    VideoFile = FileUtil.from(this, selectedVideoUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (VideoFile.exists() && VideoFile != null) {
                    try {
                        byte[] b = dataHelper.loadFile(VideoFile);
                        media.put("media", Base64.encodeToString(b, Base64.DEFAULT));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("LongLogTag")
    private void setTags(JSONObject obj) {
        LayoutInflater layoutInflater = getLayoutInflater();
        String[] Tags = {"custom", "tags", "interests", "lists"};
        for (String tag : Tags) {
            try {
                JSONArray TagsData = obj.getJSONArray(tag);
                if (TagsData.length() > 0) {
                    for (int j = 0; j < TagsData.length(); j++) {
                        View tagView = layoutInflater.inflate(R.layout.lead_tag_layout, null, false);
                        TextView tagTextView = (TextView) tagView.findViewById(R.id.tagTextView);
                        switch (tag) {
                            case "custom":
                                tagTextView.setBackgroundResource(R.drawable.custom_shape);
                                break;
                            case "lists":
                                tagTextView.setBackgroundResource(R.drawable.lead_shape);
                                break;
                            case "interests":
                                tagTextView.setBackgroundResource(R.drawable.intrest_shape);
                                break;
                            case "tags":
                                tagTextView.setBackgroundResource(R.drawable.tag_shape);
                                break;
                        }
                        tagTextView.setText(String.valueOf(TagsData.get(j)));
                        tagLayout.addView(tagView);
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
            }
        }
        try {
            String all = obj.getString("All");
            if (all.equalsIgnoreCase("ALL")) {
                View tagView = layoutInflater.inflate(R.layout.lead_tag_layout, null, false);
                TextView tagTextView = (TextView) tagView.findViewById(R.id.tagTextView);
                tagTextView.setBackgroundResource(R.drawable.custom_shape);
                tagTextView.setText(String.valueOf("ALL"));
                tagLayout.addView(tagView);
            }
        } catch (Exception e) {
            Log.i(TAG, "All tags not showing");
        }
    }

    // setup contacts message for the camapign contacts
    private void setupContactMessage(JSONObject jsonObjectFinalCampaign) {

        try {

            JSONArray contacts = jsonObjectFinalCampaign.getJSONArray("contacts");

            textViewContacts.setText(String.valueOf(contacts.length()));

            if (contacts.length() > 1) {

                contactstext.setText("Contacts");

            } else {

                contactstext.setText("Contact");
            }

            JSONArray activities = jsonObjectFinalCampaign.getJSONArray("activities");

            textViewMessage.setText(String.valueOf(activities.length()));
            if (activities.length() > 1) {
                messagetext.setText("Messages");
            } else {
                messagetext.setText("Message");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showProgressDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Creating Campaign...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void hideProgressDialog() {
        pDialog.dismiss();
    }

    @SuppressLint("LongLogTag")
    private void createCampaign(JSONObject jsonObjectFinalCampaign) {

        showProgressDialog();

        dateTime = textViewGetDate.getText().toString() + "," + textViewGetTime.getText().toString();

        final String OrganizationId = mySqliteDataBaseHelper.getOrganizationId();

        try {
            jsonObjectFinalCampaign.put("createdOn", System.currentTimeMillis());
            jsonObjectFinalCampaign.put("name", editTextCampaignTitle.getText().toString());
            jsonObjectFinalCampaign.put("credits", textViewMessage.getText().toString());
            jsonObjectFinalCampaign.put("orgId", OrganizationId);
            if (ReminderTextSwitch.isChecked()) {
                jsonObjectFinalCampaign.put("smsRemindersEvery", reminderTime);
            } else {
                jsonObjectFinalCampaign.put("smsRemindersEvery", 0);
            }
            if ((ScheduleMsgSwitch.isChecked())) {
                Log.i(TAG, "Time To Send Campaign" + String.valueOf(myCalendar.getTimeInMillis()));

                jsonObjectFinalCampaign.put("scheduleStartDate", myCalendar.getTimeInMillis());
                jsonObjectFinalCampaign.put("scheduleEndDate", myCalendar.getTimeInMillis());
                jsonObjectFinalCampaign.put("status", "SCHEDULED");
            } else {
                jsonObjectFinalCampaign.put("status", "SENT");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String jsonStr = jsonObjectFinalCampaign.toString();

        String REGISTER_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + OrganizationId + "/campaigns";

        VolleyApi.getInstance().postJsonObject(getBaseContext(), REGISTER_URL, jsonStr, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                hideProgressDialog();
                if (result.success && result.dataIsObject()) {
                    JSONObject response = result.getDataAsObject();
//                    SharedPreferences DASHBOARD = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
//                    String responseData = DASHBOARD.getString(MainActivity.MAIN_DASHBOARD_DATA, null);
//                    SharedPreferences.Editor editor = DASHBOARD.edit();
//                    editor.putString(MainActivity.MAIN_DASHBOARD_DATA, response.toString());
//                    editor.commit();
//                    if (responseData != null) {
//                        try {
//                            JSONObject  recipientsCountObj = new JSONObject(responseData);
//                            recipientsCountObj.put("recipientsCount",)
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }

                    getDashBoard(OrganizationId);

                } else {
                    if (result.volleyError instanceof TimeoutError || result.volleyError instanceof NoConnectionError)
                        ToastShow.setText(getBaseContext(), "Please Check your internet Connection", Toast.LENGTH_LONG);

                    else if (result.volleyError instanceof ServerError)
                        ToastShow.setText(getBaseContext(), "oops something went wrong. try again", Toast.LENGTH_LONG);
                }
            }
        });
    }

    /*
    * Preparing the list data
    */
    private void prepareListData() {
        myCalendar = Calendar.getInstance();
        arrowImage = (ImageView) findViewById(R.id.arrowImage);
        arrowImage2 = (ImageView) findViewById(R.id.arrowImage2);
        textView1 = (TextView) findViewById(R.id.simpletext6);
        myseekbar = (SeekBar) findViewById(R.id.seekbar2);
        ScheduleMsgSwitch = (SwitchCompat) findViewById(R.id.schduleSwitch);
        ReminderTextSwitch = (SwitchCompat) findViewById(R.id.reminderSwitch);
        textViewGetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateCampaignPreviewScreen.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                myCalendar.set(Calendar.YEAR, year);

                                myCalendar.set(Calendar.MONTH, monthOfYear);

                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                scheduleTime = myCalendar.getTimeInMillis();

                                Log.i(TAG, "New Time In Miliseconds" + String.valueOf(scheduleTime));

                                textViewGetDate.setText(DateFormat.format(myCalendar.getTime()));
                            }
                        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        textViewGetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateCampaignPreviewScreen.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);

                                myCalendar.set(Calendar.MINUTE, minute);

                                Log.i(TAG, "New Time In Miliseconds" + String.valueOf(myCalendar.getTimeInMillis()));

                                textViewGetDate.setText(DateFormat.format(myCalendar.getTime()));

                                textViewGetTime.setText(simpleTimeFormat.format(myCalendar.getTime()));
                            }
                        }, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true);
                timePickerDialog.show();

            }
        });

        ScheduleMsg = (RelativeLayout) findViewById(R.id.ScheduleMsg);
        ReminderText = (RelativeLayout) findViewById(R.id.ReminderText);

        ScheduleMsgSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ScheduleMsgSwitch.isChecked()) {
                    ScheduleMsg.setVisibility(View.VISIBLE);
                    arrowImage.setImageResource(R.drawable.down_arrow);
                    //snakebar();
                } else {
                    ScheduleMsg.setVisibility(View.GONE);
                    arrowImage.setImageResource(R.drawable.right_arrow);
                }
            }
        });

        ReminderTextSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ReminderTextSwitch.isChecked()) {
                    ReminderText.setVisibility(View.VISIBLE);
                    arrowImage2.setImageResource(R.drawable.down_arrow);
                } else {
                    ReminderText.setVisibility(View.GONE);
                    arrowImage2.setImageResource(R.drawable.right_arrow);
                }
            }
        });

        myseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                reminderTime = progress;
                switch (progress) {
                    case 0:
                        textView1.setText("");
                    case 1:
                        textView1.setText(" " + progress + " Hour");
                        break;
                    case 24:
                        textView1.setText(" " + 1 + " Day");
                        break;
                    case 25:
                        textView1.setText(" " + 2 + " Days");
                        break;
                    case 26:
                        textView1.setText(" " + 3 + " Days");
                        break;
                    case 27:
                        textView1.setText(" " + 4 + " Days");
                        break;
                    case 28:
                    case 29:
                        textView1.setText(" " + 5 + " Days");
                        break;
                    default:
                        textView1.setText(" " + progress + " Hours");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public String getTypeOfDayWithSwitchStatement(String dayOfWeekArg) {
        String typeOfDay;
        switch (dayOfWeekArg) {
            case "Monday":
                typeOfDay = "Start of work week";
                break;
            case "Tuesday":
            case "Wednesday":
            case "Thursday":
                typeOfDay = "Midweek";
                break;
            case "Friday":
                typeOfDay = "End of work week";
                break;
            case "Saturday":
            case "Sunday":
                typeOfDay = "Weekend";
                break;
            default:
                throw new IllegalArgumentException("Invalid day of the week: " + dayOfWeekArg);
        }
        return typeOfDay;
    }

    public void getCustomDialogue(String AlertTitle, String AlertMessage) {

        dialogue_custom = new Dialog(CreateCampaignPreviewScreen.this);

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

    @SuppressLint("NewApi")
    private String encodeAudio(File audioFile) {
        String _audioBase64 = null;
        byte[] audioBytes;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(audioFile);
            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);
            audioBytes = baos.toByteArray();
            // Here goes the Base64 string
            _audioBase64 = Base64.encodeToString(audioBytes, Base64.DEFAULT);
        } catch (Exception e) {
            Log.e(TAG, " Unable to Handle audio file ");
        }
        return _audioBase64;
    }

    // get dashboard data
    private void getDashBoard(String getOrgId) {

        String REGISTER_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + getOrgId + "/dashboard/info";

        VolleyApi.getInstance().getJsonObject(getBaseContext(), REGISTER_URL, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                if (result.success && result.dataIsObject()) {
                    JSONObject response = result.getDataAsObject();
                    //Log.i(TAG, response.toString());

                    //getting start profile data.
                    SharedPreferences DASHBOARD = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = DASHBOARD.edit();
                    editor.putString(MainActivity.MAIN_DASHBOARD_DATA, response.toString());
                    editor.commit();

                    ToastShow.setText(getApplicationContext(), "Great! You have Successfully Created the Campaign.", Toast.LENGTH_SHORT);
                    Intent intent = new Intent(getBaseContext(), Dashboard.class);
                    startActivity(intent);
                    hideProgressDialog();
                    finish();
                }
            }
        });
    }
}
