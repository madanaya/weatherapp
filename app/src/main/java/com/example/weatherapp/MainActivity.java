package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.models.WeatherData;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.weatherapp.Constants.FRAGMENT_LOCATION_KEY;
import static com.example.weatherapp.Constants.SHARED_PREF_NAME;


public class MainActivity extends AppCompatActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;
    public static Context applicationContext;
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;
    private String[] dataArr;

    private ArrayAdapter<String> locationsAdapter;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */

    private ProgressBar spinner;
    private SearchView.SearchAutoComplete searchAutoComplete;
    protected static ScreenSlidePagerAdapter pagerAdapter;
    private SearchView searchView;
    private int dotsCount=5;    //No of tabs or images
    private ImageView[] dots;
    LinearLayout linearLayout;
    LinearLayout progress_linear_layout;

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private AutoSuggestAdapter autoSuggestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        applicationContext = getApplicationContext();

        getSupportActionBar().show();
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#000000")));

        // 1) Creating a new view pager
        mPager = (ViewPager) findViewById(R.id.pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(mPager, true);

        // 4) Autocomplete Setting up the adapter for AutoSuggest
        autoSuggestAdapter = new AutoSuggestAdapter(this,
                android.R.layout.simple_dropdown_item_1line);

        linearLayout = findViewById(R.id.progress_indicator);
        linearLayout.setVisibility(View.VISIBLE);
        // Before Calling Adapter, load current location object and append it to favorites_GSON Of SharedPrefs
        getLocation();

    }

    /*
        This method will be called after the current location has been fetched
     */
    public void createFragments(){
        // 2) Creating a new pager adapter
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(),applicationContext);
        mPager.setAdapter(pagerAdapter);

        // 3) Set current page and set page change listener
        //drawPageSelectionIndicators(0);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                //drawPageSelectionIndicators(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    public void removeFragment(int position){
        pagerAdapter.removeAtPosition(position);
    }

    public void getLocation()
    {
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        @SuppressLint("MissingPermission") Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        Log.d("LATLON", new Double(longitude).toString());

        String latlon = new Double(latitude).toString() + "," + new Double(longitude).toString();
        String current_location = getCurrentLocationAddress(latlon);

    }

   public String getCurrentLocationAddress(String latlon){

       RequestQueue queue = Volley.newRequestQueue(this);

       String url ="https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latlon + "&key=AIzaSyCIdFpOSv3TKDsBv89aLvDWq9gWLgkCL10";

       JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url,null,
               new Response.Listener<JSONObject>() {
                   @Override
                   public void onResponse(JSONObject response) {
                       try {
                           String address = response.getJSONObject("plus_code").getString("compound_code");
                           String parts [] = address.split(" ");
                           StringBuilder req_address = new StringBuilder();

                           int no_parts = parts.length;

                           for(int i = 1; i < parts.length; i++){
                               req_address.append(parts[i]);
                           }

                           getCurrentLocationObject(req_address.toString());
                           Log.d("RESPONSE ", req_address.toString());
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
               }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               //textView.setText("That didn't work!");
               Log.d("RESPONSE","MESSAGE");
           }
       });

       queue.add(jsonRequest);
       return "abc";
    }


    public String getCurrentLocationObject(final String address){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://nodeweather.azurewebsites.net/getWeatherAddressString/" + address;

        Log.d("getCurrentLocationObjec", url);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {

                        final SharedPreferences sharedprefs = getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
                        String storedLocations = sharedprefs.getString(FRAGMENT_LOCATION_KEY,"");

                        String location_key = address + "|";

                        String parts[] = storedLocations.split("\\|");
                        String newlocations = "";

                        for(int i = 1; i < parts.length; i++)
                        {
                            newlocations +=  parts[i] + "|";
                        }

                        storedLocations = location_key + newlocations;
                        SharedPreferences.Editor editor = sharedprefs.edit();
                        editor.putString(FRAGMENT_LOCATION_KEY, storedLocations);

                        // Create new object
                        WeatherData weatherData = new WeatherData(response);
                        Gson gson = new Gson();
                        String json = gson.toJson(weatherData);

                        editor.putString(address,json);
                        editor.apply();

                        linearLayout.setVisibility(View.INVISIBLE);
                        createFragments();
                        Log.d("Cur_location_object", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //textView.setText("That didn't work!");
                Log.d("RESPONSE","MESSAGE");
            }
        });

        queue.add(jsonRequest);
        return "abc";
    }



    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }


    private void makeApiCall(String text) {
        ApiCall.make(this, text, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //parsing logic, please change it as per your requirement
                Log.d("Json Response", response.toString());
                List<String> stringList = new ArrayList<>();
                try {

                    for(int i = 0; i < response.length(); i++) {
                        try {
                            stringList.add(response.getString(i));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    autoSuggestAdapter.setData(stringList);
                    autoSuggestAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the search menu action bar.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        // Get the search menu.
        MenuItem searchMenu = menu.findItem(R.id.action_search);
        // Get SearchView object.
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenu);

        // Get SearchView autocomplete object.
        searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(R.id.search_src_text);

        searchAutoComplete.setBackgroundColor(getResources().getColor(R.color.colorVeryDarkGrey));
        searchAutoComplete.setThreshold(1);



        searchAutoComplete.setAdapter(autoSuggestAdapter);

        searchAutoComplete.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        Log.d("Item Click", autoSuggestAdapter.getObject(position));
                        searchAutoComplete.setText(autoSuggestAdapter.getObject(position));

                    }
                });


        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                String queryString= (String) adapterView.getItemAtPosition(itemIndex);
                Log.d("ITEMCLICK",queryString);
                searchAutoComplete.setText("" + queryString);

                /*
                    User has submitted search.
                    We need to handle it
                 */

                Intent i = new Intent(MainActivity.this, SearchResultsActivity.class);
                i.putExtra("SEARCH", queryString);
                startActivity(i);


//                spinner.setVisibility(View.VISIBLE);

            }
        });

        searchAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(searchAutoComplete.getText())) {
                        makeApiCall(searchAutoComplete.getText().toString());
                    }
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void deleteFragment(int position)
    {

    }

}
