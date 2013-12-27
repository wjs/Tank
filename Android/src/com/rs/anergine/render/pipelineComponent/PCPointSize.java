package com.rs.anergine.render.pipelineComponent;

import android.opengl.GLES20;

import com.rs.anergine.render.pipeline.Pipeline;

public class PCPointSize extends PipelineComponent {

    public PCPointSize(Pipeline pipeline) {
        super(pipeline);
    }

    private int pointSizeHandle;

    @Override
    public void init(int program) {
        pointSizeHandle = GLES20.glGetUniformLocation(program, "uPointSize");
    }

    @Override
    public void set() {
        Pipeline.POINTSIZE_ULOCATION = pointSizeHandle;
    }
}
