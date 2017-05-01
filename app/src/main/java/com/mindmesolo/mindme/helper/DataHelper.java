package com.mindmesolo.mindme.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.mindmesolo.mindme.CreateMobilePages.models.BusinessHours;
import com.mindmesolo.mindme.R;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by eNest on 6/23/2016.
 */
public class DataHelper extends AppCompatActivity {

    private static final String TAG = "DataHelper";
    private static DataHelper dataHelper = new DataHelper();
    Context context;

    ProgressDialog pDialog;

    //Create a singleton
    public static synchronized DataHelper getInstance() {
        return dataHelper;
    }

    public static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }

    //convert bitmap to base64 format.
    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    //convert base64 to bitmap format.
    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {
            int numberOfItems = listAdapter.getCount();
            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 500 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int) px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItemsHeight += item.getMeasuredHeight();
            }
            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            // Get padding
            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();
            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalPadding;
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;

        } else {
            return false;
        }

    }

    public boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public String nullValidation(String data) {
        if (data == null || data.equalsIgnoreCase("null")) {
            return "";
        } else {
            return data;
        }
    }

    public String GroupsFilter(String data) {
        switch (data.toUpperCase()) {
            case "LEADS":
                data = "LEAD";
                break;
            case "TRIALS":
                data = "TRIAL";
                break;
            case "CUSTOMERS":
                data = "CUSTOMER";
                break;
        }

        return data;
    }

    public int CalculatePercentage(int lead, int total) {
        double test = (double) lead / total * 100;
        int a = (int) test;
        return a;
    }

    public String getStringLength(String data) {
        String length = null;
        try {
            if (data != null) {
                JSONArray myjsonstring = new JSONArray(data);
                length = String.valueOf(myjsonstring.length());
            }
        } catch (Exception e) {
            Log.i("DatHelper", "data is null in get string");
            return String.valueOf(0);
        }

        return length;
    }

    //------------------help in api ---------------//
    public String getApiAccess(Context context) {
        SharedPreferences pref1 = context.getSharedPreferences("UserLogin", Activity.MODE_PRIVATE);
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

    //---------------method use in create campaign-------------------//

    //--------------volley error handle-----//
    public String trimMessage(String json, String key) {
        String trimmedString = null;
        try {
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return trimmedString;
    }

    public void displayMessage(String toastString) {
        Log.e("VOLLEY", toastString);
    }

    //---------------method use in create campaign    ---------------//
    public ArrayList<String> stringToArrayList(String data) {
        ArrayList objectList = new ArrayList();
        if (data != null) {
            try {
                JSONArray rawdata = new JSONArray(data);
                if (rawdata.length() > 0) {
                    for (int i = 0; i < rawdata.length(); i++) {
                        String singleObject = rawdata.getString(i);
                        objectList.add(singleObject);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return objectList;
            }
        } else {
            Log.e(TAG, "stringToArrayList----------> null data Exception");
        }
        return objectList;
    }

    public JSONArray arrayListToString(ArrayList<String> data) {
        JSONArray contacts = new JSONArray();
        for (String contact : data) {
            contacts.put(contact);
        }
        return contacts;
    }

    ;


    public String getValidData(String recipientsCount) {

        if (StringUtils.isEmpty(recipientsCount) || StringUtils.isBlank(recipientsCount) || recipientsCount.equalsIgnoreCase("null")) {
            recipientsCount = "0";
        }
        return recipientsCount;
    }

    //put icon on image.
    public Bitmap putOverlay(Bitmap source, Context context) {
        Canvas canvas = new Canvas(source);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.youtube_play_icon);
//        Bitmap b = Bitmap.createScaledBitmap(icon, 50, 50, false);
        float left = (source.getWidth() / 2) - (icon.getWidth() / 2);
        float top = (source.getHeight() / 2) - (icon.getHeight() / 2);
        canvas.drawBitmap(icon, left, top, null);
        return source;
    }

    public String loadVideoFile(Uri uri, Context context) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Video.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();
        return path;
    }

    public String loadAudioFile(Uri uri, Context context) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Audio.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
        cursor.close();
        return path;
    }

    // progress dialog
    public void startDialog(Context context, String Message) {
        pDialog = new ProgressDialog(context);
        if (pDialog != null) {
            pDialog.setCancelable(true);
            pDialog.setMessage(Message);
            pDialog.show();
        }
    }

    // progress dialog
    public void stopDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    // businessHours helper
    public String getDayOfWeek(int id) {
        String[] names = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        return names[id];
    }

    public String getTime(BusinessHours mediaItem) {
        String data = null;
        if (mediaItem.getClosed()) {
            data = "Closed";
        } else if (mediaItem.getByAppointmentOnly()) {
            data = "By Appointment";
        } else {
            data = String.format("%02d",
                    getOpenHours(mediaItem.getOpenOnHour()))
                    + ":" + String.format("%02d", mediaItem.getOpenOnMinute())
                    + " AM" + "-" + String.format("%02d", getCloseHours(mediaItem.getCloseOnHour()))
                    + ":" + String.format("%02d", mediaItem.getCloseOnMinute()) + " PM";
        }

        return data;
    }

    public int getOpenHours(int data) {
        if (data == 0) {
            return 9;
        } else {
            return data;
        }
    }

    public int getCloseHours(int data) {
        if (data == 0) {
            return 5;
        } else {
            return data;
        }
    }

    //Text copy in clipboard
    public void setClipboard(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    // Google map helper
    public String getAddress(Context context, Location latLng) {
        StringBuilder result = new StringBuilder();
        if (latLng != null) {
            try {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(latLng.getLatitude(), latLng.getLongitude(), 1);
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);
                    int addresslines = address.getMaxAddressLineIndex();
                    result.append(address.getAddressLine(1)).append("\n");
//                    if (addresslines > 2) {
//                       // result.append(address.getAddressLine(0)).append("\n");
//                        result.append(address.getAddressLine(1)).append("\n");
//                    }else {
//                        result.append(address.getAddressLine(0)).append("\n");
//                    }
                    result.append(address.getLocality()).append("\n");
                    result.append(address.getCountryName());
                }
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
        }
        return result.toString();
    }
}