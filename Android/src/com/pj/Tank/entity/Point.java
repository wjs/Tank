package com.pj.Tank.entity;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 13-11-28
 * Time: 下午3:42
 * To change this template use File | Settings | File Templates.
 */
public class Point {
	private float x;
	private float y;

	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
