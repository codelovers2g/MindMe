package com.mindmesolo.mindme.GettingStarted;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindmesolo.mindme.MobilePageHelper.ListViewItem;
import com.mindmesolo.mindme.R;

import java.util.ArrayList;

/**
 * Created by enest_09 on 9/7/2016.
 */

public class GettingStartAdapter extends ArrayAdapter<ListViewItem> {

    private ArrayList<ListViewItem> contactsList;

    public GettingStartAdapter(Context context, int textViewResourceId, ArrayList<ListViewItem> countryList) {
        super(context, textViewResourceId, countryList);
        this.contactsList = new ArrayList<ListViewItem>();
        this.contactsList.addAll(countryList);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Nullable
    @Override
    public ListViewItem getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.list_view_geeting_started_tools_settings, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.listview_item_title);
            holder.listview_image = (ImageView) convertView.findViewById(R.id.listview_image);
            holder.checkboxStatus = (ImageView) convertView.findViewById(R.id.checkboxStatus);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ListViewItem contact = contactsList.get(position);
        holder.name.setText(contact.getName());
        holder.listview_image.setImageResource(contact.getImage());
        if (contact.ischecked()) {
            holder.checkboxStatus.setVisibility(View.VISIBLE);
        }
        if (!contact.ischecked()) {
            holder.checkboxStatus.setVisibility(View.GONE);
        }
        holder.checkboxStatus.setTag(contact);
        return convertView;
    }

    private class ViewHolder {
        ImageView listview_image;
        ImageView checkboxStatus;
        TextView name;
        TextView count;
        CheckBox id;
    }
}






