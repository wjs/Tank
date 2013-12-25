package com.rs.anergine.render.mesh;

import com.rs.anergine.render.meshComponent.MCMatrixWorld;
import com.rs.anergine.render.meshComponent.MCPositionBuffer;
import com.rs.anergine.render.meshComponent.MCTexcoordBuffer;
import com.rs.anergine.render.meshComponent.MCTexture;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class MeshTexture extends Mesh {
    public MeshTexture(float[] matrixWorld, FloatBuffer positionBuffer, FloatBuffer texcoordBuffer, int texture, ShortBuffer indexBuffer, int primitiveCount, int primitiveType) {
        addComponent(new MCMatrixWorld(matrixWorld));
        addComponent(new MCPositionBuffer(positionBuffer));
        addComponent(new MCTexcoordBuffer(texcoordBuffer));
        addComponent(new MCTexture(texture));
        this.indexBuffer = indexBuffer;
        this.primitiveCount = primitiveCount;
        this.primitiveType = primitiveType;
    }
}
