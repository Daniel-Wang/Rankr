package ca.danielw.rankr.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.danielw.rankr.R;
import ca.danielw.rankr.adapters.SlidePagerAdapter;
import ca.danielw.rankr.utils.Constants;

public class MainActivity extends AppCompatActivity {

    private String mLeagueName;

    private ViewPager mPager;
    private SlidePagerAdapter mPagerAdapter;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

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

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        Log.e("MainActivity", currentUser.getUid());

        //Get this user's League and check if a game exists, if not, then start the create game activity
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        mLeagueName = sharedPref.getString(Constants.LEAGUE_NAME, null);

        Log.e("MainActivity", mLeagueName);

        if (mLeagueName != null) {
            updateUI();
        } else {
            mDatabase.child(Constants.NODE_USERS).child(currentUser.getUid())
                    .child(Constants.NODE_LEAGUE).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mLeagueName = (String) dataSnapshot.getValue();

                    SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(Constants.LEAGUE_NAME, mLeagueName);
                    editor.commit();

                    updateUI();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public String getmLeagueName() {
        return mLeagueName;
    }

    private void updateUI(){
        mDatabase.child(Constants.NODE_RANKINGS).child(mLeagueName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("mainActivity", String.valueOf(dataSnapshot.exists()));
                if (dataSnapshot.exists()) {
                    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
                    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

                    mPager = (ViewPager) findViewById(R.id.vpPager);
                    mPagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), Constants.HOME_FRAGMENT);
                    mPager.setAdapter(mPagerAdapter);
                } else {
                    Intent intent = new Intent(MainActivity.this, CreateGameActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
