package ca.danielw.rankr.activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ca.danielw.rankr.R;
import ca.danielw.rankr.adapters.SlidePagerAdapter;
import ca.danielw.rankr.utils.Constants;

public class SignUpActivity extends AppCompatActivity {

    private ViewPager mPager;
    private SlidePagerAdapter mPagerAdapter;

    public static String mEmail;
    public static String mLeagueName;
    public static String mPassword;
    public static String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_up);

        /* Instantiate a ViewPager and a PagerAdapter. */
        mPager = (android.support.v4.view.ViewPager) findViewById(R.id.vpPager);
        mPagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), Constants.SIGNUP_FRAGMENT);
        mPager.setAdapter(mPagerAdapter);
    }
}
