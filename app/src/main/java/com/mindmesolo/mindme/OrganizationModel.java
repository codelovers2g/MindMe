package com.mindmesolo.mindme;

/**
 * Created by enest_09 on 10/13/2016.
 */

public class OrganizationModel {

    final static String HomeUrl = "https://solo.mindmemobile.com";

    final static String ApiBaseUrl = "https://solo.mindmemobile.com/mindmemobile-web/api/v1/mindmemobile/orgs/";

    final static String ApiUserBaseUrl = "https://solo.mindmemobile.com/mindmemobile-web/api/v1/mindmemobile/users/";

    final static String MindMeReferral = "https://try.mindmesolo.com/app/?sourc=MindMe&referral=App";

    public static String getApiBaseUrl() {
        return ApiBaseUrl;
    }

    public static String getUserBaseUrl() {
        return ApiUserBaseUrl;
    }

    public static String getMindMeReferral() {
        return MindMeReferral;
    }

    public static String getHomeUrl() {
        return HomeUrl;
    }
}
