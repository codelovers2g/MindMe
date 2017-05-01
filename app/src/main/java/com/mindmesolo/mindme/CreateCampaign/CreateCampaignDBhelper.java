package com.mindmesolo.mindme.CreateCampaign;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by eNest on 6/28/2016.
 */
public class CreateCampaignDBhelper extends SQLiteOpenHelper {
    // Database Name
    public static final String DATABASE_NAME = "CreateCampaign.db";
    public static final String CAMPAIGN_ID = "id";
    public static final String CAMPAIGN_CONTACTS = "C_contacts";
    // Database Version
    private static final int DATABASE_VERSION = 1;

    public CreateCampaignDBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table CAMPAIGN" + "(id integer primary key,C_contacts feedbackEditText unique)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CAMPAIGN");
        onCreate(db);
    }

    public boolean InsertInCampaign(String contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CAMPAIGN_CONTACTS, contact);
        db.insert("CAMPAIGN", null, contentValues);
        return true;
    }

    public boolean deleteCampaign() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("Delete from CAMPAIGN");
        return true;
    }

    public ArrayList getAllContacts() {
        ArrayList contacts = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from CAMPAIGN", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            contacts.add(res.getString(res.getColumnIndex(CAMPAIGN_CONTACTS)));
            res.moveToNext();
        }
        return contacts;
    }

}
