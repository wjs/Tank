package com.rs.anergine.render.pipeline;

import com.rs.anergine.render.pipelineComponent.PCAcceleration;
import com.rs.anergine.render.pipelineComponent.PCColor;
import com.rs.anergine.render.pipelineComponent.PCMatrixMVP;
import com.rs.anergine.render.pipelineComponent.PCPointSize;
import com.rs.anergine.render.pipelineComponent.PCTime;

public class PipelineParticle extends Pipeline {
    //TODO uniform/attribute generated by components.
    private static final String vertexShaderCode =
            "" +
                    "uniform mat4 uMatrixMVP;" +
                    "uniform vec3 uAcceleration;" +
                    "uniform float uTime;" +
                    "uniform float uPointSize;" +
                    "attribute vec4 aPosition;" +
                    "attribute vec4 aVelocity;" +
                    "void main() {" +
                    "  vec3 pos = aPosition.xyz;" +
                    "  pos += uAcceleration * uTime * uTime * 0.5 + aVelocity.xyz * uTime;" +
                    "  if(pos.y < 0.0) pos.y = 0.0;" +
                    "  gl_Position = uMatrixMVP * vec4(pos, 1.0);" +
                    "  gl_PointSize = uPointSize;" +
                    "}";

    private static final String fragmentShaderCode =
            "" +
                    "precision mediump float;" +
                    "uniform vec4 uColor;" +
                    "void main() {" +
                    "  float d = (gl_PointCoord.x - 0.5)*(gl_PointCoord.x - 0.5) + (gl_PointCoord.y - 0.5) * (gl_PointCoord.y - 0.5);" +
                    "  if(d > 0.25) discard;" +
                    "  gl_FragColor = uColor;" +
                    "}";

    public PipelineParticle() {
        super();

        new PCAcceleration(this);
        new PCTime(this);
        new PCMatrixMVP(this);
        new PCColor(this);
        new PCPointSize(this);

        super.init(vertexShaderCode, fragmentShaderCode);
    }
}
