package com.rs.anergine.MeshComponent;

import android.opengl.GLES20;

import com.rs.anergine.Util.Holder;

public class MCSize implements MeshComponent {
  private Holder<Float> size;

  public MCSize(Holder<Float> size) {
    this.size = size;
  }

  @Override
  public void set() {
    GLES20.glLineWidth(size.get());
  }
}
