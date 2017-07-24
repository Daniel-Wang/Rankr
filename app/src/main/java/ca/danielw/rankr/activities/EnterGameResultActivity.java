package ca.danielw.rankr.activities;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
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
import java.util.Map;

import ca.danielw.rankr.R;
import ca.danielw.rankr.adapters.UsernameAdapter;
import ca.danielw.rankr.fragments.GameResultFragment;
import ca.danielw.rankr.models.LeagueModel;
import ca.danielw.rankr.models.RankingModel;
import ca.danielw.rankr.utils.Constants;
import ca.danielw.rankr.utils.Elo;
import io.codetail.animation.ViewAnimationUtils;
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
    private int mOrigElo;

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

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;

        mWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int cx = (mWin.getLeft() + mWin.getRight()) / 2;
                int cy = (mWin.getTop() + mWin.getBottom()) / 2;

                // get the final radius for the clipping circle
                int dx = Math.max(cx, width);
                int dy = Math.max(cy, height);
                float finalRadius = (float) Math.hypot(dx, dy);

                // Android native animator
                mWin.setHeight(height);
                mWin.setWidth(width);
                mLose.setVisibility(View.GONE);
                Animator animator =
                        ViewAnimationUtils.createCircularReveal(mWin, width, height, 0, finalRadius);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(800);
                animator.start();

                mResult = true;
                mCurrentUser.setWins(mCurrentUser.getWins() + 1);
                mOpponent.setLoses(mOpponent.getLoses() + 1);

                Elo.calculateElo(mCurrentUser, mOpponent, mResult);
                updateRankings();

                startGameResultFragment(true);
            }
        });

        mLose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cx = (mLose.getLeft() + mLose.getRight()) / 2;
                int cy = (mLose.getTop() + mLose.getBottom()) / 2;

                // get the final radius for the clipping circle
                int dx = Math.max(cx, width);
                int dy = Math.max(cy, height);
                float finalRadius = (float) Math.hypot(dx, dy);

                // Android native animator
                mLose.setHeight(height);
                mLose.setWidth(width);
                mWin.setVisibility(View.GONE);
                Animator animator =
                        ViewAnimationUtils.createCircularReveal(mLose, 0, height, 0, finalRadius);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(800);
                animator.start();

                mResult = false;
                mOpponent.setWins(mOpponent.getWins() + 1);
                mCurrentUser.setLoses(mCurrentUser.getLoses() + 1);

                Elo.calculateElo(mCurrentUser, mOpponent, mResult);
                updateRankings();

                startGameResultFragment(false);
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

                                LeagueModel leagueModel = new LeagueModel();
                                ArrayList<RankingModel> rankings = leagueModel.getmRankings();

                                leagueModel.setmLeaguename(snapshot.getKey());

                                //Get all the members and their ranking Models
                                for (DataSnapshot members : snapshot.getChildren()) {

                                    RankingModel rankingModel = members.getValue(RankingModel.class);

                                    rankingModel.setId(members.getKey());
                                    if (user.getUid().equals(members.getKey())) {
                                        mCurrentUser = rankingModel;
                                        mOrigElo = mCurrentUser.getElo();
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

        mCurrentUser.setId(null);
        mOpponent.setId(null);

        kFactorUpdate(mCurrentUser);
        kFactorUpdate(mOpponent);

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

    private void startGameResultFragment(final boolean isWin) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction();

                transaction.replace(R.id.rlBackground, GameResultFragment.newInstance(mOrigElo, mCurrentUser.getElo(), isWin));
                transaction.commit();
            }
        }, 800);
    }
}