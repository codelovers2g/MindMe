package com.mindmesolo.mindme.ToolsAndSettings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

public class ToolsAndSettingsEmail extends AppCompatActivity {

    SqliteDataBaseHelper dBhelper = new SqliteDataBaseHelper(this);

    EditText user_email;
    TextView toolsAndSettingsEmail2, textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tools_and_settings_email);

        user_email = (EditText) findViewById(R.id.user_email);

        toolsAndSettingsEmail2 = (TextView) findViewById(R.id.user_email);
        textView2 = (TextView) findViewById(R.id.textView2);

//        String htmlStringWithMathSymbols = "&\#0034;";
//
//        textView2.setText(Html.fromHtml("&\#0034;"));

        String orgEmail = dBhelper.getOrginalEmail();

        user_email.setText(orgEmail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        getMenuInflater().inflate(R.menu.greeting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
