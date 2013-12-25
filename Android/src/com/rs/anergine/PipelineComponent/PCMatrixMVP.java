package com.rs.anergine.PipelineComponent;

import android.opengl.GLES20;

import com.rs.anergine.MeshComponent.Location;

public class PCMatrixMVP extends PipelineComponent {

  private int matrixMVPLocation;

  @Override
  public void init(int program) {
    matrixMVPLocation = GLES20.glGetUniformLocation(program, "uMatrixMVP");
  }

  @Override
  public void set() {
    Location.MATRIXMVP_ULOCATION = matrixMVPLocation;
  }
}
