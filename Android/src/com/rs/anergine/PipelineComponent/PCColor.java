package com.rs.anergine.PipelineComponent;

import android.opengl.GLES20;

import com.rs.anergine.MeshComponent.Location;

public class PCColor extends PipelineComponent {

  private int colorLocation;

  @Override
  public void init(int program) {
    colorLocation = GLES20.glGetUniformLocation(program, "uColor");
  }

  @Override
  public void set() {
    Location.COLOR_ULOCATION = colorLocation;
  }
}
