package com.rs.anergine.render.meshComponent;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.rs.anergine.render.Renderer;
import com.rs.anergine.render.pipeline.Pipeline;

public class MCMatrixNormal implements MeshComponent {
    private float[] matrixWorld;

    public MCMatrixNormal(float[] matrixWorld) {
        this.matrixWorld = matrixWorld;
    }

    private static float[] matrixTransInvWV = new float[16];
    @Override
    public void set() {
        float[] t1 = new float[16];
        float[] t2 = new float[16];
        Matrix.multiplyMM(t1, 0, Renderer.getInstance().getCamera().getMatrixV(), 0, matrixWorld, 0);
        Matrix.invertM(t2, 0, t1, 0);
        Matrix.transposeM(matrixTransInvWV, 0, t2, 0);
        GLES20.glUniformMatrix4fv(Pipeline.MATRIXNORMAL_ULOCATION, 1, false, matrixTransInvWV, 0);
    }
}
