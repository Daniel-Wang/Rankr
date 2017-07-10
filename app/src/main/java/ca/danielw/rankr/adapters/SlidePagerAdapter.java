package ca.danielw.rankr.adapters;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import ca.danielw.rankr.fragments.RootFragment;

// PagerAdapter class
public class SlidePagerAdapter extends FragmentPagerAdapter {
    private FloatingActionButton mFab = null;
    private RootFragment fabFrag = null;

    public int NUM = 1;
    private String mLocation;

    public static final String PUT_EXTRA_SOURCE_LOCATION = "source_location";

    public SlidePagerAdapter(FragmentManager fm, String sourceLocation) {
        super(fm);
        mLocation = sourceLocation;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            Bundle bundle = new Bundle();
            bundle.putString(PUT_EXTRA_SOURCE_LOCATION, mLocation);
            RootFragment fragment = new RootFragment();
            fabFrag = fragment;
            fragment.setFab(mFab);
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

    public void setFab(FloatingActionButton fab) {
        if(fabFrag != null){
            fabFrag.setFab(fab);
        }
        Log.e("Page adapter", String.valueOf(fab));
    }
}