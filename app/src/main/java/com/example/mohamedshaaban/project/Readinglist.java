package com.example.mohamedshaaban.project;

/**
 * Created by mohamedshaaban on 12/2/15.
 */
public class Readinglist {

    private double gscopex;

    public double getGscopex() {
        return gscopex;
    }

    public void setGscopex(double gscopex) {
        this.gscopex = gscopex;
    }

    public double getGscopey() {
        return gscopey;
    }

    public void setGscopey(double gscopey) {
        this.gscopey = gscopey;
    }

    public double getGscopez() {
        return gscopez;
    }

    public void setGscopez(double gscopez) {
        this.gscopez = gscopez;
    }

    public double getGps() {
        return gps;
    }

    public void setGps(double gps) {
        this.gps = gps;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }


    public double getParkingspot() {
        return parkingspot;
    }

    public void setParkingspot(double parkingspot) {
        this.parkingspot = parkingspot;
    }

    public String getTurns() {
        return turns;
    }

    public void setTurns(String turns) {
        this.turns = turns;
    }

    public long getPhonetimestamp() {
        return phonetimestamp;
    }

    public void setPhonetimestamp(long phonetimestamp) {
        this.phonetimestamp = phonetimestamp;
    }

    public long getGpstimestamp() {
        return gpstimestamp;
    }

    public void setGpstimestamp(long gpstimestamp) {
        this.gpstimestamp = gpstimestamp;
    }

    private double gscopey;
    private double gscopez;

    private double gps;
    private double distance;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long id;

    private double parkingspot;

    public String getReadTIME() {
        return readTIME;
    }

    public void setReadTIME(String readTIME) {
        this.readTIME = readTIME;
    }

    private   String readTIME;
    private String turns;

    private   long phonetimestamp;
    private   long gpstimestamp;




}
