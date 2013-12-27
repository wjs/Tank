package com.rs.anergine.render.meshComponent;

import android.opengl.GLES20;

import com.rs.anergine.render.pipeline.Pipeline;

public class MCTime implements MeshComponent {
    private float[] time;

    public MCTime(float[] time) {
        this.time = time;
    }

    public void set() {
        GLES20.glUniform1f(Pipeline.TIME_ULOCATION, time[0]);
    }
}
