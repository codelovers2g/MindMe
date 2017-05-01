package com.mindmesolo.mindme.CreateCampaign;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.ViewCampaigns.CampaignData;
import com.mindmesolo.mindme.helper.DataHelper;
import com.mindmesolo.mindme.helper.OnStartDragListener;
import com.mindmesolo.mindme.helper.RuntimePermissionsActivity;
import com.mindmesolo.mindme.helper.SimpleItemTouchHelperCallback;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;
import com.mindmesolo.mindme.helper.ToastShow;
import com.naman14.androidlame.AndroidLame;
import com.naman14.androidlame.LameBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by User1 on 7/8/2016.
 */
public class CreateVoiceCampaign extends RuntimePermissionsActivity implements View.OnClickListener, OnStartDragListener {

    static final String TAG = "MediaRecording";

    private static final int REQUEST_PERMISSIONS = 20;
    private static String OrganizationId;
    public TextView textViewCta, textViewhelptext;
    public ImageButton imageButtonAdd;
    Button buttonRecord, buttonStop, buttonReplace, buttonPlay, buttonStopPlaying;
    LinearLayout linearLayoutNewRecord;
    Button buttonContinue;
    Chronometer myChronometer;
    DataHelper dataHelper = new DataHelper();
    int minBuffer;
    int inSamplerate = 8000;
    AudioRecord audioRecord;
    AndroidLame androidLame;
    FileOutputStream outputStream;
    boolean isRecording = false;
    boolean isPlaying = false;
    MediaPlayer mediaPlayer;
    String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
    JSONArray jsonArrayComponent = new JSONArray();
    JSONObject jsonObjectFinalCampaign = new JSONObject();
    int MediaPlayerPauseOn = 0;
    long ChronometerPauseOn = 0;
    String AudioFileName = null;
    private ArrayList<CampaignData> List = new ArrayList<CampaignData>();
    private RecyclerView recyclerView;
    private AudioVideoAdapter audioVideoAdapter;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.createvoice);

        OrganizationId = new SqliteDataBaseHelper(getBaseContext()).getOrganizationId();

        linearLayoutNewRecord = (LinearLayout) findViewById(R.id.newRecord);

        myChronometer = (Chronometer) findViewById(R.id.chronometer);

        buttonRecord = (Button) findViewById(R.id.recordBtn);
        buttonRecord.setOnClickListener(this);

        buttonStop = (Button) findViewById(R.id.stopbtn);
        buttonStop.setOnClickListener(this);

        buttonReplace = (Button) findViewById(R.id.replaceBtn);
        buttonReplace.setOnClickListener(this);

        imageButtonAdd = (ImageButton) findViewById(R.id.Add);
        imageButtonAdd.setOnClickListener(this);

        buttonPlay = (Button) findViewById(R.id.playBtn);
        buttonPlay.setOnClickListener(this);

        buttonStopPlaying = (Button) findViewById(R.id.StopPlaying);
        buttonStopPlaying.setOnClickListener(this);

        buttonContinue = (Button) findViewById(R.id.btnContinue);
        buttonContinue.setOnClickListener(this);

        textViewhelptext = (TextView) findViewById(R.id.helpText);

        textViewCta = (TextView) findViewById(R.id.ctaText);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        String series = getIntent().getStringExtra("Series");

        if (series.equals("yes")) {
            imageButtonAdd.setVisibility(View.GONE);
            textViewCta.setVisibility(View.GONE);
        }

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
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Add:
                if (isRecording == true) {
                    StopRecordingAudio();
                    ToastShow.setText(getBaseContext(), "Recording stopped.", Toast.LENGTH_LONG);
                } else if (isPlaying == true) {
                    StopPlayingAudio();
                    ToastShow.setText(getBaseContext(), "Playing audio stopped.", Toast.LENGTH_LONG);
                }
                Intent intent = new Intent(getBaseContext(), CreateEmailOption.class);
                intent.putExtra("Message", "Voice");
                startActivityForResult(intent, 1);
                break;
            case R.id.recordBtn:
                //Check  api permission is required
                if (Build.VERSION.SDK_INT >= 23) {
                    CreateVoiceCampaign.super.requestAppPermissions(new String[]{
                                    Manifest.permission.RECORD_AUDIO,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            R.string.runtime_permissions_txt, REQUEST_PERMISSIONS);
                } else {
                    StartRecordingAudio();
                }
                break;
            case R.id.stopbtn:
                StopRecordingAudio();
                break;
            case R.id.replaceBtn:
                showConfirmAlert();
                break;
            case R.id.playBtn:
                StartPlayingAudio();
                break;
            case R.id.StopPlaying:
                StopPlayingAudio();
                break;
            case R.id.btnContinue:
                finalAction();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer = null;
        isPlaying = false;
        isRecording = false;
    }

    // if recording permissions granted then start recording voice.
    @Override
    public void onPermissionsGranted(int requestCode) {
        StartRecordingAudio();
    }

    private void StartRecordingAudio() {
        buttonRecord.setVisibility(View.GONE);
        buttonStop.setVisibility(View.VISIBLE);
        myChronometer.setBase(SystemClock.elapsedRealtime());
        myChronometer.start();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String result = data.getStringExtra("result");
            switch (requestCode) {
                case 1:
                    if (result != null) {
                        updateListView(data);
                    } else {
                        updateListView3(data);
                    }
                    break;
                case 2:
                    try {
                        CampaignData ListItem = audioVideoAdapter.List.get(audioVideoAdapter.getPosition());
                        ListItem.setExtraData(result);
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
        imageButtonAdd.setVisibility(View.VISIBLE);
        textViewCta.setVisibility(View.VISIBLE);
    }

    private void StartPlayingAudio() {

        mediaPlayer = new MediaPlayer();

        try {

            File AudioPlayFile = new File(dir, AudioFileName);

            if (AudioPlayFile.exists()) {

                isPlaying = true;

                mediaPlayer.setDataSource(AudioPlayFile.getPath());

                myChronometer.setBase(SystemClock.elapsedRealtime() + ChronometerPauseOn);

                mediaPlayer.prepare();

                mediaPlayer.start();

                mediaPlayer.seekTo(MediaPlayerPauseOn);

                myChronometer.start();

                buttonReplace.setEnabled(false);
                buttonPlay.setVisibility(View.GONE);
                buttonStopPlaying.setVisibility(View.VISIBLE);
                buttonReplace.setBackgroundResource(R.drawable.phone_greeting_btn_shape_disable);
                buttonReplace.setTextColor(getResources().getColor(R.color.colorgrey));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                buttonPlay.setVisibility(View.VISIBLE);

                buttonStopPlaying.setVisibility(View.GONE);

                ChronometerPauseOn = 0;

                MediaPlayerPauseOn = 0;

                myChronometer.stop();

                isPlaying = false;

                mediaPlayer.stop();

                mediaPlayer.release();

                buttonReplace.setEnabled(true);
                buttonReplace.setBackgroundResource(R.drawable.phone_greeting_btn_shape);
                buttonReplace.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });

    }

    private void StopPlayingAudio() {

        isPlaying = false;

        buttonPlay.setVisibility(View.VISIBLE);

        buttonStopPlaying.setVisibility(View.GONE);

        MediaPlayerPauseOn = mediaPlayer.getCurrentPosition();

        mediaPlayer.pause();

        ChronometerPauseOn = myChronometer.getBase() - SystemClock.elapsedRealtime();

        myChronometer.stop();

        buttonReplace.setEnabled(true);
        buttonReplace.setBackgroundResource(R.drawable.phone_greeting_btn_shape);
        buttonReplace.setTextColor(getResources().getColor(R.color.colorPrimary));
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

        try {
            File directory = new File(dir, "MindMe/Recordings/");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            AudioFileName = "MindMe/Recordings/" + String.valueOf(System.currentTimeMillis()) + ".mp3";

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

    private void StopRecordingAudio() {
        myChronometer.stop();
        buttonStop.setVisibility(View.GONE);
        linearLayoutNewRecord.setVisibility(View.VISIBLE);
        isRecording = false;
    }

    //update ListItems if user add new item in ListView
    public void updateListView(Intent intentData) {

        CampaignData AddNewItem = new CampaignData();

        String ExtraData = intentData.getStringExtra("result");

        switch (intentData.getStringExtra("Title")) {
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
        }
        imageButtonAdd.setVisibility(View.GONE);
        textViewCta.setVisibility(View.GONE);
        audioVideoAdapter.notifyDataSetChanged();
    }

    //update ListItems if user add new item in ListView
    public void updateListView3(Intent intentData) {
        CampaignData AddNewItem = new CampaignData();
        switch (intentData.getStringExtra("Title")) {
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
        textViewCta.setVisibility(View.GONE);
        audioVideoAdapter.notifyDataSetChanged();
    }

    private void showConfirmAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateVoiceCampaign.this);
        alertDialog.setMessage("Do you want to replace your recording.");
        alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                myChronometer.setBase(SystemClock.elapsedRealtime());
                linearLayoutNewRecord.setVisibility(View.GONE);
                buttonRecord.setVisibility(View.VISIBLE);
                File AudioFile = new File(dir, AudioFileName);
                if (AudioFile.exists()) {
                    boolean deleted = AudioFile.delete();
                    if (deleted) {
                        Log.e(TAG, "VoiceCampaignAudio.mp3 ........deleted");
                    } else {
                        Log.e(TAG, "Error While Deleting file : Error  Type  --->No such a file greeting.mp3");
                    }
                } else {
                    Log.e(TAG, "Error While Deleting file : Error  Type  --->No such a file greeting.mp3");
                }
            }
        });
        alertDialog.setNegativeButton("Keep", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void finalAction() {

        if (isRecording == true) {

            ToastShow.setText(getBaseContext(), "Please stop recording.", Toast.LENGTH_LONG);

        } else if (isPlaying == true) {

            ToastShow.setText(getBaseContext(), "Please stop playing audio.", Toast.LENGTH_LONG);

        } else {

            jsonArrayComponent = new JSONArray();

            jsonObjectFinalCampaign = new JSONObject();

            if (AudioFileName != null) {

                File AudioFile = new File(dir, AudioFileName);

                if (AudioFile.exists()) {

                    String finalObject = getIntent().getExtras().getString("ChooseRouteObject");

                    String component = getIntent().getExtras().getString("Components");

                    JSONObject jsonObject = new JSONObject();
                    try {
                        if (component != null) {
                            jsonArrayComponent = new JSONArray(component);
                        } else {
                            jsonArrayComponent = new JSONArray();

                        }

                        jsonObjectFinalCampaign = new JSONObject(finalObject);

                        getAllComponents();

                        jsonObject.put("mediaFileName", String.valueOf(System.currentTimeMillis()) + ".mp3");

                        jsonObject.put("media", AudioFile.getAbsolutePath());

                        jsonObject.put("type", "VOICERECORDING");

                        jsonObject.put("status", "ACTIVE");

                        jsonObject.put("order", "1");

                        jsonObject.put("orgId", OrganizationId);
                        jsonArrayComponent.put(jsonObject);
                        Intent intent1 = new Intent(getBaseContext(), CreateCampaignPreviewScreen.class);
                        intent1.putExtra("ChooseRouteObject", jsonObjectFinalCampaign.toString());
                        intent1.putExtra("Components", jsonArrayComponent.toString());
                        startActivity(intent1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastShow.setText(getBaseContext(), "Please record voice message.", Toast.LENGTH_LONG);
                }
            } else {
                ToastShow.setText(getBaseContext(), "Please record voice message.", Toast.LENGTH_LONG);
            }
        }
    }

    private void getAllComponents() {
        int i = 2;
        for (CampaignData data : audioVideoAdapter.List) {
            if (data.getExtraData() != null) {
                try {
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
}
