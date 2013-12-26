package com.rs.anergine.render.pipelineComponent;

import android.opengl.GLES20;

import com.rs.anergine.render.pipeline.Pipeline;

public class PCLightColor extends PipelineComponent {

    public PCLightColor(Pipeline pipeline) {
        super(pipeline);
    }

    private int floatHandle;

    @Override
    public void init(int program) {
        floatHandle = GLES20.glGetUniformLocation(program, "uLightColor");
    }

    @Override
    public void set() {
        Pipeline.LIGHTCOLOR_ULOCATION = floatHandle;
        GLES20.glUniform3f(floatHandle, 1,1,1);
    }
}
