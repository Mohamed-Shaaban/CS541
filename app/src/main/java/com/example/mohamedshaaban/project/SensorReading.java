package com.example.mohamedshaaban.project;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Created by mohamedshaaban on 12/2/15.
 */
public class SensorReading extends Service implements SensorEventListener{

    String file_name;




    IBinder mBinder = new SensorBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class SensorBinder extends Binder {
        public  SensorReading getServerInstance() {
            return SensorReading.this;
        }
    }





    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        //handleCommand(intent);
        file_name = intent.getStringExtra("filename");
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }
    SensorManager GyroReading =null;
    int gyrocount =0;

    double GyroStore[][]= new double[0][4];

    long Gyrotimestamp;

    public void onCreate(){

        super.onCreate();
        GyroReading = (SensorManager)getSystemService(SENSOR_SERVICE);

        GyroReading.registerListener(this,GyroReading.getDefaultSensor(Sensor.TYPE_GYROSCOPE),SensorManager.SENSOR_DELAY_NORMAL);

        Toast.makeText(this, "GyroScope Created. ...", Toast.LENGTH_LONG).show();

    }

    public double[] getdata (){

        double [] data = new double[4];

        data[0] = GyroStore[gyrocount][0];
        data[1] = GyroStore[gyrocount][1];
        data[2] = GyroStore[gyrocount][2];
        data[3] = GyroStore[gyrocount][3];
        return data;
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    public void onSensorChanged(SensorEvent event){

        synchronized (this){
            if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                Gyrotimestamp = event.timestamp;
                GyroStore[gyrocount][0] =event.values[0];
                GyroStore[gyrocount][1]=event.values[1];
                GyroStore[gyrocount][2]=event.values[2];
                GyroStore[gyrocount][3]=event.timestamp;
            }


        }


    }

}
