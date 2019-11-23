package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class SearchResultsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#000000")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String search_result = "";

        if (intent != null ){
            search_result = intent.getStringExtra("SEARCH_STREET");
            if ( search_result == null ){
                search_result = "Los Angeles, CA, USA";
            }
        }

        setTitle(search_result);
        handleIntent(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("SearchResultsActivity","onNewIntent");
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            getSupportActionBar().setTitle(query);
            Boolean search_activity = false;
            search_activity = intent.getBooleanExtra("SEARCH", false);


            if(search_activity){
                Log.d("Boolean Value", "TRUE");
            }
            else{
                Log.d("Boolean Value", "FALSE");
            }


            try {
                Log.d("SLEEP", "START");
                Thread.sleep(3000);
                findViewById(R.id.progress_indicator).setVisibility(View.INVISIBLE);
                findViewById(R.id.content).setVisibility(View.VISIBLE);
                Log.d("SLEEP", "END");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //use the query to search your data somehow
            Log.d("SEARCHVIEW ", query);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
