package com.pj.Tank.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pj.Tank.entity.Bomb;
import com.pj.Tank.entity.GameMap;
import com.pj.Tank.entity.Point;
import com.pj.Tank.entity.Tank;
import com.pj.Tank.logic.GlobalEnvironment;

import org.apache.http.util.EncodingUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class WelcomeActivity extends Activity {
	private Button newBattleBtn;
	private Button chooseBattleBtn;
	private Button exitGameBtn;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_activity_layout);

		newBattleBtn = (Button) findViewById(R.id.newBattleBtn);
		chooseBattleBtn = (Button) findViewById(R.id.chooseBattleBtn);
		exitGameBtn = (Button) findViewById(R.id.exitGameBtn);

		newBattleBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				initGlobalEnvironment();
			}
		});
		chooseBattleBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, BattleActivity.class));
			}
		});
		exitGameBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				System.exit(0);
			}
		});

        //TODO delete these lines
        initGlobalEnvironment();
        startActivity(new Intent(WelcomeActivity.this, BattleActivity.class));
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
			GlobalEnvironment.explodingPoints = new ArrayList<Point>();
			GlobalEnvironment.bomb_table = new Hashtable<Integer, Bomb>();
			GlobalEnvironment.bomb_table.put(0, new Bomb(0, "Scud", 10, 5 * 1000));

			// 再初始化自己的坦克
			Tank tank = new Tank();
			int randomStartPoint = (int) Math.random() * GlobalEnvironment.gameMap.getStartPoints().size();
			tank.setX(GlobalEnvironment.gameMap.getStartPoints().get(randomStartPoint).getX());
			tank.setY(GlobalEnvironment.gameMap.getStartPoints().get(randomStartPoint).getY());
			tank.setRunSpeed(0);
			tank.setRunAcceleration(0);
			// 坦克初始朝向这样写比较bug，我是根据地图拿四个点来确定的
			switch (randomStartPoint) {
				case 0: tank.setHeadDirection(270); break;
				case 1: tank.setHeadDirection(0); break;
				case 2: tank.setHeadDirection(180); break;
				default: tank.setHeadDirection(90); break;
			}
			tank.setWheelSpeed(0);
			tank.setBombList(new int[]{0});
			tank.setCurrentBomb(0);
			tank.setLastShootTime(System.currentTimeMillis());
			tank.setShooting(false);
			tank.setBeShooted(0);
			tank.setHP(100);
			tank.setName("玩家A");
			tank.setId(tank.getName().hashCode());
			GlobalEnvironment.tanks.put(tank.getId(), tank);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
