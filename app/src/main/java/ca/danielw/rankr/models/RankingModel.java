package ca.danielw.rankr.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class RankingModel {
    private String id;
    private String elo;

    private String username;
    private String prevElo;
    private int rank;

    public RankingModel() {

    }

    public RankingModel(String id, String elo){
        this.id = id;
        this.elo = elo;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put(id, elo);

        return result;
    }

    public String getId() {
        return id;
    }

    public String getElo() {
        return elo;
    }

    public String getUsername() {
        return username;
    }

    public String getPrevElo() {
        return prevElo;
    }

    public int getRank(){
        return rank;
    }
}
