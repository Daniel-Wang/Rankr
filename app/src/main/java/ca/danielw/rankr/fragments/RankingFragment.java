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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ca.danielw.rankr.R;
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

    ArrayList<LeagueModel> leagues = new ArrayList<>();

    private int mCurrentLeague = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.ranking_fragment, container, false);

        final RecyclerView rvRankings = (RecyclerView) view.findViewById(R.id.rvRankings);
        tvGameTitle = (TextView) view.findViewById(R.id.tvGameTitle);
        mFabRecordGame = (FloatingActionButton) view.findViewById(R.id.fabRecordGame);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Get the rankings
        String leagueName = ((MainActivity)getActivity()).getmLeagueName();
        Log.e("Ranking Fragment", leagueName);

        mFabRecordGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LeagueModel leagueModel = leagues.get(mCurrentLeague);
                String[] usernameList = new String[leagueModel.getmRankings().size()];

                //Generate an arraylist of usernames
                int counter = 0;
                for(RankingModel model : leagueModel.getmRankings()){
                    usernameList[counter] = model.getUsername();
                    counter++;
                }

                //Start the activity
                Intent intent = new Intent(getActivity(), EnterGameResultActivity.class);
                intent.putExtra(Constants.LEAGUE_NAME, leagueModel.getmLeaguename());

                Bundle bundle = new Bundle();
                bundle.putStringArray(Constants.NODE_USERNAME, usernameList);

                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        mDatabase.child(Constants.NODE_RANKINGS).child(leagueName)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
//                            Log.e("Ranking ftrag", snapshot.getKey());
//                            Log.e("Ranking ftrag", String.valueOf(snapshot.exists()));
//                            Log.e("Ranking ftrag", String.valueOf(snapshot.getChildrenCount()));
//                            Log.e("Ranking ftrag", String.valueOf(snapshot.getChildren()));

                            LeagueModel leagueModel = new LeagueModel();
                            leagueModel.setmLeaguename(snapshot.getKey());

                            for(DataSnapshot members: snapshot.getChildren()){
//                                Log.e("Ranking ftrag", members.getKey());

                                RankingModel rankingModel = members.getValue(RankingModel.class);
                                leagueModel.getmRankings().add(rankingModel);
                            }
                            leagues.add(leagueModel);
                        }

                        tvGameTitle.setText(leagues.get(mCurrentLeague).getmLeaguename());

                        RankingAdapter adapter = new RankingAdapter(getContext(), leagues.get(0));

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
}
