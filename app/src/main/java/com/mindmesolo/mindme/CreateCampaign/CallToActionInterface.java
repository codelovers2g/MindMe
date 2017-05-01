package com.mindmesolo.mindme.CreateCampaign;

import org.json.JSONObject;

/**
 * Created by enest_09 on 11/11/2016.
 */

public interface CallToActionInterface {
    void yesNoMaybe(JSONObject object);

    void pollItems(JSONObject object);

    void feedBack(JSONObject object);

    void callToAction(JSONObject object);
}
