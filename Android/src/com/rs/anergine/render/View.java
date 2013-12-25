package com.rs.anergine.render;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.rs.anergine.GameRenderer;
import com.rs.anergine.io.IO;

public class View extends GLSurfaceView {

    private IO io;

    public View(Context context, GameRenderer gameRenderer, IO io) {
        super(context);
        this.io = io;
        setEGLContextClientVersion(2);

        setRenderer(new com.rs.anergine.render.Renderer(this.getContext(), gameRenderer));

        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        setPreserveEGLContextOnPause(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        io.touch(event);
        return true;
    }
}
