package com.rs.anergine.io.component;

import android.view.MotionEvent;

import com.pj.Tank.activity.R;
import com.rs.anergine.io.Component;
import com.rs.anergine.io.Position;
import com.rs.anergine.render.model.ModelQuad;

public class Stick implements Component {

    private final float baseRadius = 1.2f;
    private final float stickRadius = 0.5f;

    private Position position;
    private float radius;
    private StickListener listener;

    private ModelQuad base;
    private ModelQuad stick;

    private float d, theta;

    //r is against screen height.
    public Stick(Position p, float r, StickListener listener) {
        this.position = p.another();
        this.radius = r;
        this.listener = listener;

        base = new ModelQuad(R.drawable.ring);
        stick = new ModelQuad(R.drawable.ring);
    }

    @Override
    public void draw() {
//        GL10 gl = OpenglRenderer.gl;
//
//        position = position.toMiddle();
//        gl.glLoadIdentity();
//        float br = radius * baseRadius;
//        gl.glTranslatef(position.x, position.y, 0);
//        gl.glScalef(br, br, 1);
//        base.draw();
//
//        gl.glLoadIdentity();
//        float sr = radius * stickRadius;
//        gl.glTranslatef(position.x + d * (float) Math.cos(theta), position.y + d * (float) Math.sin(theta), 0);
//        gl.glScalef(sr, sr, 1);
//        stick.draw();
    }

    @Override
    public boolean touch(MotionEvent event) {
        position = position.toMiddle();

        if(event.getAction() == MotionEvent.ACTION_DOWN) {

            Position p = new Position(event.getX(), event.getY(), Position.PositionType.SCREEN).toMiddle();
            float dx = p.x - position.x, dy = p.y - position.y;
            d = (float) Math.sqrt(dx * dx + dy * dy);
            if(d > radius) {
                d = 0;
                return false;
            }
            if(d > 0.01f) {
                theta = (float) Math.acos(dx / d);
                if(d > radius) d = radius;
                if(dy < 0) theta = -theta;
            } else {
                d = 0;
                theta = 0;
            }

            listener.move(d, theta);
            return true;

        } else if(event.getAction() == MotionEvent.ACTION_MOVE) {

            Position p = new Position(event.getX(), event.getY(), Position.PositionType.SCREEN).toMiddle();
            float dx = p.x - position.x, dy = p.y - position.y;
            d = (float) Math.sqrt(dx * dx + dy * dy);

            if(d > 0.01f) {
                theta = (float) Math.acos(dx / d);
                if(d > radius) d = radius;
                if(dy < 0) theta = -theta;
            } else {
                d = 0;
                theta = 0;
            }

            listener.move(d, theta);
            return true;

        } else if(event.getAction() == MotionEvent.ACTION_UP) {
            d = 0;
            theta = 0;
            listener.move(d, theta);
            return true;
        } else
            return false;
    }

}
