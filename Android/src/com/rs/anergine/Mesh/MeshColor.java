package com.rs.anergine.Mesh;

import com.rs.anergine.MeshComponent.MCColor;
import com.rs.anergine.MeshComponent.MCMatrixWorld;
import com.rs.anergine.MeshComponent.MCPositionBuffer;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class MeshColor extends Mesh {
  public MeshColor(float[] matrixWorld, FloatBuffer positionBuffer, float[] color, ShortBuffer indexBuffer, int primitiveCount, int primitiveType) {
    addComponent(new MCMatrixWorld(matrixWorld));
    addComponent(new MCPositionBuffer(positionBuffer));
    addComponent(new MCColor(color));
    this.indexBuffer = indexBuffer;
    this.primitiveCount = primitiveCount;
    this.primitiveType = primitiveType;
  }
}
