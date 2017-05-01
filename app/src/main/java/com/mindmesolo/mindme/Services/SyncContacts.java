package com.mindmesolo.mindme.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import org.json.JSONArray;

/**
 * Created by pc-14 on 4/25/2017.
 */

public class SyncContacts extends Service {

    final String TAG = "SyncContacts";

    SqliteDataBaseHelper sqliteDataBaseHelper;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sqliteDataBaseHelper = new SqliteDataBaseHelper(this);
        getOrganizationContacts();
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {


        return null;
    }


    // Get Organization Contacts from Server
    private void getOrganizationContacts() {

        Log.e(TAG, "Service start Call");

        String Fetch_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + sqliteDataBaseHelper.getOrganizationId() + "/contacts?code=ACTIVE&size=10&asc=true";

        VolleyApi.getInstance().getJsonArray(getBaseContext(), Fetch_URL, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                if (result.success && result.dataIsArray()) {
                    JSONArray response = result.getDataAsArray();
                    Log.e(TAG, "Service start Result");
                    try {
                        for (int j = 0; j < response.length(); j++) {
                            if (response.length() == sqliteDataBaseHelper.numberOfRowsContact()) {
                                break;
                            } else {
                                sqliteDataBaseHelper.insertNewContact(response.getJSONObject(j));
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "VOLLEY  contacts:---->" + e.toString());
                    }
                }
            }
        });
    }

}
