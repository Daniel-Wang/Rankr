package ca.danielw.rankr.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ca.danielw.rankr.R;
import ca.danielw.rankr.activities.MainActivity;
import ca.danielw.rankr.adapters.RankingAdapter;
import ca.danielw.rankr.models.LeagueModel;
import ca.danielw.rankr.models.RankingModel;
import ca.danielw.rankr.utils.Constants;

public class RankingFragment extends Fragment{

    private DatabaseReference mDatabase;

    ArrayList<LeagueModel> leagues = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.ranking_fragment, container, false);

        RecyclerView rvRankings = (RecyclerView) view.findViewById(R.id.rvRankings);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Get the rankings

        mDatabase.child(Constants.NODE_RANKINGS).child(((MainActivity)getActivity()).getmLeagueName())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            LeagueModel leagueModel = new LeagueModel();
                            leagueModel.setmLeaguename(snapshot.getKey());

                            for(DataSnapshot members: snapshot.getChildren()){
                                RankingModel rankingModel = members.getValue(RankingModel.class);
                                leagueModel.getmRankings().add(rankingModel);
                            }
                            leagues.add(leagueModel);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        // Calculate the position differential from the previous day
        RankingModel model1 = new RankingModel();
        model1.setElo("2000");
        model1.setRank(1);
        model1.setUsername("dwang");

//        rankings.add(model1);

        RankingAdapter adapter = new RankingAdapter(getContext(), leagues.get(0));

        rvRankings.setAdapter(adapter);

        rvRankings.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}
