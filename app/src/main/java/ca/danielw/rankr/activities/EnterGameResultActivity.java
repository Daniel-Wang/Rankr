package ca.danielw.rankr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ca.danielw.rankr.R;
import ca.danielw.rankr.adapters.UsernameAdapter;
import ca.danielw.rankr.models.LeagueModel;
import ca.danielw.rankr.models.RankingModel;
import ca.danielw.rankr.utils.Constants;
import rx.Observer;

public class EnterGameResultActivity extends AppCompatActivity {

    private String mLeagueName;

    private TextView mWin;
    private TextView mLose;

    private boolean mResultWin;
    private RankingModel mCurrentUser;
    private LeagueModel mLeague;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_game_result);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        mLeague = (LeagueModel) bundle.getSerializable(Constants.NODE_USERS);
        mCurrentUser = (RankingModel) intent.getSerializableExtra(Constants.ME_USER);

        mWin = (TextView) findViewById(R.id.btnWin);
        mLose = (TextView) findViewById(R.id.btnLose);

        RecyclerView rvUsernames = (RecyclerView) findViewById(R.id.rvUsernames);

        UsernameAdapter adapter = new UsernameAdapter(this, mLeague);
        rvUsernames.setAdapter(adapter);
        rvUsernames.setLayoutManager(new LinearLayoutManager(this));
        rvUsernames.setHasFixedSize(true);

        adapter.getPositionClicks().subscribe(new Observer<RankingModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(RankingModel rankingModel) {

            }
        });

        mWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResultWin = true;
                calculateElo();
                updateRankings();
                finish();
            }
        });

        mLose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResultWin = false;
                calculateElo();
                updateRankings();
                finish();
            }
        });
    }

    // Update this users and mSelectedUsername user's elo
    private void calculateElo() {

    }

    //Make database call to update the elo
    private void updateRankings() {

    }
}
