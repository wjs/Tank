package com.rs.anergine.render.meshComponent;

import android.opengl.GLES20;

import com.rs.anergine.render.pipeline.Pipeline;

import java.nio.FloatBuffer;

public class MCNormalBuffer implements MeshComponent {
    private FloatBuffer normalBuffer;

    public MCNormalBuffer(FloatBuffer normalBuffer) {
        this.normalBuffer = normalBuffer;
    }

    @Override
    public void set() {
        GLES20.glVertexAttribPointer(Pipeline.NORMALBUFFER_ALOCATION, 3, GLES20.GL_FLOAT, false, 12, normalBuffer);
    }
}
