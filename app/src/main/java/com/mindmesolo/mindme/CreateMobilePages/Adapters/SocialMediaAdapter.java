package com.mindmesolo.mindme.CreateMobilePages.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindmesolo.mindme.GettingStarted.SocialMediaModel;
import com.mindmesolo.mindme.R;

import java.util.ArrayList;

/**
 * Created by pc-14 on 2/28/2017.
 */

public class SocialMediaAdapter extends ArrayAdapter<SocialMediaModel> implements Filterable {
    public ArrayList<SocialMediaModel> contactsList;
    Context context;

    public SocialMediaAdapter(Context context, int textViewResourceId, ArrayList<SocialMediaModel> countryList) {
        super(context, textViewResourceId, countryList);
        this.context = context;
        this.contactsList = new ArrayList<SocialMediaModel>();
        this.contactsList.addAll(countryList);
    }

    @Override
    public SocialMediaModel getItem(int position) {
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
        SocialMediaAdapter.ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.layout_social_media_mobile_pages, null);
            holder = new ViewHolder();

            holder.socialMediaImage = (ImageView) convertView.findViewById(R.id.socialMediaImage);

            holder.name = (TextView) convertView.findViewById(R.id.name);

            holder.id = (CheckBox) convertView.findViewById(R.id.checkBox1);

            convertView.setTag(holder);
            holder.id.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    SocialMediaModel contacts = (SocialMediaModel) cb.getTag();
                    contacts.setChecked(cb.isChecked());
                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SocialMediaModel mediaItem = contactsList.get(position);
        holder.socialMediaImage.setImageResource(mediaItem.getSocialMediaImage());
        holder.name.setText(mediaItem.getSocialMediaName());
        holder.id.setChecked(mediaItem.isChecked());
        holder.id.setTag(mediaItem);
        return convertView;
    }

    private class ViewHolder {
        ImageView socialMediaImage;
        TextView name;
        CheckBox id;
    }
}
