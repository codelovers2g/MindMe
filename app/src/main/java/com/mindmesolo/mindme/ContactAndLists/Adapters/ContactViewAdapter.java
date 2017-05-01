package com.mindmesolo.mindme.ContactAndLists.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.Contacts;
import com.mindmesolo.mindme.helper.DataHelper;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by enest_09 on 2/1/2017.
 */

public class ContactViewAdapter extends ArrayAdapter<Contacts> {
    public ArrayList<Contacts> contactsList;
    public ArrayList<Contacts> arraylist;
    public Context context;

    public ContactViewAdapter(Context context, int textViewResourceId, ArrayList<Contacts> countryList) {
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
            convertView = vi.inflate(R.layout.recentlistlayout, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.txt);
            holder.imageView = (ImageView) convertView.findViewById(R.id.images);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Contacts contacts = contactsList.get(position);
        holder.name.setText(contacts.getName());

        String Image = contacts.getImageBase64();

        Bitmap bitmap = null;

        if (Image != null && Image.length() > 10) {
            bitmap = new DataHelper().decodeBase64(Image);
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.contactsicon);
        }
        holder.imageView.setImageBitmap(bitmap);

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
        TextView name;
        ImageView imageView;
    }
}
