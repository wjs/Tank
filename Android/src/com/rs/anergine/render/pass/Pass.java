package com.rs.anergine.render.pass;

public abstract class Pass {
    private static int pointer = 0;
    public final static int DRAW = pointer++;


    public final static int PASS_COUNT = pointer;
}
