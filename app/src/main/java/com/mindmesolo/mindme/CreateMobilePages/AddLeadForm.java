package com.mindmesolo.mindme.CreateMobilePages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.mindmesolo.mindme.CreateMobilePages.Adapters.LeadCaptureAdapter;
import com.mindmesolo.mindme.Models.CommonModel;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.CommonListViewAdapter;
import com.mindmesolo.mindme.helper.CustomListView;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import java.util.ArrayList;

public class AddLeadForm extends AppCompatActivity implements View.OnClickListener, CustomListView.OnItemDoubleTapLister {

    LeadCaptureAdapter leadCaptureAdapter;

    CommonListViewAdapter commonListViewAdapter;
    ToggleButton t_btn_all, t_btn_none, t_btn_select;
    ListView listView2;
    String[] LeadItems = {
            "First Name",
            "Last Name",
            "Company",
            "Email Address",
            "Mobile Phone",
            "Home Phone",
            "Work Phone",
            "Website",
            "Address",
            "Zip Code Only"
    };
    ArrayList<CommonModel> ArrayList = new ArrayList<>();
    ArrayList<CommonModel> ArrayListInterests = new ArrayList<>();
    private CustomListView mListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.mobilepages_lead_capture);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupUi();

        mListView = (CustomListView) findViewById(R.id.ListView);

        listView2 = (ListView) findViewById(R.id.listView2);

        ArrayList = getIntent().getParcelableArrayListExtra("LeadFormExtraData");

        leadCaptureAdapter = new LeadCaptureAdapter(getBaseContext(), getListItems());

        mListView.setAdapter(leadCaptureAdapter);

        mListView.setOnItemDoubleClickListener(this);
        ArrayListInterests = getIntent().getParcelableArrayListExtra("LeadFormExtraInterestsData");
        if (ArrayListInterests != null && ArrayListInterests.size() > 0) {
            t_btn_select.setChecked(true);
            getAllInterests();
            for (CommonModel model : ArrayListInterests) {
                for (CommonModel model1 : commonListViewAdapter.List) {
                    if (model.getTitle().equalsIgnoreCase(model1.getTitle())) {
                        model1.setChecked(true);
                    }
                }
            }
        }
        DataHelper.getInstance().setListViewHeightBasedOnItems(mListView);

    }

    private void setupUi() {

        t_btn_none = (ToggleButton) findViewById(R.id.t_btn_none);
        t_btn_none.setOnClickListener(this);

        t_btn_select = (ToggleButton) findViewById(R.id.t_btn_select);
        t_btn_select.setOnClickListener(this);

        t_btn_all = (ToggleButton) findViewById(R.id.t_btn_all);
        t_btn_all.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
            if (getSelectedFields().size() > 0) {
                Intent returnIntent = new Intent();
                returnIntent.putParcelableArrayListExtra("LeadFormData", getSelectedFields());
                if (t_btn_all.isChecked() || t_btn_select.isChecked()) {
                    returnIntent.putParcelableArrayListExtra("LeadFormInterests", getSelectedInterestsFields());
                }
                returnIntent.putExtra("result", "Done");
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
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
    public void OnDoubleTap(AdapterView<?> parent, View view, int position, long id) {

        CommonModel item = leadCaptureAdapter.ArrayList.get(position);

        if (item.getRequired()) {
            item.setRequired(false);
        } else item.setRequired(true);

        leadCaptureAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnSingleTap(AdapterView<?> parent, View view, int position, long id) {

        CommonModel item = leadCaptureAdapter.ArrayList.get(position);

        if (item.getChecked()) {
            item.setRequired(false);
        } else item.setChecked(true);

        leadCaptureAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.t_btn_none:
                t_btn_none.setChecked(true);
                t_btn_select.setChecked(false);
                t_btn_all.setChecked(false);
                listView2.setVisibility(View.GONE);
                break;
            case R.id.t_btn_select:
                t_btn_none.setChecked(false);
                t_btn_select.setChecked(true);
                t_btn_all.setChecked(false);
                listView2.setVisibility(View.VISIBLE);
                getAllInterests();
                break;
            case R.id.t_btn_all:
                t_btn_none.setChecked(false);
                t_btn_select.setChecked(false);
                t_btn_all.setChecked(true);
                listView2.setVisibility(View.VISIBLE);
                getAllInterests();
                for (CommonModel model1 : commonListViewAdapter.List) {
                    model1.setChecked(true);
                }
                break;
        }
    }

    private ArrayList<CommonModel> getListItems() {
        ArrayList<CommonModel> arrayList = new ArrayList<>();
        for (String item : LeadItems) {
            if (ArrayList != null && ArrayList.size() > 0) {
                arrayList.add(getItem(item));
            } else {
                arrayList.add(new CommonModel(item, false, false));
            }
        }
        return arrayList;
    }

    private CommonModel getItem(String item) {
        CommonModel data = null;
        for (CommonModel model : ArrayList) {
            if (model.getTitle().equals(item)) {
                data = model;
                break;
            }
        }
        if (data == null) {
            data = new CommonModel(item, false, false);
        }
        return data;
    }

    private ArrayList<CommonModel> getSelectedFields() {

        ArrayList<CommonModel> SelectedItems = new ArrayList<>();

        for (CommonModel item : leadCaptureAdapter.ArrayList) {
            if (item.getChecked()) {
                SelectedItems.add(item);
            }
        }
        return SelectedItems;
    }

    private ArrayList<CommonModel> getSelectedInterestsFields() {
        ArrayList<CommonModel> List = new ArrayList<>();
        for (CommonModel interest : commonListViewAdapter.List) {
            if (interest.getChecked()) {
                List.add(interest);
            }
        }
        return List;
    }

    // Add all interests into second list view
    private void getAllInterests() {
        SqliteDataBaseHelper sqliteDataBaseHelper = new SqliteDataBaseHelper(getBaseContext());
        ArrayList<CommonModel> List = new ArrayList<CommonModel>();
        if (sqliteDataBaseHelper.getAllInterestNames().size() > 0) {
            for (String Name : sqliteDataBaseHelper.getAllInterestInLeadForm()) {
                List.add(new CommonModel(Name, false));
            }
            commonListViewAdapter = new CommonListViewAdapter(getBaseContext(), List);
            listView2.setAdapter(commonListViewAdapter);
            DataHelper.getInstance().setListViewHeightBasedOnItems(listView2);
        }
    }
}
