package com.mindmesolo.mindme.GettingStarted;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.mindmesolo.mindme.ContactAndLists.ContactsAndLists;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.DataHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User1 on 25-05-2016.
 */
public class ImportContactType extends Activity {

    public static Activity ImportContactsCallBack;
    ListView list;
    List<HashMap<String, String>> alist;
    String[] Items = {
            "Leads",
            "Trials",
            "Customers",
            "Personal",
            "Inactive",
            "Cancelled"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.importtype);

        list = (ListView) findViewById(R.id.ListViewType);
        ImportContactsCallBack = this;
        alist = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < Items.length; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("Items", "" + Items[i]);
            alist.add(hm);
        }
        String[] from = {"Items"};
        int[] to = {R.id.listview_item_title};
        SimpleAdapter simpleadapter = new SimpleAdapter(getApplicationContext(), alist, R.layout.contacttypelayout, from, to);
        list.setAdapter(simpleadapter);
        DataHelper.getInstance().setListViewHeightBasedOnItems(list);

        final Intent intent = getIntent();

        final String NextActivity = intent.getStringExtra("NextActivity");

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String ContactType = Items[position].toUpperCase();

                ContactType = new DataHelper().GroupsFilter(ContactType);

                if (NextActivity.equalsIgnoreCase("IMPORTCONTACTS")) {
                    Intent intent = new Intent(ImportContactType.this, Searchcontact.class);
                    intent.putExtra("ImportContactType", ContactType);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ImportContactType.this, ContactsAndLists.class);
                    intent.putExtra("ImportContactType", ContactType);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}
