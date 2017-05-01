package com.mindmesolo.mindme.MobilePageHelper;

/**
 * Created by eNest on 8/17/2016.
 */
public class ListViewItem {
    private String text;

    private int type;

    private int image;

    private boolean ischecked;

    private String name, id, length, contactid;

    public ListViewItem(String text, int type) {
        this.text = text;
        this.type = type;
    }


    public ListViewItem(String name, int image, boolean taskstatus) {
        this.image = image;
        this.name = name;
        this.ischecked = taskstatus;
    }

    public ListViewItem(String id, String name, String length, String contactid, int type) {
        this.id = id;
        this.name = name;
        this.length = length;
        this.contactid = contactid;
        this.type = type;
    }

    public ListViewItem(String text, boolean ischecked, int type) {
        this.text = text;
        this.ischecked = ischecked;
        this.type = type;
    }

    public ListViewItem(String text, int type, int image) {
        this.text = text;
        this.type = type;
        this.image = image;
    }

    public ListViewItem(String text, int type, int image, boolean ischecked) {
        this.text = text;
        this.type = type;
        this.image = image;
        this.ischecked = ischecked;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean ischecked() {
        return ischecked;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactid() {
        return contactid;
    }

    public void setContactid(String contactid) {
        this.contactid = contactid;
    }
}
