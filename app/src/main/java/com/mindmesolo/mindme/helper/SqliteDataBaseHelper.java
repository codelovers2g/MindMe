package com.mindmesolo.mindme.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mindmesolo.mindme.CampaignHelper.CampaignPreviewData;
import com.mindmesolo.mindme.ContactAndLists.Models.Addresses;
import com.mindmesolo.mindme.ContactAndLists.Models.Phones;
import com.mindmesolo.mindme.ContactAndLists.Models.SimpleContact;
import com.mindmesolo.mindme.CreateMobilePages.models.BusinessHours;
import com.mindmesolo.mindme.MobilePageHelper.ListViewItem;
import com.mindmesolo.mindme.Notifcations.NotificationModal;
import com.mindmesolo.mindme.ViewMobilePages.Helper.ViewMobilePagesModel;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by User1 on 6/3/2016.
 */
public class SqliteDataBaseHelper extends SQLiteOpenHelper {

    // Database Name
    public static final String DATABASE_NAME = "Organiztion.db";

    // Table Names
    public static final String CONTACTS_ORG_TABLENAME = "USERS";
    public static final String CONTACTS_INTEREST_TABLE = "INTERESTS";
    public static final String CONTACTS_LISTS_TABLE = "LISTS";
    public static final String CONTACTS_TAGS_TABLE = "TAGS";
    public static final String CONTACTS_NOTIFICATION_TABLE = "NOTIFICATION";
    public static final String CONTACT_MAIN_TABLE = "CONTACTS";
    public static final String MOBILE_PAGES_MAIN_TABLE = "MOBILEPAGES";

    // USER Table - column nmaes
    public static final String CONTACTS_column_ORG_ID = "id";
    public static final String CONTACTS_ORG_CREATED_ON = "createdOn";
    public static final String CONTACTS_ORG_UPDATED_ON = "UpdatedOn";
    public static final String CONTACTS_ORG_LEGALNAME = "legalName";
    public static final String CONTACTS_ORG_NAME = "name";
    public static final String CONTACTS_ORG_EMAIL = "email";
    public static final String CONTACTS_ORG_ORGINALEMAIL = "originalEmail";
    public static final String CONTACTS_ORG_TYPE = "organizationType";
    public static final String CONTACTS_ORG_ENTITYTYPE = "organizationEntityType";
    public static final String CONTACTS_ORG_INDUSTRYTYPE = "industryType";
    public static final String CONTACTS_ORG_REGNO = "registrationNumber";
    public static final String CONTACTS_ORG_TAXNO = "taxNumber";
    public static final String CONTACTS_ORG_SITEURL = "siteUrl";
    public static final String CONTACTS_ORG_SITEPROVIDER = "siteProvider";
    public static final String CONTACTS_ORG_FBURL = "facebookUrl";
    public static final String CONTACTS_ORG_LIURL = "linkedInUrl";
    public static final String CONTACTS_ORG_TWURL = "twitterUrl";
    public static final String CONTACTS_ORG_FBID = "facebookId";
    public static final String CONTACTS_ORG_LIID = "linkedInId";
    public static final String CONTACTS_ORG_TWID = "twitterId";
    public static final String CONTACTS_ORG_ID = "orgid";
    public static final String CONTACTS_ORG_PLANID = "planId";
    public static final String CONTACTS_ORG_APIKEY = "apiKey";
    public static final String CONTACTS_ORG_TWILIOID = "twilioAccountSid";
    public static final String CONTACTS_ORG_TWILIOTOKEN = "twilioAuthToken";
    public static final String CONTACTS_ORG_GOOGLEKEY = "googleAnalyticsKey";
    public static final String CONTACTS_ORG_DEMO = "demoCompany";
    public static final String CONTACTS_ORG_PHONE_NUMBER = "phoneNumber";
    public static final String CONTACTS_ORG_EMAIL_LEAD_ROUTE = "emailLeadRoute";
    public static final String CONTACTS_ORG_TEXT_LEAD_ROUTE = "textLeadRoute";
    public static final String CONTACTS_ORG_PHONE_LEAD_ROUTE = "phoneLeadRoute";
    public static final String CONTACT_ORG_USER = "user";
    public static final String CONTACTS_ORG_CONTACTS_TYPE = "contactsTypes";
    public static final String CONTACTS_ORG_RESTRICTION = "restriction";
    public static final String CONTACTS_ORG_STATUS = "status";

    // INTEREST Table - column names
    public static final String CONTACTS_column_INTERESRT_ID = "id";
    public static final String CONTACTS_INTEREST_CREATED_ON = "createdOn";
    public static final String CONTACTS_INTEREST_UPDATED_ON = "UpdatedOn";
    public static final String CONTACTS_INTEREST_ID = "Itid";
    public static final String CONTACTS_INTEREST_CODE = "code";
    public static final String CONTACTS_INTEREST_NAME = "name";
    public static final String CONTACTS_INTEREST_STATUS = "status";
    public static final String CONTACTS_INTEREST_CONT_LEN = "length";
    public static final String CONTACTS_INTEREST_CONTACTID = "contactid";

    // LISTS Table - column names
    public static final String CONTACTS_column_LISTS_ID = "id";
    public static final String CONTACTS_LISTS_CREATED_ON = "createdOn";
    public static final String CONTACTS_LISTS_UPDATED_ON = "UpdatedOn";
    public static final String CONTACTS_LISTS_ID = "Lid";
    public static final String CONTACTS_LISTS_CODE = "code";
    public static final String CONTACTS_LISTS_NAME = "name";
    public static final String CONTACTS_LISTS_STATUS = "status";
    public static final String CONTACTS_LISTS_CONT_LEN = "length";
    public static final String CONTACTS_LISTS_CONTACTID = "contactid";

    // TAGSS Table - column names
    public static final String CONTACTS_column_TAGS_ID = "id";
    public static final String CONTACTS_TAGS_CREATED_ON = "createdOn";
    public static final String CONTACTS_TAGS_UPDATED_ON = "UpdatedOn";
    public static final String CONTACTS_TAGS_ID = "Tid";
    public static final String CONTACTS_TAGS_CODE = "code";
    public static final String CONTACTS_TAGS_NAME = "name";
    public static final String CONTACTS_TAGS_STATUS = "status";
    public static final String CONTACTS_TAGS_CONT_LEN = "length";
    public static final String CONTACTS_TAGS_CONTACTID = "contactid";

    // Contacts Table - column names
    public static final String CONTACTS_ID = "id";
    public static final String CONTACTS_CREATED_ON = "createdOn";
    public static final String CONTACTS_UPDATED_ON = "UpdatedOn";
    public static final String CONTACTS_FIRSTNAME = "firstName";
    public static final String CONTACTS_MIDDLENAME = "middleName";
    public static final String CONTACTS_LASTNAME = "lastName";
    public static final String CONTACTS_SKYPE_USERNAME = "skypeUserName";
    public static final String CONTACTS_GENDER = "gender";
    public static final String CONTACTS_DATE_OF_BIRTH = "dateOfBirth";
    public static final String CONTACTS_EMAIL_ADDRESS = "emailAddress";
    public static final String CONTACTS_STATUS = "status";
    public static final String CONTACTS_CONT_ID = "contactid";
    public static final String CONTACTS_COMPANY_NAME = "companyName";
    public static final String CONTACTS_SITEURL = "siteUrl";
    public static final String CONTACTS_FBURL = "facebookUrl";
    public static final String CONTACTS_LIURL = "linkedInUrl";
    public static final String CONTACTS_TWURL = "twitterUrl";
    public static final String CONTACTS_PHOTO = "photo";
    public static final String CONTACTS_CONTACT_CODE = "contactCode";
    public static final String CONTACTS_CONTACT_OPT_IN = "contactOptIn";
    public static final String CONTACTS_ORGAN_ID = "orgId";
    public static final String CONTACT_FAVORITE = "favorite";
    public static final String CONTACTS_ADDRESS1 = "addressLine1";
    public static final String CONTACTS_ADDRESS2 = "addressLine2";
    public static final String CONTACTS_ADDRESS3 = "addressLine3";
    public static final String CONTACTS_ADDRESS4 = "addressLine4";
    public static final String CONTACTS_PINCODE = "postalCode";
    public static final String CONTACT_PHONE_NUMBER = "phoneNumber";
    public static final String CONTACTS_PHONE_TYPE = "phoneType";
    public static final String CONTACTS_NOTES = "notes";
    public static final String CONTACT_LIST_IDS = "listIds";
    public static final String CONTACT_INTERESTS_IDS = "interestIds";
    public static final String CONTACT_TAG_IDS = "tagIds";

    // Campaign Contact Table - column names
    public static final String CAMPAIGN_ID = "id";
    public static final String CAMPAIGN_CONTACTS_ID = "cid";
    public static final String CAMPAIGN_CONTACTS_CODE = "code";
    public static final String CAMPAIGN_CONTACTS_GROUP_ID = "groupid";
    //------------------------------------- made by srn ----------------------------------//
    //Mobile pages ListViewItems
    public static final String MOBILE_PAGE_ID = "mobilePageId";
    public static final String MOBILE_PAGE_NAME = "mobilePageName";
    public static final String MOBILE_PAGE_URL = "mobilePageUrl";
    public static final String MOBILE_PAGE_OPEN_COUNT = "mobilePageOpenCount";
    public static final String MOBILE_PAGE_CREATE_DATE = "mobilePageCreatedDate";
    public static final String MOBILE_PAGE_LEAD_CAPTURE_COUNT = "mobilePageLeadCaptureCount";

    //USER PROFILE TABEL
    public static final String PROFILE_CONTACTS_LASTUPDATE = "LastUpdate";
    public static final String PROFILE_CONTACTS_ID = "id";
    public static final String PROFILE_CONTACTS_U_ID = "Uid";
    public static final String PROFILE_CONTACTS_ACCOUNT_NAME = "AccountName";
    public static final String PROFILE_CONTACTS_FIRSTNAME = "FirstName";
    public static final String PROFILE_CONTACTS_LASTNAME = "LastName";
    public static final String PROFILE_CONTACTS_MOBILE_NO = "MobileNo";
    public static final String PROFILE_CONTACTS_BUSINESS_NO = "BusinessNo";
    public static final String PROFILE_CONTACTS_HOME_NO = "HomeNo";
    public static final String PROFILE_CONTACTS_EMAILID = "EmailId";
    public static final String PROFILE_CONTACTS_TIMEZONE = "TimeZone";
    public static final String PROFILE_CONTACTS_ADDRESS_STREET = "Street";
    public static final String PROFILE_CONTACTS_ADDRESS_TOWN = "TownCity";
    public static final String PROFILE_CONTACTS_ADDRESS_PIN = "PinCode";
    public static final String PROFILE_CONTACTS_ADDRESS_STATE = "State";
    public static final String PROFILE_CONTACTS_ADDRESS_COUNTRY = "Country";
    public static final String PROFILE_CONTACTS_IMAGELOGO = "LogoImage";
    public static final String PROFILE_CONTACTS_API = "apnsToken";

    //(id integer primary key,dayOfWeek integer ,openOnHour integer,openOnMinute integer,closeOnHour integer,closeOnMinute integer,byAppointmentOnly integers)"
    public static final String BUS_CONTACTS_ID = "id";
    public static final String BUS_CONTACTS_DAY_OF_WEEK = "dayOfWeek";
    public static final String BUS_CONTACTS_OPEN_HOUR = "openOnHour";
    public static final String BUS_CONTACTS_OPEN_MIN = "openOnMinute";
    public static final String BUS_CONTACTS_CLOSE_HOUR = "closeOnHour";
    public static final String BUS_CONTACTS_CLOSE_MIN = "closeOnMinute";
    public static final String BUS_CONTACTS_BY_APPOINTMENT = "byAppointmentOnly";
    public static final String BUS_CONTACTS_CLOSE_STATUS = "close";

    //(id integer primary key,IMC_id text unique,mobile integer,name text,email text,photo text,address text)"
    public static final String IMP_CONTACTS_ID = "id";
    public static final String IMP_CONTACTS_IMC_id = "IMC_id";
    public static final String IMP_CONTACTS_MOBILE = "mobile";
    public static final String IMP_CONTACTS_TYPE = "type";
    public static final String IMP_CONTACTS_NAME = "name";
    public static final String IMP_CONTACTS_EMAIL = "email";
    public static final String IMP_CONTACTS_PHOTO = "photo";
    public static final String IMP_CONTACTS_ADDRESS = "address";

    //Greeting and forwording
    public static final String G_ID = "id";
    public static final String G_MINDME_NO = "mindMenu";
    public static final String G_MEDIA_URL = "mediaUrl";
    public static final String G_TEXT_TO_VOICE = "textToVoice";
    public static final String G_ENABLE = "gEnable";
    public static final String F_RINGS = "fRing";
    public static final String F_U_NAME = "name";
    public static final String F_U_PHONE = "phone";
    public static final String F_ENABLE = "fEnable";

    //campaign preview data
    public static final String CAMPAIGN_VIEW_ID = "CV_id";
    public static final String CAMPAIGN_VIEW_NAME = "CV_name";
    public static final String CAMPAIGN_VIEW_DATE = "CV_date";
    public static final String CAMPAIGN_VIEW_COUNT = "CV_count";
    public static final String CAMPAIGN_VIEW_EMAIL = "CV_email";
    public static final String CAMPAIGN_VIEW_TEXT = "CV_text";
    public static final String CAMPAIGN_VIEW_VOICE = "CV_voice";
    public static final String CAMPAIGN_VIEW_ACTIVITIES = "CV_activities";

    // NOTIFICATION Table - column names
    public static final String NOTIFICATION_column_TAGS_ID = "id";
    public static final String NOTIFICATION_TITLE = "title";
    public static final String NOTIFICATION_MESSAGE = "message";
    public static final String NOTIFICATION_SOURCE = "source";
    public static final String NOTIFICATION_READ = "read";
    public static final String NOTIFICATION_READ_DATE = "readdate";
    public static final String NOTIFICATION_COMMENT = "comment";
    public static final String NOTIFICATION_ID = "Nid";
    public static final String NOTIFICATION_ORGID = "orgid";
    public static final String NOTIFICATION_CONTACTID = "contactid";
    public static final String NOTIFICATION_TYPE = "type";
    public static final String NOTIFICATION_DATE = "date";
    public static final String NOTIFICATION_UPDATE_DATE = "updatedate";
    public static final String NOTIFICATION_DELETED = "deleted";
    public static final String NOTIFICATION_MEDIA = "media";

    //socialmedia
    public static final String SOCIAL_ID = "id";
    public static final String SOCIAL_UPDATEED_ON = "updateOn";
    public static final String SOCIAL_NAME = "mediaName";
    public static final String SOCIAL_MEDIA_URL = "mediaUrl";
    public static final String SOCIAL_QNIQUE_ID = "mediaId";

    // Database Version
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "DbHelper";


