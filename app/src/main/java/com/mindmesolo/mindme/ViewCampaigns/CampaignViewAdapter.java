package com.mindmesolo.mindme.ViewCampaigns;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.Contacts;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by enest_09 on 2/1/2017.
 */

public class CampaignViewAdapter extends ArrayAdapter<Contacts> {

    public ArrayList<Contacts> contactsList;

    public ArrayList<Contacts> arraylist;

    private Context context;


    public CampaignViewAdapter(Context context, int textViewResourceId, ArrayList<Contacts> countryList) {
        super(context, textViewResourceId, countryList);
        this.context = context;
        contactsList = countryList;
        this.arraylist = new ArrayList<Contacts>();
        this.arraylist.addAll(contactsList);
    }

    @Override
    public Contacts getItem(int position) {
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
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.campaign_list_layout, null);
            holder = new ViewHolder();
            holder.ItemName = (TextView) convertView.findViewById(R.id.txt);
            holder.ItemCont = (TextView) convertView.findViewById(R.id.textnum);
            holder.progressStatus = (ProgressBar) convertView.findViewById(R.id.progressStatus);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Contacts contacts = contactsList.get(position);

        String Name = contacts.getName();
        holder.ItemName.setText(Name);
        holder.ItemCont.setText(contacts.getCount());
        holder.progressStatus.setProgress(contacts.getProgress());
        Log.i("Progress", Name + ": " + String.valueOf(contacts.getProgress()));
        if (contacts.getProgress() > 0) {
            //holder.progressStatus.setVisibility(View.VISIBLE);
            //holder.ItemName.setLayoutParams(new RelativeLayout.LayoutParams(120, ViewGroup.LayoutParams.WRAP_CONTENT));
            switch (Name) {
                case "Yes":
                    holder.progressStatus.setProgressDrawable(context.getResources().getDrawable(R.drawable.style_progress_bar_green));
                    break;
                case "No":
                    holder.progressStatus.setProgressDrawable(context.getResources().getDrawable(R.drawable.style_progress_bar_no));
                    break;
                case "Maybe":
                    holder.progressStatus.setProgressDrawable(context.getResources().getDrawable(R.drawable.style_progress_bar_maybe));
                    break;
                case "Plays":
                    holder.progressStatus.setProgressDrawable(context.getResources().getDrawable(R.drawable.style_progress_bar_plays));
                    break;
                case "Opens":
                    holder.progressStatus.setProgressDrawable(context.getResources().getDrawable(R.drawable.style_progress_bar_open));
                    break;
                default:
                    holder.progressStatus.setProgressDrawable(context.getResources().getDrawable(R.drawable.style_progress_bar_green));
                    break;
            }
        } else {
            //holder.progressStatus.setVisibility(View.GONE);
            //holder.ItemName.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        return convertView;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        contactsList.clear();
        if (charText.length() == 0) {
            contactsList.addAll(arraylist);
        } else {
            for (Contacts wp : arraylist) {
                if (wp.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    contactsList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView ItemName;
        TextView ItemCont;
        ProgressBar progressStatus;
    }
}