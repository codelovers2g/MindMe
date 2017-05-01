package com.mindmesolo.mindme.CreateMobilePages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.mindmesolo.mindme.CreateMobilePages.Adapters.SocialMediaAdapter;
import com.mindmesolo.mindme.GettingStarted.SocialMediaModel;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class AddSocialMedia extends AppCompatActivity {

    ListView ListView;

    SocialMediaAdapter socialMediaAdapter;

    SqliteDataBaseHelper sqliteDataBaseHelper;

    String[] socialMediaItems = {
            "Linkedln",
            "Facebook",
            "Twitter",
            "Google+",
            "Youtube",
            "Instagram",
            "Pinterest"
    };

    ArrayList<String> ListItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_social_media);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView = (ListView) findViewById(R.id.ListView);
        sqliteDataBaseHelper = new SqliteDataBaseHelper(this);

        ArrayList<SocialMediaModel> list = getIntent().getParcelableArrayListExtra("ExtraData");
        if (list != null && list.size() > 0) {
            for (SocialMediaModel data : list) {
                ListItems.add(data.getSocialMediaName());
            }
        }
        setupListViewForSocialMedia();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
            ArrayList<SocialMediaModel> selectedItems = getSelectedItems();
            if (selectedItems.size() > 0) {
                returnIntent.putParcelableArrayListExtra("SocialMediaData", selectedItems);
                returnIntent.putExtra("result", "Done");
            }
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupListViewForSocialMedia() {
        ArrayList<SocialMediaModel> items = new ArrayList<>();
        for (String SocialMediaItem : socialMediaItems) {
            String socialMediaId = sqliteDataBaseHelper.getSocialId(SocialMediaItem);
            if (StringUtils.isNotBlank(socialMediaId)) {
                switch (SocialMediaItem) {
                    case "Linkedln":
                        items.add(new SocialMediaModel("Linkedln", socialMediaId, getChecked("Linkedln"), R.drawable.linkedin));
                        break;

                    case "Facebook":
                        items.add(new SocialMediaModel("Facebook", socialMediaId, getChecked("Facebook"), R.drawable.facebook));
                        break;

                    case "Twitter":
                        items.add(new SocialMediaModel("Twitter", socialMediaId, getChecked("Twitter"), R.drawable.twitter));
                        break;

                    case "Google+":
                        items.add(new SocialMediaModel("Google+", socialMediaId, getChecked("Google+"), R.drawable.googleplus));
                        break;

                    case "Youtube":
                        items.add(new SocialMediaModel("Youtube", socialMediaId, getChecked("Youtube"), R.drawable.youtubeplay));
                        break;

                    case "Instagram":
                        items.add(new SocialMediaModel("Instagram", socialMediaId, getChecked("Instagram"), R.drawable.instagram));
                        break;

                    case "Pinterest":
                        items.add(new SocialMediaModel("Pinterest", sqliteDataBaseHelper.getSocialId("Pinterest"), getChecked("Pinterest"), R.drawable.pinterest));
                        break;
                }
            }
        }
        socialMediaAdapter = new SocialMediaAdapter(AddSocialMedia.this, R.layout.layout_social_media_mobile_pages, items);
        ListView.setAdapter(socialMediaAdapter);
    }

    private boolean getChecked(String item) {
        if (ListItems.size() > 0 && ListItems.contains(item))
            return true;
        else
            return false;
    }

    private ArrayList getSelectedItems() {
        ArrayList<SocialMediaModel> contactsList = new ArrayList<>();
        for (SocialMediaModel notificationModal : socialMediaAdapter.contactsList) {
            if (notificationModal.isChecked()) {
                contactsList.add(notificationModal);
            }
        }
        return contactsList;
    }
}
