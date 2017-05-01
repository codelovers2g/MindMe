package com.mindmesolo.mindme.ContactAndLists;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mindmesolo.mindme.AudioRecorder.MediaRecordingNew;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.Services.VolleyApi;
import com.mindmesolo.mindme.helper.CircleImageView;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import id.zelory.compressor.Compressor;
import id.zelory.compressor.FileUtil;

/**
 * Created by User1 on 6/28/2016.
 */

public class EditContact extends RuntimePermissionsActivity {

    final static String TAG = "EditContact";

    private static final int REQUEST_PERMISSIONS = 20;

    static String StoredContactid;

    EditText editTextname, editTextmname, editTextlname, editTextadress1, editTextaddress2, editTextaddress3, editTextaddress4;
    EditText editTextmobile, editTextemail, editTextcompname, editTextcompurl, editTextfburl, editTextliurl, editTexttwurl, editTextpin;
    EditText editBirthday;

    Button buttonAdd, buttoncancel, buttonChoose;

    CircleImageView imageView;

    SqliteDataBaseHelper sqliteDataBaseHelper;

    Spinner spinner;

    DataHelper dataHelper = new DataHelper();

    boolean CameraPermission = false;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
    private ProgressDialog pDialog;
    private Bitmap bitmap;
    private TextView buttonRemoveBirthday;
    private int PICK_IMAGE_REQUEST = 1;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.addnewcontact);

        TextView textView = (TextView) findViewById(R.id.toolbartxt);
        textView.setText("Edit Contact");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sqliteDataBaseHelper = new SqliteDataBaseHelper(this);

        Bundle extras = getIntent().getExtras();

        StoredContactid = extras.getString("contactids");

        setupUI();

        checkPermissions();
    }

    private void checkPermissions() {
        EditContact.super.requestAppPermissions(new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
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

    private void setupUI() {
        buttonRemoveBirthday = (TextView) findViewById(R.id.buttonRemoveBirthday);
        buttonRemoveBirthday.setVisibility(View.GONE);
        buttonAdd = (Button) findViewById(R.id.Add);
        buttoncancel = (Button) findViewById(R.id.cancel);
        buttonChoose = (Button) findViewById(R.id.chooseImage);
        imageView = (CircleImageView) findViewById(R.id.imageView);
        spinner = (Spinner) findViewById(R.id.spinnerPhoneType);
        editTextname = (EditText) findViewById(R.id.name);
        editTextmname = (EditText) findViewById(R.id.Mname);
        editTextlname = (EditText) findViewById(R.id.Lname);
        editTextadress1 = (EditText) findViewById(R.id.address1);
        editTextaddress2 = (EditText) findViewById(R.id.address2);
        editTextaddress3 = (EditText) findViewById(R.id.address3);
        editTextaddress4 = (EditText) findViewById(R.id.address4);
        editTextmobile = (EditText) findViewById(R.id.mobile);
        editTextemail = (EditText) findViewById(R.id.Email);
        editTextcompname = (EditText) findViewById(R.id.companyname);
        editTextcompurl = (EditText) findViewById(R.id.companyUrl);
        editTextfburl = (EditText) findViewById(R.id.fburl);
        editTextliurl = (EditText) findViewById(R.id.twurl);
        editTexttwurl = (EditText) findViewById(R.id.liurl);
        editTextpin = (EditText) findViewById(R.id.pincode);

        buttonRemoveBirthday.setOnClickListener((View v) -> {
            editBirthday.setText("");
            buttonRemoveBirthday.setVisibility(View.GONE);
        });

        editBirthday = (EditText) findViewById(R.id.Birthday);
        editBirthday.setOnClickListener((View v) -> {
            int mYear, mMonth, mDay;

            final Calendar myCalendar = Calendar.getInstance();

            mYear = myCalendar.get(Calendar.YEAR);
            mMonth = myCalendar.get(Calendar.MONTH);
            mDay = myCalendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(EditContact.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            editBirthday.setText(simpleDateFormat.format(myCalendar.getTime()));
                            buttonRemoveBirthday.setVisibility(View.VISIBLE);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        ArrayList<String> contactArray = sqliteDataBaseHelper.getContactInformation(StoredContactid);

        editTextname.setText(nullValidation(contactArray.get(1)));
        editTextmname.setText(nullValidation(contactArray.get(2)));
        editTextlname.setText(nullValidation(contactArray.get(3)));
        editTextadress1.setText(nullValidation(contactArray.get(8)));
        editTextaddress2.setText(nullValidation(contactArray.get(9)));
        editTextaddress3.setText(nullValidation(contactArray.get(11)));
        editTextaddress4.setText(nullValidation(contactArray.get(12)));
        editTextemail.setText(nullValidation(contactArray.get(7)));
        editTextpin.setText(nullValidation(contactArray.get(10)));
        editTextmobile.setText(nullValidation(contactArray.get(6)));

        bitmap = dataHelper.decodeBase64(contactArray.get(4));

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }

        editTextcompname.setText(nullValidation(contactArray.get(13)));
        editTextcompurl.setText(nullValidation(contactArray.get(14)));
        editTextfburl.setText(nullValidation(contactArray.get(15)));
        editTextliurl.setText(nullValidation(contactArray.get(16)));
        editTexttwurl.setText(nullValidation(contactArray.get(17)));
        String Birthday = nullValidation(contactArray.get(21));
        if (StringUtils.isNotBlank(Birthday) && StringUtils.isNotEmpty(Birthday) && !Birthday.equalsIgnoreCase("null")) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(Birthday));
            editBirthday.setText(simpleDateFormat.format(calendar.getTime()));
            buttonRemoveBirthday.setVisibility(View.VISIBLE);
        }

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((editTextmobile.getText().toString().length() == 0
                        || editTextmobile.getText().toString().isEmpty())
                        && (editTextemail.getText().toString().length() == 0
                        || editTextemail.getText().toString().isEmpty())) {
                    ToastShow.setText(EditContact.this, "Contact Must Have Phone or Email", Toast.LENGTH_SHORT);
                } else {
                    UpdateContact();
                }
            }
        });

        buttoncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showFileChooser();
                showDialog(v);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actiontext, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.actionDelete) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditContact.this);
            alertDialog.setMessage("Are You sure want to delete this contact ?");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    deleteContact();
                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                File actualImage = FileUtil.from(this, data.getData());
                bitmap = Compressor.getDefault(this).compressToBitmap(actualImage);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }//Activity for getting image from device

    private void showDialog(final View v) {
        dialog = new Dialog(v.getContext());
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
                    startActivityForResult(intent, 1);
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
                ((Activity) v.getContext()).startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
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

    private void UpdateContact() {

        showProgressDialog("Updating Contact.");

        String REGISTER_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + sqliteDataBaseHelper.getOrganizationId() + "/contacts/" + StoredContactid + "";

        VolleyApi.getInstance().putJsonObject(EditContact.this, REGISTER_URL, EditContactObject().toString(), new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                if (result.success && result.dataIsObject()) {
                    JSONObject response = result.getDataAsObject();
                    sqliteDataBaseHelper.deleteContact(StoredContactid);

                    try {
                        String Birthday = response.getString("dateOfBirth");

                        Log.i("Birthday", "Birthday" + Birthday);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    sqliteDataBaseHelper.insertNewContact(response);
                    finish();
                } else {
                    ToastShow.setText(getBaseContext(), "oops something went wrong. try again", Toast.LENGTH_LONG);
                }
            }
        });
    }

    private void deleteContact() {

        showProgressDialog("Deleting Contact...");

        String REGISTER_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + sqliteDataBaseHelper.getOrganizationId() + "/contacts/" + StoredContactid + "";

        VolleyApi.getInstance().deleteJsonObject(EditContact.this, REGISTER_URL, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                if (result.success && result.dataIsObject()) {
                    Log.i(TAG, "Deleting Contact Success...");
                    sqliteDataBaseHelper.deleteContact(StoredContactid);

                    //sqliteDataBaseHelper.removeContactFromList(StoredContactid);

                    Summary.KillMe.finish();

                    final String NotificationId = sqliteDataBaseHelper.getNotificationByContactId(StoredContactid);
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "Updating Organization data ...........");
                            getOrganizationData();
                        }
                    });
                    if (NotificationId != null) {
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(TAG, "Deleting Notification data ...........");
                                deleteContactNotifications(StoredContactid, NotificationId);
                            }
                        });
                    }
                } else {
                    ToastShow.setText(getBaseContext(), "oops something went wrong. try again", Toast.LENGTH_LONG);
                }
            }
        });
    }

    private void deleteContactNotifications(String ContactId, final String NotificationId) {
        String REGISTER_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + sqliteDataBaseHelper.getOrganizationId() + "/contacts/" + ContactId + "/notification/" + NotificationId + "";
        VolleyApi.getInstance().deleteJsonObject(EditContact.this, REGISTER_URL, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                if (result.success && result.dataIsObject()) {
                    Log.i(TAG, "Contact Notification Deleted  Contact Success");
                    sqliteDataBaseHelper.deleteNotificationById(NotificationId);
                } else {
                    ToastShow.setText(getBaseContext(), "oops something went wrong. try again", Toast.LENGTH_LONG);
                }
            }
        });
    }

    public String EditContactObject() {

        JSONArray jsonArrayAddress = new JSONArray();
        try {
            JSONObject list1 = new JSONObject();
            list1.put("addressLine1", editTextadress1.getText().toString());
            list1.put("addressLine2", editTextaddress2.getText().toString());
            list1.put("addressLine3", editTextaddress3.getText().toString());
            list1.put("addressLine4", editTextaddress4.getText().toString());
            list1.put("city", editTextaddress2.getText().toString());
            list1.put("country", editTextaddress4.getText().toString());
            list1.put("postalCode", editTextpin.getText().toString());
            jsonArrayAddress.put(list1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jsonArrayphone = new JSONArray();
        try {
            JSONObject list2 = new JSONObject();
            list2.put("phoneNumber", editTextmobile.getText().toString());
            list2.put("phoneType", spinner.getSelectedItem().toString());
            jsonArrayphone.put(list2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("createdOn", sqliteDataBaseHelper.getContactCreatedon(StoredContactid));
            jsonObject.put("updatedOn", System.currentTimeMillis());
            if (bitmap != null) {
                jsonObject.put("photo", dataHelper.encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 75));
            } else {
                jsonObject.put("photo", sqliteDataBaseHelper.getContactImage(StoredContactid));
            }
            jsonObject.put("favorite", sqliteDataBaseHelper.getContactFavorite(StoredContactid));
            jsonObject.put("addresses", jsonArrayAddress);
            jsonObject.put("companyName", editTextcompname.getText().toString());
            jsonObject.put("emailAddress", editTextemail.getText().toString());
            jsonObject.put("firstName", editTextname.getText().toString());
            jsonObject.put("middleName", editTextmname.getText().toString());
            jsonObject.put("lastName", editTextlname.getText().toString());
            String Birthday = editBirthday.getText().toString().trim();
            if (StringUtils.isNotBlank(Birthday)) {
                Date birthdate = null;
                try {
                    birthdate = new SimpleDateFormat("dd-MMM-yyyy").parse(Birthday);
                    final Calendar myCalendar = Calendar.getInstance();
                    myCalendar.setTime(birthdate);
                    jsonObject.put("dateOfBirth", myCalendar.getTimeInMillis());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                jsonObject.put("dateOfBirth", null);
            }
            jsonObject.put("siteUrl", editTextcompurl.getText().toString());
            jsonObject.put("phones", jsonArrayphone);
            jsonObject.put("facebookUrl", editTextfburl.getText().toString());
            jsonObject.put("linkedInUrl", editTextliurl.getText().toString());
            jsonObject.put("twitterUrl", editTexttwurl.getText().toString());
            jsonObject.put("contactCode", sqliteDataBaseHelper.getContactCode(StoredContactid));
            jsonObject.put("id", StoredContactid);
            jsonObject.put("status", "ACTIVE");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    // Get Organization from Server
    private void getOrganizationData() {
        String REGISTER_URL = "/mindmemobile-web/api/v1/mindmemobile/orgs/" + sqliteDataBaseHelper.getOrganizationId() + "";
        VolleyApi.getInstance().getJsonObject(getBaseContext(), REGISTER_URL, new VolleyApi.ApiResponse<VolleyApi.ApiResult>() {
            @Override
            public void onCompletion(VolleyApi.ApiResult result) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                if (result.success && result.dataIsObject()) {
                    Log.i(TAG, "List Items Updated");
                    JSONObject response = result.getDataAsObject();
                    sqliteDataBaseHelper.DeleteLists();
                    sqliteDataBaseHelper.insertIntoLists(response);
                    sqliteDataBaseHelper.DeleteInterests();
                    sqliteDataBaseHelper.insertInterestsJsonObj(response);
                    sqliteDataBaseHelper.DeleteTags();
                    sqliteDataBaseHelper.insertNewTag(response);
                    finish();
                } else {
                    Log.e(TAG, result.getVolleyError().toString());
                }
            }
        });
    }

    private void showProgressDialog(String message) {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(message);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    private String nullValidation(String data) {
        if (data == null || data.equalsIgnoreCase("null")) {
            return "";
        } else {
            return data;
        }
    }
}
