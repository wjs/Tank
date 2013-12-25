package com.rs.anergine.io.component;

public interface StickListener {
    //d is against screen height, theta starts from x+ counter-clockwise.
    public void move(float d, float theta);
}
