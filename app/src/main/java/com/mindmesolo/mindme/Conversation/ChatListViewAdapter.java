package com.mindmesolo.mindme.Conversation;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindmesolo.mindme.R;

import java.util.List;

/**
 * Created by enest_09 on 11/1/2016.
 */

public class ChatListViewAdapter extends BaseAdapter {

    Context context;
    private List<ApplicationInfo> mAppList;

    ChatListViewAdapter(Context context, List<ApplicationInfo> mAppList) {
        this.mAppList = mAppList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mAppList.size();
    }

    @Override
    public ApplicationInfo getItem(int position) {
        return mAppList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        // menu type count
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        // current menu type
        return position % 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context,
                    R.layout.conversation_chat_row, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        ApplicationInfo item = getItem(position);
        holder.images.setImageDrawable(item.loadIcon(context.getPackageManager()));
        holder.user_name.setText(item.loadLabel(context.getPackageManager()));
        return convertView;
    }

    class ViewHolder {
        ImageView images;
        TextView user_name;

        public ViewHolder(View view) {
            images = (ImageView) view.findViewById(R.id.images);
            user_name = (TextView) view.findViewById(R.id.user_name);
            view.setTag(this);
        }
    }
}

