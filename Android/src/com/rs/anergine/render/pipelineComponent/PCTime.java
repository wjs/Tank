package com.rs.anergine.render.pipelineComponent;

import android.opengl.GLES20;

import com.rs.anergine.render.pipeline.Pipeline;

public class PCTime extends PipelineComponent {

    public PCTime(Pipeline pipeline) {
        super(pipeline);
    }

    private int timeHandle;

    @Override
    public void init(int program) {
        timeHandle = GLES20.glGetUniformLocation(program, "uTime");
    }

    @Override
    public void set() {
        Pipeline.TIME_ULOCATION = timeHandle;
    }
}
