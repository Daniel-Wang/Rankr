package ca.danielw.rankr.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import ca.danielw.rankr.R;
import ca.danielw.rankr.fragments.SignInLeagueNameFragment;
import ca.danielw.rankr.utils.Constants;

public class SignUpActivity extends AppCompatActivity {

    public static String mEmail;
    public static String mLeagueName;
    public static String mPassword;
    public static String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_up);

        Intent intent = getIntent();
        Uri data = intent.getData();
        mLeagueName = data.getQueryParameter(Constants.NODE_LEAGUE);

        if(savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();

            Bundle bundle2 = new Bundle();
            Fragment fragment = new SignInLeagueNameFragment();

            bundle2.putString(Constants.SIGN_IN_INTENT, Constants.SIGNUP_FRAGMENT);
            fragment.setArguments(bundle2);

            transaction.replace(R.id.root_frame, fragment);
            transaction.commit();
        }
    }
}