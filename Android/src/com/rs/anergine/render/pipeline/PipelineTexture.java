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
                    "  vec4 color = texture2D(uTexture, vTexcoord);" +
                    "  if(length(color.rgb) > 1.7) color.a = 0.0;" +
                    "  gl_FragColor = color;" +
                    "}";

    public PipelineTexture() {
        super();

        new PCMatrixMVP(this);
        new PCTexture(this);

        super.init(vertexShaderCode, fragmentShaderCode);
    }
}
