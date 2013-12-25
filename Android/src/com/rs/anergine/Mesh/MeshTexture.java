package com.rs.anergine.Mesh;

import com.rs.anergine.MeshComponent.MCMatrixWorld;
import com.rs.anergine.MeshComponent.MCPositionBuffer;
import com.rs.anergine.MeshComponent.MCTexcoordBuffer;
import com.rs.anergine.MeshComponent.MCTexture;
import com.rs.anergine.Util.Holder;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class MeshTexture extends Mesh {
  public MeshTexture(float[] matrixWorld, FloatBuffer positionBuffer, FloatBuffer texcoordBuffer, Holder<Integer> texture, ShortBuffer indexBuffer, int primitiveCount, int primitiveType) {
    addComponent(new MCMatrixWorld(matrixWorld));
    addComponent(new MCPositionBuffer(positionBuffer));
    addComponent(new MCTexcoordBuffer(texcoordBuffer));
    addComponent(new MCTexture(texture));
    this.indexBuffer = indexBuffer;
    this.primitiveCount = primitiveCount;
    this.primitiveType = primitiveType;
  }
}
