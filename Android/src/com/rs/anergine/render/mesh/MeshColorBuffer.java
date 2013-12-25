package com.rs.anergine.render.mesh;

import com.rs.anergine.render.meshComponent.MCColorBuffer;
import com.rs.anergine.render.meshComponent.MCMatrixWorld;
import com.rs.anergine.render.meshComponent.MCPositionBuffer;

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
