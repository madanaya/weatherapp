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
    int icon;
    String summary, humidity, windspeed, visibility, pressure;
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
            double temperature = Double.parseDouble(jsonData.getJSONObject("currently").getString("temperature"));
            this.temperature = String.format ("%.2f", temperature);;
            this.icon = icon_map.get(jsonData.getJSONObject("currently").getString("icon"));
            this.summary = jsonData.getJSONObject("currently").getString("summary");
            this.humidity = jsonData.getJSONObject("currently").getString("humidity");
            this.windspeed = jsonData.getJSONObject("currently").getString("windSpeed");
            this.visibility = jsonData.getJSONObject("currently").getString("visibility");
            this.pressure = jsonData.getJSONObject("currently").getString("pressure");

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
        return this.temperature + "\u00B0F";
    }

    public Integer getIconId(){
        return icon;
    }

    public String getSummary(){
        return summary;
    }

    public String getHumidity(){
        //return this.humidity.replace("0.","") + "%";
        if (this.humidity == null){
            return "None";
        }
        else{
            return this.humidity.replace("0.","") + "%";
        }
    }

    public String getWindspeed(){
        return this.windspeed + " mph";
    }

    public String getVisibility(){
        return this.visibility + " km";
    }

    public String getPressure(){
        return this.pressure + " mb";
    }

    public DailyRow getDailyData(int day){
        return this.dailyRows.get(day);
    }
}
