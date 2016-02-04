package com.pmdm.orientation;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

public class MainActivity extends Activity implements SensorEventListener
{
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;

    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;

    private float[] mR = new float[9];
    private boolean bIni = false;
    private float[] mIniOrientation = {0F,0F,0F};
    private float[] mOrientation = new float[3];

    private MyView myView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        myView = new MyView(this);
        setContentView(myView);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    protected void onResume() {
        super.onResume();
        mLastAccelerometerSet = false;
        mLastMagnetometerSet = false;
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.remapCoordinateSystem(mR,
                    SensorManager.AXIS_X,
                    SensorManager.AXIS_Z, mR);
            SensorManager.getOrientation(mR, mOrientation);//Convert to degrees
            for (int i=0; i < mOrientation.length; i++) {
                Double degrees = (mOrientation[i] * 180) / Math.PI;
                mOrientation[i] = degrees.floatValue();
            }
            //azimuth,pitch,roll//z,x,y//[-PI,PI],[-PI/2,PI/2],[-PI,PI],
            if (!bIni){
                System.arraycopy( mOrientation, 0, mIniOrientation, 0, mOrientation.length);
                bIni = true;
            }
            float dif = mIniOrientation[2] - mOrientation[2];
            dif = (dif>180)?dif-360:dif;
            Log.i("OrientationTest", String.format("Dif: %f Orientation: %f, %f, %f",
                        dif, mOrientation[0], mOrientation[1], mOrientation[2]));
            myView.updateData(dif);
        }
    }
}