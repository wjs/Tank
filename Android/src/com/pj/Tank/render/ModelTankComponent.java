package com.pj.Tank.render;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.pj.Tank.activity.R;
import com.rs.anergine.render.mesh.MeshColor;
import com.rs.anergine.render.model.ModelNormalCube;
import com.rs.anergine.render.pass.Pass;
import com.rs.anergine.render.pipeline.Pipeline;

public class ModelTankComponent extends ModelNormalCube {

    protected float[] matrixWorldShadow;

    public ModelTankComponent() {
        super(R.drawable.tank);
    }

    @Override
    public void prepare() {
        Matrix.multiplyMM(matrixWorldShadow, 0, GameRenderer.matrixShadow, 0, matrixWorld, 0);
    }

    @Override
    public void setPasses() {
        setPass(Pass.TANK, Pipeline.LIGHTTEXTURE, meshNormalTexture);
        matrixWorldShadow = new float[16];
        setPass(Pass.SHADOW, Pipeline.COLOR, new MeshColor(
                matrixWorldShadow, verticesBuffer, GameRenderer.SHADOW_COLOR, indicesBuffer, indicesNumber, GLES20.GL_TRIANGLES
        ));
    }
}
