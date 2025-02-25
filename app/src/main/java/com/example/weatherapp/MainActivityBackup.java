package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivityBackup extends AppCompatActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;

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
    private PagerAdapter pagerAdapter;
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
        getSupportActionBar().show();

        // 1) Creating a new view pager
        mPager = (ViewPager) findViewById(R.id.pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(mPager, true);
        // 2) Creating a new pager adapter
        pagerAdapter = new MainActivityBackup.ScreenSlidePagerAdapter(getSupportFragmentManager());
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

        // 4) Autocomplete Setting up the adapter for AutoSuggest
        autoSuggestAdapter = new AutoSuggestAdapter(this,
                android.R.layout.simple_dropdown_item_1line);

        // 5) Make API Call
        getLocation();


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

                            for(int i = 1; i < parts.length; i++){
                                req_address.append(parts[i]);
                            }

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

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public List<Fragment> screen_fragments = new ArrayList<>();

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
//
//            Bundle bundle = new Bundle();
//            bundle.putString("KEY", new Integer(i).toString());
//            ScreenSlidePageFragment fragobj = new ScreenSlidePageFragment();
//            fragobj.setArguments(bundle);
//            screen_fragments.add(fragobj);

            for(int i = 0; i < NUM_PAGES; i++)
            {

                Bundle bundle = new Bundle();
                bundle.putString("KEY", new Integer(i).toString());
                ScreenSlidePageFragment fragobj = new ScreenSlidePageFragment();
                fragobj.setArguments(bundle);
                screen_fragments.add(fragobj);
            }
        }

        @Override
        public Fragment getItem(int position){
            return screen_fragments.get(position);
        }

        @Override
        public int getItemPosition(Object object){
            int index = screen_fragments.indexOf(object);

            if (index == -1)
                return POSITION_NONE;
            else
                return index;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }


        public void removeAtPosition(int pos){
            screen_fragments.remove(pos);
        }

    }


//    private void drawPageSelectionIndicators(int mPosition){
//        Log.d("DRAW PAGE SELECTED ", new Integer(mPosition).toString());
//        if(linearLayout!=null) {
//            linearLayout.removeAllViews();
//        }
//        linearLayout= findViewById(R.id.viewPagerCountDots);
//        dots = new ImageView[dotsCount];
//
//        for (int i = 0; i < dotsCount; i++) {
//            dots[i] = new ImageView(MainActivity.this);
//            if(i==mPosition)
//                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.item_selected));
//            else
//                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.item_unselected));
//
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//            );
//
//            params.setMargins(4, 0, 4, 0);
//            linearLayout.addView(dots[i], params);
//        }
//    }

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
        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(R.id.search_src_text);
        searchAutoComplete.setBackgroundColor(Color.BLUE);
        searchAutoComplete.setTextColor(Color.GREEN);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.white);
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

                Intent i = new Intent(MainActivityBackup.this, SearchResultsActivity.class);
                i.putExtra("SEARCH", true);
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
