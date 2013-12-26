package com.rs.anergine.render.pipelineComponent;

import android.opengl.GLES20;

import com.rs.anergine.render.pipeline.Pipeline;

public class PCMatrixNormal extends PipelineComponent {

    public PCMatrixNormal(Pipeline pipeline) {
        super(pipeline);
    }

    private int matrixHandle;

    @Override
    public void init(int program) {
        matrixHandle = GLES20.glGetUniformLocation(program, "uMatrixNormal");
    }

    @Override
    public void set() {
        Pipeline.MATRIXNORMAL_ULOCATION = matrixHandle;
    }
}
