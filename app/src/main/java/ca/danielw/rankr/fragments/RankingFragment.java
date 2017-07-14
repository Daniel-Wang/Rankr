package ca.danielw.rankr.fragments;

import android.app.Activity;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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
import java.util.List;

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

    private FloatingActionButton mFabRecordGame;
    private Spinner mGameSpinner;
    private Button mCreateButton;

    ArrayList<LeagueModel> leagues = new ArrayList<>();

    private int mCurrentLeague = 0;

    private RankingModel mCurrentUser;
    private String mLeagueName;

    private RecyclerView rvRankings;

    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.ranking_fragment, container, false);

//        mFabRecordGame = (FloatingActionButton) view.findViewById(R.id.fabRecordGame);

        rvRankings = (RecyclerView) view.findViewById(R.id.rvRankings);
        mCreateButton = (Button) view.findViewById(R.id.btnCreateGame);
        mGameSpinner = (Spinner) view.findViewById(R.id.spGame);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CreateGameActivity.class);
                startActivity(intent);
            }
        });

        // Get the rankings
        mLeagueName = ((MainActivity)getActivity()).getmLeagueName();
        Log.e("Ranking Fragment", mLeagueName);

        mDatabase.child(Constants.NODE_RANKINGS).child(mLeagueName)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.e("Data changed", "Hello");

                        List<String> gameList = new ArrayList<>();

                        //Get each game
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            Log.e("Ranking frag", snapshot.getKey());
                            Log.e("Ranking frag", String.valueOf(snapshot.exists()));
                            Log.e("Ranking frag", String.valueOf(snapshot.getChildrenCount()));
                            Log.e("Ranking frag", String.valueOf(snapshot.getChildren()));

                            LeagueModel leagueModel = new LeagueModel();
                            ArrayList<RankingModel> rankings = leagueModel.getmRankings();

                            leagueModel.setmLeaguename(snapshot.getKey());
                            gameList.add(leagueModel.getmLeaguename());

                            //Get all the members and their ranking Models
                            for(DataSnapshot members: snapshot.getChildren()){
                                Log.e("Ranking frag", members.getKey());

                                RankingModel rankingModel = members.getValue(RankingModel.class);
                                String userId = members.getKey();

                                rankingModel.setId(members.getKey());
                                if(userId.equals(user.getUid())) {
                                    mCurrentUser = rankingModel;
                                }
                                rankings.add(rankingModel);
                            }

                            //Sort the rankings by elo
                            sortRanks(rankings);

                            //Add the ranking
                            int size = rankings.size();
                            for(int i = 0; i < size; i++){
                                rankings.get(i).setRank(i+1);
                            }

                            //Add the game to the collection of games
                            leagues.add(leagueModel);
                        }

                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(mContext,
                                R.layout.game_spinner_item, gameList);
                        spinnerAdapter.setDropDownViewResource(R.layout.game_spinner_dropdown_item);

                        mGameSpinner.setAdapter(spinnerAdapter);

                        mGameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mCurrentLeague = position;
                                refreshList(rvRankings);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        refreshList(rvRankings);
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
                    leagueModel.getmRankings().remove(mCurrentUser);

                    //Start the activity
                    Intent intent = new Intent(mContext, EnterGameResultActivity.class);

                    Log.e("Current", String.valueOf(mCurrentUser.getkFactor()));
                    Log.e("Opponent1", String.valueOf(leagueModel.getmRankings().get(0).getkFactor()));
                    Log.e("Opponent2", String.valueOf(leagueModel.getmRankings().get(1).getkFactor()));
                    Log.e("Opponent3", String.valueOf(leagueModel.getmRankings().get(2).getkFactor()));

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.NODE_USERS, leagueModel);
                    bundle.putSerializable(Constants.ME_USER, mCurrentUser);

                    intent.putExtra(Constants.LEAGUE_NAME, mLeagueName);
                    intent.putExtras(bundle);


                    startActivityForResult(intent, Constants.ENTER_GAME_RESULT);

                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.ENTER_GAME_RESULT) {
            if(resultCode == Constants.RESULT_OK) {
                ArrayList<RankingModel> rankings = leagues.get(mCurrentLeague).getmRankings();
                rankings.add(mCurrentUser);

                sortRanks(rankings);
                refreshList(rvRankings);
            }
        }
    }

    private void sortRanks(ArrayList<RankingModel> rankingModels) {
        Collections.sort(rankingModels, new Comparator<RankingModel>() {
            @Override
            public int compare(RankingModel rankingA, RankingModel rankingB) {
                int ratingA = rankingA.getElo();
                int ratingB = rankingB.getElo();

                if(ratingA > ratingB) {
                    return -1;
                } else if(ratingA < ratingB) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
    }

    private void refreshList(RecyclerView rvRankings) {
        RankingAdapter adapter = new RankingAdapter(getContext(), leagues.get(mCurrentLeague));

        rvRankings.setAdapter(adapter);
        rvRankings.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}