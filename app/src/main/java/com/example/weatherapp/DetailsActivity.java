package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherapp.Fragments.PhotosFragment;
import com.example.weatherapp.Fragments.TodayFragment;
import com.example.weatherapp.Fragments.WeeklyFragment;
import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String selectedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#000000")));


        Intent intent = getIntent();
        selectedLocation = intent.getStringExtra("SELECTED_LOCATION");
        getSupportActionBar().setTitle(selectedLocation);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        createViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        createTabIcons();

        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);

                        ImageView imgView = tab.getCustomView().findViewById(R.id.icon_tab);
                        imgView.setColorFilter(getResources().getColor(R.color.colorWhite));

                        TextView tv = tab.getCustomView().findViewById(R.id.text_tab);
                        tv.setTextColor(getResources().getColor(R.color.colorWhite));

                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        ImageView imgView = tab.getCustomView().findViewById(R.id.icon_tab);
                        imgView.setColorFilter(getResources().getColor(R.color.colorDarkGrey));

                        TextView tv = tab.getCustomView().findViewById(R.id.text_tab);
                        tv.setTextColor(getResources().getColor(R.color.colorDarkGrey));
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );

    }

    private void createTabIcons() {

        View tabOne = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ImageView imgView = tabOne.findViewById(R.id.icon_tab);
        imgView.setImageResource(R.drawable.calendar_today);

        TextView tabTv = tabOne.findViewById(R.id.text_tab);
        tabTv.setText("TODAY");
        tabTv.setTextColor(getResources().getColor(R.color.colorWhite));
        tabLayout.getTabAt(0).setCustomView(tabOne);


        View tabTwo = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ImageView imgView2 = tabTwo.findViewById(R.id.icon_tab);
        imgView2.setImageResource(R.drawable.trending_up);
        imgView2.setColorFilter(getResources().getColor(R.color.colorDarkGrey));


        TextView tabTv2 = tabTwo.findViewById(R.id.text_tab);
        tabTv2.setText("WEEKLY");

        tabLayout.getTabAt(1).setCustomView(tabTwo);


        View tabThree = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ImageView imgView3 = tabThree.findViewById(R.id.icon_tab);
        imgView3.setImageResource(R.drawable.google_photos);
        imgView3.setColorFilter(getResources().getColor(R.color.colorDarkGrey));

        TextView tabTv3 = tabThree.findViewById(R.id.text_tab);
        tabTv3.setText("PHOTOS");
        tabLayout.getTabAt(2).setCustomView(tabThree);

    }

    private void createViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putString("SELECTED_LOCATION", selectedLocation);
        TodayFragment todayFragment = new TodayFragment();
        todayFragment.setArguments(bundle);
        adapter.addFrag(todayFragment, "Tab 1");

        WeeklyFragment weeklyFragment = new WeeklyFragment();
        weeklyFragment.setArguments(bundle);
        adapter.addFrag(weeklyFragment, "Tab 2");

        PhotosFragment photosFragment = new PhotosFragment();
        photosFragment.setArguments(bundle);
        adapter.addFrag(photosFragment, "Tab 3");
//        
//        adapter.addFrag(new TodayFragment(), "Tab 1");
//        adapter.addFrag(new WeeklyFragment(), "Tab 2");
//        adapter.addFrag(new PhotosFragment(), "Tab 3");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
