package com.pj.Tank.logic.impl;

import com.pj.Tank.entity.NetPackage;
import com.pj.Tank.entity.Obstacle;
import com.pj.Tank.entity.Point;
import com.pj.Tank.entity.Tank;
import com.pj.Tank.logic.GlobalEnvironment;
import com.pj.Tank.logic.UIOperation;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 13-12-14
 * Time: 下午1:06
 * To change this template use File | Settings | File Templates.
 */
public class UIOperationImpl implements UIOperation {

	@Override
	public void tankMove(float power, float wheelSpeed, boolean isShooting) {
		// ---------------------------------------------------------------------------------
		// 1、explodingPoints字段和所有坦克的isShooting和isShooted字段重置
		// ---------------------------------------------------------------------------------
		GlobalEnvironment.explodingPoints.clear();
		GlobalEnvironment.infoMessages.clear();;
		Set<Integer> tankKeySet = GlobalEnvironment.tanks.keySet();
		Iterator<Integer> tankKeySetIterator = tankKeySet.iterator();
		while (tankKeySetIterator.hasNext()) {
			int tmpTankId = tankKeySetIterator.next();
			GlobalEnvironment.tanks.get(tmpTankId).setBeShooted(0);
			GlobalEnvironment.tanks.get(tmpTankId).setShooting(false);
		}


		// （1）计算两次调用的时间间隔 -----------------------------
		long currentTimer = System.currentTimeMillis();
		int t = (int) (currentTimer - GlobalEnvironment.localTimer);	// 两次调用的时间间隔。
		GlobalEnvironment.localTimer = currentTimer;

		// ---------------------------------------------------------------------------------
		// 2、利用网络模块收包并进行处理
		// ---------------------------------------------------------------------------------
		receiveNetPackage(t);

		// ---------------------------------------------------------------------------------
		// 3、计算己方坦克的各项参数
		// ---------------------------------------------------------------------------------
		Tank myTank = GlobalEnvironment.tanks.get(GlobalEnvironment.selfTankId);
		pureMove(myTank, t, power, wheelSpeed);
		// （7）如果自己的坦克开炮 --------------------------------
		shooting(myTank);


		// ---------------------------------------------------------------------------------
		// 4、检查己方坦克的被命中情况
		// ---------------------------------------------------------------------------------
		if (myTank.getBeShooted() > 0) {
			myTank.setHP(myTank.getHP() - myTank.getBeShooted());
			if (myTank.getHP() <= 0) {
				GlobalEnvironment.infoMessages.add("你挂了，游戏结束！");
			}
		}


		// ---------------------------------------------------------------------------------
		// 5、利用网络模块发包
		// ---------------------------------------------------------------------------------
		NetPackage np = new NetPackage();
		np.setNetTank(myTank);
		np.setNetBeShooted(GlobalEnvironment.tanks.get(GlobalEnvironment.otherTankId).getBeShooted());
		np.setNetExplodingPoints(GlobalEnvironment.explodingPoints);

	}

