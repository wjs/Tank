package com.rs.anergine.render.pipelineComponent;

import android.opengl.GLES20;

import com.rs.anergine.render.pipeline.Pipeline;

public class PCAcceleration extends PipelineComponent {

    public PCAcceleration(Pipeline pipeline) {
        super(pipeline);
    }

    private int accelerationHandle;

    @Override
    public void init(int program) {
        accelerationHandle = GLES20.glGetUniformLocation(program, "uAcceleration");
    }

    @Override
    public void set() {
        Pipeline.ACCELERATION_ULOCATION = accelerationHandle;
    }
}