    public SqliteDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table INTERESTS" +
                        "(id integer primary key, Itid text unique, code text,name text,status text,length integer,contactid text)"
        );
        db.execSQL(
                "create table LISTS" +
                        "(id integer primary key,Lid text unique, code text,name text,status text,length integer,contactid text)"
        );
        db.execSQL(
                "create table TAGS" +
                        "(id integer primary key,Tid text unique, code text,name text,status text,length integer,contactid text)"
        );
        db.execSQL(
                "create table CONTACTS" +
                        "(id integer primary key, createdOn DATETIME, UpdatedOn DATETIME, firstName text, middleName text, lastName text, skypeUserName text, gender text," +
                        "dateOfBirth text, emailAddress text, contactid text unique, status text, companyName text, siteUrl text," +
                        "facebookUrl text, linkedInUrl text, twitterUrl text, photo text, contactCode text,contactOptIn text, orgId text, favorite text," +
                        "addressLine1 text, addressLine2 text, addressLine3 text, addressLine4 text, postalCode text, phoneType text, notes text, phoneNumber text,listIds text,interestIds text,tagIds text)"

        );
        db.execSQL(
                "create table USERS" +
                        "(id integer primary key, name text, legalName text, email text, originalEmail text, organizationType text," +
                        "organizationEntityType text, industryType text, registrationNumber text, taxNumber text, siteUrl text, siteProvider text," +
                        "facebookUrl text, linkedInUrl text, twitterUrl text, facebookId text,linkedInId text, twitterId text, orgid text unique, planId text," +
                        " apiKey text, twilioAccountSid text, twilioAuthToken text, googleAnalyticsKey text, demoCompany text," +
                        "phoneNumber text, emailLeadRoute text, textLeadRoute text, phoneLeadRoute text, user text, contactsTypes text, restriction text, status text)"
        );

        db.execSQL(
                "create table NOTIFICATION" +
                        "(id integer primary key, title text, message text, source text, read text, readdate text, comment text," +
                        " Nid text unique, orgid text, contactid text, type text, date text, updatedate text, deleted text, media text)"
        );

        db.execSQL(
                "create table IMPORTCONTACTS" +
                        "(id integer primary key,IMC_id text,mobile text,type text,name text,email text,photo text,address text)"
        );
        db.execSQL(
                "create table BUSINESSHOURS" +
                        "(id integer primary key,dayOfWeek integer unique,openOnHour integer,openOnMinute integer,closeOnHour integer,closeOnMinute integer,byAppointmentOnly text,close text)"
        );
        db.execSQL(
                "create table APPUSERPROFILE" +
                        "(LastUpdate text,id integer unique primary key,Uid text,AccountName text,FirstName text,LastName text,MobileNo text,BusinessNo text,HomeNo text,EmailId text,TimeZone text,Street text,TownCity text,PinCode text,State text,Country text,LogoImage text,apnsToken text)"
        );
        db.execSQL(
                "create table CAMPAIGNCONTACT" +
                        "(id integer primary key,cid text,code text,groupid text)"
        );
        db.execSQL(
                "create table CAMPAIGNVIEW" +
                        "(id integer primary key,CV_id text unique,CV_name text,CV_date text,CV_count text,CV_email text,CV_text text,CV_voice text, CV_activities text)"
        );

        db.execSQL(
                "create table GREETINGANDFORWARDING" +
                        "(id integer unique primary key,mindMenu text,mediaUrl text,textToVoice text,gEnable text,fRing text,name text,phone text,fEnable text)"
        );

        db.execSQL(
                "create table SOCIALMEDIA" +
                        "(id integer primary key,updateOn text,mediaName text,mediaUrl text,mediaId text)"
        );
        db.execSQL(
                "create table MOBILEPAGES" +
                        "(Id integer unique primary key,mobilePageId text unique," +
                        "mobilePageName text," +
                        "mobilePageUrl text," +
                        "mobilePageOpenCount text," +
                        "mobilePageCreatedDate text," +
                        "mobilePageLeadCaptureCount text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS INTERESTS");
        db.execSQL("DROP TABLE IF EXISTS LISTS");
        db.execSQL("DROP TABLE IF EXISTS TAGS");
        db.execSQL("DROP TABLE IF EXISTS CONTACTS");
        db.execSQL("DROP TABLE IF EXISTS USERS");
        db.execSQL("DROP TABLE IF EXISTS IMPORTCONTACTS");
        db.execSQL("DROP TABLE IF EXISTS BUSINESSHOURS");
        db.execSQL("DROP TABLE IF EXISTS APPUSERPROFILE");
        db.execSQL("DROP TABLE IF EXISTS CAMPAIGNCONTACT");
        db.execSQL("DROP TABLE IF EXISTS CAMPAIGNVIEW");
        db.execSQL("DROP TABLE IF EXISTS GREETINGANDFORWARDING");
        db.execSQL("DROP TABLE IF EXISTS MOBILEPAGES");
        onCreate(db);
    }

    //-----------------------all delete operations----------//

    public void DeleteBusiness() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("Delete from BUSINESSHOURS");
    }

    public void DeleteImportContacts() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("Delete from IMPORTCONTACTS");
    }

    public void DeleteUserProfile() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("Delete from APPUSERPROFILE");
    }

    public void DeleteCampaignView() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("Delete from  CAMPAIGNVIEW");
    }

    public void DeleteInterests() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("Delete from INTERESTS");
    }

    public void DeleteTags() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("Delete from TAGS");
    }

    public void DeleteLists() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("Delete from LISTS");
    }

    public void DeleteContacts() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("Delete from CONTACTS");
    }

    public void DeleteOrganization() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("Delete from USERS");
    }

    public void DeleteImportContactData() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("Delete from IMPORTCONTACTS");
    }

    public void DeletePhoneGreetingData() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("Delete from GREETINGANDFORWARDING");
    }

    public void DeleteSocialMediaData() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("Delete from SOCIALMEDIA");
    }

    public void DeleteNotifications() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("Delete from NOTIFICATION");
    }

    public void DeleteMobilePages() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("Delete from MOBILEPAGES");
    }

    //-----------------------------------Created  By SRN-----------------------------------------------------------//
    public ArrayList<String> getContactsBetweenRange(String where, long start_date, long end_date) {
        ArrayList<String> lists_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CONTACTS WHERE " + where + "  BETWEEN '" + start_date + "' AND '" + end_date + "'", null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            lists_list.add(cursor.getString(cursor.getColumnIndex(CONTACTS_CONT_ID)));
            cursor.moveToNext();
        }
        return lists_list;
    }
