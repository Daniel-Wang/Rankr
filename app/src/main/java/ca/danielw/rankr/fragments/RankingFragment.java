package ca.danielw.rankr.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.danielw.rankr.R;
import ca.danielw.rankr.activities.CreateGameActivity;
import ca.danielw.rankr.activities.MainActivity;
import ca.danielw.rankr.adapters.RankingAdapter;
import ca.danielw.rankr.models.LeagueModel;
import ca.danielw.rankr.models.RankingModel;
import ca.danielw.rankr.utils.Constants;

public class RankingFragment extends Fragment{

    private TextView mNoGames;
    private LinearLayout mRankingHeader;

    private DatabaseReference mDatabase;

    private Spinner mGameSpinner;
    private Button mCreateButton;

    ArrayList<LeagueModel> leagues = new ArrayList<>();

    private String mLeagueName;

    private RecyclerView rvRankings;

    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.ranking_fragment, container, false);

        mNoGames = (TextView) view.findViewById(R.id.tvNoGames);
        rvRankings = (RecyclerView) view.findViewById(R.id.rvRankings);
        mCreateButton = (Button) view.findViewById(R.id.btnCreateGame);
        mGameSpinner = (Spinner) view.findViewById(R.id.spGame);

        mRankingHeader = (LinearLayout) view.findViewById(R.id.llRankingHeader);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CreateGameActivity.class);
                startActivity(intent);
            }
        });

        // Get the rankings
        mLeagueName = ((MainActivity)getActivity()).getmLeagueName();


        mDatabase.child(Constants.NODE_RANKINGS).child(mLeagueName)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        List<String> gameList = new ArrayList<>();

                        //Get each game
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                            LeagueModel leagueModel = new LeagueModel();
                            ArrayList<RankingModel> rankings = leagueModel.getmRankings();

                            leagueModel.setmLeaguename(snapshot.getKey());
                            gameList.add(leagueModel.getmLeaguename());

                            //Get all the members and their ranking Models
                            for(DataSnapshot members: snapshot.getChildren()){
                                RankingModel rankingModel = members.getValue(RankingModel.class);

                                rankingModel.setId(members.getKey());
                                rankings.add(rankingModel);
                            }

                            //Sort the rankings by elo
                            sortRanks(rankings);

                            //Add the ranking
                            Map<String, Object> childUpdates = new HashMap<>();
                            int size = rankings.size();
                            for(int i = 0; i < size; i++){
                                RankingModel rank = rankings.get(i);
                                rank.setRank(i+1);
                                childUpdates.put(Constants.NODE_RANKINGS + "/" + mLeagueName + "/" +
                                        leagueModel.getmLeaguename() + "/" + rank.getId() + "/" + Constants.RANK, i+1);
                            }

                            mDatabase.updateChildren(childUpdates);

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
                                MainActivity.mCurrentGame = position;
                                refreshList(rvRankings);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        if(!leagues.isEmpty()) {
                            rvRankings.setVisibility(View.VISIBLE);
                            mNoGames.setVisibility(View.GONE);
                            mRankingHeader.setVisibility(View.VISIBLE);
                            mGameSpinner.setVisibility(View.VISIBLE);
                            refreshList(rvRankings);
                        } else {
                            mGameSpinner.setVisibility(View.INVISIBLE);
                            rvRankings.setVisibility(View.GONE);
                            mNoGames.setVisibility(View.VISIBLE);
                            mRankingHeader.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        return view;
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
        RankingAdapter adapter = new RankingAdapter(getContext(), leagues.get(MainActivity.mCurrentGame));

        rvRankings.setAdapter(adapter);
        rvRankings.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}