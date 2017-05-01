package com.mindmesolo.mindme.CreateCampaign;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.helper.ItemTouchHelperAdapter;
import com.mindmesolo.mindme.helper.OnStartDragListener;
import com.mindmesolo.mindme.helper.SimpleItemTouchHelperCallback;
import com.mindmesolo.mindme.helper.SqliteDataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

/**
 * Created by eNest on 7/20/2016.
 */
public class CreateCampaignChooseRoute extends AppCompatActivity implements OnStartDragListener {

    private static final String TAG = "CreateCampaignChooseRoute";
    static ArrayList EmailContacts = new ArrayList();
    static ArrayList TextContacts = new ArrayList();
    static ArrayList VoiceContacts = new ArrayList();
    static ArrayList FinalContacts = new ArrayList();
    static String OrgnizactionId;
    static TextView messagestatus, notifcation1, notifcation2, notifcation3, notifcation5;
    static String multipleMessagesEnabled = "false";
    public TextView selectedContacts;
    RecyclerView recyclerView;
    RelativeLayout NotifcationLayout;
    SqliteDataBaseHelper dBhelper = new SqliteDataBaseHelper(this);
    ArrayList SELECTED_CONTACTS_DATA = new ArrayList();
    String recipientKeyNames;
    Button Continue;
    SwitchCompat multiRouteSupport;
    JSONArray activityobj;
    TextView messagetext, contactsText;
    private ArrayList<ChooserRouteData> ListItems = new ArrayList<>();
    private ItemTouchHelper mItemTouchHelper;
    private Dialog dialogue_custom;
    private ChooserRouteAdapter chooserRouteAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_campaign_choose_route);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);


        messagetext = (TextView) findViewById(R.id.messagetext);

        contactsText = (TextView) findViewById(R.id.contactText);

        selectedContacts = (TextView) findViewById(R.id.SelectedContactsStatus);

        Continue = (Button) findViewById(R.id.btnContinue);

        chooserRouteAdapter = new ChooserRouteAdapter(ListItems, this);

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(chooserRouteAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(chooserRouteAdapter);

        mItemTouchHelper = new ItemTouchHelper(callback);

        mItemTouchHelper.attachToRecyclerView(recyclerView);

        Bundle bundle = getIntent().getExtras();

        SELECTED_CONTACTS_DATA = bundle.getCharSequenceArrayList("filterContacts");

        recipientKeyNames = bundle.getString("recipientKeyNames");

        prepareActivityUI(SELECTED_CONTACTS_DATA);

        Continue.setOnClickListener((View v) -> {
            sendDataInIntent();
        });

    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void NotifyDataDelete() {

    }

    @Override
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

    private void prepareActivityUI(ArrayList data) {

        messagestatus = (TextView) findViewById(R.id.messagestatus);

        notifcation1 = (TextView) findViewById(R.id.notifcation);

        String sourceString2 = "Reach your contacts through <b>Email</b>,<b> Text</b> and <b>Voice</b> broadcast.";
        notifcation1.setText(Html.fromHtml(sourceString2));
        notifcation2 = (TextView) findViewById(R.id.notifcation2);
        notifcation3 = (TextView) findViewById(R.id.notifaction3);
        notifcation5 = (TextView) findViewById(R.id.notifcation5);

        multiRouteSupport = (SwitchCompat) findViewById(R.id.multyRouteSupport);

        NotifcationLayout = (RelativeLayout) findViewById(R.id.NotificationLayout);

        // setContacts
        setContactsMessage(data.size());

        EmailContacts = dBhelper.getEmailContacts(data);

        VoiceContacts = dBhelper.getVoiceContacts(data);

        TextContacts = dBhelper.getTextContacts(data);

        OrgnizactionId = dBhelper.getOrganizationId();

        ListItems.add(new ChooserRouteData("Email", String.valueOf(EmailContacts.size()), EmailContacts, 0, R.drawable.email_black, false));
        ListItems.add(new ChooserRouteData("Text", String.valueOf(TextContacts.size()), TextContacts, 1, R.drawable.text_msg, false));
        ListItems.add(new ChooserRouteData("Voice", String.valueOf(VoiceContacts.size()), VoiceContacts, 2, R.drawable.phone_call, false));

        chooserRouteAdapter.notifyDataSetChanged();

        /*Multi  route support   */
        multiRouteSupport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (multiRouteSupport.isChecked()) {
                            multipleMessagesEnabled = "true";
                            multiMessagesSupport();
                        } else {
                            multipleMessagesEnabled = "false";
                            chooserRouteAdapter.dataChangeListrnerChecked();
                        }
                    }
                }, 200);

            }
        });
    }

    private void multiMessagesSupport() {

        activityobj = new JSONArray();

        ArrayList union = new ArrayList();

        ArrayList<ChooserRouteData> selectedItems = chooserRouteAdapter.GetSelectedItems();

        for (int i = 0; i < selectedItems.size(); i++) {
            ChooserRouteData item = selectedItems.get(i);
            for (int j = 0; j < item.getTotalcontacts().size(); j++) {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("contactId", item.getTotalcontacts().get(j));
                    obj.put("orgId", OrgnizactionId);
                    obj.put("type", "SENT");
                    obj.put("routeType", getRouteSelectType(item.getTitle()));
                    activityobj.put(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            union.addAll(item.getTotalcontacts());
        }
        switch (selectedItems.size()) {
            case 1:
                ArrayList<ChooserRouteData> selectedItems1 = chooserRouteAdapter.GetSelectedItems();
                String sourceString1 = "MindMe will Deliver <b>" + selectedItems1.get(0).getTitle() + "</b> to each contact.";
                String sourceString2 = "Send <b> multiple messages </b> to each contact. You need to select more than one route. May lead to excessive opt-outs.";
                notifcation3.setText(Html.fromHtml(sourceString2));
                notifcation5.setText(Html.fromHtml(sourceString1));
                break;

            case 2:
                ArrayList<ChooserRouteData> selectedItems2 = chooserRouteAdapter.GetSelectedItems();
                String sourceString3 = "MindMe will Deliver <b>" + selectedItems2.get(0).getTitle() + "</b> and <b>" + selectedItems2.get(1).getTitle() + "</b> to each contact where available.";
                String sourceString4 = "Send <b> multiple messages </b> to each contact. You need to select more than one route. May lead to excessive opt-outs.";
                notifcation3.setText(Html.fromHtml(sourceString4));
                notifcation5.setText(Html.fromHtml(sourceString3));
                break;

            case 3:
                ArrayList<ChooserRouteData> selectedItems3 = chooserRouteAdapter.GetSelectedItems();
                String sourceString5 = "MindMe will Deliver <b>" + selectedItems3.get(0).getTitle() + "</b> ,<b>" + selectedItems3.get(1).getTitle() + "</b> and <b>" + selectedItems3.get(2).getTitle() + "</b> to each contact where available.";
                String sourceString6 = "Send <b> multiple messages </b> to each contact. You need to select more than one route. May lead to excessive opt-outs.";
                notifcation3.setText(Html.fromHtml(sourceString6));
                notifcation5.setText(Html.fromHtml(sourceString5));
                break;
        }

        chooserRouteAdapter.getUserDataByName("Email").setCount(String.valueOf(EmailContacts.size()));

        chooserRouteAdapter.getUserDataByName("Text").setCount(String.valueOf(TextContacts.size()));

        chooserRouteAdapter.getUserDataByName("Voice").setCount(String.valueOf(VoiceContacts.size()));

        FinalContacts = union;

        messagestatus.setText(String.valueOf(FinalContacts.size()));

        setContactsMessage(0);

        setMessagesText();

        chooserRouteAdapter.notifyDataSetChanged();
    }

    // send the user to the next activity for result.
    @SuppressLint("LongLogTag")
    private void sendDataInIntent() {

        Intent intent = new Intent();

        ArrayList<String> SelectedRoutes = chooserRouteAdapter.GetSelectedItems2();

        Log.i(TAG, String.valueOf(SelectedRoutes.size()));

        if (SelectedRoutes.size() > 0) {
            String NextStage = getStage(SelectedRoutes);
            switch (NextStage) {
                case "EMAIL":
                    intent = new Intent(getBaseContext(), CreateEmailCampaign.class);
                    break;
                case "TEXT":
                    intent = new Intent(getBaseContext(), CreateTextCampaign.class);
                    intent.putExtra("Series", "no");
                    break;
                case "VOICE":
                    if (SelectedRoutes.size() >= 2) {
                        intent = new Intent(getBaseContext(), CreateTextCampaign.class);
                        intent.putExtra("Series", "no");

                    } else {
                        intent = new Intent(getBaseContext(), CreateVoiceCampaign.class);
                        intent.putExtra("Series", "no");
                    }
                    break;
            }
            intent.putExtra("ChooseRouteObject", getCampaignObject().toString());
            intent.putStringArrayListExtra("NextSection", SelectedRoutes);
            startActivity(intent);
        } else {
            getCustomDialogue("Choose Route", "You not have selected any messages to send.");
        }
    }

    private String getStage(ArrayList<String> SelectedRoutes) {
        if (SelectedRoutes.contains("EMAIL")) {
            return "EMAIL";
        } else {
            return SelectedRoutes.get(0);
        }
    }

    ;

    private JSONObject getCampaignObject() {

        JSONObject finalObject = new JSONObject();

        JSONArray routeObj = GetRouteObject();

        //-----LinkedHashSet is used to store listItems Without Duplicate Values ---//
        LinkedHashSet<String> uniqueFiltredContacts = new LinkedHashSet<String>();

        uniqueFiltredContacts.addAll(FinalContacts);

        FinalContacts.clear();

        FinalContacts.addAll(uniqueFiltredContacts);

        JSONArray selectedContactsArray = getContactObj(FinalContacts);

        JSONArray recipientKeyObject = getRecipientKeyObject(recipientKeyNames);

        try {
            finalObject.put("routes", routeObj);
            finalObject.put("contacts", selectedContactsArray);
            finalObject.put("activities", activityobj);
            finalObject.put("properties", recipientKeyObject);
            finalObject.put("multipleMessagesEnabled", multipleMessagesEnabled);

        } catch (JSONException e) {

            e.printStackTrace();
        }
        return finalObject;
    }

    private JSONArray getRecipientKeyObject(String recipientKeyNames) {

        JSONArray temparray = new JSONArray();
        try {

            JSONObject PropertiesObject = new JSONObject();
            PropertiesObject.put("name", "recipientKey");
            PropertiesObject.put("value", recipientKeyNames.toString());
            temparray.put(PropertiesObject);

        } catch (JSONException e) {

            e.printStackTrace();
        }
        return temparray;
    }

    private JSONArray GetRouteObject() {

        JSONArray jsonArray = new JSONArray();

        ArrayList<ChooserRouteData> selectedItems = chooserRouteAdapter.GetSelectedItems();

        for (int i = 0; i < selectedItems.size(); i++) {

            ChooserRouteData item = selectedItems.get(i);
            try {

                JSONObject obj1 = new JSONObject();

                if (item.getTitle().toUpperCase().equals("TEXT")) {

                    obj1.put("type", "SMS");
                } else if (item.getTitle().toUpperCase().equals("VOICE")) {

                    obj1.put("type", "PHONE");
                } else {

                    obj1.put("type", item.getTitle().toUpperCase());
                }
                int j = i + 1;

                obj1.put("order", j);
                obj1.put("priority", j);
                obj1.put("orgId", OrgnizactionId);

                jsonArray.put(obj1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

    public JSONArray getContactObj(ArrayList data) {
        JSONArray tempJson = new JSONArray();
        for (int i = 0; i < data.size(); i++) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("id", data.get(i));

                tempJson.put(obj);

            } catch (JSONException e) {

                e.printStackTrace();
            }

        }
        return tempJson;
    }

    private String getRouteSelectType(String data) {
        String TempData = null;
        switch (data) {
            case "Email":
                TempData = "EMAIL";
                break;
            case "Text":
                TempData = "SMS";
                break;
            case "Voice":
                TempData = "PHONE";
                break;
        }
        return TempData;
    }

    public void getCustomDialogue(String AlertTitle, String AlertMessage) {

        dialogue_custom = new Dialog(CreateCampaignChooseRoute.this);

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

    private void setMessagesText() {
        if (FinalContacts.size() <= 1) {
            messagetext.setText("Message");
        } else {
            messagetext.setText("Messages");
        }
    }

    private void setContactsMessage(int selectedContactsCount) {

        if (selectedContactsCount == 0) {
            selectedContacts.setText(String.valueOf(SELECTED_CONTACTS_DATA.size()));
            if (SELECTED_CONTACTS_DATA.size() > 1) {
                contactsText.setText("Contacts");
            } else {
                contactsText.setText("Contact");
            }
        } else {
            selectedContacts.setText(String.valueOf(selectedContactsCount));
            if (selectedContactsCount > 1) {
                contactsText.setText("Contacts");
            } else {
                contactsText.setText("Contact");
            }
        }

    }

    public class ChooserRouteAdapter extends RecyclerView.Adapter<ChooserRouteAdapter.MyViewHolder> implements ItemTouchHelperAdapter {

        private final OnStartDragListener mDragStartListener;
        int position;
        private ArrayList<ChooserRouteData> listItems;

        public ChooserRouteAdapter(ArrayList<ChooserRouteData> listItems, OnStartDragListener mDragStartListener) {
            this.listItems = listItems;
            this.mDragStartListener = mDragStartListener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.create_campaign_chose_route_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            ChooserRouteData RouteDatadata = listItems.get(position);
            String datacount = RouteDatadata.getCount();
            int image = RouteDatadata.getImage();
            String title = RouteDatadata.getTitle();
            boolean status = RouteDatadata.isSelected();
            holder.image.setImageResource(image);
            holder.itemtitle.setText(title);
            holder.count.setText(datacount);
            holder.onoffswitch.setChecked(status);
            if (datacount.equals("0")) {
                holder.onoffswitch.setEnabled(false);
            } else {
                holder.onoffswitch.setEnabled(true);
            }
            holder.onoffswitch.setTag(RouteDatadata);

            holder.onoffswitch.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    SwitchCompat onoffswitch = (SwitchCompat) view;
                    ChooserRouteData chooserRouteData = (ChooserRouteData) onoffswitch.getTag();
                    chooserRouteData.setSelected(onoffswitch.isChecked());
                    dataChangeListrnerChecked();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dataChangeListrnerChecked();
                        }
                    }, 300);
                }
            });
            holder.movelistview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        mDragStartListener.onStartDrag(holder);
                    }
                    return false;
                }
            });
        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            Collections.swap(listItems, fromPosition, toPosition);
            ChooserRouteData chooserRouteData = listItems.get(fromPosition);
            chooserRouteData.setPriority(toPosition);
            notifyItemMoved(fromPosition, toPosition);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    defaultCase();
                    dataChangeListrnerChecked();
                }
            }, 1000);
            return true;
        }

        @Override
        public void onItemDismiss(int position) {
            listItems.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }

        private void dataChangeListrnerChecked() {
            ArrayList<ChooserRouteData> selectedItems = GetSelectedItems();
            if (selectedItems.size() == 0) {
                enableNotification();
                defaultCase();
            } else {
                if (multiRouteSupport.isChecked()) {
                    multipleMessagesEnabled = "true";
                    multiMessagesSupport();
                } else {
                    multipleMessagesEnabled = "false";
                    switch (selectedItems.size()) {
                        case 1:
                            disableNotification();
                            enableToggleSwitchForSingleCheck();
                            ChooserRouteData item = GetSingleItem();

                            if (item.getTitle().equalsIgnoreCase("Email")) {
                                EmailFilter();
                            }
                            if (item.getTitle().equalsIgnoreCase("Text")) {
                                TextFilter();
                            }
                            if (item.getTitle().equalsIgnoreCase("Voice")) {
                                VoiceFilter();
                            }


                            break;
                        case 2:
                            disableNotification();
                            enableToggleSwitchForDoubleCheck();
                            emailFilterForDoubleChecked(selectedItems);
                            break;
                        case 3:
                            enableToggleSwitchForAllChecked();
                            disableNotification();
                            filterForAllChecked(selectedItems);
                            break;
                        case 0:
                        default:
                            enableNotification();
                            defaultCase();
                    }
                }
                messagestatus.setText(String.valueOf(FinalContacts.size()));
                setMessagesText();
            }
        }

        private ChooserRouteData getUserDataByName(String name) {
            for (ChooserRouteData item : ListItems) {
                if (item.getTitle().equals(name)) {
                    return item;
                }
            }
            return null;
        }

        private void EmailFilter() {

            ArrayList<String> textContacts = new ArrayList(TextContacts);
            textContacts.removeAll(EmailContacts);

            ArrayList<String> voiceContacts = new ArrayList(VoiceContacts);
            voiceContacts.removeAll(EmailContacts);

            getUserDataByName("Email").setCount(String.valueOf(EmailContacts.size()));

            getUserDataByName("Voice").setCount(String.valueOf(voiceContacts.size()));

            getUserDataByName("Text").setCount(String.valueOf(textContacts.size()));

            FinalContacts = EmailContacts;

            setContactsMessage(FinalContacts.size());

            chooserRouteAdapter.notifyDataSetChanged();

            GetActivityObject(FinalContacts, "EMAIL");

        }

        private void VoiceFilter() {
            ArrayList<String> emaiContacts = new ArrayList(EmailContacts);
            emaiContacts.removeAll(VoiceContacts);

            ArrayList<String> textContacts = new ArrayList(TextContacts);
            textContacts.removeAll(VoiceContacts);

            getUserDataByName("Email").setCount(String.valueOf(emaiContacts.size()));

            getUserDataByName("Voice").setCount(String.valueOf(VoiceContacts.size()));

            getUserDataByName("Text").setCount(String.valueOf(textContacts.size()));

            FinalContacts = VoiceContacts;

            setContactsMessage(FinalContacts.size());

            chooserRouteAdapter.notifyDataSetChanged();

            GetActivityObject(FinalContacts, "PHONE");
        }

        private void TextFilter() {

            ArrayList<String> emailContacts = new ArrayList(EmailContacts);
            emailContacts.removeAll(TextContacts);

            ArrayList<String> voiceContacts = new ArrayList(VoiceContacts);
            voiceContacts.removeAll(TextContacts);

            getUserDataByName("Email").setCount(String.valueOf(emailContacts.size()));

            getUserDataByName("Voice").setCount(String.valueOf(voiceContacts.size()));

            getUserDataByName("Text").setCount(String.valueOf(TextContacts.size()));

            FinalContacts = TextContacts;

            setContactsMessage(FinalContacts.size());

            chooserRouteAdapter.notifyDataSetChanged();

            GetActivityObject(FinalContacts, "SMS");

        }

        private void emailFilterForDoubleChecked(ArrayList<ChooserRouteData> selectedItems) {

            ChooserRouteData FIRST_SELECTED_ITEM = selectedItems.get(0);
            ChooserRouteData SECOND_SELECTED_ITEM = selectedItems.get(1);
            ChooserRouteData THIRD_UNSELECTED_ITEM = GetUnSelectedItems().get(0);

            activityobj = new JSONArray();

            ArrayList<String> firstItemContacts = FIRST_SELECTED_ITEM.getTotalcontacts();

            ArrayList<String> secondItemContacts = SECOND_SELECTED_ITEM.getTotalcontacts();

            ArrayList<String> union = new ArrayList<String>(firstItemContacts);

            union.addAll(secondItemContacts);

            //setup contact count for second item
            ArrayList<String> intersectionForSecondItem = new ArrayList(secondItemContacts);

            intersectionForSecondItem.removeAll(firstItemContacts);

            SECOND_SELECTED_ITEM.setCount(String.valueOf(intersectionForSecondItem.size()));

            LinkedHashSet<String> lhs = new LinkedHashSet<String>();

            lhs.addAll(union);

            union.clear();

            union.addAll(lhs);

            //setup contact count for third item
            ArrayList<String> intersectionForThirdItem = new ArrayList(THIRD_UNSELECTED_ITEM.getTotalcontacts());

            intersectionForThirdItem.removeAll(union);

            THIRD_UNSELECTED_ITEM.setCount(String.valueOf(intersectionForThirdItem.size()));

            for (String ContactId : firstItemContacts) {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("contactId", ContactId);
                    obj.put("orgId", OrgnizactionId);
                    obj.put("type", "SENT");
                    obj.put("routeType", getRouteSelectType(FIRST_SELECTED_ITEM.getTitle()));
                    activityobj.put(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            for (String ContactId : intersectionForSecondItem) {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("contactId", ContactId);
                    obj.put("orgId", OrgnizactionId);
                    obj.put("type", "SENT");
                    obj.put("routeType", getRouteSelectType(SECOND_SELECTED_ITEM.getTitle()));
                    activityobj.put(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            FinalContacts = union;

            setContactsMessage(0);

            chooserRouteAdapter.notifyDataSetChanged();

        }

        private void filterForAllChecked(ArrayList<ChooserRouteData> selectedItems) {

            activityobj = new JSONArray();

            ChooserRouteData firstItem = selectedItems.get(0);

            ChooserRouteData secondItem = selectedItems.get(1);

            ChooserRouteData thirdItem = selectedItems.get(2);

            ArrayList<String> union = new ArrayList<String>(firstItem.getTotalcontacts());

            union.addAll(secondItem.getTotalcontacts());

            /*Common Contacts from FirstSelectedItem and SecondSelectedItem contacts */
            LinkedHashSet<String> commanContacts = new LinkedHashSet<String>();

            commanContacts.addAll(union);

            /*  Add third item Contacts  to union  */
            union.addAll(thirdItem.getTotalcontacts());

            /*  lhs is a LinkedHashSet which is used to store the filter contacts(without any Duplicate Values) */
            LinkedHashSet<String> lhs = new LinkedHashSet<String>();

            lhs.addAll(union);

            union.clear();

            union.addAll(lhs);

            ArrayList<String> secondItemContacts = new ArrayList(secondItem.getTotalcontacts());

            secondItemContacts.removeAll(firstItem.getTotalcontacts());

            ArrayList<String> thirdItemContacts = new ArrayList(thirdItem.getTotalcontacts());

            /* Remove the first and second items comman contacts from the third selected item*/

            thirdItemContacts.removeAll(commanContacts);

            firstItem.setCount(String.valueOf(firstItem.getTotalcontacts().size()));

            secondItem.setCount(String.valueOf(secondItemContacts.size()));

            thirdItem.setCount(String.valueOf(thirdItemContacts.size()));

            FinalContacts = union;

            setContactsMessage(0);

            chooserRouteAdapter.notifyDataSetChanged();

            for (Object id : firstItem.getTotalcontacts()) {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("contactId", id);
                    obj.put("orgId", OrgnizactionId);
                    obj.put("type", "SENT");
                    obj.put("routeType", getRouteSelectType(firstItem.getTitle()));
                    activityobj.put(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            for (String id : secondItemContacts) {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("contactId", id);
                    obj.put("orgId", OrgnizactionId);
                    obj.put("type", "SENT");
                    obj.put("routeType", getRouteSelectType(secondItem.getTitle()));
                    activityobj.put(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            for (String id : thirdItemContacts) {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("contactId", id);
                    obj.put("orgId", OrgnizactionId);
                    obj.put("type", "SENT");
                    obj.put("routeType", getRouteSelectType(thirdItem.getTitle()));
                    activityobj.put(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        private void defaultCase() {

            activityobj = new JSONArray();

            ChooserRouteData Email = getUserDataByName("Email");

            ChooserRouteData Text = getUserDataByName("Text");

            ChooserRouteData Voice = getUserDataByName("Voice");

            Email.setCount(String.valueOf(EmailContacts.size()));

            Text.setCount(String.valueOf(TextContacts.size()));

            Voice.setCount(String.valueOf(VoiceContacts.size()));

            ArrayList tempdata = new ArrayList();

            FinalContacts = tempdata;

            setContactsMessage(0);

            messagestatus.setText(String.valueOf(FinalContacts.size()));

            chooserRouteAdapter.notifyDataSetChanged();

        }

        private ArrayList<ChooserRouteData> GetSelectedItems() {
            ArrayList<ChooserRouteData> getSelectedItems = new ArrayList();
            for (ChooserRouteData items : ListItems) {
                if (items.isSelected()) {
                    getSelectedItems.add(items);
                }
            }
            return getSelectedItems;
        }

        private ArrayList<String> GetSelectedItems2() {
            ArrayList<String> SelectedItems = new ArrayList();
            for (ChooserRouteData items : ListItems) {
                if (items.isSelected()) {
                    SelectedItems.add(items.getTitle().toUpperCase());
                }
            }
            return SelectedItems;
        }

        private ArrayList<ChooserRouteData> GetUnSelectedItems() {
            ArrayList<ChooserRouteData> getSelectedItems = new ArrayList();
            for (ChooserRouteData items : ListItems) {
                if (!items.isSelected()) {
                    getSelectedItems.add(items);
                }
            }
            return getSelectedItems;
        }

        private ChooserRouteData GetSingleItem() {
            for (ChooserRouteData item : ListItems) {
                if (item.isSelected()) {
                    return item;
                }
            }
            return null;
        }

        private void enableNotification() {
            notifcation1.setVisibility(View.VISIBLE);
            notifcation2.setVisibility(View.VISIBLE);
            String sourceString2 = "Reach your contacts through <b>Email</b>,<b> Text</b> and <b>Voice</b> broadcast.";
            notifcation1.setText(Html.fromHtml(sourceString2));
            notifcation2.setText("Select one or more routes. Messages will be sent based on available contact info for each recipient.");
            NotifcationLayout.setVisibility(View.GONE);
            notifcation5.setVisibility(View.GONE);
            multipleMessagesEnabled = "false";
            multiRouteSupport.setChecked(false);
        }

        private void disableNotification() {
            notifcation1.setVisibility(View.GONE);
            notifcation2.setVisibility(View.GONE);
            NotifcationLayout.setVisibility(View.VISIBLE);
            notifcation5.setVisibility(View.VISIBLE);
        }

        private void enableToggleSwitchForSingleCheck() {

            ArrayList<ChooserRouteData> selectedItems = chooserRouteAdapter.GetSelectedItems();

            String sourceString1 = "MindMe will Deliver <b>" + selectedItems.get(0).getTitle() + "</b> to each contact.";

            String sourceString2 = "Send <b> multiple messages </b> to each contact. You need to select more than one route. May lead to excessive opt-outs.";

            notifcation3.setText(Html.fromHtml(sourceString2));

            notifcation5.setText(Html.fromHtml(sourceString1));
        }

        private void enableToggleSwitchForDoubleCheck() {

            ArrayList<ChooserRouteData> selectedItems = chooserRouteAdapter.GetSelectedItems();

            String sourceString1 = "MindMe will Deliver <b>" + selectedItems.get(0).getTitle() + "</b> to each contact.If unavailable <b>" + selectedItems.get(1).getTitle() + "</b> will be sent.";

            String sourceString2 = "Send <b> multiple messages </b> to each contact. You need to select more than one route. May lead to excessive opt-outs.";

            notifcation3.setText(Html.fromHtml(sourceString2));

            notifcation5.setText(Html.fromHtml(sourceString1));
        }

        private void enableToggleSwitchForAllChecked() {

            ArrayList<ChooserRouteData> selectedItems = chooserRouteAdapter.GetSelectedItems();

            String sourceString1 = "MindMe will Deliver <b>" + selectedItems.get(0).getTitle() + "</b> ,<b>" + selectedItems.get(1).getTitle() + "</b> and <b>" + selectedItems.get(2).getTitle() + "</b> to each contact where available.";

            String sourceString2 = "Send <b> multiple messages </b> to each contact. You need to select more than one route. May lead to excessive opt-outs.";

            notifcation3.setText(Html.fromHtml(sourceString2));

            notifcation5.setText(Html.fromHtml(sourceString1));
        }

        private void GetActivityObject(ArrayList<String> data, String routeType) {
            activityobj = new JSONArray();
            for (String id : data) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("contactId", id);
                    obj.put("orgId", OrgnizactionId);
                    obj.put("type", "SENT");
                    obj.put("routeType", routeType);
                    activityobj.put(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView itemtitle, count;
            public ImageView image, movelistview;
            SwitchCompat onoffswitch;

            public MyViewHolder(final View view) {
                super(view);
                image = (ImageView) view.findViewById(R.id.imageview);
                movelistview = (ImageView) view.findViewById(R.id.movelistview);
                count = (TextView) view.findViewById(R.id.itemcount);
                itemtitle = (TextView) view.findViewById(R.id.itemtitle);
                onoffswitch = (SwitchCompat) view.findViewById(R.id.onoffswitch);
            }
        }
    }
}

