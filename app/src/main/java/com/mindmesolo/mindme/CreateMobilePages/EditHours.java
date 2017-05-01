package com.mindmesolo.mindme.CreateMobilePages;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.mindmesolo.mindme.CreateMobilePages.models.BusinessHours;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by pc-14 on 3/2/2017.
 */

public class EditHours extends AppCompatActivity {

    Calendar calendar;
    SqliteDataBaseHelper dBhelper;
    TimePickerDialog timepickerdialog;
    SwitchCompat dayCloseSwitch, AppointmentStatus;
    int currentday;
    String currentdayTitle;
    int openhour = 9, openmin = 0, closehour = 5, closemin = 0;
    String format;
    boolean byAppointment, isDayClose;
    boolean IsChanged;
    TextView opening_time, Close_time, status_opening_time, Status_closing_time, Appointment, daytitle;
    ArrayList<BusinessHours> TempListItems = new ArrayList<>();
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

        TempListItems = mIntent.getParcelableArrayListExtra("day_List");

        currentdayTitle = mIntent.getStringExtra("current_title");

        if (currentdayTitle != null) {
            daytitle.setText(currentdayTitle);
        }
        setupDayData(TempListItems);
    }

    @Override
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
        getMenuInflater().inflate(R.menu.done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.done) {
            BusinessHours BH = TempListItems.get(currentday);
            BH.setOpenOnHour(openhour);
            BH.setOpenOnMinute(openmin);
            BH.setCloseOnHour(closehour);
            BH.setCloseOnMinute(closemin);
            BH.setByAppointmentOnly(AppointmentStatus.isChecked());
            BH.setClosed(dayCloseSwitch.isChecked());

            Intent returnIntent = new Intent();
            returnIntent.putParcelableArrayListExtra("get_BusinessHours", TempListItems);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDayData(ArrayList<BusinessHours> currentDayData) {

        if (currentDayData.size() > 0) {

            BusinessHours BH = currentDayData.get(currentday);

            openhour = BH.getOpenOnHour();

            openmin = BH.getOpenOnMinute();

            closehour = BH.getCloseOnHour();

            closemin = BH.getCloseOnMinute();

            byAppointment = BH.getByAppointmentOnly();

            isDayClose = BH.getClosed();

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
                    openhour)
                    + ":" +
                    String.format("%02d",
                            openmin)
                    + " AM");

            Status_closing_time.setText(String.format("%02d",
                    closehour)
                    + ":" +
                    String.format("%02d",
                            closemin)
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
        timepickerdialog = new TimePickerDialog(EditHours.this,
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
                        openhour = OpenhourOfDay;
                        openmin = Openminute;
                        status_opening_time.setText(String.format("%02d", OpenhourOfDay) + ":" + String.format("%02d", Openminute) + " " + format);
                    }
                }, CalendarHour, CalendarMinute, false);
        timepickerdialog.show();
    }

    public void SetupClosingTime() {
        calendar = Calendar.getInstance();
        CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
        CalendarMinute = calendar.get(Calendar.MINUTE);
        timepickerdialog = new TimePickerDialog(EditHours.this,
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
                        closehour = CloasehourOfDay;
                        closemin = Closeminute;
                        Status_closing_time.setText(String.format("%02d", CloasehourOfDay) + ":" + String.format("%02d", Closeminute) + " " + format);
                    }
                }, CalendarHour, CalendarMinute, false);
        timepickerdialog.show();
    }
}
