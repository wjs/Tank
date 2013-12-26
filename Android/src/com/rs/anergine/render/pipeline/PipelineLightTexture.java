package com.rs.anergine.render.pipeline;

import com.rs.anergine.render.pipelineComponent.PCLightColor;
import com.rs.anergine.render.pipelineComponent.PCLightDirection;
import com.rs.anergine.render.pipelineComponent.PCMatrixMVP;
import com.rs.anergine.render.pipelineComponent.PCMatrixNormal;
import com.rs.anergine.render.pipelineComponent.PCTexture;

public class PipelineLightTexture extends Pipeline {
    private static final String vertexShaderCode =
            "" +
                    "uniform mat4 uMatrixMVP;" +
                    "uniform mat4 uMatrixNormal;" +

                    "uniform vec3 uLightDirection;" +
                    "uniform vec3 uLightColor;" +

                    "attribute vec4 aPosition;" +
                    "attribute vec4 aNormal;" +
                    "attribute vec2 aTexcoord;" +

                    "varying vec2 vTexcoord;" +
                    "varying vec3 vLightColor;" +

                    "void main() {" +
                    "  gl_Position = uMatrixMVP * aPosition;" +
                    "  vec3 normal = normalize((uMatrixNormal * aNormal).xyz);" +
                    "  vLightColor = uLightColor * clamp((0.6 + 0.6 * dot(normal, uLightDirection)), 0.0, 1.0);" +
                    "  vTexcoord = aTexcoord;" +
                    "}";

    private static final String fragmentShaderCode =
            "" +
                    "precision mediump float;" +
                    "uniform sampler2D uTexture;" +
                    "varying vec2 vTexcoord;" +
                    "varying vec3 vLightColor;" +
                    "void main() {" +
                    "  gl_FragColor = vec4(vLightColor, 1.0) * texture2D(uTexture, vTexcoord);" +
                    "}";

    public PipelineLightTexture() {
        super();

        new PCLightColor(this);
        new PCLightDirection(this);
        new PCMatrixNormal(this);
        new PCMatrixMVP(this);
        new PCTexture(this);

        super.init(vertexShaderCode, fragmentShaderCode);
    }
}
