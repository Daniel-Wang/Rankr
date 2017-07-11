package ca.danielw.rankr.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ca.danielw.rankr.R;
import ca.danielw.rankr.activities.CreateGameActivity;
import ca.danielw.rankr.activities.EnterGameResultActivity;
import ca.danielw.rankr.activities.MainActivity;
import ca.danielw.rankr.adapters.RankingAdapter;
import ca.danielw.rankr.models.LeagueModel;
import ca.danielw.rankr.models.RankingModel;
import ca.danielw.rankr.utils.Constants;

public class RankingFragment extends Fragment{

    private DatabaseReference mDatabase;
    private TextView tvGameTitle;

    private FloatingActionButton mFabRecordGame;
    private Button mCreateButton;

    ArrayList<LeagueModel> leagues = new ArrayList<>();

    private int mCurrentLeague = 0;

    private RankingModel mCurrentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.ranking_fragment, container, false);

//        mFabRecordGame = (FloatingActionButton) view.findViewById(R.id.fabRecordGame);

        final RecyclerView rvRankings = (RecyclerView) view.findViewById(R.id.rvRankings);
        tvGameTitle = (TextView) view.findViewById(R.id.tvGameTitle);
        mCreateButton = (Button) view.findViewById(R.id.btnCreateGame);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateGameActivity.class);
                startActivity(intent);
            }
        });

        // Get the rankings
        String leagueName = ((MainActivity)getActivity()).getmLeagueName();
        Log.e("Ranking Fragment", leagueName);

        mDatabase.child(Constants.NODE_RANKINGS).child(leagueName)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            Log.e("Ranking ftrag", snapshot.getKey());
                            Log.e("Ranking ftrag", String.valueOf(snapshot.exists()));
                            Log.e("Ranking ftrag", String.valueOf(snapshot.getChildrenCount()));
                            Log.e("Ranking ftrag", String.valueOf(snapshot.getChildren()));

                            LeagueModel leagueModel = new LeagueModel();
                            ArrayList<RankingModel> rankings = leagueModel.getmRankings();

                            leagueModel.setmLeaguename(snapshot.getKey());

                            for(DataSnapshot members: snapshot.getChildren()){
                                Log.e("Ranking ftrag", members.getKey());

                                RankingModel rankingModel = members.getValue(RankingModel.class);
                                String userId = members.getKey();

                                rankingModel.setId(members.getKey());
                                if(userId.equals(user.getUid())) {
                                    mCurrentUser = rankingModel;
                                }
                                rankings.add(rankingModel);
                            }

                            //Sort the rankings by elo,
                            Collections.sort(rankings, new Comparator<RankingModel>() {
                                @Override
                                public int compare(RankingModel rankingA, RankingModel rankingB) {
                                    int ratingA = rankingA.getElo();
                                    int ratingB = rankingB.getElo();

                                    if(ratingA < ratingB) {
                                        return -1;
                                    } else if(ratingA > ratingB) {
                                        return 1;
                                    } else {
                                        return 0;
                                    }
                                }
                            });

                            //Add the ranking

                            int size = rankings.size();
                            for(int i = 0; i < size; i++){
                                rankings.get(i).setRank(i+1);
                            }

                            //Add the game to the collection of games
                            leagues.add(leagueModel);
                        }

                        tvGameTitle.setText(leagues.get(mCurrentLeague).getmLeaguename());

                        RankingAdapter adapter = new RankingAdapter(getContext(), leagues.get(mCurrentLeague));

                        rvRankings.setAdapter(adapter);

                        rvRankings.setLayoutManager(new LinearLayoutManager(getContext()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        // Calculate the position differential from the previous day

        return view;
    }

    public void setFab(FloatingActionButton fab){
        mFabRecordGame = fab;
        Log.e("Ranking", String.valueOf(fab));
        if(mFabRecordGame != null){
            mFabRecordGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LeagueModel leagueModel = leagues.get(mCurrentLeague);
                    ArrayList<RankingModel> mUsersList = leagueModel.getmRankings();
                    mUsersList.remove(mCurrentUser);

                    for (RankingModel rankingModel: mUsersList){
                        boolean flag = rankingModel.equals(mCurrentUser);
                        Log.e("FAB intent", rankingModel.getUsername() + " " + flag);
                    }

                    //Start the activity
                    Intent intent = new Intent(getActivity(), EnterGameResultActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.NODE_USERS, leagueModel);
                    bundle.putSerializable(Constants.ME_USER, mCurrentUser);

                    intent.putExtras(bundle);

                    startActivity(intent);
                }
            });
        }
    }
}
