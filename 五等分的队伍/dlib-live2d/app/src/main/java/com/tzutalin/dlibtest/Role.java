package com.tzutalin.dlibtest;

public class Role {
    private String phone=null;
    private String image;

    private int imageId;                    //用户头像
    private String name;                    //用户名
    private String lastTalk;                //最后一次交谈时间
    private String lastTalkDate;            //最后一次交谈内容

    public Role(String phone, String image, String name) {
        this.phone = phone;
        this.image = image;
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public Role(int imageId, String name, String lastTalk, String lastTalkDate) {
        this.imageId = imageId;
        this.name = name;
        this.lastTalk = lastTalk;
        this.lastTalkDate = lastTalkDate;
    }
    public Role(String phone, int imageId, String name, String lastTalk, String lastTalkDate) {
        this.phone = phone;
        this.imageId = imageId;
        this.name = name;
        this.lastTalk = lastTalk;
        this.lastTalkDate = lastTalkDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastTalk() {
        return lastTalk;
    }

    public void setLastTalk(String lastTalk) {
        this.lastTalk = lastTalk;
    }

    public String getLastTalkDate() {
        return lastTalkDate;
    }

    public void setLastTalkDate(String lastTalkDate) {
        this.lastTalkDate = lastTalkDate;
    }
}
