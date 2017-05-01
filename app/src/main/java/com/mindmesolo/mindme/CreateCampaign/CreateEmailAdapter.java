package com.mindmesolo.mindme.CreateCampaign;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
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
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.mindmesolo.mindme.AudioRecorder.MediaRecordingNew;
import com.mindmesolo.mindme.AudioRecorder.PlayAndRecordAudio;
import com.mindmesolo.mindme.AudioRecorder.VideoPlayer;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.ViewCampaigns.CampaignData;
import com.mindmesolo.mindme.helper.ItemTouchHelperAdapter;
import com.mindmesolo.mindme.helper.OnStartDragListener;
import com.mindmesolo.mindme.helper.ScaledImageView;
import com.mindmesolo.mindme.helper.ToastShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import static android.view.View.SCALE_X;
import static android.view.View.SCALE_Y;

/**
 * Created by enest_09 on 11/10/2016.
 */

public class CreateEmailAdapter extends RecyclerView.Adapter<CreateEmailAdapter.MyViewHolder> implements ItemTouchHelperAdapter {

    private static final String TAG = "CreateEmailAdapter";

    private final OnStartDragListener mDragStartListener;

    public int position = 0;

    public ArrayList<CampaignData> List;

    Dialog dialog;

    String VideoFileMedia = null;

