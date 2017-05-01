package com.mindmesolo.mindme.CreateMobilePages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mindmesolo.mindme.CreateMobilePages.Adapters.HoursAdapter;
import com.mindmesolo.mindme.CreateMobilePages.models.BusinessHours;
import com.mindmesolo.mindme.CreateMobilePages.models.HoursSwitch;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Created by pc-14 on 2/20/2017.
 */

public class AddHours extends AppCompatActivity {

    String[] names = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    SqliteDataBaseHelper dBhelper = new SqliteDataBaseHelper(this);

    SwitchCompat switch_modify_hours, switch_display_time_zone;

    HoursAdapter hoursAdapter;

    ListView list;

    ArrayList<BusinessHours> ListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.business_hour_in_mobile_pages_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        switch_modify_hours = (SwitchCompat) findViewById(R.id.switch_modify_hours);

        switch_display_time_zone = (SwitchCompat) findViewById(R.id.switch_display_time_zone);

        list = (ListView) findViewById(R.id.business_list_view);

        setupBusinessHoursData();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            Intent returnIntent = new Intent();
            if (switch_modify_hours.isChecked()) {
                returnIntent.putParcelableArrayListExtra("get_BusinessHours", hoursAdapter.contactsList);
            } else {
                returnIntent.putParcelableArrayListExtra("get_BusinessHours", ListItems);
            }
            TimeZone tz = TimeZone.getDefault();
            HoursSwitch hoursSwitch = new HoursSwitch();
            hoursSwitch.setTimeSwitchChecked(switch_modify_hours.isChecked());
            hoursSwitch.setTimeZoneSwitch(switch_display_time_zone.isChecked());
            hoursSwitch.setTimeZone(tz.getDisplayName(false, TimeZone.LONG));
            returnIntent.putExtra("get_switches_data", hoursSwitch);
            returnIntent.putExtra("result", "Done");
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBusinessHoursData() {

        ListItems = new ArrayList<>();

        ArrayList<BusinessHours> OldListItems = getIntent().getParcelableArrayListExtra("ListItems");

        HoursSwitch hoursSwitch = getIntent().getParcelableExtra("HourSwitch");

        if (hoursSwitch != null) {
            switch_modify_hours.setChecked(hoursSwitch.isTimeSwitchChecked());
            switch_display_time_zone.setChecked(hoursSwitch.isTimeZoneSwitch());
        }
        //reference check for null
        if (OldListItems != null) {
            ListItems.addAll(OldListItems);
        } else {
            for (int i = 0; i < 7; i++) {
                if (dBhelper.getBusinessSingleDayModel(i).getDayOfWeek() != null) {
                    ListItems.add(dBhelper.getBusinessSingleDayModel(i));
                } else {
                    ListItems.add(getBusinessSingle(i));
                }
            }
        }
        hoursAdapter = new HoursAdapter(getBaseContext(), R.layout.business_hour_row, ListItems);

        list.setAdapter(hoursAdapter);

        DataHelper.getInstance().setListViewHeightBasedOnItems(list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            ArrayList<BusinessHours> TempListItems = new ArrayList<>();
                                            Intent intent = new Intent(AddHours.this, EditHours.class);
                                            intent.putParcelableArrayListExtra("day_List", hoursAdapter.contactsList);
                                            intent.putExtra("current_day", position);
                                            intent.putExtra("current_title", names[position]);
                                            startActivityForResult(intent, 1);
                                        }
                                    }
        );
    }

    // Fix for if user not setup business hours data
    private BusinessHours getBusinessSingle(int i) {
        BusinessHours BH = new BusinessHours();
        BH.setDayOfWeek(i);
        BH.setOpenOnHour(9);
        BH.setOpenOnMinute(0);
        BH.setCloseOnHour(5);
        BH.setCloseOnMinute(0);
        BH.setByAppointmentOnly(false);
        BH.setClosed(false);
        return BH;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            hoursAdapter = new HoursAdapter(getBaseContext(), R.layout.business_hour_row, data.getParcelableArrayListExtra("get_BusinessHours"));
            list.setAdapter(hoursAdapter);
            DataHelper.getInstance().setListViewHeightBasedOnItems(list);
        }
    }
}
