package com.rs.anergine.render.meshComponent;

import android.opengl.GLES20;

import com.rs.anergine.render.pipeline.Pipeline;

public class MCPointSize implements MeshComponent {
    private float pointSize;

    public MCPointSize(float pointSize) {
        this.pointSize = pointSize;
    }

    public void set() {
        GLES20.glUniform1f(Pipeline.POINTSIZE_ULOCATION, pointSize);
    }
}
