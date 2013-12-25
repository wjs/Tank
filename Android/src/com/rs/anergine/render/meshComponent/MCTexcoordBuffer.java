package com.rs.anergine.render.meshComponent;

import android.opengl.GLES20;

import com.rs.anergine.render.pipeline.Pipeline;

import java.nio.FloatBuffer;

public class MCTexcoordBuffer implements MeshComponent {
    private FloatBuffer texcoordBuffer;

    public MCTexcoordBuffer(FloatBuffer texcoordBuffer) {
        this.texcoordBuffer = texcoordBuffer;
    }

    @Override
    public void set() {
        GLES20.glVertexAttribPointer(Pipeline.TEXCOORDBUFFER_ALOCATION, 2, GLES20.GL_FLOAT, false, 8, texcoordBuffer);
    }
}
