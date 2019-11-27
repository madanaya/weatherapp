package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.models.WeatherData;
import com.example.weatherapp.utils.SharedPreferenceFunctions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

public class SearchResultsActivity extends AppCompatActivity {

    private Boolean isAdded = false;
    public final String FAVORITE_KEY = "fav";
    private LinearLayout progressBar;
    private LinearLayout linearLayout;
    private SharedPreferenceFunctions sharedPreferenceFunctions;
    private String address;
    private WeatherData weatherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_results);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#000000")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPreferenceFunctions = new SharedPreferenceFunctions(getApplicationContext());

        Intent intent = getIntent();
        address = intent.getStringExtra("SEARCH");
        if(address == null || address.length() == 0){
            address = "Los Angeles, LA, USA";
        }
        // Creating final instance to be accessed by inner class
        final String current_address = address;
        String title = address;
        getSupportActionBar().setTitle(title);

        isAdded = sharedPreferenceFunctions.checkCityExists(current_address);
        final FloatingActionButton fb = findViewById(R.id.floatingActionButton);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isAdded){
                    // Set boolean
                    isAdded = false;

                    // Remove from favorites
                    sharedPreferenceFunctions.removeFavoriteLocation(current_address);

                    // Change icon
                    fb.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.map_marker_plus));

                    // Display Toast
                    Toast.makeText(SearchResultsActivity.this, current_address + " is removed from your favorites", Toast.LENGTH_LONG).show();
                    MainActivity.pagerAdapter.createAdapterData();
                    Log.d("STORED_LOCATIONS REM", sharedPreferenceFunctions.getSavedLocations());
                }
                else{
                    // Set boolean
                    isAdded = true;

                    // Add address to sharedpref
                    Log.d("ADDING FAVORITE", current_address);
                    sharedPreferenceFunctions.addFavoriteLocation(current_address);

                    // Change Icon
                    fb.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.map_marker_minus));

                    // Display Toast
                    Toast.makeText(SearchResultsActivity.this, current_address + " is added to your favorites", Toast.LENGTH_LONG).show();
                    MainActivity.pagerAdapter.createAdapterData();
                    Log.d("STORED_LOCATIONS ADD", sharedPreferenceFunctions.getSavedLocations());
                }

            }
        });

        progressBar = (LinearLayout) findViewById(R.id.progress_indicator);
        linearLayout = (LinearLayout)findViewById(R.id.content);
        progressBar.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.INVISIBLE);

        if(!sharedPreferenceFunctions.checkCityExists(current_address)){
            loadData(current_address);
        }
        else{
            fb.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.map_marker_minus));
            weatherData = sharedPreferenceFunctions.getWeatherDataObject(address);
            mapData(current_address);
        }
    }

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String url = "http://www.mocky.io/v2/597c41390f0000d002f4dbd1";

    private void loadData(final String address)
    {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://nodeweather.azurewebsites.net/getWeatherAddressString/" + address;
        Log.d("SearchResultsActivity", url);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {

                        Log.d("SearchActivityResponse", response.toString());
                        sharedPreferenceFunctions.addCityInstance(address,response);
                        weatherData = sharedPreferenceFunctions.getWeatherDataObject(address);
                        mapData(address);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RESPONSE","MESSAGE");
            }
        });

        queue.add(jsonRequest);
    }

    private void mapData(final String address){
        progressBar.setVisibility(View.INVISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
//        Log.d("SearchActivityTemp",weatherDataObject.getTemperature());
        TextView tv = findViewById(R.id.place_details);
        tv.setText(address);

        CardView card_view = findViewById(R.id.card_view);
        card_view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchResultsActivity.this, DetailsActivity.class);
                intent.putExtra("SELECTED_LOCATION", address);
                startActivity(intent);
            }
        });



        ImageView card1_icon = findViewById(R.id.card1_icon);
        TextView card1_temperature = findViewById(R.id.card1_temperature);;
        TextView card1_summary = findViewById(R.id.card1_summary);

        TextView card2_humidity = findViewById(R.id.humidity_val);
        TextView card2_visibility = findViewById(R.id.visibility_val);
        TextView card2_windspeed = findViewById(R.id.windspeed_val);
        TextView card2_pressure = findViewById(R.id.gauge_val);

        card1_icon.setImageResource(weatherData.getIconId());
        card1_temperature.setText(weatherData.getTemperature());
        card1_summary.setText(weatherData.getSummary());

        card2_humidity.setText(weatherData.getHumidity());
        card2_visibility.setText(weatherData.getVisibility());
        card2_windspeed.setText(weatherData.getWindspeed());
        card2_pressure.setText(weatherData.getPressure());

        setTableRowData(0, weatherData, R.id.row1_date, R.id.row1_icon, R.id.row1_tempmin, R.id.row1_tempmax);
        setTableRowData(1, weatherData, R.id.row2_date, R.id.row2_icon, R.id.row2_tempmin, R.id.row2_tempmax);
        setTableRowData( 2, weatherData, R.id.row3_date, R.id.row3_icon, R.id.row3_tempmin, R.id.row3_tempmax);
        setTableRowData( 3, weatherData, R.id.row4_date, R.id.row4_icon, R.id.row4_tempmin, R.id.row4_tempmax);
        setTableRowData( 4, weatherData, R.id.row5_date, R.id.row5_icon, R.id.row5_tempmin, R.id.row5_tempmax);
        setTableRowData( 5, weatherData, R.id.row6_date, R.id.row6_icon, R.id.row6_tempmin, R.id.row6_tempmax);
        setTableRowData( 6, weatherData, R.id.row7_date, R.id.row7_icon, R.id.row7_tempmin, R.id.row7_tempmax);
        setTableRowData( 7, weatherData, R.id.row8_date, R.id.row8_icon, R.id.row8_tempmin, R.id.row8_tempmax);
    }

    public void setTableRowData(int row, WeatherData weatherData, int date_id, int icon_id, int tempmin_id, int tempmax_id)
    {
        TextView row_date = findViewById(date_id);
        ImageView row_image = findViewById(icon_id);
        TextView row_temperature_min = findViewById(tempmin_id);
        TextView row_temperature_max = findViewById(tempmax_id);

        row_date.setText(weatherData.getDailyData(row).getTimestamp());
        row_image.setImageResource(weatherData.getDailyData(row).getIcon());
        row_temperature_min.setText(weatherData.getDailyData(row).getTemperatureMin());
        row_temperature_max.setText(weatherData.getDailyData(row).getTemperatureMax());
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}