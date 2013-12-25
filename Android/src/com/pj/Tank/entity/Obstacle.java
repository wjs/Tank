package com.pj.Tank.entity;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 13-11-28
 * Time: 下午3:32
 * To change this template use File | Settings | File Templates.
 */
public class Obstacle {
	private int type;	// 障碍种类,障碍物都是矩形，0代表房子，1代表树木，2代表湖泊
	private String name; 	// 障碍名字
	private Point topLeft;
	private Point topRight;
	private Point bottomLeft;
	private Point bottomRight;

	public Obstacle(int type, String name, Point topLeft, Point topRight, Point bottomLeft, Point bottomRight) {
		this.type = type;
		this.name = name;
		this.topLeft = topLeft;
		this.topRight = topRight;
		this.bottomLeft = bottomLeft;
		this.bottomRight = bottomRight;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Point getTopLeft() {
		return topLeft;
	}

	public void setTopLeft(Point topLeft) {
		this.topLeft = topLeft;
	}

	public Point getTopRight() {
		return topRight;
	}

	public void setTopRight(Point topRight) {
		this.topRight = topRight;
	}

	public Point getBottomLeft() {
		return bottomLeft;
	}

	public void setBottomLeft(Point bottomLeft) {
		this.bottomLeft = bottomLeft;
	}

	public Point getBottomRight() {
		return bottomRight;
	}

	public void setBottomRight(Point bottomRight) {
		this.bottomRight = bottomRight;
	}
}
