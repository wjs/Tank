package com.rs.anergine.Model;

import com.rs.anergine.Mesh.Mesh;
import com.rs.anergine.Pass.Pass;
import com.rs.anergine.OpenglRenderer;

public abstract class Model {
  protected Mesh[] meshs = new Mesh[Pass.COUNT];
  protected int[] pipelines = new int[Pass.COUNT];

  public final void draw(int pass) {
    Mesh mesh = meshs[pass];
    int pipeline = pipelines[pass];
    if(mesh!=null) {
      OpenglRenderer.getInstance().addMesh(mesh, pipeline);
    }
  }

  protected void setPass(int pass, int pipeline, Mesh mesh) {
    meshs[pass] = mesh;
    pipelines[pass] = pipeline;
  }

}
