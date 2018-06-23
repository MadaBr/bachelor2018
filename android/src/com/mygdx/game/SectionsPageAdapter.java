package com.mygdx.game;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Mada on 4/20/2018.
 */

public class SectionsPageAdapter extends FragmentStatePagerAdapter {

   // public List<Fragment> fragments = new ArrayList<>();
   // public List<String> fragments_id = new ArrayList<>();
    int tabsNr;


    public SectionsPageAdapter(FragmentManager manager, int tabsNr){

        super(manager);
        this.tabsNr = tabsNr;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch(position){
            case 0: {
                AllUsers_FRAGMENT tab1 = new AllUsers_FRAGMENT();
                return tab1;
            }
            case 1:{
               Friends_FRAGMENT tab2 = new Friends_FRAGMENT();
                return tab2;
            }

            default: {return null;}
        }
    }

    @Override
    public int getCount() {
        return tabsNr;
    }
}
