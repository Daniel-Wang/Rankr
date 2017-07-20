package ca.danielw.rankr.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import ca.danielw.rankr.R;
import ca.danielw.rankr.fragments.EmailFragment;

public class CreateLeagueActivity extends AppCompatActivity {

    public static final String TAG = CreateLeagueActivity.class.getName();

    public static String mEmail;
    public static String mLeagueName;
    public static String mUsername;
    public static String mPassword;

    public static boolean isVerified = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_league);

        if(savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();

            transaction.replace(R.id.root_frame, new EmailFragment());
            transaction.commit();
        }
    }
}
