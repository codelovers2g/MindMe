package com.mindmesolo.mindme.CreateMobilePages.Adapters;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.afollestad.materialcamera.MaterialCamera;
import com.mindmesolo.mindme.AudioRecorder.PlayAndRecordAudio;
import com.mindmesolo.mindme.AudioRecorder.VideoPlayer;
import com.mindmesolo.mindme.CreateCampaign.CallToActionElements;
import com.mindmesolo.mindme.CreateCampaign.CreateParagraph;
import com.mindmesolo.mindme.CreateCampaign.CreateTitle;
import com.mindmesolo.mindme.CreateMobilePages.AddHours;
import com.mindmesolo.mindme.CreateMobilePages.AddLeadForm;
import com.mindmesolo.mindme.CreateMobilePages.AddMap;
import com.mindmesolo.mindme.CreateMobilePages.AddSocialMedia;
import com.mindmesolo.mindme.CreateMobilePages.AddSpecialOffer;
import com.mindmesolo.mindme.CreateMobilePages.models.BusinessHours;
import com.mindmesolo.mindme.CreateMobilePages.models.MapModel;
import com.mindmesolo.mindme.CreateMobilePages.models.SpecialOffer;
import com.mindmesolo.mindme.GettingStarted.SocialMediaModel;
import com.mindmesolo.mindme.Models.CommonModel;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.ViewCampaigns.CampaignData;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.ItemTouchHelperAdapter;
import com.mindmesolo.mindme.helper.OnStartDragListener;
import com.mindmesolo.mindme.helper.ScaledImageView;
import com.mindmesolo.mindme.helper.ToastShow;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.view.View.SCALE_X;
import static android.view.View.SCALE_Y;

/**
 * Created by enest_09 on 11/2/2016.
 */

public class WelComePageBuilderAdapter extends RecyclerView.Adapter<WelComePageBuilderAdapter.MyViewHolder> implements ItemTouchHelperAdapter {

    private static final String TAG = "CreateEmailAdapter";
    private final OnStartDragListener mDragStartListener;
    public int position = 0;
    public List<CampaignData> List;
    Dialog dialog;

    Context context;

