package com.example.mohamedshaaban.project;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;


public class MainActivity extends Activity {

    int gpsCount = 0;
    double allData[][] = new double[40000][30];
    double gpsData[][] = new double[1][6];
    double[] data= new double[4];
    String turns[][]= new String[0][1];
    int turncount=0;
    long gpstimestamp;
    boolean logGPS = false;
    SensorReading Sensor = null;
    long timestamp = 0;
    Intent sensor = null;
    boolean mBoundedS;
    String last = "username";
    int allCount = 0;
    DataBase dbase;
    String RandLturn;
    //////////////////////


    private final String fileName = "Settings.txt";

    private String units = "MPH";
    private boolean isStarted = false;
    private LocationManager lm;
    private LocationListener ll;

    private final int GPSUPDATETIME = 1000;
    private Location oldLocation;
    private double distance;
    private double parkingspot;
    private double parkingwidth = 7.5;
    private boolean firstLoc = true;
    private boolean isPaused = false;
    double [][] Dist= new double[0][1];
    int distcount =0;


    Button startButton;
    TextView speed;
    TextView unitsText;
    TextView distanceText;
    TextView currentSpeedText;
    TextView txtstatus;
    MenuItem save;
    TextView distanceUnitsText;
    TextView currentDistanceText;
    Button pauseButton;
    Button stopButton;
    boolean recording;

////////////////////////////////////
/*
    ServiceConnection mConn = new ServiceConnection()
    {
        public void onServiceDisconnected(ComponentName name)
        {
            mBoundedS = false;
            Sensor = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service)
        {

            mBoundedS = true;
           SensorReading.SensorBinder sBinder = (SensorReading.SensorBinder) service;    //sensor service
            Sensor = sBinder.getServerInstance();
        }

    };

    // END Service Bind

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        onStopClick();
        this.unbindService(mConn);
    }
    */
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ll = new MyLocationListener();

        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showDialog(0);
        }

        startButton = (Button) findViewById(R.id.start_button);
        speed = (TextView) findViewById(R.id.speed);
        unitsText = (TextView) findViewById(R.id.main_units);
        currentSpeedText = (TextView) findViewById(R.id.current_speed_text);
        distanceText = (TextView) findViewById(R.id.distance_text);
        distanceUnitsText = (TextView) findViewById(R.id.distance_units);
        currentDistanceText = (TextView) findViewById(R.id.distance);
        txtstatus =(TextView) findViewById(R.id.txtstatus);
        pauseButton = (Button) findViewById(R.id.pause_button);
        stopButton = (Button) findViewById(R.id.stop_button);
        currentDistanceText.setVisibility(View.INVISIBLE);
        distanceText.setVisibility(View.INVISIBLE);
        distanceUnitsText.setVisibility(View.INVISIBLE);
        currentSpeedText.setVisibility(View.INVISIBLE);
        unitsText.setVisibility(View.INVISIBLE);
        pauseButton.setVisibility(View.INVISIBLE);
        stopButton.setVisibility(View.INVISIBLE);


        if (units.equals("MPH")) {
            unitsText.setText(R.string.mph);
            distanceUnitsText.setText(R.string.miles);
        } else {
            unitsText.setText(R.string.kph);
            distanceUnitsText.setText(R.string.kilometers);
        }

        if (isStarted) {
            startButton.setText(R.string.stop_button);
        } else {
            startButton.setText(R.string.start_button);
            speed.setText("Press Start");
        }

        startButton.setTextColor(Color.WHITE);
        pauseButton.setTextColor(Color.WHITE);
        stopButton.setTextColor(Color.WHITE);

        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (isStarted) {
                    isStarted = false;
                    startButton.setVisibility(View.VISIBLE);
                    stopButton.setVisibility(View.INVISIBLE);
                    pauseButton.setVisibility(View.INVISIBLE);
                    save.setVisible(true);
                    startButton.setText(R.string.start_button);
                    lm.removeUpdates((LocationListener) ll);
                    speed.setText("Press Start");
                    currentSpeedText.setVisibility(View.INVISIBLE);
                    unitsText.setVisibility(View.INVISIBLE);
                    currentDistanceText.setVisibility(View.INVISIBLE);
                    distanceText.setVisibility(View.INVISIBLE);
                    distanceUnitsText.setVisibility(View.INVISIBLE);
                    firstLoc = true;
                } else {
                    startButton.setVisibility(View.INVISIBLE);
                    pauseButton.setVisibility(View.VISIBLE);
                    stopButton.setVisibility(View.VISIBLE);
                    pauseButton.setText(R.string.pause);
                    isStarted = true;
                    save.setVisible(false);
                    startButton.setText(R.string.stop_button);
                    speed.setText("No GPS");
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPSUPDATETIME, 2, (LocationListener) ll);
                    currentSpeedText.setVisibility(View.VISIBLE);
                    unitsText.setVisibility(View.VISIBLE);
                    currentDistanceText.setVisibility(View.VISIBLE);
                    distanceText.setVisibility(View.VISIBLE);
                    distanceUnitsText.setVisibility(View.VISIBLE);
                    turnDetection();
                    //startRepeatingTask();


                }


            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isStarted = false;
                startButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.INVISIBLE);
                pauseButton.setVisibility(View.INVISIBLE);
                save.setVisible(true);
                startButton.setText(R.string.start_button);
                lm.removeUpdates((LocationListener) ll);
                speed.setText("Press Start");
                currentSpeedText.setVisibility(View.INVISIBLE);
                unitsText.setVisibility(View.INVISIBLE);
                currentDistanceText.setVisibility(View.INVISIBLE);
                distanceText.setVisibility(View.INVISIBLE);
                distanceUnitsText.setVisibility(View.INVISIBLE);
                firstLoc = true;
                onStopClick();
                //stopRepeatingTask();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isPaused) {
                    isPaused = false;
                    pauseButton.setText(R.string.pause);
                    speed.setText("No GPS");
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPSUPDATETIME, 2, (LocationListener) ll);
                } else {
                    isPaused = true;
                    pauseButton.setText(R.string.resume);
                    speed.setText("Paused");
                    lm.removeUpdates(ll);
                }
            }
        });


    }
