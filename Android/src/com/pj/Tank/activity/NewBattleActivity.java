package com.pj.Tank.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.pj.Tank.entity.*;
import com.pj.Tank.logic.GlobalEnvironment;
import org.apache.http.util.EncodingUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: weijinshi
 * Date: 13-12-26
 * Time: 下午1:28
 * To change this template use File | Settings | File Templates.
 */
public class NewBattleActivity extends Activity {
	private Button startNewBattleButton;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_battle_activity_layout);

		startNewBattleButton = (Button) findViewById(R.id.startNewBattleButton);
		startNewBattleButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				initGlobalEnvironment();
				startActivity(new Intent(NewBattleActivity.this, BattleActivity.class));
			}
		});
	}

	private void initGlobalEnvironment() {
		try{
			InputStream in = getResources().getAssets().open("GameMap.cfg");
			int length = in.available();
			byte [] buffer = new byte[length];
			in.read(buffer);
//			Log.v("myLog", EncodingUtils.getString(buffer, "UTF-8"));
			GlobalEnvironment.gameMap = new GameMap(0);
			GlobalEnvironment.gameMap.setGameMapFileStr(EncodingUtils.getString(buffer, "UTF-8"));
			GlobalEnvironment.gameMap.generateRandomMap();
			GlobalEnvironment.gameMap2 = GameMap.getGameMap2(GlobalEnvironment.gameMap);

			// 初始化GlobalEnvironment中的其他变量
			GlobalEnvironment.tanks = new HashMap<Integer, Tank>();
			GlobalEnvironment.localTimer = System.currentTimeMillis();
			GlobalEnvironment.GAMEMAPE_WIDTH = GlobalEnvironment.gameMap.getWidth();
			GlobalEnvironment.GAMEMAPE_HEIGHT = GlobalEnvironment.gameMap.getHeight();
			GlobalEnvironment.HORIZONTAL_SLOT_SIZE = GlobalEnvironment.GAMEMAPE_WIDTH / GlobalEnvironment.HORIZONTAL_SLOT_NUM;
			GlobalEnvironment.VERTICAL_SLOT_SIZE = GlobalEnvironment.GAMEMAPE_HEIGHT / GlobalEnvironment.VERTICAL_SLOT_NUM;
			GlobalEnvironment.slot_table = GameMap.getGameMapSlot(GlobalEnvironment.gameMap2, GlobalEnvironment.HORIZONTAL_SLOT_NUM, GlobalEnvironment.VERTICAL_SLOT_NUM);
			Log.v("myLog", GlobalEnvironment.slot_table.get("0,0").size()+"");
			GlobalEnvironment.explodingPoints = new ArrayList<Point>();
			GlobalEnvironment.bomb_table = new Hashtable<Integer, Bomb>();
			GlobalEnvironment.bomb_table.put(0, new Bomb(0, "Scud", 10, 5 * 1000));
			GlobalEnvironment.infoMessages = new ArrayList<String>();
			GlobalEnvironment.netPackageQueue = new LinkedList<NetPackage>();

			// 再初始化自己的坦克
			Tank tank = new Tank();
			int randomStartPoint = (int) Math.random() * GlobalEnvironment.gameMap.getStartPoints().size();
			tank.setX(GlobalEnvironment.gameMap.getStartPoints().get(randomStartPoint).getX());
			tank.setY(GlobalEnvironment.gameMap.getStartPoints().get(randomStartPoint).getY());
			tank.setRunSpeed(0);
			tank.setRunAcceleration(0);
			// 坦克初始朝向这样写比较bug，我是根据地图拿四个点来确定的
			switch (randomStartPoint) {
				case 0: tank.setHeadDirection(90); break;
				case 1: tank.setHeadDirection(180); break;
				case 2: tank.setHeadDirection(0); break;
				default: tank.setHeadDirection(270); break;
			}
			tank.setWheelSpeed(0);
			tank.setBombList(new int[]{0});
			tank.setCurrentBomb(0);
			tank.setLastShootTime(System.currentTimeMillis());
			tank.setShooting(false);
			tank.setBeShooted(0);
			tank.setHP(100);
			tank.setM(1000);
			tank.setName("玩家A");
			tank.setId(tank.getName().hashCode());
			GlobalEnvironment.selfTankId = tank.getId();
			GlobalEnvironment.tanks.put(tank.getId(), tank);

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}