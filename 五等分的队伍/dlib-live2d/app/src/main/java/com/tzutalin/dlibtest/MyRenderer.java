package com.tzutalin.dlibtest;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLSurfaceView;
import android.util.Log;


import com.tzutalin.framework.L2DPhysics;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import jp.live2d.android.Live2DModelAndroid;
import jp.live2d.android.UtOpenGL;
import jp.live2d.motion.EyeBlinkMotion;
import jp.live2d.motion.Live2DMotion;
import jp.live2d.motion.MotionQueueManager;

public class MyRenderer implements GLSurfaceView.Renderer {

    Context con;
    Live2DModelAndroid live2DModel;
    Live2DMotion motion;
    MotionQueueManager motionMgr;
    L2DPhysics physics;
    EyeBlinkMotion eyeBlinkMotion;
    private static final String TAG = "MyRenderer";
    boolean eye_adjust = false;

    private List<String> modelPaths = new ArrayList<>();
    private List<String[]> texturePaths = new ArrayList<>();
    private List<String[]> motionPaths = new ArrayList<>();
    private List<String> physicsPaths = new ArrayList<>();

    float glWidth = 0;
    float glHeight = 0;

    public MyRenderer(Context context){
        initModelPaths();
        con = context;
        motionMgr = new MotionQueueManager();
        eyeBlinkMotion = new EyeBlinkMotion();
        if(ModelParam.modelIndex == 1){
            eye_adjust = true;
        }
    }

    private void initModelPaths(){
        modelPaths.add("epsilon/haru/Epsilon.moc");
//        modelPaths.add("epsilon/abc/haru_01.moc");
//        modelPaths.add("epsilon/abc/haru_02.moc");
        modelPaths.add("epsilon/delisha/model.moc");
        texturePaths.add(new String[]{
                                        "epsilon/haru/Epsilon.1024/texture_00.png",
                                        "epsilon/haru/Epsilon.1024/texture_01.png",
                                        "epsilon/haru/Epsilon.1024/texture_02.png"
        });
//        texturePaths.add(new String[]{
//                "epsilon/abc/haru_01.1024/texture_00.png",
//                "epsilon/abc/haru_01.1024/texture_01.png",
//                "epsilon/abc/haru_01.1024/texture_02.png"
//        });
//        texturePaths.add(new String[]{
//                "epsilon/abc/haru_02.1024/texture_00.png",
//                "epsilon/abc/haru_02.1024/texture_01.png",
//                "epsilon/abc/haru_02.1024/texture_02.png"
//        });
        texturePaths.add(new String[]{
                "epsilon/delisha/delisha.2048/texture_00.png",
                "epsilon/delisha/delisha.2048/texture_01.png",
        });
        motionPaths.add(new String[]{
                "epsilon/haru/motions/Epsilon_idle_01.mtn",  //自然眨眼和身体晃动的动作
        });
//        motionPaths.add(new String[]{
//                "epsilon/abc/motions/idle_00.mtn",
//        });
//        motionPaths.add(new String[]{
//                "epsilon/abc/motions/idle_00.mtn",
//        });
        motionPaths.add(new String[]{
                "epsilon/delisha/motions/idle.mtn"
        });
        physicsPaths.add("epsilon/haru/Epsilon.physics.json");
//        physicsPaths.add("epsilon/abc/haru.physics.json");
//        physicsPaths.add("epsilon/abc/haru.physics.json");
        physicsPaths.add("epsilon/delisha/physics.json");
    }


