package ca.danielw.rankr.models;

import java.util.ArrayList;

public class LeagueRankings {
    private ArrayList<RankingModel> rankings = new ArrayList<>();
    private String mLeagueName;

    public LeagueRankings(){

    }

    public ArrayList<RankingModel> getRankings() {
        return rankings;
    }

    public void setRankings(ArrayList<RankingModel> rankings) {
        this.rankings = rankings;
    }

    public String getmLeagueName() {
        return mLeagueName;
    }

    public void setmLeagueName(String mLeagueName) {
        this.mLeagueName = mLeagueName;
    }
}
