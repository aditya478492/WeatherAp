package com.example.aditya.weatherap;

import android.graphics.Bitmap;

public class Day {
    String date,temp;

    public Day(String date, String temp) {
        this.date = date;
        this.temp = temp;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

}
