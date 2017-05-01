package com.mindmesolo.mindme.LeadRoutes;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.mindmesolo.mindme.Services.VolleyApi;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.ToastShow;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pc-14 on 3/2/2017.
 */

public class LeadRouteHelper {

    private static LeadRouteHelper mInstance = new LeadRouteHelper();
    boolean Updated = false;
    String TAG = "LeadRouteHelper";
    //private SessionManager session;
    SqliteDataBaseHelper sqliteDataBaseHelper;

    //Create a singleton
    public static synchronized LeadRouteHelper getInstance() {
        return mInstance;
    }

    public boolean saveRoutes(Context context, String data) {
        sqliteDataBaseHelper = new SqliteDataBaseHelper(context);
        DataHelper.getInstance().startDialog(context, "Please wait.");
        String REGISTER_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + sqliteDataBaseHelper.getOrganizationId() + "";
        VolleyApi.getInstance().putJsonObject(context, REGISTER_URL, data, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                DataHelper.getInstance().stopDialog();
                if (result.success && result.dataIsObject()) {
                    Log.i(TAG, TAG + " Route updated...");
                    ToastShow.setText(context, "Update Successful.", Toast.LENGTH_LONG);
                    Updated = true;
                    JSONObject response = result.getDataAsObject();
                    try {
                        sqliteDataBaseHelper.updateEmailRoute(response.getJSONObject("leadCapture").getJSONObject("emailLeadRoute").toString());
                        sqliteDataBaseHelper.updateTextRoute(response.getJSONObject("leadCapture").getJSONObject("textLeadRoute").toString());
                        sqliteDataBaseHelper.updatePhoneRoute(response.getJSONObject("leadCapture").getJSONObject("phoneLeadRoute").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "Error while updating ..." + TAG);
                }
            }
        });
        return Updated;
    }
}
