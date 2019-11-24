package com.example.weatherapp.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DailyRow {
    String timestamp, temperatureMin, temperatureMax;
    int icon;

    public DailyRow(String timestamp, String temperatureMax, String temperatureMin, int icon)
    {
        this.timestamp = formatDate(timestamp);

        Double temperature = Double.parseDouble(temperatureMax);
        this.temperatureMax = String.format ("%.2f", temperature);;

        temperature = Double.parseDouble(temperatureMin);
        this.temperatureMin = String.format ("%.2f", temperature);;

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
        return temperatureMin;
    }

    public String getTemperatureMax() {
        return temperatureMax;
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
