package com.rs.anergine.io;

import android.view.MotionEvent;

import java.util.ArrayList;

//a set of components. "Multiple fingers" is not supported for now.
public class ComponentSet {
    private ArrayList<Component> set = new ArrayList<Component>();

    private Component currentActive;

    public void add(Component c) {
        set.add(c);
    }
    public void remove(Component c) {
        set.remove(c);
    }

    public void draw() {
        for(Component c : set)
            c.draw();
    }

    //When DOWN, the selected component will be always be selected until UP.
    public void touch(MotionEvent e) {
        if(e.getAction() == MotionEvent.ACTION_DOWN) {
            for(Component c : set) {
                if(c.touch(e)) {
                    currentActive = c;
                    break;
                }
            }
        } else if(e.getAction() == MotionEvent.ACTION_UP) {
            if(currentActive!=null)
                currentActive.touch(e);
            currentActive = null;
        } else if(e.getAction() == MotionEvent.ACTION_MOVE) {
            if(currentActive!=null) {
                currentActive.touch(e);
            }
        }
    }
}
