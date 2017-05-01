package com.mindmesolo.mindme.CreateMobilePages;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.mindmesolo.mindme.CreateMobilePages.Adapters.CreateWelComePageAdapter;
import com.mindmesolo.mindme.Models.CommonModel;
import com.mindmesolo.mindme.R;

import java.util.ArrayList;

/**
 * Created by enest_09 on 11/2/2016.
 */

public class CreateWelComePage extends AppCompatActivity {

    final static String TAG = "CreateWelComePage";

    CreateWelComePageAdapter mobilePageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_wel_come_page);

        Button next = (Button) findViewById(R.id.btnNext);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> items = new ArrayList<>();
                for (CommonModel item : mobilePageAdapter.ArrayList) {
                    if (item.getChecked()) {
                        items.add(item.getTitle());
                    }
                }
                Intent intent = new Intent(CreateWelComePage.this, CreateMobilePage.class);
                intent.putStringArrayListExtra("MobilePageBuilderElements", items);
                startActivity(intent);
            }
        });

        ListView listView = (ListView) findViewById(R.id.listView);

        mobilePageAdapter = new CreateWelComePageAdapter(getBaseContext(), getListItems());

        listView.setAdapter(mobilePageAdapter);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private ArrayList<CommonModel> getListItems() {
        ArrayList<CommonModel> arrayList = new ArrayList<>();
        arrayList.add(new CommonModel("Section", "Add basic info to your page", false));
        arrayList.add(new CommonModel("Logo", false));
        arrayList.add(new CommonModel("Title", false));
        arrayList.add(new CommonModel("Media", false));
        arrayList.add(new CommonModel("Paragraph", false));
        arrayList.add(new CommonModel("Hours", false));
        arrayList.add(new CommonModel("Map", false));
        arrayList.add(new CommonModel("Social", false));
        arrayList.add(new CommonModel("Section", "Engage your visitors with a call to action", false));
        arrayList.add(new CommonModel("Lead Form", false));
        arrayList.add(new CommonModel("Call to Action", false));
        arrayList.add(new CommonModel("Special Offer", false));
        return arrayList;
    }
}
