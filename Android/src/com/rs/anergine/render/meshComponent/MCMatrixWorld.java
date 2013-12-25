package com.rs.anergine.render.meshComponent;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.rs.anergine.render.Renderer;
import com.rs.anergine.render.pipeline.Pipeline;

public class MCMatrixWorld implements MeshComponent {
    private float[] matrixWorld;

    public MCMatrixWorld(float[] matrixWorld) {
        this.matrixWorld = matrixWorld;
    }

    private static float[] matrixWVP = new float[16];
    @Override
    public void set() {
        Matrix.multiplyMM(matrixWVP, 0, Renderer.getInstance().getCamera().getMatrixVP(), 0, matrixWorld, 0);
        GLES20.glUniformMatrix4fv(Pipeline.MATRIXMVP_ULOCATION, 1, false, matrixWVP, 0);
    }
}
