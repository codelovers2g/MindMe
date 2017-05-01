package com.mindmesolo.mindme.CreateMobilePages.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mindmesolo.mindme.CreateMobilePages.models.BusinessHours;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.DataHelper;

import java.util.ArrayList;

/**
 * Created by pc-14 on 3/2/2017.
 */

public class HoursAdapter extends ArrayAdapter<BusinessHours> {

    public ArrayList<BusinessHours> contactsList;

    Context context;

    public HoursAdapter(Context context, int textViewResourceId, ArrayList<BusinessHours> countryList) {
        super(context, textViewResourceId, countryList);
        this.context = context;
        this.contactsList = new ArrayList<BusinessHours>();
        this.contactsList.addAll(countryList);
    }

    @Override
    public BusinessHours getItem(int position) {
        return contactsList.get(position);
    }

    @Override
    public int getCount() {
        return contactsList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.business_hour_row, null);
            holder = new ViewHolder();
            holder.dayName = (TextView) convertView.findViewById(R.id.txt);
            holder.time = (TextView) convertView.findViewById(R.id.incomingdatastatus);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BusinessHours mediaItem = contactsList.get(position);
        holder.dayName.setText(DataHelper.getInstance().getDayOfWeek(mediaItem.getDayOfWeek()));
        holder.time.setText(DataHelper.getInstance().getTime(mediaItem));
        holder.dayName.setTag(mediaItem);
        return convertView;
    }

    private class ViewHolder {
        TextView dayName;
        TextView time;
    }
}

