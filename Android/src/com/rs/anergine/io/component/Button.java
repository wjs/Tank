package com.rs.anergine.io.component;

import android.opengl.Matrix;
import android.view.MotionEvent;

import com.rs.anergine.io.Component;
import com.rs.anergine.io.Position;
import com.rs.anergine.render.model.ModelQuad;
import com.rs.anergine.render.model.ModelTexture;
import com.rs.anergine.render.pass.Pass;

public class Button implements Component {

    protected Position position;
    protected float width_d2, height_d2;
    protected ButtonListener listener;
    protected ModelTexture currentMesh;
    protected ModelTexture upMesh;
    protected ModelTexture downMesh;

    //w,h are against screen height.
    public Button(Position p, float w, float h, ButtonListener listener, int upIconId, int downIconId) {
        this.position = p.another();
        this.width_d2 = w / 2;
        this.height_d2 = h / 2;
        this.listener = listener;

        //will make mesh support texture switches.
        upMesh = new ModelQuad(upIconId);
        downMesh = new ModelQuad(downIconId);
        currentMesh = upMesh;
    }

    @Override
    public void draw() {
        position = position.toMiddle();
        Matrix.setIdentityM(currentMesh.matrixWorld, 0);
        Matrix.translateM(currentMesh.matrixWorld, 0, position.x, position.y, 0);
        Matrix.scaleM(currentMesh.matrixWorld, 0, width_d2, height_d2, 1);
        currentMesh.draw(Pass.DRAW);
    }

    @Override
    public boolean touch(MotionEvent event) {
        position = position.toMiddle();
        if(event.getAction() == MotionEvent.ACTION_UP) {
            currentMesh = upMesh;
            listener.up();
            return true;
        }
        Position p = new Position(event.getX(), event.getY(), Position.PositionType.SCREEN).toMiddle();
        float dx = Math.abs(p.x - position.x), dy = Math.abs(p.y - position.y);
        if(dx <= width_d2 && dy <= height_d2) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                currentMesh = downMesh;
                listener.down();
                return true;
            } else
                return false;
        } else {
            return false;
        }
    }

}
