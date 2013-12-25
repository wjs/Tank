package com.rs.anergine;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class OpenglView extends GLSurfaceView {

  private final OpenglRenderer openglRenderer;

  public OpenglView(Context context) {
    super(context);

    // Create an OpenGL ES 2.0 context.
    setEGLContextClientVersion(2);

    // Set the OpenglRenderer for drawing on the GLSurfaceView
    openglRenderer = new OpenglRenderer(this.getContext());
    setRenderer(openglRenderer);

    // Render the view only when there is a change in the drawing data
    setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
  }

  private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
  private float mPreviousX;
  private float mPreviousY;

  @Override
  public boolean onTouchEvent(MotionEvent e) {
    // MotionEvent reports input details from the touch screen
    // and other input controls. In this case, you are only
    // interested in events where the touch position changed.

    float x = e.getX();
    float y = e.getY();

    switch(e.getAction()) {
    case MotionEvent.ACTION_MOVE:

      float dx = x - mPreviousX;
      float dy = y - mPreviousY;

      openglRenderer.touch(dx, dy);

      //openglRenderer.mAngle += (dx + dy) * TOUCH_SCALE_FACTOR;  // = 180.0f / 320
      requestRender();
    }

    mPreviousX = x;
    mPreviousY = y;
    return true;
  }
}
