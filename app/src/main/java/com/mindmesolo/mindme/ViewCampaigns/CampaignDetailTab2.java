package com.mindmesolo.mindme.ViewCampaigns;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindmesolo.mindme.CampaignHelper.CampaignPreviewData;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.Services.VolleyApi;
import com.mindmesolo.mindme.helper.ConnectionDetector;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.ToastShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by eNest on 7/28/2016.
 */
public class CampaignDetailTab2 extends Fragment {

    final String TAG = "CampaignDetailTab2";

    ArrayList<CampaignPreviewData> Campaigndatanew = new ArrayList<CampaignPreviewData>();

    ListView listView;

    MyCustomAdapter adapter;

    SqliteDataBaseHelper dBhelper;

    ProgressDialog nDialog;

    boolean _areFragmentLoaded = false;

    TextView tv_no_records;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_all_campaign, container, false);

        dBhelper = new SqliteDataBaseHelper(getContext());

        tv_no_records = (TextView) view.findViewById(R.id.tv_no_records);

        listView = (ListView) view.findViewById(R.id.campaign_list_view);

        createListView();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !_areFragmentLoaded) {
            if (new ConnectionDetector(getContext()).isConnectingToInternet()) {

                getOrganizationCampaigns();
            } else {
                ToastShow.setText(getContext(), "please connect to internet", Toast.LENGTH_LONG);
            }
            _areFragmentLoaded = true;
        }
    }

    private void getOrganizationCampaigns() {
        nDialog = new ProgressDialog(getContext());
        nDialog.setMessage("Retrieving latest Campaigns");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();
        String org_id = new SqliteDataBaseHelper(getContext()).getOrganizationId();
        String Fetch_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + org_id + "/campaigns/2017-01-01/now/viewall?size=2000";
        VolleyApi.getInstance().getJsonArray(getContext(), Fetch_URL, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                nDialog.dismiss();
                if (result.success && result.dataIsArray()) {
                    JSONArray response = result.getDataAsArray();
                    CampaignJsonParsing(response);
                }
            }
        });
    }

    private void CampaignJsonParsing(JSONArray responce) {

        try {
            ArrayList<CampaignPreviewData> stringArrayList = dBhelper.getCampaignViewAllData();

            if (responce.length() != stringArrayList.size()) {

                String Email = null, Text = null, Voice = null;

                try {

                    for (int i = 0; i < responce.length(); i++) {

                        JSONObject jsonObjectCampaign = responce.getJSONObject(i);

                        String campaignId = jsonObjectCampaign.getString("campaignId");

                        String campaignName = jsonObjectCampaign.getString("campaignName");

                        String contactsCount = jsonObjectCampaign.getString("contactCount");

                        String createdOn = jsonObjectCampaign.getString("createdOn");

                        JSONArray routes = jsonObjectCampaign.getJSONArray("types");
                        if (routes.length() > 0) {
                            for (int j = 0; j < routes.length(); j++) {
                                JSONObject routesObj = routes.getJSONObject(j);
                                switch (routesObj.getString("routeType")) {
                                    case "EMAIL":
                                    case "OTHER":
                                        Email = routesObj.getString("routeType");
                                        break;
                                    case "TEXT":
                                    case "SMS":
                                        Text = routesObj.getString("routeType");
                                        break;
                                    case "VOICE":
                                    case "PHONE":
                                        Voice = routesObj.getString("routeType");
                                        break;
                                }
                            }
                        }
                        dBhelper.insertCampaignView(campaignId, campaignName, createdOn, contactsCount, Email, Text, Voice, "");
                        Email = null;
                        Text = null;
                        Voice = null;
                        if (responce.length() == stringArrayList.size()) {
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        createListView();
    }

    private void createListView() {

        Campaigndatanew = dBhelper.getCampaignViewAllData();

        Collections.sort(Campaigndatanew, new CustomComparator());

        Collections.reverse(Campaigndatanew);

        adapter = new MyCustomAdapter(getContext(), R.layout.view_all_campaign_row, Campaigndatanew);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String CampaignId = adapter.contactsList.get(position).getCampaignId();
                String CampaignName = adapter.contactsList.get(position).getCampaignName();
                Intent intent = new Intent(getContext(), CampaignGetAllDetail.class);
                intent.putExtra("CampaignId", CampaignId);
                intent.putExtra("CampaignName", CampaignName);
                startActivity(intent);
            }
        });

        if (adapter.getCount() == 0) {

            tv_no_records.setVisibility(View.VISIBLE);

        } else {

            tv_no_records.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class MyCustomAdapter extends ArrayAdapter<CampaignPreviewData> {

        private ArrayList<CampaignPreviewData> contactsList;

        public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<CampaignPreviewData> countryList) {
            super(context, textViewResourceId, countryList);
            this.contactsList = new ArrayList<CampaignPreviewData>();
            this.contactsList.addAll(countryList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy @ hh:mm a");
            Calendar calendar = Calendar.getInstance();
            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.view_all_campaign_row, null);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.title);
                holder.createdate = (TextView) convertView.findViewById(R.id.date_status);
                holder.count = (TextView) convertView.findViewById(R.id.count);
                holder.EmailImage = (ImageView) convertView.findViewById(R.id.emailImage);
                holder.TextImage = (ImageView) convertView.findViewById(R.id.smsImage);
                holder.VoiceImage = (ImageView) convertView.findViewById(R.id.VoiceImage);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            CampaignPreviewData campaignPreviewData = contactsList.get(position);
            String name = campaignPreviewData.getCampaignName();

            try {
                String date = campaignPreviewData.getCampaignCreatedOn();
                calendar.setTimeInMillis(Long.parseLong(date));
            } catch (Exception e) {
                Log.e(TAG, "Fial to parse date of Campaign Name --->" + name);
            }

            String createDate = formatter.format(calendar.getTime());

            String count = campaignPreviewData.getCount();
            holder.name.setText(name);

            holder.createdate.setText(createDate);

            holder.count.setText(count);

            holder.EmailImage.setVisibility(View.GONE);

            holder.VoiceImage.setVisibility(View.GONE);

            holder.TextImage.setVisibility(View.GONE);

            if (campaignPreviewData.getEmailImage() != null) {
                holder.EmailImage.setVisibility(View.VISIBLE);
            }
            if (campaignPreviewData.getCallImage() != null) {
                holder.VoiceImage.setVisibility(View.VISIBLE);
            }
            if (campaignPreviewData.getTextImage() != null) {
                holder.TextImage.setVisibility(View.VISIBLE);
            }
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        private class ViewHolder {
            RelativeLayout layout;
            TextView name;
            TextView createdate;
            TextView count;
            ImageView EmailImage, VoiceImage, TextImage;
        }
    }

    public class CustomComparator implements Comparator<CampaignPreviewData> {
        @Override
        public int compare(CampaignPreviewData o1, CampaignPreviewData o2) {
            return o1.getCampaignCreatedOn().compareTo(o2.getCampaignCreatedOn());
        }
    }

    // The definition of our task class
    private class PostTask extends AsyncTask<String, Integer, String> {
        ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(getContext());
            nDialog.setMessage("Retrieving latest Campaigns");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String org_id = new SqliteDataBaseHelper(getContext()).getOrganizationId();
            String Fetch_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + org_id + "/campaigns/2017-01-01/now/viewall?size=2000";
            VolleyApi.getInstance().getJsonArray(getContext(), Fetch_URL, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
                @Override
                public void onCompletion(VolleyApi.ApiResult result) {
                    if (result.success && result.dataIsArray()) {
                        JSONArray response = result.getDataAsArray();
                        CampaignJsonParsing(response);
                    }
                }
            });
            return "All Done!";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            nDialog.dismiss();
            createListView();
        }
    }
}





