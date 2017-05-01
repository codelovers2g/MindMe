package com.mindmesolo.mindme.Models;

/**
 * Created by enest_09 on 11/2/2016.
 */

public class YesNoMaybe {
    private String id;

    private String maybeCode;

    private String yesCode;

    private String[] buttons;

    private String noCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaybeCode() {
        return maybeCode;
    }

    public void setMaybeCode(String maybeCode) {
        this.maybeCode = maybeCode;
    }

    public String getYesCode() {
        return yesCode;
    }

    public void setYesCode(String yesCode) {
        this.yesCode = yesCode;
    }

    public String[] getButtons() {
        return buttons;
    }

    public void setButtons(String[] buttons) {
        this.buttons = buttons;
    }

    public String getNoCode() {
        return noCode;
    }

    public void setNoCode(String noCode) {
        this.noCode = noCode;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", maybeCode = " + maybeCode + ", yesCode = " + yesCode + ", yesNoMaybe = " + buttons + ", noCode = " + noCode + "]";
    }
}
