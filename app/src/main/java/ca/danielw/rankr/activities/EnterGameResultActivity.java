package ca.danielw.rankr.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ca.danielw.rankr.R;
import ca.danielw.rankr.utils.Constants;

public class EnterGameResultActivity extends AppCompatActivity {

    private String[] mUsernameList;
    private String mLeagueName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_game_result);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        mUsernameList = bundle.getStringArray(Constants.NODE_USERNAME);
        mLeagueName = intent.getStringExtra(Constants.LEAGUE_NAME);
    }
}
