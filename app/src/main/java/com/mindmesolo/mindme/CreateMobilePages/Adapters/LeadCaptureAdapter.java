package com.mindmesolo.mindme.CreateMobilePages.Adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindmesolo.mindme.Models.CommonModel;
import com.mindmesolo.mindme.R;

import java.util.ArrayList;

/**
 * Created by enest_09 on 11/3/2016.
 */

public class LeadCaptureAdapter extends BaseAdapter {

    private static final int TYPE_PERSON = 0;
    private static final int TYPE_DIVIDER = 1;
    public ArrayList<CommonModel> ArrayList;
    private LayoutInflater inflater;

    public LeadCaptureAdapter(Context context, ArrayList<CommonModel> personArray) {
        this.ArrayList = personArray;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return ArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public CommonModel getItem(int position) {
        return ArrayList.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getTitle().equals("Section")) {
            return TYPE_DIVIDER;
        }
        return TYPE_PERSON;
    }

    @Override
    public boolean isEnabled(int position) {
        return (getItemViewType(position) == TYPE_PERSON);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_PERSON:
                    convertView = inflater.inflate(R.layout.layout_contact_row, parent, false);
                    break;
                case TYPE_DIVIDER:
                    convertView = inflater.inflate(R.layout.section_devider, parent, false);
                    break;
            }
        }
        switch (type) {
            case TYPE_PERSON:
                CommonModel person = getItem(position);
                final AppCompatCheckBox checkBox = (AppCompatCheckBox) convertView.findViewById(R.id.checkBox1);
                TextView name = (TextView) convertView.findViewById(R.id.name);
                ImageView required_button = (ImageView) convertView.findViewById(R.id.required_button);
                name.setText(person.getTitle());
                if (person.getRequired()) {
                    required_button.setVisibility(View.VISIBLE);
                } else required_button.setVisibility(View.GONE);

                checkBox.setChecked(person.getChecked());
                checkBox.setTag(person);
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppCompatCheckBox cb = (AppCompatCheckBox) v;
                        CommonModel contacts = (CommonModel) cb.getTag();
                        contacts.setChecked(cb.isChecked());
                    }
                });
                break;
            case TYPE_DIVIDER:
                TextView SectionName = (TextView) convertView.findViewById(R.id.SectionTitle);
                SectionName.setText(getItem(position).getMessage());
                break;
        }
        return convertView;
    }
}
