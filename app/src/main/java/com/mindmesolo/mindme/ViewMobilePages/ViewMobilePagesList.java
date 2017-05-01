package com.mindmesolo.mindme.ViewMobilePages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mindmesolo.mindme.CreateMobilePages.CreateWelComePage;
import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.Services.VolleyApi;
import com.mindmesolo.mindme.ViewMobilePages.Helper.ViewMobilePagesModel;
import com.mindmesolo.mindme.ViewMobilePages.adapter.ViewMobilePagesAdapter;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ViewMobilePagesList extends MainActivity {

    private static final String TAG = "ViewMobilePagesList";

    ViewMobilePagesAdapter myCustomAdapter;

    SqliteDataBaseHelper sqliteDataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        sqliteDataBaseHelper = new SqliteDataBaseHelper(this);

        View contentView = inflater.inflate(R.layout.activity_view_mobile_pages_list, frameLayout, false);
        drawer.addView(contentView, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearlayout);
        linearLayout.setVisibility(View.GONE);

        View view = (View) findViewById(R.id.view);
        view.setVisibility(View.GONE);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ImageButton imageButton = (ImageButton) findViewById(R.id.fab);
        imageButton.setOnClickListener((View v) -> {
            startActivity(new Intent(getBaseContext(), CreateWelComePage.class));
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        // new code
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        // changes

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        MainActivityObject = true;

        getMobilePages();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mobile_pages_list_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu:
                startActivity(new Intent(ViewMobilePagesList.this, ViewMobilePages.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setMobilePages() {
        List<ViewMobilePagesModel> arrayList1 = sqliteDataBaseHelper.getAllMobilePagesData();

        Collections.sort(arrayList1, new CustomComparator());

        Collections.reverse(arrayList1);

        myCustomAdapter = new ViewMobilePagesAdapter(getBaseContext(), arrayList1);
        ListView androidListView = (ListView) findViewById(R.id.list_view);
        androidListView.setAdapter(myCustomAdapter);
        androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getBaseContext(), ViewSinglePageDetail.class)
                        .putExtra("PageModel", (Parcelable) myCustomAdapter.getItem(position)));
            }
        });
    }

    private void getMobilePages() {
        DataHelper.getInstance().startDialog(ViewMobilePagesList.this, "Getting ViewMobilePages.");
        String org_id = new SqliteDataBaseHelper(getBaseContext()).getOrganizationId();
        Log.i("OrgId", org_id);
        String REGISTER_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + org_id + "/mobilePageList";
        VolleyApi.getInstance().getJsonArray(getBaseContext(), REGISTER_URL, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                DataHelper.getInstance().stopDialog();
                if (result.success && result.dataIsArray()) {
                    JSONArray response = result.getDataAsArray();
                    for (int i = 0; i < response.length(); i++) {
                        try {

                            int arrayList = sqliteDataBaseHelper.mobilePagesCount();

                            Log.i("DbHelper", String.valueOf("ContactOnLocalDB" + arrayList));

                            sqliteDataBaseHelper.insertIntoMobilePages(response.getJSONObject(i));
                            if (arrayList == response.length()) {
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                setMobilePages();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivityObject = true;
    }

    public class CustomComparator implements Comparator<ViewMobilePagesModel> {
        @Override
        public int compare(ViewMobilePagesModel o1, ViewMobilePagesModel o2) {
            return o1.getMobilePageCreatedDate().compareTo(o2.getMobilePageCreatedDate());
        }
    }
}
