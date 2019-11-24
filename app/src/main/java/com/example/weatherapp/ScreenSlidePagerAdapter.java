package com.example.weatherapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.weatherapp.utils.SharedPreferenceFunctions;

import java.util.ArrayList;
import java.util.List;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    public List<Fragment> screen_fragments = new ArrayList<>();
    final SharedPreferenceFunctions sharedPreferenceFunctions;

    int num_pages;

    public ScreenSlidePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        sharedPreferenceFunctions = new SharedPreferenceFunctions(context);
        createAdapterData();
    }

    public void createAdapterData(){
        screen_fragments.clear();
        String storedLocations = sharedPreferenceFunctions.getSavedLocations();
        Log.d("CREATEADAPTERDATA", storedLocations);
        String parts[] = storedLocations.split("\\|");
        num_pages = parts.length;
        Log.d("CREATEADAPTERDATA", new Integer(num_pages).toString());
        for(int i = 0; i < parts.length; i++)
        {
            Log.d("PART", parts[i]);
            Bundle bundle = new Bundle();
            bundle.putString("KEY", new Integer(i).toString());
            bundle.putString("LOCATION", parts[i]);
            ScreenSlidePageFragment fragobj = new ScreenSlidePageFragment();
            fragobj.setArguments(bundle);
            screen_fragments.add(fragobj);
        }
        this.notifyDataSetChanged();
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
        return screen_fragments.size();
    }

    public void removeAtPosition(int pos){
        Log.d("BEFORE REMOVE", sharedPreferenceFunctions.getSavedLocations());
        sharedPreferenceFunctions.removeAtPosition(pos);
        Log.d("REMOVED ", new Integer(pos).toString());
        Log.d("AFTER REMOVE", sharedPreferenceFunctions.getSavedLocations());
        createAdapterData();
    }

}