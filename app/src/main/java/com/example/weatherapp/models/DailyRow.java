package com.example.weatherapp.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DailyRow {
    String timestamp;
    Double temperatureMin, temperatureMax;
    int icon;

    public DailyRow(String timestamp, String temperatureMax, String temperatureMin, int icon)
    {
        this.timestamp = formatDate(timestamp);
        this.temperatureMax = Double.parseDouble(temperatureMax);
        this.temperatureMin =  Double.parseDouble(temperatureMin);
        this.icon = icon;
    }

    public String formatDate(String timestamp){

        long epoch = Long.parseLong( timestamp );
        Date date = new Date( epoch * 1000 );

        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        return simpleDateFormat.format(date);

    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTemperatureMin() {
        return String.format ("%.0f", temperatureMin);
    }

    public Double getTemperatureMinActual(){
        return this.temperatureMin;
    }

    public Double getTemperatureMaxActual(){
        return this.temperatureMax;
    }

    public String getTemperatureMax() {
        return String.format ("%.0f", temperatureMax);
    }

    public int getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return "DailyRow{" +
                "timestamp='" + timestamp + '\'' +
                ", temperatureMin='" + temperatureMin + '\'' +
                ", temperatureMax='" + temperatureMax + '\'' +
                ", icon=" + icon +
                '}';
    }
}
