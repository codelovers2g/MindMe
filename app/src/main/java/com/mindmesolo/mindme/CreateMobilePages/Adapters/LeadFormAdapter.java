package com.mindmesolo.mindme.CreateMobilePages.Adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mindmesolo.mindme.Models.CommonModel;
import com.mindmesolo.mindme.R;

import java.util.ArrayList;

/**
 * Created by pc-14 on 4/3/2017.
 */

public class LeadFormAdapter extends BaseAdapter {
    public ArrayList<CommonModel> List;
    Context context;
    private LayoutInflater inflater;

    public LeadFormAdapter(Context context, ArrayList<CommonModel> List) {
        this.context = context;
        this.List = new ArrayList<CommonModel>();
        this.List = List;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return List.size();
    }

    @Override
    public CommonModel getItem(int position) {
        return List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_lead_form_row, parent, false);
            final AppCompatCheckBox checkBox = (AppCompatCheckBox) convertView.findViewById(R.id.checkBox1);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            CommonModel person = getItem(position);
            name.setText(person.getTitle());
        }
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