//-----------------this method is made for if you want to get the detailof contacts with Contact id-------------//

    public ArrayList<String> getEmailContacts(ArrayList contacts) {
        ArrayList<String> Contacts = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        for (int i = 0; i < contacts.size(); i++) {
            Cursor cursor = db.rawQuery("SELECT * FROM CONTACTS WHERE contactid = ?", new String[]{contacts.get(i) + ""});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                String count = cursor.getString(cursor.getColumnIndex(CONTACTS_EMAIL_ADDRESS));
                if ((!count.equals("null")) && (!count.equals(""))) {
                    Contacts.add(cursor.getString(cursor.getColumnIndex(CONTACTS_CONT_ID)));
                }
            }
        }
        return Contacts;
    }

    public ArrayList<String> getVoiceContacts(ArrayList contacts) {
        ArrayList<String> Contacts = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        for (int i = 0; i < contacts.size(); i++) {
            Cursor cursor = db.rawQuery("SELECT * FROM CONTACTS WHERE (phoneType = \"HOME\" OR  phoneType =\"MOBILE\")  AND " + CONTACTS_CONT_ID + " = ?", new String[]{contacts.get(i) + ""});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                String count = cursor.getString(cursor.getColumnIndex(CONTACT_PHONE_NUMBER));
                if ((!count.equals("null")) && (!count.equals(""))) {
                    Contacts.add(cursor.getString(cursor.getColumnIndex(CONTACTS_CONT_ID)));
                }
            }
        }
        return Contacts;
    }

    public ArrayList<String> getTextContacts(ArrayList contacts) {
        ArrayList<String> Contacts = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        for (int i = 0; i < contacts.size(); i++) {
            Cursor cursor = db.rawQuery("SELECT * FROM CONTACTS WHERE phoneType = \"MOBILE\" AND " + CONTACTS_CONT_ID + " = ?", new String[]{contacts.get(i) + ""});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                String count = cursor.getString(cursor.getColumnIndex(CONTACT_PHONE_NUMBER));
                if ((!count.equals("null")) && (!count.equals(""))) {
                    Contacts.add(cursor.getString(cursor.getColumnIndex(CONTACTS_CONT_ID)));
                }
            }
        }
        return Contacts;
    }

    //Mobile pages tables
    public boolean insertIntoMobilePages(JSONObject MobilePage) {

        Log.i(TAG, MobilePage.toString());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        try {
            contentValues.put(MOBILE_PAGE_ID, MobilePage.getString("mobilePageId"));
            contentValues.put(MOBILE_PAGE_NAME, MobilePage.getString("mobilePageName"));
            contentValues.put(MOBILE_PAGE_URL, MobilePage.getString("mobilePageUrl"));
            contentValues.put(MOBILE_PAGE_OPEN_COUNT, MobilePage.getString("mobilePageOpenCount"));
            contentValues.put(MOBILE_PAGE_CREATE_DATE, MobilePage.getString("mobilePageCreatedDate"));
            contentValues.put(MOBILE_PAGE_LEAD_CAPTURE_COUNT, MobilePage.getString("mobilePageLeadCaptureCount"));
            db.insert("MOBILEPAGES", null, contentValues);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    public List<ViewMobilePagesModel> getAllMobilePagesData() {
        List<ViewMobilePagesModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from MOBILEPAGES", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            ViewMobilePagesModel viewMobilePagesModel = new ViewMobilePagesModel();
            viewMobilePagesModel.setMobilePageId(res.getString(res.getColumnIndex(MOBILE_PAGE_ID)));
            viewMobilePagesModel.setMobilePageName(res.getString(res.getColumnIndex(MOBILE_PAGE_NAME)));
            viewMobilePagesModel.setMobilePageUrl(res.getString(res.getColumnIndex(MOBILE_PAGE_URL)));
            viewMobilePagesModel.setMobilePageOpenCount(res.getInt(res.getColumnIndex(MOBILE_PAGE_OPEN_COUNT)));
            viewMobilePagesModel.setMobilePageCreatedDate(res.getLong(res.getColumnIndex(MOBILE_PAGE_CREATE_DATE)));
            viewMobilePagesModel.setMobilePageLeadCaptureCount(res.getInt(res.getColumnIndex(MOBILE_PAGE_LEAD_CAPTURE_COUNT)));
            arrayList.add(viewMobilePagesModel);
            res.moveToNext();
        }
        return arrayList;
    }

    public int mobilePagesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from MOBILEPAGES", null);
        return res.getCount();
    }

    //-----------------Last Update ---10-13-2016---------made by srn-------------------------//
    public boolean insertInterests(String id, String code, String name, String status, int length, String contid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_INTEREST_ID, id);
        contentValues.put(CONTACTS_INTEREST_CODE, code);
        contentValues.put(CONTACTS_INTEREST_NAME, name);
        contentValues.put(CONTACTS_INTEREST_STATUS, status);
        contentValues.put(CONTACTS_INTEREST_CONT_LEN, length);
        contentValues.put(CONTACTS_INTEREST_CONTACTID, contid);
        db.insert("INTERESTS", null, contentValues);
        return true;
    }

    public void insertInterestsJsonObj(JSONObject ResponceData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < 1; i++) {
            try {
                JSONArray jsonArray = ResponceData.getJSONArray("interests");
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                    JSONArray jsonArray1 = jsonObject1.getJSONArray("contactIds");
                    final String jsonstr = jsonArray1.toString();
                    int len = jsonArray1.length();
                    contentValues.put(CONTACTS_INTEREST_ID, jsonObject1.getString("id"));
                    contentValues.put(CONTACTS_INTEREST_CODE, jsonObject1.getString("code"));
                    contentValues.put(CONTACTS_INTEREST_NAME, jsonObject1.getString("name"));
                    contentValues.put(CONTACTS_INTEREST_STATUS, jsonObject1.getString("status"));
                    contentValues.put(CONTACTS_INTEREST_CONT_LEN, len);
                    contentValues.put(CONTACTS_INTEREST_CONTACTID, jsonstr);
                    db.insert("INTERESTS", null, contentValues);
                }
            } catch (Exception e) {
                // textView.setText(e.toString());
            }
        }
    }

    public ArrayList<String> getAllInterestNames() {
        ArrayList<String> Intrest_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from INTERESTS", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            Intrest_list.add(res.getString(res.getColumnIndex(CONTACTS_INTEREST_NAME)));
            res.moveToNext();
        }
        return Intrest_list;
    }

    public ArrayList<String> getAllInterestInLeadForm() {
        ArrayList<String> Intrest_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from INTERESTS", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            Intrest_list.add(res.getString(res.getColumnIndex(CONTACTS_INTEREST_NAME)));
            res.moveToNext();
        }
        return Intrest_list;
    }

    public ArrayList<String> getAllInterestId() {
        ArrayList<String> lists_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from INTERESTS", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            lists_list.add(res.getString(res.getColumnIndex(CONTACTS_INTEREST_ID)));
            res.moveToNext();
        }
        return lists_list;
    }

    public int getNumOfRowsInterests() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_INTEREST_TABLE);
        return numRows;
    }

    public int numberOfRowsInterestContactId(String id) {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;
        cursor = db.rawQuery("select * from INTERESTS WHERE contactid LIKE '%' || ? || '%'", new String[]{id + ""});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            count = cursor.getCount();

        }

        return count;
    }

    public int updateInterestContactsAndLength(String Id, String contid, int len) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CONTACTS_INTEREST_CONTACTID, contid);
        values.put(CONTACTS_INTEREST_CONT_LEN, len);
        return db.update("INTERESTS", values, CONTACTS_INTEREST_ID + " = ?", new String[]{Id});
    }

    public ArrayList<ListViewItem> getInterestNameIdLength() {
        ArrayList<ListViewItem> tagItmes = new ArrayList<ListViewItem>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from INTERESTS", null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String id = cursor.getString(cursor.getColumnIndex(CONTACTS_INTEREST_ID));
            String name = cursor.getString(cursor.getColumnIndex(CONTACTS_INTEREST_NAME));
            String length = cursor.getString(cursor.getColumnIndex(CONTACTS_INTEREST_CONT_LEN));
            String contactid = cursor.getString(cursor.getColumnIndex(CONTACTS_INTEREST_CONTACTID));
            ListViewItem item = new ListViewItem(id, name, length, contactid, 4);
            tagItmes.add(item);
            cursor.moveToNext();
        }
        return tagItmes;
    }

    public ArrayList<String> getAllInterestLength() {
        ArrayList<String> lists_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from INTERESTS", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            lists_list.add(res.getString(res.getColumnIndex(CONTACTS_INTEREST_CONT_LEN)));
            res.moveToNext();
        }
        return lists_list;
    }

    public String getInterestsContacts(String id) {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String interestcontactid = "";
        cursor = db.rawQuery("SELECT contactid FROM INTERESTS WHERE Itid=?", new String[]{id + ""});

        if (cursor.getCount() > 0) {

            cursor.moveToFirst();
            interestcontactid = cursor.getString(cursor.getColumnIndex(CONTACTS_INTEREST_CONTACTID));
        }

        return interestcontactid;
    }

    public int updateInterestById(String Id, String Name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CONTACTS_INTEREST_NAME, Name);
        return db.update("INTERESTS", values, CONTACTS_INTEREST_ID + " = ?", new String[]{Id});
    }

    public String getInterestsContactsById(String Id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Contacts = null;
        Cursor cursor = db.rawQuery("SELECT * FROM INTERESTS WHERE Itid=?", new String[]{Id + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Contacts = cursor.getString(cursor.getColumnIndex(CONTACTS_INTEREST_CONTACTID));
        }
        return Contacts;
    }

    public String getInterestIdFromName(String Name) {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String listsname = "";
        cursor = db.rawQuery("SELECT * FROM INTERESTS WHERE name=?", new String[]{Name + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            listsname = cursor.getString(cursor.getColumnIndex(CONTACTS_INTEREST_ID));
        }
        return listsname;
    }

    public void removeInterestById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(CONTACTS_INTEREST_TABLE, CONTACTS_INTEREST_ID + " = ?", new String[]{id});
    }

    public String getAllInterestContactIds(String id) {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String interestcontactid = "";
        cursor = db.rawQuery("SELECT * FROM INTERESTS WHERE Itid=?", new String[]{id + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            interestcontactid = cursor.getString(cursor.getColumnIndex(CONTACTS_INTEREST_CONTACTID));
        }
        return interestcontactid;
    }

    //------------------------------------- "List -Table -Methods" --------------------------------------------------//
    public boolean insertNewList(String id, String code, String name, String status, int length, String contactid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_LISTS_ID, id);
        contentValues.put(CONTACTS_LISTS_CODE, code);
        contentValues.put(CONTACTS_LISTS_NAME, name);
        contentValues.put(CONTACTS_LISTS_STATUS, status);
        contentValues.put(CONTACTS_LISTS_CONT_LEN, length);
        contentValues.put(CONTACTS_LISTS_CONTACTID, contactid);
        db.insert("LISTS", null, contentValues);
        return true;
    }

    public void insertIntoLists(JSONObject ResponseData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try {
            JSONArray jsonArray = ResponseData.getJSONArray("lists");
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                JSONArray ContactsInList = jsonObject1.getJSONArray("contactIds");
                contentValues.put(CONTACTS_LISTS_ID, jsonObject1.getString("id"));
                contentValues.put(CONTACTS_LISTS_CODE, jsonObject1.getString("code"));
                contentValues.put(CONTACTS_LISTS_NAME, jsonObject1.getString("name"));
                contentValues.put(CONTACTS_LISTS_STATUS, jsonObject1.getString("status"));
                contentValues.put(CONTACTS_LISTS_CONT_LEN, ContactsInList.length());
                contentValues.put(CONTACTS_LISTS_CONTACTID, ContactsInList.toString());
                db.insert("LISTS", null, contentValues);
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public ArrayList<String> getNameLength() {
        ArrayList<String> lists_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from LISTS", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            lists_list.add(res.getString(res.getColumnIndex(CONTACTS_LISTS_CONT_LEN)));
            res.moveToNext();
        }
        return lists_list;
    }

    public int getNumbersOfRowsInListByListId(String id) {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;
        cursor = db.rawQuery("select * from LISTS WHERE contactid LIKE '%' || ? || '%'", new String[]{id + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            count = cursor.getCount();

        }

        return count;
    }

    public ArrayList<String> getAllListNames() {
        ArrayList<String> lists_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from LISTS", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            lists_list.add(res.getString(res.getColumnIndex(CONTACTS_LISTS_NAME)));
            res.moveToNext();
        }
        return lists_list;
    }

    public Contacts getLastCreatedList() {
        Contacts country = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM LISTS ORDER BY column DESC LIMIT 1;", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            String id = (res.getString(res.getColumnIndex(CONTACTS_LISTS_ID)));
            String name = (res.getString(res.getColumnIndex(CONTACTS_LISTS_NAME)));
            String length = (res.getString(res.getColumnIndex(CONTACTS_LISTS_CONT_LEN)));
            country = new Contacts(id, name, length, false);
            res.moveToNext();
        }
        return country;
    }

    public ArrayList<String> getAllListIds() {
        ArrayList<String> lists_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from LISTS", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            lists_list.add(res.getString(res.getColumnIndex(CONTACTS_LISTS_ID)));
            res.moveToNext();
        }
        return lists_list;
    }

    public ArrayList getAllContactsById() {
        ArrayList<String> countryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from CONTACTS", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            String ids = res.getString(res.getColumnIndex(CONTACTS_CONT_ID));
            countryList.add(ids);
            res.moveToNext();
        }
        return countryList;
    }

    public String getListIdByName(String Name) {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String listsname = "";
        cursor = db.rawQuery("SELECT * FROM LISTS WHERE name=?", new String[]{Name + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            listsname = cursor.getString(cursor.getColumnIndex(CONTACTS_LISTS_ID));
        }
        return listsname;
    }

    public ArrayList<ListViewItem> getListNameIdLength() {
        ArrayList<ListViewItem> tagItmes = new ArrayList<ListViewItem>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from LISTS", null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String id = cursor.getString(cursor.getColumnIndex(CONTACTS_LISTS_ID));
            String name = cursor.getString(cursor.getColumnIndex(CONTACTS_LISTS_NAME));
            String length = cursor.getString(cursor.getColumnIndex(CONTACTS_LISTS_CONT_LEN));
            String contactid = cursor.getString(cursor.getColumnIndex(CONTACTS_LISTS_CONTACTID));
            ListViewItem item = new ListViewItem(id, name, length, contactid, 4);
            tagItmes.add(item);
            cursor.moveToNext();
        }
        return tagItmes;
    }

    public void removeContactFromList(String ContactID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from LISTS", null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            String ListID = cursor.getString(cursor.getColumnIndex(CONTACTS_LISTS_ID));

            String ListContacts = cursor.getString(cursor.getColumnIndex(CONTACTS_LISTS_CONTACTID));

            if (StringUtils.isNotBlank(ListContacts)) {
                ArrayList<String> Contacts = DataHelper.getInstance().stringToArrayList(ListContacts);
                Contacts.remove(ContactID);
                JSONArray ContactArray = DataHelper.getInstance().arrayListToString(Contacts);
                ContentValues values = new ContentValues();
                values.put(CONTACTS_LISTS_CONTACTID, ContactArray.toString());
                db.update("LISTS", values, CONTACTS_LISTS_ID + " = ?", new String[]{ListID});
            }
            cursor.moveToNext();
        }
    }


    public void removeListById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(CONTACTS_LISTS_TABLE, CONTACTS_LISTS_ID + " = ?", new String[]{id});
    }

    public int updateListById(String Id, String Name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CONTACTS_LISTS_NAME, Name);
        return db.update("LISTS", values, CONTACTS_LISTS_ID + " = ?", new String[]{Id});
    }

    public String getListContactsById(String Id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Contacts = null;
        Cursor cursor = db.rawQuery("SELECT * FROM LISTS WHERE Lid=?", new String[]{Id + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Contacts = cursor.getString(cursor.getColumnIndex(CONTACTS_LISTS_CONTACTID));
        }
        return Contacts;
    }

    public int updateListContactsAndLength(String Id, String contid, int len) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CONTACTS_LISTS_CONTACTID, contid);
        values.put(CONTACTS_LISTS_CONT_LEN, len);
        return db.update("LISTS", values, CONTACTS_LISTS_ID + " = ?", new String[]{Id});
    }

    public String getAllContactsFromList(String Lid) {
        SQLiteDatabase db = this.getReadableDatabase();
        String contacts = null;
        Cursor res = db.rawQuery("select * from LISTS where Lid =?", new String[]{Lid});
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            contacts = res.getString(res.getColumnIndex(CONTACTS_LISTS_CONTACTID));
            res.moveToNext();
        }
        return contacts;
    }

    //-------------------------------- "Tags" table methods -----------------------------------------------//
    public boolean insertNewTag(String id, String code, String name, String status, int length, String contid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_TAGS_ID, id);
        contentValues.put(CONTACTS_TAGS_CODE, code);
        contentValues.put(CONTACTS_TAGS_NAME, name);
        contentValues.put(CONTACTS_TAGS_STATUS, status);
        contentValues.put(CONTACTS_TAGS_CONT_LEN, length);
        contentValues.put(CONTACTS_TAGS_CONTACTID, contid);
        db.insert("TAGS", null, contentValues);
        return true;
    }

    public void insertNewTag(JSONObject ResponceData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < 1; i++) {
            try {
                JSONArray jsonArray = ResponceData.getJSONArray("tags");
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                    JSONArray jsonArray1 = jsonObject1.getJSONArray("contactIds");
                    final String jsonstr = jsonArray1.toString();
                    int len = jsonArray1.length();
                    contentValues.put(CONTACTS_TAGS_ID, jsonObject1.getString("id"));
                    contentValues.put(CONTACTS_TAGS_CODE, jsonObject1.getString("code"));
                    contentValues.put(CONTACTS_TAGS_NAME, jsonObject1.getString("name"));
                    contentValues.put(CONTACTS_TAGS_STATUS, jsonObject1.getString("status"));
                    contentValues.put(CONTACTS_TAGS_CONT_LEN, len);
                    contentValues.put(CONTACTS_TAGS_CONTACTID, jsonstr);
                    db.insert("TAGS", null, contentValues);
                }
            } catch (Exception e) {
                // textView.setText(e.toString());
            }
        }
    }

    public ArrayList<String> getAllTagNames() {
        ArrayList<String> lists_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from TAGS", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            lists_list.add(res.getString(res.getColumnIndex(CONTACTS_TAGS_NAME)));
            res.moveToNext();
        }
        return lists_list;
    }

    public ArrayList<String> getAllTagId() {
        ArrayList<String> lists_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from TAGS", null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            lists_list.add(res.getString(res.getColumnIndex(CONTACTS_TAGS_ID)));
            res.moveToNext();
        }
        return lists_list;
    }

    public int numberOfRowsTagsContactId(String id) {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;
        cursor = db.rawQuery("select * from TAGS WHERE contactid LIKE '%' || ? || '%'", new String[]{id + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            count = cursor.getCount();

        }
        return count;
    }

    public ArrayList<ListViewItem> getTagNameIdLength() {
        ArrayList<ListViewItem> tagItmes = new ArrayList<ListViewItem>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from TAGS", null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String id = cursor.getString(cursor.getColumnIndex(CONTACTS_TAGS_ID));
            String name = cursor.getString(cursor.getColumnIndex(CONTACTS_TAGS_NAME));
            String length = cursor.getString(cursor.getColumnIndex(CONTACTS_TAGS_CONT_LEN));
            String contactid = cursor.getString(cursor.getColumnIndex(CONTACTS_TAGS_CONTACTID));
            ListViewItem item = new ListViewItem(id, name, length, contactid, 4);
            tagItmes.add(item);
            cursor.moveToNext();
        }
        return tagItmes;
    }

    public void removeTagById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(CONTACTS_TAGS_TABLE, CONTACTS_TAGS_ID + " = ?", new String[]{id});
    }

    public int numberOfRowsTags() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TAGS_TABLE);
        return numRows;
    }

    public int updateTagContactsAndLength(String Id, String contid, int len) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CONTACTS_TAGS_CONTACTID, contid);
        values.put(CONTACTS_TAGS_CONT_LEN, len);
        return db.update("TAGS", values, CONTACTS_TAGS_ID + " = ?", new String[]{Id});
    }

    public int updateTagById(String Id, String Name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CONTACTS_TAGS_NAME, Name);
        return db.update("TAGS", values, CONTACTS_TAGS_ID + " = ?", new String[]{Id});
    }

    public String getAllContactsFromTag(String id) {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String listscontactid = "";
        cursor = db.rawQuery("SELECT contactid FROM TAGS WHERE Tid=?", new String[]{id + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            listscontactid = cursor.getString(cursor.getColumnIndex(CONTACTS_TAGS_CONTACTID));
        }
        return listscontactid;
    }

    public String getTagIdFromName(String Name) {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String listsname = "";
        cursor = db.rawQuery("SELECT Tid FROM TAGS WHERE name=?", new String[]{Name + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            listsname = cursor.getString(cursor.getColumnIndex(CONTACTS_TAGS_ID));
        }
        return listsname;
    }

    public String getTagContactsById(String Id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Contacts = null;
        Cursor cursor = db.rawQuery("SELECT * FROM TAGS WHERE Tid=?", new String[]{Id + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Contacts = cursor.getString(cursor.getColumnIndex(CONTACTS_TAGS_CONTACTID));
        }
        return Contacts;
    }

    public ArrayList<String> getAllTagLength() {
        ArrayList<String> lists_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from TAGS", null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            lists_list.add(res.getString(res.getColumnIndex(CONTACTS_TAGS_CONT_LEN)));
            res.moveToNext();
        }
        return lists_list;
    }


    // ------------------------ "contact" table methods ----------------//
    public boolean insertNewContact(JSONObject ContactJsonObject) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put(CONTACTS_CREATED_ON, ContactJsonObject.getString("createdOn"));
            contentValues.put(CONTACTS_UPDATED_ON, ContactJsonObject.getString("updatedOn"));
            contentValues.put(CONTACTS_FIRSTNAME, ContactJsonObject.getString("firstName"));
            contentValues.put(CONTACTS_MIDDLENAME, ContactJsonObject.getString("middleName"));
            contentValues.put(CONTACTS_LASTNAME, ContactJsonObject.getString("lastName"));
            contentValues.put(CONTACTS_SKYPE_USERNAME, ContactJsonObject.getString("skypeUserName"));
            contentValues.put(CONTACTS_GENDER, ContactJsonObject.getString("gender"));
            contentValues.put(CONTACTS_DATE_OF_BIRTH, ContactJsonObject.getString("dateOfBirth"));
            contentValues.put(CONTACTS_EMAIL_ADDRESS, ContactJsonObject.getString("emailAddress"));
            contentValues.put(CONTACTS_CONT_ID, ContactJsonObject.getString("id"));
            contentValues.put(CONTACTS_STATUS, ContactJsonObject.getString("status"));
            contentValues.put(CONTACTS_COMPANY_NAME, ContactJsonObject.getString("companyName"));
            contentValues.put(CONTACTS_SITEURL, ContactJsonObject.getString("siteUrl"));
            contentValues.put(CONTACTS_FBURL, ContactJsonObject.getString("facebookUrl"));
            contentValues.put(CONTACTS_LIURL, ContactJsonObject.getString("linkedInUrl"));
            contentValues.put(CONTACTS_TWURL, ContactJsonObject.getString("twitterUrl"));
            contentValues.put(CONTACTS_PHOTO, ContactJsonObject.getString("photo"));
            contentValues.put(CONTACTS_CONTACT_CODE, ContactJsonObject.getString("contactCode"));
            contentValues.put(CONTACTS_CONTACT_OPT_IN, ContactJsonObject.getString("contactOptIn"));
            contentValues.put(CONTACTS_ORGAN_ID, ContactJsonObject.getString("orgId"));
            contentValues.put(CONTACT_FAVORITE, ContactJsonObject.getString("favorite"));

            JSONArray jsonArrayNotes = ContactJsonObject.getJSONArray("notes");

            if (jsonArrayNotes.length() > 0) {
                contentValues.put(CONTACTS_NOTES, jsonArrayNotes.getString(0));
            } else {
                contentValues.put(CONTACTS_NOTES, "");
            }

            JSONArray ContactAddressesJSONArray = ContactJsonObject.getJSONArray("addresses");

            if (ContactAddressesJSONArray.length() > 0) {
                JSONObject addressesArrayJSONObject = ContactAddressesJSONArray.getJSONObject(0);
                contentValues.put(CONTACTS_ADDRESS1, addressesArrayJSONObject.getString("addressLine1"));
                contentValues.put(CONTACTS_ADDRESS2, addressesArrayJSONObject.getString("addressLine2"));
                contentValues.put(CONTACTS_ADDRESS3, addressesArrayJSONObject.getString("addressLine3"));
                contentValues.put(CONTACTS_ADDRESS4, addressesArrayJSONObject.getString("addressLine4"));
                contentValues.put(CONTACTS_PINCODE, addressesArrayJSONObject.getString("postalCode"));
            }
            JSONArray ContactsPhoneArray = ContactJsonObject.getJSONArray("phones");
            if (ContactsPhoneArray.length() > 0) {
                contentValues.put(CONTACTS_PHONE_TYPE, getPhoneType(ContactsPhoneArray.getJSONObject(0).getString("phoneType")));
                contentValues.put(CONTACT_PHONE_NUMBER, ContactsPhoneArray.getJSONObject(0).getString("phoneNumber"));
            }

//            JSONArray listIds = ContactJsonObject.getJSONArray("listIds");
//            if (listIds.length() > 0) {
//                JSONObject phonesArrayJSONObject = ContactsPhoneArray.getJSONObject(0);
//                contentValues.put(CONTACT_LIST_IDS, phonesArrayJSONObject.getString("listIds"));
//            }
//
//            JSONArray interestIds = ContactJsonObject.getJSONArray("interestIds");
//            if (interestIds.length() > 0) {
//                JSONObject phonesArrayJSONObject = ContactsPhoneArray.getJSONObject(0);
//                contentValues.put(CONTACT_INTERESTS_IDS, phonesArrayJSONObject.getString("listIds"));
//            }
//
//            JSONArray tagIds = ContactJsonObject.getJSONArray("tagIds");
//            if (tagIds.length() > 0) {
//                JSONObject phonesArrayJSONObject = ContactsPhoneArray.getJSONObject(0);
//                contentValues.put(CONTACT_TAG_IDS, phonesArrayJSONObject.getString("listIds"));
//            }
            contentValues.put(CONTACT_LIST_IDS, "");
            contentValues.put(CONTACT_INTERESTS_IDS, "");
            contentValues.put(CONTACT_TAG_IDS, "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.insert("CONTACTS", null, contentValues);
        return true;
    }

    public int updateContact(String createon, String updatedon, String fn, String mn, String ln, String skynam, String gen, String dob, String email,
                             String contactid, String status, String compname, String siteurl, String fburl, String liurl, String photo,
                             String twurl, String contactcode, String contactopt, String orgid, String fav, String address1, String address2,
                             String address3, String address4, String pincode, String number, String notes, String phoneType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_CREATED_ON, createon);
        contentValues.put(CONTACTS_UPDATED_ON, updatedon);
        contentValues.put(CONTACTS_FIRSTNAME, fn);
        contentValues.put(CONTACTS_MIDDLENAME, mn);
        contentValues.put(CONTACTS_LASTNAME, ln);
        contentValues.put(CONTACTS_SKYPE_USERNAME, skynam);
        contentValues.put(CONTACTS_GENDER, gen);
        contentValues.put(CONTACTS_DATE_OF_BIRTH, dob);
        contentValues.put(CONTACTS_EMAIL_ADDRESS, email);
        contentValues.put(CONTACTS_STATUS, status);
        contentValues.put(CONTACTS_COMPANY_NAME, compname);
        contentValues.put(CONTACTS_SITEURL, siteurl);
        contentValues.put(CONTACTS_FBURL, fburl);
        contentValues.put(CONTACTS_LIURL, liurl);
        contentValues.put(CONTACTS_TWURL, twurl);
        contentValues.put(CONTACTS_PHOTO, photo);
        contentValues.put(CONTACTS_CONTACT_CODE, contactcode);
        contentValues.put(CONTACTS_CONTACT_OPT_IN, contactopt);
        contentValues.put(CONTACTS_ORGAN_ID, orgid);
        contentValues.put(CONTACT_FAVORITE, fav);
        contentValues.put(CONTACTS_ADDRESS1, address1);
        contentValues.put(CONTACTS_ADDRESS2, address2);
        contentValues.put(CONTACTS_ADDRESS3, address3);
        contentValues.put(CONTACTS_ADDRESS4, address4);
        contentValues.put(CONTACTS_PINCODE, pincode);
        contentValues.put(CONTACT_PHONE_NUMBER, number);
        contentValues.put(CONTACTS_NOTES, notes);
        contentValues.put(CONTACTS_PHONE_TYPE, phoneType);
        return db.update("CONTACTS", contentValues, CONTACTS_CONT_ID + " = ?", new String[]{contactid});
    }

    public int numberOfRowsContact() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACT_MAIN_TABLE);
        return numRows;
    }

    public ArrayList<String> getContactInformation(String id) {
        ArrayList<String> userdata = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from CONTACTS WHERE " + CONTACTS_CONT_ID + " = ?", new String[]{id + ""});
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            userdata.add((res.getString(res.getColumnIndex(CONTACTS_CREATED_ON))));
            userdata.add((res.getString(res.getColumnIndex(CONTACTS_FIRSTNAME))));
            userdata.add((res.getString(res.getColumnIndex(CONTACTS_MIDDLENAME))));
            userdata.add((res.getString(res.getColumnIndex(CONTACTS_LASTNAME))));
            userdata.add((res.getString(res.getColumnIndex(CONTACTS_PHOTO))));
            userdata.add((res.getString(res.getColumnIndex(CONTACTS_CONTACT_CODE))));
            userdata.add((res.getString(res.getColumnIndex(CONTACT_PHONE_NUMBER))));
            userdata.add((res.getString(res.getColumnIndex(CONTACTS_EMAIL_ADDRESS))));
            userdata.add((res.getString(res.getColumnIndex(CONTACTS_ADDRESS1))));
            userdata.add((res.getString(res.getColumnIndex(CONTACTS_ADDRESS2))));
            userdata.add((res.getString(res.getColumnIndex(CONTACTS_PINCODE))));
            userdata.add((res.getString(res.getColumnIndex(CONTACTS_ADDRESS3))));
            userdata.add((res.getString(res.getColumnIndex(CONTACTS_ADDRESS4))));
            userdata.add((res.getString(res.getColumnIndex(CONTACTS_COMPANY_NAME))));
            userdata.add((res.getString(res.getColumnIndex(CONTACTS_SITEURL))));
            userdata.add((res.getString(res.getColumnIndex(CONTACTS_FBURL))));
            userdata.add((res.getString(res.getColumnIndex(CONTACTS_LIURL))));
            userdata.add((res.getString(res.getColumnIndex(CONTACTS_TWURL))));
            userdata.add((res.getString(res.getColumnIndex(CONTACTS_NOTES))));
            userdata.add((res.getString(res.getColumnIndex(CONTACT_FAVORITE))));
            userdata.add((res.getString(res.getColumnIndex(CONTACTS_PHONE_TYPE))));
            userdata.add((res.getString(res.getColumnIndex(CONTACTS_DATE_OF_BIRTH))));
            res.moveToNext();
        }
        return userdata;
    }

    public JSONObject getContactObject(String id) {
        JSONObject ContactObject = new JSONObject();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from CONTACTS WHERE " + CONTACTS_CONT_ID + " = ?", new String[]{id + ""});
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            try {
                ContactObject.put("updatedOn", System.currentTimeMillis());
                ContactObject.put("createdOn", res.getString(res.getColumnIndex(CONTACTS_CREATED_ON)));
                ContactObject.put("firstName", res.getString(res.getColumnIndex(CONTACTS_FIRSTNAME)));
                ContactObject.put("middleName", res.getString(res.getColumnIndex(CONTACTS_MIDDLENAME)));
                ContactObject.put("lastName", res.getString(res.getColumnIndex(CONTACTS_LASTNAME)));
                ContactObject.put("emailAddress", res.getString(res.getColumnIndex(CONTACTS_EMAIL_ADDRESS)));

                String gender = res.getString(res.getColumnIndex(CONTACTS_GENDER));

                if (gender.equalsIgnoreCase("f") || gender.equalsIgnoreCase("m")) {
                    ContactObject.put("gender", gender);
                }

                ContactObject.put("photo", res.getString(res.getColumnIndex(CONTACTS_PHOTO)));
                ContactObject.put("siteUrl", res.getString(res.getColumnIndex(CONTACTS_SITEURL)));
                ContactObject.put("contactCode", res.getString(res.getColumnIndex(CONTACTS_CONTACT_CODE)));
                ContactObject.put("companyName", res.getString(res.getColumnIndex(CONTACTS_COMPANY_NAME)));
                ContactObject.put("favorite", res.getString(res.getColumnIndex(CONTACT_FAVORITE)));
                ContactObject.put("orgId", res.getString(res.getColumnIndex(CONTACTS_ORGAN_ID)));
                ContactObject.put("id", res.getString(res.getColumnIndex(CONTACTS_CONT_ID)));
                ContactObject.put("facebookUrl", res.getString(res.getColumnIndex(CONTACTS_FBURL)));
                ContactObject.put("linkedInUrl", res.getString(res.getColumnIndex(CONTACTS_LIURL)));
                ContactObject.put("twitterUrl", res.getString(res.getColumnIndex(CONTACTS_TWURL)));

                ContactObject.put("addresses", new JSONArray()
                        .put(new JSONObject()
                                .put("addressLine1", (res.getString(res.getColumnIndex(CONTACTS_ADDRESS1))))
                                .put("addressLine2", (res.getString(res.getColumnIndex(CONTACTS_ADDRESS2))))
                                .put("addressLine3", (res.getString(res.getColumnIndex(CONTACTS_ADDRESS3))))
                                .put("addressLine4", (res.getString(res.getColumnIndex(CONTACTS_ADDRESS4))))
                                .put("postalCode", (res.getString(res.getColumnIndex(CONTACTS_PINCODE))))
                        ));

                ContactObject.put("phones", new JSONArray()
                        .put(new JSONObject()
                                .put("phoneNumber", (res.getString(res.getColumnIndex(CONTACT_PHONE_NUMBER))))
                                .put("phoneType", (getPhoneType(res.getString(res.getColumnIndex(CONTACTS_PHONE_TYPE))))))
                );

                ContactObject.put("notes", new JSONArray()
                        .put(res.getString(res.getColumnIndex(CONTACTS_NOTES))));

                ContactObject.put("status", "ACTIVE");

                ContactObject.put("listIds", new JSONArray()
                        .put(res.getString(res.getColumnIndex(CONTACT_LIST_IDS))));

                ContactObject.put("interestIds", new JSONArray()
                        .put(res.getString(res.getColumnIndex(CONTACT_INTERESTS_IDS))));

                ContactObject.put("tagIds", new JSONArray()
                        .put(res.getString(res.getColumnIndex(CONTACT_TAG_IDS))));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            res.moveToNext();
        }
        return ContactObject;
    }

    private String getPhoneType(String string) {
        if (string == null || string.equalsIgnoreCase("null") || string.length() == 0)
            return "MOBILE";
        else
            return string;
    }

    public SimpleContact getContactDetail(String id) {
        SimpleContact userdata = new SimpleContact();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from CONTACTS WHERE " + CONTACTS_CONT_ID + " = ?", new String[]{id + ""});
        res.moveToFirst();
        SimpleContact simpleContact = new SimpleContact();
        Addresses addresses = new Addresses();

        while (res.isAfterLast() == false) {
            simpleContact.setCreatedOn((res.getString(res.getColumnIndex(CONTACTS_CREATED_ON))));
            simpleContact.setFirstName((res.getString(res.getColumnIndex(CONTACTS_FIRSTNAME))));
            simpleContact.setMiddleName((res.getString(res.getColumnIndex(CONTACTS_MIDDLENAME))));
            simpleContact.setLastName((res.getString(res.getColumnIndex(CONTACTS_LASTNAME))));
            simpleContact.setPhoto((res.getString(res.getColumnIndex(CONTACTS_PHOTO))));
            simpleContact.setContactCode((res.getString(res.getColumnIndex(CONTACTS_CONTACT_CODE))));
            simpleContact.setEmailAddress((res.getString(res.getColumnIndex(CONTACTS_EMAIL_ADDRESS))));

            addresses.setAddressLine1((res.getString(res.getColumnIndex(CONTACTS_ADDRESS1))));
            addresses.setAddressLine2((res.getString(res.getColumnIndex(CONTACTS_ADDRESS2))));
            addresses.setPostalCode((res.getString(res.getColumnIndex(CONTACTS_PINCODE))));
            addresses.setAddressLine3((res.getString(res.getColumnIndex(CONTACTS_ADDRESS3))));
            addresses.setAddressLine4((res.getString(res.getColumnIndex(CONTACTS_ADDRESS4))));

            simpleContact.setAddresses(addresses);

            simpleContact.setCompanyName((res.getString(res.getColumnIndex(CONTACTS_COMPANY_NAME))));
            simpleContact.setSiteUrl((res.getString(res.getColumnIndex(CONTACTS_SITEURL))));
            simpleContact.setFacebookUrl((res.getString(res.getColumnIndex(CONTACTS_FBURL))));
            simpleContact.setLinkedInUrl((res.getString(res.getColumnIndex(CONTACTS_LIURL))));
            simpleContact.setTwitterUrl((res.getString(res.getColumnIndex(CONTACTS_TWURL))));

            simpleContact.setFavorite((res.getString(res.getColumnIndex(CONTACT_FAVORITE))));

            String[] arr = {(res.getString(res.getColumnIndex(CONTACTS_NOTES)))};

            simpleContact.setNotes(arr);
            Phones phones = new Phones();
            phones.setPhoneNumber(res.getString(res.getColumnIndex(CONTACT_PHONE_NUMBER)));
            phones.setPhoneType((res.getString(res.getColumnIndex(CONTACTS_PHONE_TYPE))));

            Phones[] phone = {phones};
            simpleContact.setPhones(phone);

            res.moveToNext();
        }
        return userdata;
    }

    public String getContactCreatedon(String id) {
        String date = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + CONTACTS_CREATED_ON + " FROM CONTACTS WHERE " + CONTACTS_CONT_ID + " = ?", new String[]{id + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            date = cursor.getString(cursor.getColumnIndex(CONTACTS_CREATED_ON));
        }
        return date;
    }

    public String getContactImage(String id) {
        String date = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + CONTACTS_PHOTO + " FROM CONTACTS WHERE " + CONTACTS_CONT_ID + " = ?", new String[]{id + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            date = cursor.getString(cursor.getColumnIndex(CONTACTS_PHOTO));
        }
        return date;
    }

    public String isContactExist(String id) {
        String name = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CONTACTS WHERE " + CONTACTS_CONT_ID + " = ?", new String[]{id + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex(CONTACTS_CONT_ID));
        }
        return name;
    }

    public String getContactFnameOfContact(String id) {
        String name = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CONTACTS WHERE " + CONTACTS_CONT_ID + " = ?", new String[]{id + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex(CONTACTS_FIRSTNAME));
        }
        return name;
    }

    public String getContactFname(String id) {
        String Name = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM CONTACTS WHERE " + CONTACTS_CONT_ID + " = ?", new String[]{id + ""});
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            String FirstName = res.getString(res.getColumnIndex(CONTACTS_FIRSTNAME));

            String MiddleName = res.getString(res.getColumnIndex(CONTACTS_MIDDLENAME));

            String LastName = res.getString(res.getColumnIndex(CONTACTS_LASTNAME));

            String Phone = res.getString(res.getColumnIndex(CONTACT_PHONE_NUMBER));

            String Email = res.getString(res.getColumnIndex(CONTACTS_EMAIL_ADDRESS));

            String ContactId = res.getString(res.getColumnIndex(CONTACTS_CONT_ID));

            String ContactImage = res.getString(res.getColumnIndex(CONTACTS_PHOTO));

            if (nullCheck(FirstName)) {
                FirstName = "";
            }

            if (nullCheck(MiddleName)) {
                MiddleName = "";
            }
            if (nullCheck(LastName)) {
                LastName = "";
            }

            Name = FirstName.trim() + " " + MiddleName.trim() + " " + LastName.trim();

            Name.replaceAll("\\s+", " ");

            if (nullCheck(Name)) {
                if (!nullCheck(Phone)) {
                    Name = Phone;
                } else {
                    Name = Email;
                }
            }
            res.moveToNext();
        }
        return Name;
    }

    public String getContactLname(String id) {
        Cursor cursor = null;
        String name = null;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("SELECT " + CONTACTS_LASTNAME + " FROM CONTACTS WHERE " + CONTACTS_CONT_ID + " = ?", new String[]{id + ""});
        if (cursor.getCount() > 0) {

            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex(CONTACTS_LASTNAME));
        }
        return name;
    }

    public String getContactMname(String id) {
        Cursor cursor = null;
        String name = null;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("SELECT " + CONTACTS_MIDDLENAME + " FROM CONTACTS WHERE " + CONTACTS_CONT_ID + " = ?", new String[]{id + ""});
        if (cursor.getCount() > 0) {

            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex(CONTACTS_MIDDLENAME));
        }
        return name;
    }

    public String getContactphone(String id) {
        Cursor cursor = null;
        String name = null;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("SELECT " + CONTACT_PHONE_NUMBER + " FROM CONTACTS WHERE " + CONTACTS_CONT_ID + " = ?", new String[]{id + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex(CONTACT_PHONE_NUMBER));
        }
        return name;
    }

    public String getContactsPhoneType(String id) {
        Cursor cursor = null;
        String name = null;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("SELECT " + CONTACTS_PHONE_TYPE + " FROM CONTACTS WHERE " + CONTACTS_CONT_ID + " = ?", new String[]{id + ""});
        if (cursor.getCount() > 0) {

            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex(CONTACTS_PHONE_TYPE));
        }
        return name;
    }

    public String getContactemail(String id) {
        Cursor cursor = null;
        String name = null;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("SELECT " + CONTACTS_EMAIL_ADDRESS + " FROM CONTACTS WHERE " + CONTACTS_CONT_ID + " = ?", new String[]{id + ""});
        if (cursor.getCount() > 0) {

            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex(CONTACTS_EMAIL_ADDRESS));
        }
        return name;
    }

    public String getContactaddress1(String id) {
        Cursor cursor = null;
        String name = null;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("SELECT " + CONTACTS_ADDRESS1 + " FROM CONTACTS WHERE " + CONTACTS_CONT_ID + " = ?", new String[]{id + ""});
        if (cursor.getCount() > 0) {

            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex(CONTACTS_ADDRESS1));
        }
        return name;
    }

    public String getContactaddress2(String id) {
        Cursor cursor = null;
        String name = null;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("SELECT " + CONTACTS_ADDRESS2 + " FROM CONTACTS WHERE " + CONTACTS_CONT_ID + " = ?", new String[]{id + ""});
        if (cursor.getCount() > 0) {

            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex(CONTACTS_ADDRESS2));
        }
        return name;
    }

    public String getContactaddress3(String id) {
        Cursor cursor = null;
        String name = null;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("SELECT " + CONTACTS_ADDRESS3 + " FROM CONTACTS WHERE " + CONTACTS_CONT_ID + " = ?", new String[]{id + ""});
        if (cursor.getCount() > 0) {

            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex(CONTACTS_ADDRESS3));
        }
        return name;
    }

    public String getContactaddress4(String id) {
        Cursor cursor = null;
        String name = null;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("SELECT " + CONTACTS_ADDRESS4 + " FROM CONTACTS WHERE " + CONTACTS_CONT_ID + " = ?", new String[]{id + ""});
        if (cursor.getCount() > 0) {

            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex(CONTACTS_ADDRESS4));
        }
        return name;
    }

    public String getContactpincode(String id) {
        Cursor cursor = null;
        String name = null;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("SELECT " + CONTACTS_PINCODE + " FROM CONTACTS WHERE " + CONTACTS_CONT_ID + " = ?", new String[]{id + ""});
        if (cursor.getCount() > 0) {

            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex(CONTACTS_PINCODE));
        }
        return name;
    }

    public String getContactsDateOfBirth(String id) {
        Cursor cursor = null;
        String name = null;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("SELECT " + CONTACTS_DATE_OF_BIRTH + " FROM CONTACTS WHERE " + CONTACTS_CONT_ID + " = ?", new String[]{id + ""});
        if (cursor.getCount() > 0) {

            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex(CONTACTS_DATE_OF_BIRTH));
        }
        return name;
    }

    public String getContactsCompanyName(String id) {
        Cursor cursor = null;
        String name = null;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("SELECT " + CONTACTS_COMPANY_NAME + " FROM CONTACTS WHERE " + CONTACTS_CONT_ID + " = ?", new String[]{id + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex(CONTACTS_COMPANY_NAME));
        }
        return name;
    }

    public String getContactsSiteurl(String id) {
        Cursor cursor = null;
        String name = null;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("SELECT " + CONTACTS_SITEURL + " FROM CONTACTS WHERE " + CONTACTS_CONT_ID + " = ?", new String[]{id + ""});
        if (cursor.getCount() > 0) {

            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex(CONTACTS_SITEURL));
        }
        return name;
    }

    public boolean getContactFavorite(String id) {
        Cursor cursor = null;
        String name = null;
        boolean fab = false;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("SELECT " + CONTACT_FAVORITE + " FROM CONTACTS WHERE " + CONTACTS_CONT_ID + " = ?", new String[]{id + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex(CONTACT_FAVORITE));
        }
        if (name != null) {
            if (name.equalsIgnoreCase("true")) {
                fab = true;
            }
        }
        return fab;
    }

    public ArrayList<String> getfirstnamecontact() {
        ArrayList<String> Firsname_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM contacts ORDER BY UpdatedOn DESC  Limit 20", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            String fName = res.getString(res.getColumnIndex(CONTACTS_FIRSTNAME));
            String mName = res.getString(res.getColumnIndex(CONTACTS_MIDDLENAME));
            String lName = res.getString(res.getColumnIndex(CONTACTS_LASTNAME));

            if (mName.isEmpty() || mName.toLowerCase().equals("null")) {
                mName = "";
            }
            if (lName.isEmpty() || lName.toLowerCase().equals("null")) {
                lName = "";
            }

            String Name = fName.trim() + " " + mName.trim() + " " + lName.trim();

            Firsname_list.add(Name.replaceAll("\\s+", " "));

            res.moveToNext();
        }
        return Firsname_list;

    }

    public ArrayList<String> getContactImage() {
        ArrayList<String> Firsname_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM contacts ORDER BY UpdatedOn DESC  Limit 20", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Firsname_list.add(res.getString(res.getColumnIndex(CONTACTS_PHOTO)));
            res.moveToNext();
        }
        return Firsname_list;

    }

    public ArrayList<String> getcontactid() {
        ArrayList<String> Firsname_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM contacts ORDER BY UpdatedOn DESC Limit 20", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Firsname_list.add(res.getString(res.getColumnIndex(CONTACTS_CONT_ID)));
            // Firsname_list.add(res.getString(res.getColumnIndex(CONTACTS_CONT_ID)));
            res.moveToNext();
        }
        return Firsname_list;

    }

    public ArrayList<Contacts> getRecentContacts() {

        ArrayList<Contacts> List = new ArrayList<Contacts>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM contacts ORDER BY UpdatedOn DESC  Limit 20", null);

        res.moveToFirst();

        while (res.isAfterLast() == false) {

            String FirstName = res.getString(res.getColumnIndex(CONTACTS_FIRSTNAME));

            String MiddleName = res.getString(res.getColumnIndex(CONTACTS_MIDDLENAME));

            String LastName = res.getString(res.getColumnIndex(CONTACTS_LASTNAME));

            String Phone = res.getString(res.getColumnIndex(CONTACT_PHONE_NUMBER));

            String Email = res.getString(res.getColumnIndex(CONTACTS_EMAIL_ADDRESS));

            String ContactId = res.getString(res.getColumnIndex(CONTACTS_CONT_ID));

            String ContactImage = res.getString(res.getColumnIndex(CONTACTS_PHOTO));

            if (nullCheck(FirstName)) {
                FirstName = "";
            }

            if (nullCheck(MiddleName)) {
                MiddleName = "";
            }
            if (nullCheck(LastName)) {
                LastName = "";
            }

            String Name = FirstName.trim() + " " + MiddleName.trim() + " " + LastName.trim();

            Name = Name.replaceAll("\\s+", " ");

            if (nullCheck(Name)) {
                if (!nullCheck(Phone)) {
                    Name = Phone;
                } else {
                    Name = Email;
                }
            }
            List.add(new Contacts(Name, ContactId, ContactImage));
            res.moveToNext();

        }
        return List;

    }

    public ArrayList<Contacts> getFavoriteContacts() {

        ArrayList<Contacts> List = new ArrayList<Contacts>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("Select * from  CONTACTS where favorite = \"true\"", null);

        res.moveToFirst();

        while (res.isAfterLast() == false) {

            String FirstName = res.getString(res.getColumnIndex(CONTACTS_FIRSTNAME));

            String MiddleName = res.getString(res.getColumnIndex(CONTACTS_MIDDLENAME));

            String LastName = res.getString(res.getColumnIndex(CONTACTS_LASTNAME));

            String Phone = res.getString(res.getColumnIndex(CONTACT_PHONE_NUMBER));

            String Email = res.getString(res.getColumnIndex(CONTACTS_EMAIL_ADDRESS));

            String ContactId = res.getString(res.getColumnIndex(CONTACTS_CONT_ID));

            String ContactImage = res.getString(res.getColumnIndex(CONTACTS_PHOTO));


            if (nullCheck(FirstName)) {
                FirstName = "";
            }

            if (nullCheck(MiddleName)) {
                MiddleName = "";
            }
            if (nullCheck(LastName)) {
                LastName = "";
            }

            String Name = FirstName.trim() + " " + MiddleName.trim() + " " + LastName.trim();
            Name.replaceAll("\\s+", " ");

            if (nullCheck(Name)) {
                if (!nullCheck(Phone)) {
                    Name = Phone;
                } else {
                    Name = Email;
                }
            }
            List.add(new Contacts(Name, ContactId, ContactImage));
            res.moveToNext();
        }
        return List;
    }

    public int getFavlen() {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("Select * from  CONTACTS where favorite = \"true\"", null);
        int favcnt = cursor.getCount();
        return favcnt;
    }

    public int getContactTypelen(String code) {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        if (code.equals("ALL")) {
            cursor = db.rawQuery("SELECT * FROM CONTACTS", null);
        } else {
            cursor = db.rawQuery("SELECT * FROM CONTACTS WHERE  Contactcode = ?", new String[]{code + ""});
        }
        int Leadcnt = cursor.getCount();
        return Leadcnt;
    }

    public void deleteContact(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CONTACT_MAIN_TABLE, CONTACTS_CONT_ID + " = ?", new String[]{id});
    }

    public ArrayList<String> getcontact(String code) {
        ArrayList<String> leadContact = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (code.equals("ALL")) {
            cursor = db.rawQuery("SELECT * FROM CONTACTS", null);
        } else {
            cursor = db.rawQuery("SELECT * FROM CONTACTS WHERE  Contactcode = ?", new String[]{code + ""});
        }

        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            // leadContact.add(cursor.getString(cursor.getColumnIndex(CONTACTS_FIRSTNAME)));
            String FirstName = cursor.getString(cursor.getColumnIndex(CONTACTS_FIRSTNAME));
            String MiddleName = cursor.getString(cursor.getColumnIndex(CONTACTS_MIDDLENAME));
            String LastName = cursor.getString(cursor.getColumnIndex(CONTACTS_LASTNAME));
            String Phone = cursor.getString(cursor.getColumnIndex(CONTACT_PHONE_NUMBER));
            String Email = cursor.getString(cursor.getColumnIndex(CONTACTS_EMAIL_ADDRESS));

            if (nullCheck(FirstName)) {
                FirstName = "";
            }

            if (nullCheck(MiddleName)) {
                MiddleName = "";
            }
            if (nullCheck(LastName)) {
                LastName = "";
            }

            String Name = FirstName.trim() + " " + MiddleName.trim() + " " + LastName.trim();

            Name.replaceAll("\\s+", " ");
            if (nullCheck(Name)) {
                if (!nullCheck(Phone)) {
                    Name = Phone;
                } else {
                    Name = Email;
                }
            }
            leadContact.add(Name);
            cursor.moveToNext();
        }
        return leadContact;
    }

    // --------------------------new Methods Start----------------------//

    public ArrayList<Contacts> getContactsDetailDayWise(String code, int days) {
        ArrayList<Contacts> AllContacts = new ArrayList<Contacts>();
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Calendar theEnd = Calendar.getInstance();
        Calendar theStart = (Calendar) theEnd.clone();
        theStart.add(Calendar.DAY_OF_MONTH, -days);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        long startTime = theStart.getTimeInMillis();
        long endTime = theEnd.getTimeInMillis();
        if (days <= 90) {
            if (code.equals("ALL")) {
                cursor = db.rawQuery("Select * from CONTACTS WHERE createdOn BETWEEN " + startTime + " AND " + endTime + "", null);
            } else {
                cursor = db.rawQuery("Select * from CONTACTS WHERE createdOn BETWEEN " + startTime + " AND " + endTime + " AND Contactcode = ?", new String[]{code + ""});
            }
        } else {
            if (code.equals("ALL")) {
                cursor = db.rawQuery("Select * from CONTACTS WHERE createdOn <= " + startTime, null);
            } else {
                cursor = db.rawQuery("Select * from CONTACTS WHERE createdOn <= " + startTime + " AND Contactcode = ?", new String[]{code + ""});
            }
        }
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String createON = cursor.getString(cursor.getColumnIndex(CONTACTS_CREATED_ON));
            String FirstName = cursor.getString(cursor.getColumnIndex(CONTACTS_FIRSTNAME));
            String MiddleName = cursor.getString(cursor.getColumnIndex(CONTACTS_MIDDLENAME));
            String LastName = cursor.getString(cursor.getColumnIndex(CONTACTS_LASTNAME));
            String Phone = cursor.getString(cursor.getColumnIndex(CONTACT_PHONE_NUMBER));
            String Email = cursor.getString(cursor.getColumnIndex(CONTACTS_EMAIL_ADDRESS));
            String Image = cursor.getString(cursor.getColumnIndex(CONTACTS_PHOTO));
            String ContactID = cursor.getString(cursor.getColumnIndex(CONTACTS_CONT_ID));

            if (nullCheck(FirstName)) {
                FirstName = "";
            }

            if (nullCheck(MiddleName)) {
                MiddleName = "";
            }
            if (nullCheck(LastName)) {
                LastName = "";
            }

            String Name = FirstName.trim() + " " + MiddleName.trim() + " " + LastName.trim();
            Name.replaceAll("\\s+", " ");
            if (nullCheck(Name)) {
                if (!nullCheck(Phone)) {
                    Name = Phone;
                } else {
                    Name = Email;
                }
            }
            AllContacts.add(new Contacts(Name, ContactID, Image, createON));
            cursor.moveToNext();
        }
        return AllContacts;

    }

    public ArrayList<Contacts> getAllContactsBYContactIds(ArrayList<String> ContactIds) {
        ArrayList<Contacts> AllContacts = new ArrayList<Contacts>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        for (String ContactId : ContactIds) {
            cursor = db.rawQuery("SELECT * FROM CONTACTS WHERE contactid=?", new String[]{ContactId + ""});
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String FirstName = cursor.getString(cursor.getColumnIndex(CONTACTS_FIRSTNAME));
                String MiddleName = cursor.getString(cursor.getColumnIndex(CONTACTS_MIDDLENAME));
                String LastName = cursor.getString(cursor.getColumnIndex(CONTACTS_LASTNAME));
                String Phone = cursor.getString(cursor.getColumnIndex(CONTACT_PHONE_NUMBER));
                String Email = cursor.getString(cursor.getColumnIndex(CONTACTS_EMAIL_ADDRESS));
                String Image = cursor.getString(cursor.getColumnIndex(CONTACTS_PHOTO));
                String ContactID = cursor.getString(cursor.getColumnIndex(CONTACTS_CONT_ID));

                if (nullCheck(FirstName)) {
                    FirstName = "";
                }

                if (nullCheck(MiddleName)) {
                    MiddleName = "";
                }
                if (nullCheck(LastName)) {
                    LastName = "";
                }

                String Name = FirstName.trim() + " " + MiddleName.trim() + " " + LastName.trim();
                Name.replaceAll("\\s+", " ");
                if (nullCheck(Name)) {
                    if (!nullCheck(Phone)) {
                        Name = Phone;
                    } else {
                        Name = Email;
                    }
                }
                AllContacts.add(new Contacts(Name, ContactID, Image));
                cursor.moveToNext();
            }
        }
        return AllContacts;

    }

    // ---------------------------new Methods End----------------------//
    public ArrayList<String> getcontactid(String code) {
        ArrayList<String> leadContact = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (code.equals("ALL")) {
            cursor = db.rawQuery("SELECT * FROM CONTACTS", null);
        } else {
            cursor = db.rawQuery("SELECT * FROM CONTACTS WHERE  Contactcode = ?", new String[]{code + ""});
        }
        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            leadContact.add(cursor.getString(cursor.getColumnIndex(CONTACTS_CONT_ID)));
            cursor.moveToNext();
        }
        return leadContact;

    }

    public ArrayList<Contacts> getContactsBetweenTimeWithContactCode(String ContactCode, String type) {


        Log.i(TAG, ContactCode);

        ArrayList<Contacts> ContactsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        Calendar calendarStartTime = Calendar.getInstance();

        Calendar calendarEndTime = Calendar.getInstance();

        long startTime = 0;

        long endTime = 0;

        switch (type) {
            case "Last 7 Days":
                calendarStartTime.set(Calendar.DAY_OF_YEAR, calendarStartTime.get(Calendar.DAY_OF_YEAR) - 7);
                startTime = beginOfDay(calendarStartTime.getTime());
                endTime = endOfDay(calendarEndTime.getTime());
                break;

            case "Last 30 Days":
                calendarStartTime.set(Calendar.DAY_OF_YEAR, calendarStartTime.get(Calendar.DAY_OF_YEAR) - 30);
                startTime = beginOfDay(calendarStartTime.getTime());
                endTime = endOfDay(calendarEndTime.getTime());
                break;

            case "Last 60 Days":
                calendarStartTime.set(Calendar.DAY_OF_YEAR, calendarStartTime.get(Calendar.DAY_OF_YEAR) - 60);
                startTime = beginOfDay(calendarStartTime.getTime());
                endTime = endOfDay(calendarEndTime.getTime());
                break;

            case "Last 90 Days":
                calendarStartTime.set(Calendar.DAY_OF_YEAR, calendarStartTime.get(Calendar.DAY_OF_YEAR) - 90);
                startTime = beginOfDay(calendarStartTime.getTime());
                endTime = endOfDay(calendarEndTime.getTime());
                break;

            case "Over 90 Days":
                calendarEndTime.set(Calendar.DAY_OF_YEAR, calendarEndTime.get(Calendar.DAY_OF_YEAR) - 90);
                endTime = endOfDay(calendarEndTime.getTime());
        }
        ///AllCase
        switch (type) {
            case "All":
                if (ContactCode.equalsIgnoreCase("ALL")) {
                    cursor = db.rawQuery("Select * from CONTACTS", null);
                } else {
                    cursor = db.rawQuery("Select * from CONTACTS WHERE Contactcode = ?", new String[]{ContactCode + ""});
                }
                break;
            case "Favorites":
                if (ContactCode.equalsIgnoreCase("ALL")) {
                    cursor = db.rawQuery("Select * from CONTACTS WHERE favorite = \"true\"", null);
                } else {
                    cursor = db.rawQuery("Select * from CONTACTS WHERE favorite = \"true\" AND createdOn BETWEEN " + startTime + " AND " + endTime + " AND Contactcode = ?", new String[]{ContactCode + ""});
                }
                break;
            default:
                if (ContactCode.equalsIgnoreCase("ALL")) {
                    cursor = db.rawQuery("Select * from CONTACTS WHERE createdOn BETWEEN " + startTime + " AND " + endTime, null);
                } else {
                    cursor = db.rawQuery("Select * from CONTACTS WHERE createdOn BETWEEN " + startTime + " AND " + endTime + " AND Contactcode = ?", new String[]{ContactCode + ""});
                }

        }
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String FirstName = cursor.getString(cursor.getColumnIndex(CONTACTS_FIRSTNAME));
                String MiddleName = cursor.getString(cursor.getColumnIndex(CONTACTS_MIDDLENAME));
                String LastName = cursor.getString(cursor.getColumnIndex(CONTACTS_LASTNAME));
                String Phone = cursor.getString(cursor.getColumnIndex(CONTACT_PHONE_NUMBER));
                String Email = cursor.getString(cursor.getColumnIndex(CONTACTS_EMAIL_ADDRESS));
                String Image = cursor.getString(cursor.getColumnIndex(CONTACTS_PHOTO));
                String ContactID = cursor.getString(cursor.getColumnIndex(CONTACTS_CONT_ID));

                if (nullCheck(FirstName)) {
                    FirstName = "";
                }

                if (nullCheck(MiddleName)) {
                    MiddleName = "";
                }
                if (nullCheck(LastName)) {
                    LastName = "";
                }

                String Name = FirstName.trim() + " " + MiddleName.trim() + " " + LastName.trim();
                Name.replaceAll("\\s+", " ");

                if (nullCheck(Name)) {
                    if (!nullCheck(Phone)) {
                        Name = Phone;
                    } else {
                        Name = Email;
                    }
                }
                ContactsList.add(new Contacts(Name, ContactID, Image));
                cursor.moveToNext();
            }
        }
        return ContactsList;
    }

    public ArrayList<Contacts> getBirthdayContactsBetweenTime(String type) {

        ArrayList<Contacts> ContactsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        Calendar calendarStartTime = Calendar.getInstance();

        Calendar calendarEndTime = Calendar.getInstance();

        long startTime = 0;

        long endTime = 0;

        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy @ hh:mm a");

        switch (type) {
            case "Last Month":
                calendarStartTime.set(Calendar.MONTH, calendarStartTime.get(Calendar.MONTH) - 1);
                calendarStartTime.set(Calendar.DAY_OF_MONTH, 1);
                startTime = beginOfDay(calendarStartTime.getTime());

                calendarEndTime.set(Calendar.MONTH, calendarEndTime.get(Calendar.MONTH) - 1);
                calendarEndTime.set(Calendar.DAY_OF_MONTH, 30);
                endTime = endOfDay(calendarEndTime.getTime());
                break;

            case "Lastweek":
                calendarStartTime.set(Calendar.WEEK_OF_MONTH, calendarStartTime.get(Calendar.WEEK_OF_MONTH) - 1);
                calendarStartTime.set(Calendar.DAY_OF_WEEK, 1);
                startTime = beginOfDay(calendarStartTime.getTime());

                calendarEndTime.set(Calendar.WEEK_OF_MONTH, calendarEndTime.get(Calendar.WEEK_OF_MONTH) - 1);
                calendarEndTime.set(Calendar.DAY_OF_WEEK, 7);
                endTime = endOfDay(calendarEndTime.getTime());
                break;

            case "Yesterday":
                calendarStartTime.set(Calendar.DAY_OF_YEAR, calendarStartTime.get(Calendar.DAY_OF_YEAR) - 1);
                startTime = beginOfDay(calendarStartTime.getTime());

                calendarEndTime.set(Calendar.DAY_OF_YEAR, calendarEndTime.get(Calendar.DAY_OF_YEAR) - 1);
                endTime = endOfDay(calendarEndTime.getTime());
                break;

            case "Today":
                startTime = beginOfDay(calendarStartTime.getTime());
                endTime = endOfDay(calendarEndTime.getTime());
                break;

            case "Tomorrow":
                calendarStartTime.set(Calendar.DAY_OF_YEAR, calendarStartTime.get(Calendar.DAY_OF_YEAR) + 1);
                startTime = beginOfDay(calendarStartTime.getTime());


                calendarEndTime.set(Calendar.DAY_OF_YEAR, calendarEndTime.get(Calendar.DAY_OF_YEAR) + 1);
                endTime = endOfDay(calendarEndTime.getTime());

                break;

            case "This Week":
                calendarStartTime.set(Calendar.DAY_OF_WEEK, 1);
                startTime = beginOfDay(calendarStartTime.getTime());

                calendarEndTime.set(Calendar.DAY_OF_WEEK, 7);
                endTime = endOfDay(calendarEndTime.getTime());
                break;

            case "This Month":
                calendarStartTime.set(Calendar.DAY_OF_MONTH, 1);
                startTime = beginOfDay(calendarStartTime.getTime());

                calendarEndTime.set(Calendar.DAY_OF_MONTH, 30);
                endTime = endOfDay(calendarEndTime.getTime());

                break;

            case "Next Week":
                calendarStartTime.set(Calendar.WEEK_OF_MONTH, calendarStartTime.get(Calendar.WEEK_OF_MONTH) + 1);
                calendarStartTime.set(Calendar.DAY_OF_WEEK, 1);
                startTime = beginOfDay(calendarStartTime.getTime());

                calendarEndTime.set(Calendar.WEEK_OF_MONTH, calendarEndTime.get(Calendar.WEEK_OF_MONTH) + 1);
                calendarEndTime.set(Calendar.DAY_OF_WEEK, 7);
                endTime = endOfDay(calendarEndTime.getTime());
                break;


            case "Next Month":
                calendarStartTime.set(Calendar.MONTH, calendarStartTime.get(Calendar.MONTH) + 1);
                calendarStartTime.set(Calendar.DAY_OF_MONTH, 1);
                startTime = beginOfDay(calendarStartTime.getTime());

                calendarEndTime.set(Calendar.MONTH, calendarEndTime.get(Calendar.MONTH) + 1);
                calendarEndTime.set(Calendar.DAY_OF_MONTH, 30);

                endTime = endOfDay(calendarEndTime.getTime());
                break;
        }

        ///AllCase
        switch (type) {
            case "All Birthday":
                cursor = db.rawQuery("Select * from  CONTACTS where dateOfBirth != \"null\"", null);
                break;

            case "No Birthday":
                cursor = db.rawQuery("Select * from  CONTACTS where dateOfBirth = \"null\"", null);
                break;

            default:
                cursor = db.rawQuery("Select * from CONTACTS WHERE dateOfBirth BETWEEN " + startTime + " AND " + endTime + "", null);
        }
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                String FirstName = cursor.getString(cursor.getColumnIndex(CONTACTS_FIRSTNAME));
                String MiddleName = cursor.getString(cursor.getColumnIndex(CONTACTS_MIDDLENAME));
                String LastName = cursor.getString(cursor.getColumnIndex(CONTACTS_LASTNAME));
                String Phone = cursor.getString(cursor.getColumnIndex(CONTACT_PHONE_NUMBER));
                String Email = cursor.getString(cursor.getColumnIndex(CONTACTS_EMAIL_ADDRESS));
                String Image = cursor.getString(cursor.getColumnIndex(CONTACTS_PHOTO));
                String ContactID = cursor.getString(cursor.getColumnIndex(CONTACTS_CONT_ID));

                if (nullCheck(FirstName)) {
                    FirstName = "";
                }

                if (nullCheck(MiddleName)) {
                    MiddleName = "";
                }
                if (nullCheck(LastName)) {
                    LastName = "";
                }

                String Name = FirstName.trim() + " " + MiddleName.trim() + " " + LastName.trim();
                Name.replaceAll("\\s+", " ");

                if (nullCheck(Name)) {
                    if (!nullCheck(Phone)) {
                        Name = Phone;
                    } else {
                        Name = Email;
                    }
                }
                ContactsList.add(new Contacts(Name, ContactID, Image));
                cursor.moveToNext();
            }
        }
        return ContactsList;
    }

    public long beginOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTimeInMillis();
    }

    public long endOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTimeInMillis();
    }

    public int getAllBirthdaylen() {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("Select * from  CONTACTS where dateOfBirth != \"null\"", null);
        int favcnt = cursor.getCount();
        return favcnt;
    }

    public String getContactCode(String id) {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String contactCode = "";
        cursor = db.rawQuery("SELECT contactCode FROM CONTACTS WHERE contactid=?", new String[]{id + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            contactCode = cursor.getString(cursor.getColumnIndex(CONTACTS_CONTACT_CODE));
        }
        return contactCode;
    }

    public String getContactsPhoto(String id) {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String photo = "";
        cursor = db.rawQuery("SELECT photo FROM CONTACTS WHERE contactid=?", new String[]{id + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            photo = cursor.getString(cursor.getColumnIndex(CONTACTS_PHOTO));
        }
        return photo;
    }

    //All update Methods for updating contacts //
    public int changeContactUpdatedON(String contactid, String updateTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_UPDATED_ON, updateTime);
        return db.update("CONTACTS", contentValues, CONTACTS_CONT_ID + " = ?", new String[]{contactid});
    }

    public int updateContactNotes(String contactId, String Notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_NOTES, Notes);
        return db.update("CONTACTS", contentValues, CONTACTS_CONT_ID + " = ?", new String[]{contactId});
    }

    public int changeContactFavorite(String contactId, String fab) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACT_FAVORITE, fab);
        return db.update("CONTACTS", contentValues, CONTACTS_CONT_ID + " = ?", new String[]{contactId});
    }

    public int changeContactCode(String contactId, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_CONTACT_CODE, type);
        return db.update("CONTACTS", contentValues, CONTACTS_CONT_ID + " = ?", new String[]{contactId});
    }

    // ------------------------ "user" table methods ----------------//
    public boolean insertuserTable(String nam, String ln, String email, String orgemail, String orgtype, String entitytype, String indtype,
                                   String regno, String taxno, String siteurl, String siteprovider, String fburl, String liurl,
                                   String twurl, String fbid, String liid, String twid, String orgid, String planid, String apikey,
                                   String twilioid, String twiliotoken, String googlekey, String demo, String mobile, String emailroute,
                                   String textroute, String phoneroute, String user, String contactTypes, String restriction, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_ORG_NAME, nam);
        contentValues.put(CONTACTS_ORG_LEGALNAME, ln);
        contentValues.put(CONTACTS_ORG_EMAIL, email);
        contentValues.put(CONTACTS_ORG_ORGINALEMAIL, orgemail);
        contentValues.put(CONTACTS_ORG_TYPE, orgtype);
        contentValues.put(CONTACTS_ORG_ENTITYTYPE, entitytype);
        contentValues.put(CONTACTS_ORG_INDUSTRYTYPE, indtype);
        contentValues.put(CONTACTS_ORG_REGNO, regno);
        contentValues.put(CONTACTS_ORG_TAXNO, taxno);
        contentValues.put(CONTACTS_ORG_SITEURL, siteurl);
        contentValues.put(CONTACTS_ORG_SITEPROVIDER, siteprovider);
        contentValues.put(CONTACTS_ORG_FBURL, fburl);
        contentValues.put(CONTACTS_ORG_LIURL, liurl);
        contentValues.put(CONTACTS_ORG_TWURL, twurl);
        contentValues.put(CONTACTS_ORG_FBID, fbid);
        contentValues.put(CONTACTS_ORG_LIID, liid);
        contentValues.put(CONTACTS_ORG_TWID, twid);
        contentValues.put(CONTACTS_ORG_ID, orgid);
        contentValues.put(CONTACTS_ORG_PLANID, planid);
        contentValues.put(CONTACTS_ORG_APIKEY, apikey);
        contentValues.put(CONTACTS_ORG_TWILIOID, twilioid);
        contentValues.put(CONTACTS_ORG_TWILIOTOKEN, twiliotoken);
        contentValues.put(CONTACTS_ORG_GOOGLEKEY, googlekey);
        contentValues.put(CONTACTS_ORG_DEMO, demo);
        contentValues.put(CONTACTS_ORG_PHONE_NUMBER, mobile);
        contentValues.put(CONTACTS_ORG_EMAIL_LEAD_ROUTE, emailroute);
        contentValues.put(CONTACTS_ORG_TEXT_LEAD_ROUTE, textroute);
        contentValues.put(CONTACTS_ORG_PHONE_LEAD_ROUTE, phoneroute);
        contentValues.put(CONTACT_ORG_USER, user);
        contentValues.put(CONTACTS_ORG_CONTACTS_TYPE, contactTypes);
        contentValues.put(CONTACTS_ORG_RESTRICTION, restriction);
        contentValues.put(CONTACTS_ORG_STATUS, status);
        db.insert("USERS", null, contentValues);
        return true;
    }

    public String getRestriction() {

        String restriction = null;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor res = db.rawQuery("select restriction from USERS", null);
            if (res != null) {
                res.moveToFirst();
                while (res.isAfterLast() == false) {
                    restriction = res.getString(res.getColumnIndex("restriction"));
                    res.moveToNext();
                }
            } else {
                Log.e(TAG, "select restriction from USERS return null curser");
            }
        } catch (Exception e) {
            Log.e(TAG, "No Such Curser is Empty ");
            return restriction;
        }

        return restriction;
    }

    public int updateRestriction(String restriction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_ORG_RESTRICTION, restriction);
        return db.update("USERS", contentValues, null, null);
    }

    public int updateContactsTypes(String ContactsTypes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_ORG_CONTACTS_TYPE, ContactsTypes);
        return db.update("USERS", contentValues, null, null);
    }

    public String getOrginalEmail() {
        String email = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select email from USERS", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            email = res.getString(res.getColumnIndex("email"));

            res.moveToNext();
        }
        return email;
    }

    public String getPhone() {
        String phoneNumber = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select phoneNumber from USERS", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            phoneNumber = res.getString(res.getColumnIndex("phoneNumber"));

            res.moveToNext();
        }
        return phoneNumber;
    }

    public String getEmailRoute() {
        String phoneNumber = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select emailLeadRoute from USERS", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            phoneNumber = res.getString(res.getColumnIndex("emailLeadRoute"));

            res.moveToNext();
        }
        return phoneNumber;
    }

    public String getTextRoute() {
        String phoneNumber = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select textLeadRoute from USERS", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            phoneNumber = res.getString(res.getColumnIndex("textLeadRoute"));

            res.moveToNext();
        }
        return phoneNumber;
    }

    public String getPhoneRoute() {
        String phoneNumber = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select phoneLeadRoute from USERS", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            phoneNumber = res.getString(res.getColumnIndex("phoneLeadRoute"));
            res.moveToNext();
        }
        return phoneNumber;
    }

    public String getUserData() {
        String phoneNumber = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from USERS", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            phoneNumber = res.getString(res.getColumnIndex(CONTACT_ORG_USER));

            res.moveToNext();
        }
        return phoneNumber;
    }

    public int updateUserData(String userData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACT_ORG_USER, userData);
        return db.update("USERS", contentValues, null, null);
    }

    public int updatePhoneRoute(String phoneRoute) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_ORG_PHONE_LEAD_ROUTE, phoneRoute);
        return db.update("USERS", contentValues, null, null);
    }

    public int updateEmailRoute(String emailRoute) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_ORG_EMAIL_LEAD_ROUTE, emailRoute);
        return db.update("USERS", contentValues, null, null);
    }

    public int updateTextRoute(String textRoute) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_ORG_TEXT_LEAD_ROUTE, textRoute);
        return db.update("USERS", contentValues, null, null);
    }

    //created by srn -----------------IMPORT CONTACT FUNCTIONS----------------------//
    public String getAllContactsFromInterests(String Itid) {
        SQLiteDatabase db = this.getReadableDatabase();
        String contacts = null;
        Cursor res = db.rawQuery("select * from INTERESTS where Itid =?", new String[]{Itid});
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            contacts = res.getString(res.getColumnIndex(CONTACTS_INTEREST_CONTACTID));
            res.moveToNext();
        }
        return contacts;
    }

    //table for user profile setup
    public boolean updateAppUserData(JSONObject jsonObject) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put(PROFILE_CONTACTS_LASTUPDATE, jsonObject.getString("updatedOn"));
            contentValues.put(PROFILE_CONTACTS_ACCOUNT_NAME, jsonObject.getString("name"));
            contentValues.put(PROFILE_CONTACTS_IMAGELOGO, jsonObject.getString("logo"));
            contentValues.put(PROFILE_CONTACTS_TIMEZONE, jsonObject.getString("baseTimezone"));
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("addresses");
                for (int m = 0; m < jsonArray.length(); m++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(m);
                    contentValues.put(PROFILE_CONTACTS_ADDRESS_STREET, jsonObject1.getString("addressLine1"));
                    contentValues.put(PROFILE_CONTACTS_ADDRESS_TOWN, jsonObject1.getString("city"));
                    contentValues.put(PROFILE_CONTACTS_ADDRESS_PIN, jsonObject1.getString("postalCode"));
                    contentValues.put(PROFILE_CONTACTS_ADDRESS_STATE, jsonObject1.getString("region"));
                    contentValues.put(PROFILE_CONTACTS_ADDRESS_COUNTRY, jsonObject1.getString("country"));
                }
            } catch (Exception e) {
                Log.e(TAG, "USER_PROFILE_PARSING --->error in parsing addresses array");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.insert("APPUSERPROFILE", null, contentValues);
        return true;
    }

    public boolean updateAppUserData(JSONArray jsonArray) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try {
            JSONObject jsonObject = new JSONObject(jsonArray.getJSONObject(0).toString());
            contentValues.put(PROFILE_CONTACTS_U_ID, jsonObject.getString("id"));
            contentValues.put(PROFILE_CONTACTS_FIRSTNAME, jsonObject.getString("firstName"));
            contentValues.put(PROFILE_CONTACTS_LASTNAME, jsonObject.getString("lastName"));
            contentValues.put(PROFILE_CONTACTS_EMAILID, jsonObject.getString("emailAddress"));
            contentValues.put(PROFILE_CONTACTS_API, jsonObject.getString("apnsToken"));
            JSONArray phonesArray = jsonObject.getJSONArray("phones");
            for (int i = 0; i < phonesArray.length(); i++) {
                JSONObject PhoneType = phonesArray.getJSONObject(i);
                switch (PhoneType.getString("phoneType")) {
                    case "MOBILE":
                        contentValues.put(PROFILE_CONTACTS_MOBILE_NO, PhoneType.getString("phoneNumber"));
                        break;
                    case "DEFAULT":
                        contentValues.put(PROFILE_CONTACTS_BUSINESS_NO, PhoneType.getString("phoneNumber"));
                        break;
                    case "HOME":
                        contentValues.put(PROFILE_CONTACTS_HOME_NO, PhoneType.getString("phoneNumber"));
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.update("APPUSERPROFILE", contentValues, PROFILE_CONTACTS_ID + " = ?", new String[]{String.valueOf(1)});
        return true;
    }

    public String getUserMobileNumber() {
        String userMobile = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from APPUSERPROFILE", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            userMobile = res.getString(res.getColumnIndex(PROFILE_CONTACTS_MOBILE_NO));
            res.moveToNext();
        }
        return userMobile;
    }

    public String getUserID() {
        String userID = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from APPUSERPROFILE", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            userID = res.getString(res.getColumnIndex(PROFILE_CONTACTS_U_ID));
            res.moveToNext();
        }
        return userID;
    }

    public boolean updateUserProfile(ArrayList<String> data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PROFILE_CONTACTS_LASTUPDATE, data.get(0));
        contentValues.put(PROFILE_CONTACTS_ACCOUNT_NAME, data.get(1));
        contentValues.put(PROFILE_CONTACTS_FIRSTNAME, data.get(2));
        contentValues.put(PROFILE_CONTACTS_LASTNAME, data.get(3));
        contentValues.put(PROFILE_CONTACTS_MOBILE_NO, data.get(4));
        contentValues.put(PROFILE_CONTACTS_BUSINESS_NO, data.get(5));
        contentValues.put(PROFILE_CONTACTS_HOME_NO, data.get(6));
        contentValues.put(PROFILE_CONTACTS_TIMEZONE, data.get(7));
        contentValues.put(PROFILE_CONTACTS_ADDRESS_STREET, data.get(8));
        contentValues.put(PROFILE_CONTACTS_ADDRESS_TOWN, data.get(9));
        contentValues.put(PROFILE_CONTACTS_ADDRESS_PIN, data.get(10));
        contentValues.put(PROFILE_CONTACTS_ADDRESS_STATE, data.get(11));
        contentValues.put(PROFILE_CONTACTS_ADDRESS_COUNTRY, data.get(12));
        db.update("APPUSERPROFILE", contentValues, PROFILE_CONTACTS_ID + " = ?", new String[]{String.valueOf(1)});
        return true;
    }

    public String getUserLogo() {
        String Logo = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select LogoImage from APPUSERPROFILE", null);
        if (res.getCount() > 0) {
            res.moveToFirst();
            while (res.isAfterLast() == false) {
                Logo = res.getString(res.getColumnIndex(PROFILE_CONTACTS_IMAGELOGO));
                res.moveToNext();
            }
        } else {
            Log.e(TAG, "getUserLogo() --->Logo Not Found");
        }
        return Logo;
    }

    public boolean updateUserLogo(String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PROFILE_CONTACTS_IMAGELOGO, data);
        db.update("APPUSERPROFILE", contentValues, PROFILE_CONTACTS_ID + " = ?", new String[]{String.valueOf(1)});
        return true;
    }

    public ArrayList<String> getUserProfileData() {
        ArrayList<String> userdata = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from APPUSERPROFILE where id =1", null);
        res.moveToFirst();
        if (res.getCount() > 0) {
            while (res.isAfterLast() == false) {
                userdata.add((res.getString(res.getColumnIndex(PROFILE_CONTACTS_ACCOUNT_NAME))));
                userdata.add((res.getString(res.getColumnIndex(PROFILE_CONTACTS_FIRSTNAME))));
                userdata.add((res.getString(res.getColumnIndex(PROFILE_CONTACTS_LASTNAME))));
                userdata.add((res.getString(res.getColumnIndex(PROFILE_CONTACTS_MOBILE_NO))));
                userdata.add((res.getString(res.getColumnIndex(PROFILE_CONTACTS_BUSINESS_NO))));
                userdata.add((res.getString(res.getColumnIndex(PROFILE_CONTACTS_HOME_NO))));
                userdata.add((res.getString(res.getColumnIndex(PROFILE_CONTACTS_EMAILID))));
                userdata.add((res.getString(res.getColumnIndex(PROFILE_CONTACTS_TIMEZONE))));
                userdata.add((res.getString(res.getColumnIndex(PROFILE_CONTACTS_ADDRESS_STREET))));
                userdata.add((res.getString(res.getColumnIndex(PROFILE_CONTACTS_ADDRESS_TOWN))));
                userdata.add((res.getString(res.getColumnIndex(PROFILE_CONTACTS_ADDRESS_PIN))));
                userdata.add((res.getString(res.getColumnIndex(PROFILE_CONTACTS_ADDRESS_STATE))));
                userdata.add((res.getString(res.getColumnIndex(PROFILE_CONTACTS_ADDRESS_COUNTRY))));
                userdata.add((res.getString(res.getColumnIndex(PROFILE_CONTACTS_IMAGELOGO))));
                userdata.add((res.getString(res.getColumnIndex(PROFILE_CONTACTS_API))));
                res.moveToNext();
            }
        } else {
            Log.i(TAG, "Fail to get data from getUserProfileData() please check it");
        }
        return userdata;
    }

    public ArrayList<String> getOrganizationContacts() {
        ArrayList<String> userdata = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from APPUSERPROFILE where id =1", null);
        res.moveToFirst();
        if (res.getCount() > 0) {
            while (res.isAfterLast() == false) {
                userdata.add((res.getString(res.getColumnIndex(PROFILE_CONTACTS_MOBILE_NO))));
                userdata.add((res.getString(res.getColumnIndex(PROFILE_CONTACTS_BUSINESS_NO))));
                userdata.add((res.getString(res.getColumnIndex(PROFILE_CONTACTS_HOME_NO))));
                res.moveToNext();
            }
        } else {
            Log.i(TAG, "Fail to get data from getUserProfileData() please check it");
        }


        return userdata;
    }

    public String getUserProfileUserID() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from APPUSERPROFILE where id =1", null);
        String Data = null;
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            Data = res.getString(res.getColumnIndex(PROFILE_CONTACTS_U_ID));
            res.moveToNext();
        }
        return Data;
    }

    public boolean importContacts(String IMC_id, String mobile, String type, String name, String email, String photo, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMP_CONTACTS_IMC_id, IMC_id);
        contentValues.put(IMP_CONTACTS_MOBILE, mobile);
        contentValues.put(IMP_CONTACTS_TYPE, type);
        contentValues.put(IMP_CONTACTS_NAME, name);
        contentValues.put(IMP_CONTACTS_EMAIL, email);
        contentValues.put(IMP_CONTACTS_PHOTO, photo);
        contentValues.put(IMP_CONTACTS_ADDRESS, address);
        db.insert("IMPORTCONTACTS", null, contentValues);
        return true;
    }

    public String getOrganizationId() {
        String org_id = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from USERS", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            org_id = res.getString(res.getColumnIndex(CONTACTS_ORG_ID));
            res.moveToNext();
        }
        return org_id;
    }

    public ArrayList<Contacts> getContactsNames() {
        ArrayList<Contacts> countryList = new ArrayList<Contacts>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from IMPORTCONTACTS", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            String id = res.getString(res.getColumnIndex(IMP_CONTACTS_IMC_id));
            String name = res.getString(res.getColumnIndex(IMP_CONTACTS_NAME));
            String mobile = res.getString(res.getColumnIndex(IMP_CONTACTS_MOBILE));
            String type = res.getString(res.getColumnIndex(IMP_CONTACTS_TYPE));
            String email = res.getString(res.getColumnIndex(IMP_CONTACTS_EMAIL));
            Contacts contact = new Contacts(mobile, type, name, id, email, false);
            countryList.add(contact);
            res.moveToNext();
        }
        return countryList;
    }

    //return single day full data
    public ArrayList getContactsByItsContactType(String ContactType) {
        ArrayList rawdata = new ArrayList();
        String contacts = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Contacts where contactcode =?", new String[]{ContactType});
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            contacts = res.getString(res.getColumnIndex(CONTACTS_CONT_ID));
            rawdata.add(contacts);
            res.moveToNext();
        }
        return rawdata;
    }


    //-----------------get current day status by passing day id------------------//
    public boolean insertTempBusinessHours(int i) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BUS_CONTACTS_DAY_OF_WEEK, i);
        contentValues.put(BUS_CONTACTS_OPEN_HOUR, 9);
        contentValues.put(BUS_CONTACTS_OPEN_MIN, 0);
        contentValues.put(BUS_CONTACTS_CLOSE_HOUR, 5);
        contentValues.put(BUS_CONTACTS_CLOSE_MIN, 0);
        contentValues.put(BUS_CONTACTS_BY_APPOINTMENT, "false");
        if (i > 4) {
            contentValues.put(BUS_CONTACTS_CLOSE_STATUS, "true");
        } else {
            contentValues.put(BUS_CONTACTS_CLOSE_STATUS, "false");
        }
        db.insert("BUSINESSHOURS", null, contentValues);
        return true;
    }

    public boolean insertBusinessHours(JSONObject jsonObject1) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            JSONObject hoursOfOperation = new JSONObject(jsonObject1.get("hoursOfOperation").toString());
            if (hoursOfOperation != null) {
                try {
                    JSONArray includeds = hoursOfOperation.getJSONArray("includeds");
                    for (int k = 0; k < includeds.length(); k++) {
                        JSONObject singleday = new JSONObject(includeds.getJSONObject(k).toString());
                        int dayOfWeek = Integer.parseInt(String.valueOf(singleday.get("dayOfWeek")));
                        int openOnHour = Integer.parseInt(String.valueOf(singleday.get("openOnHour")));
                        int openOnMinute = Integer.parseInt(String.valueOf(singleday.get("openOnMinute")));
                        int closeOnHour = Integer.parseInt(String.valueOf(singleday.get("closeOnHour")));
                        int closeOnMinute = Integer.parseInt(String.valueOf(singleday.get("closeOnMinute")));
                        String byAppointmentOnly = String.valueOf(singleday.get("byAppointmentOnly"));
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(BUS_CONTACTS_DAY_OF_WEEK, dayOfWeek);
                        contentValues.put(BUS_CONTACTS_OPEN_HOUR, openOnHour);
                        contentValues.put(BUS_CONTACTS_OPEN_MIN, openOnMinute);
                        contentValues.put(BUS_CONTACTS_CLOSE_HOUR, closeOnHour);
                        contentValues.put(BUS_CONTACTS_CLOSE_MIN, closeOnMinute);
                        contentValues.put(BUS_CONTACTS_BY_APPOINTMENT, byAppointmentOnly);
                        contentValues.put(BUS_CONTACTS_CLOSE_STATUS, "false");
                        db.insert("BUSINESSHOURS", null, contentValues);
                    }
                } catch (Exception e) {
                    Log.i(TAG, "Day Parsing error  ---> Day of week parsing error Includes");
                }
                try {
                    JSONArray excludeds = hoursOfOperation.getJSONArray("excludeds");
                    for (int k = 0; k < excludeds.length(); k++) {
                        JSONObject singleday = new JSONObject(excludeds.getJSONObject(k).toString());
                        int dayOfWeek = Integer.parseInt(String.valueOf(singleday.get("dayOfWeek")));
                        int openOnHour = Integer.parseInt(String.valueOf(singleday.get("openOnHour")));
                        int openOnMinute = Integer.parseInt(String.valueOf(singleday.get("openOnMinute")));
                        int closeOnHour = Integer.parseInt(String.valueOf(singleday.get("closeOnHour")));
                        int closeOnMinute = Integer.parseInt(String.valueOf(singleday.get("closeOnMinute")));
                        String byAppointmentOnly = String.valueOf(singleday.get("byAppointmentOnly"));
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(BUS_CONTACTS_DAY_OF_WEEK, dayOfWeek);
                        contentValues.put(BUS_CONTACTS_OPEN_HOUR, openOnHour);
                        contentValues.put(BUS_CONTACTS_OPEN_MIN, openOnMinute);
                        contentValues.put(BUS_CONTACTS_CLOSE_HOUR, closeOnHour);
                        contentValues.put(BUS_CONTACTS_CLOSE_MIN, closeOnMinute);
                        contentValues.put(BUS_CONTACTS_BY_APPOINTMENT, byAppointmentOnly);
                        contentValues.put(BUS_CONTACTS_CLOSE_STATUS, "true");
                        db.insert("BUSINESSHOURS", null, contentValues);
                    }
                } catch (Exception e) {
                    Log.i(TAG, "Day Parsing error -->Day of week parsing error Excludes");
                }

            } else {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;

    }

    public String getBusinessHoursDetail(int currentday) {
        String data = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from BUSINESSHOURS where dayOfWeek =" + currentday, null);
        if (res.getCount() > 0) {
            res.moveToFirst();
            while (res.isAfterLast() == false) {
                String dayOfWeek = res.getString(res.getColumnIndex(BUS_CONTACTS_DAY_OF_WEEK));
                int openOnHour = res.getInt(res.getColumnIndex(BUS_CONTACTS_OPEN_HOUR));
                int openOnMinute = res.getInt(res.getColumnIndex(BUS_CONTACTS_OPEN_MIN));
                int closeOnHour = res.getInt(res.getColumnIndex(BUS_CONTACTS_CLOSE_HOUR));
                int closeOnMinute = res.getInt(res.getColumnIndex(BUS_CONTACTS_CLOSE_MIN));
                String byAppointmentOnly = res.getString(res.getColumnIndex(BUS_CONTACTS_BY_APPOINTMENT));
                String close = res.getString(res.getColumnIndex(BUS_CONTACTS_CLOSE_STATUS));
                if (close.equals("true")) {
                    data = "Closed";
                } else if (byAppointmentOnly.equals("true")) {
                    data = "By Appointment";
                } else {
                    data = String.format("%02d", getOpenHours(openOnHour)) + ":" + String.format("%02d", openOnMinute) + " AM" + "-" + String.format("%02d", getCloseHours(closeOnHour)) + ":" + String.format("%02d", closeOnMinute) + " PM";
                }
                res.moveToNext();
            }
        }
        if (data != null)
            return data;
        else return "";
    }

    private int getOpenHours(int data) {
        if (data == 0) {
            return 9;
        } else {
            return data;
        }

    }

    private int getCloseHours(int data) {
        if (data == 0) {
            return 5;
        } else {
            return data;
        }
    }

    public ArrayList getBusinessSingleDay(int currentday) {
        ArrayList daydata = new ArrayList();
        String data = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from BUSINESSHOURS where dayOfWeek =" + currentday, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            String dayOfWeek = res.getString(res.getColumnIndex(BUS_CONTACTS_DAY_OF_WEEK));
            String openOnHour = res.getString(res.getColumnIndex(BUS_CONTACTS_OPEN_HOUR));
            String openOnMinute = res.getString(res.getColumnIndex(BUS_CONTACTS_OPEN_MIN));
            String closeOnHour = res.getString(res.getColumnIndex(BUS_CONTACTS_CLOSE_HOUR));
            String closeOnMinute = res.getString(res.getColumnIndex(BUS_CONTACTS_CLOSE_MIN));
            String byAppointmentOnly = res.getString(res.getColumnIndex(BUS_CONTACTS_BY_APPOINTMENT));
            String close = res.getString(res.getColumnIndex(BUS_CONTACTS_CLOSE_STATUS));
            daydata.add(dayOfWeek);
            daydata.add(openOnHour);
            daydata.add(openOnMinute);
            daydata.add(closeOnHour);
            daydata.add(closeOnMinute);
            daydata.add(byAppointmentOnly);
            daydata.add(close);
            res.moveToNext();
        }
        return daydata;
    }

    public BusinessHours getBusinessSingleDayModel(int currentday) {
        BusinessHours BH = new BusinessHours();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from BUSINESSHOURS where dayOfWeek =" + currentday, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            BH.setDayOfWeek(res.getInt(res.getColumnIndex(BUS_CONTACTS_DAY_OF_WEEK)));
            BH.setOpenOnHour(res.getInt(res.getColumnIndex(BUS_CONTACTS_OPEN_HOUR)));
            BH.setOpenOnMinute(res.getInt(res.getColumnIndex(BUS_CONTACTS_OPEN_MIN)));
            BH.setCloseOnHour(res.getInt(res.getColumnIndex(BUS_CONTACTS_CLOSE_HOUR)));
            BH.setCloseOnMinute(res.getInt(res.getColumnIndex(BUS_CONTACTS_CLOSE_MIN)));
            BH.setByAppointmentOnly(Boolean.valueOf(res.getString(res.getColumnIndex(BUS_CONTACTS_BY_APPOINTMENT))));
            BH.setClosed(Boolean.valueOf(res.getString(res.getColumnIndex(BUS_CONTACTS_CLOSE_STATUS))));
            res.moveToNext();
        }
        return BH;
    }


    //Synic data
    public int getWorkingDaysCount() {
        String dayOfWeek = null;
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from BUSINESSHOURS ", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            dayOfWeek = res.getString(res.getColumnIndex(BUS_CONTACTS_DAY_OF_WEEK));
            if (dayOfWeek != null) {
                arrayList.add(dayOfWeek);
            }
            res.moveToNext();
        }
        return res.getCount();
    }

    // ------------------------ "campaigncontact" table methods ----------------//
    public boolean insertCampaignView(String id, String name, String date, String count, String email, String text, String voice, String activities) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CAMPAIGN_VIEW_ID, id);
        contentValues.put(CAMPAIGN_VIEW_NAME, name);
        contentValues.put(CAMPAIGN_VIEW_DATE, date);
        contentValues.put(CAMPAIGN_VIEW_COUNT, count);
        contentValues.put(CAMPAIGN_VIEW_EMAIL, email);
        contentValues.put(CAMPAIGN_VIEW_TEXT, text);
        contentValues.put(CAMPAIGN_VIEW_VOICE, voice);
        contentValues.put(CAMPAIGN_VIEW_ACTIVITIES, activities);
        db.insert("CAMPAIGNVIEW", null, contentValues);
        return true;
    }

    public ArrayList<CampaignPreviewData> getCampaignViewAllData() {
        ArrayList<CampaignPreviewData> campaigndata = new ArrayList<CampaignPreviewData>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM CAMPAIGNVIEW ORDER BY CV_date DESC", null);

        res.moveToFirst();
        while (res.isAfterLast() == false) {
            String id = res.getString(res.getColumnIndex(CAMPAIGN_VIEW_ID));
            String name = res.getString(res.getColumnIndex(CAMPAIGN_VIEW_NAME));
            String date = res.getString(res.getColumnIndex(CAMPAIGN_VIEW_DATE));
            String count = res.getString(res.getColumnIndex(CAMPAIGN_VIEW_COUNT));
            String email = res.getString(res.getColumnIndex(CAMPAIGN_VIEW_EMAIL));
            String text = res.getString(res.getColumnIndex(CAMPAIGN_VIEW_TEXT));
            String voice = res.getString(res.getColumnIndex(CAMPAIGN_VIEW_VOICE));
            String activities = res.getString(res.getColumnIndex(CAMPAIGN_VIEW_ACTIVITIES));
            CampaignPreviewData campaignPreviewData = new CampaignPreviewData(id, name, date, count, email, text, voice, activities);
            campaigndata.add(campaignPreviewData);
            res.moveToNext();
        }
        return campaigndata;
    }

    // ------------------------ "Notification" table methods ----------------//

    public boolean insertNotification(JSONObject jsonobject) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put(NOTIFICATION_TITLE, jsonobject.getString("title"));
            contentValues.put(NOTIFICATION_MESSAGE, "");
            contentValues.put(NOTIFICATION_SOURCE, jsonobject.getString("source"));
            contentValues.put(NOTIFICATION_READ, jsonobject.getString("read"));
            contentValues.put(NOTIFICATION_READ_DATE, jsonobject.getString("readDate"));
            contentValues.put(NOTIFICATION_COMMENT, jsonobject.getString("comment"));
            contentValues.put(NOTIFICATION_ID, jsonobject.getString("id"));
            contentValues.put(NOTIFICATION_ORGID, jsonobject.getString("orgId"));
            contentValues.put(NOTIFICATION_CONTACTID, jsonobject.getString("contactId"));
            contentValues.put(NOTIFICATION_TYPE, jsonobject.getString("type"));
            contentValues.put(NOTIFICATION_DATE, jsonobject.getString("notificationDate"));
            contentValues.put(NOTIFICATION_UPDATE_DATE, jsonobject.getString("updateDate"));
            contentValues.put(NOTIFICATION_DELETED, jsonobject.getString("deleted"));
            contentValues.put(NOTIFICATION_MEDIA, jsonobject.getString("media"));
            db.insert("NOTIFICATION", null, contentValues);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    public String getNotificationByContactId(String ContactId) {
        String NotificationId = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM NOTIFICATION WHERE " + NOTIFICATION_CONTACTID + " = ?", new String[]{ContactId + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            NotificationId = cursor.getString(cursor.getColumnIndex(NOTIFICATION_ID));
        }
        return NotificationId;
    }

    public boolean deleteNotificationById(String NotifcationId) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("NOTIFICATION", NOTIFICATION_ID + " = ?", new String[]{NotifcationId});
        return true;
    }

    public ArrayList<NotificationModal> getNotifications() {
        boolean MsgRead;
        ArrayList<NotificationModal> notificationLists = new ArrayList<NotificationModal>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM NOTIFICATION ", null);
        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            String title = cursor.getString(cursor.getColumnIndex(NOTIFICATION_TITLE));
            String ContactId = cursor.getString(cursor.getColumnIndex(NOTIFICATION_CONTACTID));
            String NotificationId = cursor.getString(cursor.getColumnIndex(NOTIFICATION_ID));
            String notificationDate = cursor.getString(cursor.getColumnIndex(NOTIFICATION_DATE));
            String type = cursor.getString(cursor.getColumnIndex(NOTIFICATION_TYPE));
            String read = cursor.getString(cursor.getColumnIndex(NOTIFICATION_READ));
            if (read.equalsIgnoreCase("true")) MsgRead = true;
            else MsgRead = false;
            notificationLists.add(new NotificationModal(title, ContactId, NotificationId, notificationDate, type, MsgRead, 2));
            cursor.moveToNext();
        }
        return notificationLists;
    }

    public int updateReadMessage(String read, String NotifcationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTIFICATION_READ, read);
        return db.update("NOTIFICATION", contentValues, NOTIFICATION_ID + " = ?", new String[]{NotifcationId});
    }

    public int getPhoneTypelen() {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("Select * from NOTIFICATION where type = \"LEAD\" and source = \"VOICE\"", null);
        int favcnt = cursor.getCount();

        return favcnt;
    }

    public int getsmsTypelen() {
        String id = "SMS";
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM NOTIFICATION WHERE type = \"LEAD\" and source = \"SMS\"", null);
        int favcnt = cursor.getCount();

        return favcnt;
    }

    public int getEmailTypelen() {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery("Select * from NOTIFICATION where type = \"LEAD\" and source = \"EMAIL\"", null);
        int favcnt = cursor.getCount();

        return favcnt;
    }

    public String getEmailLastDate() {
        String date = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT date FROM NOTIFICATION WHERE SOURCE = \"EMAIL\" ORDER BY ID DESC LIMIT 1", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            date = res.getString(res.getColumnIndex("date"));

            res.moveToNext();
        }
        if (date == null) {
            return "";
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy @ hh:mm a");
            final Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(date));
            return "Last " + formatter.format(calendar.getTime());
        }
    }

    public String getPhoneLastDate() {
        String date = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT date FROM NOTIFICATION WHERE SOURCE = \"VOICE\" ORDER BY ID DESC LIMIT 1", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            date = res.getString(res.getColumnIndex("date"));

            res.moveToNext();
        }
        if (date == null) {
            return "";
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy @ hh:mm a");
            final Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(date));
            return "Last " + formatter.format(calendar.getTime());
        }
    }

    public String getSmsLastDate() {
        String date = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT date FROM NOTIFICATION WHERE SOURCE = \"SMS\" ORDER BY ID DESC LIMIT 1", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            date = res.getString(res.getColumnIndex("date"));

            res.moveToNext();
        }
        if (date == null) {
            return "";
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy @ hh:mm a");
            final Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(date));
            return "Last " + formatter.format(calendar.getTime());
        }
    }

    public boolean InsertIntoPhoneGreeting(JSONObject PhoneGreetingObject) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        JSONArray phones = new JSONArray();

        try {

            phones = PhoneGreetingObject.getJSONArray("phones");

            JSONObject singlephone = new JSONObject(phones.getJSONObject(0).toString());

            contentValues.put(G_MINDME_NO, singlephone.getString("phoneNumber"));

            JSONObject phoneConcierge = PhoneGreetingObject.getJSONObject("phoneConcierge");

            JSONObject greeting = phoneConcierge.getJSONObject("greeting");

            contentValues.put(G_MEDIA_URL, greeting.getString("voiceRecordingMediaURL"));

            contentValues.put(G_TEXT_TO_VOICE, greeting.getString("textToVoiceMessage"));

            contentValues.put(G_ENABLE, greeting.getString("enabled"));

            JSONObject forward = phoneConcierge.getJSONObject("forward");

            contentValues.put(F_RINGS, forward.getString("rings"));

            contentValues.put(F_ENABLE, forward.getString("enabled"));

            contentValues.put(F_U_NAME, forward.getJSONArray("properties").getJSONObject(0).getString("name"));

            contentValues.put(F_U_PHONE, forward.getJSONArray("properties").getJSONObject(0).getString("value"));

            db.insert("GREETINGANDFORWARDING", null, contentValues);

        } catch (Exception e) {

        }
        return true;
    }

    public boolean InsertIntoPhoneGreeting(String MindMeNum, String FarwordingNum) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(G_MINDME_NO, MindMeNum);
        contentValues.put(G_MEDIA_URL, "");
        contentValues.put(G_TEXT_TO_VOICE, "");
        contentValues.put(G_ENABLE, "false");
        contentValues.put(F_RINGS, "2");
        contentValues.put(F_ENABLE, "false");
        contentValues.put(F_U_NAME, "phoneNumber");
        contentValues.put(F_U_PHONE, FarwordingNum);
        db.insert("GREETINGANDFORWARDING", null, contentValues);
        return true;
    }

    public ArrayList<String> getPhoneGreetings() {

        ArrayList<String> greetingdata = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM GREETINGANDFORWARDING order by id asc limit 1 ", null);

        res.moveToFirst();

        while (res.isAfterLast() == false) {
            greetingdata.add(res.getString(res.getColumnIndex(G_MINDME_NO)));
            greetingdata.add(res.getString(res.getColumnIndex(G_MEDIA_URL)));
            greetingdata.add(res.getString(res.getColumnIndex(G_TEXT_TO_VOICE)));
            greetingdata.add(res.getString(res.getColumnIndex(G_ENABLE)));
            greetingdata.add(res.getString(res.getColumnIndex(F_RINGS)));
            greetingdata.add(res.getString(res.getColumnIndex(F_ENABLE)));
            greetingdata.add(res.getString(res.getColumnIndex(F_U_NAME)));
            greetingdata.add(res.getString(res.getColumnIndex(F_U_PHONE)));
            res.moveToNext();
        }
        return greetingdata;
    }


    //-------------------------------"SOCIAL   Media"--------------------------------//
    public void InsertIntoSocialMedia(JSONObject SocialData) {
        SQLiteDatabase db = this.getWritableDatabase();
        final String[] SocialmediaItems = new String[]{"Linkedln", "Facebook", "Twitter", "Google+", "Youtube", "Instagram", "Pinterest"};
        final String[] SocialmediaUrls = new String[]
                {"www.linkedln.com", "www.facebook.com", "www.twitter.com", "www.googleplus.com", "www.youtube.com", "www.instagram.com", "www.pinterest.com"};
        try {
            JSONArray socials = SocialData.getJSONArray("socials");
            if (socials.length() > 0) {
                for (int i = 0; i < SocialmediaItems.length; i++) {
                    ContentValues contentValues = new ContentValues();
                    ArrayList<String> arraylist = new ArrayList<>();
                    for (int j = 0; j < socials.length(); j++) {
                        JSONObject singleObject = socials.getJSONObject(j);
                        if (SocialmediaItems[i].toLowerCase().equals(singleObject.getString("name").toLowerCase())) {
                            arraylist.add(singleObject.getString("id"));
                        }
                    }
                    contentValues.put(SOCIAL_UPDATEED_ON, getDateInMilliseconds());
                    contentValues.put(SOCIAL_NAME, SocialmediaItems[i]);
                    contentValues.put(SOCIAL_MEDIA_URL, SocialmediaUrls[i]);
                    if (arraylist.size() > 1) {
                        contentValues.put(SOCIAL_QNIQUE_ID, arraylist.get(arraylist.size() - 1));
                    }
                    db.insert("SOCIALMEDIA", null, contentValues);
                }
            }
        } catch (JSONException e) {
            Log.i(TAG, "Unable To Get Social Media ");
            e.printStackTrace();
        }
    }

    public void InsertIntoSocialMedia() {
        SQLiteDatabase db = this.getWritableDatabase();
        final String[] SocialmediaItems = new String[]{"Linkedln", "Facebook", "Twitter", "Google+", "Youtube", "Instagram", "Pinterest"};
        final String[] SocialmediaUrls = new String[]
                {"www.linkedln.com/", "www.facebook.com/", "www.twitter.com/", "www.googleplus.com/", "www.youtube.com/", "www.instagram.com/", "www.pinterest.com/"};
        for (int i = 0; i < 7; i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(SOCIAL_UPDATEED_ON, getDateInMilliseconds());
            contentValues.put(SOCIAL_NAME, SocialmediaItems[i]);
            contentValues.put(SOCIAL_MEDIA_URL, SocialmediaUrls[i]);
            contentValues.put(SOCIAL_QNIQUE_ID, "");
            db.insert("SOCIALMEDIA", null, contentValues);
        }
    }

    public boolean UpdateSocialMedia(String SocialMediaName, String MediaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SOCIAL_UPDATEED_ON, getDateInMilliseconds());
        values.put(SOCIAL_QNIQUE_ID, MediaId);
        db.update("SOCIALMEDIA", values, SOCIAL_NAME + " = ?", new String[]{SocialMediaName});
        return true;
    }

    public ArrayList<String> getSocialMediaArray() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> socialIds = new ArrayList<String>();
        Cursor res = db.rawQuery("select * from SOCIALMEDIA", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            socialIds.add(res.getString(res.getColumnIndex(SOCIAL_QNIQUE_ID)));
            res.moveToNext();
        }
        return socialIds;
    }

    public int getSocialCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from SOCIALMEDIA", null);
        return res.getCount();
    }

    public String getSocialId(String SocialMedia) {

        String SocialId = null;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM SOCIALMEDIA WHERE " + SOCIAL_NAME + " = ?", new String[]{SocialMedia + ""});

        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {

            SocialId = cursor.getString(cursor.getColumnIndex(SOCIAL_QNIQUE_ID));
            cursor.moveToNext();
        }
        return SocialId;
    }

    public String getDateInMilliseconds() {

        Date date = new Date();

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        return String.valueOf(date.getTime());
    }

    private boolean nullCheck(String str) {

        boolean isEmpty = str == null || str.trim().length() == 0 || str.equalsIgnoreCase("null");

        return isEmpty;
    }
}





