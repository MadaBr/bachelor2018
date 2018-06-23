package com.mygdx.game;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

public class ExploreTabActivity extends AppCompatActivity {

    public static String TAG = "EXPLORE_ACTIVITY";
    public SectionsPageAdapter sectionsAdapter;
    public ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_tab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("All users"));
        tabs.addTab(tabs.newTab().setText("Friends"));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        sectionsAdapter = new SectionsPageAdapter(getSupportFragmentManager(), tabs.getTabCount());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(sectionsAdapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    /*public void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new AllUsers_FRAGMENT(), "ALL_USERS");
        adapter.addFragment(new AllUsers_FRAGMENT(), "ALL_USERS2");
        viewPager.setAdapter(adapter);
    }*/
}

