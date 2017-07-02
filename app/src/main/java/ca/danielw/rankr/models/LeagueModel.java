package ca.danielw.rankr.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.danielw.rankr.utils.Constants;

@IgnoreExtraProperties
public class LeagueModel {
    private String mLeaguename;
    private List<String> mMembers = new ArrayList<>();
    private List<String> mGames = new ArrayList<>();

    private ArrayList<RankingModel> mRankings = new ArrayList<>();

    public LeagueModel(){

    }

    public LeagueModel(List<String> members){
        mMembers = members;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(Constants.NODE_MEMBERS, mMembers);
        result.put(Constants.NODE_GAMES, mGames);

        return result;
    }

    public String getmLeaguename() {
        return mLeaguename;
    }

    public void setmLeaguename(String mLeaguename) {
        this.mLeaguename = mLeaguename;
    }

    public List<String> getmMembers() {
        return mMembers;
    }

    public void setmMembers(List<String> mMembers) {
        this.mMembers = mMembers;
    }

    public List<String> getmGames() {
        return mGames;
    }

    public void setmGames(List<String> mGames) {
        this.mGames = mGames;
    }

    public ArrayList<RankingModel> getmRankings() {
        return mRankings;
    }

    public void setmRankings(ArrayList<RankingModel> mRankings) {
        this.mRankings = mRankings;
    }
}
