package com.rs.anergine.render.pipeline;

import com.rs.anergine.render.pipelineComponent.PCMatrixMVP;

public class PipelineColorBuffer extends Pipeline {
    //TODO uniform/attribute generated by components.
    private static final String vertexShaderCode =
            "" +
                    "uniform mat4 uMatrixMVP;" +
                    "attribute vec4 aPosition;" +
                    "attribute vec4 aColor;" +
                    "varying vec4 vColor;" +
                    "void main() {" +
                    "  gl_Position = uMatrixMVP * aPosition;" +
                    "  vColor = aColor;" +
                    "}";

    private static final String fragmentShaderCode =
            "" +
                    "precision mediump float;" +
                    "varying vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    public PipelineColorBuffer() {
        super();

        new PCMatrixMVP(this);

        super.init(vertexShaderCode, fragmentShaderCode);
    }
}