	/**
	 * 为了重用，把之前坦克移动的代码分离出来，可以计算一辆坦克在t时间内运动情况
	 * @param myTank
	 * @param t
	 * @param power
	 * @param wheelSpeed
	 */
	private void pureMove(Tank myTank, int t, float power, float wheelSpeed) {
		// （2）理论计算的位移 -------------------------------------
		float s = myTank.getRunSpeed() * t;	// 理论位移
		float angle = myTank.getHeadDirection() + myTank.getWheelSpeed() * t;	// 理论当前朝向
		int tmpX = (int) (myTank.getX() + s * Math.cos(angle));
		int tmpY = (int) (myTank.getY() + s * Math.sin(angle));

		// （3）直线速度 -------------------------------------------
		if (myTank.getRunAcceleration() == 0 && myTank.getRunSpeed() != 0) {
			myTank.setRunSpeed(myTank.getRunSpeed() * GlobalEnvironment.runSpeedFadingRate);
		} else {
			myTank.setRunSpeed(myTank.getRunSpeed() + myTank.getRunAcceleration() * t);
		}

		//（4） 坦克朝向 ------------------------------------------
		myTank.setHeadDirection(angle);

		// （5）刷新角速度和直线加速度 ----------------------------
		myTank.setWheelSpeed(wheelSpeed);
		myTank.setRunAcceleration((power - GlobalEnvironment.gameMap.getFriction() * myTank.getRunSpeed()) / myTank.getM());

		// （6）进行碰撞判断 --------------------------------------
		// 先获得位移起点终点的slotId
		Point start = new Point(myTank.getX(), myTank.getY());
		Point end = new Point(tmpX, tmpY);
		int[] startPointSlotId = getSlotIdByPoint(start);
		int[] endPointSlotId = getSlotIdByPoint(end);
		// 如果终点落在某个obstacle里面的话
		if (!isSafePoint(new Point(tmpX, tmpY), endPointSlotId)) {
			ArrayList<Point[]> crossPoints = getCrossPointListBySlotIds(start, end, startPointSlotId, endPointSlotId);
			// 如果有多个交叉点，再确定最近的那个点precise_collision
			Point[] precise_collision = null;
			if (crossPoints.size() == 1) {
				precise_collision = crossPoints.get(0);
			} else if (crossPoints.size() > 1) {
				precise_collision = getClosePoint(start, crossPoints);
			}
			// 修改坦克的位置，朝向,速度投影，垂直或水平撞的不用变朝向
			if (precise_collision != null && precise_collision.length == 3) {
				// 速度投影
				myTank.setRunSpeed((int) (myTank.getRunSpeed() * Math.abs(Math.cos(myTank.getHeadDirection()))));
				// 位置
				myTank.setX(precise_collision[1].getX());
				myTank.setY(precise_collision[1].getY());
				// 朝向
				if (precise_collision[0].getX() < precise_collision[2].getX()) {
					myTank.setHeadDirection(0);
				} else if (precise_collision[0].getX() > precise_collision[2].getX()) {
					myTank.setHeadDirection(180);
				} else if (precise_collision[0].getY() < precise_collision[2].getY()) {
					myTank.setHeadDirection(270);
				} else if (precise_collision[0].getY() > precise_collision[2].getY()) {
					myTank.setHeadDirection(90);
				}
			} else if (precise_collision != null && precise_collision.length == 1) {
				// 速度变为0
				myTank.setRunSpeed(0);
				// 位置
				myTank.setX(precise_collision[0].getX());
				myTank.setY(precise_collision[0].getY());
			}
		}
	}

	/**
	 * 根据点的坐标，获得点所在的 slot 的id，即一个int[]
	 * @param p
	 * @return
	 */
	private int[] getSlotIdByPoint(Point p) {
		int[] slotId = new int[2];
		slotId[0] = (int) (p.getX() / GlobalEnvironment.HORIZONTAL_SLOT_SIZE);
		slotId[1] = (int) (p.getY() / GlobalEnvironment.VERTICAL_SLOT_SIZE);
		return slotId;
	}

