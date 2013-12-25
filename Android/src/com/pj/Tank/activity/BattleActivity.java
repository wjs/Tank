package com.pj.Tank.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Window;
import android.view.WindowManager;

import com.pj.Tank.render.GameRenderer;
import com.rs.anergine.io.IO;
import com.rs.anergine.render.View;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 13-11-26
 * Time: 下午3:27
 * To change this template use File | Settings | File Templates.
 */
public class BattleActivity extends Activity {

    private IO io;
    private View view;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Full screen, landscape.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        io = new IO((Vibrator) getSystemService(VIBRATOR_SERVICE),(SensorManager) getSystemService(SENSOR_SERVICE));
        io.activateAccelerator();

        view = new View(this, new GameRenderer(io), io);
        setContentView(view);
	}

    @Override
    protected void onPause() {
        super.onPause();
        if(view!=null) view.onPause();
        if(io!=null) io.deactivateAccelerator();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(view!=null) view.onResume();
        if(io!=null) io.activateAccelerator();
    }
}