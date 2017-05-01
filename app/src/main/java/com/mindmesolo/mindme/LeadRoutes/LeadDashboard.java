package com.mindmesolo.mindme.LeadRoutes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mindmesolo.mindme.ContactAndLists.Summary;
import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.Contacts;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import java.util.ArrayList;


/**
 * Created by User1 on 9/16/2016.
 */

public class LeadDashboard extends MainActivity {
    private static final String TAG = "LeadDashboard";
    ListView list;
    SqliteDataBaseHelper dBhelper;
    EditText searchView;
    MyCustomAdapter dataAdapter = null;
    DataHelper dataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.leaddashboard, frameLayout, false);

        drawer.addView(contentView, 0);

        list = (ListView) findViewById(R.id.listView);

        searchView = (EditText) findViewById(R.id.searchview);


        dBhelper = new SqliteDataBaseHelper(this);


        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                contentView.getWindowVisibleDisplayFrame(r);
                int screenHeight = contentView.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                Log.d(TAG, "keypadHeight = " + keypadHeight);

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                } else {
                    // keyboard is closed
                }
            }
        });

        displayListView();
    }

    private void displayListView() {
        ArrayList<Contacts> List = new ArrayList<Contacts>();
        final ArrayList<String> array_list = dBhelper.getfirstnamecontact();
        final ArrayList<String> arrayListcontid = dBhelper.getcontactid();
        final ArrayList<String> arrayListImages = dBhelper.getContactImage();
        for (int i = 0; i < array_list.size(); i++) {
            String name = array_list.get(i);
            String image = arrayListImages.get(i);
            Bitmap bitmap = dataHelper.decodeBase64(image);
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.contactsicon);
            }
            Contacts contacts = new Contacts(name, bitmap);
            List.add(contacts);
        }

        dataAdapter = new MyCustomAdapter(getBaseContext(), R.layout.recentlistlayout, List);

        list.setAdapter(dataAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int id_To_Search = position + 1;

                String Contactid = String.valueOf(arrayListcontid.get(position));

                Bundle dataBundle = new Bundle();

                dataBundle.putString("contactid", Contactid);

                Intent intent = new Intent(getBaseContext(), Summary.class);

                intent.putExtras(dataBundle);

                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        displayListView();
        searchView.setText("");
        searchView.clearFocus();
    }

    //---------------------CUSTOM ADAPTER FOR LIST VIEW ----------//
    private class MyCustomAdapter extends ArrayAdapter<Contacts> {

        private ArrayList<Contacts> contact;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Contacts> contactd) {
            super(context, textViewResourceId, contactd);
            this.contact = new ArrayList<Contacts>();
            this.contact.addAll(contactd);
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
            Contacts contacts = contact.get(position);
            holder.name.setText(contacts.getName());
            holder.imageView.setImageBitmap(contacts.getImage());
            return convertView;

        }

        private class ViewHolder {
            TextView name;
            ImageView imageView;
        }
    }
}