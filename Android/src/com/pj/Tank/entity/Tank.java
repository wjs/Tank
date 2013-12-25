package com.pj.Tank.entity;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 13-11-28
 * Time: 下午2:02
 * To change this template use File | Settings | File Templates.
 */
public class Tank implements Serializable {
	private int id; // 坦克的ID，由下面的name.hashCode()得到
	private String name;	// 玩家一开始手动输入名字
	private int x;	// 坦克中心点的X坐标
	private int y;	// 坦克中心点的Y坐标
	private float runSpeed;	// 速度的值，以像素/秒为单位，正值代表每秒往前指定像素值，负值代表每秒往后制定像素值，0为停止。速度最好不超过300px/s,建议此值不要超过（地图长度/10s）。
	private float runAcceleration;	// 运动加速度，正值为往前的加速度，负值为往后的加速度。
	private float headDirection;	// 坦克头的朝向，范围是0-360度，水平往右为0度，逆时针方向增大。
	private float wheelSpeed;	// 最大转弯角速度，单位为每秒转多少度。
	private int[] bombList;	// 可选择的炮弹种类列表；
	private int currentBomb; // 当前炮弹的种类。
	private long lastShootTime;	// 上一次开炮时间。
	private boolean isShooting;	// 如果上面计算得出符合开炮条件，正在发射炮弹，则该值为true，否则为false。前台检测该值为true时候做炮弹发射特效，画完了把该值置为false。
	private int beShooted;	// 被打中则把它设成相应炮弹的伤害值
	private int HP;	// 坦克生命值。
	private int m;	// 坦克质量

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public float getRunSpeed() {
		return runSpeed;
	}

	public void setRunSpeed(float runSpeed) {
		this.runSpeed = runSpeed;
	}

	public float getRunAcceleration() {
		return runAcceleration;
	}

	public void setRunAcceleration(float runAcceleration) {
		this.runAcceleration = runAcceleration;
	}

	public float getHeadDirection() {
		return headDirection;
	}

	public void setHeadDirection(float headDirection) {
		this.headDirection = headDirection;
	}

	public float getWheelSpeed() {
		return wheelSpeed;
	}

	public void setWheelSpeed(float wheelSpeed) {
		this.wheelSpeed = wheelSpeed;
	}

	public int[] getBombList() {
		return bombList;
	}

	public void setBombList(int[] bombList) {
		this.bombList = bombList;
	}

	public int getCurrentBomb() {
		return currentBomb;
	}

	public void setCurrentBomb(int currentBomb) {
		this.currentBomb = currentBomb;
	}

	public long getLastShootTime() {
		return lastShootTime;
	}

	public void setLastShootTime(long lastShootTime) {
		this.lastShootTime = lastShootTime;
	}

	public boolean isShooting() {
		return isShooting;
	}

	public void setShooting(boolean shooting) {
		isShooting = shooting;
	}

	public int getBeShooted() {
		return beShooted;
	}

	public void setBeShooted(int beShooted) {
		this.beShooted = beShooted;
	}

	public int getHP() {
		return HP;
	}

	public void setHP(int HP) {
		this.HP = HP;
	}

	public int getM() {
		return m;
	}

	public void setM(int m) {
		this.m = m;
	}
}
