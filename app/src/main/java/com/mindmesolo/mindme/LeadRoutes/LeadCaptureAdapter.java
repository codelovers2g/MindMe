package com.mindmesolo.mindme.LeadRoutes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindmesolo.mindme.R;

import java.util.ArrayList;

/**
 * Created by pc-14 on 4/8/2017.
 */

public class LeadCaptureAdapter extends ArrayAdapter<LeadCaptureModel> {
    private ArrayList<LeadCaptureModel> contactsList;

    public LeadCaptureAdapter(Context context, int textViewResourceId, ArrayList<LeadCaptureModel> countryList) {
        super(context, textViewResourceId, countryList);
        this.contactsList = new ArrayList<LeadCaptureModel>();
        this.contactsList.addAll(countryList);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.leadcapturelayout, null);

            holder.LeadTitle = (TextView) convertView.findViewById(R.id.listview_item_title);
            holder.LeadCaptureDate = (TextView) convertView.findViewById(R.id.listview_item_date);
            holder.LeadCount = (TextView) convertView.findViewById(R.id.textnum);
            holder.LeadImage = (ImageView) convertView.findViewById(R.id.listview_image);
            holder.LeadCaptureDate.setVisibility(View.GONE);
            convertView.setTag(holder);

            LeadCaptureModel LeadData = contactsList.get(position);
            holder.LeadTitle.setText(LeadData.getTitle());
            int LeadCount = LeadData.getLeadCount();
            if (LeadCount > 0) {
                holder.LeadCaptureDate.setVisibility(View.VISIBLE);
                holder.LeadCaptureDate.setText(LeadData.getLeadCaptureDate());
            }
            holder.LeadCount.setText(String.valueOf(LeadData.getLeadCount()));
            holder.LeadImage.setImageResource(LeadData.getImage());
            holder.LeadTitle.setTag(LeadData);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    private class ViewHolder {
        TextView LeadTitle, LeadCount, LeadCaptureDate;
        ImageView LeadImage;

    }


}
