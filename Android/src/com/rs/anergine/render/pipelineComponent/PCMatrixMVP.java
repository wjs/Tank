package com.rs.anergine.render.pipelineComponent;

import android.opengl.GLES20;

import com.rs.anergine.render.pipeline.Pipeline;

public class PCMatrixMVP extends PipelineComponent {

    public PCMatrixMVP(Pipeline pipeline) {
        super(pipeline);
    }

    private int matrixHandle;

    @Override
    public void init(int program) {
        matrixHandle = GLES20.glGetUniformLocation(program, "uMatrixMVP");
    }

    @Override
    public void set() {
        Pipeline.MATRIXMVP_ULOCATION = matrixHandle;
    }
}
