package com.mindmesolo.mindme.CampaignHelper;

import java.util.ArrayList;

/**
 * Created by eNest on 8/1/2016.
 */
public class Constant {

    public static ArrayList<CampaignPreviewData> data = null;

    public static ArrayList<CampaignPreviewData> getData() {
        return data;
    }

    public static void setData(ArrayList<CampaignPreviewData> data) {
        Constant.data = data;
    }
}
