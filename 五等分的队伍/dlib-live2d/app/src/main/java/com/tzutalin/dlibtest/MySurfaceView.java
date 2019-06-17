package com.tzutalin.dlibtest;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class MySurfaceView extends GLSurfaceView {

    private MyRenderer renderer;
    private Context context;

    public MySurfaceView(Context context) {
        super(context);

        this.context = context;
        renderer = new MyRenderer(context);
        setRenderer(renderer);
        setRenderMode(RENDERMODE_CONTINUOUSLY);
    }

    public MyRenderer getRenderer() {
        return renderer;
    }
}
