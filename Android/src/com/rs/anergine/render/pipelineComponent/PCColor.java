package com.rs.anergine.render.pipelineComponent;

import android.opengl.GLES20;

import com.rs.anergine.render.pipeline.Pipeline;

public class PCColor extends PipelineComponent {

    public PCColor(Pipeline pipeline) {
        super(pipeline);
    }

    private int colorHandle;

    @Override
    public void init(int program) {
        colorHandle = GLES20.glGetUniformLocation(program, "uColor");
    }

    @Override
    public void set() {
        Pipeline.COLOR_ULOCATION = colorHandle;
    }
}
