package ca.danielw.rankr.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ca.danielw.rankr.R;
import ca.danielw.rankr.adapters.RankingAdapter;
import ca.danielw.rankr.models.RankingModel;

public class RankingFragment extends Fragment{

    ArrayList<RankingModel> rankings = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.ranking_fragment, container, false);

        RecyclerView rvRankings = (RecyclerView) view.findViewById(R.id.rvRankings);

        // Get the rankings

        // Calculate the position differential from the previous day
        RankingModel model1 = new RankingModel();
        model1.setElo("2000");
        model1.setRank(1);
        model1.setUsername("dwang");

        rankings.add(model1);

        RankingAdapter adapter = new RankingAdapter(getContext(), rankings);

        rvRankings.setAdapter(adapter);

        rvRankings.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}
