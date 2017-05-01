package com.mindmesolo.mindme.ViewMobilePages.adapter;//package com.mindmesolo.mindme.Notifcations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.ViewMobilePages.Helper.ViewMobilePagesModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by enest_09 on 12/30/2016.
 */


public class ViewMobilePagesAdapter extends BaseAdapter {

    public List<ViewMobilePagesModel> MobilePages;

    Context context;

    public ViewMobilePagesAdapter(Context context, List<ViewMobilePagesModel> MobilePages) {
        this.MobilePages = MobilePages;
        this.context = context;
    }

    @Override
    public int getCount() {
        return MobilePages.size();
    }

    @Override
    public Object getItem(int position) {
        return MobilePages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.notification_list_layout, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.tv_main_title);
            holder.date = (TextView) convertView.findViewById(R.id.tv_main_date);

            holder.imageView = (ImageView) convertView.findViewById(R.id.dot);
            holder.imageView.setVisibility(View.GONE);

            holder.button = (Button) convertView.findViewById(R.id.btntype);
            holder.button.setVisibility(View.GONE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ViewMobilePagesModel mobilePageModel = MobilePages.get(position);

        holder.title.setText(mobilePageModel.getMobilePageName());

        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy @ hh:mm a");

        final Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(mobilePageModel.getMobilePageCreatedDate());

        holder.date.setText(formatter.format(calendar.getTime()));

        return convertView;
    }

    private class ViewHolder {
        TextView title, date;
        ImageView imageView;
        Button button;
    }
}