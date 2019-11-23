package com.example.weatherapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    public List<Fragment> screen_fragments = new ArrayList<>();
    int num_pages = 5;

    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);

        for(int i = 0; i < num_pages; i++)
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
        return num_pages;
    }

    public void removeAtPosition(int pos){
        screen_fragments.remove(pos);
        num_pages -= 1;
        screen_fragments.clear();

        for(int i = 0; i < num_pages; i++)
        {
            Bundle bundle = new Bundle();
            bundle.putString("KEY", new Integer(i).toString());
            ScreenSlidePageFragment fragobj = new ScreenSlidePageFragment();
            fragobj.setArguments(bundle);
            screen_fragments.add(fragobj);
        }
        this.notifyDataSetChanged();
    }

    public void addNewFragment(){

        ScreenSlidePageFragment fragobj = new ScreenSlidePageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("KEY", new Integer(num_pages).toString());
        fragobj.setArguments(bundle);
        screen_fragments.add(fragobj);
        num_pages += 1;
        this.notifyDataSetChanged();
    }
}