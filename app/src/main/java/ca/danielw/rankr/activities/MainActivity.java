package ca.danielw.rankr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import ca.danielw.rankr.R;
import ca.danielw.rankr.adapters.SlidePagerAdapter;
import ca.danielw.rankr.utils.Constants;

public class MainActivity extends AppCompatActivity {

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
//        final String leagueKey;

        mDatabase.child(Constants.NODE_USERS).child(currentUser.getUid())
                .child(Constants.NODE_LEAGUE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String leagueKey = (String) dataSnapshot.getValue();

                mDatabase.child(Constants.NODE_RANKINGS).child(leagueKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue() == null){
                            Intent intent = new Intent(MainActivity.this, CreateGameActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        Log.e("League key", leagueKey);
//        Log.e("Value retrieved", mDatabase.child(Constants.NODE_RANKINGS).orderByChild(leagueKey).toString());

//        if(mDatabase.child(Constants.NODE_RANKINGS).orderByChild(leagueKey) == null){
//            Intent intent = new Intent(this, CreateGameActivity.class);
//            startActivity(intent);
//        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mPager = (ViewPager) findViewById(R.id.vpPager);
        mPagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), Constants.HOME_FRAGMENT);
        mPager.setAdapter(mPagerAdapter);
    }

}
