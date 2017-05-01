package com.mindmesolo.mindme.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.mindmesolo.mindme.CreateCampaign.MediaTypes;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.ViewCampaigns.CampaignData;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import id.zelory.compressor.Compressor;
import id.zelory.compressor.FileUtil;

/**
 * Created by pc-14 on 2/27/2017.
 */

public class CampaignAndMobilePageHelper {

    private static final String TAG = "CampaignMobile";

    Context context;

    public CampaignAndMobilePageHelper(Context context) {
        this.context = context;
    }

    ;

    //update ListItems if user add new item in ListView
    public void updateListView(Intent intentData, List<CampaignData> List) {
        CampaignData AddNewItem = new CampaignData();
        String ExtraData = intentData.getStringExtra("result");
        switch (intentData.getStringExtra("Title")) {
            case MediaTypes.IMAGE:
                AddNewItem.setTitle("Image");
                AddNewItem.setImage(R.drawable.camera);
                setImage(AddNewItem, intentData);
                List.add(AddNewItem);
                break;
            case MediaTypes.VIDEO:
                AddNewItem.setTitle("Video");
                AddNewItem.setImage(R.drawable.play);
                setVideoItems(AddNewItem, intentData);
                List.add(AddNewItem);
                break;
            case MediaTypes.AUDIO:
                AddNewItem.setTitle("Audio");
                AddNewItem.setImage(R.drawable.microphone);
                setAudioData(AddNewItem, intentData);
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
            case MediaTypes.LEADFORM:
                AddNewItem.setTitle("Lead Form");
                AddNewItem.setImage(R.drawable.lead_form_icon);
                AddNewItem.setLeadFormData(intentData.getParcelableArrayListExtra("LeadFormData"));
                AddNewItem.setLeadFormInterestsData(intentData.getParcelableArrayListExtra("LeadFormInterests"));
                List.add(AddNewItem);
                break;
            case MediaTypes.SOCIAL:
                AddNewItem.setTitle("Social");
                AddNewItem.setImage(R.drawable.social_icon);
                AddNewItem.setSocialMediaData(intentData.getParcelableArrayListExtra("SocialMediaData"));
                List.add(AddNewItem);
                break;
            case MediaTypes.HOURS:
                AddNewItem.setTitle("Hours");
                AddNewItem.setImage(R.drawable.hour_icon);
                AddNewItem.setHoursSwitch(intentData.getParcelableExtra("get_switches_data"));
                AddNewItem.setBusinessHoursData(intentData.getParcelableArrayListExtra("get_BusinessHours"));
                List.add(AddNewItem);
                break;
            case MediaTypes.SPECIAL_OFFERS:
                AddNewItem.setTitle("Special Offer");
                AddNewItem.setImage(R.drawable.special_offer_icon);
                AddNewItem.setSpecialOffer(intentData.getParcelableExtra("get_specialOffer_data"));
                List.add(AddNewItem);
                break;
            case MediaTypes.MAP:
                AddNewItem.setTitle("Map");
                AddNewItem.setImage(R.drawable.map_icon);
                AddNewItem.setMapModel(intentData.getParcelableExtra("map_data"));
                break;
        }
    }

