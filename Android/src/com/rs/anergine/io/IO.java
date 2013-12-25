package com.rs.anergine.io;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.view.MotionEvent;

//take care of Vibrator & Accelerometer & ComponentSet.
public class IO {
    private Vibrator vibrator;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private SensorEventListener accListener;
    private ComponentSet componentSet;

    private float[] gravity = new float[3];

    //need these things when activity started.
    public IO(Vibrator vibrator, SensorManager sensorManager) {
        this.vibrator = vibrator;
        this.sensorManager = sensorManager;
        this.accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.accListener = new SensorEventListener() {
            public void onSensorChanged(SensorEvent e) {
                gravity[0] = e.values[0];
                gravity[1] = e.values[1];
                gravity[2] = e.values[2];
            }

            public void onAccuracyChanged(Sensor s, int accuracy) {
            }
        };
    }

    public float[] getGravity() {
        return gravity;
    }

    public void activateAccelerator() {
        sensorManager
                .registerListener(accListener, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void deactivateAccelerator() {
        sensorManager.unregisterListener(accListener);
    }

    public void touch(MotionEvent event) {
        if(componentSet != null)
            componentSet.touch(event);
    }

    public void vibrate(int time) {
        if(time > 0 && time <= 1000)
            vibrator.vibrate(time);
    }

    public void setComponentSet(ComponentSet set) {
        this.componentSet = set;
    }

    public void draw() {
        if(componentSet != null)
            componentSet.draw();
    }
}
