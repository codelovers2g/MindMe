package com.mindmesolo.mindme.Conversation;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import java.util.ArrayList;

public class ConversationMain extends AppCompatActivity {


    final String TAG = "ConversationMain";
    ConversationAdapter adapter;
    SqliteDataBaseHelper dBhelper = new SqliteDataBaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        TabLayout tablayout = (TabLayout) findViewById(R.id.Tabs);

        ArrayList<String> tabs = new ArrayList<>();
        tabs.add("All Contacts");
        tabs.add("Leads");

        final ViewPager viewPager = (ViewPager) findViewById(R.id.ViewPager);

        adapter = new ConversationAdapter(getSupportFragmentManager(), tabs);

        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);
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
        getMenuInflater().inflate(R.menu.conversation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
