package com.example.arpansharma.bloodbankremastered;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Arpan Sharma on 21-09-2017.
 */

public class Pager extends FragmentStatePagerAdapter {
    int tabCount;

    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0 :
                RequestActivity tab1 = new RequestActivity();
                return tab1;
            case 1 :
                RegisterActivity tab2 = new RegisterActivity();
                return tab2;
            default :
                return null;
        }
    }

    @Override
    public int getCount(){
        return tabCount;
    }
}
