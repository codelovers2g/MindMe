package com.mindmesolo.mindme.ViewMobilePages;

/**
 * Created by eNest on 5/16/2016.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindmesolo.mindme.CreateMobilePages.CreateWelComePage;
import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.Services.VolleyApi;
import com.mindmesolo.mindme.ViewMobilePages.Helper.OnLeftRightSwipeListener;
import com.mindmesolo.mindme.ViewMobilePages.Helper.OnTopBottomSwipeListener;
import com.mindmesolo.mindme.ViewMobilePages.Helper.ViewMobilePagesModel;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.ToastShow;
import com.tuyenmonkey.mkloader.MKLoader;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ViewMobilePages extends MainActivity implements View.OnClickListener, Animation.AnimationListener {

    private static final String TAG = "ViewMobilePages";
    static boolean popupViewIsEnable = false;
    // Animation
    Animation animSlideUp;
    Animation animSlideDown;
    LinearLayout layoutMenuItems;
    LinearLayout LayoutMainBottomView;

    WebView webView;

    ImageView imageViewCreateMobilePage;

    TextView textViewQuickMenu, textViewMobilePageTitle;

    TextView textViewCopyText, textViewLinkUrl, textViewLeadCount, textViewPageViewCount;

    MKLoader loader_progress;

    SqliteDataBaseHelper sqliteDataBaseHelper;

    Dialog dialog;

    int PagePosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        sqliteDataBaseHelper = new SqliteDataBaseHelper(this);

        View contentView = inflater.inflate(R.layout.mobile_pages_activity, frameLayout, false);
        drawer.addView(contentView, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearlayout);
        linearLayout.setVisibility(View.GONE);

        View view = (View) findViewById(R.id.view);
        view.setVisibility(View.GONE);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

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

        //--------------------main activity data-------------------------//
        MainActivityObject = true;

        loadViews();

        loadWebView();
    }

    private void loadWebView() {
        loader_progress = (MKLoader) findViewById(R.id.loader_progress);
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new myWebClient());
        webView.setVerticalScrollBarEnabled(true);

        getMobilePages();

        loadUrl();

        webView.setOnTouchListener(new OnLeftRightSwipeListener(ViewMobilePages.this) {
            public void onSwipeRight() {
                PagePosition--;
                loadUrl();
            }

            public void onSwipeLeft() {
                PagePosition++;
                loadUrl();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mobile_page_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu:
                startActivity(new Intent(ViewMobilePages.this, ViewMobilePagesList.class));
                break;

            case R.id.action_settings:
                actionViewMenus();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Popup dialog for inner menus in pages
    private void actionViewMenus() {
        dialog = new Dialog(this);
        dialog.getWindow().setWindowAnimations(R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.mobile_pages_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM | Gravity.CENTER;
        wmlp.x = 0;   //x position
        wmlp.y = 50;   //y position
        dialog.show();
    }

    private void loadViews() {

        // load the animation
        animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);

        // set animation listener
        animSlideUp.setAnimationListener(this);
        animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        // set animation listener
        animSlideUp.setAnimationListener(this);

        imageViewCreateMobilePage = (ImageView) findViewById(R.id.fab);
        imageViewCreateMobilePage.setOnClickListener(this);

        textViewQuickMenu = (TextView) findViewById(R.id.fab2);
        textViewQuickMenu.setOnClickListener(this);

        textViewMobilePageTitle = (TextView) findViewById(R.id.textViewMobilePageTitle);

        textViewLinkUrl = (TextView) findViewById(R.id.textViewLinkUrl);

        textViewLeadCount = (TextView) findViewById(R.id.textViewleadCaptureCount);

        textViewPageViewCount = (TextView) findViewById(R.id.textViewPageViewCount);

        textViewCopyText = (TextView) findViewById(R.id.textViewCopyText);
        textViewCopyText.setOnClickListener(this);

        layoutMenuItems = (LinearLayout) findViewById(R.id.layoutMenu);
        LayoutMainBottomView = (LinearLayout) findViewById(R.id.buttom_menu_layout);
        LayoutMainBottomView.setOnTouchListener(new OnTopBottomSwipeListener(ViewMobilePages.this) {
            public void onSwipeTop() {
                OptionMenu();
            }

            public void onSwipeBottom() {
                OptionMenu();
            }
        });
        TextView ViewPageDetail = (TextView) findViewById(R.id.ViewPageDetail);
        ViewPageDetail.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //loadWebView();
        MainActivityObject = true;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        LayoutMainBottomView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // + icon image on click
            case R.id.fab:
                startActivity(new Intent(getBaseContext(), CreateWelComePage.class));
                break;
            // Quick view menu
            case R.id.fab2:
                OptionMenu();
                break;
            case R.id.textViewCopyText:
                DataHelper.getInstance().setClipboard(getBaseContext(), textViewLinkUrl.getText().toString());
                ToastShow.setText(ViewMobilePages.this, "Link Copied Successfully.", Toast.LENGTH_SHORT);
                break;
            case R.id.ViewPageDetail:
                List<ViewMobilePagesModel> arrayList1 = sqliteDataBaseHelper.getAllMobilePagesData();
                startActivity(new Intent(getBaseContext(), ViewSinglePageDetail.class)
                        .putExtra("PageModel", (Parcelable) arrayList1.get(PagePosition)));
                break;
        }
    }

    private void getMobilePages() {
        DataHelper.getInstance().startDialog(ViewMobilePages.this, "Getting ViewMobilePages.");
        String org_id = new SqliteDataBaseHelper(getBaseContext()).getOrganizationId();
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
                            if (arrayList == response.length()) break;
                            sqliteDataBaseHelper.insertIntoMobilePages(response.getJSONObject(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    loadUrl();
                }
            }
        });
    }

    private void loadUrl() {

        List<ViewMobilePagesModel> arrayList1 = sqliteDataBaseHelper.getAllMobilePagesData();

        Collections.sort(arrayList1, new CustomComparator());

        Collections.reverse(arrayList1);

        if (PagePosition < 0 || PagePosition > arrayList1.size()) PagePosition = 0;

        if (arrayList1 != null && arrayList1.size() > 0) {
            webView.loadUrl(arrayList1.get(PagePosition).getMobilePageUrl());
            textViewMobilePageTitle.setText(arrayList1.get(PagePosition).getMobilePageName());
            textViewLinkUrl.setText(arrayList1.get(PagePosition).getMobilePageUrl());
            textViewLeadCount.setText(String.valueOf(arrayList1.get(PagePosition).getMobilePageLeadCaptureCount()));
            textViewPageViewCount.setText(String.valueOf(arrayList1.get(PagePosition).getMobilePageOpenCount()));
        }
    }

    private void OptionMenu() {
        if (popupViewIsEnable == true) {
            popupViewIsEnable = false;
            layoutMenuItems.setVisibility(View.GONE);
        } else {
            popupViewIsEnable = true;
            layoutMenuItems.setVisibility(View.VISIBLE);
        }
    }

    private class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            webView.setVisibility(View.GONE);
            loader_progress.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            webView.setVisibility(View.VISIBLE);
            loader_progress.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            webView.setVisibility(View.GONE);
            loader_progress.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;
        }

        @RequiresApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            final Uri uri = request.getUrl();
            loader_progress.setVisibility(View.VISIBLE);
            view.loadUrl(String.valueOf(uri));
            return true;
        }
    }

    public class CustomComparator implements Comparator<ViewMobilePagesModel> {
        @Override
        public int compare(ViewMobilePagesModel o1, ViewMobilePagesModel o2) {
            return o1.getMobilePageCreatedDate().compareTo(o2.getMobilePageCreatedDate());
        }
    }
}
