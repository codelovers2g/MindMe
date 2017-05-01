package com.mindmesolo.mindme.ContactAndLists;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mindmesolo.mindme.AudioRecorder.MediaRecordingNew;
import com.mindmesolo.mindme.OrganizationModel;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.CircleImageView;
import com.mindmesolo.mindme.helper.RuntimePermissionsActivity;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.ToastShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import id.zelory.compressor.Compressor;
import id.zelory.compressor.FileUtil;

/**
 * Created by User1 on 6/22/2016.
 */
public class AddContact extends RuntimePermissionsActivity implements View.OnClickListener {
    private static final String TAG = "AddContact";
    private static final int REQUEST_PERMISSIONS = 20;
    EditText editTextname, editTextmname, editTextlname, editTextadress1, editTextaddress2, editTextaddress3, editTextaddress4;
    EditText editTextmobile, editTextemail, editTextcompname, editTextcompurl, editTextfburl, editTextliurl, editTexttwurl, editTextpin;
    EditText editBirthday;
    String fName, mName, lName, address, address2, address3, address4, mobile, email, pincode, companyName;
    Button buttonAdd, buttoncancel, buttonChoose;
    CircleImageView imageView;
    String Storedemail, Storedpassword, StoredOrgid = null;
    String jsonStr;
    String valid_email = null;
    SqliteDataBaseHelper dBhelper;
    Spinner spinner;
    boolean CameraPermission = false;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private ProgressDialog pDialog;
    private Dialog dialog;

    private TextView buttonRemoveBirthday;

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }//convert image into bitmap format

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }//decode bitmap

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnewcontact);
        buttonRemoveBirthday = (TextView) findViewById(R.id.buttonRemoveBirthday);
        buttonRemoveBirthday.setVisibility(View.GONE);
        buttonAdd = (Button) findViewById(R.id.Add);
        buttoncancel = (Button) findViewById(R.id.cancel);
        buttonChoose = (Button) findViewById(R.id.chooseImage);
        imageView = (CircleImageView) findViewById(R.id.imageView);
        spinner = (Spinner) findViewById(R.id.spinnerPhoneType);
        dBhelper = new SqliteDataBaseHelper(this);
        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("UserLogin", Context.MODE_PRIVATE);
        Storedemail = sharedPreferences.getString("Email", null);
        Storedpassword = sharedPreferences.getString("Password", null);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Creating...");
        pDialog.setCancelable(false);

        checkPermissions();

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

        fName = editTextname.getText().toString();
        mName = editTextmname.getText().toString();
        lName = editTextlname.getText().toString();
        address = editTextadress1.getText().toString();
        address2 = editTextaddress2.getText().toString();
        address3 = editTextaddress3.getText().toString();
        address4 = editTextaddress4.getText().toString();
        email = editTextemail.getText().toString();
        companyName = editTextcompname.getText().toString();
        pincode = editTextpin.getText().toString();
        mobile = editTextmobile.getText().toString();


        editTextmobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isValidPhone(mobile)) {
                    editTextmobile.setError("Mobile must be Filled & must be 10 digit");
                }
            }
        });
        editTextemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Is_Valid_Email(editTextemail);
            }
        });

        buttonAdd.setOnClickListener(this);
        buttoncancel.setOnClickListener(this);
        buttonChoose.setOnClickListener(this);


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
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddContact.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            //In which you need put here
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                            editBirthday.setText(sdf.format(myCalendar.getTime()));
                            buttonRemoveBirthday.setVisibility(View.VISIBLE);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });


    }

    private void checkPermissions() {
        AddContact.super.requestAppPermissions(new String[]{Manifest.permission.CAMERA,
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Add:
                if ((editTextmobile.getText().toString().length() == 0 || editTextmobile.getText().toString().isEmpty()) &&
                        (editTextemail.getText().toString().length() == 0 || editTextemail.getText().toString().isEmpty())) {
                    ToastShow.setText(AddContact.this, "Contact Must Have Phone or Email", Toast.LENGTH_SHORT);
                } else {
                    AddNewContact();
                }
                break;
            case R.id.cancel:
                finish();
                break;
            case R.id.chooseImage:
                showDialog();
                break;
        }
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

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void showDialog() {
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

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    public void AddNewContact() {
        SharedPreferences pref = getBaseContext().getSharedPreferences("Organization", Context.MODE_PRIVATE);
        StoredOrgid = pref.getString("OrgId", null);
        String orgid = StoredOrgid;
        String email = Storedemail;
        String password = Storedpassword;
        String token = email + ":" + password;
        byte[] data = null;
        try {
            data = token.getBytes("UTF-8");

        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        final String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        final String finalToken = base64.replace("\n", "");
        String myBase64Image = null;
        if (bitmap != null) {
            myBase64Image = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 50);
        }
        JSONArray jsonArrayAddress = new JSONArray();
        try {
            JSONObject list1 = new JSONObject();
            list1.put("addressLine1", address);
            list1.put("addressLine2", address2);
            list1.put("addressLine3", address3);
            list1.put("addressLine4", address4);
            list1.put("city", address2);
            list1.put("country", address4);
            list1.put("postalCode", pincode);
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
            long time = System.currentTimeMillis();
            jsonObject.put("createdOn", String.valueOf(time));
            jsonObject.put("updatedOn", String.valueOf(time));
            jsonObject.put("photo", myBase64Image);
            jsonObject.put("addresses", jsonArrayAddress);
            jsonObject.put("companyName", editTextcompname.getText().toString());
            jsonObject.put("emailAddress", editTextemail.getText().toString());
            jsonObject.put("firstName", editTextname.getText().toString());
            jsonObject.put("middleName", editTextmname.getText().toString());
            jsonObject.put("lastName", editTextlname.getText().toString());
            String Birthday = editBirthday.getText().toString();
            if (Birthday != null) {
                Date birthdate = null;
                try {
                    birthdate = new SimpleDateFormat("dd-MMM-yyyy").parse(Birthday);
                    final Calendar myCalendar = Calendar.getInstance();
                    myCalendar.setTime(birthdate);
                    jsonObject.put("dateOfBirth", myCalendar.getTimeInMillis());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            jsonObject.put("siteUrl", editTextcompurl.getText().toString());
            jsonObject.put("phones", jsonArrayphone);
            jsonObject.put("facebookUrl", editTextfburl.getText().toString());
            jsonObject.put("linkedInUrl", editTextliurl.getText().toString());
            jsonObject.put("twitterUrl", editTexttwurl.getText().toString());
            jsonObject.put("contactCode", "CUSTOMER");
            jsonObject.put("id", "");
            jsonObject.put("status", "ACTIVE");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonStr = jsonObject.toString();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        showProgressDialog();
        final String ADD_INTEREST_URL = OrganizationModel.getApiBaseUrl() + orgid + "/contacts";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ADD_INTEREST_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dBhelper.insertNewContact(response);
                        hideProgressDialog();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "VOLLEY----->" + error.toString());
                        hideProgressDialog();
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return jsonStr == null ? null : jsonStr.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            jsonStr, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Basic " + finalToken);
                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private boolean isValidPhone(String mob) {
        if (mob != null && mob.length() < 10) {
            return true;
        }

        return false;
    }

    public void Is_Valid_Email(EditText edt) {
        if (edt.getText().toString() == null) {
            edt.setError("Invalid Email Address");
            valid_email = null;
        } else if (isEmailValid(edt.getText().toString()) == false) {
            edt.setError("Invalid Email Address");
            valid_email = null;
        } else {
            valid_email = edt.getText().toString();
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
