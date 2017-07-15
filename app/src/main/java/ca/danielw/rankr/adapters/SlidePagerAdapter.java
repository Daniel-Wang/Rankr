package ca.danielw.rankr.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import ca.danielw.rankr.fragments.RootFragment;
import ca.danielw.rankr.utils.Constants;

// PagerAdapter class
public class SlidePagerAdapter extends FragmentStatePagerAdapter {

    private int NUM = 2;
    private String mLocation;

    public SlidePagerAdapter(FragmentManager fm, String sourceLocation) {
        super(fm);
        mLocation = sourceLocation;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0 || position == 1){
            Bundle bundle = new Bundle();
            bundle.putString(Constants.SOURCE_LOCATION, mLocation);
            RootFragment fragment = new RootFragment();
            fragment.setArguments(bundle);
            return fragment;
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return NUM;
    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }
}