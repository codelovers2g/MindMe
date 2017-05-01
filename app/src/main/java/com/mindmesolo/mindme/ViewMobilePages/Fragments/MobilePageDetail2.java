package com.mindmesolo.mindme.ViewMobilePages.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.ViewMobilePages.Helper.ViewMobilePagesModel;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.ToastShow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc-14 on 3/17/2017.
 */

public class MobilePageDetail2 extends Fragment {

    private static boolean _areLecturesLoaded = false;
    private static ViewMobilePagesModel viewMobilePagesModel;

    TextView textViewPageViewCount, textViewLeadCount;
    TextView textViewLinkUrl;
    TextView textViewShareLink, textViewCopyLinkText;
    TextView textViewPageViewDate, textViewLeadCaptureDate;
    LineChart chart;

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mobile_page_detail_two, container, false);

        textViewLinkUrl = (TextView) rootView.findViewById(R.id.textViewLinkUrl);

        textViewLeadCount = (TextView) rootView.findViewById(R.id.textViewLeadCount);

        textViewShareLink = (TextView) rootView.findViewById(R.id.textViewShareLink);

        textViewPageViewCount = (TextView) rootView.findViewById(R.id.textViewPageViewCount);

        textViewPageViewDate = (TextView) rootView.findViewById(R.id.textViewPageViewDate);

        textViewLeadCaptureDate = (TextView) rootView.findViewById(R.id.textViewLeadCaptureDate);

        chart = (LineChart) rootView.findViewById(R.id.chart);

        List<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(100, 100));
        entries.add(new Entry(105, 180));
        entries.add(new Entry(10, 120));

        LineDataSet dataSet = new LineDataSet(entries, "Label");
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // refresh

        textViewShareLink.setOnClickListener((View v) -> {
            shareLink();
        });

        textViewCopyLinkText = (TextView) rootView.findViewById(R.id.textViewCopyLinkText);
        textViewCopyLinkText.setOnClickListener((View v) -> {
            DataHelper.getInstance().setClipboard(getContext(), textViewLinkUrl.getText().toString());
            ToastShow.setText(getContext(), "Text copy success.", Toast.LENGTH_SHORT);
        });
        return rootView;
    }

    private void shareLink() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, textViewLinkUrl.getText().toString());
        startActivity(Intent.createChooser(sharingIntent, "Share Link"));
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromActivity();
    }

    @Override
    public void onDestroy() {
        this.viewMobilePagesModel = null;
        super.onDestroy();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !_areLecturesLoaded) {
            getDataFromActivity();
            _areLecturesLoaded = true;
        }
    }

    private void getDataFromActivity() {
        if (viewMobilePagesModel != null) {
            textViewLinkUrl.setText(viewMobilePagesModel.getMobilePageUrl());
            textViewPageViewCount.setText(String.valueOf(viewMobilePagesModel.getMobilePageOpenCount()));
            textViewLeadCount.setText(String.valueOf(viewMobilePagesModel.getMobilePageLeadCaptureCount()));
//          textViewPageViewDate.setText(viewMobilePagesModel);
//          textViewLeadCaptureDate.setText();
        }
    }

    public void setData(ViewMobilePagesModel data) {
        this.viewMobilePagesModel = data;
    }
}
