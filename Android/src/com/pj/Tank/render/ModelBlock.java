package com.pj.Tank.render;

import android.opengl.Matrix;

import com.rs.anergine.render.model.ModelCube;

public class ModelBlock extends ModelCube {

    public float x,y,z,sx,sy,sz;

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
    }

}