    //执行渲染工作
    @Override
    public void onDrawFrame(GL10 gl) {
        //对OpenGL的设置
        gl.glMatrixMode(GL10.GL_MODELVIEW ) ;
        gl.glLoadIdentity() ;
        gl.glClear( GL10.GL_COLOR_BUFFER_BIT ) ;
        gl.glEnable( GL10.GL_BLEND ) ;
        gl.glBlendFunc( GL10.GL_ONE , GL10.GL_ONE_MINUS_SRC_ALPHA ) ;
        gl.glDisable( GL10.GL_DEPTH_TEST ) ;
        gl.glDisable( GL10.GL_CULL_FACE ) ;

        live2DModel.loadParam();

        if(motionMgr.isFinished()){
            /*
                播放动画需要使用MotionQueueManager的startMotion函数
                第二个参数可以设定动作结束以后删除与否（设置为true时，Live2D会自动调用delete()函数）
            */
            motionMgr.startMotion(motion, false);
        }else{
            /*
                当然只是Motion肯定不能动的，所以必须调用updateParam()函数设定将正在播放的动作参数设定到模型中
             */
            motionMgr.updateParam(live2DModel);
        }
        //暂时保存目前所有数值
        live2DModel.saveParam();

        live2DModel.setParamFloat("PARAM_ANGLE_X", ModelParam.HEAD_X * 30);
        live2DModel.setParamFloat("PARAM_ANGLE_Y", ModelParam.HEAD_Y * 30);

        live2DModel.setParamFloat("PARAM_EYE_BALL_X", ModelParam.EYE_BALL_X );
        live2DModel.setParamFloat("PARAM_EYE_BALL_Y", ModelParam.EYE_BALL_Y );

        live2DModel.setParamFloat("PARAM_MOUTH_OPEN_Y", ModelParam.MOUTH_OPEN);

        if(eye_adjust){
            live2DModel.setParamFloat("PARAM_EYE_L_OPEN", 1 - ModelParam.EYE_L);
            live2DModel.setParamFloat("PARAM_EYE_R_OPEN", 1 - ModelParam.EYE_R);
        }else{
            live2DModel.setParamFloat("PARAM_EYE_L_OPEN", ModelParam.EYE_L);
            live2DModel.setParamFloat("PARAM_EYE_R_OPEN", ModelParam.EYE_R);
        }


         /*
            这个类是 Live2D 库中简易使用物理演算的封装类。
            不使用也可设置物理演算。
            L2DPhysics 类的 load 函数用来读取 JSON 文件。
            更新时，调用 update 函数，把参数应用在模型上。
        */
        physics.updateParam(live2DModel);
        //配置绘图环境
        live2DModel.setGL(gl);
        //更新顶点
        live2DModel.update();
        //绘制
        live2DModel.draw();
    }

    // 渲染窗口大小发生改变或者屏幕方法发生变化时候回调
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();

        float modelWidth = live2DModel.getCanvasWidth();
        gl.glOrthof(
                0,
                modelWidth,
                modelWidth * height / width,
                0,
                0.5f, -0.5f
        );
        glWidth = width;
        glHeight = height;
    }

    //surface被创建后需要做的处理
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        AssetManager mngr = con.getAssets();
        try{
            InputStream in = mngr.open(modelPaths.get(ModelParam.modelIndex));
            live2DModel = Live2DModelAndroid.loadModel(in);
            in.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        try{
            //texture
            String[] textures = texturePaths.get(ModelParam.modelIndex);
            for(int i = 0; i < textures.length; i++){
                InputStream in = mngr.open(textures[i]);
                int texNo = UtOpenGL.loadTexture(gl, in, true);
                live2DModel.setTexture(i, texNo);
                in.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        try {
            InputStream in;
            String[] motions = motionPaths.get(ModelParam.modelIndex);
            for(int i = 0; i < motions.length; i++){
                in = mngr.open(motions[i]) ;
                motion = Live2DMotion.loadMotion(in) ;
                in.close() ;
            }

            in=mngr.open(physicsPaths.get(ModelParam.modelIndex));
            physics=L2DPhysics.load(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  当引擎被破坏时调用。做任何必要的清理，因为此时，渲染器实例已完成。
     */
    public void release()
    {
        if(live2DModel==null)return;
        live2DModel.deleteTextures();
    }

    /**
     * 控制头的朝向
     *
     * @param x -1 ~ 1
     * @param y -1 ~ 1
     */
    public void setHeadForwards(float x, float y){
        ModelParam.HEAD_X = x;
        ModelParam.HEAD_Y = y;
        Log.v(TAG, "head_x: " + x);
        Log.v(TAG, "head_y: " + y);
    }

    /**
     * 控制眼睛的朝向
     *
     * @param x -1 ~ 1
     * @param y -1 ~ 1
     */
    public void setEyeForwards(float x, float y){
        ModelParam.EYE_BALL_X = x;
        ModelParam.EYE_BALL_Y = y;
        Log.v(TAG, "eye_ball_x: " + x);
        Log.v(TAG, "eye_ball_Y: " + y);
    }

    /**
     * 控制嘴巴的张闭程度
     *
     * @param x 0 ~ 1
     */
    public void setMouth(float x){
        ModelParam.MOUTH_OPEN = x;
//        Log.v(TAG, "mouth: " + x);
    }

    /**
     * 控制眨眼的程度
     *
     * @param l 0 ~ 1   左眼
     * @param r 0 ~ 1   右眼
     */
    public void setBlink(float l, float r){
        ModelParam.EYE_L = l;
        ModelParam.EYE_R = r;
        Log.v(TAG, "eye_l: " + l);
        Log.v(TAG, "eye_r: " + r);
    }
}