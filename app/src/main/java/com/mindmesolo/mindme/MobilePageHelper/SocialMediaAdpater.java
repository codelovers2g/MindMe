package com.mindmesolo.mindme.MobilePageHelper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.mindmesolo.mindme.R;

import java.util.ArrayList;

/**
 * Created by eNest on 8/18/2016.
 */
public class SocialMediaAdpater extends ArrayAdapter {

    public ArrayList<ListViewItem> ListItems = new ArrayList<ListViewItem>();
    int currentPosition;
    View.OnClickListener MyClickListrner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            final ListViewItem ClickedItem = (ListViewItem) v.getTag();
            switch (v.getId()) {
                case R.id.m_pages_image:

                    PopupMenu popup = new PopupMenu(v.getContext(), v);

                    popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int id = item.getItemId();
                            if (id == R.id.actionUpdate) {

                            }
                            if (id == R.id.actionDelete) {
                                ListItems.remove(ClickedItem);
                            }
                            return true;
                        }
                    });

                    /** Showing the popup menu */
                    popup.show();

                    break;
                case R.id.m_pages_count:
                case R.id.m_pages_title:


                default:
                    break;
            }
        }
    };


    public SocialMediaAdpater(Context context, int resource, ArrayList<ListViewItem> ListItems) {
        super(context, resource, ListItems);
        this.ListItems = ListItems;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        ListViewItem listViewItem = ListItems.get(position);

        int listViewItemType = listViewItem.getType();

        if (convertView == null) {
            switch (listViewItemType) {
                case 0:
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.m_pages_social_media_row, null);
                    TextView name = (TextView) convertView.findViewById(R.id.m_pages_social_title);
                    ImageView logoimage = (ImageView) convertView.findViewById(R.id.m_pages_social_logo);
                    CheckBox CheckBox = (CheckBox) convertView.findViewById(R.id.m_pages_social_checkbox);
                    viewHolder = new ViewHolder(name, logoimage, CheckBox);
                    convertView.setTag(viewHolder);

                    ListViewItem items = ListItems.get(position);
                    viewHolder.getName().setText(items.getText());
                    viewHolder.getImage().setImageResource(items.getImage());
                    viewHolder.getCheckbox().setChecked(items.ischecked());
                    break;
                case 1:
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.m_pages_social_media_row_button, null);
                    TextView name1 = (TextView) convertView.findViewById(R.id.m_pages_social_title);
                    ImageView logoimage2 = (ImageView) convertView.findViewById(R.id.m_pages_social_logo);
                    Button button = (Button) convertView.findViewById(R.id.m_pages_social_button);
                    viewHolder = new ViewHolder(name1, logoimage2, button);
                    convertView.setTag(viewHolder);

                    ListViewItem items1 = ListItems.get(position);
                    viewHolder.getName().setText(items1.getText());
                    viewHolder.getImage().setImageResource(items1.getImage());
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i("Button", "ButtonClicked");
                        }
                    });
                case 3:
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragmentsrow, null);
                    TextView name3 = (TextView) convertView.findViewById(R.id.listview_item_title);
                    CheckBox checkbox3 = (CheckBox) convertView.findViewById(R.id.CheckBox01);
                    viewHolder = new ViewHolder(name3, checkbox3);
                    convertView.setTag(viewHolder);

                    ListViewItem items2 = ListItems.get(position);
                    viewHolder.getName().setText(items2.getText());
                    viewHolder.getCheckbox().setChecked(items2.ischecked());
                    break;
                case 4:
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.tools_settings_tags_row, null);
                    TextView title = (TextView) convertView.findViewById(R.id.m_pages_title);
                    TextView count = (TextView) convertView.findViewById(R.id.m_pages_count);
                    ImageView image = (ImageView) convertView.findViewById(R.id.m_pages_image);
                    viewHolder = new ViewHolder(title, count, image);
                    convertView.setTag(viewHolder);
                    ListViewItem items4 = ListItems.get(position);
                    viewHolder.getName().setText(items4.getName());
                    viewHolder.getCount().setText(items4.getLength());
                    viewHolder.getImage().setTag(items4);
                    viewHolder.getName().setOnClickListener(MyClickListrner);
                    viewHolder.getCount().setOnClickListener(MyClickListrner);
                    viewHolder.getImage().setOnClickListener(MyClickListrner);

                    break;
            }

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    public class ViewHolder {
        TextView name, count;
        ImageView Image;
        CheckBox checkbox;
        Button button;

        public ViewHolder(TextView name, ImageView Image, CheckBox checkbox) {
            this.name = name;
            this.Image = Image;
            this.checkbox = checkbox;
        }

        public ViewHolder(TextView name, TextView count, ImageView Image) {
            this.name = name;
            this.count = count;
            this.Image = Image;
        }

        public ViewHolder(TextView name, CheckBox checkbox) {
            this.name = name;
            this.checkbox = checkbox;
        }

        public ViewHolder(TextView name, ImageView Image, Button button) {
            this.name = name;
            this.Image = Image;
            this.button = button;
        }

        public TextView getCount() {
            return count;
        }

        public void setCount(TextView count) {
            this.count = count;
        }

        public CheckBox getCheckbox() {
            return checkbox;
        }

        public void setCheckbox(CheckBox checkbox) {
            this.checkbox = checkbox;
        }

        public ImageView getImage() {
            return Image;
        }

        public void setImage(ImageView image) {
            Image = image;
        }

        public TextView getName() {
            return name;
        }

        public void setName(TextView name) {
            this.name = name;
        }

        public Button getButton() {
            return button;
        }

        public void setButton(Button button) {
            this.button = button;
        }
    }

}