package com.mindmesolo.mindme.helper;

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

public class CommonListViewAdapter extends BaseAdapter {
    public ArrayList<CommonModel> List;
    Context context;
    private LayoutInflater inflater;

    public CommonListViewAdapter(Context context, ArrayList<CommonModel> List) {
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
            convertView = inflater.inflate(R.layout.layout_contact_row, parent, false);
            final AppCompatCheckBox checkBox = (AppCompatCheckBox) convertView.findViewById(R.id.checkBox1);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            CommonModel person = getItem(position);
            name.setText(person.getTitle());
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
        }
        return convertView;
    }
}