package ca.danielw.rankr.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

import ca.danielw.rankr.utils.Constants;

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

    public void setId(String id) {
        this.id = id;
    }

    public void setElo(String elo) {
        this.elo = elo;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPrevElo(String prevElo) {
        this.prevElo = prevElo;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put(Constants.NODE_ELO, elo);
        result.put(Constants.NODE_PREV, elo);

        return result;
    }
}
