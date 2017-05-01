package com.mindmesolo.mindme.GettingStarted;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.OrganizationModel;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.ToastShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by eNest on 5/19/2016.
 */
public class BusinessHoursInnerSettings extends AppCompatActivity {

    public static final String PREFS_NAME = "WORKING_DAY_PREFS";
    private static final String TAG = "BHInnerSettings";
    Calendar calendar;

    SqliteDataBaseHelper dBhelper;

    TimePickerDialog timepickerdialog;

    SwitchCompat dayCloseSwitch, AppointmentStatus;

    int currentday;

    String currentdayTitle;

    String openhour = "9", openmin = "0", closehour = "5", closemin = "0", format;

    boolean byAppointment, isDayClose;

    boolean IsChanged;

    TextView opening_time, Close_time, status_opening_time, Status_closing_time, Appointment, daytitle;

    ProgressDialog pDialog;
    private int CalendarHour, CalendarMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.business_hour_inner_settings);

        dBhelper = new SqliteDataBaseHelper(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        daytitle = (TextView) findViewById(R.id.daytitle);

        opening_time = (TextView) findViewById(R.id.opening_time);

        Close_time = (TextView) findViewById(R.id.Close_time);

        status_opening_time = (TextView) findViewById(R.id.status_opening_time);

        Status_closing_time = (TextView) findViewById(R.id.Status_closing_time);

        Appointment = (TextView) findViewById(R.id.Appointment);

        dayCloseSwitch = (SwitchCompat) findViewById(R.id.close_switch);

        AppointmentStatus = (SwitchCompat) findViewById(R.id.AppointmentStatus);

        Intent mIntent = getIntent();

        currentday = mIntent.getIntExtra("current_day", 0);

        currentdayTitle = mIntent.getStringExtra("current_title");

        if (currentdayTitle != null) {
            daytitle.setText(currentdayTitle);
        }
        setupDayData();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.greeting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            updateOrganization();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDayData() {

        ArrayList currentDayData = dBhelper.getBusinessSingleDay(currentday);

        if (currentDayData.size() > 0) {

            openhour = currentDayData.get(1).toString();

            openmin = currentDayData.get(2).toString();

            closehour = currentDayData.get(3).toString();

            closemin = currentDayData.get(4).toString();

            byAppointment = Boolean.parseBoolean(currentDayData.get(5).toString());

            isDayClose = Boolean.parseBoolean(currentDayData.get(6).toString());

            dayCloseSwitch.setChecked(isDayClose);

            AppointmentStatus.setChecked(byAppointment);

            if (dayCloseSwitch.isChecked()) {
                AppointmentStatus.setEnabled(false);
                Status_closing_time.setEnabled(false);
                status_opening_time.setEnabled(false);

            } else if (AppointmentStatus.isChecked()) {
                dayCloseSwitch.setEnabled(false);
                Status_closing_time.setEnabled(false);
                status_opening_time.setEnabled(false);
            } else {
                dayCloseSwitch.setEnabled(true);
                AppointmentStatus.setEnabled(true);
                Status_closing_time.setEnabled(true);
                status_opening_time.setEnabled(true);
            }

            status_opening_time.setText(String.format("%02d",
                    Integer.parseInt(openhour))
                    + ":" +
                    String.format("%02d",
                            Integer.parseInt(openmin))
                    + " AM");

            Status_closing_time.setText(String.format("%02d",
                    Integer.parseInt(closehour))
                    + ":" +
                    String.format("%02d",
                            Integer.parseInt(closemin))
                    + " PM");
        } else {
            AppointmentStatus.setEnabled(false);
            Status_closing_time.setEnabled(false);
            status_opening_time.setEnabled(false);
        }

        status_opening_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetupOpeningTime();
            }
        });

        Status_closing_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetupClosingTime();
            }
        });

        dayCloseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    IsChanged = true;
                    dayCloseSwitch.setChecked(true);
                    AppointmentStatus.setEnabled(false);
                    Status_closing_time.setEnabled(false);
                    status_opening_time.setEnabled(false);
                } else {
                    IsChanged = true;
                    dayCloseSwitch.setChecked(false);
                    AppointmentStatus.setEnabled(true);
                    Status_closing_time.setEnabled(true);
                    status_opening_time.setEnabled(true);
                }
            }
        });


        AppointmentStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    IsChanged = true;
                    AppointmentStatus.setChecked(true);
                    dayCloseSwitch.setEnabled(false);
                    Status_closing_time.setEnabled(false);
                    status_opening_time.setEnabled(false);
                } else if (!isChecked) {
                    IsChanged = true;
                    AppointmentStatus.setChecked(false);
                    dayCloseSwitch.setEnabled(true);
                    Status_closing_time.setEnabled(true);
                    status_opening_time.setEnabled(true);
                }
            }
        });


    }

    public void SetupOpeningTime() {
        calendar = Calendar.getInstance();
        CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
        CalendarMinute = calendar.get(Calendar.MINUTE);
        timepickerdialog = new TimePickerDialog(BusinessHoursInnerSettings.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int OpenhourOfDay, int Openminute) {
                        if (OpenhourOfDay == 0) {
                            OpenhourOfDay += 12;
                            format = "AM";
                        } else if (OpenhourOfDay == 12) {
                            format = "PM";
                        } else if (OpenhourOfDay > 12) {
                            OpenhourOfDay -= 12;
                            format = "PM";
                        } else {
                            format = "AM";
                        }

                        IsChanged = true;
                        openhour = String.valueOf(OpenhourOfDay);
                        openmin = String.valueOf(Openminute);
                        status_opening_time.setText(String.format("%02d", OpenhourOfDay) + ":" + String.format("%02d", Openminute) + " " + format);
                    }
                }, CalendarHour, CalendarMinute, false);
        timepickerdialog.show();
    }

    public void SetupClosingTime() {
        calendar = Calendar.getInstance();
        CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
        CalendarMinute = calendar.get(Calendar.MINUTE);
        timepickerdialog = new TimePickerDialog(BusinessHoursInnerSettings.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int CloasehourOfDay,
                                          int Closeminute) {
                        if (CloasehourOfDay == 0) {
                            CloasehourOfDay += 12;
                            format = "AM";
                        } else if (CloasehourOfDay == 12) {
                            format = "PM";
                        } else if (CloasehourOfDay > 12) {
                            CloasehourOfDay -= 12;
                            format = "PM";
                        } else {
                            format = "AM";
                        }
                        String.format("%02d", Closeminute);
                        IsChanged = true;
                        closehour = String.valueOf(CloasehourOfDay);
                        closemin = String.valueOf(Closeminute);
                        Status_closing_time.setText(String.format("%02d", CloasehourOfDay) + ":" + String.format("%02d", Closeminute) + " " + format);
                    }
                }, CalendarHour, CalendarMinute, false);
        timepickerdialog.show();

    }

    // Main method for updating te social media
    public void updateOrganization() {
        startDialog("Updating working days..");
        try {

            final String jsonbodydata = getWorkingMediaObject();


            Log.i(TAG, jsonbodydata);

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, OrganizationModel.getApiBaseUrl() + dBhelper.getOrganizationId(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i(TAG, "Organization Update Success");
                            //prepareSocialUiComponents();
                            try {
                                String GettingStartData = response.getJSONArray("properties").getJSONObject(0).getString("value");
                                SharedPreferences DASHBOARD = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = DASHBOARD.edit();
                                editor.putString(MainActivity.PREFS_KEY_GETTING_START, GettingStartData);
                                editor.commit();
                                dBhelper.DeleteBusiness();
                                dBhelper.insertBusinessHours(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e(TAG, e.toString());
                            }
                            pDialog.dismiss();
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(TAG, error.toString());

                            pDialog.dismiss();

                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                ToastShow.setText(getBaseContext(), "Please Check your internet Connection", Toast.LENGTH_LONG);
                            } else {
                                ToastShow.setText(getBaseContext(), "oops something went wrong. try again", Toast.LENGTH_LONG);
                            }
                            String json = null;
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                switch (response.statusCode) {
                                    case 400:
                                        DataHelper mydatahelper = new DataHelper();
                                        json = new String(response.data);
                                        json = mydatahelper.trimMessage(json, "message");
                                        if (json != null) mydatahelper.displayMessage(json);
                                        break;
                                }
                                //Additional cases
                            }
                        }
                    }) {

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() {
                    try {
                        return jsonbodydata == null ? null : jsonbodydata.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                jsonbodydata, "utf-8");
                        return null;
                    }
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", "Basic " + GetApiAccess());
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    9000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
        }
    }

    private String getWorkingMediaObject() {
        JSONObject finalJsonObject = new JSONObject();
        JSONObject hoursOfOperation = new JSONObject();
        JSONArray includeds = new JSONArray();
        JSONArray excludeds = new JSONArray();
        if (dBhelper.getWorkingDaysCount() > 0) {
            for (int i = 0; i <= 7; i++) {
                if (i == currentday && IsChanged == true) {
                    if (dayCloseSwitch.isChecked() == true) {
                        excludeds.put(getCurrentDayObject());
                    } else {
                        includeds.put(getCurrentDayObject());
                    }
                } else {
                    ArrayList currentDayList = dBhelper.getBusinessSingleDay(i);
                    if (currentDayList.size() > 0) {
                        try {
                            JSONObject businesshours = new JSONObject();
                            businesshours.put("dayOfWeek", currentDayList.get(0).toString());
                            businesshours.put("openOnHour", currentDayList.get(1).toString());
                            businesshours.put("openOnMinute", currentDayList.get(2).toString());
                            businesshours.put("closeOnHour", currentDayList.get(3).toString());
                            businesshours.put("closeOnMinute", currentDayList.get(4).toString());
                            businesshours.put("byAppointmentOnly", currentDayList.get(5).toString());
                            if (Boolean.parseBoolean(currentDayList.get(6).toString()) == true) {
                                excludeds.put(businesshours);
                            } else {
                                includeds.put(businesshours);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            if (dayCloseSwitch.isChecked() == true) {
                excludeds.put(getCurrentDayObject());
            } else {
                includeds.put(getCurrentDayObject());
            }
        }
        try {
            finalJsonObject.put("hoursOfOperation", hoursOfOperation
                    .put("includeds", includeds)
                    .put("excludeds", excludeds));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (IsChanged == true) {
            try {
                finalJsonObject.put("properties",
                        new JSONArray().put(
                                new JSONObject().put("name", "Greeting Started")
                                        .put("value", getPropertiesObject())));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return finalJsonObject.toString();
    }

    JSONObject getCurrentDayObject() {
        JSONObject businesshours = new JSONObject();
        try {
            businesshours.put("dayOfWeek", currentday);
            businesshours.put("openOnHour", openhour);
            businesshours.put("openOnMinute", openmin);
            businesshours.put("closeOnHour", closehour);
            businesshours.put("closeOnMinute", closemin);
            businesshours.put("byAppointmentOnly", getAppointmentSwitch(AppointmentStatus));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return businesshours;
    }

    private boolean getAppointmentSwitch(SwitchCompat sw) {
        if (sw.isChecked())
            return true;
        else return false;
    }

    private String getPropertiesObject() {

        SharedPreferences DASHBOARD = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);

        String GettingStartData = DASHBOARD.getString(MainActivity.PREFS_KEY_GETTING_START, null);

        StringBuilder stringBuilder;

        if (GettingStartData != null) {
            stringBuilder = new StringBuilder(GettingStartData);
            if (!GettingStartData.contains("Business Hours")) {
                stringBuilder.append("," + "Business Hours");
            }
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Business Hours");
        }

        return String.valueOf(stringBuilder);
    }

    // progress dialog
    private void startDialog(String Message) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(true);
        pDialog.setMessage(Message);
        pDialog.show();
    }

    //api access token
    public String GetApiAccess() {
        SharedPreferences pref1 = getBaseContext().getSharedPreferences("UserLogin", Context.MODE_PRIVATE);
        String email = pref1.getString("Email", null);
        String password = pref1.getString("Password", null);
        String token = email + ":" + password;
        byte[] data = null;
        try {
            data = token.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        final String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        final String finalToken = base64.replace("\n", "");
        return finalToken;
    }

}