    private void setAudioData(CampaignData AddNewItem, Intent intentData) {
        if (intentData.getStringExtra("result") != null) {
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
    }

    private void setVideoItems(CampaignData AddNewItem, Intent intent) {
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
                VideoFile = FileUtil.from(context, intent.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (VideoFile != null) {
                Bitmap OrgnalImage = ThumbnailUtils.createVideoThumbnail(VideoFile.getAbsoluteFile().toString(), MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
                Bitmap image = DataHelper.getInstance().putOverlay(OrgnalImage, context);
                AddNewItem.setBitmapImage(image);
                if (image != null) {
                    String myBase64Image = DataHelper.getInstance().encodeToBase64(image, Bitmap.CompressFormat.JPEG, 90);
                    JSONObject jsonObject3 = new JSONObject();
                    try {
                        jsonObject3.put("mediaFileName", String.valueOf(System.currentTimeMillis()) + ".png");
                        jsonObject3.put("media", myBase64Image);
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


    }

    private void setImage(CampaignData AddNewItem, Intent intentData) {
        if (intentData.getData() != null) {
            try {
                File actualImage = FileUtil.from(context, intentData.getData());
                Bitmap bitmap2 = Compressor.getDefault(context).compressToBitmap(actualImage);
                AddNewItem.setBitmapImage(bitmap2);
            } catch (IOException e) {
                e.printStackTrace();
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
        }

    }

    //update ListItems if user add new item in ListView
    public void updateListView3(Intent intentData, List<CampaignData> List) {
        CampaignData AddNewItem = new CampaignData();
        switch (intentData.getStringExtra("Title")) {
            case "Image":
                AddNewItem.setTitle("Image");
                AddNewItem.setImage(R.drawable.image);
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
            case MediaTypes.LEADFORM:
                AddNewItem.setTitle("Lead Form");
                AddNewItem.setImage(R.drawable.lead_form_icon);
                List.add(AddNewItem);
                break;
            case MediaTypes.SOCIAL:
                AddNewItem.setTitle("Social");
                AddNewItem.setImage(R.drawable.social_icon);
                List.add(AddNewItem);
                break;
            case MediaTypes.HOURS:
                AddNewItem.setTitle("Hours");
                AddNewItem.setImage(R.drawable.hour_icon);
                List.add(AddNewItem);
                break;

        }
    }

    public void imageFromCameraTest(Intent data, CampaignData ListItem) {
        if (data.getData() != null) {
            Bitmap bitmap = null;
            try {
                File actualImage = FileUtil.from(context, data.getData());
                bitmap = Compressor.getDefault(context).compressToBitmap(actualImage);
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

    public void setCTAElements(CampaignData ListItem, String result) {
        try {
            String type = new JSONObject(result).getString("type");
            switch (type) {
                case "TITLE":
                    ListItem.setTitle("Title");
                    ListItem.setImage(R.drawable.titleicon);
                    break;
                case "PARAGRAPH":
                    ListItem.setTitle("Paragraph");
                    ListItem.setImage(R.drawable.paragraph_icon);
                    break;
            }
            switch (new JSONObject(result).getJSONObject("callToAction").getString("type")) {
                case "YESNOMAYBE":
                    ListItem.setTitle("Response");
                    ListItem.setImage(R.drawable.response);
                    break;
                case "POLL":
                    ListItem.setTitle("Poll");
                    ListItem.setImage(R.drawable.poll);
                    break;
                case "FEEDBACK":
                    ListItem.setTitle("Feedback");
                    ListItem.setImage(R.drawable.poll);
                    break;
                case "CALLME":
                    ListItem.setTitle("Link/Call");
                    ListItem.setImage(R.drawable.linkcall);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getAudio(Intent intentData, CampaignData ListItem) {
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

    public void getVideo(Intent data, CampaignData ListItem) {
        if (data.getData() != null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("mediaFileName", "video.mp4");
                jsonObject.put("media", data.getData());
                jsonObject.put("type", "VIDEO");
                jsonObject.put("mediaStorageType", "AMAZONS_3");
                jsonObject.put("status", "ACTIVE");
                ListItem.setVideoExtraData(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getVideoImage(Intent data, CampaignData ListItem) {
        File VideoFile = null;
        try {
            VideoFile = FileUtil.from(context, data.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap OrgnalImage = ThumbnailUtils.createVideoThumbnail(VideoFile.getAbsoluteFile().toString(), MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
        Bitmap image = DataHelper.getInstance().putOverlay(OrgnalImage, context);
        ListItem.setBitmapImage(image);
        if (image != null) {
            String myBase64Image = DataHelper.getInstance().encodeToBase64(image, Bitmap.CompressFormat.JPEG, 70);
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

    public void imageFromAdobeSdk(Intent data, CampaignData ListItem) {
        if (data.getData() != null) {
            Bitmap bitmap = null;
            try {
                Uri editedImageUri = data.getParcelableExtra(AdobeImageIntent.EXTRA_OUTPUT_URI);
                File actualImage = FileUtil.from(context, editedImageUri);
                bitmap = Compressor.getDefault(context).compressToBitmap(actualImage);
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

    public void addLeadForm(Intent intentData, CampaignData ListItem) {
        ListItem.setTitle("Lead Form");
        ListItem.setLeadFormData(intentData.getParcelableArrayListExtra("LeadFormData"));
        ListItem.setLeadFormInterestsData(intentData.getParcelableArrayListExtra("LeadFormInterests"));
    }

    public void addSocial(Intent intentData, CampaignData ListItem) {
        ListItem.setTitle("Social");
        ListItem.setImage(R.drawable.social_icon);
        ListItem.setSocialMediaData(intentData.getParcelableArrayListExtra("SocialMediaData"));
    }

    public void addSpecialOffers(Intent intentData, CampaignData ListItem) {
        ListItem.setTitle("Special Offer");
        ListItem.setImage(R.drawable.special_offer_icon);
        ListItem.setSpecialOffer(intentData.getParcelableExtra("get_specialOffer_data"));
    }

    public void addMap(Intent intentData, CampaignData ListItem) {
        ListItem.setTitle("Map");
        ListItem.setImage(R.drawable.map_icon);
        ListItem.setMapModel(intentData.getParcelableExtra("map_data"));
    }

    public void addBusinessHours(Intent intentData, CampaignData ListItem) {
        ListItem.setTitle("Hours");
        ListItem.setImage(R.drawable.hours);
        ListItem.setBusinessHoursData(intentData.getParcelableArrayListExtra("get_BusinessHours"));
        ListItem.setHoursSwitch(intentData.getParcelableExtra("get_switches_data"));
    }

    public void ImageObject(JSONObject media) {
        try {
            Bitmap bitmap = null;
            Uri data = Uri.parse(media.getString("media"));
            try {
                File actualImage = FileUtil.from(context, data);
                bitmap = Compressor.getDefault(context).compressToBitmap(actualImage);
                media.put("media", DataHelper.getInstance().encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateVideoObject(JSONObject media) {
        try {
            Uri selectedVideoUri = Uri.parse(media.getString("media"));
            if (selectedVideoUri != null) {
                File VideoFile = null;
                try {
                    VideoFile = FileUtil.from(context, selectedVideoUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (VideoFile.exists() && VideoFile != null) {
                    try {
                        byte[] b = DataHelper.getInstance().loadFile(VideoFile);
                        media.put("media", Base64.encodeToString(b, Base64.DEFAULT));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateCallMe(JSONObject jsonObject) {
        JSONArray poll = new JSONArray();
        try {
            JSONObject callToAction = jsonObject.getJSONObject("callToAction");
            if (callToAction.getString("type").equalsIgnoreCase("CALLME")) {
                JSONArray properties = jsonObject.getJSONArray("properties");
                for (int i = 0; i < properties.length(); i++) {
                    if (properties.getJSONObject(i).getString("name").equalsIgnoreCase("buttonCorners")) {
                        properties.getJSONObject(i).put("value", properties.getJSONObject(i).getString("value") + "px");
                    }
                }
                callToAction.put("pollItems", poll);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void updateCallToAction(JSONObject jsonObject) {

        JSONArray poll = new JSONArray();
        try {
            JSONObject callToAction = jsonObject.getJSONObject("callToAction");
            if (callToAction.getString("type").equalsIgnoreCase("YESNOMAYBE")) {
                JSONArray properties = jsonObject.getJSONArray("properties");
                for (int i = 0; i < properties.length(); i++) {
                    if (properties.getJSONObject(i).getString("name").equalsIgnoreCase("buttonCorners")) {
                        properties.getJSONObject(i).put("value", properties.getJSONObject(i).getString("value") + "px");
                    }
                }
                callToAction.put("pollItems", poll);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updatePollObject(JSONObject jsonObject) {
        JSONArray poll = new JSONArray();
        try {
            JSONObject callToAction = jsonObject.getJSONObject("callToAction");
            if (callToAction.getString("type").equalsIgnoreCase("POLL")) {
                JSONArray pollItems = callToAction.getJSONArray("pollItems");
                for (int i = 0; i < pollItems.length(); i++) {
                    String item = pollItems.getJSONObject(i).getString("label");
                    if (StringUtils.isNotBlank(item)) {
                        poll.put(new JSONObject().put("label", item));
                    }
                }
                callToAction.put("pollItems", poll);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateFeedBackObject(JSONObject jsonObject) {
        try {
            JSONArray properties = jsonObject.getJSONArray("properties");

            int ElementsSize = properties.getJSONObject(3).getInt("value");

            if (ElementsSize <= 35) {
                properties.getJSONObject(3).put("value", "small");
            }
            if (ElementsSize > 35) {
                properties.getJSONObject(3).put("value", "medium");
            }
            if (ElementsSize > 75) {
                properties.getJSONObject(3).put("value", "large");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateAudioObject(JSONObject jsonObject) {
        File AudioFile;
        try {
            Uri data = Uri.parse(jsonObject.getString("media"));
            try {
                AudioFile = new File(DataHelper.getInstance().loadAudioFile(data, context));

            } catch (Exception e) {

                AudioFile = new File(jsonObject.getString("media"));

            }
            jsonObject.put("media", encodeAudio(AudioFile));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    private String encodeAudio(File audioFile) {
        String _audioBase64 = null;
        byte[] audioBytes;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(audioFile);
            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);
            audioBytes = baos.toByteArray();
            // Here goes the Base64 string
            _audioBase64 = Base64.encodeToString(audioBytes, Base64.DEFAULT);
        } catch (Exception e) {
            Log.e(TAG, " Unable to Handle audio file ");
        }
        return _audioBase64;
    }
}
