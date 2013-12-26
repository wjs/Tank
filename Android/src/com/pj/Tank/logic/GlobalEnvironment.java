package com.pj.Tank.logic;

import com.pj.Tank.entity.*;
import com.pj.Tank.network.socket.CSFather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Queue;

/**
 * Created with IntelliJ IDEA.
 *
 *
 * User: admin
 * Date: 13-11-30
 * Time: 下午6:11
 * To change this template use File | Settings | File Templates.
 */
public class GlobalEnvironment {
	public static GameMap gameMap;	// @
	public static GameMap gameMap2;	// 拿来计算的 @
	public static HashMap<Integer, Tank> tanks;
	public static long localTimer;	// @
	public static int selfTankId;
	public static int otherTankId;
	public static int GAMEMAPE_WIDTH;	// @
	public static int GAMEMAPE_HEIGHT;  // @
	public static final int TANK_RADIUS = 40;	// GameMap2 使用的
	public static final int HORIZONTAL_SLOT_NUM = 1;
	public static final int VERTICAL_SLOT_NUM = 1;
	public static int HORIZONTAL_SLOT_SIZE; // @
	public static int VERTICAL_SLOT_SIZE;	// @
	public static Hashtable<String, ArrayList<Obstacle>> slot_table;	// @
	public static ArrayList<Point> explodingPoints;	// @
	public static Hashtable<Integer, Bomb> bomb_table;	// @
	public static ArrayList<String> infoMessages;
//	public static String serverIP;	// server的IP，如果是服务器端，这个字段为null
//	public static int connectionPort = 12345;	// 服务器端通信端口
//	public static Hashtable<Integer, String> tankId2ClientIP;	// 根据坦克ID寻找其相应IP，里面存所有客户端的东西，客户端用不到这个变量
//	public static ArrayList<Queue<NetPackage>> netPackageQueues;	// 接收到的网络包的队列。
	public static String otherIP;	// 对方的IP
	public static int connectionPort;	// 通信端口
	public static Queue<NetPackage> netPackageQueue;	// 接收到的网络包的队列
	public static CSFather csFather;	// 网络模块client 或者 server 的父类，为了不区分sever或client而建立。
}