/*
    private final static int INTERVAL = 80; //1000ms=1 second

    Handler m_handler = new Handler();

    Runnable m_handlerTask = new Runnable()
    {
        @Override
        public synchronized  void run()
        //     public  void run()

        {
            if (Sensor == null)
            {
                txtstatus.append("\nConnecting Sensor Service");
                //return;
            }
            else {
                data = Sensor.getdata();

                allData[allCount][0] = (float) System.nanoTime();
                allData[allCount][1] = gpsData[gpsCount][1];
                allData[allCount][5] = Dist[distcount][0];
                allData[allCount][6] = Dist[distcount][1];


            }
            m_handler.postDelayed(m_handlerTask, INTERVAL);

        }
    };

    void startRepeatingTask()
    {

        m_handlerTask.run();
    }

    void stopRepeatingTask()
    {

        m_handler.removeCallbacks(m_handlerTask);
    }
    */
    public boolean turnDetection() {
        boolean turn;


        //for (int i=0 ; i <= SensorService.gyroData.length ; i++ )
        int i = Sensor.GyroStore.length - 1;
        if (Sensor.GyroStore[i][1] >= 0.2 || Sensor.GyroStore[i][1] <= -0.2) {
            turn = true;
            //DandTData [DandTCount][0] = (long) SensorService.gyroData[i][1]; // saving whether its left or right.
            if (Sensor.GyroStore[i][1] >= 0.2) {
                RandLturn = "Right";
                turns[turncount][1] = RandLturn;
            } else if (Sensor.GyroStore[i][1] <= 0.2) {
                RandLturn = "Left";
                turns[turncount][1] = RandLturn;
            }

        } else {
            turn = false;
            RandLturn = "No turn";
            turns[turncount][1] = RandLturn;
        }

        return turn;

    }// calss


    // End Bluetooth service binder


    @SuppressLint("SdCardPath")
    private void onStopClick() {
        {
            Toast.makeText(getApplicationContext(), "--Stop Recording--", Toast.LENGTH_SHORT).show();
            if (recording == false) {
                return;
            }

            if (recording) {


                //stopService(bt);
                stopService(sensor);
               // MainActivity.this.unbindService(mConn);

                //	  		PrintWriter captureFile1=null;
                PrintWriter captureFile2 = null;
                PrintWriter captureFile3 = null;


                long fileTimestamp = timestamp;//System.currentTimeMillis();
                //  		String gpsFN = String.valueOf(fileTimestamp)+"GPSData.csv";
                //  		String obdFN =  String.valueOf(fileTimestamp)+"OBDdata.csv";
                String allFN = last + "_" + String.valueOf(fileTimestamp) + "ALLdata.csv";
                String tripFN = "Trips.csv";

                String path = Environment.getExternalStorageDirectory().getPath();
                //		File captureAccFile = new File(  path+"/DriveCapture", gpsFN );
                File captureAccFile2 = new File(path + "/DriveCapture", tripFN);
                File captureAccFile3 = new File(path + "/DriveCapture", allFN);

                try {
                    //	captureFile1 = new PrintWriter( new FileWriter( captureAccFile, false ) );
                    captureFile2 = new PrintWriter(new FileWriter(captureAccFile2, true));
                    captureFile3 = new PrintWriter(new FileWriter(captureAccFile3, false));


           	 /* 		for(int i=0;i<obdCount;i++)
           	  		{
           	  			captureFile2.println(String.valueOf(obdData[i][0])+","+String.valueOf(obdData[i][1]));
           	  			//txtStatus.append(String.valueOf(obdData[i][0]));
           	  		}

           	  		for(int i=0;i<gpsCount;i++)
           	  		{   captureFile1.println(gpsData[i][0]+","+gpsData[i][1]+","+gpsData[i][2]+","+gpsData[i][3]+","+gpsData[i][4]);}
           	  	*/

                    Readinglist rec = new Readinglist();
                    String tripID = last + String.valueOf(System.currentTimeMillis());


                    for (int i = 0; i < allCount; i++) {
                        //Add record to file
                        // /*
                        String line = String.valueOf(allData[i][0]);
                        for (int k = 1; k < 30; k++)
                            line = line + "," + String.valueOf(allData[i][k]);
                        line = tripID + "," + line;
                        captureFile3.println(line);
                        //*/
                        //Adding Records to Data base
                        //rec=null;
                        rec.setGps(allData[i][1]);
                        rec.setGscopex(allData[i][2]);
                        rec.setGscopey(allData[i][3]);
                        rec.setGscopez(allData[i][4]);
                        rec.setDistance(allData[i][5]);
                        rec.setParkingspot(allData[i][6]);
                        rec.setId((long)allData[i][7]);
                        rec.setTurns(RandLturn);


                        txtstatus.setText("TIME: " + rec.getReadTIME());

                        dbase.addRecording(rec);
                        //txtStatus.append(String.valueOf(obdData[i][0]));

                        //Add record to server //

                    }
                    //************************************************ connecting to the server
                    //	new connectDBserver().execute(rec);


                    // End of adding the last record to the database for testing

                    txtstatus.append("\nAll RECORDS stored successfully");//.["+gpsCount+"]-["+obdCount+"]");
                    //Toast.makeText(getApplicationContext(), "-- OBD,GPS Files writen --", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    txtstatus.setText(e.getMessage());
                    Log.i("SensorService: ", e.getMessage());
                } finally {
                    //		captureFile1.close();
                    captureFile2.close();
                    captureFile3.close();


                }
            }
        }
    }




    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            if (loc != null) {
                if (loc.hasSpeed()) {
                    if (units.equals("MPH")) {
                        double sp = (double) (loc.getSpeed() * (float) 2.236936);
                        gpsData[gpsCount][1] =sp;
                        DecimalFormat f = new DecimalFormat();
                        f.setMaximumFractionDigits(2);
                        String fm = f.format(sp);
                        speed.setText(fm);

                        if (firstLoc == false) {
                            distance += loc.distanceTo(oldLocation);
                            Dist[distcount][0]=distance;
                            parkingspot = distance/parkingwidth;
                            Dist[distcount][1]=parkingspot;
                            currentDistanceText.setText(f.format(distance * .000621371192));
                        } else {
                            distance = 0;
                            Dist[distcount][0]=distance;
                            parkingspot = distance/parkingwidth;
                            Dist[distcount][1]=parkingspot;
                            firstLoc = false;
                            currentDistanceText.setText("0.00");
                            oldLocation = loc;
                        }


                    } else {
                        double sp = (double) (loc.getSpeed() * (float) 3.6);
                        gpsData[gpsCount][1] =sp;
                        DecimalFormat f = new DecimalFormat();
                        f.setMaximumFractionDigits(2);
                        String fm = f.format(sp);
                        speed.setText(fm);

                        if (firstLoc == false) {
                            distance += loc.distanceTo(oldLocation);
                            Dist[distcount][0]=distance;
                            parkingspot = distance/parkingwidth;
                            Dist[distcount][1]=parkingspot;
                            currentDistanceText.setText(f.format(distance * .001));
                        } else {
                            distance = 0;
                            firstLoc = false;
                            Dist[distcount][0]=distance;
                            parkingspot = distance/parkingwidth;
                            Dist[distcount][1]=parkingspot;
                            currentDistanceText.setText("0.00");
                            oldLocation = loc;
                        }


                    }
                } else {
                    speed.setText(R.string.speed);
                }
                oldLocation = loc;
            }
        }

        @Override
        public void onProviderDisabled(String arg0) {

        }

        @Override
        public void onProviderEnabled(String arg0) {

        }

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

        }


    }


}