    public WelComePageBuilderAdapter(Context context, List<CampaignData> List, OnStartDragListener mDragStartListener) {
        this.List = List;
        this.context = context;
        this.mDragStartListener = mDragStartListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        MyViewHolder itemViewHolder = new MyViewHolder(view);
        itemViewHolder.setIsRecyclable(false);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        CampaignData campaignData = this.List.get(position);

        holder.parentLayout.setTag(campaignData);

        String extraData = campaignData.getExtraData();

        Bitmap Image = campaignData.getBitmapImage();

        holder.MainListView.setVisibility(View.VISIBLE);

        holder.title.setText(campaignData.getTitle());

        holder.imageView.setImageResource(campaignData.getImage());

        holder.imageView2.setTag(campaignData);

        ArrayList<CommonModel> leadFormData = campaignData.getLeadFormData();
        ArrayList<SocialMediaModel> SocialMediaData = campaignData.getSocialMediaData();
        ArrayList<BusinessHours> BusinessHoursData = campaignData.getBusinessHoursData();
        SpecialOffer SpecialOffer = campaignData.getSpecialOffer();
        MapModel mapModel = campaignData.getMapModel();
        if (extraData != null || leadFormData != null ||
                SocialMediaData != null || BusinessHoursData != null ||
                SpecialOffer != null || mapModel != null) {
            holder.MainListView.setVisibility(View.GONE);
            handleViews(campaignData, holder);
        }

        if (Image != null) {
            holder.MainListView.setVisibility(View.GONE);
            holder.CampaignImage.setVisibility(View.VISIBLE);
            holder.CampaignImage.setImageBitmap(Image);
        }

        if (campaignData.isAudio) {
            holder.MainListView.setVisibility(View.GONE);
            holder.linearLayoutAudio.setVisibility(View.VISIBLE);
        }

        holder.imageView3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                    // Execute some code after 2 seconds have passed
                }
                return false;
            }
        });
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(List, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        //List.remove(position);
        //notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    private void handleViews(CampaignData campaignData, MyViewHolder holder) {
        switch (campaignData.getTitle()) {
            case "Image":
            case "Logo":
            case "Media":
                break;
            case "Video":
                break;
            case "Audio":
                break;
            case "Title":
                handleTitle(campaignData.getExtraData(), holder);
                break;
            case "Paragraph":
                handleParagraph(campaignData.getExtraData(), holder);
                break;
            case "Feedback":
                handleFeedBack(campaignData.getExtraData(), holder);
                break;
            case "Response":
                handleYesNoMayBe(campaignData.getExtraData(), holder);
                break;
            case "Poll":
                handlePoll(campaignData.getExtraData(), holder);
                break;
            case "Call to Action":
            case "Link/Call":
                handleLinkCall(campaignData.getExtraData(), holder);
                break;
            case "Lead Form":
                handleLeadForm(campaignData, holder);
                break;
            case "Social":
                handleSocialMedia(campaignData, holder);
                break;
            case "Hours":
                handleHours(campaignData, holder);
                break;
            case "Special Offer":
                handleSpecialOffer(campaignData, holder);
                break;
            case "Map":
                handleGoogleMaps(campaignData, holder);
                break;
        }
    }

    private void handleGoogleMaps(CampaignData campaignData, MyViewHolder holder) {
        holder.GoogleMapsLayout.setVisibility(View.VISIBLE);

        MapModel mapModel = campaignData.getMapModel();

        String type = mapModel.getType();

        Bitmap bitmap = null;

        if (mapModel.getMapImage() != null) {
            bitmap = DataHelper.getInstance().decodeBase64(mapModel.getMapImage());
        }
        switch (type) {
            case "Map":
                holder.img_view_googleMaps.setImageBitmap(bitmap);
                holder.tv_address.setVisibility(View.GONE);
                holder.tv_address.setText(mapModel.getAddress());
                break;
            case "Address":
                holder.img_view_googleMaps.setVisibility(View.GONE);
                holder.tv_address.setText(mapModel.getAddress());
                break;
            case "Map Address":
                holder.img_view_googleMaps.setImageBitmap(bitmap);
                holder.tv_address.setText(mapModel.getAddress());
                break;
        }
    }

    private void handleSpecialOffer(CampaignData campaignData, MyViewHolder holder) {
        SpecialOffer specialOffer = campaignData.getSpecialOffer();
        if (specialOffer != null) {
            holder.SpecialOfferLayout.setVisibility(View.VISIBLE);
            holder.Tv_special_offer_title.setText(specialOffer.getName());
            holder.Tv_special_offer_description.setText(specialOffer.getDescription());
            String offerAmount = specialOffer.getOfferAmount();
            String UnitFormat = specialOffer.getOfferUnit();
            String date = specialOffer.getExpireDate();
            String other = specialOffer.getOther();
            if (StringUtils.isNotBlank(offerAmount)) {
                holder.Tv_special_offer_saveUpTo.setVisibility(View.VISIBLE);
                if (UnitFormat.equalsIgnoreCase("%")) {
                    holder.Tv_special_offer_saveUpTo.setText("Save " + offerAmount + "" + UnitFormat);
                } else {
                    holder.Tv_special_offer_saveUpTo.setText("Save " + UnitFormat + "" + offerAmount);
                }
            }
            if (StringUtils.isNotBlank(other)) {
                holder.Tv_special_offer_saveUpTo.setVisibility(View.VISIBLE);
                holder.Tv_special_offer_saveUpTo.setText(other);
            }

            if (StringUtils.isNotBlank(date)) {
                holder.Tv_special_offer_expire.setVisibility(View.VISIBLE);
                holder.Tv_special_offer_expire.setText("Expires " + date);
            }
        }
    }

    private void handleHours(CampaignData campaignData, MyViewHolder holder) {
        holder.layout_Business_hours.setVisibility(View.VISIBLE);
        for (BusinessHours businessHours : campaignData.getBusinessHoursData()) {
            switch (businessHours.getDayOfWeek()) {
                case 0:
                    holder.tv_date_Monday.setText(DataHelper.getInstance().getTime(businessHours));
                    break;
                case 1:
                    holder.tv_date_Tuesday.setText(DataHelper.getInstance().getTime(businessHours));
                    break;
                case 2:
                    holder.tv_date_Wednesday.setText(DataHelper.getInstance().getTime(businessHours));
                    break;
                case 3:
                    holder.tv_date_Thursday.setText(DataHelper.getInstance().getTime(businessHours));
                    break;
                case 4:
                    holder.tv_date_Friday.setText(DataHelper.getInstance().getTime(businessHours));
                    break;
                case 5:
                    holder.tv_date_Saturday.setText(DataHelper.getInstance().getTime(businessHours));
                    break;
                case 6:
                    holder.tv_date_Sunday.setText(DataHelper.getInstance().getTime(businessHours));
                    break;
            }
        }
        if (campaignData.getHoursSwitch().isTimeZoneSwitch()) {
            holder.tv_display_time_zone.setVisibility(View.VISIBLE);
            holder.tv_display_time_zone.setText(campaignData.getHoursSwitch().getTimeZone());
        }
    }

    private void handleSocialMedia(CampaignData socialMedia, MyViewHolder holder) {
        ArrayList<SocialMediaModel> listItems = socialMedia.getSocialMediaData();
        if (listItems != null) {
            for (SocialMediaModel socialMediaModel : listItems) {
                holder.SocialMediaLayout.setVisibility(View.VISIBLE);
                switch (socialMediaModel.getSocialMediaName()) {
                    case "Linkedln":
                        holder.img_view_Linkedln.setVisibility(View.VISIBLE);
                        break;

                    case "Facebook":
                        holder.img_view_Facebook.setVisibility(View.VISIBLE);
                        break;

                    case "Twitter":
                        holder.img_view_Twitter.setVisibility(View.VISIBLE);
                        break;

                    case "Google":
                        holder.img_view_Google.setVisibility(View.VISIBLE);
                        break;

                    case "Youtube":
                        holder.img_view_Youtube.setVisibility(View.VISIBLE);
                        break;

                    case "Instagram":
                        holder.img_view_Instagram.setVisibility(View.VISIBLE);
                        break;

                    case "Pinterest":
                        holder.img_view_Pinterest.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }
    }

    private void handleLeadForm(CampaignData data, MyViewHolder holder) {
        holder.layout_lead_Form.setVisibility(View.VISIBLE);
        for (CommonModel commonModel : data.getLeadFormData()) {
            switch (commonModel.getTitle()) {
                case "First Name":
                    holder.R_layout_FirstName.setVisibility(View.VISIBLE);
                    if (commonModel.getRequired())
                        holder.Tv_Required_FirstName.setVisibility(View.VISIBLE);
                    break;

                case "Last Name":
                    holder.R_layout_LastName.setVisibility(View.VISIBLE);
                    if (commonModel.getRequired())
                        holder.Tv_Required_LastName.setVisibility(View.VISIBLE);
                    break;

                case "Company":
                    holder.R_layout_Company.setVisibility(View.VISIBLE);
                    if (commonModel.getRequired())
                        holder.Tv_Required_Company.setVisibility(View.VISIBLE);

                    break;

                case "Email Address":
                    holder.R_layout_EmailAddress.setVisibility(View.VISIBLE);
                    if (commonModel.getRequired())
                        holder.Tv_Required_EmailAddress.setVisibility(View.VISIBLE);

                    break;

                case "Mobile Phone":
                    holder.R_layout_MobilePhone.setVisibility(View.VISIBLE);
                    if (commonModel.getRequired())
                        holder.Tv_Required_MobilePhone.setVisibility(View.VISIBLE);

                    break;

                case "Home Phone":
                    holder.R_layout_HomePhone.setVisibility(View.VISIBLE);
                    if (commonModel.getRequired())
                        holder.Tv_Required_HomePhone.setVisibility(View.VISIBLE);

                    break;

                case "Work Phone":
                    holder.R_layout_WorkPhone.setVisibility(View.VISIBLE);
                    if (commonModel.getRequired())
                        holder.Tv_Required_WorkPhone.setVisibility(View.VISIBLE);

                    break;

                case "Website":
                    holder.R_layout_WebSite.setVisibility(View.VISIBLE);
                    if (commonModel.getRequired())
                        holder.Tv_Required_WebSite.setVisibility(View.VISIBLE);

                    break;

                case "Address":
                    holder.R_layout_Address.setVisibility(View.VISIBLE);
                    if (commonModel.getRequired())
                        holder.Tv_Required_Address.setVisibility(View.VISIBLE);

                    break;

                case "Zip Code Only":
                    holder.R_layout_ZipCode.setVisibility(View.VISIBLE);
                    if (commonModel.getRequired())
                        holder.Tv_Required_ZipCode.setVisibility(View.VISIBLE);
                    break;
            }
        }
        ArrayList<CommonModel> Interests = data.getLeadFormInterestsData();
        if (Interests != null && Interests.size() > 0) {
            holder.LayoutSelectedInterests.setVisibility(View.VISIBLE);
            holder.tvLeadFromText.setVisibility(View.VISIBLE);
            LeadFormAdapter commonListViewAdapter = new LeadFormAdapter(context, Interests);
            holder.LayoutSelectedInterests.setAdapter(commonListViewAdapter);
            DataHelper.getInstance().setListViewHeightBasedOnItems(holder.LayoutSelectedInterests);
        }
    }

    private void handleLinkCall(String data, MyViewHolder holder) {

        holder.linkOutputLayout.setVisibility(View.VISIBLE);
        try {

            JSONObject jsonObject = new JSONObject(data);

            JSONArray LinkCallProperties = jsonObject.getJSONObject("callToAction").getJSONObject("linkCall").getJSONArray("properties");

            String title = LinkCallProperties.getJSONObject(1).getString("value");

            if (title != null) {

                holder.button.setText(title);

            }

            JSONArray properties = jsonObject.getJSONArray("properties");

            switch (properties.getJSONObject(2).getString("value")) {
                case "center":
                    holder.linkOutputLayout.setGravity(Gravity.CENTER);
                    break;
                case "left":
                    holder.linkOutputLayout.setGravity(Gravity.LEFT);
                    break;
                case "right":
                    holder.linkOutputLayout.setGravity(Gravity.RIGHT);
                    break;
            }

            int buttonCorner = properties.getJSONObject(3).getInt("value");

            int buttonSize = properties.getJSONObject(4).getInt("value");

            GradientDrawable gd = new GradientDrawable();

            gd.setColor(Color.parseColor("#FFA500"));

            gd.setCornerRadius(buttonCorner);

            holder.button.setBackgroundDrawable(gd);

            if (buttonSize > 100) {
                LinearLayout.LayoutParams lyt = new LinearLayout.LayoutParams(buttonSize, buttonSize / 3);
                holder.button.setLayoutParams(lyt);
            } else {
                LinearLayout.LayoutParams lyt = new LinearLayout.LayoutParams(100, 100 / 3);
                holder.button.setLayoutParams(lyt);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleFeedBack(String data, MyViewHolder holder) {
        holder.feedbackOutputLayout.setVisibility(View.VISIBLE);
        try {
            JSONObject jsonObject = new JSONObject(data);
            String message = jsonObject.getString("message");
            if (message != null) {
                holder.feedbackText.setText(message);
            }
            JSONArray properties = jsonObject.getJSONArray("properties");
            switch (properties.getJSONObject(2).getString("value")) {
                case "center":
                    holder.feedbackOutputLayout.setGravity(Gravity.CENTER);
                    holder.feedbackText.setGravity(Gravity.CENTER);
                    break;
                case "left":
                    holder.feedbackOutputLayout.setGravity(Gravity.LEFT);
                    holder.feedbackText.setGravity(Gravity.LEFT);
                    break;
                case "right":
                    holder.feedbackOutputLayout.setGravity(Gravity.RIGHT);
                    holder.feedbackText.setGravity(Gravity.RIGHT);
                    break;
            }

            int ElementsSize = properties.getJSONObject(3).getInt("value");

            if (ElementsSize <= 35) {
                SCALE_X.set(holder.StartRating, (float) 0.3);
                SCALE_Y.set(holder.StartRating, (float) 0.3);
            }
            if (ElementsSize > 35 || ElementsSize == 0) {
                SCALE_X.set(holder.StartRating, (float) 0.7);
                SCALE_Y.set(holder.StartRating, (float) 0.7);
            }
            if (ElementsSize > 75) {
                SCALE_X.set(holder.StartRating, (float) 1);
                SCALE_Y.set(holder.StartRating, (float) 1);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void handlePoll(String data, MyViewHolder holder) {

        holder.linearLayout.setVisibility(View.VISIBLE);
        holder.optionOnelayout.setVisibility(View.GONE);
        holder.optionTwoLayout.setVisibility(View.GONE);
        holder.optionThreeLayout.setVisibility(View.GONE);
        try {

            JSONObject jsonObject = new JSONObject(data);

            String message = jsonObject.getString("message");

            if (message != null) {
                holder.textView1.setText(message);
            }
            JSONObject jsonObject1 = jsonObject.getJSONObject("callToAction");
            JSONArray jsonArray = jsonObject1.getJSONArray("pollItems");
            for (int i = 0; i < jsonArray.length(); i++) {
                String title = jsonArray.getJSONObject(i).getString("label");
                switch (i) {
                    case 0:
                        if (title.length() > 0) {
                            holder.optionOnelayout.setVisibility(View.VISIBLE);
                            holder.option1Text.setText(title);
                        }
                        break;
                    case 1:
                        if (title.length() > 0) {
                            holder.optionTwoLayout.setVisibility(View.VISIBLE);
                            holder.option2Text.setText(title);
                        }
                        break;
                    case 2:
                        if (title.length() > 0) {
                            holder.optionThreeLayout.setVisibility(View.VISIBLE);
                            holder.option3Text.setText(title);
                        }
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleYesNoMayBe(String data, MyViewHolder holder) {

        holder.yes_no_maybe_Layout.setVisibility(View.VISIBLE);

        holder.no.setVisibility(View.GONE);

        holder.maybe.setVisibility(View.GONE);

        try {

            JSONObject jsonObject = new JSONObject(data);

            String message = jsonObject.getString("message");

            if (message != null) {

                holder.yes_no_maybe_text.setText(message);
            }

            JSONObject callToAction = jsonObject.getJSONObject("callToAction");

            JSONObject yesNoMaybe = callToAction.getJSONObject("yesNoMaybe");

            JSONArray jsonArrayBtn = yesNoMaybe.getJSONArray("buttons");

            for (int i = 0; i < jsonArrayBtn.length(); i++) {
                if (jsonArrayBtn.getString(i).equalsIgnoreCase("no")) {
                    holder.no.setVisibility(View.VISIBLE);
                } else if (jsonArrayBtn.getString(i).equalsIgnoreCase("maybe")) {
                    holder.maybe.setVisibility(View.VISIBLE);
                }
            }

            JSONArray propertiesArray = jsonObject.getJSONArray("properties");

            switch (propertiesArray.getJSONObject(2).getString("value")) {
                case "center":
                    holder.yes_no_maybe_Layout.setGravity(Gravity.CENTER);
                    holder.yes_no_maybe_text.setGravity(Gravity.CENTER);
                    break;
                case "left":
                    holder.yes_no_maybe_Layout.setGravity(Gravity.LEFT);
                    holder.yes_no_maybe_text.setGravity(Gravity.LEFT);
                    break;
                case "right":
                    holder.yes_no_maybe_Layout.setGravity(Gravity.RIGHT);
                    holder.yes_no_maybe_text.setGravity(Gravity.RIGHT);
                    break;
            }

            int border = propertiesArray.getJSONObject(3).getInt("value");
            GradientDrawable gd = new GradientDrawable();
            GradientDrawable gd1 = new GradientDrawable();
            GradientDrawable gd2 = new GradientDrawable();
            gd.setColor(Color.parseColor("#57ac2d"));
            gd1.setColor(Color.parseColor("#B22222"));
            gd2.setColor(Color.parseColor("#FF6600"));
            gd.setCornerRadius(border);
            gd1.setCornerRadius(border);
            gd2.setCornerRadius(border);
            holder.yes.setBackgroundDrawable(gd);
            holder.no.setBackgroundDrawable(gd1);
            holder.maybe.setBackgroundDrawable(gd2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleParagraph(String data, MyViewHolder holder) {

        holder.linearLayoutparagraph.setVisibility(View.VISIBLE);

        try {

            JSONObject jsonObject = new JSONObject(data);

            JSONArray propertiesArray = jsonObject.getJSONArray("properties");

            int progress = Integer.parseInt(propertiesArray.getJSONObject(0).getString("value"));

            if (progress >= 12) {
                holder.textTitle.setTextSize(progress);
                holder.createParagraph1.setTextSize(progress);
                holder.createParagraph2.setTextSize(progress);
            } else {
                holder.textTitle.setTextSize(15);
                holder.createParagraph1.setTextSize(15);
                holder.createParagraph2.setTextSize(15);
            }

            int valFontWeight = propertiesArray.getJSONObject(1).getInt("value");

            if (valFontWeight >= 1 && valFontWeight <= 20) {
                holder.createParagraph1.setTypeface(null, Typeface.NORMAL);
                holder.createParagraph2.setTypeface(null, Typeface.NORMAL);
                holder.textTitle.setTypeface(null, Typeface.NORMAL);
            }
            if (valFontWeight >= 20) {
                holder.createParagraph1.setTypeface(null, Typeface.BOLD);
                holder.createParagraph2.setTypeface(null, Typeface.BOLD);
                holder.textTitle.setTypeface(null, Typeface.BOLD);
            }


            switch (propertiesArray.getJSONObject(2).getString("value")) {
                case "center":
                    holder.createParagraph1.setGravity(Gravity.CENTER);
                    holder.createParagraph2.setGravity(Gravity.CENTER);
                    holder.textTitle.setGravity(Gravity.CENTER);
                    break;
                case "left":
                    holder.createParagraph1.setGravity(Gravity.LEFT);
                    holder.createParagraph2.setGravity(Gravity.LEFT);
                    holder.textTitle.setGravity(Gravity.LEFT);
                    break;
                case "right":
                    holder.createParagraph1.setGravity(Gravity.RIGHT);
                    holder.createParagraph2.setGravity(Gravity.RIGHT);
                    holder.textTitle.setGravity(Gravity.RIGHT);
                    break;
            }

            int col = propertiesArray.getJSONObject(3).getInt("value");

            if (col == 1) {
                holder.textTitle.setVisibility(View.VISIBLE);
                holder.linearLayoutparagraph.setVisibility(View.GONE);
                holder.textTitle.setText(propertiesArray.getJSONObject(4).getString("value"));
            } else {
                holder.textTitle.setVisibility(View.GONE);
                holder.linearLayoutparagraph.setVisibility(View.VISIBLE);
                holder.createParagraph1.setText(propertiesArray.getJSONObject(4).getString("value"));
                holder.createParagraph2.setText(propertiesArray.getJSONObject(5).getString("value"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleTitle(String data, MyViewHolder holder) {

        try {

            JSONObject jsonObject = new JSONObject(data);

            holder.textTitle.setVisibility(View.VISIBLE);

            String titleText = jsonObject.getString("message");

            if (titleText != null) {
                holder.textTitle.setText(titleText);
            }

            JSONArray properties = jsonObject.getJSONArray("properties");

            int progress = Integer.parseInt(properties.getJSONObject(0).getString("value"));

            if (progress >= 12) {

                holder.textTitle.setTextSize(progress);

            } else {

                holder.textTitle.setTextSize(15);
            }


            int valFontWeight = properties.getJSONObject(1).getInt("value");

            if (valFontWeight >= 1 && valFontWeight <= 20) {

                holder.textTitle.setTypeface(null, Typeface.NORMAL);

            }
            if (valFontWeight >= 20) {

                holder.textTitle.setTypeface(null, Typeface.BOLD);
            }

            JSONObject jsonObject3 = properties.getJSONObject(2);

            String alignment = jsonObject3.getString("value");

            switch (alignment) {
                case "center":
                    holder.textTitle.setGravity(Gravity.CENTER);
                    break;
                case "left":
                    holder.textTitle.setGravity(Gravity.LEFT);
                    break;
                case "right":
                    holder.textTitle.setGravity(Gravity.RIGHT);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getPosition() {
        return position;
    }

    private void delete(int position) {
        if (List.get(position) != null) {
            List.remove(position);
            notifyDataSetChanged();
        }
        ;
    }

    private void copyItems(int position) {
        CampaignData newItem2 = new CampaignData();
        if (!List.get(position).getTitle().equalsIgnoreCase("Lead Form")) {
            newItem2.setTitle(List.get(position).getTitle());
            newItem2.setImage(List.get(position).getImage());
            newItem2.setTitle(List.get(position).getTitle());
            newItem2.setBusinessHoursData(List.get(position).getBusinessHoursData());
            newItem2.setSpecialOffer(List.get(position).getSpecialOffer());
            newItem2.setSocialMediaData(List.get(position).getSocialMediaData());
            newItem2.setExtraData(List.get(position).getExtraData());
            newItem2.setBitmapImage(List.get(position).getBitmapImage());
            newItem2.setAudio(List.get(position).isAudio());
            List.add(position + 1, newItem2);
            notifyItemInserted(position + 1);
        } else {
            ToastShow.setText(context, "Cannot copy this element", Toast.LENGTH_LONG);
        }
    }

    //items on on click Listener
    public void HandleResponse(String ClickedItemTitle, View v, int position, CampaignData item) {
        Intent intent;
        switch (ClickedItemTitle) {
            case "Image":
            case "Logo":
                showDialog(v, item);
                break;
            case "Video":
                showDialogVideo(v, item);
                break;
            case "Audio":
                intent = new Intent(v.getContext(), PlayAndRecordAudio.class);
                if (List.get(position).getExtraData() != null) {
                    intent.putExtra("ExtraData", List.get(position).getExtraData());
                }
                ((Activity) v.getContext()).startActivityForResult(intent, 5);
                break;
            case "Title":
                intent = new Intent(v.getContext(), CreateTitle.class);
                if (List.get(position).getExtraData() != null) {
                    intent.putExtra("ExtraData", List.get(position).getExtraData());
                }
                ((Activity) v.getContext()).startActivityForResult(intent, 2);
                break;
            case "Paragraph":
                intent = new Intent(v.getContext(), CreateParagraph.class);
                if (List.get(position).getExtraData() != null) {
                    intent.putExtra("ExtraData", List.get(position).getExtraData());
                }
                ((Activity) v.getContext()).startActivityForResult(intent, 2);
                break;
            case "Feedback":
            case "Response":
            case "Poll":
            case "Link/Call":
            case "Call to Action":
                intent = new Intent(v.getContext(), CallToActionElements.class);
                intent.putExtra("MediaType", List.get(position).getTitle());
                if (List.get(position).getExtraData() != null) {
                    intent.putExtra("ExtraData", List.get(position).getExtraData());
                }
                ((Activity) v.getContext()).startActivityForResult(intent, 2);
                break;
            case "Media":
                showDialog(v, item);
                break;
            case "Hours":
                intent = new Intent(v.getContext(), AddHours.class);
                intent.putExtra("MediaType", List.get(position).getTitle());
                intent.putExtra("HourSwitch", List.get(position).getHoursSwitch());
                if (List.get(position).getBusinessHoursData() != null) {
                    intent.putParcelableArrayListExtra("ListItems", List.get(position).getBusinessHoursData());
                }
                ((Activity) v.getContext()).startActivityForResult(intent, 7);
                break;
            case "Map":
                intent = new Intent(v.getContext(), AddMap.class);
                intent.putExtra("MediaType", List.get(position).getTitle());
                if (List.get(position).getMapModel() != null) {
                    intent.putExtra("map_data", List.get(position).getMapModel());
                }
                ((Activity) v.getContext()).startActivityForResult(intent, 8);
                break;
            case "Social":
                intent = new Intent(v.getContext(), AddSocialMedia.class);
                intent.putExtra("MediaType", List.get(position).getTitle());
                if (List.get(position).getSocialMediaData() != null) {
                    intent.putParcelableArrayListExtra("ExtraData", List.get(position).getSocialMediaData());
                }
                ((Activity) v.getContext()).startActivityForResult(intent, 9);
                break;
            case "Lead Form":
                intent = new Intent(v.getContext(), AddLeadForm.class);
                intent.putExtra("MediaType", List.get(position).getTitle());
                if (List.get(position).getLeadFormData() != null) {
                    intent.putParcelableArrayListExtra("LeadFormExtraData", List.get(position).getLeadFormData());
                    intent.putParcelableArrayListExtra("LeadFormExtraInterestsData", List.get(position).getLeadFormInterestsData());
                }
                ((Activity) v.getContext()).startActivityForResult(intent, 10);
                break;
            case "Special Offer":
                intent = new Intent(v.getContext(), AddSpecialOffer.class);
                intent.putExtra("MediaType", List.get(position).getTitle());
                if (List.get(position).getSpecialOffer() != null) {
                    intent.putExtra("ExtraData", List.get(position).getSpecialOffer());
                }
                ((Activity) v.getContext()).startActivityForResult(intent, 11);
                break;
        }
    }

    private void showDialogVideo(final View v, final CampaignData item) {
        dialog = new Dialog(v.getContext());
        dialog.getWindow().setWindowAnimations(R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.video_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM | Gravity.CENTER;
        wmlp.x = 0;   //x position
        wmlp.y = 50;   //y position

        TextView tv_preview_video = (TextView) dialog.findViewById(R.id.tv_preview_video);
        tv_preview_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String extraData = item.getExtraData();
                    if (extraData != null) {
                        JSONObject jsonObject = new JSONObject(extraData);
                        Uri data = Uri.parse(jsonObject.getString("media"));
                        Intent intent = new Intent((Activity) v.getContext(), VideoPlayer.class);
                        intent.setData(data);
                        view.getContext().startActivity(intent);
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
                dialog.dismiss();
            }
        });

        TextView tv_take_video = (TextView) dialog.findViewById(R.id.tv_take_video);
        tv_take_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File saveDir = null;
                if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    saveDir = new File(Environment.getExternalStorageDirectory(), "MaterialCamera/video");
                    saveDir.mkdirs();
                }

                new MaterialCamera((Activity) v.getContext())              // Constructor takes an Activity
                        .allowRetry(true)                                  // Whether or not 'Retry' is visible during playback
                        .autoSubmit(false)                                 // Whether or not user is allowed to playback videos after recording. This can affect other things, discussed in the next section.
                        .saveDir(saveDir)                                  // The folder recorded videos are saved to
                        .primaryColorAttr(R.attr.colorPrimary)             // The theme color used for the camera, defaults to colorPrimary of Activity in the constructor
                        .showPortraitWarning(true)                         // Whether or not a warning is displayed if the user presses record in portrait orientation
                        .defaultToFrontFacing(false)                       // Whether or not the camera will initially show the front facing camera
                        .retryExits(false)                                 // If true, the 'Retry' button in the playback screen will exit the camera instead of going back to the recorder
                        .restartTimerOnRetry(false)                        // If true, the countdown timer is reset to 0 when the user taps 'Retry' in playback
                        .continueTimerInPlayback(false)                    // If true, the countdown timer will continue to go down during playback, rather than pausing.
                        .videoEncodingBitRate(1024000)                     // Sets a custom bit rate for video recording.
                        .audioEncodingBitRate(50000)                       // Sets a custom bit rate for audio recording.
                        .videoFrameRate(24)                                // Sets a custom frame rate (FPS) for video recording.
                        .qualityProfile(MaterialCamera.QUALITY_HIGH)       // Sets a quality profile, manually setting bit rates or frame rates with other settings will overwrite individual quality profile settings
                        .videoPreferredHeight(720)                         // Sets a preferred height for the recorded video output.
                        .videoPreferredAspect(4f / 3f)                     // Sets a preferred aspect ratio for the recorded video output.
                        .maxAllowedFileSize(1024 * 1024 * 5)               // Sets a max file size of 5MB, recording will stop if file reaches this limit. Keep in mind, the FAT file system has a file size limit of 4GB.
                        .iconRecord(R.drawable.mcam_action_capture)        // Sets a custom icon for the button used to start recording
                        .iconStop(R.drawable.mcam_action_stop)             // Sets a custom icon for the button used to stop recording
                        .iconFrontCamera(R.drawable.mcam_camera_front)     // Sets a custom icon for the button used to switch to the front camera
                        .iconRearCamera(R.drawable.mcam_camera_rear)       // Sets a custom icon for the button used to switch to the rear camera
                        .iconPlay(R.drawable.evp_action_play)              // Sets a custom icon used to start playback
                        .iconPause(R.drawable.evp_action_pause)            // Sets a custom icon used to pause playback
                        .iconRestart(R.drawable.evp_action_restart)        // Sets a custom icon used to restart playback
                        .labelRetry(R.string.mcam_retry)                   // Sets a custom button label for the button used to retry recording, when available
                        .labelConfirm(R.string.mcam_use_video)             // Sets a custom button label for the button used to confirm/submit a recording
                        .autoRecordWithDelaySec(5)                         // The video camera will start recording automatically after a 5 second countdown. This disables switching between the front and back camera initially.
                        .autoRecordWithDelayMs(5000)                       // Same as the above, expressed with milliseconds instead of seconds.
                        .audioDisabled(false)                              // Set to true to record video without any audio.
                        .countdownMinutes(2.0f)                            // Set to record video time limit.
                        .start(4);                                 // Starts the camera activity, the result will be sent back to the current Activity
                dialog.dismiss();
            }
        });

        TextView tv_saved_video = (TextView) dialog.findViewById(R.id.tv_saved_video);
        tv_saved_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                ((Activity) v.getContext()).startActivityForResult(Intent.createChooser(intent, "Select Video"), 4);
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

    private void showDialog(final View v, final CampaignData data) {
        dialog = new Dialog(v.getContext());
        dialog.getWindow().setWindowAnimations(R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_profile_new);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM | Gravity.CENTER;
        wmlp.x = 0;   //x position
        wmlp.y = 50;   //y position

        View edit_view = (View) dialog.findViewById(R.id.edit_view);

        TextView tv_edit_image = (TextView) dialog.findViewById(R.id.tv_edit_image);
        if (data.getExtraData() != null) {
            edit_view.setVisibility(View.VISIBLE);
            tv_edit_image.setVisibility(View.VISIBLE);
        } else {
            edit_view.setVisibility(View.GONE);
            tv_edit_image.setVisibility(View.GONE);
        }

        tv_edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jsonData = data.getExtraData();
                if (jsonData != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonData);
                        Uri selectedImageUri = Uri.parse(jsonObject.getString("media"));
                        Intent imageEditorIntent = new AdobeImageIntent.Builder((Activity) v.getContext())
                                .setData(selectedImageUri)
                                .build();
                        ((Activity) v.getContext()).startActivityForResult(imageEditorIntent, 6);
                        dialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        TextView camera = (TextView) dialog.findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File saveDir = null;
                if (ContextCompat.checkSelfPermission((Activity) v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    saveDir = new File(Environment.getExternalStorageDirectory(), "MaterialCamera");
                    saveDir.mkdirs();
                }
                MaterialCamera materialCamera = new MaterialCamera((Activity) v.getContext())
                        .saveDir(saveDir)
                        .showPortraitWarning(true)
                        .allowRetry(true)
                        .defaultToFrontFacing(false);
                materialCamera.stillShot();
                materialCamera.start(3);
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
                ((Activity) v.getContext()).startActivityForResult(Intent.createChooser(intent, "Select Picture"), 3);
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

    public class MyViewHolder extends RecyclerView.ViewHolder {

        //basic main layouts ---
        public TextView title;

        public ImageView imageView, imageView2, imageView3;

        RelativeLayout MainListView;

        //All layouts for title-----
        TextView textTitle;
        RelativeLayout parentLayout;

        //All Layouts for paragraph
        TextView createParagraph1, createParagraph2;

        LinearLayout linearLayoutparagraph;

        //campaignImage
        ScaledImageView CampaignImage;

        //layouts for audio
        LinearLayout linearLayoutAudio;

        //Layouts for yes no maybe
        Button yes, no, maybe;
        LinearLayout yes_no_maybe_Layout;
        TextView yes_no_maybe_text;

        // Layouts for poll items
        TextView textView1, option1Text, option2Text, option3Text;
        LinearLayout linearLayout;
        LinearLayout optionOnelayout, optionTwoLayout, optionThreeLayout;

        //feed back layouts
        LinearLayout feedbackOutputLayout;
        TextView feedbackText;
        RatingBar StartRating;

        //call to action layout
        Button button;
        LinearLayout linkOutputLayout;

        //All Special Offer Data

        LinearLayout SpecialOfferLayout;
        TextView Tv_special_offer_title, Tv_special_offer_description, Tv_special_offer_saveUpTo, Tv_special_offer_expire;


        //All BusinessHours Data
        LinearLayout layout_Business_hours;
        TextView tv_date_Sunday, tv_date_Monday, tv_date_Tuesday, tv_date_Wednesday, tv_date_Thursday, tv_date_Friday, tv_date_Saturday;
        TextView tv_display_time_zone;

        //All Social Media Data
        LinearLayout SocialMediaLayout;
        ImageView img_view_Linkedln, img_view_Facebook, img_view_Twitter;
        ImageView img_view_Google, img_view_Youtube, img_view_Instagram, img_view_Pinterest;


        //All google maps data

        LinearLayout GoogleMapsLayout;
        ScaledImageView img_view_googleMaps;
        TextView tv_address;


        //All Lead Form Data
        LinearLayout layout_lead_Form;

        ListView LayoutSelectedInterests;

        TextView tvLeadFromText;

        RelativeLayout R_layout_FirstName, R_layout_LastName, R_layout_Company, R_layout_EmailAddress, R_layout_MobilePhone;

        RelativeLayout R_layout_HomePhone, R_layout_WorkPhone, R_layout_WebSite, R_layout_Address, R_layout_ZipCode;

        TextView Tv_Required_FirstName, Tv_Required_LastName, Tv_Required_Company, Tv_Required_EmailAddress, Tv_Required_MobilePhone;

        TextView Tv_Required_HomePhone, Tv_Required_WorkPhone, Tv_Required_WebSite, Tv_Required_Address, Tv_Required_ZipCode;

        public MyViewHolder(final View view) {
            super(view);
            //basic main layouts ---
            parentLayout = (RelativeLayout) view.findViewById(R.id.parentLayout);

            MainListView = (RelativeLayout) view.findViewById(R.id.MainListView);

            title = (TextView) view.findViewById(R.id.MainTitle);

            imageView2 = (ImageView) view.findViewById(R.id.imageViewPopup);

            imageView3 = (ImageView) view.findViewById(R.id.movearrows);

            imageView = (ImageView) view.findViewById(R.id.lytPatternColorDraw);

            //Title views
            textTitle = (TextView) view.findViewById(R.id.textTitle);

            //campaign image
            CampaignImage = (ScaledImageView) view.findViewById(R.id.CampaignImage);

            //layouts for audio
            linearLayoutAudio = (LinearLayout) view.findViewById(R.id.linearLayoutAudio);

            //All Layouts for paragraph
            createParagraph1 = (TextView) view.findViewById(R.id.createParagraph1);

            createParagraph2 = (TextView) view.findViewById(R.id.createParagraph2);

            linearLayoutparagraph = (LinearLayout) view.findViewById(R.id.linearLayoutparagraph);

            // All layouts for yes no maybe
            yes_no_maybe_text = (TextView) view.findViewById(R.id.yes_no_maybe_text);

            yes = (Button) view.findViewById(R.id.yes);

            no = (Button) view.findViewById(R.id.no);

            maybe = (Button) view.findViewById(R.id.maybe);

            yes_no_maybe_Layout = (LinearLayout) view.findViewById(R.id.yes_no_maybe_Layout);

            //All poll  views
            textView1 = (TextView) view.findViewById(R.id.pollText);
            option1Text = (TextView) view.findViewById(R.id.option1Text);
            option2Text = (TextView) view.findViewById(R.id.option2Text);
            option3Text = (TextView) view.findViewById(R.id.option3Text);

            linearLayout = (LinearLayout) view.findViewById(R.id.pollOutputLayout);

            optionOnelayout = (LinearLayout) view.findViewById(R.id.optionOnelayout);

            optionTwoLayout = (LinearLayout) view.findViewById(R.id.optionTwoLayout);

            optionThreeLayout = (LinearLayout) view.findViewById(R.id.optionThreeLayout);

            //All feedback layouts
            feedbackOutputLayout = (LinearLayout) view.findViewById(R.id.feedbackOutputLayout);

            feedbackText = (TextView) view.findViewById(R.id.feedbackText);

            StartRating = (RatingBar) view.findViewById(R.id.StartRating);

            //Lead capture form data
            layout_lead_Form = (LinearLayout) view.findViewById(R.id.layout_lead_Form);

            // All  link to call buttons
            button = (Button) view.findViewById(R.id.button);
            linkOutputLayout = (LinearLayout) view.findViewById(R.id.linkOutputLayout);

            //Social Media Views
            SocialMediaLayout = (LinearLayout) view.findViewById(R.id.SocialMediaLayout);
            img_view_Linkedln = (ImageView) view.findViewById(R.id.img_view_Linkedln);
            img_view_Facebook = (ImageView) view.findViewById(R.id.img_view_Facebook);
            img_view_Twitter = (ImageView) view.findViewById(R.id.img_view_Twitter);
            img_view_Google = (ImageView) view.findViewById(R.id.img_view_Google);
            img_view_Youtube = (ImageView) view.findViewById(R.id.img_view_Youtube);
            img_view_Instagram = (ImageView) view.findViewById(R.id.img_view_Instagram);
            img_view_Pinterest = (ImageView) view.findViewById(R.id.img_view_Pinterest);


            //All special offer data
            SpecialOfferLayout = (LinearLayout) view.findViewById(R.id.SpecialOfferLayout);
            Tv_special_offer_title = (TextView) view.findViewById(R.id.Tv_special_offer_title);
            Tv_special_offer_description = (TextView) view.findViewById(R.id.Tv_special_offer_description);
            Tv_special_offer_saveUpTo = (TextView) view.findViewById(R.id.Tv_special_offer_saveUpTo);
            Tv_special_offer_expire = (TextView) view.findViewById(R.id.Tv_special_offer_expire);


            GoogleMapsLayout = (LinearLayout) view.findViewById(R.id.GoogleMapsLayout);
            img_view_googleMaps = (ScaledImageView) view.findViewById(R.id.img_view_googleMaps);
            tv_address = (TextView) view.findViewById(R.id.tv_address);


            //All business hours data
            layout_Business_hours = (LinearLayout) view.findViewById(R.id.layout_Business_hours);
            tv_display_time_zone = (TextView) view.findViewById(R.id.tv_display_time_zone);
            tv_date_Sunday = (TextView) view.findViewById(R.id.tv_date_Sunday);
            tv_date_Monday = (TextView) view.findViewById(R.id.tv_date_Monday);
            tv_date_Tuesday = (TextView) view.findViewById(R.id.tv_date_Tuesday);
            tv_date_Wednesday = (TextView) view.findViewById(R.id.tv_date_Wednesday);
            tv_date_Thursday = (TextView) view.findViewById(R.id.tv_date_Thursday);
            tv_date_Friday = (TextView) view.findViewById(R.id.tv_date_Friday);
            tv_date_Saturday = (TextView) view.findViewById(R.id.tv_date_Saturday);

            //Lead Form Data Views
            R_layout_FirstName = (RelativeLayout) view.findViewById(R.id.R_layout_FirstName);

            R_layout_LastName = (RelativeLayout) view.findViewById(R.id.R_layout_LastName);

            R_layout_Company = (RelativeLayout) view.findViewById(R.id.R_layout_Company);

            R_layout_EmailAddress = (RelativeLayout) view.findViewById(R.id.R_layout_EmailAddress);

            R_layout_MobilePhone = (RelativeLayout) view.findViewById(R.id.R_layout_MobilePhone);

            R_layout_HomePhone = (RelativeLayout) view.findViewById(R.id.R_layout_HomePhone);

            R_layout_WorkPhone = (RelativeLayout) view.findViewById(R.id.R_layout_WorkPhone);

            R_layout_WebSite = (RelativeLayout) view.findViewById(R.id.R_layout_WebSite);

            R_layout_Address = (RelativeLayout) view.findViewById(R.id.R_layout_Address);

            R_layout_ZipCode = (RelativeLayout) view.findViewById(R.id.R_layout_ZipCode);

            Tv_Required_FirstName = (TextView) view.findViewById(R.id.Tv_Required_FirstName);

            Tv_Required_LastName = (TextView) view.findViewById(R.id.Tv_Required_LastName);

            Tv_Required_Company = (TextView) view.findViewById(R.id.Tv_Required_Company);

            Tv_Required_EmailAddress = (TextView) view.findViewById(R.id.Tv_Required_EmailAddress);

            Tv_Required_MobilePhone = (TextView) view.findViewById(R.id.Tv_Required_MobilePhone);

            Tv_Required_HomePhone = (TextView) view.findViewById(R.id.Tv_Required_HomePhone);

            Tv_Required_WorkPhone = (TextView) view.findViewById(R.id.Tv_Required_WorkPhone);

            Tv_Required_WebSite = (TextView) view.findViewById(R.id.Tv_Required_WebSite);

            Tv_Required_Address = (TextView) view.findViewById(R.id.Tv_Required_Address);

            Tv_Required_ZipCode = (TextView) view.findViewById(R.id.Tv_Required_ZipCode);

            LayoutSelectedInterests = (ListView) view.findViewById(R.id.LayoutSelectedInterests);

            tvLeadFromText = (TextView) view.findViewById(R.id.tvLeadFromText);

            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    {
                        PopupMenu popup = new PopupMenu(v.getContext(), v);

                        popup.getMenuInflater().inflate(R.menu.action, popup.getMenu());

                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                int id = item.getItemId();
                                if (id == R.id.actionCopy) {
                                    copyItems(getAdapterPosition());
                                }
                                if (id == R.id.actionDelete) {
                                    delete(getAdapterPosition());
                                }
                                return true;
                            }
                        });
                        popup.show();
                    }
                }
            });

            CampaignImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position = getAdapterPosition();
                    HandleResponse(title.getText().toString(), view, getAdapterPosition(), List.get(position));
                }
            });

            parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view1) {
                    position = getAdapterPosition();
                    HandleResponse(title.getText().toString(), view1, getAdapterPosition(), List.get(position));
                }
            });
        }
    }
}
