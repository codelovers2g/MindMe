package com.mindmesolo.mindme.Notifcations;

import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by User1 on 9/13/2016.
 */
public class NotificationModal {
    public int position;
    String title = null;
    String date = null;
    Button button;
    String buttonText = null;
    String ContactId = null;
    ImageView image;
    String NotificationId = null;
    boolean read;

    public NotificationModal(String title, String ContactId, String NotificationId, String date, String buttonText, boolean read, int position) {
        super();
        this.title = title;
        this.ContactId = ContactId;
        this.date = date;
        this.buttonText = buttonText;
        this.NotificationId = NotificationId;
        this.read = read;
        this.position = position;
    }


    public NotificationModal() {
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;

    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public String getContactId() {
        return ContactId;
    }

    public void setContactId(String contactId) {
        ContactId = contactId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotificationId() {
        return NotificationId;
    }

    public void setNotificationId(String notificationId) {
        NotificationId = notificationId;
    }
}
