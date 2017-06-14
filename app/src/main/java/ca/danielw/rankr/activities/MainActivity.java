package ca.danielw.rankr.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import ca.danielw.rankr.R;
import ca.danielw.rankr.adapters.SlidePagerAdapter;
import ca.danielw.rankr.utils.Constants;

public class MainActivity extends AppCompatActivity {

    private ViewPager mPager;
    private SlidePagerAdapter mPagerAdapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mPagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), Constants.HOME_FRAGMENT);
                    mPager.setAdapter(mPagerAdapter);
                    return true;
                case R.id.navigation_profile:
//                    mPagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), Constants.PROFILE_FRAGMENT);
//                    mPager.setAdapter(mPagerAdapter);
                    return true;
                case R.id.navigation_settings:
//                    mPagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), Constants.SETTINGS_FRAGMENT);
//                    mPager.setAdapter(mPagerAdapter);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mPager = (ViewPager) findViewById(R.id.vpPager);
        mPagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), Constants.HOME_FRAGMENT);
        mPager.setAdapter(mPagerAdapter);
    }

}
