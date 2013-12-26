package com.pj.Tank.render;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.rs.anergine.render.mesh.MeshColor;
import com.rs.anergine.render.model.ModelNormalCube;
import com.rs.anergine.render.pass.Pass;
import com.rs.anergine.render.pipeline.Pipeline;

public class ModelBlock extends ModelNormalCube {

    public float x,y,z,sx,sy,sz;
    protected float[] matrixWorldShadow = new float[16];

    public ModelBlock(float x1, float y1, float z1, float x2, float y2, float z2, int resourceId) {
        super(resourceId);
        this.x = (x1 + x2)/2;
        this.y = (y1 + y2)/2;
        this.z = (z1 + z2)/2;
        this.sx = (x2 - x1)/2;
        this.sy = (y2 - y1)/2;
        this.sz = (z2 - z1)/2;
        setPasses();
    }

    @Override
    public void prepare() {
        Matrix.setIdentityM(matrixWorld, 0);
        Matrix.translateM(matrixWorld, 0, x, y, z);
        Matrix.scaleM(matrixWorld, 0, sx, sy, sz);

        Matrix.multiplyMM(matrixWorldShadow, 0, GameRenderer.matrixShadow, 0, matrixWorld, 0);
    }

    @Override
    public void setPasses() {
        setPass(Pass.TANK, Pipeline.LIGHTTEXTURE, meshNormalTexture);

        setPass(Pass.SHADOW, Pipeline.COLOR, new MeshColor(
                matrixWorldShadow, verticesBuffer, GameRenderer.SHADOW_COLOR, indicesBuffer, indicesNumber, GLES20.GL_TRIANGLES
        ));
    }

}
