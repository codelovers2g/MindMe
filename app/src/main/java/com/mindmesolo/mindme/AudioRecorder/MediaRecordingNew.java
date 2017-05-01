package com.mindmesolo.mindme.AudioRecorder;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.florent37.camerafragment.CameraFragment;
import com.github.florent37.camerafragment.CameraFragmentApi;
import com.github.florent37.camerafragment.configuration.Configuration;
import com.github.florent37.camerafragment.listeners.CameraFragmentControlsListener;
import com.github.florent37.camerafragment.listeners.CameraFragmentResultListener;
import com.github.florent37.camerafragment.listeners.CameraFragmentStateListener;
import com.github.florent37.camerafragment.listeners.CameraFragmentVideoRecordTextListener;
import com.github.florent37.camerafragment.widgets.CameraSettingsView;
import com.github.florent37.camerafragment.widgets.CameraSwitchView;
import com.github.florent37.camerafragment.widgets.FlashSwitchView;
import com.github.florent37.camerafragment.widgets.MediaActionSwitchView;
import com.github.florent37.camerafragment.widgets.RecordButton;
import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.RuntimePermissionsActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc-14 on 2/22/2017.
 */

public class MediaRecordingNew extends RuntimePermissionsActivity implements View.OnClickListener {

    public static final String FRAGMENT_TAG = "camera";

    private static final int REQUEST_CAMERA_PERMISSIONS = 931;

