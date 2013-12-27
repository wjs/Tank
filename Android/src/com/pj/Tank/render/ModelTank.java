package com.pj.Tank.render;

public class ModelTank {

    public ModelTankComponent base;
    public ModelTankComponent turret;

    public ModelTank() {
        base = new ModelTankComponent();
        turret = new ModelTankComponent();
    }

    public void draw(int pass) {
        base.draw(pass);
        turret.draw(pass);
    }
}
