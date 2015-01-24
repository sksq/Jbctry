package com.shubham.rural2g;

/**
 * Created by Shubham on 20-01-2015.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    protected Context mContext;
    String query;

    public SectionsPagerAdapter(Context context, FragmentManager fm, String query1) {
        super(fm);
        query = query1;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch(position) {
            case 0:
                return new EduFragment(query);
            case 1:
                return new EnterFragment(query);
            case 2:
                return new NewsFragment(query);
            case 3:
                return new WeatherFragment(query);
        }

        return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return mContext.getString(R.string.title_section1).toUpperCase(l);
            case 1:
                return mContext.getString(R.string.title_section3).toUpperCase(l);
            case 2:
                return mContext.getString(R.string.title_section2).toUpperCase(l);
            case 3:
                return mContext.getString(R.string.title_section4).toUpperCase(l);
        }
        return null;
    }
}
