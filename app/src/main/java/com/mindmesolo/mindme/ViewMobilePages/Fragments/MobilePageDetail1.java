package com.mindmesolo.mindme.ViewMobilePages.Fragments;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.ViewMobilePages.Helper.ViewMobilePagesModel;
import com.tuyenmonkey.mkloader.MKLoader;


/**
 * Created by pc-14 on 3/17/2017.
 */

public class MobilePageDetail1 extends Fragment {

    private static boolean _areLecturesLoaded = false;
    private static ViewMobilePagesModel viewMobilePagesModel;
    View rootView;
    WebView webView;
    MKLoader loader_progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.web_view_for_mobile_pager, container, false);
        webView = (WebView) rootView.findViewById(R.id.webView);
        loader_progress = (MKLoader) rootView.findViewById(R.id.loader_progress);

        Log.i("FragmentData", "On CreateView  ");

        if (_areLecturesLoaded) {
            getDataFromActivity();
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("FragmentData", "On Resume ");
        getDataFromActivity();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !_areLecturesLoaded) {
            //getDataFromActivity();
            _areLecturesLoaded = true;
        }
    }

    private void getDataFromActivity() {
        Log.i("FragmentData", "getDataFromActivity ");
        if (viewMobilePagesModel != null && _areLecturesLoaded == true && webView != null) {
            webView.getSettings().setLoadsImagesAutomatically(true);
            //webView.getSettings().setJavaScriptEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.setWebViewClient(new myWebClient());
            webView.loadUrl(viewMobilePagesModel.getMobilePageUrl());
            webView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
    }

    public void setData(ViewMobilePagesModel data) {
        this.viewMobilePagesModel = data;
    }

    @Override
    public void onDetach() {
        this.viewMobilePagesModel = null;
        super.onDetach();
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

}
