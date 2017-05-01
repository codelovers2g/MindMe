package com.mindmesolo.mindme.CreateCampaign;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mindmesolo.mindme.AudioRecorder.MediaRecordingNew;
import com.mindmesolo.mindme.AudioRecorder.PlayAndRecordAudio;
import com.mindmesolo.mindme.CreateMobilePages.AddHours;
import com.mindmesolo.mindme.CreateMobilePages.AddLeadForm;
import com.mindmesolo.mindme.CreateMobilePages.AddMap;
import com.mindmesolo.mindme.CreateMobilePages.AddSocialMedia;
import com.mindmesolo.mindme.CreateMobilePages.AddSpecialOffer;
import com.mindmesolo.mindme.R;

/**
 * Created by User1 on 7/1/2016.
 */
public class CreateEmailOption extends AppCompatActivity implements View.OnClickListener {

    TextView textViewCancel;

    LinearLayout linearLayoutProfile, linearLayoutMedia, linearLayoutText, linearLayoutCTA, linearLayoutCTANew;

    ImageButton imageButtonImage, imageButtonVideo, imageButtonAudio, imageButtonSocial, imageButtonHours;

    ImageButton imageButtonLogo, imageButtonLocation;

    ImageButton imageButtonParagraph, imageButtonTitle, imageButtonLeadForm, imageButtonSpecialOffers;

    ImageButton imageButtonResponse, imageButtonCall, imageButtonCall2, imageButtonFeedback, imageButtonPoll;

    Dialog dialog;

