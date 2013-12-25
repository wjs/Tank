package com.rs.anergine.render.mesh;

import com.rs.anergine.render.meshComponent.MCColor;
import com.rs.anergine.render.meshComponent.MCMatrixWorld;
import com.rs.anergine.render.meshComponent.MCPositionBuffer;

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
