package com.mindmesolo.mindme.GettingStarted;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mindmesolo.mindme.R;

import java.util.ArrayList;

/**
 * Created by aman on 9/28/2016.
 */
public class SocialMediaAdapter extends ArrayAdapter<SocialMediaModel> {

    public ArrayList<String> listItems = new ArrayList<String>();

    private ArrayList<SocialMediaModel> contactsList;

    public SocialMediaAdapter(Context context, int textViewResourceId, ArrayList<SocialMediaModel> countryList) {

        super(context, textViewResourceId, countryList);

        this.contactsList = new ArrayList<SocialMediaModel>();

        this.contactsList.addAll(countryList);
    }

    // method to  get Values
    public ArrayList<String> getValueList() {
        return listItems;
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
    public SocialMediaModel getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.social_media_row, null);

            holder = new ViewHolder();

            holder.item_title = (TextView) convertView.findViewById(R.id.socialMediaName);

            holder.item_url = (TextView) convertView.findViewById(R.id.socialMediaUrl);

            holder.social_id = (EditText) convertView.findViewById(R.id.socialMediaId);

            holder.item_Image = (ImageView) convertView.findViewById(R.id.socialMediaImage);

            holder.url_and_id = (LinearLayout) convertView.findViewById(R.id.url_and_id);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final SocialMediaModel contact = contactsList.get(position);

        holder.social_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (contact.getSocialMediaName().equals("Linkedln")) {
                    SocialMedia.text = holder.social_id.getText().toString();
                    SocialMedia.editor = SocialMedia.preferences.edit();
                    SocialMedia.editor.putString("gpref", SocialMedia.text);
                    SocialMedia.editor.commit();
                }
                if (contact.getSocialMediaName().equals("Facebook")) {
                    SocialMedia.text2 = holder.social_id.getText().toString();
                    SocialMedia.editor = SocialMedia.preferences.edit();
                    SocialMedia.editor.putString("gpref2", SocialMedia.text2);
                    SocialMedia.editor.commit();
                }
                if (contact.getSocialMediaName().equals("Twitter")) {
                    SocialMedia.text3 = holder.social_id.getText().toString();
                    SocialMedia.editor = SocialMedia.preferences.edit();
                    SocialMedia.editor.putString("gpref3", SocialMedia.text3);
                    SocialMedia.editor.commit();
                }
                if (contact.getSocialMediaName().equals("Google+")) {
                    SocialMedia.text4 = holder.social_id.getText().toString();
                    SocialMedia.editor = SocialMedia.preferences.edit();
                    SocialMedia.editor.putString("gpref4", SocialMedia.text4);
                    SocialMedia.editor.commit();
                }
                if (contact.getSocialMediaName().equals("Youtube")) {
                    SocialMedia.text5 = holder.social_id.getText().toString();
                    SocialMedia.editor = SocialMedia.preferences.edit();
                    SocialMedia.editor.putString("gpref5", SocialMedia.text5);
                    SocialMedia.editor.commit();
                }
                if (contact.getSocialMediaName().equals("Instagram")) {
                    SocialMedia.text6 = holder.social_id.getText().toString();
                    SocialMedia.editor = SocialMedia.preferences.edit();
                    SocialMedia.editor.putString("gpref6", SocialMedia.text6);
                    SocialMedia.editor.commit();
                }
                if (contact.getSocialMediaName().equals("Pinterest")) {
                    SocialMedia.text7 = holder.social_id.getText().toString();
                    SocialMedia.editor = SocialMedia.preferences.edit();
                    SocialMedia.editor.putString("gpref7", SocialMedia.text7);
                    SocialMedia.editor.commit();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                Log.i("TESTXHANGE", "TESTXHANGE");

                SocialMedia.userChangeProfile = true;
            }
        });
        holder.item_title.setText(contact.getSocialMediaName());
        holder.item_url.setText(contact.getSocialMediaUrl());
        holder.social_id.setText(contact.getSocialMediaId());
        holder.item_Image.setImageResource(contact.getSocialMediaImage());
        holder.social_id.setTag(contact);
        return convertView;
    }

    public class ViewHolder {
        TextView item_url, item_title;

        EditText social_id;

        ImageView item_Image;

        LinearLayout url_and_id;
    }
}
