package com.rs.anergine.render.pipeline;

import com.rs.anergine.render.pipelineComponent.PCMatrixMVP;
import com.rs.anergine.render.pipelineComponent.PCTexture;

public class PipelineTexture extends Pipeline {
    private static final String vertexShaderCode =
            "" +
                    "uniform mat4 uMatrixMVP;" +
                    "attribute vec4 aPosition;" +
                    "attribute vec2 aTexcoord;" +
                    "varying vec2 vTexcoord;" +
                    "void main() {" +
                    "  gl_Position = uMatrixMVP * aPosition;" +
                    "  vTexcoord = aTexcoord;" +
                    "}";

    private static final String fragmentShaderCode =
            "" +
                    "precision mediump float;" +
                    "uniform sampler2D uTexture;" +
                    "varying vec2 vTexcoord;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D(uTexture, vTexcoord);" +
                    "}";

    public PipelineTexture() {
        super();

        new PCMatrixMVP(this);
        new PCTexture(this);

        super.init(vertexShaderCode, fragmentShaderCode);
    }
}
