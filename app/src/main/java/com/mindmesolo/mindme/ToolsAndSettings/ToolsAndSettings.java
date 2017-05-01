package com.mindmesolo.mindme.ToolsAndSettings;
/**
 * Created by eNest on 5/16/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mindmesolo.mindme.GettingStarted.BusinessHours;
import com.mindmesolo.mindme.GettingStarted.Greeting_and_forwarding;
import com.mindmesolo.mindme.GettingStarted.ImportContactType;
import com.mindmesolo.mindme.GettingStarted.Profile_settings;
import com.mindmesolo.mindme.GettingStarted.SocialMedia;
import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.R;

import java.util.ArrayList;

public class ToolsAndSettings extends MainActivity {

    MyCustomAdapter myCustomAdapter;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.tools_settings, frameLayout, false);
        drawer.addView(contentView, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        // new code
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        // changes

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearlayout);
        linearLayout.setVisibility(View.GONE);

        View view = (View) findViewById(R.id.view);
        view.setVisibility(View.GONE);

        MainActivityObject = true;

        ArrayList<Object> arrayList = getListItems();

        myCustomAdapter = new MyCustomAdapter(getBaseContext(), arrayList);

        ListView androidListView = (ListView) findViewById(R.id.list_view);
        androidListView.setAdapter(myCustomAdapter);

        androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(getBaseContext(), Profile_settings.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(getBaseContext(), ContactTypes.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(getBaseContext(), BusinessHours.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(getBaseContext(), ToolsSettingsLists.class);
                        startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(getBaseContext(), ToolsSettingsInterests.class);
                        startActivity(intent4);
                        break;
                    case 5:
                        Intent intent5 = new Intent(getBaseContext(), ToolsSettingsTags.class);
                        startActivity(intent5);
                        break;
                    case 7:
                        Intent intent7 = new Intent(getBaseContext(), ToolsAndSettingsEmail.class);
                        startActivity(intent7);
                        break;
                    case 8:
                        Intent intent8 = new Intent(getBaseContext(), Greeting_and_forwarding.class);
                        startActivity(intent8);
                        break;
                    case 9:
                        Intent intent9 = new Intent(getBaseContext(), RestrictionsToolsSettings.class);
                        startActivity(intent9);
                        break;
                    case 10:
                        Intent intent11 = new Intent(getBaseContext(), ImportContactType.class);
                        intent11.putExtra("NextActivity", "IMPORTCONTACTS");
                        startActivity(intent11);
                        break;
                    case 11:
                        Intent intent12 = new Intent(getBaseContext(), SocialMedia.class);
                        startActivity(intent12);
                        break;
                    case 13:
                        Intent intent13 = new Intent(getBaseContext(), Resetpassword.class);
                        startActivity(intent13);
                        break;
                    case 14:
                        break;
                }
            }
        });
    }

    private ArrayList<Object> getListItems() {
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add(new toolsModel("Profile", R.drawable.account_icon));
        arrayList.add(new toolsModel("Contact Types", R.drawable.contact_type_icon));
        arrayList.add(new toolsModel("Business Hours", R.drawable.clock_icon));
        arrayList.add(new toolsModel("Lists", R.drawable.lists_icon));
        arrayList.add(new toolsModel("Interests", R.drawable.interests_icon));
        arrayList.add(new toolsModel("Tags", R.drawable.tags_icon));
        arrayList.add("section");
        arrayList.add(new toolsModel("Email", R.drawable.email));
        arrayList.add(new toolsModel("Phone Greeting & Forwarding", R.drawable.phone_greeting_icon));
        //arrayList.add(new toolsModel("Restrictions", R.drawable.restrictions_icon));
        arrayList.add("section");
        arrayList.add(new toolsModel("Import Contacts", R.drawable.import_contacts_icon));
        arrayList.add(new toolsModel("Social Media", R.drawable.social_media_icon));
        arrayList.add("section");
        arrayList.add(new toolsModel("Reset Password", R.drawable.reset_password_icon));
        //arrayList.add(new toolsModel("Mobile Pages", R.drawable.mobile_pages));
        //arrayList.add(new toolsModel("Terms and Conditions", R.drawable.terms_icon));
        return arrayList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivityObject = true;
    }

    public class MyCustomAdapter extends BaseAdapter {
        private static final int TYPE_PERSON = 0;
        private static final int TYPE_DIVIDER = 1;
        private ArrayList<Object> personArray;
        private LayoutInflater inflater;

        public MyCustomAdapter(Context context, ArrayList<Object> personArray) {
            this.personArray = personArray;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return personArray.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return personArray.get(position);
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (getItem(position) instanceof toolsModel) {
                return TYPE_PERSON;
            }
            return TYPE_DIVIDER;
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
                        convertView = inflater.inflate(R.layout.tools_settings_list_view, parent, false);
                        break;
                    case TYPE_DIVIDER:
                        convertView = inflater.inflate(R.layout.tools_settings_section, parent, false);
                        break;
                }
            }

            switch (type) {
                case TYPE_PERSON:
                    toolsModel person = (toolsModel) getItem(position);
                    ImageView image = (ImageView) convertView.findViewById(R.id.listview_image);
                    TextView name = (TextView) convertView.findViewById(R.id.listview_item_title);
                    image.setImageResource(person.getImage());
                    name.setText(person.getName());
                    break;
                case TYPE_DIVIDER:
                    break;
            }
            return convertView;
        }
    }

    private class toolsModel {
        String name;
        int image;

        toolsModel(String name, int imageResource) {
            this.name = name;
            this.image = imageResource;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
