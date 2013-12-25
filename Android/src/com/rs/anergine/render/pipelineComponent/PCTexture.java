package com.rs.anergine.render.pipelineComponent;

import android.opengl.GLES20;

import com.rs.anergine.render.pipeline.Pipeline;

public class PCTexture extends PipelineComponent {

    public PCTexture(Pipeline pipeline) {
        super(pipeline);
    }

    private int textureHandle;

    @Override
    public void init(int program) {
        textureHandle = GLES20.glGetUniformLocation(program, "uTexture");
    }

    @Override
    public void set() {
        Pipeline.TEXTURE_ULOCATION = textureHandle;
    }
}
