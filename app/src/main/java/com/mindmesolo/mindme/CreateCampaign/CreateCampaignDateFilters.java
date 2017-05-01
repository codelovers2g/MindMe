
package com.mindmesolo.mindme.CreateCampaign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.ToastShow;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * dd/mm/yyyy
 * Created by srn on 21/4/2016.
 */
public class CreateCampaignDateFilters extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CreateCampaignDateFilters";

    static String setStartTime;

    ToggleButton ContactCreated, leadcreated, trailcreated, customercreated, birthday;

    ToggleButton last, next, This, custom, saven_dayz, fourty_dayz;

    ToggleButton thirty_dayz, sixty_dayz, week, month, quarter, year;

    Button ButtonApplyFilter, ButtonRemoveFilter;

    RelativeLayout DateFilterLayout, ChildLayout, RelativeLayout, CustomFilter;

    TextView from_date, to_date, DisplayFiltersStatus, SelectedContactsStatus;

    String ButtonContactType, ButtonDateType;

    SqliteDataBaseHelper dBhelper = new SqliteDataBaseHelper(this);

    ArrayList SELECTED_CONTACTS_DATA = new ArrayList();

    ArrayList FinalContactsList = new ArrayList();

    DateFormat DateYearFormat = new SimpleDateFormat("MM/dd/yyyy");

    DateFormat DateMonthFormat = new SimpleDateFormat("MMM dd");

    Calendar calendarStartTime = Calendar.getInstance();

    Calendar calendarEndTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_campaign_date_filter);

        addButtonListener();

        Intent intent = getIntent();

        SELECTED_CONTACTS_DATA = intent.getCharSequenceArrayListExtra("myfilterContacts");

        ArrayList FilterStatus = intent.getCharSequenceArrayListExtra("myfilterstats");

        if (SELECTED_CONTACTS_DATA != null) {
            String count = String.valueOf(SELECTED_CONTACTS_DATA.size());
            SelectedContactsStatus.setText(count);
        }

        //-----get the filter status check filter is apply or not--if yes then handle the ButtonContactType state--//
        if (FilterStatus.size() > 0) {
            DisplayFiltersStatus.setText("Date filter is done, To change click on remove filter");
            DisplayFiltersStatus.setTextColor(Color.RED);
            ButtonApplyFilter.setVisibility(View.GONE);
            ButtonRemoveFilter.setVisibility(View.VISIBLE);
            ContactCreated.setEnabled(false);
            birthday.setEnabled(false);
        }
    }

    public void addButtonListener() {

        CustomFilter = (RelativeLayout) findViewById(R.id.customfilters);

        DateFilterLayout = (RelativeLayout) findViewById(R.id.datefilters);

        ChildLayout = (RelativeLayout) findViewById(R.id.childlayout);

        RelativeLayout = (RelativeLayout) findViewById(R.id.childlayout);

        SelectedContactsStatus = (TextView) findViewById(R.id.textView3);

        DisplayFiltersStatus = (TextView) findViewById(R.id.finalstatus);

        from_date = (TextView) findViewById(R.id.from_date);
        from_date.setOnClickListener(this);

        to_date = (TextView) findViewById(R.id.to_date);
        to_date.setOnClickListener(this);

        ContactCreated = (ToggleButton) findViewById(R.id.contactcreated);
        ContactCreated.setOnClickListener(this);

        leadcreated = (ToggleButton) findViewById(R.id.leadcreated);
        leadcreated.setOnClickListener(this);

        trailcreated = (ToggleButton) findViewById(R.id.trailcreated);
        trailcreated.setOnClickListener(this);

        customercreated = (ToggleButton) findViewById(R.id.customercreated);
        customercreated.setOnClickListener(this);

        birthday = (ToggleButton) findViewById(R.id.birthday);
        birthday.setOnClickListener(this);

        last = (ToggleButton) findViewById(R.id.last);
        last.setOnClickListener(this);

        next = (ToggleButton) findViewById(R.id.next);
        next.setOnClickListener(this);

        This = (ToggleButton) findViewById(R.id.This);
        This.setOnClickListener(this);

        custom = (ToggleButton) findViewById(R.id.custom);
        custom.setOnClickListener(this);

        saven_dayz = (ToggleButton) findViewById(R.id.saven_dayz);
        saven_dayz.setOnClickListener(this);

        fourty_dayz = (ToggleButton) findViewById(R.id.fourty_dayz);
        fourty_dayz.setOnClickListener(this);

        thirty_dayz = (ToggleButton) findViewById(R.id.thirty_dayz);
        thirty_dayz.setOnClickListener(this);

        sixty_dayz = (ToggleButton) findViewById(R.id.sixty_dayz);
        sixty_dayz.setOnClickListener(this);

        week = (ToggleButton) findViewById(R.id.week);
        week.setOnClickListener(this);

        month = (ToggleButton) findViewById(R.id.month);
        month.setOnClickListener(this);

        quarter = (ToggleButton) findViewById(R.id.quarter);
        quarter.setOnClickListener(this);

        year = (ToggleButton) findViewById(R.id.year);
        year.setOnClickListener(this);

        ButtonApplyFilter = (Button) findViewById(R.id.applyfilter);
        ButtonApplyFilter.setOnClickListener(this);
        ButtonApplyFilter.setVisibility(View.GONE);

        ButtonRemoveFilter = (Button) findViewById(R.id.removefilter);
        ButtonRemoveFilter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contactcreated:
                birthday.setChecked(false);

                next.setEnabled(false);
                next.setChecked(false);

                ContactCreated.setChecked(true);

                ButtonContactType = "createdOn";
                DisplayFiltersStatus.setText("");

                ButtonApplyFilter.setVisibility(View.GONE);
                DateFilterLayout.setVisibility(View.VISIBLE);

                ClearCheckedItems();
                ClearLastNextThisCustom();
                disableLayoutElements();
                break;

            case R.id.birthday:
                birthday.setChecked(true);
                ContactCreated.setChecked(false);
                next.setEnabled(true);
                DateFilterLayout.setVisibility(View.VISIBLE);
                ButtonApplyFilter.setVisibility(View.GONE);
                year.setEnabled(false);
                ButtonContactType = "dateOfBirth";
                DisplayFiltersStatus.setText("");
                ClearCheckedItems();
                ClearLastNextThisCustom();
                disableLayoutElements();
                break;

            case R.id.last:
                last.setChecked(true);
                next.setChecked(false);
                This.setChecked(false);
                custom.setChecked(false);
                ButtonApplyFilter.setVisibility(View.GONE);
                enableLayoutElements();
                ClearCheckedItems();
                if (ButtonContactType.equals("dateOfBirth")) {
                    year.setEnabled(false);
                }
                CustomFilter.setVisibility(View.GONE);
                ButtonDateType = "Last";
                DisplayFiltersStatus.setText("");

                break;

            case R.id.next:
                next.setChecked(true);
                last.setChecked(false);
                This.setChecked(false);
                custom.setChecked(false);
                ButtonDateType = "Next";
                CustomFilter.setVisibility(View.GONE);
                ButtonApplyFilter.setVisibility(View.GONE);
                DisplayFiltersStatus.setText("");
                enableLayoutElements();
                ClearCheckedItems();
                if (ButtonContactType.equals("dateOfBirth")) {
                    year.setEnabled(false);
                }
                break;

            case R.id.This:
                This.setChecked(true);
                last.setChecked(false);
                next.setChecked(false);
                custom.setChecked(false);
                enableLayoutElements();
                ClearCheckedItems();
                saven_dayz.setEnabled(false);
                fourty_dayz.setEnabled(false);
                thirty_dayz.setEnabled(false);
                sixty_dayz.setEnabled(false);
                CustomFilter.setVisibility(View.GONE);
                ButtonApplyFilter.setVisibility(View.GONE);
                ButtonDateType = "This";
                DisplayFiltersStatus.setText("");
                if (ButtonContactType.equals("dateOfBirth")) {
                    year.setEnabled(false);
                }

                break;
            case R.id.custom:
                custom.setChecked(true);
                last.setChecked(false);
                next.setChecked(false);
                This.setChecked(false);
                CustomFilter.setVisibility(View.VISIBLE);
                ButtonApplyFilter.setVisibility(View.GONE);
                DisplayFiltersStatus.setText("");
                disableLayoutElements();
                ClearCheckedItems();
                break;
            case R.id.saven_dayz:
                saven_dayz.setChecked(true);
                week.setChecked(false);
                month.setChecked(false);
                quarter.setChecked(false);
                year.setChecked(false);
                sixty_dayz.setChecked(false);
                thirty_dayz.setChecked(false);
                fourty_dayz.setChecked(false);
                getContactsAccordingToDays(7);
                break;
            case R.id.fourty_dayz:
                fourty_dayz.setChecked(true);
                saven_dayz.setChecked(false);
                thirty_dayz.setChecked(false);
                sixty_dayz.setChecked(false);
                week.setChecked(false);
                month.setChecked(false);
                quarter.setChecked(false);
                year.setChecked(false);
                getContactsAccordingToDays(14);
                break;
            case R.id.thirty_dayz:
                thirty_dayz.setChecked(true);
                saven_dayz.setChecked(false);
                fourty_dayz.setChecked(false);
                sixty_dayz.setChecked(false);
                week.setChecked(false);
                month.setChecked(false);
                quarter.setChecked(false);
                year.setChecked(false);
                getContactsAccordingToDays(30);
                break;
            case R.id.sixty_dayz:
                sixty_dayz.setChecked(true);
                saven_dayz.setChecked(false);
                fourty_dayz.setChecked(false);
                thirty_dayz.setChecked(false);
                week.setChecked(false);
                month.setChecked(false);
                quarter.setChecked(false);
                year.setChecked(false);
                getContactsAccordingToDays(60);
                break;
            case R.id.week:
                week.setChecked(true);
                month.setChecked(false);
                quarter.setChecked(false);
                year.setChecked(false);
                saven_dayz.setChecked(false);
                fourty_dayz.setChecked(false);
                thirty_dayz.setChecked(false);
                sixty_dayz.setChecked(false);
                getContactsAccordingToWeeks();
                break;
            case R.id.month:
                month.setChecked(true);
                week.setChecked(false);
                quarter.setChecked(false);
                year.setChecked(false);
                saven_dayz.setChecked(false);
                fourty_dayz.setChecked(false);
                thirty_dayz.setChecked(false);
                sixty_dayz.setChecked(false);
                getContactsAccordingToMonth();
                break;
            case R.id.quarter:
                quarter.setChecked(true);
                week.setChecked(false);
                month.setChecked(false);
                year.setChecked(false);
                saven_dayz.setChecked(false);
                fourty_dayz.setChecked(false);
                thirty_dayz.setChecked(false);
                sixty_dayz.setChecked(false);
                getContactsAccordingToQuarter(getCurrentQuarter());
                break;
            case R.id.year:
                year.setChecked(true);
                week.setChecked(false);
                month.setChecked(false);
                quarter.setChecked(false);
                saven_dayz.setChecked(false);
                fourty_dayz.setChecked(false);
                thirty_dayz.setChecked(false);
                sixty_dayz.setChecked(false);
                getContactsAccordingToYear();
                break;
            case R.id.from_date:
                datePickerSelectFromDate();
                break;
            case R.id.to_date:
                datePickerSelectToDate();
                break;
            case R.id.applyfilter:
                try {
                    if (FinalContactsList.size() > 0) {
                        Intent returnIntent = new Intent();
                        returnIntent.putCharSequenceArrayListExtra("result", FinalContactsList);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } else {
                        ConfirmAlert();
                    }
                } catch (Exception e) {
                    ToastShow.setText(this, "No data for filter", Toast.LENGTH_SHORT);
                }
                break;
            case R.id.removefilter:
                ContactCreated.setEnabled(true);
                birthday.setEnabled(true);
                FinalContactsList = new ArrayList();
                Intent returnIntent = new Intent();
                returnIntent.putCharSequenceArrayListExtra("result", FinalContactsList);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            default:
                break;
        }
    }
    //------------Main methods------------------//

    //Enable layout elements when any button selected
    private void enableLayoutElements() {
        for (int i = 0; i < ChildLayout.getChildCount(); i++) {
            View view = ChildLayout.getChildAt(i);
            view.getId();
            view.setEnabled(true);
        }
    }

    //Disable layout elements when no button selected
    private void disableLayoutElements() {
        for (int i = 0; i < ChildLayout.getChildCount(); i++) {
            View view = ChildLayout.getChildAt(i);
            view.setEnabled(false);
        }
    }

    //Clear all checked items
    private void ClearCheckedItems() {
        saven_dayz.setChecked(false);
        sixty_dayz.setChecked(false);
        thirty_dayz.setChecked(false);
        fourty_dayz.setChecked(false);
        week.setChecked(false);
        month.setChecked(false);
        quarter.setChecked(false);
        year.setChecked(false);
    }

    //Clear selected checked items
    private void ClearLastNextThisCustom() {
        last.setChecked(false);
        next.setChecked(false);
        This.setChecked(false);
        custom.setChecked(false);
    }

    private void datePickerSelectFromDate() {
        calendarStartTime = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        calendarStartTime.set(Calendar.YEAR, year);
                        calendarStartTime.set(Calendar.MONTH, monthOfYear);
                        calendarStartTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        setStartTime = String.valueOf(DateYearFormat.format(calendarStartTime.getTime()));
                        from_date.setText(setStartTime);
                    }
                }, calendarStartTime.get(Calendar.YEAR), calendarStartTime.get(Calendar.MONTH), calendarStartTime.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void datePickerSelectToDate() {
        calendarEndTime = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        calendarEndTime.set(Calendar.YEAR, year);
                        calendarEndTime.set(Calendar.MONTH, monthOfYear);
                        calendarEndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        setStartTime = String.valueOf(DateYearFormat.format(calendarEndTime.getTime()));
                        to_date.setText(setStartTime);
                        displayContactsStatus(2);
                    }

                }, calendarEndTime.get(Calendar.YEAR), calendarEndTime.get(Calendar.MONTH), calendarEndTime.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    // get Contacts according to days of year
    private void getContactsAccordingToDays(int DaysCount) {
        calendarStartTime = Calendar.getInstance();
        calendarEndTime = Calendar.getInstance();
        switch (ButtonDateType) {
            case "Last":
                calendarStartTime.set(Calendar.DAY_OF_YEAR, calendarStartTime.get(Calendar.DAY_OF_YEAR) - DaysCount);
                break;
            case "Next":
                calendarEndTime.set(Calendar.DAY_OF_YEAR, calendarStartTime.get(Calendar.DAY_OF_YEAR) + DaysCount);
                break;
        }
        displayContactsStatus(1);
    }

    // get Contacts according to weeks  i.e  this week, next week,last week
    private void getContactsAccordingToWeeks() {
        calendarStartTime = Calendar.getInstance();
        calendarEndTime = Calendar.getInstance();
        switch (ButtonDateType) {
            case "Last":
                calendarStartTime.set(Calendar.WEEK_OF_MONTH, calendarStartTime.get(Calendar.WEEK_OF_MONTH) - 1);
                calendarStartTime.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

                calendarEndTime.set(Calendar.WEEK_OF_MONTH, calendarEndTime.get(Calendar.WEEK_OF_MONTH) - 1);
                calendarEndTime.set(Calendar.DAY_OF_YEAR, calendarStartTime.get(Calendar.DAY_OF_YEAR) + 6);
                break;
            case "Next":
                calendarStartTime.set(Calendar.WEEK_OF_MONTH, calendarStartTime.get(Calendar.WEEK_OF_MONTH) + 1);
                calendarStartTime.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

                calendarEndTime.set(Calendar.WEEK_OF_MONTH, calendarEndTime.get(Calendar.WEEK_OF_MONTH) + 1);
                calendarEndTime.set(Calendar.DAY_OF_YEAR, calendarStartTime.get(Calendar.DAY_OF_YEAR) + 6);
                break;
            case "This":
                calendarStartTime.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                calendarEndTime.set(Calendar.DAY_OF_YEAR, calendarStartTime.get(Calendar.DAY_OF_YEAR) + 6);
                break;
        }
        displayContactsStatus(1);
    }

    private void getContactsAccordingToMonth() {
        calendarStartTime = Calendar.getInstance();
        calendarEndTime = Calendar.getInstance();
        switch (ButtonDateType) {
            case "Last":
                calendarStartTime.set(Calendar.MONTH, calendarStartTime.get(Calendar.MONTH) - 1);
                calendarStartTime.set(Calendar.DAY_OF_MONTH, 1);

                calendarEndTime.set(Calendar.MONTH, calendarEndTime.get(Calendar.MONTH) - 1);
                calendarEndTime.set(Calendar.DAY_OF_MONTH, calendarEndTime.getActualMaximum(Calendar.DAY_OF_MONTH));

                break;
            case "Next":
                calendarStartTime.set(Calendar.MONTH, calendarStartTime.get(Calendar.MONTH) + 1);
                calendarStartTime.set(Calendar.DAY_OF_MONTH, 1);

                calendarEndTime.set(Calendar.MONTH, calendarEndTime.get(Calendar.MONTH) + 1);
                calendarEndTime.set(Calendar.DAY_OF_MONTH, calendarEndTime.getActualMaximum(Calendar.DAY_OF_MONTH));
                break;
            case "This":
                calendarStartTime.set(Calendar.DAY_OF_MONTH, 1);
                calendarEndTime.set(Calendar.DAY_OF_MONTH, calendarEndTime.getActualMaximum(Calendar.DAY_OF_MONTH));
                break;
        }
        displayContactsStatus(1);
    }

    @SuppressLint("LongLogTag")
    private void getContactsAccordingToQuarter(int currentRunningQuarter) {
        /*currentRunningQuarter*/

        Log.i(TAG, "Current Quarter before filter: " + String.valueOf(currentRunningQuarter));

        switch (ButtonDateType) {
            case "Last":
                // check current month of the date
                if (calendarStartTime.get(Calendar.MONTH) <= 1) {
                    calendarStartTime.set(Calendar.YEAR, Calendar.YEAR - 1);
                    calendarEndTime.set(Calendar.YEAR, Calendar.YEAR - 1);
                    currentRunningQuarter = 4;
                } else {
                    currentRunningQuarter--;
                }
                break;
            case "Next":
                // if month = October or >
                if (calendarStartTime.get(Calendar.MONTH) > 9) {
                    calendarStartTime.set(Calendar.YEAR, Calendar.YEAR + 1);
                    calendarEndTime.set(Calendar.YEAR, Calendar.YEAR + 1);
                    currentRunningQuarter = 1;
                } else {
                    currentRunningQuarter++;
                }
                break;
        }

        Log.i(TAG, "Current Quarter after filter: " + String.valueOf(currentRunningQuarter));
        //Now I is the accurate quarter .
        switch (currentRunningQuarter) {
            case 1:
                calendarStartTime.set(Calendar.MONTH, Calendar.JANUARY);
                calendarStartTime.set(Calendar.DAY_OF_MONTH, 1);

                calendarEndTime.set(Calendar.MONTH, Calendar.MARCH);
                calendarEndTime.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 2:
                calendarStartTime.set(Calendar.MONTH, Calendar.APRIL);
                calendarStartTime.set(Calendar.DAY_OF_MONTH, 1);

                calendarEndTime.set(Calendar.MONTH, Calendar.JUNE);
                calendarEndTime.set(Calendar.DAY_OF_MONTH, 30);
                break;
            case 3:
                calendarStartTime.set(Calendar.MONTH, Calendar.JULY);
                calendarStartTime.set(Calendar.DAY_OF_MONTH, 1);

                calendarEndTime.set(Calendar.MONTH, Calendar.SEPTEMBER);
                calendarEndTime.set(Calendar.DAY_OF_MONTH, 30);
                break;
            case 4:
                calendarStartTime.set(Calendar.MONTH, Calendar.OCTOBER);
                calendarStartTime.set(Calendar.DAY_OF_MONTH, 1);

                calendarEndTime.set(Calendar.MONTH, Calendar.DECEMBER);
                calendarEndTime.set(Calendar.DAY_OF_MONTH, 31);
                break;
        }
        displayContactsStatus(1);
    }

    private void getContactsAccordingToYear() {

        calendarStartTime = Calendar.getInstance();

        calendarEndTime = Calendar.getInstance();

        switch (ButtonDateType) {
            case "Last":
                calendarStartTime.set(Calendar.YEAR, calendarStartTime.get(Calendar.YEAR) - 1);
                calendarStartTime.set(Calendar.MONTH, Calendar.JANUARY);
                calendarStartTime.set(Calendar.DAY_OF_MONTH, 1);

                calendarEndTime.set(Calendar.YEAR, calendarEndTime.get(Calendar.YEAR) - 1);
                calendarEndTime.set(Calendar.MONTH, Calendar.DECEMBER);
                calendarEndTime.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case "Next":
                calendarStartTime.set(Calendar.YEAR, calendarStartTime.get(Calendar.YEAR) + 1);
                calendarStartTime.set(Calendar.MONTH, Calendar.JANUARY);
                calendarStartTime.set(Calendar.DAY_OF_MONTH, 1);

                calendarEndTime.set(Calendar.YEAR, calendarEndTime.get(Calendar.YEAR) + 1);
                calendarEndTime.set(Calendar.MONTH, Calendar.DECEMBER);
                calendarEndTime.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case "This":
                calendarStartTime.set(Calendar.MONTH, Calendar.JANUARY);
                calendarStartTime.set(Calendar.DAY_OF_MONTH, 1);

                calendarEndTime.set(Calendar.MONTH, Calendar.DECEMBER);
                calendarEndTime.set(Calendar.DAY_OF_MONTH, 31);
                break;
        }
        displayContactsStatus(2);
    }

    // get Start date and end date of the contacts and apply filter
    @SuppressLint("LongLogTag")
    private void displayContactsStatus(int dateFormatType) {
        String result;
        // 1 for monthType
        // 2 for yearType

        Log.i(TAG, "Start Time : " + DateYearFormat.format(calendarStartTime.getTime()));

        Log.i(TAG, "End  Time : " + DateYearFormat.format(calendarEndTime.getTime()));

        switch (dateFormatType) {
            case 1:
                result = getSelectedButtonName(ButtonContactType) + " from " + DateMonthFormat.format(calendarStartTime.getTime())
                        + " " + "to " + DateMonthFormat.format(calendarEndTime.getTime());
                DisplayFiltersStatus.setText(result);
                break;

            case 2:
                result = getSelectedButtonName(ButtonContactType) + " from " + DateYearFormat.format(calendarStartTime.getTime())
                        + " " + "to " + DateYearFormat.format(calendarEndTime.getTime());
                DisplayFiltersStatus.setText(result);
                break;
        }

        FinalContactsList.clear();

        FinalContactsList = dBhelper.getContactsBetweenRange(ButtonContactType, beginOfDay(calendarStartTime.getTime()), endOfDay(calendarEndTime.getTime()));

        ButtonApplyFilter.setVisibility(View.VISIBLE);
    }

    //-------Helper Methods---------------//
    @SuppressLint("LongLogTag")
    private int getCurrentQuarter() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.MONTH, Calendar.JANUARY);
        int month2 = calendar1.get(Calendar.MONTH);

        Log.i(TAG, "Current Month :" + String.valueOf(month));
        Log.i(TAG, "index number of JANUARY :" + String.valueOf(month2));

        int Quarter = 0;
        switch (month) {
            case 0:
            case 1:
            case 2:
                Quarter = 1;
                break;
            case 3:
            case 4:
            case 5:
                Quarter = 2;
                break;
            case 6:
            case 7:
            case 8:
                Quarter = 3;
                break;
            case 9:
            case 10:
            case 11:
                Quarter = 4;
                break;
        }
        return Quarter;
    }

    private String getSelectedButtonName(String name) {
        String data = null;
        switch (name) {
            case "createdOn":
                data = "Contact Created";
                break;
            case "dateOfBirth":
                data = "Birthday";
                break;
        }
        return data;
    }

    private void ConfirmAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateCampaignDateFilters.this);
        alertDialog.setTitle("No results found");
        alertDialog.setMessage("Please try with different criteria.");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }

    public long beginOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTimeInMillis();
    }

    public long endOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTimeInMillis();
    }

}
