package com.rs.anergine.render.pipeline;

import com.rs.anergine.render.pipelineComponent.PCAcceleration;
import com.rs.anergine.render.pipelineComponent.PCColor;
import com.rs.anergine.render.pipelineComponent.PCMatrixMVP;
import com.rs.anergine.render.pipelineComponent.PCTime;

public class PipelineParticle extends Pipeline {
    //TODO uniform/attribute generated by components.
    private static final String vertexShaderCode =
            "" +
                    "uniform mat4 uMatrixMVP;" +
                    "uniform vec3 uAcceleration;" +
                    "uniform float uTime;" +
                    "attribute vec4 aPosition;" +
                    "attribute vec4 aVelocity;" +
                    "void main() {" +
                    "  vec3 pos = aPosition.xyz;" +
                    "  pos += uAcceleration * uTime * uTime * 0.5 + aVelocity.xyz * uTime;" +
                    "  gl_Position = uMatrixMVP * vec4(pos, 1.0);" +
                    "  gl_PointSize = 3.0;" +
                    "}";

    private static final String fragmentShaderCode =
            "" +
                    "precision mediump float;" +
                    "uniform vec4 uColor;" +
                    "void main() {" +
                    "  gl_FragColor = uColor;" +
                    "}";

    public PipelineParticle() {
        super();

        new PCAcceleration(this);
        new PCTime(this);
        new PCMatrixMVP(this);
        new PCColor(this);

        super.init(vertexShaderCode, fragmentShaderCode);
    }
}