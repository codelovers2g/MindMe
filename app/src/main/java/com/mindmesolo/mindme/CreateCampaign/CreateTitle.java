package com.mindmesolo.mindme.CreateCampaign;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.ToastShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User1 on 25/11/2016.
 */
public class CreateTitle extends AppCompatActivity implements View.OnClickListener {

    ToggleButton left, center, right;

    EditText TitleText;

    SeekBar fontSizeSeekbar, fontWeightSeekbar;

    int fontSize = 28, fontWeight = 300;

    String alignment = "center";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.addtitle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TitleText = (EditText) findViewById(R.id.TitleText);

        fontSizeSeekbar = (SeekBar) findViewById(R.id.fontSizeSeekbar);

        fontWeightSeekbar = (SeekBar) findViewById(R.id.fontWeightSeekbar);

        left = (ToggleButton) findViewById(R.id.left);
        left.setOnClickListener(this);

        center = (ToggleButton) findViewById(R.id.center);
        center.setOnClickListener(this);

        right = (ToggleButton) findViewById(R.id.right);
        right.setOnClickListener(this);

        fontSizeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (progress >= 15) {
                    fontSize = progress;
                    TitleText.setTextSize(progress);
                } else {
                    fontSize = progress;
                    TitleText.setTextSize(15);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        fontWeightSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                fontWeight = progress;
                if (progress >= 1 && progress <= 10) {
                    TitleText.setTypeface(null, Typeface.NORMAL);
                }
                if (progress > 10) {
                    TitleText.setTypeface(null, Typeface.BOLD);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        HandleResponse();
    }

    private void HandleResponse() {

        String ExtraData = getIntent().getStringExtra("ExtraData");

        if (ExtraData != null) {

            try {

                JSONObject jsonObject = new JSONObject(ExtraData);

                String titleText = jsonObject.getString("message");

                TitleText.setText(titleText);

                JSONArray properties = jsonObject.getJSONArray("properties");

//                int progress = Integer.parseInt(properties.getJSONObject(0).getString("value"));
//
//                if(progress>=15){
//                    TitleText.setTextSize(17);
//                }else {
//                    TitleText.setTextSize(17);
//                }

                fontSizeSeekbar.setProgress(properties.getJSONObject(0).getInt("value"));

                fontWeightSeekbar.setProgress(properties.getJSONObject(1).getInt("value"));

                //get alignment properties from json
                switch (properties.getJSONObject(2).getString("value")) {
                    case "center":
                        TitleText.setGravity(Gravity.CENTER);
                        alignment = "center";
                        center.setChecked(true);
                        break;
                    case "left":
                        TitleText.setGravity(Gravity.LEFT);
                        alignment = "left";
                        left.setChecked(true);
                        break;
                    case "right":
                        TitleText.setGravity(Gravity.RIGHT);
                        alignment = "right";
                        right.setChecked(true);
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            center.setChecked(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.done) {
            if (TitleText.getText().length() == 0) {
                ToastShow.setText(CreateTitle.this, "Please enter text first", Snackbar.LENGTH_LONG);
            } else {

                JSONObject jsonObject = new JSONObject();

                JSONArray properties = new JSONArray();

                try {
                    int progress = fontSizeSeekbar.getProgress();
//                    if(progress>=15)
//                    {
//                        properties.put(new JSONObject().put("name", "fontSize").put("value",progress ));
//                    }else {
//                        properties.put(new JSONObject().put("name", "fontSize").put("value",28));
//                    }
//                    properties.put(new JSONObject().put("name", "fontWeight").put("value", fontWeightSeekbar.getProgress()));
//                    properties.put(new JSONObject().put("name", "alignment").put("value", alignment));
                    properties.put(new JSONObject().put("name", "fontSize").put("value", "28px"));
                    properties.put(new JSONObject().put("name", "fontWeight").put("value", 300));
                    properties.put(new JSONObject().put("name", "alignment").put("value", alignment));
                    jsonObject.put("message", TitleText.getText().toString());
                    jsonObject.put("properties", properties);
                    jsonObject.put("type", "TITLE");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", jsonObject.toString());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left:
                alignment = "left";
                left.setChecked(true);
                center.setChecked(false);
                right.setChecked(false);
                TitleText.setGravity(Gravity.LEFT);
                break;
            case R.id.center:
                alignment = "center";
                left.setChecked(false);
                center.setChecked(true);
                right.setChecked(false);
                TitleText.setGravity(Gravity.CENTER);
                break;
            case R.id.right:
                alignment = "right";
                left.setChecked(false);
                center.setChecked(false);
                right.setChecked(true);
                TitleText.setGravity(Gravity.RIGHT);
                break;
        }
    }


}