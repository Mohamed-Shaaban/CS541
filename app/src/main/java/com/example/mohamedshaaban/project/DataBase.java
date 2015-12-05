package com.example.mohamedshaaban.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.RequiresPermission;
import android.util.Log;
import com.example.mohamedshaaban.project.Readinglist;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mohamedshaaban on 12/2/15.
 */
public class DataBase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "DriveDB";

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        String CREATE_Recording_TABLE = "CREATE TABLE recording (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "turn TEXT, " +
                "phonetimestamp INTEGER, " +
                "gpstimestamp INTEGER, " +
                "parkingspot INTEGER, " +
                "gscopex REAL, " +
                "gscopey REAL, " +
                "gscopez REAL, " +
                "distance REAL, " +
                "gps REAL ) ";

        db.execSQL(CREATE_Recording_TABLE);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS books");

        // create fresh books table
        this.onCreate(db);
    }


    private static final String TABLE_Recording = "recordings";
    private static final String KEY_ID = "id";
    private static final String KEY_turn = "turn";
    private static final String KEY_phonetimestamp = "phonetimestamp";
    private static final String KEY_gpstimestamp = "gpstimestamp";
    private static final String KEY_parkingspot = "parkingspot";
    private static final String KEY_gscopex = "gyrox";
    private static final String KEY_gscopey = "gyroy";
    private static final String KEY_gscopez = "gyroz";
    private static final String KEY_distance = "distance";
    private static final String KEY_gps = "gps";

    private static final String[] COLUMNS = {
            KEY_ID,
            KEY_turn,
            KEY_phonetimestamp,
            KEY_gpstimestamp,
            KEY_parkingspot,
            KEY_gscopex,
            KEY_gscopey,
            KEY_gscopez,
            KEY_distance,
            KEY_gps

    };

    public void addRecording(Readinglist record) {

        Log.d("addRecord", record.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();

        values.put(KEY_ID, record.getId());
        values.put(KEY_turn, record.getTurns());
        values.put(KEY_phonetimestamp, record.getPhonetimestamp());
        values.put(KEY_gpstimestamp, record.getPhonetimestamp());
        values.put(KEY_parkingspot, record.getParkingspot());
        values.put(KEY_gscopex, record.getGscopex());
        values.put(KEY_gscopey, record.getGscopey());
        values.put(KEY_gscopez, record.getGscopez());
        values.put(KEY_distance, record.getDistance());
        values.put(KEY_gps, record.getGps());

        db.insert(TABLE_Recording, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public List<Readinglist> getAllReadings() {
        List<Readinglist> Reco = new LinkedList<Readinglist>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_Recording;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Readinglist record = null;
        if (cursor.moveToFirst()) {
            do {
                try {
                    record = new Readinglist();
                    record.setId(Long.parseLong(cursor.getString(0)));
                    record.setTurns(cursor.getString(1));
                    record.setPhonetimestamp(Long.parseLong(cursor.getString(2)));
                    record.setPhonetimestamp(Long.parseLong(cursor.getString(3)));
                    record.setParkingspot(Integer.parseInt(cursor.getString(4)));
                    record.setGscopex(Float.parseFloat(cursor.getString(5)));
                    record.setGscopey(Float.parseFloat(cursor.getString(6)));
                    record.setGscopez(Float.parseFloat(cursor.getString(7)));
                    record.setDistance(Float.parseFloat(cursor.getString(8)));
                    record.setGps(Float.parseFloat(cursor.getString(9)));

                } catch (Exception ex)
                {

                }
                Reco.add(record);
            } while (cursor.moveToNext());
        }
        Log.d("getAllBooks()", Reco.toString());

        // return books
        return Reco;
    }

    public void deleteRecord(Readinglist record) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_Recording,
                KEY_ID + " = ?",
                new String[]{String.valueOf(record.getId())});

        // 3. close
        db.close();

        Log.d("deleteBook", record.toString());

    }

}


