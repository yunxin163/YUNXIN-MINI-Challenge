package com.tzutalin.dlibtest;

import android.util.Pair;

import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Locale;

import jp.live2d.util.Json;

public class JSONWrapper {
    private static JSONWrapper singleton;
    public static JSONWrapper getInstance(){
        if(singleton == null){
            synchronized (JSONWrapper.class){
                if(singleton == null){
                    singleton = new JSONWrapper();
                }
            }
        }
        return singleton;
    }
    private JSONObject warpJSON(int cmd,JSONObject body) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("cmd",cmd);
        obj.put("body",body);
        return obj;
    }
    private JSONObject warpJSON(int cmd,JSONArray body) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("cmd",cmd);
        obj.put("body",body);
        return obj;
    }
    private JSONObject warpJSON(int cmd,JsonElement body) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("cmd",cmd);
        obj.put("body",body);
        return obj;
    }
    private JSONObject warpJSON(int cmd,String body) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("cmd",cmd);
        obj.put("body",body);
        return obj;
    }
    public JSONObject loginJSON(String account)throws JSONException{
        return warpJSON(LOGIN,new JSONObject(String.format("{\"username\":\"%s\"}",account)));
    }
    public JSONObject startChatJSON(String targetAccount)throws JSONException{
        return warpJSON(START_CHAT,new JSONObject(String.format("{\"target\":\"%s\"}",targetAccount)));
    }
    public JSONObject endChatJSON()throws JSONException{
        return warpJSON(END_CHAT,new JSONObject(String.format("{}")));
    }


    public JSONObject packVertJSON(Live2DFrameData frame)throws JSONException{
        return new JSONObject(String.format(Locale.getDefault(),
                "{cmd=%d,body=[%f,%f,%f,%f,%f,%f,%f]}",
                SEND_VERT,
                frame.blinkLeft,
                frame.blinkRight,
                frame.eyeX,
                frame.eyeY,
                frame.headX,
                frame.headY,
                frame.mouth));
    }

    public Pair<Integer,Object> unpackJSON(JSONObject obj)throws JSONException{
        int cmd = obj.getInt("cmd");

        Object result = null;
        switch (cmd){
            case LOGIN_STATE:
                Integer status = obj.getJSONObject("body").getInt("status");
                result = status;
                break;
            case RECE_VERT:
                String str = obj.getJSONArray("body").toString();
                synchronized (Live2DFrameData.bufFrame){
                    result = string2Frame(str,Live2DFrameData.bufFrame);
                }
                break;
            case RECE_CHAT:
                String fromAccount = obj.getJSONObject("body").getString("from");
                result = fromAccount;
                break;
            case RECE_STATE:
                Integer status2 = obj.getJSONObject("body").getInt("status");
                result = status2;
                break;
        }
        return new Pair<>(cmd,result);
    }
    private Live2DFrameData string2Frame(String str){
        return string2Frame(str,new Live2DFrameData());
    }
    private Live2DFrameData string2Frame(String str,Live2DFrameData data){
        char[] chars = str.toCharArray();
        float[] nums = new float[7];
        int numIndex = 0;
        StringBuffer buf = new StringBuffer();
        for(int i=0;i<chars.length;i++){
            buf.delete(0,buf.length());
            while(chars[i]>='0'&&chars[i]<='9'||chars[i]=='.'){
                buf.append(chars[i++]);
            }
            if(buf.length()!=0){
                nums[numIndex++] = Float.parseFloat(buf.toString());
            }
        }
        data.blinkLeft = nums[0];
        data.blinkRight = nums[1];
        data.eyeX = nums[2];
        data.eyeY = nums[3];
        data.headX = nums[4];
        data.eyeY = nums[5];
        data.mouth = nums[6];
        return data;
    }


    public static final int LOGIN = 0;
    public static final int START_CHAT = 1;
    public static final int END_CHAT = 2;
    public static final int SEND_VERT = 3;

    public static final int LOGIN_STATE = 0;
    public static final int RECE_VERT = 1;
    public static final int RECE_CHAT = 2;
    public static final int RECE_STATE = 3;
}
