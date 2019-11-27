package com.example.weatherapp.models;

import android.util.Log;

import com.example.weatherapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherData {

    String temperature;
    String formattedTemperature;
    int icon, daily_icon;
    String summary, humidity, windspeed, visibility, pressure, precipitation, cloudCover, ozone, weekly_card_summary;
    JSONObject jsonData;
    Map<String,Integer> icon_map;
    List<DailyRow> dailyRows;

    public WeatherData(JSONObject jsonData){
        this.jsonData = jsonData;

        // Initialize Icon Map
        icon_map = new HashMap<>();
        icon_map.put("partly-cloudy-day", R.drawable.weather_partly_cloudy);
        icon_map.put("partly-cloudy-night", R.drawable.weather_night_partly_cloudy);
        icon_map.put("cloudy",R.drawable.weather_cloudy);
        icon_map.put("fog",R.drawable.weather_fog);
        icon_map.put("wind", R.drawable.weather_windy_variant);
        icon_map.put("snow",R.drawable.weather_snowy);
        icon_map.put("sleet",R.drawable.weather_snowy_rainy);
        icon_map.put("rain", R.drawable.weather_rainy);
        icon_map.put("clear-night",R.drawable.weather_night);
        icon_map.put("clear-day", R.drawable.weather_sunny);

        // Initialize Daily Row Array
        dailyRows = new ArrayList<>();

        try {
            try {
                double temperature = Double.parseDouble(jsonData.getJSONObject("currently").getString("temperature"));
                this.temperature = String.format("%.0f", temperature);
                this.formattedTemperature = String.format("%.2f", temperature);
            }
            catch (Exception exception){
                this.temperature = null;
            }


            try {
                this.icon = icon_map.get(jsonData.getJSONObject("currently").getString("icon"));
            }
            catch (Exception exception){
                this.icon = R.drawable.weather_sunny;
            }


            try {
                this.daily_icon = icon_map.get(jsonData.getJSONObject("daily").getString("icon"));
            }
            catch (Exception exception){
                this.daily_icon = R.drawable.weather_sunny;
            }

            try {
                this.summary = jsonData.getJSONObject("currently").getString("summary");
            }
            catch (Exception exception){
                this.summary = null;
            }

            try {
                this.weekly_card_summary = jsonData.getJSONObject("daily").getString("summary");
            }
            catch (Exception exception){
                this.weekly_card_summary = null;
            }

            try {
                this.humidity = jsonData.getJSONObject("currently").getString("humidity");
            }
            catch (Exception exception){
                this.humidity = null;
            }


            try{
                Double windspeed = Double.parseDouble(jsonData.getJSONObject("currently").getString("windSpeed"));
                this.windspeed = String.format("%.2f", windspeed);
            }
            catch (Exception exception){
                this.windspeed = null;
            }

            try{
                Double visibility = Double.parseDouble(jsonData.getJSONObject("currently").getString("visibility"));
                this.visibility = String.format("%.2f", visibility);
            }
            catch (Exception exception){
                this.visibility = null;
            }

            try{
                Double pressure = Double.parseDouble(jsonData.getJSONObject("currently").getString("pressure"));
                this.pressure = String.format("%.2f", pressure);
            }
            catch (Exception exception){
                this.pressure = null;
            }


            try{
                Double precipitation = Double.parseDouble(jsonData.getJSONObject("currently").getString("precipIntensity"));
                this.precipitation = String.format("%.2f", precipitation);
            }
            catch (Exception exception){
                this.precipitation = null;
            }

            try{
                Integer cloudcover = Integer.parseInt(jsonData.getJSONObject("currently").getString("precipIntensity"));
                this.cloudCover = cloudcover.toString();
            }
            catch (Exception exception){
                this.cloudCover = null;
            }

            try{
                Double ozone = Double.parseDouble(jsonData.getJSONObject("currently").getString("ozone"));
                this.ozone = String.format("%.2f", ozone);
            }
            catch (Exception exception){
                this.ozone = null;
            }


            JSONArray dailyData = jsonData.getJSONObject("daily").getJSONArray("data");
            String timestamp, temperatureMin, temperatureMax;
            int dailyicon;
            DailyRow dailyRow;

            for(int i = 0; i < dailyData.length(); i++)
            {
                timestamp = dailyData.getJSONObject(i).getString("time");
                temperatureMin = dailyData.getJSONObject(i).getString("temperatureMin");
                temperatureMax = dailyData.getJSONObject(i).getString("temperatureMax");
                dailyicon = icon_map.get(dailyData.getJSONObject(i).getString("icon"));
                dailyRow = new DailyRow(timestamp,temperatureMax,temperatureMin,dailyicon);
                dailyRows.add(dailyRow);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTemperature() {
        if(this.temperature == null){
            return "NA";
        }
        return this.temperature + "\u00B0F";
    }

    public String getFormattedTemperature(){
        if(this.temperature == null)
        {
            return "NA";
        }
        return formattedTemperature;

    }

    public Integer getIconId(){
        return icon;
    }

    public String getSummary(){
        if(this.summary == null){
            return "NA";
        }
        return summary;
    }

    public String getHumidity(){
        //return this.humidity.replace("0.","") + "%";
        if (this.humidity == null){
            return "NA";
        }
        else{
            return this.humidity.replace("0.","") + "%";
        }
    }

    public String getWindspeed(){
        if(this.windspeed == null){
            return "NA";
        }
        return this.windspeed + " mph";
    }

    public String getVisibility(){
        if(this.visibility == null){
            return "NA";
        }
        return this.visibility + " km";
    }

    public String getPressure(){
        if(this.pressure == null){
            return "NA";
        }
        return this.pressure + " mb";
    }

    public String getPrecipitation() {
        if(this.precipitation == null){
            return "NA";
        }
        return precipitation;
    }

    public String getCloudCover() {
        if(this.cloudCover == null){
            return "NA";
        }
        return cloudCover;
    }

    public String getOzone() {
        if(this.ozone == null){
            return "NA";
        }
        return ozone;
    }

    public String getWeekly_card_summary() {
        if(weekly_card_summary == null)
            return null;

        return weekly_card_summary;
    }

    public DailyRow getDailyData(int day){
        return this.dailyRows.get(day);
    }

    public ArrayList<Double> getDailyTemperatureMin(){
        ArrayList<Double> dailyMins = new ArrayList<>();
        for(DailyRow dailyRow: dailyRows){
            dailyMins.add(dailyRow.getTemperatureMinActual());
        }
        return dailyMins;
    }

    public ArrayList<Double> getDailyTemperatureMax(){
        ArrayList<Double> dailyMaxs = new ArrayList<>();
        for(DailyRow dailyRow: dailyRows){
            dailyMaxs.add(dailyRow.getTemperatureMaxActual());
        }
        return dailyMaxs;
    }

    public int getDaily_icon() {
        return daily_icon;
    }
}