    public CreateEmailAdapter(ArrayList<CampaignData> List, OnStartDragListener mDragStartListener) {
        this.List = List;
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

        String MediaType = campaignData.getTitle();

        holder.MainListView.setVisibility(View.VISIBLE);

        holder.title.setText(campaignData.getTitle());

        holder.imageView.setImageResource(campaignData.getImage());

        holder.imageView2.setTag(campaignData);

        if (extraData != null) {
            holder.MainListView.setVisibility(View.GONE);
            handleViews(MediaType, extraData, holder);
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

    // handle All View Genrated By RecycleView Adapter
    private void handleViews(String MediaType, String data, MyViewHolder holder) {
        switch (MediaType) {
            case "Title":
                handleTitle(data, holder);
                break;
            case "Paragraph":
                handleParagraph(data, holder);
                break;
            case "Feedback":
                handleFeedBack(data, holder);
                break;
            case "Response":
                handleYesNoMayBe(data, holder);
                break;
            case "Poll":
                handlePoll(data, holder);
                break;
            case "Link/Call":
                handleLinkCall(data, holder);
                break;
            case "Call to Action":
                break;
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

//          int buttonCorner = properties.getJSONObject(3).getInt("value");
//
//          int buttonSize = properties.getJSONObject(4).getInt("value");

            GradientDrawable gd = new GradientDrawable();

            gd.setColor(Color.parseColor("#FFA500"));

            gd.setCornerRadius(8);

            holder.button.setBackgroundDrawable(gd);

//            if(buttonSize>100){
//                LinearLayout.LayoutParams lyt = new LinearLayout.LayoutParams(buttonSize, buttonSize / 3);
//                holder.ButtonContactType.setLayoutParams(lyt);
//            }else {
//                LinearLayout.LayoutParams lyt = new LinearLayout.LayoutParams(100, 100 / 3);
//                holder.ButtonContactType.setLayoutParams(lyt);
//            }

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
        holder.optionFourLayout.setVisibility(View.GONE);
        holder.optionFiveLayout.setVisibility(View.GONE);
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

                    case 3:
                        if (title.length() > 0) {
                            holder.optionFourLayout.setVisibility(View.VISIBLE);
                            holder.option4Text.setText(title);
                        }
                        break;
                    case 4:
                        if (title.length() > 0) {
                            holder.optionFiveLayout.setVisibility(View.VISIBLE);
                            holder.option5Text.setText(title);
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
            gd2.setColor(Color.parseColor("#FFA500"));
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


//            int progress = Integer.parseInt(propertiesArray.getJSONObject(0).getString("value"));
//
//            if (progress >= 12) {
//                holder.textTitle.setTextSize(15);
//                holder.createParagraph1.setTextSize(15);
//                holder.createParagraph2.setTextSize(15);
//            } else {
//                holder.textTitle.setTextSize(15);
//                holder.createParagraph1.setTextSize(15);
//                holder.createParagraph2.setTextSize(15);
//            }

            //          int valFontWeight = propertiesArray.getJSONObject(1).getInt("value");

//            if (valFontWeight >= 1 && valFontWeight <= 20) {
//                holder.createParagraph1.setTypeface(null, Typeface.NORMAL);
//                holder.createParagraph2.setTypeface(null, Typeface.NORMAL);
//                holder.textTitle.setTypeface(null, Typeface.NORMAL);
//            }
//            if (valFontWeight >= 20) {
//                holder.createParagraph1.setTypeface(null, Typeface.BOLD);
//                holder.createParagraph2.setTypeface(null, Typeface.BOLD);
//                holder.textTitle.setTypeface(null, Typeface.BOLD);
//            }

            // un-comment above lines and remove below code
            holder.createParagraph1.setTypeface(null, Typeface.NORMAL);
            holder.createParagraph2.setTypeface(null, Typeface.NORMAL);
            holder.textTitle.setTypeface(null, Typeface.NORMAL);
            holder.textTitle.setTextSize(17);
            holder.createParagraph1.setTextSize(17);
            holder.createParagraph2.setTextSize(17);

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

//            int progress = Integer.parseInt(properties.getJSONObject(0).getString("value"));
//
//            if (progress >= 12) {
//
//                holder.textTitle.setTextSize(15);
//
//            } else {
//
//                holder.textTitle.setTextSize(15);
//            }

//            int valFontWeight = properties.getJSONObject(1).getInt("value");

//            if (valFontWeight >= 1 && valFontWeight <= 20) {
//
//                holder.textTitle.setTypeface(null, Typeface.NORMAL);
//
//            }
//            if (valFontWeight >= 20) {
//
//                holder.textTitle.setTypeface(null, Typeface.BOLD);
//            }

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
        newItem2.setImage(List.get(position).getImage());
        newItem2.setTitle(List.get(position).getTitle());
        newItem2.setExtraData(List.get(position).getExtraData());
        newItem2.setBitmapImage(List.get(position).getBitmapImage());
        newItem2.setAudio(List.get(position).isAudio());
        List.add(position + 1, newItem2);
        notifyItemInserted(position + 1);
    }

    //items on on click Listener
    public void HandleResponse(String ClickedItemTitle, View v, int position, CampaignData item) {
        Intent intent;
        switch (ClickedItemTitle) {
            case "Image":
                //select image by camera or direct pickup from gallery
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

        View view_preview_video = (View) dialog.findViewById(R.id.view_preview_video);

        tv_preview_video.setVisibility(View.GONE);

        view_preview_video.setVisibility(View.GONE);

        try {
            String extraData = item.getVideoExtraData();
            if (extraData != null) {
                JSONObject jsonObject = new JSONObject(extraData);
                VideoFileMedia = jsonObject.getString("media");
                if (VideoFileMedia != null) {
                    tv_preview_video.setVisibility(View.VISIBLE);
                    view_preview_video.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        tv_preview_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((Activity) v.getContext(), VideoPlayer.class);
                intent.putExtra("MediaLink", VideoFileMedia);
                ((Activity) v.getContext()).startActivity(intent);
                dialog.dismiss();
            }
        });

        TextView tv_take_video = (TextView) dialog.findViewById(R.id.tv_take_video);
        tv_take_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MediaRecordingNew.class);
                intent.putExtra("CAMERA_REQUEST", "VIDEO");
                ((Activity) v.getContext()).startActivityForResult(intent, 4);
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
                Intent intent = new Intent(view.getContext(), MediaRecordingNew.class);
                intent.putExtra("CAMERA_REQUEST", "IMAGE");
                ((Activity) v.getContext()).startActivityForResult(intent, 3);
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

    public boolean checkCta() {

        String[] CTA = new String[]{"Response", "Poll", "Feedback", "Link/Call", "Call to Action"};

        ArrayList<String> CtaData = new ArrayList<>();

        boolean ctaElement = false;
        for (CampaignData campaignData : List) {
            CtaData.add(campaignData.getTitle());

        }
        for (String data : CTA) {
            if (CtaData.contains(data)) {
                ctaElement = true;
            }
        }
        return ctaElement;
    }

    public boolean checkCtaForCopy(String Title) {
        ArrayList<String> CtaData = new ArrayList<>();
        CtaData.add("Response");
        CtaData.add("Poll");
        CtaData.add("Feedback");
        CtaData.add("Link/Call");
        CtaData.add("Call to Action");
        boolean ctaElement = false;
        if (CtaData.contains(Title)) {
            ctaElement = true;
        }
        return ctaElement;
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
        TextView textView1, option1Text, option2Text, option3Text, option4Text, option5Text;
        LinearLayout linearLayout;
        LinearLayout optionOnelayout, optionTwoLayout, optionThreeLayout, optionFourLayout, optionFiveLayout;

        //feed back layouts
        LinearLayout feedbackOutputLayout;
        TextView feedbackText;
        RatingBar StartRating;

        //call to action layout
        Button button;
        LinearLayout linkOutputLayout;

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
            option4Text = (TextView) view.findViewById(R.id.option4Text);
            option5Text = (TextView) view.findViewById(R.id.option5Text);

            linearLayout = (LinearLayout) view.findViewById(R.id.pollOutputLayout);

            optionOnelayout = (LinearLayout) view.findViewById(R.id.optionOnelayout);

            optionTwoLayout = (LinearLayout) view.findViewById(R.id.optionTwoLayout);

            optionThreeLayout = (LinearLayout) view.findViewById(R.id.optionThreeLayout);

            optionFourLayout = (LinearLayout) view.findViewById(R.id.optionFourLayout);

            optionFiveLayout = (LinearLayout) view.findViewById(R.id.optionFiveLayout);

            //All feedback layouts
            feedbackOutputLayout = (LinearLayout) view.findViewById(R.id.feedbackOutputLayout);

            feedbackText = (TextView) view.findViewById(R.id.feedbackText);

            StartRating = (RatingBar) view.findViewById(R.id.StartRating);

            // All  link to call buttons
            button = (Button) view.findViewById(R.id.button);
            linkOutputLayout = (LinearLayout) view.findViewById(R.id.linkOutputLayout);

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
                                    if (checkCtaForCopy(title.getText().toString())) {
                                        ToastShow.setText(v.getContext(), "Cannot copy this element", Toast.LENGTH_LONG);
                                    } else {
                                        copyItems(getAdapterPosition());
                                    }
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

