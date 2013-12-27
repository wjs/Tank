package com.rs.anergine.render.meshComponent;

import android.opengl.GLES20;

import com.rs.anergine.render.pipeline.Pipeline;

public class MCAcceleration implements MeshComponent {
    private float[] acceleration;

    public MCAcceleration(float[] acceleration) {
        this.acceleration = acceleration;
    }

    public void set() {
        GLES20.glUniform3fv(Pipeline.ACCELERATION_ULOCATION, 1, acceleration, 0);
    }
}
