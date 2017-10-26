package com.example.christopher.pneuvida;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christopher on 10/21/2017.
 */

public class SectionPageAdapter extends FragmentPagerAdapter{

    private final List<Fragment> hFragmentList = new ArrayList<>();
    private final List<String> hFragmentTitleList = new ArrayList<>();

    public SectionPageAdapter (FragmentManager fm) {
        super(fm);
    }

    public void addFrag (String title, Fragment fragment) {
        hFragmentTitleList.add(title);
        hFragmentList.add(fragment);
    }

    @Override
    public Fragment getItem (int position) {
        return hFragmentList.get(position);
    }

    @Override
    public int getCount () {
        return hFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle (int position) {
        return hFragmentTitleList.get(position);
    }
}
