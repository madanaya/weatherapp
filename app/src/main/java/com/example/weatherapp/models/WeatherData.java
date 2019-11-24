package com.example.weatherapp.models;

import com.example.weatherapp.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WeatherData {

    int temperature;
    int icon;
    String summary, humidity, windspeed, visibility, pressure;
    JSONObject jsonData;

    Map<String,Integer> icon_map;

    public WeatherData(JSONObject jsonData){
        this.jsonData = jsonData;
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

        try {
            double temperature = Double.parseDouble(jsonData.getJSONObject("currently").getString("temperature"));
            this.temperature = (int) Math.round(temperature);
            this.icon = icon_map.get(jsonData.getJSONObject("currently").getString("icon"));
            this.summary = jsonData.getJSONObject("currently").getString("summary");
            this.humidity = jsonData.getJSONObject("currently").getString("humidity");
            this.windspeed = jsonData.getJSONObject("currently").getString("windSpeed");
            this.visibility = jsonData.getJSONObject("currently").getString("visibility");
            this.pressure = jsonData.getJSONObject("currently").getString("pressure");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTemperature(){
        return new Integer(temperature).toString();
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
}
