package com.rs.anergine.render.mesh;

import android.opengl.GLES20;

import com.rs.anergine.render.meshComponent.MCAcceleration;
import com.rs.anergine.render.meshComponent.MCColor;
import com.rs.anergine.render.meshComponent.MCMatrixWorld;
import com.rs.anergine.render.meshComponent.MCPointSize;
import com.rs.anergine.render.meshComponent.MCPositionBuffer;
import com.rs.anergine.render.meshComponent.MCTime;
import com.rs.anergine.render.meshComponent.MCVelocityBuffer;

import java.nio.FloatBuffer;

public class MeshParticle extends Mesh {
    public MeshParticle(float[] matrixWorld, FloatBuffer positionBuffer, FloatBuffer velocityBuffer, float[] acceleration, float[] time, float[] color, float pointSize, int primitiveCount, int primitiveType) {
        addComponent(new MCMatrixWorld(matrixWorld));
        addComponent(new MCPositionBuffer(positionBuffer));
        addComponent(new MCVelocityBuffer(velocityBuffer));
        addComponent(new MCColor(color));
        addComponent(new MCAcceleration(acceleration));
        addComponent(new MCTime(time));
        addComponent(new MCPointSize(pointSize));
        this.primitiveCount = primitiveCount;
        this.primitiveType = primitiveType;
    }

    @Override
    public void draw() {
        GLES20.glDrawArrays(primitiveType, 0, primitiveCount);
    }
}
