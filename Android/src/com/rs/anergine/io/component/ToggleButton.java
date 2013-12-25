package com.rs.anergine.io.component;

import android.view.MotionEvent;

import com.rs.anergine.io.Position;

public class ToggleButton extends Button {

    protected boolean down = false;

    public ToggleButton(Position p, float w, float h, ButtonListener listener, int upIconId, int downIconId) {
        super(p, w, h, listener, upIconId, downIconId);
    }

    @Override
    public boolean touch(MotionEvent event) {
        position = position.toMiddle();
        if(event.getAction() == MotionEvent.ACTION_UP) {
            down = !down;
            if(down) {
                currentMesh = downMesh;
                listener.down();
            } else {
                currentMesh = upMesh;
                listener.up();
            }
            return true;
        }
        Position p = new Position(event.getX(), event.getY(), Position.PositionType.SCREEN).toMiddle();
        float dx = Math.abs(p.x - position.x), dy = Math.abs(p.y - position.y);
        if(dx <= width_d2 && dy <= height_d2) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                return true;
            } else
                return false;
        } else {
            return false;
        }
    }

}
