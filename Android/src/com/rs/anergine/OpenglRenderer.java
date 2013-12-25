package com.rs.anergine;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.rs.anergine.Mesh.Mesh;
import com.rs.anergine.Mesh.MeshColorBuffer;
import com.rs.anergine.Model.Pacman;
import com.rs.anergine.Pass.Pass;
import com.rs.anergine.Pipeline.Pipeline;
import com.rs.anergine.Pipeline.PipelineBillboardXZ;
import com.rs.anergine.Pipeline.PipelineColor;
import com.rs.anergine.Pipeline.PipelineColorBuffer;
import com.rs.anergine.Pipeline.PipelineLineStrip;
import com.rs.anergine.Pipeline.PipelineTexture;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenglRenderer implements android.opengl.GLSurfaceView.Renderer {

  private static OpenglRenderer instance;
  public static OpenglRenderer getInstance() { return instance; }

  private static int width, height;
  public static int getWidth() { return width; }
  public static int getHeight() { return height; }

  private static float[] matrixVP = new float[16];
  public static float[] getMatrixVP() {
    return matrixVP;
  }

  private float[] matrixP = new float[16];
  private MeshColorBuffer mesh;
  private Pipeline pipeline;
  private Pacman s;

  private static Context context;

  public static Context getContext() {
    return context;
  }

  public OpenglRenderer(Context context) {
    instance = this;
    OpenglRenderer.context = context;
  }

  @Override
  public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    GLES20.glClearDepthf(1.0f);
    GLES20.glEnable(GLES20.GL_DEPTH_TEST);

    pipeline = new PipelineColorBuffer();
//    mesh = new MeshColorBuffer();

    long t1 = System.currentTimeMillis();
    s = new Pacman(4);
    s.load();
    long t2 = System.currentTimeMillis();
    System.out.println("pacman "+(t2-t1));

    loadPipelines();


//    mesh = new MeshLineStrip();

//    setMesh();

//    setLine();
  }

  private void setMesh() {
    //----------------------------------------------

//    float triangleCoords[] = { // in counterclockwise order:
//        0.0f, 0.622008459f, 0.0f,   // top
//        -0.5f, -0.311004243f, 0.0f,   // bottom left
//        0.5f, -0.311004243f, 0.0f    // bottom right
//    };
//    ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
//    bb.order(ByteOrder.nativeOrder());
//    mesh.positionBuffer = bb.asFloatBuffer();
//    mesh.positionBuffer.put(triangleCoords);
//    mesh.positionBuffer.position(0);

    //----------------------------------------------

//    short indices[] = {
//        0,1,2
//    };
//    ByteBuffer ii = ByteBuffer.allocateDirect(indices.length * 2);
//    ii.order(ByteOrder.nativeOrder());
//    mesh.indexBuffer = ii.asShortBuffer();
//    mesh.indexBuffer.put(indices);
//    mesh.indexBuffer.position(0);
//    mesh.indexCount = 3;

    //----------------------------------------------

//    float textureCoords[] = { // in counterclockwise order:
//        0.5f, 1 - 0.86602540378f,   // top
//        0, 1,   // bottom left
//        1, 1,    // bottom right
//    };
//    ByteBuffer cc = ByteBuffer.allocateDirect(
//        textureCoords.length * 4);
//    cc.order(ByteOrder.nativeOrder());
//    mesh.texcoordBuffer = cc.asFloatBuffer();
//    mesh.texcoordBuffer.put(textureCoords);
//    mesh.texcoordBuffer.position(0);
//
//    mesh.texture = Util.getTexture(R.drawable.arrow);

    //----------------------------------------------

//    float colors[] = {
//        1, 0, 0,
//        0, 1, 0,
//        0, 0, 1
//    };
//    ByteBuffer cc = ByteBuffer.allocateDirect(colors.length * 4);
//    cc.order(ByteOrder.nativeOrder());
//
//    mesh.colorBuffer = cc.asFloatBuffer();
//    mesh.colorBuffer.put(colors);
//    mesh.colorBuffer.position(0);

    //----------------------------------------------

//    mesh.color = new float[]{1,0,1,1};
//    mesh.width = 5;

  }

  @Override
  public void onSurfaceChanged(GL10 gl, int width, int height) {
    OpenglRenderer.width = width;
    OpenglRenderer.height = height;
    GLES20.glViewport(0, 0, width, height);

    float ratio = (float) width / height;

//    Matrix.frustumM(matrixP, 0, -ratio, ratio, -1, 1, 1, 10000);
    Matrix.perspectiveM(matrixP, 0, 45f, ratio, 1, 1000);
  }

  float x = 0, y = 0;
  public void touch(float dx, float dy) {
    x+=dx;
    y+=dy;
  }

  @Override
  public void onDrawFrame(GL10 gl) {
    long t1 = System.currentTimeMillis();
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

    // Set the camera position (View matrix)
    float[] matrixV = new float[16];
    Matrix.setLookAtM(matrixV, 0, 0, 0, 5f, 0f, 0f, 0f, 0f, 1f, 0f);

    // Calculate the projection and view transformation
    Matrix.rotateM(matrixV, 0, y, 1,0,0);
    Matrix.rotateM(matrixV, 0, x, 0,1,0);
    Matrix.multiplyMM(matrixVP, 0, matrixP, 0, matrixV, 0);

    for(int i=0; i!=Pass.COUNT; i++) {
      s.draw(i);

      for(int j=0; j!=Pipeline.COUNT; j++) {
        pipelines[j].flush();
      }
    }


    long t2 = System.currentTimeMillis();
    System.out.println(t2-t1);
  }

  private Pipeline[] pipelines = new Pipeline[Pipeline.COUNT];
  private void loadPipelines() {
    pipelines[Pipeline.PIPELINE_BILLBOARDXZ] = new PipelineBillboardXZ();
    pipelines[Pipeline.PIPELINE_COLOR] = new PipelineColor();
    pipelines[Pipeline.PIPELINE_COLORBUFFER] = new PipelineColorBuffer();
    pipelines[Pipeline.PIPELINE_LINESTRIP] = new PipelineLineStrip();
    pipelines[Pipeline.PIPELINE_TEXTURE] = new PipelineTexture();
  }

  public void addMesh(Mesh mesh, int pipeline) {
    pipelines[pipeline].add(mesh);
  }

  public static void checkGlError(String glOperation) {
    int error;
    if((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
      Log.e("Renderer", glOperation + ": glError " + error);
      throw new RuntimeException(glOperation + ": glError " + error);
    }
  }
}
