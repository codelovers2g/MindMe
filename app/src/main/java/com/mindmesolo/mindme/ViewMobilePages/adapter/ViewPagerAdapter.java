package com.mindmesolo.mindme.ViewMobilePages.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.ViewMobilePages.Helper.ViewMobilePagesModel;

import java.util.List;

/**
 * Created by pc-14 on 3/17/2017.
 */

public class ViewPagerAdapter extends PagerAdapter {

    Context context;

    List<ViewMobilePagesModel> arrayList;

    public ViewPagerAdapter(Context context, List<ViewMobilePagesModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewMobilePagesModel customPagerEnum = arrayList.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.web_view_for_mobile_pager, container, false);


        WebView webView = (WebView) layout.findViewById(R.id.webView);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new myWebClient());
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        webView.loadUrl(customPagerEnum.getMobilePageUrl());
        TextView textView = (TextView) layout.findViewById(R.id.textView);
        textView.setText(customPagerEnum.getMobilePageName());
        container.addView(layout);
        return container;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    private class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //webView.setVisibility(View.GONE);
            //loader_progress.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //webView.setVisibility(View.VISIBLE);
            //loader_progress.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //webView.setVisibility(View.GONE);
            //loader_progress.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;
        }

        @RequiresApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            final Uri uri = request.getUrl();
            //loader_progress.setVisibility(View.VISIBLE);
            view.loadUrl(String.valueOf(uri));
            return true;
        }
    }
}
