package com.rs.anergine.Mesh;

import com.rs.anergine.MeshComponent.MCColorBuffer;
import com.rs.anergine.MeshComponent.MCMatrixWorld;
import com.rs.anergine.MeshComponent.MCPositionBuffer;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class MeshColorBuffer extends Mesh {
  public MeshColorBuffer(float[] matrixWorld, FloatBuffer positionBuffer, FloatBuffer colorBuffer, ShortBuffer indexBuffer, int primitiveCount, int primitiveType) {
    addComponent(new MCMatrixWorld(matrixWorld));
    addComponent(new MCPositionBuffer(positionBuffer));
    addComponent(new MCColorBuffer(colorBuffer));
    this.indexBuffer = indexBuffer;
    this.primitiveCount = primitiveCount;
    this.primitiveType = primitiveType;
  }
}
