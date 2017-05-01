package com.mindmesolo.mindme.AudioRecorder;
/**
 * Created by pc-14 on 2/14/2017.
 */

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mindmesolo.mindme.R;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.FileUtil;

public class VideoPlayer extends AppCompatActivity implements EasyVideoCallback {

    private static final String TAG = "VideoPlayer";
    private static final String TEST_URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    private EasyVideoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.video_player);

        player = (EasyVideoPlayer) findViewById(R.id.player);

        assert player != null;

        player.setCallback(this);
        File VideoFile = null;
        try {
            VideoFile = FileUtil.from(this, Uri.parse(getIntent().getStringExtra("MediaLink")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (VideoFile.exists()) {
            Log.i(TAG, "VideoFileExist");
            Uri uri = Uri.fromFile(VideoFile);
            player.setSource(uri);
        }
        // player.setSource(Uri.parse(TEST_URL));
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.pause();
    }

    @Override
    public void onStarted(EasyVideoPlayer player) {
    }

    @Override
    public void onPaused(EasyVideoPlayer player) {
    }

    @Override
    public void onPreparing(EasyVideoPlayer player) {
        Log.d("EVP-Sample", "onPreparing()");
    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {
        Log.d("EVP-Sample", "onPrepared()");
    }

    @Override
    public void onBuffering(int percent) {
        Log.d("EVP-Sample", "onBuffering(): " + percent + "%");
    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {
        Log.d("EVP-Sample", "onError(): " + e.getMessage());
        new MaterialDialog.Builder(this)
                .title(R.string.error)
                .content(e.getMessage())
                .positiveText(android.R.string.ok)
                .show();
    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {
        Log.d("EVP-Sample", "onCompletion()");
    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {
        //Toast.makeText(this, "Retry", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {
        //Toast.makeText(this, "Submit", Toast.LENGTH_SHORT).show();
        finish();
    }

}
