package com.tzutalin.dlibtest;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Scanner;

public class LandmarkManager {
    private static final int PacketLen = 200;
    private static final String TAG = "LandmarkManager";
    private ManagerStatu statu = ManagerStatu.None;
    private Socket socket;
    private String socketAddr = "125.124.155.138";
    private int socketPort = 3001;
    private Scanner in;
    private OutputStream out;
    private InputStream inStream;
    private OutputStream outStream;
    private byte[] inputBuffer = new byte[PacketLen];
    private byte[] outputBuffer = new byte[PacketLen];
    private JSONWrapper w = JSONWrapper.getInstance();
    private OnGetLandmarkListener listener;
    private Thread thread;
    private Method targetMethod;
    private Object methodArg;
    private LandmarkProcessor processor;
    public boolean chatlock = true;
    private String phoneNumber = null;
    private String chatTarget = null;
    private Context context = null;

    public void reset(){
        chatlock = true;
        setStatu(ManagerStatu.Unlogin);
        Thread readThread = new Thread(()->{
            while(statu!=ManagerStatu.Error&&statu!=ManagerStatu.End){
                try {
                    if(statu == ManagerStatu.Logined){
                        forceReadChat();
                    }else if(statu == ManagerStatu.Talking){
                        receiveFrame();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            endChat();
        });
        Thread writeThread = new Thread(()->{
            try {
                init();
                login(phoneNumber,null);
                readThread.start();
                while(chatlock){
                    try {
                        Thread.sleep(100);
                    }catch (Exception e){}
                }
                while(statu!=ManagerStatu.Error&&statu!=ManagerStatu.End){
                    synchronized (processor.localTarget){
                        processor.localTarget.wait();
                        sendFrame(processor.localTarget);
                    }
                }
                endChat();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        readThread.setName("readThread");
        writeThread.setName("writeThread");
        writeThread.start();
    }
    public void activeChat(String chatTarget){
        if(!chatlock){
            endChat();
            reset();
        }
        try{
            this.chatTarget = chatTarget;
            new Thread(()->{
                chatWith(chatTarget,null);
                chatlock = false;
            }).start();
        }catch (Exception e){}

    }
    public void setContext(Context context){
        this.context = context;
    }
    private static LandmarkManager singleton;
    public static LandmarkManager getInstance(){
        if(singleton == null){
            synchronized (LandmarkManager.class){
                if(singleton == null){
                    singleton = new LandmarkManager();
                }
            }
        }
        return singleton;
    }
    public void circle(String phoneNumber,LandmarkProcessor processor){
        this.processor = processor;
        this.phoneNumber = phoneNumber;
        setOnGetLandmarkListener(landmark ->{
            synchronized (processor.remoteTarget){
                landmark.copy2(processor.remoteTarget);
            }
        });

        Thread readThread = new Thread(()->{
            while(statu!=ManagerStatu.Error&&statu!=ManagerStatu.End){
                try {
                    if(statu == ManagerStatu.Logined){
                        forceReadChat();
                    }else if(statu == ManagerStatu.Talking){
                        receiveFrame();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            endChat();
        });
        Thread writeThread = new Thread(()->{
            try {
                init();
                login(phoneNumber,null);
                readThread.start();
                while(chatlock){
                    try {
                        Thread.sleep(100);
                    }catch (Exception e){}
                }
                while(statu!=ManagerStatu.Error&&statu!=ManagerStatu.End){
                    synchronized (processor.localTarget){
                        processor.localTarget.wait();
                        sendFrame(processor.localTarget);
                    }
                }
                endChat();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        readThread.setName("readThread");
        writeThread.setName("writeThread");
        writeThread.start();
    }

    public void pipeline(String phoneNumber,String targetAccount,LandmarkProcessor processor){
        this.processor = processor;
        setOnGetLandmarkListener(landmark ->{
            synchronized (processor.localTarget){
                landmark.copy2(processor.localTarget);
            }
        });
        Thread readThread = new Thread(()->{
           while(statu!=ManagerStatu.Error){
               try {
                   receiveFrame();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
        });

        new Thread(()->{
            try {
                init();
                login(phoneNumber,null);
                chatWith(targetAccount,null);
                readThread.start();
                while(statu!=ManagerStatu.Error){
                    synchronized (processor.localTarget){
                        processor.localTarget.wait();
                        sendFrame(processor.localTarget);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    public void init() throws IOException {
        chatlock = true;
        socket = new Socket(socketAddr,socketPort);
        inStream = socket.getInputStream();
        outStream = socket.getOutputStream();
        in = new Scanner(inStream);
        out = outStream;

        setStatu(ManagerStatu.Unlogin);
    }
    public void write(String s) throws IOException {
        Log.i(TAG, "sendData: "+s);
        writeBuffer(s);
        //Log.i(TAG, "rawSendData: "+new String(outputBuffer));
        out.write(outputBuffer);
        out.flush();
    }
    public String read() throws IOException {
        inStream.read(inputBuffer,0,inputBuffer.length);
        String s = readBuffer();
        Log.i(TAG, "receiveData: "+s);
        return s;
    }
    public String readByByte()throws IOException {
        /*int offset = 0;
        byte[] temp = new byte[1];
        while(offset<inputBuffer.length){
            inStream.read(temp);
            inputBuffer[offset++] = temp[0];
            Log.i(TAG, "receiveOneByte: "+temp[0]);
        }*/
        int readLen = inStream.read(inputBuffer);
        String s = readBuffer();
        Log.i(TAG, "receiveData: "+String.format("%s,len=%d",s,readLen));
        return s;
    }
    public void login(String phoneNumber, Runnable callback){
        if(out == null){
            setStatu(ManagerStatu.Error);
            return;
        }
        setStatu(ManagerStatu.Logining);
        try {
            String temp = w.loginJSON(phoneNumber).toString();
            write(temp);

            String readStr = readByByte();
            readStr = readStr.replaceAll("\n","");

            Pair<Integer,Object> pair = w.unpackJSON(new JSONObject(readStr));
            StringBuilder builder = new StringBuilder();
            if(pair.first==JSONWrapper.LOGIN_STATE){
                builder.append("login state:").append(pair.second.toString());
                if(Integer.valueOf(1).equals(pair.second)){
                    setStatu(ManagerStatu.Logined);
                }else {
                    setStatu(ManagerStatu.Error);
                }
            }else if(pair.first == JSONWrapper.RECE_VERT){
                builder.append("receive vertex:").append(pair.second.toString());
                setStatu(ManagerStatu.Error);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(callback!=null)callback.run();
    }
    public void endChat(){
        new Thread(()->{
            try {
                String temp = w.endChatJSON().toString();
                write(temp);
                setStatu(ManagerStatu.End);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        destroy();
    }
    public void chatWith(String target,Runnable callback){
        if(out == null){
            setStatu(ManagerStatu.Error);
            return;
        }
        try {
            synchronized (inStream){
                String temp = w.startChatJSON(target).toString();
                write(temp);

                /*String readStr = readByByte();
                readStr = readStr.replaceAll("\n","");

                Pair<Integer,Object> pair = w.unpackJSON(new JSONObject(readStr));
                StringBuilder builder = new StringBuilder();
                if(pair.first==JSONWrapper.RECE_STATE) {
                    builder.append("recev state:").append(pair.second.toString());
                    if (Integer.valueOf(1).equals(pair.second)) {
                        setStatu(ManagerStatu.Talking);
                    } else {
                        setStatu(ManagerStatu.Error);
                    }
                }*/
                setStatu(ManagerStatu.Talking);
                //Log.i(TAG, "chatWith: "+builder.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if(callback!=null)callback.run();
    }
    public String forceReadChat() throws IOException{
        String result = null;
        try{
            String readStr = readByByte();
            Pair<Integer,Object> pair = w.unpackJSON(new JSONObject(readStr));
            StringBuilder builder = new StringBuilder();
            if(pair.first==JSONWrapper.RECE_CHAT){
                result = pair.second.toString();
                setStatu(ManagerStatu.Talking);
                chatlock = false;
                /*if(context!=null){
                    Intent intent = new Intent(context, CameraActivity.class);
                    intent.putExtra("target",CameraActivity.PASSIVE);
                    context.startActivity(intent);
                }*/

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    public void setOnGetLandmarkListener(OnGetLandmarkListener listener){
        this.listener = listener;
    }

    public void sendFrame(Live2DFrameData data){
        try {
            String temp = w.packVertJSON(data).toString();
            write(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void receiveFrame() throws IOException{

        String readStr = readByByte();
        readStr = readStr.replaceAll("\n","");

        Pair<Integer,Object> pair = null;
        try {
            pair = w.unpackJSON(new JSONObject(readStr));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringBuilder builder = new StringBuilder();
        if(pair.first==JSONWrapper.LOGIN_STATE){
            builder.append("login state:").append(pair.second.toString());
            setStatu(ManagerStatu.Error);
        }else if(pair.first == JSONWrapper.RECE_VERT){
            builder.append("receive vertex:").append(pair.second.toString());
            listener.OnGetLandmark((Live2DFrameData) pair.second);
        }else{
            setStatu(ManagerStatu.Error);
        }
    }
    private void writeBuffer(String s){
        byte[] strbytes = s.getBytes();
        System.arraycopy(strbytes,0,outputBuffer,0,strbytes.length);
        outputBuffer[strbytes.length] = 0;
    }
    private String readBuffer(){
        int endIndex = -1;
        for(endIndex=0;endIndex<inputBuffer.length;endIndex++){
            if(inputBuffer[endIndex]=='\n'||inputBuffer[endIndex]=='\0'||inputBuffer[endIndex]=='\t') break;
        }
        if(endIndex!=0){
            byte[] strBytes = new byte[endIndex];
            System.arraycopy(inputBuffer,0,strBytes,0,strBytes.length);
            return new String(strBytes);
        }
        else return "";
    }
    private void setStatu(ManagerStatu statu) {
        onStatuChanges(this.statu,statu);
        this.statu = statu;
    }
    private void onStatuChanges(ManagerStatu statuSrc,ManagerStatu statuDst){
        Log.i(TAG, "onStatuChanges: "+statuSrc.toString()+">>>"+statuDst.toString());
    }
    private void destroy(){
        setStatu(ManagerStatu.Error);
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
interface OnGetLandmarkListener{
    void OnGetLandmark(Live2DFrameData data);
}
enum ManagerStatu{
    None,
    Unlogin,
    Logining,
    Logined,
    Talking,
    Error,
    End
}
