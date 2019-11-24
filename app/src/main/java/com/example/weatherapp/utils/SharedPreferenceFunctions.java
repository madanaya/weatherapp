package com.example.weatherapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.weatherapp.models.WeatherData;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Map;

import static com.example.weatherapp.Constants.FRAGMENT_LOCATION_KEY;
import static com.example.weatherapp.Constants.SHARED_PREF_NAME;

public class SharedPreferenceFunctions {
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPreferenceFunctions(Context context){
        this.context = context;
        this.sharedPreferences = this.context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();
    }

    public String getSavedLocations(){
        return this.sharedPreferences.getString(FRAGMENT_LOCATION_KEY,"");
    }

    public void addFavoriteLocation(String address)
    {

        String storedLocations = this.sharedPreferences.getString(FRAGMENT_LOCATION_KEY,"");
        if(!checkCityExists(address)){
            String location_key = address + "|";
            storedLocations = storedLocations + location_key;
            this.editor.putString(FRAGMENT_LOCATION_KEY, storedLocations);
            this.editor.apply();
        }
    }

    public void removeFavoriteLocation(String address)
    {
        Log.d("removeFavoriteLocation", address);
        String storedLocations = this.sharedPreferences.getString(FRAGMENT_LOCATION_KEY,"");
        String location_key = address + "|";
        if(storedLocations.contains(location_key)){
            Log.d("removeFavoriteLocation", "TRUE");
        }
        else{
            Log.d("removeFavoriteLocation", "FALSE");
        }
        String new_locations = storedLocations.replace(location_key,"");
        Log.d("removeFavoriteLocation", new_locations);
        this.editor.putString(FRAGMENT_LOCATION_KEY, new_locations);
        this.editor.apply();
    }

    public boolean checkCityExists(String address){
        String storedLocations = this.sharedPreferences.getString(FRAGMENT_LOCATION_KEY,"");
        String location_key = address + "|";
        return storedLocations.contains(location_key);
    }

    public void addCityInstance(String address, JSONObject jsonObject){
        WeatherData weatherData = new WeatherData(jsonObject);
        Gson gson = new Gson();
        String json = gson.toJson(weatherData);
        this.editor.putString(address,json);
        this.editor.apply();
    }

    public WeatherData getWeatherDataObject(String address){
        Gson gson = new Gson();
        String json = this.sharedPreferences.getString(address, "");
        WeatherData weatherData = gson.fromJson(json, WeatherData.class);
        return weatherData;
    }

    public void removeAtPosition(int position){
        String storedLocations = this.sharedPreferences.getString(FRAGMENT_LOCATION_KEY,"");
        String parts[] = storedLocations.split("\\|");
        StringBuilder newLocations = new StringBuilder();
        for(int i = 0; i < parts.length; i++){
            if(i != position){
                newLocations.append(parts[i]);
                newLocations.append("|");
            }
        }
        this.editor.putString(FRAGMENT_LOCATION_KEY,newLocations.toString());
        this.editor.apply();
    }

    public void printAllData(){
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
        }
    }

}
