package com.pj.Tank.logic;

import com.pj.Tank.entity.Point;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 13-12-14
 * Time: 下午1:00
 * To change this template use File | Settings | File Templates.
 */
public interface UIOperation {
	public void tankMove(float runAcceleration, float wheelSpeed, boolean isShooting);
}
