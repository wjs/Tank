package com.rs.anergine.render.meshComponent;

import android.opengl.GLES20;

import com.rs.anergine.render.pipeline.Pipeline;

public class MCTexture implements MeshComponent {
    int texture;

    public MCTexture(int texture) {
        this.texture = texture;
    }

    @Override
    public void set() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
        GLES20.glUniform1i(Pipeline.TEXTURE_ULOCATION, 0);
    }
}
