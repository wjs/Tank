package com.rs.anergine.render;

import android.content.Context;
import android.opengl.GLES20;

import com.rs.anergine.GameRenderer;
import com.rs.anergine.io.Position;
import com.rs.anergine.render.pass.PipelineSet;
import com.rs.anergine.render.util.ThirdPersonCamera;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Renderer implements android.opengl.GLSurfaceView.Renderer {

    private static Renderer instance;
    public static Renderer getInstance() { return instance; }

    //Context
    private Context context;
    public Context getContext() { return context; }

    //PipelineSet
    private PipelineSet pipelines;
    public PipelineSet getPipelines() { return pipelines; }

    //Camera
    private ThirdPersonCamera gameCamera = new ThirdPersonCamera();
    public void useGameCamera() { camera = gameCamera; camera.action(); }
    private ThirdPersonCamera ioCamera = new ThirdPersonCamera();
    public void useIoCamera() { camera = ioCamera; camera.action(); }
    private ThirdPersonCamera camera = gameCamera;
    public ThirdPersonCamera getCamera() { return camera; }

    private GameRenderer gameRenderer;

    public Renderer(Context context, GameRenderer gameRenderer) {
        this.context = context;
        instance = this;
        this.gameRenderer = gameRenderer;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glClearDepthf(1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LESS);
        GLES20.glDisable(GL10.GL_CULL_FACE);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        pipelines = new PipelineSet();

        gameRenderer.load();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        Position.setSize(width, height);

        gameCamera.setAspect(ratio);
        ioCamera.setAspect(ratio);
        gameRenderer.onChangeSetCamera();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        gameRenderer.render();
    }



}