	/**
	 * 判断终点是否再安全区域，如果在某个obstacle里面则是不安全的
	 * @param endPoint
	 * @param endPointSlotId
	 * @return
	 */
	private boolean isSafePoint(Point endPoint, int[] endPointSlotId) {
		int x1, x2, y1, y2;
		for (Obstacle o : GlobalEnvironment.slot_table.get(intArray2String(endPointSlotId))) {
			x1 = (int) o.getTopLeft().getX();
			y1 = (int) o.getTopLeft().getY();
			x2 = (int) o.getBottomRight().getX();
			y2 = (int) o.getBottomRight().getY();
			if (endPoint.getX() >= x1 && endPoint.getX() <= x2 && endPoint.getY() >= y1 && endPoint.getY() <= y2) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 通过起点，终点，起点slotId，终点slotId，返回所有的交点，这是在已经判断出终点在obstacle里面的情况，才会调用这个方法
	 * @param start
	 * @param end
	 * @param startPointSlotId
	 * @param endPointSlotId
	 * @return
	 */
	private ArrayList<Point[]> getCrossPointListBySlotIds(Point start, Point end, int[] startPointSlotId, int[] endPointSlotId) {
		ArrayList<Point[]> crossPoints = new ArrayList<Point[]>();
		ArrayList<Obstacle> tmpObstacleList = GlobalEnvironment.slot_table.get(intArray2String(endPointSlotId));
		// 如果 start 和 end 在不同的 slot 里面
		if (!(startPointSlotId[0] == endPointSlotId[0] && startPointSlotId[1] == endPointSlotId[1])) {
			tmpObstacleList.addAll(GlobalEnvironment.slot_table.get(intArray2String(startPointSlotId)));
		}
		for (Obstacle o : tmpObstacleList) {
			// 位移的线如果是水平或者垂直的时候，就是没有斜率或者斜率为0时,此时往 crossPoints 里面塞的 Point[] 长度是1，就是那个交点而已
			if (start.getX() == end.getX()) {
				Point[] cp = new Point[1];
				// 如果从上往下走
				if (start.getY() < end.getY()) {
					cp[0] = new Point(start.getX(), o.getTopLeft().getY());
				} else {
					// 如果从下往上走
					cp[0] = new Point(start.getX(), o.getBottomLeft().getY());
				}
				crossPoints.add(cp);
			} else if (start.getY() == end.getY()) {
				Point[] cp = new Point[1];
				// 如果从左往右走
				if (start.getX() < end.getX()) {
					cp[0] = new Point(o.getTopLeft().getX(), start.getY());
				} else {
					// 如果从右往左走
					cp[0] = new Point(o.getTopRight().getX(), start.getY());
				}
				crossPoints.add(cp);
			} else {
				// 存在斜率的情况，此时往 crossPoints 里面塞的 Point[] 长度是3，另外两个是所交的边的两个端点
				Point[] cp1 = getCrossPoint(start, end, o.getTopLeft(), o.getTopRight());
				Point[] cp2 = getCrossPoint(start, end, o.getTopRight(), o.getBottomRight());
				Point[] cp3 = getCrossPoint(start, end, o.getBottomLeft(), o.getBottomRight());
				Point[] cp4 = getCrossPoint(start, end, o.getTopLeft(), o.getBottomLeft());
				if (cp1 != null) {
					crossPoints.add(cp1);
				}
				if (cp2 != null) {
					crossPoints.add(cp2);
				}
				if (cp3 != null) {
					crossPoints.add(cp3);
				}
				if (cp4 != null) {
					crossPoints.add(cp4);
				}
			}
		}
		return crossPoints;
	}

	/**
	 * 根据两条线段的4个端点，确定是否有交点，有的话返回三个点的数组，没有返回null
	 * @param start
	 * @param end
	 * @param p1
	 * @param p2
	 * @return
	 */
	private Point[] getCrossPoint(Point start, Point end, Point p1, Point p2) {
		double k = (start.getY() - end.getY()) / (start.getX() - end.getX());
		int x1 = (int) p1.getX();
		int y1 = (int) p1.getY();
		int x2 = (int) p2.getX();
		int y2 = (int) p2.getY();
		// obstacle的水平的边
		if (y1 == y2) {
			int cx = (int) (y1 / k);
			if (cx > x1 && cx < x2) {
				Point[] ps = new Point[3];
				ps[1] = new Point(cx, y1);
				if (start.getX() < end.getX()) {
					ps[0] = new Point(x1, y1);
					ps[2] = new Point(x2, y1);
				} else {
					ps[0] = new Point(x2, y1);
					ps[2] = new Point(x1, y1);
				}
				return ps;
			}
		}
		// obstacle的竖直的边
		if (x1 == x2) {
			int cy = (int) (x1 * k);
			if (cy > y1 && cy < y2) {
				Point[] ps = new Point[3];
				ps[1] = new Point(x1, cy);
				if (start.getY() < end.getY()) {
					ps[0] = new Point(x1, y1);
					ps[2] = new Point(x1, y2);
				} else {
					ps[0] = new Point(x1, y2);
					ps[2] = new Point(x1, y1);
				}
				return ps;
			}
		}
		return null;
	}

	/**
	 * 得出交叉点的列表之后，再通过此方法，得到最靠近起点的那个点
	 * @param start
	 * @param crossPoints
	 * @return
	 */
	private Point[] getClosePoint(Point start, ArrayList<Point[]> crossPoints) {
		Point[] precise_collision = crossPoints.get(0);
		int minLength = (int) Math.abs(start.getX() - precise_collision[1].getX());
		for (int i = 1; i < crossPoints.size(); i++) {
			// 此时的pp长度应该是3，里面是有三个点的，因为垂直或水平撞的时候不可能有多个交叉点
			int tmpLength = (int) Math.abs(start.getX() - crossPoints.get(i)[1].getX());
			if (tmpLength < minLength) {
				minLength = tmpLength;
				precise_collision = crossPoints.get(i);
			}
		}
		return precise_collision;
	}

	/**
	 * 自己的坦克发射炮弹时调用的方法，根据天翼的伪代码写的，先看伪代码更容易理解这段代码
	 * @param myTank
	 */
	private void shooting(Tank myTank) {
		Point startPoint = new Point(myTank.getX(), myTank.getY());
		Point endPoint = getEndPointWithBoundary(startPoint, myTank.getHeadDirection());

		ArrayList<Point> tankHitPoint = new ArrayList<Point>();		// 弹道线经过（被命中）的坦克的弹着点
		ArrayList<Point> obstacleHitPoint = new ArrayList<Point>();	// 弹道线经过（被命中）的障碍物的弹着点
		Hashtable<Point,Tank> tankHit = new Hashtable<Point, Tank>();		// 弹道线经过（被命中）的坦克的弹着点及其实例

		// 计算除己方坦克之外的其他坦克是否被命中
		for(Tank curTank : GlobalEnvironment.tanks.values()) {
			if (curTank.getId() != GlobalEnvironment.selfTankId) {
				float len;	// 坦克中心到炮弹轨迹线的距离
				Point p;	// 坦克中心到炮弹轨迹线的距离最近的点
				if (startPoint.getY() == endPoint.getY()) {
					// 水平的炮弹
					len = Math.abs(curTank.getY() - startPoint.getY());
					p = new Point(curTank.getX(), startPoint.getY());
				} else if (startPoint.getX() == endPoint.getX()) {
					// 竖直的炮弹
					len = Math.abs(curTank.getX() - startPoint.getX());
					p = new Point(startPoint.getX(), curTank.getY());
				} else {
					// 炮弹有斜率
					float k = (startPoint.getY() - endPoint.getY()) / (startPoint.getX() - endPoint.getX());
					int x0 = (int) (curTank.getY() / k);
					len = Math.abs(curTank.getX() - x0);
					p = new Point(x0, curTank.getY());
				}
				if(len <= GlobalEnvironment.TANK_RADIUS) {
					// 该坦克在弹道线上
					tankHitPoint.add(p);
					tankHit.put(p, curTank);
				}
			}
		}

		// 计算障碍物的弹着点（一定会有的，至少会在边界上产生一个弹着点）这一段稍微复杂一些
		// 找到开炮点所在的分区ID
		int[] startPointSlotId = getSlotIdByPoint(startPoint);
		// 进行迭代扩展式搜索：
		ArrayList<int[]> slotsWaitForSearch ;
		int x1, x2, y1, y2;	// 这四个点是遍历obstacle的时候所用的，不在循环里声明而已。
		// 搜索的深度初始值为0，表示不扩展，仅搜索开炮点所在分区。如果尚未找到障碍物弹着点，则增加深度并继续搜索。
		for(int searchDepth = 0; obstacleHitPoint.size() == 0 ; searchDepth++) {
			// 根据搜索深度确定此次需要搜索的分区列表。计算的方法见最后的注释<1>
			slotsWaitForSearch = decideSlotsToBeSearched(startPointSlotId, searchDepth);
			// 遍历slotsWaitForSearch中每一个分区的所有障碍物，判断其与shotLine的交点
			for(int[] slotId : slotsWaitForSearch) {
				for(Obstacle o : GlobalEnvironment.slot_table.get(intArray2String(slotId))) {
					x1 = (int) o.getTopLeft().getX();
					y1 = (int) o.getTopLeft().getY();
					x2 = (int) o.getBottomRight().getX();
					y2 = (int) o.getBottomRight().getY();
					if (startPoint.getY() == endPoint.getY()) {
						// 水平的炮弹
						if(!((y1 < startPoint.getY() && y2 < startPoint.getY()) || (y1 > startPoint.getY() && y2 > startPoint.getY())))	{
							if (startPoint.getX() < endPoint.getX()) {
								obstacleHitPoint.add(new Point(x1, startPoint.getY()));
							} else {
								obstacleHitPoint.add(new Point(x2, startPoint.getY()));
							}
						}
					} else if (startPoint.getX() == endPoint.getX()) {
						// 竖直的炮弹
						if(!((x1 < startPoint.getX() && x2 < startPoint.getX()) || (x1 > startPoint.getX() && x2 > startPoint.getX())))	{
							if (startPoint.getY() < endPoint.getY()) {
								obstacleHitPoint.add(new Point(startPoint.getX(), y1));
							} else {
								obstacleHitPoint.add(new Point(startPoint.getX(), y2));
							}
						}
					} else {
						// 炮弹有斜率,此时往 crossPoints 里面塞的 Point[] 长度是3，另外两个是所交的边的两个端点
						Point[] cp1 = getCrossPoint(startPoint, endPoint, o.getTopLeft(), o.getTopRight());
						Point[] cp2 = getCrossPoint(startPoint, endPoint, o.getTopRight(), o.getBottomRight());
						Point[] cp3 = getCrossPoint(startPoint, endPoint, o.getBottomLeft(), o.getBottomRight());
						Point[] cp4 = getCrossPoint(startPoint, endPoint, o.getTopLeft(), o.getBottomLeft());
						ArrayList<Point[]> crossPoints = new ArrayList<Point[]>();
						if (cp1 != null) {
							crossPoints.add(cp1);
						}
						if (cp2 != null) {
							crossPoints.add(cp2);
						}
						if (cp3 != null) {
							crossPoints.add(cp3);
						}
						if (cp4 != null) {
							crossPoints.add(cp4);
						}
						Point realCrossPoint = null;	// 真是的交点
						if (crossPoints.size() == 1) {
							realCrossPoint = crossPoints.get(0)[1];
						} else if (crossPoints.size() > 1) {
							realCrossPoint = getClosePoint(startPoint, crossPoints)[1];
						}
						if(realCrossPoint != null) {
							obstacleHitPoint.add(realCrossPoint);
						}
					}
				}
			}
		}

		// 根据弹着点与开炮点的距离排序（从近到远）
		Point nearestTankHitPoint = null;
		Point nearestObstacleHitPoint = null;
		if (tankHitPoint.size() > 0) {
			nearestTankHitPoint = tankHitPoint.get(0);
		}
		if (obstacleHitPoint.size() > 0) {
			nearestObstacleHitPoint = obstacleHitPoint.get(0);
		}
		for (int i = 1; i < tankHitPoint.size(); i++) {
			if (Math.abs(startPoint.getX() - nearestTankHitPoint.getX()) > Math.abs(startPoint.getX() - tankHitPoint.get(i).getX())) {
				nearestTankHitPoint = tankHitPoint.get(i);
			}
		}
		for (int i = 1; i < obstacleHitPoint.size(); i++) {
			if (Math.abs(startPoint.getX() - nearestObstacleHitPoint.getX()) > Math.abs(startPoint.getX() - obstacleHitPoint.get(i).getX())) {
				nearestObstacleHitPoint = obstacleHitPoint.get(i);
			}
		}

		// 在tankHitPoint和obstacleHitPoint各自的第一项中选择最近的弹着点
		if (nearestObstacleHitPoint != null && nearestTankHitPoint != null) {
			if(Math.abs(startPoint.getX() - nearestTankHitPoint.getX()) < Math.abs(startPoint.getX() - nearestObstacleHitPoint.getX())) {
				tankHit.get(nearestTankHitPoint).setBeShooted(GlobalEnvironment.bomb_table.get(myTank.getCurrentBomb()).getDamage());
			} else {
				GlobalEnvironment.explodingPoints.add(nearestObstacleHitPoint);
			}
		} else if (nearestObstacleHitPoint != null) {
			GlobalEnvironment.explodingPoints.add(nearestObstacleHitPoint);
		} else if (nearestTankHitPoint != null) {
			tankHit.get(nearestTankHitPoint).setBeShooted(GlobalEnvironment.bomb_table.get(myTank.getCurrentBomb()).getDamage());
		}
	}

	/**
	 * 该方法获得发炮点跟地图四个边界交点
	 * @param startPoint
	 * @param angle
	 * @return
	 */
	private Point getEndPointWithBoundary(Point startPoint, float angle) {
		Point resultPoint = null;

		if (angle == 0) {
			resultPoint = new Point(GlobalEnvironment.GAMEMAPE_WIDTH, startPoint.getY());
		} else if (angle == 90) {
			resultPoint = new Point(startPoint.getX(), 0);
		} else if (angle == 180) {
			resultPoint = new Point(0, startPoint.getY());
		} else if (angle == 270) {
			resultPoint = new Point(startPoint.getX(), GlobalEnvironment.GAMEMAPE_HEIGHT);
		} else {
			// 直线公式 y = kx + b
			double k = Math.tan(angle);
			double b = startPoint.getY() - k * startPoint.getX();
			if (angle > 0 && angle < 90) {
				// top
				int tmpX = (int) (- b / k);
				if (tmpX < GlobalEnvironment.GAMEMAPE_WIDTH) {
					resultPoint = new Point(tmpX, 0);
				}
				// right
				int tmpY = (int) (k * GlobalEnvironment.GAMEMAPE_WIDTH + b);
				if (tmpY < GlobalEnvironment.GAMEMAPE_HEIGHT) {
					resultPoint = new Point(GlobalEnvironment.GAMEMAPE_WIDTH, tmpY);
				}
			} else if (angle > 90 && angle < 180) {
				// top
				int tmpX = (int) (- b / k);
				if (tmpX < GlobalEnvironment.GAMEMAPE_WIDTH) {
					resultPoint = new Point(tmpX, 0);
				}
				// left
				int tmpY = (int) (Math.abs(b));
				if (tmpY < GlobalEnvironment.GAMEMAPE_WIDTH) {
					resultPoint = new Point(0, tmpY);
				}
			} else if (angle > 180 && angle < 270) {
				// left
				int tmpY = (int) (Math.abs(b));
				if (tmpY < GlobalEnvironment.GAMEMAPE_HEIGHT) {
					resultPoint = new Point(0, tmpY);
				}
				// bottom
				int tmpX = (int) ((GlobalEnvironment.GAMEMAPE_HEIGHT - b) / k);
				if (tmpX < GlobalEnvironment.GAMEMAPE_WIDTH) {
					resultPoint = new Point(tmpX, GlobalEnvironment.GAMEMAPE_HEIGHT);
				}
			} else {
				// right
				int tmpY = (int) (k * GlobalEnvironment.GAMEMAPE_WIDTH + b);
				if (tmpY < GlobalEnvironment.GAMEMAPE_HEIGHT) {
					resultPoint = new Point(GlobalEnvironment.GAMEMAPE_WIDTH, tmpY);
				}
				// bottom
				int tmpX = (int) ((GlobalEnvironment.GAMEMAPE_HEIGHT - b) / k);
				if (tmpX < GlobalEnvironment.GAMEMAPE_WIDTH) {
					resultPoint = new Point(tmpX, GlobalEnvironment.GAMEMAPE_HEIGHT);
				}
			}
		}
		return resultPoint;
	}

	/**
	 * 该方法根据搜索深度n，返回中间slot的外面第n圈的slotId的列表
	 * @param startPointSlotId
	 * @param searchDepth
	 * @return
	 */
	private ArrayList<int[]> decideSlotsToBeSearched(int[] startPointSlotId, int searchDepth) {
		ArrayList<int[]> resultList = new ArrayList<int[]>();
		if (searchDepth > 0) {
			int tmpSize = searchDepth * 2;
			for (int i = 0; i < tmpSize; i++) {
				resultList.add(new int[] {startPointSlotId[0]-searchDepth+i, startPointSlotId[1]-searchDepth});
				resultList.add(new int[] {startPointSlotId[0]+searchDepth, startPointSlotId[1]-searchDepth+i});
				resultList.add(new int[] {startPointSlotId[0]+searchDepth-i, startPointSlotId[1]-searchDepth});
				resultList.add(new int[] {startPointSlotId[0]-searchDepth, startPointSlotId[1]+searchDepth-i});
			}
		} else {
			resultList.add(startPointSlotId);
		}
		return resultList;
	}

	private void receiveNetPackage(int t) {
		// （1）如果没有收到从B发来的网络包，那么本地需要根据全局变量中已有的B的数据，以及T，自行计算B此刻的新的位移、速度、角度，并进行碰撞判断；
		if (GlobalEnvironment.tanks.size() < 2) {
			if (GlobalEnvironment.netPackageQueue.size() > 0) {
				NetPackage p;
				Tank netTank = null;
				for (int i = 0; i < GlobalEnvironment.netPackageQueue.size(); i++) {
					p = GlobalEnvironment.netPackageQueue.remove();
					netTank = p.getNetTank();
					GlobalEnvironment.tanks.put(netTank.getId(), netTank);
					GlobalEnvironment.otherTankId = netTank.getId();
				}
				GlobalEnvironment.infoMessages.add(netTank.getName() + "坦克加入战斗了！");
			}
		} else if (GlobalEnvironment.netPackageQueue.size() == 0) {
			Tank otherTank = GlobalEnvironment.tanks.get(GlobalEnvironment.otherTankId);
			pureMove(otherTank, t, otherTank.getM() * otherTank.getRunAcceleration() + GlobalEnvironment.gameMap.getFriction() * otherTank.getRunSpeed(), otherTank.getWheelSpeed());
		} else if (GlobalEnvironment.netPackageQueue.size() > 0) {
			// （2）如果收到了从B发来的网络包（可能不止一个，全部存在全局变量的队列里），那么将这些包一次性全部取出（并从队列中删除）。假设收到的包依次为P1，P2，P3……其中P1为最新收到的包，那么对于这些包，做以下的操作：
			NetPackage p;
			Tank netTank = null;
			Tank tmpNetTank;
			int beShootedSum = 0;
			for (int i = 0; i < GlobalEnvironment.netPackageQueue.size(); i++) {
				 p = GlobalEnvironment.netPackageQueue.remove();
				 tmpNetTank = p.getNetTank();
				if (netTank == null) {
					netTank = tmpNetTank;
					Tank oldTank = GlobalEnvironment.tanks.get(GlobalEnvironment.otherTankId);
					netTank.setBeShooted(netTank.getBeShooted() + oldTank.getBeShooted());
					netTank.setShooting(netTank.isShooting() | oldTank.isShooting());
					netTank.setHP(Math.min(GlobalEnvironment.tanks.get(netTank.getId()).getHP(), oldTank.getHP()));
				} else {
					netTank.setBeShooted(netTank.getBeShooted() + tmpNetTank.getBeShooted());
					netTank.setShooting(netTank.isShooting() | tmpNetTank.isShooting());
					netTank.setHP(Math.min(GlobalEnvironment.tanks.get(netTank.getId()).getHP(), tmpNetTank.getHP()));
				}
				beShootedSum += p.getNetBeShooted();
				GlobalEnvironment.explodingPoints.addAll(p.getNetExplodingPoints());
			}
			GlobalEnvironment.tanks.put(netTank.getId(), netTank);
			GlobalEnvironment.tanks.get(GlobalEnvironment.selfTankId).setBeShooted(beShootedSum);

			if (GlobalEnvironment.tanks.get(netTank.getId()).getHP() <= 0) {
				GlobalEnvironment.tanks.remove(netTank.getId());
				GlobalEnvironment.infoMessages.add(netTank.getName() + "坦克被击毁！");
			}
		}
	}

	/**
	 * 把 int[] 的数组变成 形如 "1,2" 的String
	 * @param arr
	 * @return
	 */
	private String intArray2String(int[] arr) {
		if (arr.length == 2) {
			return arr[0]+","+arr[1];
		}
		return null;
	}
}
