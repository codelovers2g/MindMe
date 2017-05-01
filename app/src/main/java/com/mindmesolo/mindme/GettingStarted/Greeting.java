package com.mindmesolo.mindme.GettingStarted;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.Chronometer;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.naman14.androidlame.AndroidLame;
import com.naman14.androidlame.LameBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by User1 on 19-05-2016.
 */
public class Greeting extends Fragment implements View.OnClickListener {

    public static final String TAG = "Greeting";

    int inSamplerate = 8000;

    AudioRecord audioRecord;

    AndroidLame androidLame;

    FileOutputStream outputStream;

    boolean isRecording = false;
    Button recordBtn, stopbtn, audio_play, audio_stop, audio_replace;
    TextView mobilenumber, message;
    Chronometer myChronometer;
    LinearLayout play_and_stop;
    SwitchCompat greeting_switch;
    View rootView;
    int minBuffer;
    long ChronometerPauseOn = 0;
    int MediaPlayerPauseOn = 0;
    MediaPlayer mediaPlayer;
    SqliteDataBaseHelper sqliteDataBaseHelper;
    ArrayList<String> greetingdata = new ArrayList<String>();
    String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
    private GreetingAndForwarding mCallback;
    MediaPlayer.OnCompletionListener CompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {

            mCallback.playPauseRecord(false);

            ChronometerPauseOn = 0;

            MediaPlayerPauseOn = 0;

            myChronometer.stop();

            mediaPlayer.stop();

            mediaPlayer.release();

            audio_stop.setVisibility(View.GONE);

            audio_play.setVisibility(View.VISIBLE);

            audio_replace.setEnabled(true);

            audio_replace.setBackgroundResource(R.drawable.phone_greeting_btn_shape);

            audio_replace.setTextColor(getResources().getColor(R.color.colorPrimary));

            greeting_switch.setClickable(true);

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.greeting, container, false);

        sqliteDataBaseHelper = new SqliteDataBaseHelper(getContext());

        prepareComponents();

        setupGreetingUserInterface();

