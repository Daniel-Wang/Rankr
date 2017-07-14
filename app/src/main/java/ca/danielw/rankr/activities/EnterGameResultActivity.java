package ca.danielw.rankr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.danielw.rankr.R;
import ca.danielw.rankr.adapters.UsernameAdapter;
import ca.danielw.rankr.models.LeagueModel;
import ca.danielw.rankr.models.RankingModel;
import ca.danielw.rankr.utils.Constants;
import ca.danielw.rankr.utils.Elo;
import rx.Observer;

public class EnterGameResultActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private TextView mWin;
    private TextView mLose;
    private LinearLayout mBottomBar;

    private boolean mResult;
    private RankingModel mCurrentUser;
    private RankingModel mOpponent;

    private LeagueModel mLeague;
    private String mLeagueName;

    private int mCurrentGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_game_result);

        mWin = (TextView) findViewById(R.id.btnWin);
        mLose = (TextView) findViewById(R.id.btnLose);
        mBottomBar = (LinearLayout) findViewById(R.id.llSelector);

        RecyclerView rvUsernames = (RecyclerView) findViewById(R.id.rvUsernames);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        mCurrentGame = intent.getIntExtra(Constants.CURRENT_GAME, 0);
        mLeagueName = intent.getStringExtra(Constants.LEAGUE_NAME);

        getUsersFromDb(rvUsernames);

        mWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResult = true;
                mCurrentUser.setWins(mCurrentUser.getWins() + 1);
                mOpponent.setLoses(mOpponent.getLoses() + 1);

                Log.e("Current", String.valueOf(mCurrentUser.getkFactor()));
                Log.e("Opponent", String.valueOf(mOpponent.getkFactor()));

                Elo.calculateElo(mCurrentUser, mOpponent, mResult);
                updateRankings();
                //Show animation
                sendEndResultIntent();
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
                sendEndResultIntent();
                finish();
            }
        });
    }

    private void getUsersFromDb(final RecyclerView rvUsernames) {
        mDatabase.child(Constants.NODE_RANKINGS).child(mLeagueName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        int gameNum = 0;
                        //Get each game
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            if(mCurrentGame == gameNum) {
                                Log.e("Ranking frag", snapshot.getKey());
                                Log.e("Ranking frag", String.valueOf(snapshot.exists()));
                                Log.e("Ranking frag", String.valueOf(snapshot.getChildrenCount()));
                                Log.e("Ranking frag", String.valueOf(snapshot.getChildren()));

                                LeagueModel leagueModel = new LeagueModel();
                                ArrayList<RankingModel> rankings = leagueModel.getmRankings();

                                leagueModel.setmLeaguename(snapshot.getKey());

                                //Get all the members and their ranking Models
                                for (DataSnapshot members : snapshot.getChildren()) {
                                    Log.e("Ranking frag", members.getKey());

                                    RankingModel rankingModel = members.getValue(RankingModel.class);

                                    rankingModel.setId(members.getKey());
                                    if (user.getUid().equals(members.getKey())) {
                                        mCurrentUser = rankingModel;
                                    } else {
                                        rankings.add(rankingModel);
                                    }
                                }
                                mLeague = leagueModel;

                                UsernameAdapter adapter = new UsernameAdapter(EnterGameResultActivity.this, mLeague);
                                rvUsernames.setAdapter(adapter);
                                rvUsernames.setLayoutManager(new LinearLayoutManager(EnterGameResultActivity.this));
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
                                        mBottomBar.setVisibility(View.VISIBLE);
                                    }
                                });
                                break;
                            } else {
                                gameNum++;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    //Make database call to update the elo
    private void updateRankings() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        String gameName = mLeague.getmLeaguename();
        String currentUserId = mCurrentUser.getId();

        String oppUserId = mOpponent.getId();

        Log.e("Current user k factor", String.valueOf(mCurrentUser.getkFactor()));
        Log.e("Opponent k factor", String.valueOf(mOpponent.getkFactor()));

        mCurrentUser.setId(null);
        mOpponent.setId(null);

        kFactorUpdate(mCurrentUser);
        kFactorUpdate(mOpponent);

        Log.e("Current user", String.valueOf(mCurrentUser.getElo()));
        Log.e("Opponent", String.valueOf(mOpponent.getElo()));

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put(Constants.NODE_RANKINGS + "/" + mLeagueName + "/" + gameName
                 + "/" + currentUserId, mCurrentUser);
        childUpdates.put(Constants.NODE_USERS + "/" + currentUserId + "/"
                + Constants.NODE_GAMES + "/" + gameName, mCurrentUser.getElo());

        childUpdates.put(Constants.NODE_RANKINGS + "/" + mLeagueName + "/" + gameName
                + "/" + oppUserId, mOpponent);
        childUpdates.put(Constants.NODE_USERS + "/" + oppUserId + "/"
                + Constants.NODE_GAMES + "/" + gameName, mOpponent.getElo());

        db.updateChildren(childUpdates);
    }

    private void kFactorUpdate(RankingModel model) {

        //More than 30 games or past master level
        if(model.getWins() + model.getLoses() > 30 || model.getElo() > Constants.MASTER_ELO) {
            model.setkFactor(Constants.MASTER_KFACTOR);
        }
    }

    private void sendEndResultIntent() {
        Intent returnIntent = new Intent();
        setResult(Constants.RESULT_OK,returnIntent);
    }
}