package com.pj.Tank.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
				startActivity(new Intent(WelcomeActivity.this, NewBattleActivity.class));
			}
		});
		chooseBattleBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(WelcomeActivity.this, ChooseBattleActivity.class));
			}
		});
		exitGameBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				System.exit(0);
			}
		});

        //TODO delete these lines
//        initGlobalEnvironment();
//        startActivity(new Intent(WelcomeActivity.this, BattleActivity.class));
	}
}