        return rootView;
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (GreetingAndForwarding) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IFragmentToActivity");
        }
    }

    private void prepareComponents() {

        message = (TextView) rootView.findViewById(R.id.message);

        myChronometer = (Chronometer) rootView.findViewById(R.id.chronometer);

        myChronometer.setVisibility(View.GONE);

        mobilenumber = (TextView) rootView.findViewById(R.id.mobilenumber);

        recordBtn = (Button) rootView.findViewById(R.id.recordBtn);
        recordBtn.setOnClickListener(this);

        stopbtn = (Button) rootView.findViewById(R.id.stopbtn);
        stopbtn.setOnClickListener(this);

        audio_play = (Button) rootView.findViewById(R.id.audio_play);
        audio_play.setOnClickListener(this);

        audio_stop = (Button) rootView.findViewById(R.id.audio_stop);
        audio_stop.setOnClickListener(this);

        audio_replace = (Button) rootView.findViewById(R.id.audio_replace);
        audio_replace.setOnClickListener(this);

        greeting_switch = (SwitchCompat) rootView.findViewById(R.id.greeting_switch);
        greeting_switch.setOnClickListener(this);

        play_and_stop = (LinearLayout) rootView.findViewById(R.id.play_and_stop);
    }

    private void setupGreetingUserInterface() {

        SharedPreferences pref = getContext().getSharedPreferences("UserData", MODE_PRIVATE);

        String orgnizationName = pref.getString("Name", "");

        String str = "Record a simple message such as: &quot;Hello&#44 thanks for calling " + orgnizationName + "&#46;&quot;";

        //message.setText(str);
        message.setText(Html.fromHtml(str));

        File file = new File(dir, "greeting.mp3");

        greetingdata = sqliteDataBaseHelper.getPhoneGreetings();

        if (greetingdata.size() > 0) {

            //get mobile number
            mobilenumber.setText(greetingdata.get(0));

            // get greeting switch is checked .?
            if (greetingdata.get(3).equals("true")) {

                greeting_switch.setChecked(true);
                mCallback.greetingDataFromFragment(true);
                // if greeting file exist
                if (file.exists()) {

                    myChronometer.setVisibility(View.VISIBLE);

                    play_and_stop.setVisibility(View.VISIBLE);

                    stopbtn.setVisibility(View.GONE);

                    recordBtn.setVisibility(View.GONE);

                    try {
                        mediaPlayer = new MediaPlayer();

                        mediaPlayer.setDataSource(file.getPath());

                        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                        mmr.setDataSource(file.getPath());

                        long millis = Long.parseLong(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

                        mmr.release();

                        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                                TimeUnit.MILLISECONDS.toSeconds(millis));

                        myChronometer.setText(hms);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {

                    myChronometer.setVisibility(View.GONE);

                    play_and_stop.setVisibility(View.GONE);

                    stopbtn.setVisibility(View.GONE);

                    recordBtn.setVisibility(View.VISIBLE);

                    recordBtn.setEnabled(true);

                    recordBtn.setBackgroundResource(R.drawable.phone_greeting_btn_shape);

                    recordBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            } else {

                greeting_switch.setChecked(false);

                mCallback.greetingDataFromFragment(false);

                recordBtn.setBackgroundResource(R.drawable.phone_greeting_btn_shape_disable);

                myChronometer.setVisibility(View.GONE);

                play_and_stop.setVisibility(View.GONE);

                stopbtn.setVisibility(View.GONE);

                recordBtn.setVisibility(View.VISIBLE);

                recordBtn.setEnabled(false);

                recordBtn.setTextColor(getResources().getColor(R.color.colorgrey));
            }
        } else {
            insertGreeting();
        }
    }

    // first time if use not setup any greeting data then insert data from profile.
    private void insertGreeting() {
        String MindMeMobile = sqliteDataBaseHelper.getPhone();
        String UserMobile = sqliteDataBaseHelper.getUserMobileNumber();
        sqliteDataBaseHelper.InsertIntoPhoneGreeting(MindMeMobile, UserMobile);
        setupGreetingUserInterface();
    }

    // first time if use not setup any greeting data then insert data from profile.
    private void updateSwitchStatus(boolean greetingSwitch) {
        // get greeting switch is checked .?
        if (greetingSwitch) {
            greeting_switch.setChecked(true);
            mCallback.greetingDataFromFragment(true);
            File file = new File(dir, "greeting.mp3");
            // if greeting file exist
            if (file.exists()) {

                myChronometer.setVisibility(View.VISIBLE);

                play_and_stop.setVisibility(View.VISIBLE);

                stopbtn.setVisibility(View.GONE);

                recordBtn.setVisibility(View.GONE);

                try {
                    mediaPlayer = new MediaPlayer();

                    mediaPlayer.setDataSource(file.getPath());

                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(file.getPath());

                    long millis = Long.parseLong(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

                    mmr.release();

                    String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                            TimeUnit.MILLISECONDS.toSeconds(millis));

                    myChronometer.setText(hms);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {

                myChronometer.setVisibility(View.GONE);

                play_and_stop.setVisibility(View.GONE);

                stopbtn.setVisibility(View.GONE);

                recordBtn.setVisibility(View.VISIBLE);

                recordBtn.setEnabled(true);

                recordBtn.setBackgroundResource(R.drawable.phone_greeting_btn_shape);

                recordBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        } else {

            greeting_switch.setChecked(false);

            mCallback.greetingDataFromFragment(false);

            recordBtn.setBackgroundResource(R.drawable.phone_greeting_btn_shape_disable);

            myChronometer.setVisibility(View.GONE);

            play_and_stop.setVisibility(View.GONE);

            stopbtn.setVisibility(View.GONE);

            recordBtn.setVisibility(View.VISIBLE);

            recordBtn.setEnabled(false);

            recordBtn.setTextColor(getResources().getColor(R.color.colorgrey));
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.greeting_switch:
                updateSwitchStatus(greeting_switch.isChecked());
                break;
            case R.id.recordBtn:
                mCallback.checkPermissions(20);
                break;
            case R.id.stopbtn:
                stopAudioRecording();
                break;
            case R.id.audio_play:
                playAudio();
                break;
            case R.id.audio_stop:
                stopAudioPlaying();
                break;
            case R.id.audio_replace:
                showConfirmAlert();
                break;
        }
    }

    private void playAudio() {

        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnCompletionListener(CompletionListener);

        File AudioPlayFile = new File(dir, "greeting.mp3");
        if (AudioPlayFile.exists()) {

            try {

                mediaPlayer.setDataSource(AudioPlayFile.getPath());

                myChronometer.setBase(SystemClock.elapsedRealtime() + ChronometerPauseOn);

                mediaPlayer.prepare();

                mediaPlayer.start();

                mediaPlayer.seekTo(MediaPlayerPauseOn);

                myChronometer.start();

                mCallback.playPauseRecord(true);

                audio_stop.setVisibility(View.VISIBLE);
                audio_play.setVisibility(View.GONE);
                audio_replace.setEnabled(false);

                audio_replace.setBackgroundResource(R.drawable.phone_greeting_btn_shape_disable);
                audio_replace.setTextColor(getResources().getColor(R.color.colorgrey));
                greeting_switch.setClickable(false);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "Error While Playing audio file : greeting.mp3 does not Exists ");
        }
    }

    private void stopAudioPlaying() {

        MediaPlayerPauseOn = mediaPlayer.getCurrentPosition();

        mediaPlayer.pause();

        ChronometerPauseOn = myChronometer.getBase() - SystemClock.elapsedRealtime();

        myChronometer.stop();

        mCallback.playPauseRecord(false);

        audio_stop.setVisibility(View.GONE);

        audio_play.setVisibility(View.VISIBLE);

        audio_replace.setEnabled(true);

        audio_replace.setBackgroundResource(R.drawable.phone_greeting_btn_shape);

        audio_replace.setTextColor(getResources().getColor(R.color.colorPrimary));

        greeting_switch.setClickable(true);
    }

    private void stopAudioRecording() {
        myChronometer.stop();
        stopbtn.setVisibility(View.GONE);
        play_and_stop.setVisibility(View.VISIBLE);
        isRecording = false;
        greeting_switch.setClickable(true);
        mCallback.playPauseRecord(false);
    }

    private void showConfirmAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setMessage("Do you want to replace your recording.");
        alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mCallback.greetingDeleted(true);
                mCallback.checkPermissions(21);
            }
        });
        alertDialog.setNegativeButton("Keep", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void startRecordingAudio() {
        myChronometer.setVisibility(View.VISIBLE);
        myChronometer.setBase(SystemClock.elapsedRealtime());
        myChronometer.start();
        stopbtn.setVisibility(View.VISIBLE);
        recordBtn.setVisibility(View.GONE);
        if (!isRecording) {
            new Thread() {
                @Override
                public void run() {
                    mCallback.playPauseRecord(true);
                    isRecording = true;
                    greeting_switch.setClickable(false);
                    startRecording();
                }
            }.start();

        } else
            Toast.makeText(getContext(), "Already recording", Toast.LENGTH_SHORT).show();
    }

    private void startRecording() {

        File AudioFile = new File(dir, "greeting.mp3");
        if (AudioFile.exists()) {
            AudioFile.delete();
        }
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
        try {
            outputStream = new FileOutputStream(new File(dir, "greeting.mp3"));
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
            //  addLog("reading to short array buffer, buffer sze- " + minBuffer);
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

    public void deleteRecordingAudio() {
        myChronometer.setBase(SystemClock.elapsedRealtime());
        ChronometerPauseOn = 0;
        File file = new File(dir, "greeting.mp3");
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                setupGreetingUserInterface();
            } else {
                Log.e(TAG, "Error While Deleting file : Error  Type  --->No such a file greeting.mp3");
            }
        } else {
            Log.e(TAG, "Error While Deleting file : Error  Type  --->No such a file greeting.mp3");
        }
    }

    public void fragmentCommunication() {

        ChronometerPauseOn = 0;

        MediaPlayerPauseOn = 0;

        stopAudioRecording();

        if (mediaPlayer != null)
            stopAudioPlaying();

        //mediaPlayer = new MediaPlayer();

    }
}
