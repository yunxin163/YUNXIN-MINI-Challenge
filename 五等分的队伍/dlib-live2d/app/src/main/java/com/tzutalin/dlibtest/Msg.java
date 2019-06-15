package com.tzutalin.dlibtest;

public class Msg {
    public static final int SEND_MASSAGE=0;
    public static final int RECEIVED_MASSAGE=0;
    private int imageID;
    private String message;
    private int type;

    public Msg(int imageID, String message, int type) {
        this.imageID = imageID;
        this.message = message;
        this.type = type;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
