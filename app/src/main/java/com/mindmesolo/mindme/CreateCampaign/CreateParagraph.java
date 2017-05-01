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
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.ToastShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User1 on 7/4/2016.
 */
public class CreateParagraph extends AppCompatActivity implements View.OnClickListener {

    ToggleButton toggleButtonOne, toggleButtonTwo;

    LinearLayout linearLayouttwo;

    ToggleButton left, center, right;

    EditText editTextone, editTexttwo1, editTexttwo2;

    int fontSize = 15, fontWeight = 200, column = 1;

    String alignment = "center";

    SeekBar fontSizeSeekbar, fontWeightSeekbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.createparagraph);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView textView = (TextView) findViewById(R.id.toolbartxt);

        textView.setText("Add Paragraph");

        editTextone = (EditText) findViewById(R.id.editTextOne);

        editTexttwo1 = (EditText) findViewById(R.id.editTextTwo1);

        editTexttwo2 = (EditText) findViewById(R.id.editTextTwo2);

        fontSizeSeekbar = (SeekBar) findViewById(R.id.fontSizeSeekbar);

        fontWeightSeekbar = (SeekBar) findViewById(R.id.fontWeightSeekbar);

        linearLayouttwo = (LinearLayout) findViewById(R.id.linearLayouttwo);

        toggleButtonOne = (ToggleButton) findViewById(R.id.one);
        toggleButtonOne.setOnClickListener(this);
        toggleButtonOne.setChecked(true);

        toggleButtonTwo = (ToggleButton) findViewById(R.id.two);
        toggleButtonTwo.setOnClickListener(this);

        left = (ToggleButton) findViewById(R.id.left);
        left.setOnClickListener(this);

        center = (ToggleButton) findViewById(R.id.center);
        center.setOnClickListener(this);

        right = (ToggleButton) findViewById(R.id.right);
        right.setOnClickListener(this);

        fontSizeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress >= 12) {
                    fontSize = progress;
                    editTextone.setTextSize(17);
                    editTexttwo1.setTextSize(17);
                    editTexttwo2.setTextSize(17);
                } else {
                    editTextone.setTextSize(17);
                    editTexttwo1.setTextSize(17);
                    editTexttwo2.setTextSize(17);
                    fontSize = progress;
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
            int progress = 200;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                fontWeight = progress;
                if (progress >= 200 && progress <= 300) {
                    editTextone.setTypeface(null, Typeface.NORMAL);
                    editTexttwo1.setTypeface(null, Typeface.NORMAL);
                    editTexttwo2.setTypeface(null, Typeface.NORMAL);
                }
                if (progress >= 300) {
                    editTextone.setTypeface(null, Typeface.BOLD);
                    editTexttwo1.setTypeface(null, Typeface.BOLD);
                    editTexttwo2.setTypeface(null, Typeface.BOLD);
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

                JSONArray propertiesArray = jsonObject.getJSONArray("properties");

                //String progress1 = propertiesArray.getJSONObject(0).getString("value");

                //progress1.substring(0, progress1.length() - 2);
                //progress1.replaceAll(s2, "")

//                int progress = Integer.parseInt(progress1);
//
//                if (progress >= 12) {
//                    editTextone.setTextSize(17);
//                    editTexttwo1.setTextSize(17);
//                    editTexttwo2.setTextSize(17);
//                } else {
//                    editTextone.setTextSize(17);
//                    editTexttwo1.setTextSize(17);
//                    editTexttwo2.setTextSize(17);
//                }
//
//                fontSizeSeekbar.setProgress(progress);

                int valFontWeight = propertiesArray.getJSONObject(1).getInt("value");

                fontWeightSeekbar.setProgress(valFontWeight);

//                if (valFontWeight >= 1 && valFontWeight <= 20) {
//                    editTextone.setTypeface(null, Typeface.NORMAL);
//                    editTexttwo1.setTypeface(null, Typeface.NORMAL);
//                    editTexttwo2.setTypeface(null, Typeface.NORMAL);
//                }
//                if (valFontWeight >= 20) {
//                    editTextone.setTypeface(null, Typeface.BOLD);
//                    editTexttwo1.setTypeface(null, Typeface.BOLD);
//                    editTexttwo2.setTypeface(null, Typeface.BOLD);
//                }

                switch (propertiesArray.getJSONObject(2).getString("value")) {
                    case "center":
                        center.setChecked(true);
                        alignment = "center";
                        break;
                    case "left":
                        left.setChecked(true);
                        alignment = "left";
                        break;
                    case "right":
                        right.setChecked(true);
                        alignment = "right";
                        break;
                }

                int col = propertiesArray.getJSONObject(3).getInt("value");
                if (col == 1) {
                    column = 1;
                    editTextone.setTypeface(null, Typeface.BOLD);
                    editTexttwo1.setTypeface(null, Typeface.BOLD);
                    editTexttwo2.setTypeface(null, Typeface.BOLD);
                    editTextone.setVisibility(View.VISIBLE);
                    toggleButtonOne.setChecked(true);
                    editTextone.setText(propertiesArray.getJSONObject(4).getString("value"));
                    linearLayouttwo.setVisibility(View.GONE);
                } else {
                    column = 2;
                    editTextone.setVisibility(View.GONE);
                    toggleButtonOne.setChecked(false);
                    toggleButtonTwo.setChecked(true);
                    linearLayouttwo.setVisibility(View.VISIBLE);
                    editTexttwo1.setText(propertiesArray.getJSONObject(4).getString("value"));
                    editTexttwo2.setText(propertiesArray.getJSONObject(5).getString("value"));
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
            if (toggleButtonOne.isChecked()) {
                if (editTextone.getText().length() == 0) {
                    ToastShow.setText(CreateParagraph.this, "Please enter text first", Snackbar.LENGTH_LONG);
                } else {
                    sendDataBack();
                }
            } else {
                if (editTexttwo1.getText().length() == 0 || editTexttwo2.getText().length() == 0) {
                    ToastShow.setText(CreateParagraph.this, "Please enter text first", Snackbar.LENGTH_LONG);
                } else {
                    sendDataBack();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendDataBack() {
        JSONArray jsonArrayProperties = new JSONArray();
        try {
//            jsonArrayProperties.put(new JSONObject().put("name", "fontSize").put("value", fontSize));
//            jsonArrayProperties.put(new JSONObject().put("name", "fontWeight").put("value", fontWeight));

            jsonArrayProperties.put(new JSONObject().put("name", "fontSize").put("value", "28px"));
            jsonArrayProperties.put(new JSONObject().put("name", "fontWeight").put("value", 300));
            jsonArrayProperties.put(new JSONObject().put("name", "alignment").put("value", alignment));
            jsonArrayProperties.put(new JSONObject().put("name", "columns").put("value", column));

            if (column == 2) {
                jsonArrayProperties.put(new JSONObject().put("name", "valueColumn1").put("value", editTexttwo1.getText().toString()));
                jsonArrayProperties.put(new JSONObject().put("name", "valueColumn2").put("value", editTexttwo2.getText().toString()));
            } else {
                jsonArrayProperties.put(new JSONObject().put("name", "valueColumn1").put("value", editTextone.getText().toString()));
            }
            jsonArrayProperties.put(new JSONObject().put("name", "color").put("value", "#555555"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();
        try {
            if (column == 2) {
                jsonObject.put("message", editTexttwo1.getText().toString());
            } else {
                jsonObject.put("message", editTextone.getText().toString());
            }
            jsonObject.put("properties", jsonArrayProperties);
            jsonObject.put("type", "PARAGRAPH");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", jsonObject.toString());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left:
                alignment = "left";
                left.setChecked(true);
                right.setChecked(false);
                center.setChecked(false);
                editTextone.setGravity(Gravity.LEFT);
                editTexttwo1.setGravity(Gravity.LEFT);
                editTexttwo2.setGravity(Gravity.LEFT);
                break;

            case R.id.center:
                alignment = "center";
                left.setChecked(false);
                right.setChecked(false);
                center.setChecked(true);
                editTextone.setGravity(Gravity.CENTER);
                editTexttwo1.setGravity(Gravity.CENTER);
                editTexttwo2.setGravity(Gravity.CENTER);
                break;

            case R.id.right:
                alignment = "right";
                left.setChecked(false);
                right.setChecked(true);
                center.setChecked(false);
                editTextone.setGravity(Gravity.RIGHT);
                editTexttwo1.setGravity(Gravity.RIGHT);
                editTexttwo2.setGravity(Gravity.RIGHT);
                break;

            case R.id.one:
                toggleButtonOne.setChecked(true);
                toggleButtonTwo.setChecked(false);
                editTextone.setVisibility(View.VISIBLE);
                linearLayouttwo.setVisibility(View.GONE);
                String s = editTexttwo1.getText().toString();
                editTextone.setText(s);
                column = 1;
                break;
            case R.id.two:
                toggleButtonOne.setChecked(false);
                toggleButtonTwo.setChecked(true);
                editTextone.setVisibility(View.GONE);
                linearLayouttwo.setVisibility(View.VISIBLE);
                String s2 = editTextone.getText().toString();
                editTexttwo1.setText(s2);
                column = 2;
                break;
        }
    }
}