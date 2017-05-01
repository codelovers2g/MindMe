package com.mindmesolo.mindme.ToolsAndSettings;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mindmesolo.mindme.ContactAndLists.AddTag;
import com.mindmesolo.mindme.ContactAndLists.ContactView;
import com.mindmesolo.mindme.MobilePageHelper.ListViewItem;
import com.mindmesolo.mindme.OrganizationModel;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.ToastShow;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by eNest on 8/22/2016.
 */
public class ToolsSettingsTags extends AppCompatActivity {

    private static boolean popupIsVisible = false;
    MyCustomAdapter socialMediaAdpater;
    ListView tools_settings_listview;
    ImageView fab;
    ArrayList<ListViewItem> arrayList;
    String ActivityTag = "ToolsSettingsTags";
    TextView title_of_page;
    SqliteDataBaseHelper dBhelper = new SqliteDataBaseHelper(this);
    DataHelper dataHelper = new DataHelper();
    PopupMenu popup;
    View.OnClickListener fabBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                Intent intent = new Intent(getBaseContext(), AddTag.class);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.tools_settings_tags);

        title_of_page = (TextView) findViewById(R.id.title_of_page);

        title_of_page.setText("Tags");

        tools_settings_listview = (ListView) findViewById(R.id.list_view);

        fab = (ImageView) findViewById(R.id.fab);

        fab.setOnClickListener(fabBtnClickListener);

        setUpUiComponents();

    }

    private void setUpUiComponents() {
        arrayList = new ArrayList<ListViewItem>();
        arrayList = dBhelper.getTagNameIdLength();
        socialMediaAdpater = new MyCustomAdapter(getBaseContext(), R.layout.tools_settings_tags_row, arrayList);
        tools_settings_listview.setAdapter(socialMediaAdpater);
        tools_settings_listview.setFocusable(false);
    }

    private void deleteTagById(ListViewItem TagId) {
        final String tagid = TagId.getId();
        final String tagName = TagId.getName();
        showProgressDialog("Deleting " + tagName);
        final String token = GetApiAccess();
        String org_id = dBhelper.getOrganizationId();
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        JsonObjectRequest JsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, OrganizationModel.getApiBaseUrl() + org_id + "/tags/" + tagid + "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dBhelper.removeTagById(tagid);
                        setUpUiComponents();
                        pDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String json = null;
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 400:
                                    json = new String(response.data);
                                    json = dataHelper.trimMessage(json, "message");
                                    if (json != null) dataHelper.displayMessage(json);
                                    break;
                            }
                            pDialog.dismiss();
                            ToastShow.setText(getBaseContext(), "oops something went wrong. try again", Toast.LENGTH_LONG);
                        }
                    }
                }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Basic " + token);
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        JsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(JsonObjectRequest);
    }

    public String GetApiAccess() {
        SharedPreferences pref1 = getBaseContext().getSharedPreferences("UserLogin", Context.MODE_PRIVATE);
        String email = pref1.getString("Email", null);
        String password = pref1.getString("Password", null);
        String token = email + ":" + password;
        byte[] data = null;
        try {
            data = token.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        final String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        final String finalToken = base64.replace("\n", "");
        return finalToken;
    }

    private void showProgressDialog(String dialogMsg) {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(dialogMsg);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpUiComponents();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (popupIsVisible) {
            popup.dismiss();
        }
    }

    //confirm if user wants remove Tag
    private void removeTagConfirm(final ListViewItem TagId) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ToolsSettingsTags.this);
        alertDialog.setMessage("Are you sure want to remove this Tag?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteTagById(TagId);
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private class MyCustomAdapter extends ArrayAdapter<ListViewItem> {
        private ArrayList<ListViewItem> contactsList;

        public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<ListViewItem> countryList) {

            super(context, textViewResourceId, countryList);

            this.contactsList = new ArrayList<ListViewItem>();
            this.contactsList.addAll(countryList);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.tools_settings_tags_row, null);

                holder.name = (TextView) convertView.findViewById(R.id.m_pages_title);

                holder.count = (TextView) convertView.findViewById(R.id.m_pages_count);

                holder.popupmenu = (ImageView) convertView.findViewById(R.id.m_pages_image);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.popupmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.m_pages_image:
                            popup = new PopupMenu(v.getContext(), v);
                            popupIsVisible = true;
                            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    int id = item.getItemId();
                                    if (id == R.id.actionUpdate) {
                                        popupIsVisible = false;
                                        Intent intent = new Intent(getBaseContext(), UpdateTags.class);
                                        intent.putExtra("TagId", contactsList.get(position).getId());
                                        intent.putExtra("TagName", contactsList.get(position).getName());
                                        startActivity(intent);
                                    }
                                    if (id == R.id.actionDelete) {
                                        popupIsVisible = false;

                                        removeTagConfirm(contactsList.get(position));
                                        notifyDataSetChanged();
                                    }
                                    return true;
                                }
                            });
                            popup.show();
                            break;

                        default:
                            break;
                    }
                }
            });
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!contactsList.get(position).getLength().equals("0")) {
                        Bundle dataBundle = new Bundle();
                        dataBundle.putString("Contactid", contactsList.get(position).getContactid());
                        dataBundle.putString("Name", contactsList.get(position).getName());
                        Intent intent = new Intent(getBaseContext(), ContactView.class);
                        intent.putExtras(dataBundle);
                        startActivity(intent);
                    } else {
                        ToastShow.setText(getBaseContext(), "Please add Contacts in " + contactsList.get(position).getName(), Toast.LENGTH_LONG);
                    }
                }
            });
            ListViewItem contact = contactsList.get(position);
            holder.name.setText(contact.getName());
            holder.count.setText(contact.getLength());
            holder.popupmenu.setTag(contact);


            return convertView;
        }

        private class ViewHolder {
            TextView name, count;
            ImageView popupmenu;
        }
    }

}