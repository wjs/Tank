package com.rs.anergine.Mesh;

import com.rs.anergine.MeshComponent.MCColor;
import com.rs.anergine.MeshComponent.MCMatrixWorld;
import com.rs.anergine.MeshComponent.MCPositionBuffer;
import com.rs.anergine.MeshComponent.MCSize;
import com.rs.anergine.Util.Holder;

import java.nio.FloatBuffer;

public class MeshLineStrip extends Mesh {
  public MeshLineStrip(float[] matrixWorld, FloatBuffer positionBuffer, float[] color, Holder<Float> width, int primitiveCount, int primitiveType) {
    addComponent(new MCMatrixWorld(matrixWorld));
    addComponent(new MCPositionBuffer(positionBuffer));
    addComponent(new MCColor(color));
    addComponent(new MCSize(width));
    this.primitiveCount = primitiveCount;
    this.primitiveType = primitiveType;
  }
}
