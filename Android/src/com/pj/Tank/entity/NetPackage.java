package com.pj.Tank.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created with IntelliJ IDEA.
 * User: weijinshi
 * Date: 13-12-24
 * Time: 下午2:41
 * To change this template use File | Settings | File Templates.
 */
public class NetPackage implements Serializable {
//	private ArrayList<Tank> netTanks;
//	private Hashtable<Integer, Integer> tankId2BeShooted;
//	private ArrayList<Point> netExplodingPoints;
	private Tank netTank;
	private int netBeShooted;
	private ArrayList<Point> netExplodingPoints;

	public Tank getNetTank() {
		return netTank;
	}

	public void setNetTank(Tank netTank) {
		this.netTank = netTank;
	}

	public int getNetBeShooted() {
		return netBeShooted;
	}

	public void setNetBeShooted(int netBeShooted) {
		this.netBeShooted = netBeShooted;
	}

	public ArrayList<Point> getNetExplodingPoints() {
		return netExplodingPoints;
	}

	public void setNetExplodingPoints(ArrayList<Point> netExplodingPoints) {
		this.netExplodingPoints = netExplodingPoints;
	}
}
