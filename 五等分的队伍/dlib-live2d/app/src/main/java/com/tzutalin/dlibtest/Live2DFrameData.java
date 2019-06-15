package com.tzutalin.dlibtest;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Locale;

import jp.live2d.util.Json;

class Live2DFrameData implements Serializable {
    public float headX,headY;
    public float eyeX,eyeY;
    public float mouth;
    public float blinkLeft,blinkRight;

    public Live2DFrameData(float headX, float headY, float eyeX, float eyeY, float mouth, float blinkLeft, float blinkRight) {
        this.headX = headX;
        this.headY = headY;
        this.eyeX = eyeX;
        this.eyeY = eyeY;
        this.mouth = mouth;
        this.blinkLeft = blinkLeft;
        this.blinkRight = blinkRight;
    }
    public Live2DFrameData() {
        this.headX = 0;
        this.headY = 0;
        this.eyeX = 0;
        this.eyeY = 0;
        this.mouth = 0;
        this.blinkLeft = 0;
        this.blinkRight = 0;
    }
    public float getHeadX() {
        return headX;
    }

    public float getHeadY() {
        return headY;
    }

    public float getEyeX() {
        return eyeX;
    }

    public float getEyeY() {
        return eyeY;
    }

    public float getMouth() {
        return mouth;
    }

    public float getBlinkLeft() {
        return blinkLeft;
    }

    public float getBlinkRight() {
        return blinkRight;
    }

    public void setHead(float x,float y) {
        headX = numToRange(x,-1,1);
        headY = numToRange(y,-1,1);
    }
    public void setEye(float x,float y) {
        eyeX = numToRange(x,-1,1);
        eyeY = numToRange(y,-1,1);
    }

    public void setMouth(float mouth) {
        this.mouth = numToRange(mouth,0,1);
    }

    public void setBlink(float l,float r) {
        blinkLeft = numToRange(l,0,1);
        blinkRight = numToRange(r,0,1);
    }
    private float numToRange(float num,float min,float max){
        return (num>min)?Math.min(max,num):min;
    }
    public static final Live2DFrameData bufFrame = new Live2DFrameData();

    public void copy2(Live2DFrameData dstData){
        dstData.setBlink(blinkLeft,blinkRight);
        dstData.setEye(eyeX,eyeY);
        dstData.setHead(headX,headY);
        dstData.setMouth(mouth);
    }

    @Override
    public String toString() {
        return super.toString()+String.format(Locale.getDefault(),
                "blink:(%f,%f),head:(%f,%f),mouth:%f\n",blinkLeft,blinkRight,headX,headY,mouth);
    }
}