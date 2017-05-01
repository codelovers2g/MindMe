package com.mindmesolo.mindme.AudioRecorder;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.Chronometer;
import com.mindmesolo.mindme.helper.RuntimePermissionsActivity;
import com.naman14.androidlame.AndroidLame;
import com.naman14.androidlame.LameBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PlayAndRecordAudio extends RuntimePermissionsActivity implements View.OnClickListener {

    static final String TAG = "MediaRecording";

    private static final int REQUEST_PERMISSIONS = 20;

    int minBuffer;

    int inSamplerate = 8000;

    AudioRecord audioRecord;

    AndroidLame androidLame;

    Chronometer chronometer;

    FileOutputStream outputStream;

    boolean isRecording = false;

    boolean isPlaying = false;
    boolean isOldAudioFile = false;

    MediaPlayer mediaPlayer;

    int MediaPlayerPauseOn = 0;

    long ChronometerPauseOn = 0;

    TextView textView_done, textView_audio_play_stop_status;

    ImageView img_view_exit_icon, img_view_record, img_view_stop_recording, img_view_play, img_view_pause, img_view_is_recording;

    String AudioFileName = null;

    String dir = Environment.getExternalStorageDirectory().getAbsolutePath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play_and_record_audio);

        getIntent().getData();

        setupInterface();
    }

    private void setupInterface() {

        textView_done = (TextView) findViewById(R.id.textView_done);

        textView_done.setOnClickListener(this);

        textView_audio_play_stop_status = (TextView) findViewById(R.id.textView_audio_play_stop_status);

        img_view_is_recording = (ImageView) findViewById(R.id.img_view_is_recording);

        img_view_exit_icon = (ImageView) findViewById(R.id.img_view_exit_icon);
        img_view_exit_icon.setOnClickListener(this);

        img_view_record = (ImageView) findViewById(R.id.img_view_record);
        img_view_record.setOnClickListener(this);

        img_view_stop_recording = (ImageView) findViewById(R.id.img_view_stop_recording);
        img_view_stop_recording.setOnClickListener(this);

        img_view_play = (ImageView) findViewById(R.id.img_view_play);
        img_view_play.setOnClickListener(this);

        img_view_pause = (ImageView) findViewById(R.id.img_view_pause);
        img_view_pause.setOnClickListener(this);

        chronometer = (Chronometer) findViewById(R.id.chronometer);

        HandleResponse();
    }

    private void HandleResponse() {
        String ExtraData = getIntent().getStringExtra("ExtraData");
        if (ExtraData != null) {
            try {
                JSONObject jsonObject = new JSONObject(ExtraData);
                AudioFileName = jsonObject.getString("media");
                File AudioFile = new File(AudioFileName);
                if (AudioFile.exists()) {
                    textView_audio_play_stop_status.setText("Tap to Listen Recording");
                    img_view_is_recording.setVisibility(View.GONE);
                    img_view_play.setVisibility(View.VISIBLE);
                    img_view_record.setVisibility(View.GONE);
                    img_view_stop_recording.setVisibility(View.GONE);
                    img_view_pause.setVisibility(View.GONE);
                }
                isOldAudioFile = true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_view_record:
                //Check  api permission is required
                if (Build.VERSION.SDK_INT >= 23) {
                    PlayAndRecordAudio.super.requestAppPermissions(new String[]{
                                    Manifest.permission.RECORD_AUDIO,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            R.string.runtime_permissions_txt, REQUEST_PERMISSIONS);
                } else {
                    StartRecordingAudio();
                }
                break;
            case R.id.img_view_stop_recording:
                StopRecordingAudio();
                textView_audio_play_stop_status.setText("Tap to Listen Recording");
                img_view_is_recording.setVisibility(View.GONE);
                img_view_play.setVisibility(View.VISIBLE);
                img_view_record.setVisibility(View.GONE);
                img_view_stop_recording.setVisibility(View.GONE);
                img_view_pause.setVisibility(View.GONE);
                break;
            case R.id.img_view_play:
                StartPlayingAudio();
                textView_audio_play_stop_status.setText("Tap to Pause Playing");
                img_view_play.setVisibility(View.GONE);
                img_view_pause.setVisibility(View.VISIBLE);
                img_view_record.setVisibility(View.GONE);
                img_view_stop_recording.setVisibility(View.GONE);
                break;
            case R.id.img_view_pause:
                StopPlayingAudio();
                textView_audio_play_stop_status.setText("Tap to Listen Recording");
                img_view_play.setVisibility(View.VISIBLE);
                img_view_record.setVisibility(View.GONE);
                img_view_stop_recording.setVisibility(View.GONE);
                img_view_pause.setVisibility(View.GONE);
                break;
            case R.id.img_view_exit_icon:
                if (isRecording == true) StopRecordingAudio();
                if (isPlaying == true) StopPlayingAudio();
                finish();
                break;
            case R.id.textView_done:
                if (isRecording == true) StopRecordingAudio();
                if (isPlaying == true) StopPlayingAudio();
                finalAction();
                break;
        }
    }

    private void StartRecordingAudio() {
        textView_audio_play_stop_status.setText("Tap to Stop Recording");
        img_view_stop_recording.setVisibility(View.VISIBLE);
        img_view_play.setVisibility(View.GONE);
        img_view_record.setVisibility(View.GONE);
        img_view_pause.setVisibility(View.GONE);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        if (!isRecording) {
            new Thread() {
                @Override
                public void run() {
                    isRecording = true;
                    startRecording();
                }
            }.start();
        }
    }

    private void finalAction() {
        if (AudioFileName != null && isOldAudioFile == false) {
            File audiofile = new File(dir, AudioFileName);
            if (audiofile.exists()) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", audiofile.getAbsolutePath());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        } else {
            finish();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        StartRecordingAudio();
    }


    private void startRecording() {
        minBuffer = AudioRecord.getMinBufferSize(inSamplerate, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        //addLog("Initialising audio recorder..");
        audioRecord = new AudioRecord(
                MediaRecorder.AudioSource.MIC, inSamplerate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, minBuffer * 2);
        //5 seconds data
        short[] buffer = new short[inSamplerate * 2 * 5];
        // 'mp3buf' should be at least 7200 bytes long
        byte[] mp3buffer = new byte[(int) (7200 + buffer.length * 2 * 1.25)];

        File directory = new File(dir, "MindMe/Recordings/");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        AudioFileName = "MindMe/Recordings/" + String.valueOf(System.currentTimeMillis()) + ".mp3";

        try {
            outputStream = new FileOutputStream(new File(dir, AudioFileName));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //addLog("Initialising Andorid Lame");
        androidLame = new LameBuilder()
                .setInSampleRate(inSamplerate)
                .setOutChannels(1)
                .setOutBitrate(32)
                .setOutSampleRate(inSamplerate)
                .build();
        //updateStatus("Recording...");
        audioRecord.startRecording();
        int bytesRead = 0;
        while (isRecording) {
            //addLog("reading to short array buffer, buffer sze- " + minBuffer);
            bytesRead = audioRecord.read(buffer, 0, minBuffer);
            //addLog("bytes read=" + bytesRead);
            if (bytesRead > 0) {
                //addLog("encoding bytes to mp3 buffer..");
                int bytesEncoded = androidLame.encode(buffer, buffer, bytesRead, mp3buffer);
                //addLog("bytes encoded=" + bytesEncoded);
                if (bytesEncoded > 0) {
                    try {
                        //addLog("writing mp3 buffer to outputstream with " + bytesEncoded + " bytes");
                        outputStream.write(mp3buffer, 0, bytesEncoded);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //updateStatus("Recording stopped");
        //addLog("flushing final mp3buffer");
        int outputMp3buf = androidLame.flush(mp3buffer);
        //addLog("flushed " + outputMp3buf + " bytes");
        if (outputMp3buf > 0) {
            try {
                //      addLog("writing final mp3buffer to outputstream");
                outputStream.write(mp3buffer, 0, outputMp3buf);
                //    addLog("closing output stream");
                outputStream.close();
                //  updateStatus("Output recording saved in " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //addLog("releasing audio recorder");
        audioRecord.stop();
        audioRecord.release();
        androidLame.close();
        isRecording = false;
    }

    private void StopRecordingAudio() {
        isRecording = false;
        chronometer.stop();
        isPlaying = false;
    }

    private void StartPlayingAudio() {

        mediaPlayer = new MediaPlayer();

        File AudioPlayFile = null;

        if (isOldAudioFile) AudioPlayFile = new File(AudioFileName);

        else AudioPlayFile = new File(dir, AudioFileName);

        try {

            if (AudioPlayFile.exists()) {

                isPlaying = true;

                mediaPlayer.setDataSource(AudioPlayFile.getPath());

                chronometer.setBase(SystemClock.elapsedRealtime() + ChronometerPauseOn);

                mediaPlayer.prepare();

                mediaPlayer.start();

                mediaPlayer.seekTo(MediaPlayerPauseOn);

                chronometer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                textView_audio_play_stop_status.setText("Tap to Listen Recording");

                img_view_play.setVisibility(View.VISIBLE);
                img_view_record.setVisibility(View.GONE);
                img_view_stop_recording.setVisibility(View.GONE);
                img_view_pause.setVisibility(View.GONE);

                ChronometerPauseOn = 0;
                MediaPlayerPauseOn = 0;
                chronometer.stop();
                isPlaying = false;
                mediaPlayer.stop();
                mediaPlayer.release();

            }
        });
    }

    private void StopPlayingAudio() {

        isPlaying = false;

        MediaPlayerPauseOn = mediaPlayer.getCurrentPosition();

        mediaPlayer.pause();

        ChronometerPauseOn = chronometer.getBase() - SystemClock.elapsedRealtime();

        chronometer.stop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer = null;
        isPlaying = false;
        isRecording = false;
    }
}
