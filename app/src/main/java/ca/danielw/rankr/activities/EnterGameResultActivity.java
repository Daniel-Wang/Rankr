package ca.danielw.rankr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import ca.danielw.rankr.R;
import ca.danielw.rankr.adapters.UsernameAdapter;
import ca.danielw.rankr.models.LeagueModel;
import ca.danielw.rankr.models.RankingModel;
import ca.danielw.rankr.utils.Constants;
import ca.danielw.rankr.utils.Elo;
import rx.Observer;

public class EnterGameResultActivity extends AppCompatActivity {

    private TextView mWin;
    private TextView mLose;

    private boolean mResult;
    private RankingModel mCurrentUser;
    private RankingModel mOpponent;

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
                mOpponent = rankingModel;
            }
        });

        mWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResult = true;
                mCurrentUser.setWins(mCurrentUser.getWins() + 1);
                mOpponent.setLoses(mOpponent.getLoses() + 1);

                Elo.calculateElo(mCurrentUser, mOpponent, mResult);
                updateRankings();
                //Show animation
                finish();
            }
        });

        mLose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResult = false;
                mOpponent.setWins(mOpponent.getWins() + 1);
                mCurrentUser.setLoses(mCurrentUser.getLoses() + 1);

                Elo.calculateElo(mCurrentUser, mOpponent, mResult);
                updateRankings();
                //Show animation
                finish();
            }
        });
    }

    //Make database call to update the elo
    private void updateRankings() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        String leagueName = mLeague.getmLeaguename();
        String currentUserId = mCurrentUser.getId();
        String currentUsername = mCurrentUser.getUsername();

        String oppUserId = mOpponent.getId();
        String oppUsername = mOpponent.getUsername();

        kFactorUpdate(mCurrentUser);
        kFactorUpdate(mOpponent);

        Log.e("Current user", String.valueOf(mCurrentUser.getElo()));
        Log.e("Opponent", String.valueOf(mOpponent.getElo()));

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put(Constants.NODE_RANKINGS + "/" + leagueName
                + "/" + currentUsername + "/" + currentUserId, mCurrentUser);
        childUpdates.put(Constants.NODE_USERS + "/" + currentUserId + "/"
                + Constants.NODE_GAMES + "/" + currentUsername, Constants.BASE_RATING);

        childUpdates.put(Constants.NODE_RANKINGS + "/" + leagueName
                + "/" + oppUsername + "/" + oppUserId, mOpponent);
        childUpdates.put(Constants.NODE_USERS + "/" + oppUserId + "/"
                + Constants.NODE_GAMES + "/" + oppUsername, Constants.BASE_RATING);

        db.updateChildren(childUpdates);
    }

    private void kFactorUpdate(RankingModel model) {
        if(model.getElo() > Constants.MASTER_ELO) {
            model.setkFactor(Constants.MASTER_KFACTOR);
        }
    }
}
