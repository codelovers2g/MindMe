package com.mindmesolo.mindme.CreateCampaign;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.ViewCampaigns.CampaignData;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.OnStartDragListener;
import com.mindmesolo.mindme.helper.RuntimePermissionsActivity;
import com.mindmesolo.mindme.helper.SimpleItemTouchHelperCallback;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;
import id.zelory.compressor.FileUtil;

/*
 * Created by User1 on 7/18/2016.
 */
public class CreateEmailCampaign extends RuntimePermissionsActivity implements OnStartDragListener {

    public static final String TAG = "CreateEmailCampaign";
    private static final int REQUEST_PERMISSIONS = 20;
    public static boolean CameraPermission = false;
    private static String OrganizationId;
    public ArrayList<CampaignData> List = new ArrayList<>();
    public RecyclerView recyclerView;
    public CreateEmailAdapter createEmailAdapter;
    public ItemTouchHelper mItemTouchHelper;
    ImageButton buttonAdd;
    Button buttonContinue;
    EditText editTextSubject;
    JSONObject jsonObjectFinalCampaign = new JSONObject();
    JSONArray jsonArrayComponent = new JSONArray();
    DataHelper dataHelper = new DataHelper();
    SqliteDataBaseHelper sqliteDataBaseHelper;
    private Dialog dialogue_custom;

    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.createemailcampaign);

        sqliteDataBaseHelper = new SqliteDataBaseHelper(getBaseContext());

        OrganizationId = sqliteDataBaseHelper.getOrganizationId();

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        editTextSubject = (EditText) findViewById(R.id.AddEmailSubject);

        buttonContinue = (Button) findViewById(R.id.btnContinue);

        buttonAdd = (ImageButton) findViewById(R.id.Add);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEmailAdapter.position = -1;
                Intent intent = new Intent(getBaseContext(), CreateEmailOption.class);
                intent.putExtra("Message", "Email");
                intent.putExtra("CTA", createEmailAdapter.checkCta());
                startActivityForResult(intent, 1);
            }
        });

        checkPermissions();

        prepareData();

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonObjectFinalCampaign = new JSONObject();
                jsonArrayComponent = new JSONArray();
                String subject = editTextSubject.getText().toString();
                if (subject.isEmpty() || subject == null) {
                    getCustomDialogue("Enter Subject", "Please enter email subject.");
                } else {
                    getAllComponents();
                    Bundle exBundle = getIntent().getExtras();
                    String finalObject = exBundle.getString("ChooseRouteObject");
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("message", subject);
                        jsonObject.put("type", "EMAIL_SUBJECT");
                        jsonObject.put("order", 1);
                        jsonObject.put("orgId", OrganizationId);
                        jsonArrayComponent.put(jsonObject);
                        jsonObjectFinalCampaign = new JSONObject(finalObject);

                        // get all section names from the route
                        ArrayList<String> NextSection = getIntent().getStringArrayListExtra("NextSection");
                        if (NextSection != null && NextSection.size() > 0) {
                            startIntent(NextSection);
                        } else {
                            Intent intent = new Intent(getBaseContext(), CreateCampaignPreviewScreen.class);
                            intent.putExtra("ChooseRouteObject", jsonObjectFinalCampaign.toString());
                            intent.putExtra("Components", jsonArrayComponent.toString());
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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

    // initialize all views
    private void prepareData() {
        List.add(new CampaignData("Title", R.drawable.titleicon));
        List.add(new CampaignData("Image", R.drawable.camera));
        List.add(new CampaignData("Paragraph", R.drawable.paragraph_icon));
        List.add(new CampaignData("Call to Action", R.drawable.response));
        setupListView();
    }

    // setup list items
    private void setupListView() {

        createEmailAdapter = new CreateEmailAdapter(List, this);

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(createEmailAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(createEmailAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    //update ListItems if user add new item in ListView
    public void updateListView(Intent intent) {
        CampaignData AddNewItem = new CampaignData();
        String ExtraData = intent.getStringExtra("result");
        switch (intent.getStringExtra("Title")) {
            case "Image":
            case "IMAGE":
                AddNewItem.setTitle("Image");
                AddNewItem.setImage(R.drawable.image);
                if (intent.getData() != null) {
                    File actualImage = null;
                    try {
                        actualImage = FileUtil.from(this, intent.getData());
                        if (actualImage.exists()) {
                            Bitmap bitmap2 = Compressor.getDefault(this).compressToBitmap(actualImage);
                            AddNewItem.setBitmapImage(bitmap2);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("mediaFileName", String.valueOf(System.currentTimeMillis()) + ".png");
                    jsonObject.put("media", intent.getData());
                    jsonObject.put("type", "IMAGE");
                    AddNewItem.setExtraData(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                List.add(AddNewItem);
                createEmailAdapter.notifyDataSetChanged();
                break;
            case "Video":
            case "VIDEO":
                AddNewItem.setTitle("Video");
                AddNewItem.setImage(R.drawable.play);
                if (intent.getData() != null) {
                    JSONObject jsonObject2 = new JSONObject();
                    try {
                        jsonObject2.put("mediaFileName", "video.mp4");
                        jsonObject2.put("media", intent.getData());
                        jsonObject2.put("type", "VIDEO");
                        jsonObject2.put("mediaStorageType", "AMAZONS_3");
                        jsonObject2.put("status", "ACTIVE");
                        AddNewItem.setVideoExtraData(jsonObject2.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    File VideoFile = null;
                    try {
                        VideoFile = FileUtil.from(this, intent.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (VideoFile.exists()) {
                        Bitmap OrgnalImage = ThumbnailUtils.createVideoThumbnail(VideoFile.getAbsolutePath(),
                                MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
                        Bitmap image = dataHelper.putOverlay(OrgnalImage, getBaseContext());
                        AddNewItem.setBitmapImage(image);
                        if (image != null) {
                            JSONObject jsonObject3 = new JSONObject();
                            try {
                                jsonObject3.put("mediaFileName", String.valueOf(System.currentTimeMillis()) + ".png");
                                jsonObject3.put("media", dataHelper.encodeToBase64(image, Bitmap.CompressFormat.JPEG, 90));
                                jsonObject3.put("type", "VIDEO_IMAGE");
                                jsonObject3.put("status", "ACTIVE");
                                AddNewItem.setExtraData(jsonObject3.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e(TAG, "No image Found From Video file");
                            }
                        } else {
                            Log.e(TAG, "No image Found From Video file");
                        }

                    }
                }
                List.add(AddNewItem);
                break;
            case "Audio":
                AddNewItem.setTitle("Audio");
                AddNewItem.setImage(R.drawable.microphone);
                if (intent.getExtras() != null) {
                    AddNewItem.setAudio(true);
                    JSONObject jsonObject1 = new JSONObject();
                    try {
                        jsonObject1.put("mediaFileName", String.valueOf(System.currentTimeMillis()) + ".mp3");
                        jsonObject1.put("media", intent.getStringExtra("result"));
                        jsonObject1.put("type", "VOICERECORDING");
                        AddNewItem.setExtraData(jsonObject1.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                List.add(AddNewItem);
                break;
            case MediaTypes.TITLE:
                AddNewItem.setTitle("Title");
                AddNewItem.setExtraData(ExtraData);
                AddNewItem.setImage(R.drawable.titleicon);
                List.add(AddNewItem);
                break;
            case MediaTypes.PARAGRAPH:
                AddNewItem.setTitle("Paragraph");
                AddNewItem.setExtraData(ExtraData);
                AddNewItem.setImage(R.drawable.paragraph_icon);
                List.add(AddNewItem);
                break;
            case MediaTypes.FEEDBACK:
                AddNewItem.setTitle("Feedback");
                AddNewItem.setExtraData(ExtraData);
                AddNewItem.setImage(R.drawable.feedback);
                List.add(AddNewItem);
                break;
            case MediaTypes.YESNOMAYBE:
            case MediaTypes.RESPONSE:
                AddNewItem.setTitle("Response");
                AddNewItem.setExtraData(ExtraData);
                AddNewItem.setImage(R.drawable.response);
                List.add(AddNewItem);
                break;
            case MediaTypes.POLL:
                AddNewItem.setTitle("Poll");
                AddNewItem.setExtraData(ExtraData);
                AddNewItem.setImage(R.drawable.poll);
                List.add(AddNewItem);
                break;
            case MediaTypes.LINKCALL:
            case MediaTypes.CALL_TO_ACTION:
                AddNewItem.setTitle("Link/Call");
                AddNewItem.setExtraData(ExtraData);
                AddNewItem.setImage(R.drawable.linkcall);
                List.add(AddNewItem);
                break;
        }

    }

    //update ListItems if user add new item in ListView
    public void updateListView3(Intent intentData) {
        CampaignData AddNewItem = new CampaignData();
        switch (intentData.getStringExtra("Title")) {
            case "Image":
            case "IMAGE":
                AddNewItem.setTitle("Image");
                AddNewItem.setImage(R.drawable.camera);
                List.add(AddNewItem);
                break;
            case "Video":
            case "VIDEO":
                AddNewItem.setTitle("Video");
                AddNewItem.setImage(R.drawable.play);
                List.add(AddNewItem);
                break;
            case "Audio":
                AddNewItem.setTitle("Audio");
                AddNewItem.setImage(R.drawable.microphone);
                List.add(AddNewItem);
                break;
            case MediaTypes.TITLE:
                AddNewItem.setTitle("Title");
                AddNewItem.setImage(R.drawable.titleicon);
                List.add(AddNewItem);
                break;
            case MediaTypes.PARAGRAPH:
                AddNewItem.setTitle("Paragraph");
                AddNewItem.setImage(R.drawable.paragraph_icon);
                List.add(AddNewItem);
                break;
            case MediaTypes.FEEDBACK:
                AddNewItem.setTitle("Feedback");
                AddNewItem.setImage(R.drawable.feedback);
                List.add(AddNewItem);
                break;
            case MediaTypes.YESNOMAYBE:
            case MediaTypes.RESPONSE:
                AddNewItem.setTitle("Response");
                AddNewItem.setImage(R.drawable.response);
                List.add(AddNewItem);
                break;
            case MediaTypes.POLL:
                AddNewItem.setTitle("Poll");
                AddNewItem.setImage(R.drawable.poll);
                List.add(AddNewItem);
                break;
            case MediaTypes.LINKCALL:
            case MediaTypes.CALL_TO_ACTION:
                AddNewItem.setTitle("Link/Call");
                AddNewItem.setImage(R.drawable.linkcall);
                List.add(AddNewItem);
                break;
        }
    }

    // handle all incoming results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String result = data.getStringExtra("result");
            switch (requestCode) {
                case 1:
                    if (result != null) {
                        updateListView(data);
                        createEmailAdapter.notifyDataSetChanged();
                    } else {
                        updateListView3(data);
                        createEmailAdapter.notifyDataSetChanged();
                    }
                    break;
                case 2:
                    try {
                        CampaignData ListItem = createEmailAdapter.List.get(createEmailAdapter.getPosition());
                        ListItem.setExtraData(result);
                        String type = new JSONObject(result).getString("type");
                        switch (type) {
                            case "TITLE":
                                ListItem.setTitle("Title");
                                break;
                            case "PARAGRAPH":
                                ListItem.setTitle("Paragraph");
                                break;
                        }
                        switch (new JSONObject(result).getJSONObject("callToAction").getString("type")) {
                            case "YESNOMAYBE":
                                ListItem.setTitle("Response");
                                break;
                            case "POLL":
                                ListItem.setTitle("Poll");
                                break;
                            case "FEEDBACK":
                                ListItem.setTitle("Feedback");
                                break;
                            case "CALLME":
                                ListItem.setTitle("Link/Call");
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    createEmailAdapter.notifyDataSetChanged();
                    break;
                case 3:
                    imageFromCameraTest(data);
                    createEmailAdapter.notifyDataSetChanged();
                    break;
                case 4:
                    getVideo(data);
                    getVideoImage(data);
                    createEmailAdapter.notifyDataSetChanged();
                    break;
                case 5:
                    getAudio(data);
                    createEmailAdapter.notifyDataSetChanged();
                    break;
                case 6:
                    imageFromAdobeSdk(data);
                    createEmailAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    private void getAudio(Intent intentData) {
        CampaignData ListItem = createEmailAdapter.List.get(createEmailAdapter.getPosition());
        if (intentData.getExtras() != null) {
            ListItem.setAudio(true);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("mediaFileName", String.valueOf(System.currentTimeMillis() + ".mp3"));
                jsonObject.put("media", intentData.getStringExtra("result"));
                jsonObject.put("type", "VOICERECORDING");
                ListItem.setExtraData(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void imageFromCameraTest(Intent data) {

        CampaignData ListItem = createEmailAdapter.List.get(createEmailAdapter.getPosition());

        if (data.getData() != null) {

            Bitmap bitmap = null;

            try {

                File actualImage = FileUtil.from(this, data.getData());

                bitmap = Compressor.getDefault(this).compressToBitmap(actualImage);

                ListItem.setBitmapImage(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("mediaFileName", String.valueOf(System.currentTimeMillis()) + ".png");
                jsonObject.put("media", data.getData());
                jsonObject.put("type", "IMAGE");
                jsonObject.put("status", "ACTIVE");
                ListItem.setExtraData(jsonObject.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void imageFromAdobeSdk(Intent data) {

        CampaignData ListItem = createEmailAdapter.List.get(createEmailAdapter.getPosition());

        if (data.getData() != null) {

            Bitmap bitmap = null;

            try {

                Uri editedImageUri = data.getParcelableExtra(AdobeImageIntent.EXTRA_OUTPUT_URI);

                File actualImage = FileUtil.from(this, editedImageUri);

                bitmap = Compressor.getDefault(this).compressToBitmap(actualImage);

                ListItem.setBitmapImage(bitmap);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("mediaFileName", String.valueOf(System.currentTimeMillis()) + ".png");
                    jsonObject.put("media", editedImageUri);
                    jsonObject.put("type", "IMAGE");
                    jsonObject.put("status", "ACTIVE");
                    ListItem.setExtraData(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    private void getVideo(Intent intent) {
        CampaignData ListItem = createEmailAdapter.List.get(createEmailAdapter.getPosition());
        if (intent.getData() != null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("mediaFileName", "video.mp4");
                jsonObject.put("media", intent.getData());
                jsonObject.put("type", "VIDEO");
                jsonObject.put("mediaStorageType", "AMAZONS_3");
                jsonObject.put("status", "ACTIVE");
                ListItem.setVideoExtraData(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getVideoImage(Intent intent) {
        CampaignData ListItem = createEmailAdapter.List.get(createEmailAdapter.getPosition());
        File VideoFile = null;
        try {
            VideoFile = FileUtil.from(this, intent.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (VideoFile.exists()) {
            ListItem.setTitle("Video");
            Bitmap OrgnalImage = ThumbnailUtils.createVideoThumbnail(VideoFile.getAbsoluteFile().toString(), MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
            Bitmap image = dataHelper.putOverlay(OrgnalImage, getBaseContext());
            ListItem.setBitmapImage(image);
            if (image != null) {
                String myBase64Image = dataHelper.encodeToBase64(image, Bitmap.CompressFormat.JPEG, 70);
                JSONObject jsonObject = new JSONObject();
                try {
                    long time = System.currentTimeMillis();
                    jsonObject.put("message", null);
                    jsonObject.put("mediaFileName", String.valueOf(time) + ".png");
                    jsonObject.put("media", myBase64Image);
                    jsonObject.put("type", "VIDEO_IMAGE");
                    jsonObject.put("status", "ACTIVE");
                    ListItem.setExtraData(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "No image Found From Video file");
                }
            } else {
                Log.e(TAG, "No image Found From Video file");
            }
        }

    }

    private void getAllComponents() {
        int i = 2;
        for (CampaignData data : createEmailAdapter.List) {
            if (data.getExtraData() != null) {
                try {
                    if (data.getTitle().equalsIgnoreCase("Video")) {
                        JSONObject jsonObject = new JSONObject(data.getVideoExtraData());
                        jsonObject.put("order", i);
                        jsonObject.put("orgId", OrganizationId);
                        jsonArrayComponent.put(jsonObject);
                    }
                    JSONObject jsonObject = new JSONObject(data.getExtraData());
                    jsonObject.put("order", i);
                    jsonObject.put("orgId", OrganizationId);
                    jsonArrayComponent.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            i++;
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void NotifyDataDelete() {
    }

    public void getCustomDialogue(String AlertTitle, String AlertMessage) {

        dialogue_custom = new Dialog(CreateEmailCampaign.this);

        dialogue_custom.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialogue_custom.setContentView(R.layout.campaign_dialog_layout);

        Button btn_ok_dialogue = (Button) dialogue_custom.findViewById(R.id.btn_ok_dialogue);

        TextView title = (TextView) dialogue_custom.findViewById(R.id.title);

        title.setText(AlertTitle);

        TextView message = (TextView) dialogue_custom.findViewById(R.id.message);
        message.setText(AlertMessage);

        btn_ok_dialogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogue_custom.dismiss();
            }
        });

        dialogue_custom.show();
    }

    public void checkPermissions() {
        CreateEmailCampaign.super.requestAppPermissions(new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO
                },
                R.string.runtime_permissions_txt
                , REQUEST_PERMISSIONS);
    }

    private void startIntent(ArrayList<String> NextSection) {
        NextSection.remove("EMAIL");
        Intent intent = new Intent();
        if (NextSection.size() == 0) {
            intent = new Intent(getBaseContext(), CreateCampaignPreviewScreen.class);
            intent.putExtra("ChooseRouteObject", jsonObjectFinalCampaign.toString());
            intent.putExtra("Components", jsonArrayComponent.toString());
            startActivity(intent);
        } else {
            switch (NextSection.get(0)) {
                case "TEXT":
                    intent = new Intent(getBaseContext(), CreateTextCampaign.class);
                    intent.putExtra("ChooseRouteObject", jsonObjectFinalCampaign.toString());
                    intent.putExtra("Components", jsonArrayComponent.toString());
                    intent.putExtra("NextSection", NextSection);
                    intent.putExtra("Series", "yes");
                    startActivity(intent);
                    break;
                case "VOICE":
                    if (NextSection.size() >= 2) {
                        intent = new Intent(getBaseContext(), CreateTextCampaign.class);
                    } else {
                        intent = new Intent(getBaseContext(), CreateVoiceCampaign.class);
                    }
                    intent.putExtra("ChooseRouteObject", jsonObjectFinalCampaign.toString());
                    intent.putExtra("Components", jsonArrayComponent.toString());
                    intent.putExtra("NextSection", NextSection);
                    intent.putExtra("Series", "yes");
                    startActivity(intent);
                    break;
                default:
                    intent = new Intent(getBaseContext(), CreateCampaignPreviewScreen.class);
                    intent.putExtra("ChooseRouteObject", jsonObjectFinalCampaign.toString());
                    intent.putExtra("Components", jsonArrayComponent.toString());
                    startActivity(intent);
                    break;
            }
        }
    }
}








