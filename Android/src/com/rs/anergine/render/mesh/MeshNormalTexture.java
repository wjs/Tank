package com.rs.anergine.render.mesh;

import com.rs.anergine.render.meshComponent.MCMatrixNormal;
import com.rs.anergine.render.meshComponent.MCMatrixWorld;
import com.rs.anergine.render.meshComponent.MCNormalBuffer;
import com.rs.anergine.render.meshComponent.MCPositionBuffer;
import com.rs.anergine.render.meshComponent.MCTexcoordBuffer;
import com.rs.anergine.render.meshComponent.MCTexture;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class MeshNormalTexture extends Mesh {
    public MeshNormalTexture(float[] matrixWorld, FloatBuffer positionBuffer, FloatBuffer normalBuffer, FloatBuffer texcoordBuffer, int texture, ShortBuffer indexBuffer, int primitiveCount, int primitiveType) {
        addComponent(new MCMatrixWorld(matrixWorld));
        addComponent(new MCMatrixNormal(matrixWorld));
        addComponent(new MCPositionBuffer(positionBuffer));
        addComponent(new MCNormalBuffer(normalBuffer));
        addComponent(new MCTexcoordBuffer(texcoordBuffer));
        addComponent(new MCTexture(texture));
        this.indexBuffer = indexBuffer;
        this.primitiveCount = primitiveCount;
        this.primitiveType = primitiveType;
    }
}
