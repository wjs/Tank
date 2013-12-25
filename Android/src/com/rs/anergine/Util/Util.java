package com.rs.anergine.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.rs.anergine.OpenglRenderer;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Util {
  public static int getTexture(int resourceId) {
    InputStream inputStream = OpenglRenderer.getContext().getResources().openRawResource(resourceId);
    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

    int[] textures = new int[1];
    GLES20.glGenTextures(1, textures, 0);
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);

    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
    bitmap.recycle();

    return textures[0];
  }

  public static FloatBuffer getFloatBuffer(float[] data) {
    ByteBuffer bb = ByteBuffer.allocateDirect(data.length * 4);
    bb.order(ByteOrder.nativeOrder());
    FloatBuffer result = bb.asFloatBuffer();
    result.put(data);
    result.position(0);
    return result;
  }
  public static ShortBuffer getShortBuffer(short[] data) {
    ByteBuffer bb = ByteBuffer.allocateDirect(data.length * 2);
    bb.order(ByteOrder.nativeOrder());
    ShortBuffer result = bb.asShortBuffer();
    result.put(data);
    result.position(0);
    return result;
  }
}
