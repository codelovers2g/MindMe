//package com.mindmesolo.mindme.helper;
//
//import android.app.IntentService;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import com.mindmesolo.mindme.Services.VolleyApi;
//import org.json.JSONArray;
//
///**
// * Created by pc-14 on 4/18/2017.
// */
//
//public class DashboardIntentService extends IntentService {
//
//    SqliteDataBaseHelper sqliteDataBaseHelper ;
//
//    public DashboardIntentService() {
//        super("SimpleIntentService");
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        sqliteDataBaseHelper = new SqliteDataBaseHelper(getApplicationContext());
//        getOrganizationContacts();
//    }
//
//    // Get Organization Contacts from Server
//    private void getOrganizationContacts() {
//
//        String Fetch_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + getOrgId() + "/contacts?code=ACTIVE&size=500&asc=true";
//
//        VolleyApi.getInstance().getJsonArray(getApplicationContext(), Fetch_URL, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
//            @Override
//            public void onCompletion(VolleyApi.ApiResult result) {
//                if (result.success && result.dataIsArray()) {
//                    JSONArray response = result.getDataAsArray();
//                    try {
//                        for (int j = 0; j < response.length(); j++) {
//                            if (response.length() == sqliteDataBaseHelper.numberOfRowsContact()) {
//                                break;
//                            } else {
//                                sqliteDataBaseHelper.insertNewContact(response.getJSONObject(j));
//                            }
//                        }
//                    } catch (Exception e) {
//                        //Log.e(TAG, "VOLLEY  contacts:---->" + e.toString());
//                    }
//                }
//            }
//        });
//    }
//
//    private String getOrgId() {
//        SharedPreferences pref = getApplicationContext().getSharedPreferences("Organization", Context.MODE_PRIVATE);
//        String StoredOrgid = pref.getString("OrgId", null);
//        return StoredOrgid;
//    }
//
//}