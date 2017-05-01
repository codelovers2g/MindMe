package com.mindmesolo.mindme.CreateCampaign;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.ViewCampaigns.CampaignData;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.OnStartDragListener;
import com.mindmesolo.mindme.helper.SimpleItemTouchHelperCallback;
import com.mindmesolo.mindme.helper.ToastShow;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;
import id.zelory.compressor.FileUtil;

/**
 * Created by User1 on 7/27/2016.
 */

public class CreateTextCampaign extends AppCompatActivity implements OnStartDragListener {

    private static final String TAG = "CreateTextCampaign";
    String OrganizationId;
    EditText editTextText;
    TextView ctaText, helpText;
    TextView textViewCreditInfo, textViewCount, textViewTotalCount, textViewCtaMessage;
    TextView orgName;
    Button buttonContinue;
    DataHelper dataHelper = new DataHelper();
    ImageButton imageButtonAdd;
    JSONObject jsonObjectFinalCampaign = new JSONObject();
    JSONArray jsonArrayComponent = new JSONArray();
    String StoredName;
    private ArrayList<CampaignData> List = new ArrayList<>();
    private RecyclerView recyclerView;
    private AudioVideoAdapter audioVideoAdapter;
    private ItemTouchHelper mItemTouchHelper;

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createtext);

        SharedPreferences pref = getBaseContext().getSharedPreferences("Organization", Context.MODE_PRIVATE);
        OrganizationId = pref.getString("OrgId", null);

        SharedPreferences pref2 = getApplicationContext().getSharedPreferences("UserData", MODE_PRIVATE);
        StoredName = pref2.getString("Name", null);

        orgName = (TextView) findViewById(R.id.orgName);

        orgName.setText(StoredName + ":");

        editTextText = (EditText) findViewById(R.id.createText);
        editTextText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        textViewCreditInfo = (TextView) findViewById(R.id.creditDetail);
        textViewCount = (TextView) findViewById(R.id.count);
        textViewTotalCount = (TextView) findViewById(R.id.totalCount);
        ctaText = (TextView) findViewById(R.id.ctaText);
        helpText = (TextView) findViewById(R.id.helpText);
        textViewCtaMessage = (TextView) findViewById(R.id.ctaMessage);
        buttonContinue = (Button) findViewById(R.id.btnContinue);
        imageButtonAdd = (ImageButton) findViewById(R.id.Add);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        imageButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CreateEmailOption.class);
                intent.putExtra("Message", "Text");
                startActivityForResult(intent, 1);
            }
        });

        editTextText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleTextLengths();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        audioVideoAdapter = new AudioVideoAdapter(List, this);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(audioVideoAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(audioVideoAdapter);

        mItemTouchHelper = new ItemTouchHelper(callback);

        mItemTouchHelper.attachToRecyclerView(recyclerView);

        // Change it according to the new roles


        String series = getIntent().getStringExtra("Series");

        if (series != null && series.equals("yes")) {
            helpText.setVisibility(View.VISIBLE);
            imageButtonAdd.setVisibility(View.GONE);
            ctaText.setVisibility(View.GONE);
        }


        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jsonObjectFinalCampaign = new JSONObject();

                jsonArrayComponent = new JSONArray();

                String subject = editTextText.getText().toString();

                if (StringUtils.isEmpty(subject) || StringUtils.isBlank(subject)) {
                    ToastShow.setText(getBaseContext(), "Please enter sms message.", Toast.LENGTH_SHORT);

                } else {

                    Bundle exBundle = getIntent().getExtras();

                    String finalCampaign = exBundle.getString("ChooseRouteObject");

                    String component = exBundle.getString("Components");

                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("message", StoredName + ": " + subject);
                        jsonObject.put("type", "SMS_SUBJECT");
                        jsonObject.put("order", "1");
                        jsonObject.put("orgId", OrganizationId);

                        jsonObjectFinalCampaign = new JSONObject(finalCampaign);
                        if (component != null) {
                            jsonArrayComponent = new JSONArray(component);
                        }
                        getAllComponents();

                        ///  testing code need Updation
                        // new code start Here
                        ArrayList<String> NextSection = getIntent().getStringArrayListExtra("NextSection");
                        if (NextSection != null && NextSection.size() > 0) {
                            NextSection.remove("TEXT");
                            if (NextSection.contains("VOICE")) {
                                jsonArrayComponent.put(jsonObject);
                                Intent intent = new Intent(getBaseContext(), CreateVoiceCampaign.class);
                                intent.putExtra("ChooseRouteObject", jsonObjectFinalCampaign.toString());
                                intent.putExtra("Components", jsonArrayComponent.toString());
                                intent.putExtra("Series", "yes");
                                startActivity(intent);
                            } else {
                                jsonArrayComponent.put(jsonObject);
                                Intent intent = new Intent(getBaseContext(), CreateCampaignPreviewScreen.class);
                                intent.putExtra("ChooseRouteObject", jsonObjectFinalCampaign.toString());
                                intent.putExtra("Components", jsonArrayComponent.toString());
                                startActivity(intent);
                            }
                        } else {
                            jsonArrayComponent.put(jsonObject);
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

        handleTextLengths();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String result = data.getStringExtra("result");
            switch (requestCode) {
                case 1:
                    if (result != null) {
                        updateListView(data);
                        audioVideoAdapter.notifyDataSetChanged();
                    } else {
                        updateListView3(data);
                        audioVideoAdapter.notifyDataSetChanged();
                    }
                    break;
                case 2:
                    try {
                        CampaignData ListItem = audioVideoAdapter.List.get(audioVideoAdapter.getPosition());
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
                        handleTextViews(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    audioVideoAdapter.notifyDataSetChanged();
                    break;
                case 3:
                    textViewCtaMessage.setVisibility(View.GONE);
                    imageFromCameraTest(data);
                    audioVideoAdapter.notifyDataSetChanged();
                    break;
                case 4:
                    textViewCtaMessage.setVisibility(View.GONE);
                    getVideo(data);
                    getVideoImage(data);
                    audioVideoAdapter.notifyDataSetChanged();
                    break;
                case 5:
                    textViewCtaMessage.setVisibility(View.GONE);
                    getAudio(data);
                    audioVideoAdapter.notifyDataSetChanged();
                    break;
                case 6:
                    imageFromAdobeSdk(data);
                    audioVideoAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void NotifyDataDelete() {
        helpText.setVisibility(View.GONE);
        imageButtonAdd.setVisibility(View.VISIBLE);
        ctaText.setVisibility(View.VISIBLE);
        textViewCtaMessage.setVisibility(View.GONE);
    }

    //update ListItems if user add new item in ListView
    public void updateListView(Intent intentData) {
        CampaignData AddNewItem = new CampaignData();
        String ExtraData = intentData.getStringExtra("result");
        switch (intentData.getStringExtra("Title")) {
            case "Image":
            case "IMAGE":
                AddNewItem.setTitle("Image");
                AddNewItem.setImage(R.drawable.image);
                if (intentData.getData() != null) {
                    try {
                        File actualImage = FileUtil.from(this, intentData.getData());
                        Bitmap bitmap2 = Compressor.getDefault(this).compressToBitmap(actualImage);
                        AddNewItem.setBitmapImage(bitmap2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("mediaFileName", String.valueOf(System.currentTimeMillis()) + ".png");
                    jsonObject.put("media", intentData.getData());
                    jsonObject.put("type", "IMAGE");
                    AddNewItem.setExtraData(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                List.add(AddNewItem);
                textViewCtaMessage.setVisibility(View.GONE);
                audioVideoAdapter.notifyDataSetChanged();
                break;
            case "Video":
            case "VIDEO":
                AddNewItem.setTitle("Video");
                AddNewItem.setImage(R.drawable.play);
                if (intentData.getData() != null) {
                    JSONObject jsonObject2 = new JSONObject();
                    try {
                        jsonObject2.put("mediaFileName", "video.mp4");
                        jsonObject2.put("media", intentData.getData());
                        jsonObject2.put("type", "VIDEO");
                        jsonObject2.put("mediaStorageType", "AMAZONS_3");
                        jsonObject2.put("status", "ACTIVE");
                        AddNewItem.setVideoExtraData(jsonObject2.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    File VideoFile = null;
                    try {
                        VideoFile = FileUtil.from(this, intentData.getData());
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
                if (intentData.getExtras() != null) {
                    AddNewItem.setAudio(true);
                    JSONObject jsonObject1 = new JSONObject();
                    try {
                        jsonObject1.put("mediaFileName", String.valueOf(System.currentTimeMillis()) + ".mp3");
                        jsonObject1.put("media", intentData.getStringExtra("result"));
                        jsonObject1.put("type", "VOICERECORDING");
                        AddNewItem.setExtraData(jsonObject1.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                textViewCtaMessage.setVisibility(View.GONE);
                List.add(AddNewItem);
                break;
            case MediaTypes.TITLE:
                AddNewItem.setTitle("Title");
                AddNewItem.setExtraData(ExtraData);
                AddNewItem.setImage(R.drawable.titleicon);
                List.add(AddNewItem);
                handleTextViews(ExtraData);
                break;
            case MediaTypes.PARAGRAPH:
                AddNewItem.setTitle("Paragraph");
                AddNewItem.setExtraData(ExtraData);
                AddNewItem.setImage(R.drawable.paragraph_icon);
                List.add(AddNewItem);
                handleTextViews(ExtraData);
                break;
            case MediaTypes.FEEDBACK:
                AddNewItem.setTitle("Feedback");
                AddNewItem.setExtraData(ExtraData);
                AddNewItem.setImage(R.drawable.feedback);
                handleTextViews(ExtraData);
                List.add(AddNewItem);
                break;
            case MediaTypes.YESNOMAYBE:
            case MediaTypes.RESPONSE:
                AddNewItem.setTitle("Response");
                AddNewItem.setExtraData(ExtraData);
                AddNewItem.setImage(R.drawable.response);
                handleTextViews(ExtraData);
                List.add(AddNewItem);
                break;
            case MediaTypes.POLL:
                AddNewItem.setTitle("Poll");
                AddNewItem.setExtraData(ExtraData);
                AddNewItem.setImage(R.drawable.poll);
                handleTextViews(ExtraData);
                List.add(AddNewItem);
                break;
            case MediaTypes.LINKCALL:
            case MediaTypes.CALL_TO_ACTION:
                AddNewItem.setTitle("Link/Call");
                AddNewItem.setExtraData(ExtraData);
                AddNewItem.setImage(R.drawable.linkcall);
                List.add(AddNewItem);
                handleTextViews(ExtraData);
                break;
        }

        imageButtonAdd.setVisibility(View.GONE);
        ctaText.setVisibility(View.GONE);
        audioVideoAdapter.notifyDataSetChanged();
    }

    //update ListItems if user add new item in ListView
    public void updateListView3(Intent intentData) {
        CampaignData AddNewItem = new CampaignData();
        switch (intentData.getStringExtra("Title")) {
            case "Image":
                AddNewItem.setTitle("Image");
                AddNewItem.setImage(R.drawable.camera);
                List.add(AddNewItem);
                break;
            case "Video":
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
        imageButtonAdd.setVisibility(View.GONE);
        ctaText.setVisibility(View.GONE);
        audioVideoAdapter.notifyDataSetChanged();
    }

    //get all components for next activity.
    private void getAllComponents() {
        int i = 2;
        for (CampaignData data : audioVideoAdapter.List) {
            if (data.getExtraData() != null) {
                try {
                    //Don't change or remove it this because video object has two components video image && video file.
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

    private void handleTextViews(String Data) {
        textViewCtaMessage.setVisibility(View.VISIBLE);
        textViewCtaMessage.setText(fromHtml("Click here to reply: <font color=#446cb3>Link to CTA here</font>"));
//        try {
//            String type = new JSONObject(Data).getJSONObject("callToAction").getString("type");
//            switch (type) {
//                case MediaTypes.YESNOMAYBE:
//                case MediaTypes.RESPONSE:
//                    try {
//                        JSONObject jsonObject = new JSONObject(Data);
//                        JSONObject callToAction = jsonObject.getJSONObject("callToAction");
//                        JSONObject yesNoMaybe = callToAction.getJSONObject("yesNoMaybe");
//                        JSONArray jsonArrayBtn = yesNoMaybe.getJSONArray("buttons");
//                        switch (jsonArrayBtn.length()) {
//                            case 1:
//                                textViewCtaMessage.setText("Reply ## for Yes");
//                                break;
//                            case 2:
//                                String secondObj =jsonArrayBtn.getString(1);
//                                secondObj = secondObj.substring(0,1).toUpperCase() + secondObj.substring(1).toLowerCase();
//                                textViewCtaMessage.setText("Reply ## for Yes,## for "
//                                        + secondObj + "");
//                                break;
//                            case 3:
//                                textViewCtaMessage.setText("Reply ## for Yes,## for No,## for Maybe");
//                                break;
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//                case MediaTypes.POLL:
//                    try {
//                        JSONObject jsonObject = new JSONObject(Data);
//                        JSONObject callToAction = jsonObject.getJSONObject("callToAction");
//                        JSONArray pollItems = callToAction.getJSONArray("pollItems");
//                        switch (pollItems.length()) {
//                            case 2:
//                                String text = "Reply "
//                                        + pollItems.getJSONObject(0).getString("label") + ","
//                                        + pollItems.getJSONObject(1).getString("label") + "";
//                                textViewCtaMessage.setText(text);
//                                break;
//                            case 3:
//                                String text2 = "Reply "
//                                        + pollItems.getJSONObject(0).getString("label") + ","
//                                        + pollItems.getJSONObject(1).getString("label") + ","
//                                        + pollItems.getJSONObject(2).getString("label") + "";
//                                textViewCtaMessage.setText(text2.replace(",,", ","));
//                                break;
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    break;
//                case MediaTypes.FEEDBACK:
//                    textViewCtaMessage.setText("Reply 1-5, 5 = BEST");
//                    break;
//                case MediaTypes.CALLME:
//                    try {
//                        JSONObject jsonObject = new JSONObject(Data);
//                        JSONObject linkCall = jsonObject.getJSONObject("callToAction").getJSONObject("linkCall");
//                        textViewCtaMessage.setText(handleLinkCall(linkCall));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        handleTextLengths();
    }

    private void handleTextLengths() {
        int len = textViewCtaMessage.length();
        int len1 = orgName.length();
        int length = editTextText.getText().length() + 18 + len + len1;
        if (length <= 160) {
            textViewCount.setText(String.valueOf(160 - length) + "/160");
            textViewCreditInfo.setTextColor(Color.parseColor("#01bfff"));
            textViewCreditInfo.setText(R.string.onecredit);
        }
        if (length > 160 && length <= 320) {
            textViewCount.setText(String.valueOf(320 - length) + "/320");
            textViewCreditInfo.setTextColor(Color.parseColor("#ff0000"));
            textViewCreditInfo.setText(R.string.twocredit);
        }
    }

    private String handleLinkCall(JSONObject linkCall) {
        String linkValue = null;
        try {
            if (linkCall.getString("link").length() > 0) {
                linkValue = "Click " + linkCall.getString("link");
            } else {
                linkValue = "Click " + "Add Url";
            }
        } catch (JSONException e) {
            try {
                if (linkCall.getString("phone").length() > 0) {
                    linkValue = "Call " + linkCall.getString("phone");
                } else {
                    linkValue = "Call " + "Add Phone";
                }
            } catch (JSONException e1) {
                try {
                    if (linkCall.getString("email").length() > 0) {
                        linkValue = "Click " + linkCall.getString("email");
                    } else {
                        linkValue = "Click " + "Add Email";
                    }
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return linkValue;
    }

    private void imageFromCameraTest(Intent data) {

        CampaignData ListItem = audioVideoAdapter.List.get(audioVideoAdapter.getPosition());

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

        CampaignData ListItem = audioVideoAdapter.List.get(audioVideoAdapter.getPosition());

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

    private void getAudio(Intent intentData) {
        CampaignData ListItem = audioVideoAdapter.List.get(audioVideoAdapter.getPosition());
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

    private void getVideo(Intent intent) {
        CampaignData ListItem = audioVideoAdapter.List.get(audioVideoAdapter.getPosition());
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

    private void getVideoImage(Intent data) {
        CampaignData ListItem = audioVideoAdapter.List.get(audioVideoAdapter.getPosition());
        File VideoFile = new File(dataHelper.loadVideoFile(data.getData(), getBaseContext()));
        Bitmap OrgnalImage = ThumbnailUtils.createVideoThumbnail(VideoFile.getAbsoluteFile().toString(), MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
        Bitmap image = dataHelper.putOverlay(OrgnalImage, getBaseContext());
        ListItem.setBitmapImage(image);
        if (image != null) {
            String myBase64Image = dataHelper.encodeToBase64(image, Bitmap.CompressFormat.JPEG, 90);

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
