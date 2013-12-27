package com.pj.Tank.render;

import android.opengl.Matrix;

import com.rs.anergine.render.Renderer;
import com.rs.anergine.render.model.ModelTexture;
import com.rs.anergine.render.pass.Pass;
import com.rs.anergine.render.pipeline.Pipeline;

public class ModelSkyBox extends ModelTexture{

    public ModelSkyBox(int meshFileNumber, int textureId) {
        super(meshFileNumber, textureId);
    }

    @Override
    public void prepare() {
        float[] target = Renderer.getInstance().getCamera().getTarget();
        Matrix.setIdentityM(matrixWorld, 0);
        Matrix.translateM(matrixWorld, 0, target[0], 0, target[2]);
        Matrix.rotateM(matrixWorld, 0, 90, 0, 0, 1);
        Matrix.scaleM(matrixWorld, 0, 10000, 10000, 10000);
    }

    @Override
    public void setPasses() {
        setPass(Pass.TERRAIN, Pipeline.TEXTURE, meshTexture);
    }
}
