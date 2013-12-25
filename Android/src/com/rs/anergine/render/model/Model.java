package com.rs.anergine.render.model;

import android.opengl.Matrix;

import com.rs.anergine.render.Renderer;
import com.rs.anergine.render.mesh.Mesh;
import com.rs.anergine.render.pass.Pass;

public abstract class Model {
    public float matrixWorld[] = new float[16];
    {
        Matrix.setIdentityM(matrixWorld, 0);
    }

    private int[] pipelineList = new int[Pass.PASS_COUNT];
    private Mesh[] meshList = new Mesh[Pass.PASS_COUNT];

    protected final void setPass(int pass, int pipeline, Mesh mesh) {
        pipelineList[pass] = pipeline;
        meshList[pass] = mesh;
    }

    public abstract void setPasses();

    public abstract void prepare();

    public final void draw(int pass) {
        prepare();
        Renderer.getInstance().getPipelines().addMesh(pipelineList[pass], meshList[pass]);
    }
}
