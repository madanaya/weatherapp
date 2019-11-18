package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

    private List<String> suggestions = new ArrayList<>();
    private ArrayAdapter<String> locationsAdapter;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */

    private SearchView.SearchAutoComplete searchAutoComplete;
    private PagerAdapter pagerAdapter;
    private SearchView searchView;
    private int dotsCount=5;    //No of tabs or images
    private ImageView[] dots;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().show();

        // 1) Creating a new view pager
        mPager = (ViewPager) findViewById(R.id.pager);

        // 2) Creating a new pager adapter
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);

        // 3) Set current page and set page change listener
        drawPageSelectionIndicators(0);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                drawPageSelectionIndicators(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
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

        List<Fragment> screen_fragments = new ArrayList<>();

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);

            for(int i = 0; i < NUM_PAGES; i++){
                Bundle bundle = new Bundle();
                bundle.putString("KEY", new Integer(i).toString());
                ScreenSlidePageFragment fragobj = new ScreenSlidePageFragment();
                fragobj.setArguments(bundle);
                screen_fragments.add(fragobj);
            }
//            List<Fragment> screen_fragments = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {

            return screen_fragments.get(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


    private void drawPageSelectionIndicators(int mPosition){
        Log.d("DRAW PAGE SELECTED ", new Integer(mPosition).toString());
        if(linearLayout!=null) {
            linearLayout.removeAllViews();
        }
        linearLayout= findViewById(R.id.viewPagerCountDots);
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(MainActivityBackup.this);
            if(i==mPosition)
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.item_selected));
            else
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.item_unselected));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);
            linearLayout.addView(dots[i], params);
        }
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
        searchAutoComplete.setTextColor(getResources().getColor(R.color.colorWhite));
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.white);

        suggestions.add("Apple");
        suggestions.add("Amazon");
        suggestions.add("Akamai");
        suggestions.add("Akuna Capital");

        // Create a new ArrayAdapter and add data to search auto complete object.
        dataArr = new String[]{"Apple" , "Amazon" , "Amd", "Microsoft", "Microwave", "MicroNews", "Intel", "Intelligence"};
        locationsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, suggestions);
        searchAutoComplete.setAdapter(locationsAdapter);

//        searchAutoComplete.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                retrieveData(s.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                //retrieveData(s.toString()); //this will call your method every time the user stops typing, if you want to call it for each letter, call it in onTextChanged
//
//            }
//        });

        // Listen to search view item on click event.
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {

                String queryString=(String)adapterView.getItemAtPosition(itemIndex);
                Log.d("ITEMCLICK",queryString);
                searchAutoComplete.setText("" + queryString);
                Toast.makeText(MainActivityBackup.this, "you clicked " + queryString, Toast.LENGTH_LONG).show();
            }


        });

        // Below event is triggered when submit search query.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivityBackup.this).create();
                alertDialog.setMessage("Search keyword is " + query);
                alertDialog.show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("QUERY CHANGE", newText);
//                if(newText.startsWith("a")){
//                    dataArr = new String[]{"abc", "abbb", "abbdsfsdfa", "abbaa"};
//                }
//                else{
//                    dataArr = new String[]{"baa", "bac", "badwes", "bsdfae"};
//                }
//
//                // 2) After receiving the data, create new adapter
//                ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, dataArr);
//                searchAutoComplete.setAdapter(newsAdapter);
                retrieveData(newText);

//                ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, dataArr);
//                searchAutoComplete.setAdapter(newsAdapter);

                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);

    }

    private void retrieveData(String s){
        // 1) Make an API call


        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://weather-node-app-259004.appspot.com/getAutocompleteSuggestion/" + s;

        // Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
////                new Response.Listener<String>() {
////                    @Override
////                    public void onResponse(String response) {
////                        // Display the first 500 characters of the response string.
////
////                        ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, dataArr);
////                        searchAutoComplete.setAdapter(newsAdapter);
////                        Log.d("VOLLEY REQUEST ", response);
//////                        textView.setText("Response is: "+ response.substring(0,500));
////                    }
////                }, new Response.ErrorListener() {
////            @Override
////            public void onErrorResponse(VolleyError error) {
////                Log.d("VOLLEY RESPONSE ", error.toString());
////                //Log.d("Received Response ", response);
////            }
////        });

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        dataArr = new String[response.length()];
                        for(int i = 0; i < response.length(); i++) {
                            try {
                                dataArr[i] = response.getString(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        for(int j = 0; j < response.length(); j++){
                            Log.d("ARR VAL", dataArr[j]);
                        }


                        ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(MainActivityBackup.this, android.R.layout.simple_dropdown_item_1line, dataArr);
                        searchAutoComplete.setAdapter(newsAdapter);

                        Log.d("VOLLEY RESPONSE ", response.toString());
                        Log.d("VOLLEY RESPONSE ", dataArr.toString());

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.d("VOLLEY error ", error.toString());
                    }
                });


// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);


//        if(s.startsWith("a")){
//            dataArr = new String[]{"abc", "abbb", "abbdsfsdfa", "abbaa"};
//        }
//        else{
//            dataArr = new String[]{"baa", "bac", "badwes", "bsdfae"};
//        }
//
//        // 2) After receiving the data, create new adapter
//        ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, dataArr);
//        searchAutoComplete.setAdapter(newsAdapter);

    }



//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
//        alertDialog.setMessage("Search keyword is " + query);
//        alertDialog.show();
//        return false;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
//        Log.d("QUERY CHANGE", newText);
//
//        return false;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
