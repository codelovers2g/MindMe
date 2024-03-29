package com.mindmesolo.mindme.AudioRecorder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

import com.github.florent37.camerafragment.configuration.Configuration;
import com.github.florent37.camerafragment.internal.enums.MediaAction;
import com.github.florent37.camerafragment.internal.ui.view.AspectFrameLayout;
import com.github.florent37.camerafragment.internal.utils.Utils;

import java.io.File;
import java.io.IOException;

/**
 * Created by pc-14 on 2/22/2017.
 */

public class PreviewImageActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int ACTION_CONFIRM = 900;
    public static final int ACTION_RETAKE = 901;
    public static final int ACTION_CANCEL = 902;
    private static final String TAG = "PreviewActivity";
    private final static String MEDIA_ACTION_ARG = "media_action_arg";
    private final static String FILE_PATH_ARG = "file_path_arg";
    private final static String RESPONSE_CODE_ARG = "response_code_arg";
    private final static String VIDEO_POSITION_ARG = "current_video_position";
    private final static String VIDEO_IS_PLAYED_ARG = "is_played";
    private final static String MIME_TYPE_VIDEO = "video";
    private final static String MIME_TYPE_IMAGE = "image";
    private static String ReturnMediaType = "Image";
    private int mediaAction;
    private String previewFilePath;
    private SurfaceView surfaceView;
    private FrameLayout photoPreviewContainer;
    private ImageView imagePreview;
    private ViewGroup buttonPanel;
    private AspectFrameLayout videoPreviewContainer;
    private View cropMediaAction;
    private TextView ratioChanger;
    private MediaController mediaController;
    private MediaPlayer mediaPlayer;
    private int currentPlaybackPosition = 0;
    private boolean isVideoPlaying = true;
    private int currentRatioIndex = 0;
    private float[] ratios;
    private String[] ratioLabels;

    public static Intent newIntentPhoto(Context context, String filePath) {
        ReturnMediaType = "Image";
        return new Intent(context, PreviewImageActivity.class)
                .putExtra(MEDIA_ACTION_ARG, MediaAction.ACTION_PHOTO)
                .putExtra(FILE_PATH_ARG, filePath);
    }

    public static Intent newIntentVideo(Context context, String filePath) {
        ReturnMediaType = "Video";
        return new Intent(context, PreviewImageActivity.class)
                .putExtra(MEDIA_ACTION_ARG, MediaAction.ACTION_VIDEO)
                .putExtra(FILE_PATH_ARG, filePath);
    }

    public static String getMediaFilePatch(@NonNull Intent resultIntent) {
        return resultIntent.getStringExtra(FILE_PATH_ARG);
    }

    public static boolean isResultConfirm(@NonNull Intent resultIntent) {
        return ACTION_CONFIRM == resultIntent.getIntExtra(RESPONSE_CODE_ARG, -1);
    }

    public static boolean isResultRetake(@NonNull Intent resultIntent) {
        return ACTION_RETAKE == resultIntent.getIntExtra(RESPONSE_CODE_ARG, -1);
    }

    public static boolean isResultCancel(@NonNull Intent resultIntent) {
        return ACTION_CANCEL == resultIntent.getIntExtra(RESPONSE_CODE_ARG, -1);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.github.florent37.camerafragment.R.layout.activity_preview);

        String originalRatioLabel = getString(com.github.florent37.camerafragment.R.string.preview_controls_original_ratio_label);
        ratioLabels = new String[]{originalRatioLabel, "1:1", "4:3", "16:9"};
        ratios = new float[]{0f, 1f, 4f / 3f, 16f / 9f};

        surfaceView = (SurfaceView) findViewById(com.github.florent37.camerafragment.R.id.video_preview);
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mediaController == null) return false;
                if (mediaController.isShowing()) {
                    mediaController.hide();
                    showButtonPanel(true);
                } else {
                    showButtonPanel(false);
                    mediaController.show();
                }
                return false;
            }
        });

        videoPreviewContainer = (AspectFrameLayout) findViewById(com.github.florent37.camerafragment.R.id.previewAspectFrameLayout);
        photoPreviewContainer = (FrameLayout) findViewById(com.github.florent37.camerafragment.R.id.photo_preview_container);
        buttonPanel = (ViewGroup) findViewById(com.github.florent37.camerafragment.R.id.preview_control_panel);
        View confirmMediaResult = findViewById(com.github.florent37.camerafragment.R.id.confirm_media_result);
        View reTakeMedia = findViewById(com.github.florent37.camerafragment.R.id.re_take_media);
        View cancelMediaAction = findViewById(com.github.florent37.camerafragment.R.id.cancel_media_action);
        cropMediaAction = findViewById(com.github.florent37.camerafragment.R.id.crop_image);
        ratioChanger = (TextView) findViewById(com.github.florent37.camerafragment.R.id.ratio_image);
        ratioChanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentRatioIndex = (currentRatioIndex + 1) % ratios.length;
                ratioChanger.setText(ratioLabels[currentRatioIndex]);
            }
        });

        cropMediaAction.setVisibility(View.GONE);
        ratioChanger.setVisibility(View.GONE);

        if (cropMediaAction != null)
            cropMediaAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });

        if (confirmMediaResult != null)
            confirmMediaResult.setOnClickListener(this);

        if (reTakeMedia != null)
            reTakeMedia.setOnClickListener(this);

        if (cancelMediaAction != null)
            cancelMediaAction.setOnClickListener(this);

        Bundle args = getIntent().getExtras();

        mediaAction = args.getInt(MEDIA_ACTION_ARG);
        previewFilePath = args.getString(FILE_PATH_ARG);

        if (mediaAction == Configuration.MEDIA_ACTION_VIDEO) {
            displayVideo(savedInstanceState);
        } else if (mediaAction == Configuration.MEDIA_ACTION_PHOTO) {
            displayImage();
        } else {
            String mimeType = Utils.getMimeType(previewFilePath);
            if (mimeType.contains(MIME_TYPE_VIDEO)) {
                displayVideo(savedInstanceState);
            } else if (mimeType.contains(MIME_TYPE_IMAGE)) {
                displayImage();
            } else finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveVideoParams(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (mediaController != null) {
            mediaController.hide();
            mediaController = null;
        }
    }

    private void displayImage() {
        videoPreviewContainer.setVisibility(View.GONE);
        surfaceView.setVisibility(View.GONE);
        showImagePreview();
        ratioChanger.setText(ratioLabels[currentRatioIndex]);
    }

    private void showImagePreview() {
        imagePreview = new ImageView(this);
        ImageLoaderNew.Builder builder = new ImageLoaderNew.Builder(this);
//        Bitmap myBitmap = BitmapFactory.decodeFile(previewFilePath);
//        rotateBitmap(myBitmap,getExifOrientation(previewFilePath));
//        rotateBitmap(myBitmap,getExifOrientation(previewFilePath));

        builder.load(previewFilePath).build().into(imagePreview);
        photoPreviewContainer.removeAllViews();
        photoPreviewContainer.addView(imagePreview);


//        imagePreview = new ImageView(this);
//        ImageLoaderNew.Builder builder = new ImageLoaderNew.Builder(this);
//        builder.load(previewFilePath).build().into(imagePreview);
//        photoPreviewContainer.removeAllViews();
//        photoPreviewContainer.addView(imagePreview);
    }

    private void displayVideo(Bundle savedInstanceState) {
        cropMediaAction.setVisibility(View.GONE);
        ratioChanger.setVisibility(View.GONE);
        if (savedInstanceState != null) {
            loadVideoParams(savedInstanceState);
        }
        photoPreviewContainer.setVisibility(View.GONE);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                showVideoPreview(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    private void showVideoPreview(SurfaceHolder holder) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(previewFilePath);
            mediaPlayer.setDisplay(holder);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaController = new MediaController(PreviewImageActivity.this);
                    mediaController.setAnchorView(surfaceView);
                    mediaController.setMediaPlayer(new MediaController.MediaPlayerControl() {
                        @Override
                        public void start() {
                            mediaPlayer.start();
                        }

                        @Override
                        public void pause() {
                            mediaPlayer.pause();
                        }

                        @Override
                        public int getDuration() {
                            return mediaPlayer.getDuration();
                        }

                        @Override
                        public int getCurrentPosition() {
                            return mediaPlayer.getCurrentPosition();
                        }

                        @Override
                        public void seekTo(int pos) {
                            mediaPlayer.seekTo(pos);
                        }

                        @Override
                        public boolean isPlaying() {
                            return mediaPlayer.isPlaying();
                        }

                        @Override
                        public int getBufferPercentage() {
                            return 0;
                        }

                        @Override
                        public boolean canPause() {
                            return true;
                        }

                        @Override
                        public boolean canSeekBackward() {
                            return true;
                        }

                        @Override
                        public boolean canSeekForward() {
                            return true;
                        }

                        @Override
                        public int getAudioSessionId() {
                            return mediaPlayer.getAudioSessionId();
                        }
                    });

                    int videoWidth = mp.getVideoWidth();
                    int videoHeight = mp.getVideoHeight();

                    videoPreviewContainer.setAspectRatio((double) videoWidth / videoHeight);

                    mediaPlayer.start();
                    mediaPlayer.seekTo(currentPlaybackPosition);

                    if (!isVideoPlaying)
                        mediaPlayer.pause();
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    finish();
                    return true;
                }
            });
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            Log.e(TAG, "Error media player playing video.");
            finish();
        }
    }

    private void saveVideoParams(Bundle outState) {
        if (mediaPlayer != null) {
            outState.putInt(VIDEO_POSITION_ARG, mediaPlayer.getCurrentPosition());
            outState.putBoolean(VIDEO_IS_PLAYED_ARG, mediaPlayer.isPlaying());
        }
    }

    private void loadVideoParams(Bundle savedInstanceState) {
        currentPlaybackPosition = savedInstanceState.getInt(VIDEO_POSITION_ARG, 0);
        isVideoPlaying = savedInstanceState.getBoolean(VIDEO_IS_PLAYED_ARG, true);
    }

    private void showButtonPanel(boolean show) {
        if (show) {
            buttonPanel.setVisibility(View.VISIBLE);
        } else {
            buttonPanel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        Intent resultIntent = new Intent();
        if (view.getId() == com.github.florent37.camerafragment.R.id.confirm_media_result) {
            resultIntent.putExtra(RESPONSE_CODE_ARG, ACTION_CONFIRM).putExtra(FILE_PATH_ARG, previewFilePath)
                    .putExtra(MEDIA_ACTION_ARG, ReturnMediaType);
            setResult(RESULT_OK, resultIntent);
        } else if (view.getId() == com.github.florent37.camerafragment.R.id.re_take_media) {
            deleteMediaFile();
            resultIntent.putExtra(RESPONSE_CODE_ARG, ACTION_RETAKE);
            setResult(RESULT_CANCELED, resultIntent);
        } else if (view.getId() == com.github.florent37.camerafragment.R.id.cancel_media_action) {
            deleteMediaFile();
            resultIntent.putExtra(RESPONSE_CODE_ARG, ACTION_CANCEL);
            setResult(RESULT_CANCELED, resultIntent);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        deleteMediaFile();
    }

    private boolean deleteMediaFile() {
        File mediaFile = new File(previewFilePath);
        return mediaFile.delete();
    }

    private Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }

        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError ignore) {
            return null;
        }
    }

    private int getExifOrientation(String url) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(url);
        } catch (IOException ignore) {
        }
        return exif == null ? ExifInterface.ORIENTATION_UNDEFINED :
                exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
    }

}

