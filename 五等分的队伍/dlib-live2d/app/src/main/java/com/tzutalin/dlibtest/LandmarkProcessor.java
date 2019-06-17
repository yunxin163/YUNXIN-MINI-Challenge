package com.tzutalin.dlibtest;

import android.graphics.Point;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class LandmarkProcessor {
    public LandmarkProcessor thiz;
    public final InterpolationMode runningMode;
    public long dertaTime;
    public int frameRate;
    private ArrayList<Point> landmark;
    private boolean stoped = true;
    private boolean paused = true;

    private float common_t = 0.5f;
    private float blink_t,eye_t,head_t,mouth_t;

    private final SparseArray<Point> ps = new SparseArray<>();
    private Thread holdingThread;

    public final Live2DFrameData localTarget = new Live2DFrameData();
    public final Live2DFrameData remoteTarget = new Live2DFrameData();
    public Live2DFrameData target;
    private Live2DFrameData buffer = new Live2DFrameData();
    private Interpolator interpolator = new LinearInterpolator();//插值器



    private MyRenderer renderer;

    private int indexes[] = {4,8,12,24,66,30,31,33,35,62,36,37,40,41,43,44,46,47};

    public LandmarkProcessor(InterpolationMode mode, int frameRate,ProcessType processType){
        runningMode = mode;
        this.frameRate = frameRate;
        this.dertaTime = (int)(1000000000.0/frameRate);
        this.thiz = this;
        for(int index : indexes){
            ps.put(index,new Point(0,0));
        }
        buffer.setMouth(0);
        buffer.setEye(0,0);
        buffer.setHead(0,0);
        buffer.setBlink(1,1);

        blink_t = 1-(float) Math.pow(1-common_t,10.0/frameRate);
        eye_t = 1-(float) Math.pow(1-common_t,10.0/frameRate);
        head_t = 1-(float) Math.pow(1-common_t,10.0/frameRate);
        mouth_t = 1-(float) Math.pow(1-common_t,10.0/frameRate);
        if(processType == ProcessType.Local){
            target = localTarget;
        }else if(processType == ProcessType.Remote){
            target = remoteTarget;
        }
    }
    public LandmarkProcessor(){
        this(InterpolationMode.Linear,10,ProcessType.Remote);
    }
    public void pause(){
        paused = true;
    }
    public void stop(){
        if(!stoped){
            stoped = true;
            paused = true;
        }
    }

    public void start(MyRenderer renderer){
        this.renderer = renderer;
        if(stoped) {
            stoped = false;
            paused = false;
            new Thread(this::runningCode).start();
        }
        else if(paused){
            paused = false;
        }
    }
    private void runningCode(){
        while (!stoped){
            long prevTime = System.nanoTime();
            while(!paused){
                long derta = System.nanoTime()-prevTime;
                if(derta>dertaTime) {
                    prevTime = System.nanoTime();
                    fixedUpdate();
                    Log.d(TAG, "runningCode: "+"fixedUpdate");
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void fixedUpdate(){
        //calculateTarget();
        calculateBuffer();
        updateFrame();
    }
    private void updateFrame(){
        if(renderer!=null){
            renderer.setBlink(buffer.getBlinkLeft(),buffer.getBlinkRight());
            renderer.setEyeForwards(buffer.getEyeX(),buffer.getEyeY());
            renderer.setMouth(buffer.getMouth());
            renderer.setHeadForwards(buffer.getHeadX(),buffer.getHeadY());
        }
    }
    private void calculateBuffer(){
        buffer.setBlink(interpolator.interpolate(buffer.blinkLeft, target.blinkLeft,blink_t),
                interpolator.interpolate(buffer.blinkLeft, target.blinkLeft,blink_t));
        buffer.setHead(interpolator.interpolate(buffer.headX, target.headX,head_t),
                interpolator.interpolate(buffer.headY, target.headY,head_t));
        buffer.setEye(interpolator.interpolate(buffer.eyeX, target.eyeX,eye_t),
                interpolator.interpolate(buffer.eyeY, target.eyeY,eye_t));
        buffer.setMouth(interpolator.interpolate(buffer.mouth, target.mouth,mouth_t));
    }

    float eyeMax = 0;
    private void calculateTarget(){
        float faceSize = ps.get(24).y-ps.get(8).y;
        float faceXsrc = (ps.get(66).x-(ps.get(4).x+ps.get(12).x)/2)/faceSize;
        float faceYsrc = (ps.get(33).y-(ps.get(35).y+ps.get(31).y)/2)/faceSize;
        float mouthsrc = (ps.get(62).y-ps.get(66).y)/faceSize;
        float eyeLsrc = ((ps.get(37).y+ps.get(36).y-ps.get(41).y-ps.get(40).y)/2)/faceSize;
        float eyeRsrc = ((ps.get(43).y+ps.get(44).y-ps.get(46).y-ps.get(47).y)/2)/faceSize;

        float faceXdst = remap(faceXsrc,-0.2f,0.2f,-1,1);
        float faceYdst = remap(faceYsrc,-0.06f,0.0f,-1,1);

        if(eyeLsrc>eyeMax) eyeMax = eyeLsrc;
        if(eyeRsrc>eyeMax) eyeMax = eyeRsrc;

        localTarget.setBlink(remap(eyeLsrc,eyeMax*0.35f,eyeMax/2.2f,0,1),
                remap(eyeRsrc,eyeMax*0.35f,eyeMax/2.2f,0,1));
        localTarget.setHead(-faceXdst, faceYdst);
        localTarget.setEye(-faceXdst,faceYdst);
        localTarget.setMouth(remap(mouthsrc,0,0.1f,0,1));

        Log.d(TAG, "calculateTarget: "+String.format("blink:(%f,%f),head:(%f,%f),mouth:%f\n",
                localTarget.getBlinkLeft(), localTarget.getBlinkRight(),-localTarget.getHeadX(), localTarget.getHeadY(),
                localTarget.getMouth()));
        Log.d(TAG, "calculateTarget: "+String.format("blinksrc:(%f,%f),headsrc:(%f,%f),mouthsrc:%f,eyeMax:%f\n",
                eyeLsrc,eyeRsrc,faceXsrc,faceYsrc,
                mouthsrc,eyeMax));
        //localTarget.setBlink();
        try{
            localTarget.notifyAll();
        }catch (Exception e){

        }

    }
    private float remap(float num,float srcMin,float srcMax,float dstMin,float dstMax){
        return (num-srcMin)/(srcMax-srcMin)*(dstMax-dstMin)+dstMin;
    }
    public void updateLandmark(ArrayList<Point> landmark){
        for (int index : indexes){
            ps.put(index,landmark.get(index));
        }
        synchronized (this.localTarget){
            calculateTarget();
        }
    }

    public enum InterpolationMode{
        None,
        Linear
    }
    public enum ProcessType{
        Remote,
        Local
    }
}
interface Interpolator{
    float interpolate(float src,float dst,float t);
}
class LinearInterpolator implements Interpolator{
    @Override
    public float interpolate(float src, float dst, float t) {
        return (1-t)*src+t*dst;
    }
}
