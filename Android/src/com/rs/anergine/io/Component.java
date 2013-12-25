package com.rs.anergine.io;

import android.view.MotionEvent;

//a component should react to motion events and can draw itself.
//event info should be returned using listener of certain component type.
public interface Component {
    public void draw();

    public boolean touch(MotionEvent event);
}
