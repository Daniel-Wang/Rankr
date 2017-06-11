package ca.danielw.rankr.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.danielw.rankr.R;
import ca.danielw.rankr.adapters.SlidePagerAdapter;

public class CreateLeagueActivity extends AppCompatActivity {

    public static final String SOURCE_LOCATION = "CREATE_LEAGUE";

    public static final String TAG = CreateLeagueActivity.class.getName();

    public static FirebaseAuth mAuth;

    private ViewPager mPager;
    private SlidePagerAdapter mPagerAdapter;

    public static String mEmail;
    public static String mLeagueName;
    public static String mUsername;
    public static String mPassword;

    public FirebaseDatabase database;
    public static DatabaseReference dbRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_league);

        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();

        /* Instantiate a ViewPager and a PagerAdapter. */
        mPager = (ViewPager) findViewById(R.id.vpPager);
        mPagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), SOURCE_LOCATION);
        mPager.setAdapter(mPagerAdapter);

    }
}
