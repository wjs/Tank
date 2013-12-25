package com.rs.anergine.render.meshComponent;

import android.opengl.GLES20;

import com.rs.anergine.render.pipeline.Pipeline;

import java.nio.FloatBuffer;

public class MCPositionBuffer implements MeshComponent {
    private FloatBuffer positionBuffer;

    public MCPositionBuffer(FloatBuffer positionBuffer) {
        this.positionBuffer = positionBuffer;
    }

    @Override
    public void set() {
        GLES20.glVertexAttribPointer(Pipeline.POSITIONBUFFER_ALOCATION, 3, GLES20.GL_FLOAT, false, 12, positionBuffer);
    }
}
