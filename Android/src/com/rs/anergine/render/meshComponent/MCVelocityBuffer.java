package com.rs.anergine.render.meshComponent;

import android.opengl.GLES20;

import com.rs.anergine.render.pipeline.Pipeline;

import java.nio.FloatBuffer;

public class MCVelocityBuffer implements MeshComponent {
    private FloatBuffer velocityBuffer;

    public MCVelocityBuffer(FloatBuffer velocityBuffer) {
        this.velocityBuffer = velocityBuffer;
    }

    @Override
    public void set() {
        GLES20.glVertexAttribPointer(Pipeline.VELOCITY_ALOCATION, 3, GLES20.GL_FLOAT, false, 12, velocityBuffer);
    }
}
