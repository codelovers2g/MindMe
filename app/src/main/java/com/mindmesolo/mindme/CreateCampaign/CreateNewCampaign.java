package com.mindmesolo.mindme.CreateCampaign;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.CustomViewPager;
import com.mindmesolo.mindme.helper.SwipeDirection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by srn on 6/22/2016.
 */
public class CreateNewCampaign extends AppCompatActivity implements IFragmentToActivity {

    private static final String TAG = "CreateNewCampaign";
    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;
    TextView TotalSelectedContacts;
    ArrayList<String> AllSelectedContacts = new ArrayList<String>();
    ArrayList<String> SelectedListsContacts = new ArrayList<String>();
    ArrayList<String> SelectedInterestsContacts = new ArrayList<String>();
    ArrayList<String> SelectedTagsContacts = new ArrayList<String>();
    ArrayList DateFilterContacts = new ArrayList<String>();
    ArrayList<String> SelectedListNames = new ArrayList<String>();
    ArrayList<String> SelectedInterestNames = new ArrayList<String>();
    ArrayList<String> SelectedTagNames = new ArrayList<String>();
    ArrayList FinalFilterContacts = new ArrayList<String>();
    String AllContacts = null;
    Button btnContinue;
    CustomViewPager viewPager;
    boolean DateFilterIsApplied = false;
    private CreateCampaignPagerAdapter adapter;
    private Dialog dialogue_custom;
    private TabLayout tabLayout;
    private MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_campaign);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TotalSelectedContacts = (TextView) findViewById(R.id.textView3);

        btnContinue = (Button) findViewById(R.id.btnContinue);

        ArrayList<String> tabs = new ArrayList<>();
        tabs.add("Lists");
        tabs.add("Interests");
        tabs.add("Tags");

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (CustomViewPager) findViewById(R.id.pager);

        adapter = new CreateCampaignPagerAdapter(getSupportFragmentManager(), tabs);

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        ///get values back
        if (savedInstanceState != null) {
            Log.i(TAG, "SelectedListsContacts  save state method called");

            AllSelectedContacts = savedInstanceState.getStringArrayList("AllSelectedContacts");
            SelectedListsContacts = savedInstanceState.getStringArrayList("SelectedListsContacts");
            SelectedInterestsContacts = savedInstanceState.getStringArrayList("SelectedInterestsContacts");
            SelectedTagsContacts = savedInstanceState.getStringArrayList("SelectedTagsContacts");
            DateFilterContacts = savedInstanceState.getStringArrayList("DateFilterContacts");
            SelectedListNames = savedInstanceState.getStringArrayList("SelectedListNames");
            SelectedInterestNames = savedInstanceState.getStringArrayList("SelectedInterestNames");
            SelectedTagNames = savedInstanceState.getStringArrayList("SelectedTagNames");
            FilterContacts();
        }

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject recipientKey = getRecipientKey();

                pref = getPreferences(MODE_PRIVATE);

                CreateNewCampaign.editor = pref.edit();

                CreateNewCampaign.editor.putString(recipientKey.toString(), null);

                if (FinalFilterContacts.size() <= 0) {
                    getCustomDialogue("Choose Recipients", "You have not selected any recipients to receive your campaign.");
                } else {
                    Intent intent = new Intent(getBaseContext(), CreateCampaignChooseRoute.class);
                    intent.putCharSequenceArrayListExtra("filterContacts", FinalFilterContacts);
                    intent.putExtra("recipientKeyNames", recipientKey.toString());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calander, menu);
        item = menu.findItem(R.id.calander);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.calander) {
            Intent intent = new Intent(getBaseContext(), CreateCampaignDateFilters.class);
            intent.putCharSequenceArrayListExtra("myfilterContacts", FinalFilterContacts);
            intent.putCharSequenceArrayListExtra("myfilterstats", DateFilterContacts);
            startActivityForResult(intent, 1);
        }
        //on calender click event start here ///
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void communicateToFragment2() {
        CreateCampaignInterestsTab fragment = (CreateCampaignInterestsTab) adapter.getFragment(1);
        if (fragment != null) {
            fragment.fragmentCommunication();
        } else {
            Log.i(TAG, "Fragment 2 is not initialized");
        }
    }

    @Override
    public void getAllDateFilterContacts(ArrayList data) {
        DateFilterContacts = data;
        if (AllSelectedContacts.size() > 0) {
            getAllContacts(AllSelectedContacts);
        } else {
            FilterContacts();
        }
    }

    @Override
    public void getAllContacts(ArrayList data) {
        if (data.size() > 0) {
            ArrayList tempArrayList = new ArrayList();
            tempArrayList.addAll(data);
            AllSelectedContacts.clear();
            AllSelectedContacts.addAll(tempArrayList);
            viewPager.setAllowedSwipeDirection(SwipeDirection.none);
            AllContacts = "ALL";
            // check if date filter is applied
            if (DateFilterContacts.size() > 0) {
                ArrayList tempList = new ArrayList();
                tempList.addAll(data);
                tempList.retainAll(DateFilterContacts);
                FinalFilterContacts.clear();
                FinalFilterContacts.addAll(tempList);
            } else {
                FinalFilterContacts.clear();
                FinalFilterContacts.addAll(data);
            }
            String filterContacts = String.valueOf(FinalFilterContacts.size());
            TotalSelectedContacts.setText(filterContacts);
            tabLayout.setEnabled(false);
            LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
            tabStrip.setEnabled(false);
            for (int i = 0; i < tabStrip.getChildCount(); i++) {
                tabStrip.getChildAt(i).setClickable(false);
            }
        } else {
            AllContacts = null;
            AllSelectedContacts.clear();
            FinalFilterContacts.clear();
            viewPager.setAllowedSwipeDirection(SwipeDirection.all);
            TotalSelectedContacts.setText("0");
            tabLayout.setEnabled(true);
            LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
            tabStrip.setEnabled(true);
            for (int i = 0; i < tabStrip.getChildCount(); i++) {
                tabStrip.getChildAt(i).setClickable(true);
            }

        }
    }

    @Override
    public void getListData(ArrayList data, ArrayList names) {
        SelectedListNames = names;
        SelectedListsContacts = data;
        FilterContacts();
    }

    @Override
    public void getInterestData(ArrayList data, ArrayList names) {
        SelectedInterestNames = names;
        SelectedInterestsContacts = data;
        FilterContacts();
    }

    @Override
    public void getTagData(ArrayList data, ArrayList names) {
        SelectedTagNames = names;
        SelectedTagsContacts = data;
        FilterContacts();
    }

    public void FilterContacts() {

        ArrayList TempContactsList = new ArrayList();

        ArrayList<String> tempList = new ArrayList<>();

        ArrayList<String> tempInterests = new ArrayList<>();

        if ((SelectedListsContacts.size() > 0) && (SelectedInterestsContacts.size() < 1) && (SelectedTagsContacts.size() < 1)) {

            TempContactsList.addAll(SelectedListsContacts);

        } else if ((SelectedListsContacts.size() > 0) && (SelectedInterestsContacts.size() > 0) && (SelectedTagsContacts.size() < 1)) {

            tempList.addAll(SelectedListsContacts);

            tempList.retainAll(SelectedInterestsContacts);

            TempContactsList.addAll(tempList);

        } else if ((SelectedListsContacts.size() > 0) && (SelectedInterestsContacts.size() > 0) && (SelectedTagsContacts.size() > 0)) {

            tempList.addAll(SelectedListsContacts);

            tempList.retainAll(SelectedInterestsContacts);

            tempList.retainAll(SelectedTagsContacts);

            TempContactsList.addAll(tempList);

        } else if ((SelectedListsContacts.size() < 1) && (SelectedInterestsContacts.size() < 1) && (SelectedTagsContacts.size() > 0)) {

            TempContactsList.addAll(SelectedTagsContacts);

        } else if ((SelectedListsContacts.size() < 1) && (SelectedInterestsContacts.size() > 0) && (SelectedTagsContacts.size() > 0)) {

            tempInterests.addAll(SelectedInterestsContacts);

            tempInterests.retainAll(SelectedTagsContacts);

            TempContactsList.addAll(tempInterests);

        } else if ((SelectedListsContacts.size() > 0) && (SelectedInterestsContacts.size() < 1) && (SelectedTagsContacts.size() > 0)) {

            tempList.addAll(SelectedListsContacts);

            tempList.retainAll(SelectedTagsContacts);

            TempContactsList.addAll(tempList);

        } else if ((SelectedListsContacts.size() < 1) && (SelectedInterestsContacts.size() > 0) && (SelectedTagsContacts.size() < 1)) {

            TempContactsList.addAll(SelectedInterestsContacts);

        }
        //----------------date-filter----------------//
        if ((DateFilterContacts.size() > 0) && (TempContactsList.size() > 0)) {
            TempContactsList.retainAll(DateFilterContacts);
        }

        FinalFilterContacts.clear();

        FinalFilterContacts.addAll(TempContactsList);

        TotalSelectedContacts.setText(String.valueOf(FinalFilterContacts.size()));
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putStringArrayList("AllSelectedContacts", AllSelectedContacts);
        state.putStringArrayList("SelectedListsContacts", SelectedListsContacts);
        state.putStringArrayList("SelectedInterestsContacts", SelectedInterestsContacts);
        state.putStringArrayList("SelectedTagsContacts", SelectedTagsContacts);
        state.putStringArrayList("DateFilterContacts", DateFilterContacts);
        state.putStringArrayList("SelectedListNames", SelectedListNames);
        state.putStringArrayList("SelectedInterestNames", SelectedInterestNames);
        state.putStringArrayList("SelectedTagNames", SelectedTagNames);
        //state.putCharSequenceArrayList("savedList", SelectedTagNames);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            ArrayList result = data.getCharSequenceArrayListExtra("result");

            getAllDateFilterContacts(result);

            if (result.size() > 0) {
                DateFilterIsApplied = true;
                item.setIcon(R.drawable.calendar_check);
            } else {
                DateFilterIsApplied = false;
                item.setIcon(R.drawable.calendar);
            }
        }
    }

    private JSONObject getRecipientKey() {
        JSONObject recipientKey = new JSONObject();
        if (AllContacts != null) {
            try {
                recipientKey.put("All", "All");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                if (SelectedTagsContacts.size() > 0) {
                    recipientKey.put("tags", arrayListToStringArray(SelectedTagNames));
                }
                if (SelectedInterestsContacts.size() > 0) {
                    recipientKey.put("interests", arrayListToStringArray(SelectedInterestNames));
                }
                if (SelectedListsContacts.size() > 0) {
                    recipientKey.put("lists", getListArray(SelectedListNames, "LIST"));
                    recipientKey.put("custom", getListArray(SelectedListNames, "CUSTOM"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return recipientKey;
    }

    private JSONArray getListArray(ArrayList<String> selectedListNames, String Type) {
        JSONArray ListJsonArray = new JSONArray();
        ArrayList contactsTypes = new ArrayList();
        contactsTypes.add("LEAD");
        contactsTypes.add("TRIAL");
        contactsTypes.add("CUSTOMER");
        contactsTypes.add("INACTIVE");
        contactsTypes.add("PERSONAL");
        contactsTypes.add("OTHER");
        switch (Type) {
            case "LIST":
                for (String selectedListName : selectedListNames) {
                    if (!contactsTypes.contains(selectedListName.toUpperCase())) {
                        ListJsonArray.put(selectedListName);
                    }
                }
                break;
            case "CUSTOM":
                for (String selectedListName : selectedListNames) {
                    if (contactsTypes.contains(selectedListName.toUpperCase())) {
                        ListJsonArray.put(selectedListName);
                    }
                }
                break;
        }
        return ListJsonArray;
    }

    private JSONArray arrayListToStringArray(ArrayList<String> data) {
        JSONArray tempJsonArray = new JSONArray();
        for (String Contact : data) {
            tempJsonArray.put(Contact);
        }
        return tempJsonArray;
    }

    public void getCustomDialogue(String AlertTitle, String AlertMessage) {

        dialogue_custom = new Dialog(CreateNewCampaign.this);

        dialogue_custom.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialogue_custom.setContentView(R.layout.campaign_dialog_layout);

        Button btn_ok_dialogue = (Button) dialogue_custom.findViewById(R.id.btn_ok_dialogue);

        TextView title = (TextView) dialogue_custom.findViewById(R.id.title);

        title.setText(AlertTitle);

        TextView message = (TextView) dialogue_custom.findViewById(R.id.message);
        message.setText(AlertMessage);
        btn_ok_dialogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogue_custom.dismiss();
            }
        });
        dialogue_custom.show();
    }
}
