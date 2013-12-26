package com.rs.anergine.render.meshComponent;

import android.opengl.GLES20;

import com.rs.anergine.render.pipeline.Pipeline;

public class MCColor implements MeshComponent {
    private float[] color;

    public MCColor(float[] color) {
        this.color = color;
    }

    public void set() {
        GLES20.glUniform4fv(Pipeline.COLOR_ULOCATION, 1, color, 0);
    }
}
