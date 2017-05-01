package com.mindmesolo.mindme.GettingStarted;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.MobilePageHelper.ListViewItem;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.ToolsAndSettings.ToolsSettingsInterests;
import com.mindmesolo.mindme.helper.CircleImageView;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GettingStart extends AppCompatActivity {

    SqliteDataBaseHelper dBhelper = new SqliteDataBaseHelper(this);
    DataHelper dataHelper = new DataHelper();
    GettingStartAdapter gettingStartAdapter;
    CircleImageView companylogo;
    ListView androidListView;
    ImageView trainingImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gettingstart);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        androidListView = (ListView) findViewById(R.id.list_view);

        setupListViewItems();

        androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(getBaseContext(), ImportContactType.class);
                        intent.putExtra("NextActivity", "IMPORTCONTACTS");
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(getBaseContext(), Profile_settings.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(getBaseContext(), Greeting_and_forwarding.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(getBaseContext(), BusinessHours.class);
                        startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(getBaseContext(), ToolsSettingsInterests.class);
                        startActivity(intent4);
                        break;
                    case 5:
                        Intent intent7 = new Intent(getBaseContext(), SocialMedia.class);
                        startActivity(intent7);
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trainingandsupport, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(GettingStart.this, TrainingAndSupport.class);
                intent.putExtra("URL", "http://support.mindmesolo.com/");
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupListViewItems() {
        trainingImage = (ImageView) findViewById(R.id.training);

        trainingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), TrainingAndSupport.class);
                startActivity(intent);
            }
        });

        ArrayList<ListViewItem> ListViewItems = getListViewItems();

        gettingStartAdapter = new GettingStartAdapter(getBaseContext(), R.layout.list_view_geeting_started_tools_settings, ListViewItems);

        androidListView.setAdapter(gettingStartAdapter);

        DataHelper.getInstance().setListViewHeightBasedOnItems(androidListView);

    }

    ;

    private ArrayList<ListViewItem> getListViewItems() {

        ArrayList<ListViewItem> ListViewItems = new ArrayList<ListViewItem>();

        ListViewItems.add(new ListViewItem("Import Contacts", R.drawable.import_contacts_icon, getItemsStatus("Import Contacts")));

        ListViewItems.add(new ListViewItem("Profile", R.drawable.account_icon, getItemsStatus("Profile")));

        ListViewItems.add(new ListViewItem("Phone Greeting & Forwarding", R.drawable.phone_greeting_icon, getItemsStatus("Phone Greeting & Forwarding")));

        ListViewItems.add(new ListViewItem("Business Hours", R.drawable.clock_icon, getItemsStatus("Business Hours")));

        ListViewItems.add(new ListViewItem("Interests", R.drawable.interests_icon, getItemsStatus("Interests")));

        ListViewItems.add(new ListViewItem("Social Media", R.drawable.social_media_icon, getItemsStatus("Social Media")));

        return ListViewItems;
    }

    private boolean getItemsStatus(String Data) {
        SharedPreferences DASHBOARD = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        String GettingStartData = DASHBOARD.getString(MainActivity.PREFS_KEY_GETTING_START, null);
        if (GettingStartData != null) {
            List<String> ArrayList = Arrays.asList(GettingStartData.split(","));
            if (ArrayList.contains(Data)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    ;

    @Override
    protected void onResume() {
        super.onResume();
        setupListViewItems();
    }
}
