package com.mindmesolo.mindme.CreateMobilePages;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mindmesolo.mindme.CreateMobilePages.models.SpecialOffer;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.ToastShow;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by enest_09 on 11/2/2016.
 */

public class AddSpecialOffer extends AppCompatActivity implements View.OnClickListener {

    static String setStartTime;
    static String SectionOne = "Free";
    static String SectionTwo = "None";
    static String SectionThree = "This Page";
    EditText EditTextOfferName, EditTextOther;
    EditText EditTextOfferDescription;
    EditText EditTextOfferAmount;
    ToggleButton free, Save, Other;
    ToggleButton saveMoney, savePercent;
    ToggleButton None, After, OnDate;
    TextView TV_OnDate;
    ToggleButton ThisPage, ThankYou, Both;
    LinearLayout saveMenu, otherMenu;
    LinearLayout onDateMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_pages_special_offer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toggleButtonInitialization();
        SpecialOffer specialOffer = getIntent().getParcelableExtra("ExtraData");
        if (specialOffer != null) {
            setupUiData(specialOffer);
        }
    }

    private void setupUiData(SpecialOffer specialOffer) {
        EditTextOfferName.setText(specialOffer.getName());
        EditTextOfferDescription.setText(specialOffer.getDescription());
        SectionOne = specialOffer.getOfferType();
        if (SectionOne != null) {
            switch (SectionOne) {
                case "Free":
                    free.setChecked(true);
                    Other.setChecked(false);
                    Save.setChecked(false);
                    saveMenu.setVisibility(View.GONE);
                    otherMenu.setVisibility(View.GONE);
                    break;
                case "Save":
                    Other.setChecked(false);
                    free.setChecked(false);
                    Save.setChecked(true);
                    EditTextOfferAmount.setText(specialOffer.getOfferAmount());
                    if (specialOffer.getOfferUnit().equalsIgnoreCase("%")) {
                        saveMoney.setChecked(false);
                        savePercent.setChecked(true);
                    } else {
                        saveMoney.setChecked(true);
                        savePercent.setChecked(false);
                    }
                    otherMenu.setVisibility(View.GONE);
                    saveMenu.setVisibility(View.VISIBLE);
                    break;
                case "Other":
                    Other.setChecked(true);
                    free.setChecked(false);
                    Save.setChecked(false);
                    EditTextOther.setText(specialOffer.getOther());
                    saveMenu.setVisibility(View.GONE);
                    otherMenu.setVisibility(View.VISIBLE);
                    break;
            }
        }

        SectionTwo = specialOffer.getExpireType();
        setStartTime = specialOffer.getExpireDate();

        if (SectionTwo != null) {
            //Expiration //
            switch (SectionTwo) {
                case "None":
                    None.setChecked(true);
                    After.setChecked(false);
                    OnDate.setChecked(false);
                    onDateMenu.setVisibility(View.GONE);
                    break;

                case "After":
                    None.setChecked(false);
                    After.setChecked(true);
                    OnDate.setChecked(false);
                    TV_OnDate.setText(specialOffer.getExpireDate());
                    onDateMenu.setVisibility(View.VISIBLE);
                    break;

                case "On Date":
                    None.setChecked(false);
                    After.setChecked(false);
                    OnDate.setChecked(true);
                    TV_OnDate.setText(specialOffer.getExpireDate());
                    onDateMenu.setVisibility(View.VISIBLE);
                    break;
            }
        }

        SectionThree = specialOffer.getDisplayOfferOn();

        if (SectionThree != null) {
            //Display On //
            switch (SectionThree) {
                case "This Page":
                    ThisPage.setChecked(true);
                    ThankYou.setChecked(false);
                    Both.setChecked(false);
                    break;

                case "Thank You":
                    ThisPage.setChecked(false);
                    ThankYou.setChecked(true);
                    Both.setChecked(false);
                    break;

                case "Both":
                    ThisPage.setChecked(false);
                    ThankYou.setChecked(false);
                    Both.setChecked(true);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
    /*first section type of offer  */
            case R.id.free:
                free.setChecked(true);
                Save.setChecked(false);
                Other.setChecked(false);
                saveMenu.setVisibility(View.GONE);
                otherMenu.setVisibility(View.GONE);
                SectionOne = "Free";
                break;

            case R.id.Save:
                Save.setChecked(true);
                free.setChecked(false);
                Other.setChecked(false);
                otherMenu.setVisibility(View.GONE);
                saveMenu.setVisibility(View.VISIBLE);
                SectionOne = "Save";
                break;

            case R.id.Other:
                Save.setChecked(false);
                free.setChecked(false);
                Other.setChecked(true);
                saveMenu.setVisibility(View.GONE);
                otherMenu.setVisibility(View.VISIBLE);
                SectionOne = "Other";
                break;

    /*second section  Expiration On*/
            case R.id.None:
                None.setChecked(true);
                After.setChecked(false);
                OnDate.setChecked(false);
                onDateMenu.setVisibility(View.GONE);
                SectionTwo = "None";
                break;

            case R.id.After:
                None.setChecked(false);
                OnDate.setChecked(false);
                After.setChecked(true);
                onDateMenu.setVisibility(View.VISIBLE);
                SectionTwo = "After";
                break;

            case R.id.OnDate:
                None.setChecked(false);
                After.setChecked(false);
                OnDate.setChecked(true);
                onDateMenu.setVisibility(View.VISIBLE);
                SectionTwo = "On Date";
                break;

            case R.id.tv_on_date:
                datePickerSelectFromDate();
                break;

    /*second section child part */
            case R.id.t_btn_save_money:
                saveMoney.setChecked(true);
                savePercent.setChecked(false);
                break;

            case R.id.t_btn_save_percent:
                saveMoney.setChecked(false);
                savePercent.setChecked(true);
                break;

    /*third section  Display offers On*/
            case R.id.ThisPage:
                ThisPage.setChecked(true);
                ThankYou.setChecked(false);
                Both.setChecked(false);
                SectionThree = "This page";
                break;
            case R.id.ThankYou:
                ThankYou.setChecked(true);
                ThisPage.setChecked(false);
                Both.setChecked(false);
                SectionThree = "Thank You";
                break;
            case R.id.Both:
                ThankYou.setChecked(false);
                ThisPage.setChecked(false);
                Both.setChecked(true);
                SectionThree = "Both";
                break;
        }
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
            if (StringUtils.isBlank(EditTextOfferName.getText())) {
                ToastShow.setText(getBaseContext(), "Please enter offer name.", Toast.LENGTH_LONG);

            } else if (StringUtils.isBlank(EditTextOfferDescription.getText())) {
                ToastShow.setText(getBaseContext(), "Please enter offer Description.", Toast.LENGTH_LONG);

            } else {
                SpecialOffer specialOffer = getSpecialOfferData();
                returnIntent.putExtra("get_specialOffer_data", specialOffer);
                returnIntent.putExtra("result", "Done");
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleButtonInitialization() {

        EditTextOfferName = (EditText) findViewById(R.id.edit_txt_offer_name);

        EditTextOfferDescription = (EditText) findViewById(R.id.edit_txt_offer_description);

        EditTextOther = (EditText) findViewById(R.id.EditTextOther);

        EditTextOfferAmount = (EditText) findViewById(R.id.EditTextOfferAmount);

        saveMenu = (LinearLayout) findViewById(R.id.saveMenu);

        otherMenu = (LinearLayout) findViewById(R.id.otherMenu);

        onDateMenu = (LinearLayout) findViewById(R.id.onDateMenu);

        free = (ToggleButton) findViewById(R.id.free);
        free.setOnClickListener(this);

        Save = (ToggleButton) findViewById(R.id.Save);
        Save.setOnClickListener(this);

        Other = (ToggleButton) findViewById(R.id.Other);
        Other.setOnClickListener(this);

        saveMoney = (ToggleButton) findViewById(R.id.t_btn_save_money);
        saveMoney.setOnClickListener(this);

        savePercent = (ToggleButton) findViewById(R.id.t_btn_save_percent);
        savePercent.setOnClickListener(this);

        None = (ToggleButton) findViewById(R.id.None);
        After = (ToggleButton) findViewById(R.id.After);
        OnDate = (ToggleButton) findViewById(R.id.OnDate);

        None.setOnClickListener(this);
        After.setOnClickListener(this);
        OnDate.setOnClickListener(this);

        ThisPage = (ToggleButton) findViewById(R.id.ThisPage);
        ThankYou = (ToggleButton) findViewById(R.id.ThankYou);

        Both = (ToggleButton) findViewById(R.id.Both);
        ThisPage.setOnClickListener(this);
        ThankYou.setOnClickListener(this);
        Both.setOnClickListener(this);

        TV_OnDate = (TextView) findViewById(R.id.tv_on_date);
        TV_OnDate.setOnClickListener(this);
    }

    private void datePickerSelectFromDate() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        Calendar calendar = Calendar.getInstance();

                        calendar.set(year, (monthOfYear), dayOfMonth);

                        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

                        setStartTime = String.valueOf(dateFormat.format(calendar.getTime()));

                        TV_OnDate.setText(setStartTime);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private SpecialOffer getSpecialOfferData() {
        SpecialOffer specialOffer = new SpecialOffer();
        specialOffer.setName(EditTextOfferName.getText().toString());
        specialOffer.setDescription(EditTextOfferDescription.getText().toString());
        //Type of offers //
        switch (SectionOne) {
            case "Free":
                specialOffer.setOfferType(SectionOne);
                break;
            case "Save":
                specialOffer.setOfferType(SectionOne);
                specialOffer.setOfferAmount(EditTextOfferAmount.getText().toString());
                if (saveMoney.isChecked()) {
                    specialOffer.setOfferUnit("$");
                } else {
                    specialOffer.setOfferUnit("%");
                }
                break;
            case "Other":
                specialOffer.setOther(EditTextOther.getText().toString());
                specialOffer.setOfferType(SectionOne);
                break;
        }
        //Expiration //
        switch (SectionTwo) {
            case "None":
                //no case exist for this //
                specialOffer.setExpireType(SectionTwo);
                break;
            case "After":
            case "On Date":
                specialOffer.setExpireType(SectionTwo);
                specialOffer.setExpireDate(setStartTime);
                break;
        }
        //Display On //
        switch (SectionThree) {
            case "This Page":
                specialOffer.setDisplayOfferOn(SectionThree);
                break;
            case "Thank You":
                specialOffer.setDisplayOfferOn(SectionThree);
                break;
            case "Both":
                specialOffer.setDisplayOfferOn(SectionThree);
                break;
        }
        return specialOffer;
    }

    @Override
    protected void onPause() {
        super.onPause();
        setStartTime = null;
        SectionOne = "Free";
        SectionTwo = "None";
        SectionThree = "This Page";

    }
}


