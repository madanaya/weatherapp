package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_results);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#000000")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPreferenceFunctions = new SharedPreferenceFunctions(getApplicationContext());

        Intent intent = getIntent();
        String address = intent.getStringExtra("SEARCH");
        if(address == null || address.length() == 0){
            address = "Los Angeles, LA, USA";
        }

        // Creating final instance to be accessed by inner class
        final String current_address = address;
        String title = address;

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
                        mapData(address);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //textView.setText("That didn't work!");
                Log.d("RESPONSE","MESSAGE");
            }
        });

        queue.add(jsonRequest);
    }

    private void mapData(String address){
        progressBar.setVisibility(View.INVISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
        WeatherData weatherDataObject = sharedPreferenceFunctions.getWeatherDataObject(address);
        Log.d("SearchActivityTemp",weatherDataObject.getTemperature());

        TextView tv = findViewById(R.id.place_details);

        CardView card_view = findViewById(R.id.card_view);
        card_view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchResultsActivity.this, DetailsActivity.class);
                // Pass location key
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