    private static final int REQUEST_PREVIEW_CODE = 1001;
    static String camera_request = "IMAGE";
    CameraSettingsView settingsView;
    FlashSwitchView flashSwitchView;
    CameraSwitchView cameraSwitchView;
    RecordButton recordButton;
    MediaActionSwitchView mediaActionSwitchView;
    TextView recordDurationText;
    TextView recordSizeText;
    RelativeLayout cameraLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_recording_new);
        camera_request = getIntent().getStringExtra("CAMERA_REQUEST");

        settingsView = (CameraSettingsView) findViewById(R.id.settings_view);
        settingsView.setOnClickListener(this);

        flashSwitchView = (FlashSwitchView) findViewById(R.id.flash_switch_view);
        flashSwitchView.setOnClickListener(this);

        cameraSwitchView = (CameraSwitchView) findViewById(R.id.front_back_camera_switcher);
        cameraSwitchView.setOnClickListener(this);

        recordButton = (RecordButton) findViewById(R.id.record_button);
        recordButton.setOnClickListener(this);

        mediaActionSwitchView = (MediaActionSwitchView) findViewById(R.id.photo_video_camera_switcher);
        mediaActionSwitchView.setOnClickListener(this);

        recordDurationText = (TextView) findViewById(R.id.record_duration_text);
        recordSizeText = (TextView) findViewById(R.id.record_size_mb_text);
        cameraLayout = (RelativeLayout) findViewById(R.id.cameraLayout);
        onAddCameraClicked();
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        addCamera();
    }

    public void addCamera() {
        cameraLayout.setVisibility(View.VISIBLE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        CameraFragment cameraFragment = null;
        if (camera_request.equalsIgnoreCase("VIDEO")) {
            cameraFragment = CameraFragment.newInstance(new Configuration.Builder()
                    .setCamera(Configuration.CAMERA_FACE_FRONT)
                    .setVideoDuration(120000)
                    .setMediaQuality(Configuration.MEDIA_QUALITY_LOW)
                    .setMediaAction(Configuration.MEDIA_ACTION_VIDEO)
                    .build());
        } else {
            cameraFragment = CameraFragment.newInstance(new Configuration.Builder()
                    .setCamera(Configuration.CAMERA_FACE_FRONT)
                    .setFlashMode(Configuration.FLASH_MODE_AUTO)
                    .build());
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, cameraFragment, FRAGMENT_TAG)
                .commit();

        if (cameraFragment != null) {
            cameraFragment.setResultListener(new CameraFragmentResultListener() {
                @Override
                public void onVideoRecorded(String filePath) {
                    Intent intent = PreviewImageActivity.newIntentVideo(MediaRecordingNew.this, filePath);
                    startActivityForResult(intent, REQUEST_PREVIEW_CODE);
                }

                @Override
                public void onPhotoTaken(byte[] bytes, String filePath) {
                    Intent intent = PreviewImageActivity.newIntentPhoto(MediaRecordingNew.this, filePath);
                    startActivityForResult(intent, REQUEST_PREVIEW_CODE);
                }
            });
            cameraFragment.setStateListener(new CameraFragmentStateListener() {

                @Override
                public void onCurrentCameraBack() {
                    cameraSwitchView.displayBackCamera();
                }

                @Override
                public void onCurrentCameraFront() {
                    cameraSwitchView.displayFrontCamera();
                }

                @Override
                public void onFlashAuto() {
                    flashSwitchView.displayFlashAuto();
                }

                @Override
                public void onFlashOn() {
                    flashSwitchView.displayFlashOn();
                }

                @Override
                public void onFlashOff() {
                    flashSwitchView.displayFlashOff();
                }

                @Override
                public void onCameraSetupForPhoto() {
                    mediaActionSwitchView.displayActionWillSwitchVideo();
                    recordButton.displayPhotoState();
                    flashSwitchView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCameraSetupForVideo() {
                    mediaActionSwitchView.displayActionWillSwitchPhoto();
                    recordButton.displayVideoRecordStateReady();
                    flashSwitchView.setVisibility(View.GONE);
                }

                @Override
                public void shouldRotateControls(int degrees) {
                    ViewCompat.setRotation(cameraSwitchView, degrees);
                    ViewCompat.setRotation(mediaActionSwitchView, degrees);
                    ViewCompat.setRotation(flashSwitchView, degrees);
                    ViewCompat.setRotation(recordDurationText, degrees);
                    ViewCompat.setRotation(recordSizeText, degrees);
                }

                @Override
                public void onRecordStateVideoReadyForRecord() {
                    recordButton.displayVideoRecordStateReady();
                }

                @Override
                public void onRecordStateVideoInProgress() {
                    recordButton.displayVideoRecordStateInProgress();
                }

                @Override
                public void onRecordStatePhoto() {
                    recordButton.displayPhotoState();
                }

                @Override
                public void onStopVideoRecord() {
                    recordSizeText.setVisibility(View.GONE);
                    cameraSwitchView.setVisibility(View.VISIBLE);
                    settingsView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onStartVideoRecord(File outputFile) {
                }
            });
            cameraFragment.setControlsListener(new CameraFragmentControlsListener() {
                @Override
                public void lockControls() {
//                    cameraSwitchView.setEnabled(false);
//                    recordButton.setEnabled(false);
//                    settingsView.setEnabled(false);
//                    flashSwitchView.setEnabled(false);
                }

                @Override
                public void unLockControls() {
//                    cameraSwitchView.setEnabled(true);
//                    recordButton.setEnabled(true);
//                    settingsView.setEnabled(true);
//                    flashSwitchView.setEnabled(true);
                }

                @Override
                public void allowCameraSwitching(boolean allow) {
                    cameraSwitchView.setVisibility(allow ? View.VISIBLE : View.GONE);
                }

                @Override
                public void allowRecord(boolean allow) {
                    recordButton.setEnabled(allow);
                }

                @Override
                public void setMediaActionSwitchVisible(boolean visible) {
                    mediaActionSwitchView.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                }
            });
            cameraFragment.setTextListener(new CameraFragmentVideoRecordTextListener() {
                @Override
                public void setRecordSizeText(long size, String text) {
                    recordSizeText.setText(text);
                }

                @Override
                public void setRecordSizeTextVisible(boolean visible) {
                    recordSizeText.setVisibility(visible ? View.VISIBLE : View.GONE);
                }

                @Override
                public void setRecordDurationText(String text) {
                    recordDurationText.setText(text);
                }

                @Override
                public void setRecordDurationTextVisible(boolean visible) {
                    recordDurationText.setVisibility(visible ? View.VISIBLE : View.GONE);
                }
            });
        }
        //SwitchTo RearCamera
        onSwitchCameraClicked();
    }

    private CameraFragmentApi getCameraFragment() {
        return (CameraFragmentApi) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
    }

    // handle all incoming results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_PREVIEW_CODE) {
            Uri uri = Uri.fromFile(new File(data.getStringExtra("file_path_arg")));
            setResult(RESULT_OK, new Intent().setData(uri)
                    .putExtra("Title", data.getStringExtra("media_action_arg")));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flash_switch_view:
                onFlashSwitchClicked();
                break;

            case R.id.front_back_camera_switcher:
                onSwitchCameraClicked();
                break;

            case R.id.record_button:
                long TIME = 1 * 1000;
                recordButton.setEnabled(false);
                onRecordButtonClicked();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recordButton.setEnabled(true);
                    }
                }, TIME);
                break;
            case R.id.settings_view:
                onSettingsClicked();
                break;
            case R.id.photo_video_camera_switcher:
                onMediaActionSwitchClicked();
                break;
        }
    }

    public void onFlashSwitchClicked() {
        final CameraFragmentApi cameraFragment = getCameraFragment();
        if (cameraFragment != null) {
            cameraFragment.toggleFlashMode();
        }
    }

    public void onSwitchCameraClicked() {
        final CameraFragmentApi cameraFragment = getCameraFragment();
        if (cameraFragment != null) {
            cameraFragment.switchCameraTypeFrontBack();
        }
    }

    public void onRecordButtonClicked() {
        String path = Environment.getExternalStorageDirectory().toString();
        File appDirectory = new File(path + "/" + "FolderName");
        appDirectory.mkdirs();
        String NewPath = appDirectory.getPath();
        String name = "File" + System.currentTimeMillis();
        final CameraFragmentApi cameraFragment = getCameraFragment();
        if (cameraFragment != null) {
            cameraFragment.takePhotoOrCaptureVideo(new CameraFragmentResultListener() {
                @Override
                public void onVideoRecorded(String filePath) {
                }

                @Override
                public void onPhotoTaken(byte[] bytes, String filePath) {

                }
            }, NewPath, name);
        }
    }

    public void onSettingsClicked() {
        final CameraFragmentApi cameraFragment = getCameraFragment();
        if (cameraFragment != null) {
            cameraFragment.openSettingDialog();
        }
    }

    public void onMediaActionSwitchClicked() {
        final CameraFragmentApi cameraFragment = getCameraFragment();
        if (cameraFragment != null) {
            cameraFragment.switchActionPhotoVideo();
        }
    }

    public void onAddCameraClicked() {
        if (Build.VERSION.SDK_INT > 15) {
            final String[] permissions = {
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE};

            final List<String> permissionsToRequest = new ArrayList<>();
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(permission);
                }
            }
            if (!permissionsToRequest.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                        REQUEST_CAMERA_PERMISSIONS);
            } else addCamera();
        } else {
            addCamera();
        }
    }
}
