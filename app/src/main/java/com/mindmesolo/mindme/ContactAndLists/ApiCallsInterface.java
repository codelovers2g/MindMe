package com.mindmesolo.mindme.ContactAndLists;

import org.json.JSONObject;

/**
 * Created by enest_09 on 11/8/2016.
 */

public interface ApiCallsInterface {

    public JSONObject AddContact_In_List_Interest_Tags(JSONObject data, String type);

    public JSONObject Remove_Contact_From_List_Interest_Tags(JSONObject data, String type);
}
