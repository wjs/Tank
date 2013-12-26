package com.rs.anergine.render.pipelineComponent;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.pj.Tank.render.GameRenderer;
import com.rs.anergine.render.Renderer;
import com.rs.anergine.render.pipeline.Pipeline;

public class PCLightDirection extends PipelineComponent {

    public PCLightDirection(Pipeline pipeline) {
        super(pipeline);
    }

    private int floatHandle;

    @Override
    public void init(int program) {
        floatHandle = GLES20.glGetUniformLocation(program, "uLightDirection");
    }

    @Override
    public void set() {
        Pipeline.LIGHTDIRECTION_ULOCATION = floatHandle;

        float[] matrixTransInvV = new float[16];
        float[] t2 = new float[16];
        Matrix.invertM(t2, 0, Renderer.getInstance().getCamera().getMatrixV(), 0);
        Matrix.transposeM(matrixTransInvV, 0, t2, 0);
        float[] d = new float[4];
        Matrix.multiplyMV(d, 0, matrixTransInvV, 0, GameRenderer.lightDirection, 0);
        float l = 1f/(float)Math.sqrt(d[0]*d[0] + d[1]*d[1] + d[2]*d[2]);
        GLES20.glUniform3f(floatHandle, d[0]*l, d[1]*l, d[2]*l);
    }
}
