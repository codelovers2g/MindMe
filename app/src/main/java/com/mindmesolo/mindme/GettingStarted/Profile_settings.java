package com.mindmesolo.mindme.GettingStarted;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindmesolo.mindme.AudioRecorder.MediaRecordingNew;
import com.mindmesolo.mindme.MainActivity;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.Services.VolleyApi;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.RuntimePermissionsActivity;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.ToastShow;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;
import id.zelory.compressor.FileUtil;

/**
 * updated on eNest on 8/29/2016.
 */

public class Profile_settings extends RuntimePermissionsActivity implements View.OnClickListener {

    private static final int REQUEST_PERMISSIONS = 20;
    ImageView imageView;
    EditText name, firstName, lastName, mobileno, businessMobile, homeMobile, street, town_city, pin_code, state, country;
    EditText useremail;
    RelativeLayout chooselogo;
    SqliteDataBaseHelper dBhelper = new SqliteDataBaseHelper(this);
    DataHelper dataHelper = new DataHelper();
    Button buttonRemove;
    EditText timezone;
    Dialog dialog;
    boolean CameraPermission = false;
    boolean userChangeProfile = false;
    boolean userChangeLogo = false;
    View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            addTextWatcher();
        }
    };
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private String TAG = "Profile_settings";
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


        }

        @Override
        public void afterTextChanged(Editable editable) {
            userChangeProfile = true;
            Log.i(TAG, "TEXT CHANGED");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_in_tools_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = (EditText) findViewById(R.id.name);

        firstName = (EditText) findViewById(R.id.firstName);

        lastName = (EditText) findViewById(R.id.lastName);

        mobileno = (EditText) findViewById(R.id.mobileno);

        businessMobile = (EditText) findViewById(R.id.businessmobileno);

        homeMobile = (EditText) findViewById(R.id.homemobileno);

        useremail = (EditText) findViewById(R.id.useremail);

        timezone = (EditText) findViewById(R.id.timezone);
        timezone.setOnClickListener((View v) -> {
            setupSpinerItems();
        });

        street = (EditText) findViewById(R.id.street);

        town_city = (EditText) findViewById(R.id.town_city);

        pin_code = (EditText) findViewById(R.id.pin_code);

        state = (EditText) findViewById(R.id.state);


        country = (EditText) findViewById(R.id.country);


        imageView = (ImageView) findViewById(R.id.imageView);

        buttonRemove = (Button) findViewById(R.id.buttonRemove);
        buttonRemove.setOnClickListener(this);

        chooselogo = (RelativeLayout) findViewById(R.id.buttonChoose);
        chooselogo.setOnClickListener(this);

        country.setOnFocusChangeListener(onFocusChangeListener);
        state.setOnFocusChangeListener(onFocusChangeListener);
        pin_code.setOnFocusChangeListener(onFocusChangeListener);
        town_city.setOnFocusChangeListener(onFocusChangeListener);
        street.setOnFocusChangeListener(onFocusChangeListener);
        useremail.setOnFocusChangeListener(onFocusChangeListener);
        homeMobile.setOnFocusChangeListener(onFocusChangeListener);
        businessMobile.setOnFocusChangeListener(onFocusChangeListener);
        mobileno.setOnFocusChangeListener(onFocusChangeListener);
        lastName.setOnFocusChangeListener(onFocusChangeListener);
        firstName.setOnFocusChangeListener(onFocusChangeListener);
        name.setOnFocusChangeListener(onFocusChangeListener);


        getUserProfileData();
        setupUserProfileData();

        checkPermissions();
    }

    private void checkPermissions() {
        Profile_settings.super.requestAppPermissions(new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                R.string.runtime_permissions_txt
                , REQUEST_PERMISSIONS);
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        if (requestCode == REQUEST_PERMISSIONS) {
            CameraPermission = true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.greeting, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonChoose:
                pickImageFromCameraAndGallery();
                break;
            case R.id.buttonRemove:
                removeLogoConfirm();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            updateOrganization();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                userChangeLogo = true;
                File actualImage = FileUtil.from(this, data.getData());
                bitmap = Compressor.getDefault(this).compressToBitmap(actualImage);
                imageView.setImageBitmap(bitmap);
                setRemoveButtonVisibility("true");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getUserProfileData() {
        String REGISTER_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + dBhelper.getOrganizationId() + "/users";
        VolleyApi.getInstance().getJsonArray(getBaseContext(), REGISTER_URL, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                if (result.success && result.dataIsArray()) {
                    JSONArray response = result.getDataAsArray();
                    dBhelper.updateAppUserData(response);
                    setupUserProfileData();
                }
            }
        });
    }


    // set user Profile data when user activity start
    private void setupUserProfileData() {
        ArrayList<String> userProfileSetup = dBhelper.getUserProfileData();
        try {
            if (userProfileSetup.size() > 1) {
                name.setText((userProfileSetup.get(0)));
                firstName.setText(userProfileSetup.get(1));
                lastName.setText(userProfileSetup.get(2));
                mobileno.setText(userProfileSetup.get(3));
                businessMobile.setText(userProfileSetup.get(4));
                homeMobile.setText(userProfileSetup.get(5));
                useremail.setText(userProfileSetup.get(6));
                timezone.setText(setTimeZoneList(userProfileSetup.get(7)));
                street.setText(userProfileSetup.get(8));
                town_city.setText(userProfileSetup.get(9));
                pin_code.setText(userProfileSetup.get(10));
                state.setText(userProfileSetup.get(11));
                country.setText(userProfileSetup.get(12));
                bitmap = dataHelper.decodeBase64(userProfileSetup.get(13));
                imageView.setImageBitmap(bitmap);
                setRemoveButtonVisibility(userProfileSetup.get(13));
            }
        } catch (Exception e) {
            Log.e(TAG, "Data is not Sync Yet Please retry after some time..");
        }
    }

    private void setupSpinerItems() {
        String[] simplenames = {"Alaskan Standard Time", "Central Standard Time", "Eastern Standard Time", "Hawaiian Standard Time", "Mountain Standard Time", "Pacific Standard Time"};
        Dialog dialog = new Dialog(Profile_settings.this);
        dialog.setContentView(R.layout.popup_list_menu);
        ListView lv = (ListView) dialog.findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, simplenames);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                timezone.setText(simplenames[position]);
                dialog.dismiss();
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }


    private String setTimeZoneList(String timeZone) {

        String data = "Alaskan Standard Time";

        final String[] celebrities = {"ALASKANSTANDARDTIME", "CENTRALSTANDARDTIME", "EASTERNSTANDARDTIME", "HAWAIIANSTANDARDTIME", "MOUNTAINSTANDARDTIME", "PACIFICSTANDARDTIME"};

        String[] simpleNames = {"Alaskan Standard Time", "Central Standard Time", "Eastern Standard Time", "Hawaiian Standard Time", "Mountain Standard Time", "Pacific Standard Time"};

        if (StringUtils.isBlank(timeZone) || StringUtils.isEmpty(timeZone)) {
            return "";
        } else {
            for (int i = 0; i < celebrities.length; i++) {
                if (celebrities[i].equalsIgnoreCase(timeZone)) {
                    data = simpleNames[i];
                }
            }
        }
        return data;
    }

    private String getTimeZoneList(String timeZone) {

        String data = "";

        final String[] celebrities = {"ALASKANSTANDARDTIME", "CENTRALSTANDARDTIME", "EASTERNSTANDARDTIME", "HAWAIIANSTANDARDTIME", "MOUNTAINSTANDARDTIME", "PACIFICSTANDARDTIME"};

        String[] simplenames = {"Alaskan Standard Time", "Central Standard Time", "Eastern Standard Time", "Hawaiian Standard Time", "Mountain Standard Time", "Pacific Standard Time"};

        if (StringUtils.isBlank(timeZone) || StringUtils.isEmpty(timeZone)) {
            return "";
        } else {
            for (int i = 0; i < simplenames.length; i++) {
                if (simplenames[i].equalsIgnoreCase(timeZone)) {
                    data = celebrities[i];
                }
            }
        }
        return data;
    }

    private void setRemoveButtonVisibility(String s) {
        if (s == null || s.length() < 5 || s.isEmpty()) {
            buttonRemove.setVisibility(View.GONE);
        } else {
            buttonRemove.setVisibility(View.VISIBLE);
        }
    }

    private String getOrganizationBodyData() {

        JSONObject finalJsonObject = new JSONObject();

        JSONArray addressarray = new JSONArray();

        JSONObject addressjsonObj = new JSONObject();
        try {
            addressjsonObj.put("addressLine1", street.getText().toString());
            addressjsonObj.put("city", town_city.getText().toString());
            addressjsonObj.put("postalCode", pin_code.getText().toString());
            addressjsonObj.put("region", state.getText().toString());
            addressjsonObj.put("country", country.getText().toString());
            addressarray.put(addressjsonObj);

            finalJsonObject.put("name", name.getText().toString());
            finalJsonObject.put("legalName", name.getText().toString());
            finalJsonObject.put("baseTimezone", getTimeZoneList(timezone.getText().toString()));
            finalJsonObject.put("addresses", addressarray);
            if (bitmap != null) {
                finalJsonObject.put("logo", dataHelper.encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100));
            } else {
                finalJsonObject.put("logo", "");
            }
            if (userChangeProfile == true || userChangeLogo == true) {
                finalJsonObject.put("properties",
                        new JSONArray().put(
                                new JSONObject().put("name", "Greeting Started")
                                        .put("value", getPropertiesObject())));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return finalJsonObject.toString();
    }

    private String getPropertiesObject() {

        StringBuilder stringBuilder;

        SharedPreferences DASHBOARD = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        String GettingStartData = DASHBOARD.getString(MainActivity.PREFS_KEY_GETTING_START, null);

        if (GettingStartData != null) {
            stringBuilder = new StringBuilder(GettingStartData);
            if (!GettingStartData.contains("Profile")) {
                stringBuilder.append("," + "Profile");
            }
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Profile");
        }
        return String.valueOf(stringBuilder);
    }

    private void updateProfile() {
        ArrayList<String> userProfileData = new ArrayList<>();
        userProfileData.add(String.valueOf(System.currentTimeMillis()));
        userProfileData.add(name.getText().toString());
        userProfileData.add(firstName.getText().toString());
        userProfileData.add(lastName.getText().toString());
        userProfileData.add(mobileno.getText().toString());
        userProfileData.add(businessMobile.getText().toString());
        userProfileData.add(homeMobile.getText().toString());
        userProfileData.add(getTimeZoneList(timezone.getText().toString()));
        userProfileData.add(street.getText().toString());
        userProfileData.add(town_city.getText().toString());
        userProfileData.add(pin_code.getText().toString());
        userProfileData.add(state.getText().toString());
        userProfileData.add(country.getText().toString());
        dBhelper.updateUserProfile(userProfileData);
    }

    public void updateOrganization() {

        DataHelper.getInstance().startDialog(Profile_settings.this, "Updating Profile ...");

        String REGISTER_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + dBhelper.getOrganizationId() + "";

        final String jsonbodydata = getOrganizationBodyData();

        VolleyApi.getInstance().putJsonObject(getBaseContext(), REGISTER_URL, jsonbodydata, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                if (result.success && result.dataIsObject()) {
                    JSONObject response = result.getDataAsObject();
                    updateOrganizationUser();
                    updateNameEmailPhone(response);
                    try {
                        String GettingStartData = response.getJSONArray("properties").getJSONObject(0).getString("value");
                        SharedPreferences DASHBOARD = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = DASHBOARD.edit();
                        editor.putString(MainActivity.PREFS_KEY_GETTING_START, GettingStartData);
                        editor.commit();
                    } catch (JSONException e) {
                        Log.e(TAG, e.toString());
                    }
                    try {
                        dBhelper.updateUserLogo(response.getString("logo"));
                    } catch (JSONException e) {
                        Log.e(TAG, e.toString());
                    }
                } else {
                    DataHelper.getInstance().stopDialog();
                    ToastShow.setText(getBaseContext(), "Please Check your internet Connection", Toast.LENGTH_LONG);
                }
            }
        });
    }

    private void updateNameEmailPhone(JSONObject jsonObject) {
        try {

            String name = jsonObject.getString("name");

            String mindMeEmail = jsonObject.getString("email");

            String phone = jsonObject.getJSONArray("phones").getJSONObject(0).getString("phoneNumber");

            SharedPreferences pref = getApplicationContext().getSharedPreferences("UserData", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            editor.putString("Name", name);
            editor.putString("mindMeEmail", mindMeEmail);
            editor.putString("phone", phone);
            editor.commit();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateOrganizationUser() {

        String REGISTER_URL = "/mindmemobile-web/api/v1/mindmemobile/users/" + dBhelper.getUserProfileUserID() + "";

        final String jsonbodydata = getUserProfileBodyData();

        VolleyApi.getInstance().putJsonObject(getBaseContext(), REGISTER_URL, jsonbodydata, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                if (result.success && result.dataIsObject()) {
                    JSONObject response = result.getDataAsObject();
                    updateProfile();
                    updateSharedData();
                    DataHelper.getInstance().stopDialog();
                    ToastShow.setText(Profile_settings.this, "Update Successful.", Snackbar.LENGTH_SHORT);
                    finish();
                } else {
                    DataHelper.getInstance().stopDialog();
                    ToastShow.setText(getBaseContext(), "Please Check your internet Connection", Toast.LENGTH_LONG);
                }
            }
        });
    }

    //confirm if user wants remove logo
    private void removeLogoConfirm() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Profile_settings.this);
        alertDialog.setMessage("Are You sure want to remove logo ?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                bitmap = null;
                imageView.setImageResource(android.R.color.transparent);
                setRemoveButtonVisibility("");
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    //Popup dialog for selecting image from gallery and camera
    private void pickImageFromCameraAndGallery() {
        dialog = new Dialog(this);
        dialog.getWindow().setWindowAnimations(R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_profile);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM | Gravity.CENTER;
        wmlp.x = 0;   //x position
        wmlp.y = 50;   //y position

        TextView camera = (TextView) dialog.findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CameraPermission) {
                    Intent intent = new Intent(view.getContext(), MediaRecordingNew.class);
                    intent.putExtra("CAMERA_REQUEST", "IMAGE");
                    startActivityForResult(intent, 3);
                } else {
                    checkPermissions();
                }
                dialog.dismiss();
            }
        });
        TextView Image_Gallery = (TextView) dialog.findViewById(R.id.Image_Gallery);
        Image_Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                dialog.dismiss();
            }
        });

        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private String getUserProfileBodyData() {

        ArrayList<String> userProfileSetup = dBhelper.getUserProfileData();

        JSONObject userObject = new JSONObject();

        try {

            userObject.put("updatedOn", String.valueOf(System.currentTimeMillis()));

            userObject.put("id", dBhelper.getUserProfileUserID());

            userObject.put("firstName", firstName.getText().toString());

            userObject.put("lastName", lastName.getText().toString());

            userObject.put("emailAddress", userProfileSetup.get(6));

            userObject.put("username", userProfileSetup.get(6));

            userObject.put("apnsToken", userProfileSetup.get(14));

            SharedPreferences settings = getBaseContext().getSharedPreferences("DEVICE_TOKEN_PREFS", Context.MODE_PRIVATE);

            final String DeviceToken = settings.getString("DEVICE_TOKEN_STRING", null);

            userObject.put("deviceToken", DeviceToken);

            JSONArray PhoneArray = new JSONArray();

            JSONArray orgnizationArray = new JSONArray();

            orgnizationArray.put(dBhelper.getOrganizationId());

            if (StringUtils.isNotEmpty(mobileno.getText().toString())) {
                PhoneArray.put(new JSONObject().put("phoneType", "MOBILE").put("phoneNumber", mobileno.getText().toString()));
            }

            if (userProfileSetup.get(4) != null) {
                PhoneArray.put(new JSONObject().put("phoneType", "DEFAULT").put("phoneNumber", userProfileSetup.get(4)));
            }

            if (StringUtils.isNotEmpty(homeMobile.getText().toString())) {

                PhoneArray.put(new JSONObject().put("phoneType", "HOME").put("phoneNumber", homeMobile.getText().toString()));

            }

            userObject.put("phones", PhoneArray);

            userObject.put("orgIds", orgnizationArray);

            userObject.put("passwordHash", getUserPassHash());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userObject.toString();
    }


    //get user password in base 64 format for save it
    private String getUserPassHash() {
        SharedPreferences pref1 = getBaseContext().getSharedPreferences("UserLogin", Context.MODE_PRIVATE);
        byte[] data = null;
        try {
            data = pref1.getString("Password", null).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        final String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        final String finalToken = base64.replace("\n", "");
        return finalToken;
    }

    //attach text watcher to the all text vies for track changes in text filds
    private void addTextWatcher() {
        country.addTextChangedListener(textWatcher);
        state.addTextChangedListener(textWatcher);
        pin_code.addTextChangedListener(textWatcher);
        town_city.addTextChangedListener(textWatcher);
        street.addTextChangedListener(textWatcher);
        useremail.addTextChangedListener(textWatcher);
        homeMobile.addTextChangedListener(textWatcher);
        businessMobile.addTextChangedListener(textWatcher);
        mobileno.addTextChangedListener(textWatcher);
        lastName.addTextChangedListener(textWatcher);
        firstName.addTextChangedListener(textWatcher);
        name.addTextChangedListener(textWatcher);
    }

    //update shared account name if changed
    private void updateSharedData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Name", name.getText().toString());
        editor.apply();
    }

}