    LinearLayout link_call_1, link_call_2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createemailoption);

        linearLayoutProfile = (LinearLayout) findViewById(R.id.linearLayoutProfile);
        linearLayoutMedia = (LinearLayout) findViewById(R.id.linearLayoutMedia);
        linearLayoutText = (LinearLayout) findViewById(R.id.linearLayoutText);
        linearLayoutCTA = (LinearLayout) findViewById(R.id.linearLayoutCTA);
        linearLayoutCTANew = (LinearLayout) findViewById(R.id.linearLayoutCTANew);

        link_call_1 = (LinearLayout) findViewById(R.id.link_call_1);
        link_call_2 = (LinearLayout) findViewById(R.id.link_call_2);

        imageButtonHours = (ImageButton) findViewById(R.id.img_btn_hours);
        imageButtonHours.setOnClickListener(this);

        imageButtonLogo = (ImageButton) findViewById(R.id.img_btn_logo);
        imageButtonLogo.setOnClickListener(this);

        imageButtonLocation = (ImageButton) findViewById(R.id.img_btn_location);
        imageButtonLocation.setOnClickListener(this);

        imageButtonImage = (ImageButton) findViewById(R.id.image);
        imageButtonImage.setOnClickListener(this);

        imageButtonVideo = (ImageButton) findViewById(R.id.video);
        imageButtonVideo.setOnClickListener(this);

        imageButtonAudio = (ImageButton) findViewById(R.id.audio);
        imageButtonAudio.setOnClickListener(this);

        imageButtonSocial = (ImageButton) findViewById(R.id.img_btn_social);
        imageButtonSocial.setOnClickListener(this);

        imageButtonParagraph = (ImageButton) findViewById(R.id.paragraph);
        imageButtonParagraph.setOnClickListener(this);

        imageButtonTitle = (ImageButton) findViewById(R.id.title);
        imageButtonTitle.setOnClickListener(this);

        imageButtonResponse = (ImageButton) findViewById(R.id.response);
        imageButtonResponse.setOnClickListener(this);

        imageButtonCall = (ImageButton) findViewById(R.id.linkcall);
        imageButtonCall.setOnClickListener(this);


        imageButtonCall2 = (ImageButton) findViewById(R.id.linkcall2);
        imageButtonCall2.setOnClickListener(this);

        imageButtonFeedback = (ImageButton) findViewById(R.id.feedback);
        imageButtonFeedback.setOnClickListener(this);

        imageButtonPoll = (ImageButton) findViewById(R.id.poll);
        imageButtonPoll.setOnClickListener(this);

        textViewCancel = (TextView) findViewById(R.id.cancel);
        textViewCancel.setOnClickListener(this);

        imageButtonLeadForm = (ImageButton) findViewById(R.id.img_leadForm);
        imageButtonLeadForm.setOnClickListener(this);

        imageButtonSpecialOffers = (ImageButton) findViewById(R.id.img_special_offer);
        imageButtonSpecialOffers.setOnClickListener(this);

        String message = getIntent().getStringExtra("Message");

        if (message != null) {
            switch (message) {
                case "Email":
                    linearLayoutProfile.setVisibility(View.GONE);
                    break;
                case "Text":
                    linearLayoutProfile.setVisibility(View.GONE);
                    linearLayoutText.setVisibility(View.GONE);
                    break;
                case "MobilePage":
                    String LeadForm = getIntent().getStringExtra("LeadForm");
                    if (LeadForm != null) {
                        imageButtonLeadForm.setVisibility(View.INVISIBLE);
                    } else {
                        imageButtonLeadForm.setVisibility(View.VISIBLE);
                    }
                    imageButtonSpecialOffers.setVisibility(View.VISIBLE);
                    link_call_1.setVisibility(View.GONE);
                    link_call_2.setVisibility(View.VISIBLE);
                    break;
                case "Voice":
                    linearLayoutProfile.setVisibility(View.GONE);
                    linearLayoutMedia.setVisibility(View.GONE);
                    linearLayoutText.setVisibility(View.GONE);
                    break;
            }
        }
        // check if cta element is already added
        boolean CTA = getIntent().getBooleanExtra("CTA", false);
        if (CTA) {
            linearLayoutCTA.setVisibility(View.GONE);
            linearLayoutCTANew.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.image:
            case R.id.img_btn_logo:
                showDialog(view);
                break;

            case R.id.video:
                showDialogVideo(view);
                break;

            case R.id.audio:
                intent = new Intent(this, PlayAndRecordAudio.class);
                startActivityForResult(intent, 3);
                break;

            case R.id.title:
                intent = new Intent(this, CreateTitle.class);
                startActivityForResult(intent, 4);
                break;

            case R.id.paragraph:
                intent = new Intent(this, CreateParagraph.class);
                startActivityForResult(intent, 5);
                break;

            case R.id.response:
                intent = new Intent(this, CallToActionElements.class);
                intent.putExtra("MediaType", "Response");
                startActivityForResult(intent, 6);
                break;

            case R.id.linkcall:
            case R.id.linkcall2:
                intent = new Intent(this, CallToActionElements.class);
                intent.putExtra("MediaType", "Link/Call");
                startActivityForResult(intent, 7);
                break;

            case R.id.feedback:
                intent = new Intent(this, CallToActionElements.class);
                intent.putExtra("MediaType", "Feedback");
                startActivityForResult(intent, 8);
                break;

            case R.id.poll:
                intent = new Intent(this, CallToActionElements.class);
                intent.putExtra("MediaType", "Poll");
                startActivityForResult(intent, 9);
                break;

            case R.id.img_leadForm:
                intent = new Intent(this, AddLeadForm.class);
                startActivityForResult(intent, 10);
                break;

            case R.id.img_special_offer:
                intent = new Intent(this, AddSpecialOffer.class);
                startActivityForResult(intent, 11);
                break;

            case R.id.img_btn_social:
                intent = new Intent(this, AddSocialMedia.class);
                startActivityForResult(intent, 12);
                break;

            case R.id.img_btn_hours:
                intent = new Intent(this, AddHours.class);
                startActivityForResult(intent, 13);
                break;

            case R.id.img_btn_location:
                intent = new Intent(this, AddMap.class);
                startActivityForResult(intent, 14);
                break;

            case R.id.cancel:
                finish();
                break;
        }
    }

    // handle all incoming results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    setResult(Activity.RESULT_OK, data.putExtra("Title", "Image").putExtra("result", "Image"));
                } else {
                    setResult(Activity.RESULT_OK, new Intent().putExtra("Title", "Image"));
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    setResult(Activity.RESULT_OK, data.putExtra("Title", "Video").putExtra("result", "Video"));
                } else {
                    setResult(Activity.RESULT_OK, new Intent().putExtra("Title", "Video"));
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    setResult(Activity.RESULT_OK, data.putExtra("Title", "Audio"));
                } else {
                    setResult(Activity.RESULT_OK, new Intent().putExtra("Title", "Audio"));
                }
                break;
            case 4:
                if (resultCode == RESULT_OK) {
                    setResult(Activity.RESULT_OK, data.putExtra("Title", MediaTypes.TITLE));
                } else {
                    setResult(Activity.RESULT_OK, new Intent().putExtra("Title", MediaTypes.TITLE));
                }
                break;
            case 5:
                if (resultCode == RESULT_OK) {
                    setResult(Activity.RESULT_OK, data.putExtra("Title", MediaTypes.PARAGRAPH));
                } else {
                    setResult(Activity.RESULT_OK, new Intent().putExtra("Title", MediaTypes.PARAGRAPH));
                }
                break;
            case 6:
                if (resultCode == RESULT_OK) {
                    setResult(Activity.RESULT_OK, data);
                } else {
                    setResult(Activity.RESULT_OK, new Intent().putExtra("Title", MediaTypes.RESPONSE));
                }
                break;
            case 7:
                if (resultCode == RESULT_OK) {
                    setResult(Activity.RESULT_OK, data);
                } else {
                    setResult(Activity.RESULT_OK, new Intent().putExtra("Title", MediaTypes.LINKCALL));
                }
                break;
            case 8:
                if (resultCode == RESULT_OK) {
                    setResult(Activity.RESULT_OK, data);
                } else {
                    setResult(Activity.RESULT_OK, new Intent().putExtra("Title", MediaTypes.FEEDBACK));
                }
                break;
            case 9:
                if (resultCode == RESULT_OK) {
                    setResult(Activity.RESULT_OK, data);
                } else {
                    setResult(Activity.RESULT_OK, new Intent().putExtra("Title", MediaTypes.POLL));
                }
                break;
            case 10:
                if (resultCode == RESULT_OK) {
                    setResult(Activity.RESULT_OK, data.putExtra("Title", MediaTypes.LEADFORM));
                } else {
                    setResult(Activity.RESULT_OK, new Intent().putExtra("Title", MediaTypes.LEADFORM));
                }
                break;
            case 11:
                if (resultCode == RESULT_OK) {
                    setResult(Activity.RESULT_OK, data.putExtra("Title", MediaTypes.SPECIAL_OFFERS));
                } else {
                    setResult(Activity.RESULT_OK, new Intent().putExtra("Title", MediaTypes.SPECIAL_OFFERS));
                }
                break;
            case 12:
                if (resultCode == RESULT_OK) {
                    setResult(Activity.RESULT_OK, data.putExtra("Title", MediaTypes.SOCIAL));
                } else {
                    setResult(Activity.RESULT_OK, new Intent().putExtra("Title", MediaTypes.SOCIAL));
                }
                break;
            case 13:
                if (resultCode == RESULT_OK) {
                    setResult(Activity.RESULT_OK, data.putExtra("Title", MediaTypes.HOURS));
                } else {
                    setResult(Activity.RESULT_OK, new Intent().putExtra("Title", MediaTypes.HOURS));
                }
                break;
            case 14:
                if (resultCode == RESULT_OK) {
                    setResult(Activity.RESULT_OK, data.putExtra("Title", MediaTypes.MAP));
                } else {
                    setResult(Activity.RESULT_OK, new Intent().putExtra("Title", MediaTypes.MAP));
                }
                break;
        }
        finish();
    }

    // show Dialog For Message For Video
    private void showDialogVideo(final View v) {
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
        tv_preview_video.setVisibility(View.GONE);

        View view_preview_video = (View) dialog.findViewById(R.id.view_preview_video);
        view_preview_video.setVisibility(View.GONE);

        TextView tv_take_video = (TextView) dialog.findViewById(R.id.tv_take_video);
        tv_take_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateEmailOption.this, MediaRecordingNew.class);
                intent.putExtra("CAMERA_REQUEST", "VIDEO");
                startActivityForResult(intent, 2);
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
                ((Activity) v.getContext()).startActivityForResult(Intent.createChooser(intent, "Select Video"), 2);
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

    // show Dialog For Message For Image
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
                Intent intent = new Intent(CreateEmailOption.this, MediaRecordingNew.class);
                intent.putExtra("CAMERA_REQUEST", "IMAGE");
                startActivityForResult(intent, 1);
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
                ((Activity) v.getContext()).startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
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
}
