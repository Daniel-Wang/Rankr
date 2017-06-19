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
    private List<String> mMembers = new ArrayList<>();
    private List<String> mGames = new ArrayList<>();

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
}
