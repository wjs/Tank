package com.pj.Tank.entity;

import com.pj.Tank.logic.GlobalEnvironment;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 13-11-28
 * Time: 下午3:56
 * To change this template use File | Settings | File Templates.
 */
public class GameMap {
	private int mapId;
	private int width;
	private int height;
	private float friction;
	private ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
	private ArrayList<Point> startPoints = new ArrayList<Point>();
	private String gameMapFileStr;

	public GameMap(int mapId) {
		this.mapId = mapId;
	}

	public GameMap(int mapId, int width, int height, float friction) {
		this.mapId = mapId;
		this.width = width;
		this.height = height;
		this.friction = friction;
	}

	// 以后从配置文件里面读map
	public void generateRandomMap() {
		Scanner scanner = new Scanner(gameMapFileStr);
		String tempStr;
		String[] tempStrArr;
		while (scanner.hasNextLine() && (tempStr = scanner.nextLine()) != null) {
			tempStr = tempStr.trim();
			if (!tempStr.startsWith("#")) {
				tempStrArr = tempStr.split(" ");
				if (tempStrArr.length == 2) {
					Point p = new Point(Integer.parseInt(tempStrArr[0]), Integer.parseInt(tempStrArr[1]));
					startPoints.add(p);
				} else if (tempStrArr.length == 3) {
					width = Integer.parseInt(tempStrArr[0]);
					height = Integer.parseInt(tempStrArr[1]);
					friction = Float.parseFloat(tempStrArr[2]);
				} else if (tempStrArr.length == 5) {
					Obstacle o = null;
					int top = Integer.parseInt(tempStrArr[1]);
					int bottom = Integer.parseInt(tempStrArr[2]);
					int left = Integer.parseInt(tempStrArr[3]);
					int right = Integer.parseInt(tempStrArr[4]);
					Point topLeft = new Point(left, top);
					Point topRight =  new Point(right, top);
					Point bottomRight =  new Point(right, bottom);
					Point bottomLeft =  new Point(left, bottom);
					if (tempStrArr[0].equals("0")) {
						o = new Obstacle(0, "房屋", topLeft, topRight, bottomLeft, bottomRight);
					} else if (tempStrArr[0].equals("1")) {
						o = new Obstacle(1, "树木", topLeft, topRight, bottomLeft, bottomRight);
					} else if (tempStrArr[0].equals("2")) {
						o = new Obstacle(2, "湖泊", topLeft, topRight, bottomLeft, bottomRight);
					}
					obstacles.add(o);
				}
			}
		}
	}

	public static GameMap getGameMap2(GameMap gm) {
		GameMap gm2 = new GameMap(1, gm.getWidth(), gm.getHeight(), gm.getFriction());
		int top, bottom, left, right;
		for (Obstacle o : gm.getObstacles()) {
			Obstacle o2 = null;
			top = (int) (o.getTopLeft().getY() - GlobalEnvironment.TANK_RADIUS);
			left = (int) (o.getTopLeft().getX() - GlobalEnvironment.TANK_RADIUS);
			bottom = (int) (o.getBottomRight().getY() + GlobalEnvironment.TANK_RADIUS);
			right = (int) (o.getBottomRight().getX() + GlobalEnvironment.TANK_RADIUS);
			if (top < 0) {
				top = 0;
			}
			if (left < 0) {
				left = 0;
			}
			if (bottom > GlobalEnvironment.GAMEMAPE_WIDTH) {
				bottom = GlobalEnvironment.GAMEMAPE_WIDTH;
			}
			if (right > GlobalEnvironment.GAMEMAPE_WIDTH) {
				bottom = GlobalEnvironment.GAMEMAPE_WIDTH;
			}
			o2 = new Obstacle(o.getType(), o.getName(), new Point(left, top), new Point(right, top), new Point(right, bottom), new Point(left, bottom));

			gm2.getObstacles().add(o2);
		}
		return gm2;
	}

	public static Hashtable<String, ArrayList<Obstacle>> getGameMapSlot(GameMap gm, int horizontalNum, int verticalNum) {
		Hashtable<String, ArrayList<Obstacle>> slot_table = new Hashtable<String, ArrayList<Obstacle>>();
		int x1, x2, y1, y2, ox1, ox2, oy1, oy2;
		for (int i = 0; i < horizontalNum; i++)
            for(int k = 0; k < verticalNum; k++) {
                ArrayList<Obstacle> obstacleList = new ArrayList<Obstacle>();
                // 下面判断哪些obstacle在这个slot里面
                // 先计算当前slot的x1,x2,y1,y2
                x1 = k * GlobalEnvironment.HORIZONTAL_SLOT_SIZE;
                x2 = x1 + GlobalEnvironment.HORIZONTAL_SLOT_SIZE;
                y1 = i * GlobalEnvironment.VERTICAL_SLOT_SIZE;
                y2 = y1 + GlobalEnvironment.VERTICAL_SLOT_SIZE;
                for(Obstacle o : GlobalEnvironment.gameMap2.getObstacles()) {
                    // 拿到obstacle的ox1, ox2, oy1, oy2
                    ox1 = (int) o.getTopLeft().getX();
                    oy1 = (int) o.getTopLeft().getY();
                    ox2 = (int) o.getBottomRight().getX();
                    oy2 = (int) o.getBottomRight().getY();
                    // 排除掉4种特殊情况，剩下的肯定在slot里面
                    if(!((ox1 <= x1 && ox2 <= x1) || (ox1 >= x2 && ox2 >= x2) || (oy1 <= y1 && oy2 <= y2) || (oy1 >= y1 && oy2 >= y2))) {
                        obstacleList.add(o);
                    }
                }

                String key = i + "," + k;
                slot_table.put(key, obstacleList);
            }
 		return slot_table;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public float getFriction() {
		return friction;
	}

	public void setFriction(float friction) {
		this.friction = friction;
	}

	public ArrayList<Obstacle> getObstacles() {
		return obstacles;
	}

	public void setObstacles(ArrayList<Obstacle> obstacles) {
		this.obstacles = obstacles;
	}

	public ArrayList<Point> getStartPoints() {
		return startPoints;
	}

	public void setStartPoints(ArrayList<Point> startPoints) {
		this.startPoints = startPoints;
	}

	public String getGameMapFileStr() {
		return gameMapFileStr;
	}

	public void setGameMapFileStr(String gameMapFileStr) {
		this.gameMapFileStr = gameMapFileStr;
	}
}
