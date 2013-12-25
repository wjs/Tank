package com.pj.Tank.entity;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 13-11-28
 * Time: 下午2:03
 * To change this template use File | Settings | File Templates.
 */
public class Bomb {
	private int type;	// 炮弹种类。
	private String name;	// 炮弹名字。
	private int damage;	// 炮弹破坏力。
	private int shootGap;	// 连续两次开炮的最短时间间隔，以毫秒为单位。

	public Bomb() {
	}

	public Bomb(int type, String name, int damage, int shootGap) {
		this.type = type;
		this.name = name;
		this.damage = damage;
		this.shootGap = shootGap;
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

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getShootGap() {
		return shootGap;
	}

	public void setShootGap(int shootGap) {
		this.shootGap = shootGap;
	}
}
