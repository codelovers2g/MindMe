package com.mindmesolo.mindme.GettingStarted;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BusinessHours extends AppCompatActivity {

    String[] names = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    SqliteDataBaseHelper dBhelper = new SqliteDataBaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.business_hour_in_tools_settings);

        String Data = dBhelper.getBusinessHoursDetail(0);

        // check if business Hour Data is inserted or not
//        if (StringUtils.isBlank(Data) || StringUtils.isEmpty(Data)) {
//            if (dBhelper.insertTempBusinessHours()) {
//                setupBusinessHoursData();
//            }
//        } else {
//            setupBusinessHoursData();
//        }
        setTitle("Business Hours");
        setupBusinessHoursData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupBusinessHoursData();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void setupBusinessHoursData() {

        ListView list = (ListView) findViewById(R.id.business_list_view);

        List<HashMap<String, String>> ArrayList = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            HashMap<String, String> hm = new HashMap<>();
            hm.put("name", "" + names[i]);
            String Value = getStringValues(dBhelper.getBusinessHoursDetail(i));
            if (StringUtils.isNotBlank(Value)) {
                hm.put("values", "" + Value);
            } else {
                dBhelper.insertTempBusinessHours(i);
                hm.put("values", "" + getStringValues(dBhelper.getBusinessHoursDetail(i)));
            }
            ArrayList.add(hm);
        }

        String[] from = {"name", "values"};

        int[] to = {R.id.txt, R.id.incomingdatastatus};

        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), ArrayList, R.layout.business_hour_row, from, to);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent intent = new Intent(BusinessHours.this, BusinessHoursInnerSettings.class);
                                            intent.putExtra("current_day", position);
                                            intent.putExtra("current_title", names[position]);
                                            startActivity(intent);
                                        }
                                    }
        );
    }

    private String getStringValues(String data) {
        if (data != null) {
            return data;
        } else {
            return "";
        }
    }

    ;
}
