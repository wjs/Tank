package com.rs.anergine.Pipeline;

import android.opengl.GLES20;
import android.util.Log;

import com.rs.anergine.MeshComponent.Location;
import com.rs.anergine.PipelineComponent.PCColor;
import com.rs.anergine.PipelineComponent.PipelineComponent;
import com.rs.anergine.Mesh.Mesh;

import java.util.ArrayList;
import java.util.List;

public abstract class Pipeline {

  //loaded shaders.
  protected int program;
  //load shader. GLES20.GL_VERTEX_SHADER for vs and GLES20.GL_FRAGMENT_SHADER for ps.
  private static int loadShader(int type, String shaderCode) {
    //TODO cache here.

    //create, bind, compile.
    int shader = GLES20.glCreateShader(type);
    GLES20.glShaderSource(shader, shaderCode);
    GLES20.glCompileShader(shader);
    int[] compiled = new int[1];
    GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
    if (compiled[0] == 0) {
      Log.e("Renderer", "Could not compile shader " + type + ":");
      Log.e("Renderer", GLES20.glGetShaderInfoLog(shader));
      GLES20.glDeleteShader(shader);
      shader = 0;
    }

    return shader;
  }

  //load shaders, link, get handles(component).
  public void init(String vs, String ps) {
    //load shaders
    int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vs);
    int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, ps);

    //combine
    program = GLES20.glCreateProgram();
    GLES20.glAttachShader(program, vertexShader);
    GLES20.glAttachShader(program, pixelShader);

    GLES20.glBindAttribLocation(program, Location.POSITIONBUFFER_ALOCATIONS[0], "aPosition");
    GLES20.glBindAttribLocation(program, Location.POSITIONBUFFER_ALOCATIONS[1], "aPosition1");
    GLES20.glBindAttribLocation(program, Location.COLORBUFFER_ALOCATION, "aColor");
    GLES20.glBindAttribLocation(program, Location.TEXCOORDBUFFER_ALOCATIONS[0], "aTexcoord");
    GLES20.glBindAttribLocation(program, Location.TEXCOORDBUFFER_ALOCATIONS[1], "aTexcoord1");

    GLES20.glLinkProgram(program);
    int[] linkStatus = new int[1];
    GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
    if (linkStatus[0] != GLES20.GL_TRUE) {
      Log.e("Renderer", "Could not link program: ");
      Log.e("Renderer", GLES20.glGetProgramInfoLog(program));
      GLES20.glDeleteProgram(program);
      program = 0;
    }

    for(PipelineComponent component : components) {
      component.init(program);
    }

  }

  //Add multiple times and draw them all. every frame.
  private ArrayList<Mesh> renderList = new ArrayList<Mesh>();

  public void add(Mesh mesh) {
    renderList.add(mesh);
  }

  public void flush() {
    if(renderList.isEmpty())
      return;

    GLES20.glUseProgram(program);

    GLES20.glEnableVertexAttribArray(Location.POSITIONBUFFER_ALOCATIONS[0]);
    GLES20.glEnableVertexAttribArray(Location.POSITIONBUFFER_ALOCATIONS[1]);
    GLES20.glEnableVertexAttribArray(Location.COLORBUFFER_ALOCATION);
    GLES20.glEnableVertexAttribArray(Location.TEXCOORDBUFFER_ALOCATIONS[0]);
    GLES20.glEnableVertexAttribArray(Location.TEXCOORDBUFFER_ALOCATIONS[1]);

    for(PipelineComponent component : components) {
      component.set();
    }

    for(Mesh mesh : renderList) {
      mesh.set();
      mesh.draw();
    }
    renderList.clear();
  }

  private List<PipelineComponent> components = new ArrayList<PipelineComponent>();
  protected void addUniform(PipelineComponent component) {
    components.add(component);
  }

  public static final int COUNT = 5;
  public static final int PIPELINE_COLOR = 0;
  public static final int PIPELINE_COLORBUFFER = 1;
  public static final int PIPELINE_LINESTRIP = 2;
  public static final int PIPELINE_TEXTURE = 3;
  public static final int PIPELINE_BILLBOARDXZ = 4;
}