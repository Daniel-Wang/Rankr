package ca.danielw.rankr.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ca.danielw.rankr.utils.Constants;

@IgnoreExtraProperties
public class RankingModel implements Serializable{
    private String id;
    private String elo;

    private String username;
    private String prevEloRank;
    private int rank;

    public RankingModel() {

    }

    public RankingModel(String id, String elo, String username){
        this.id = id;
        this.elo = elo;
        this.username = username;
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

    public String getPrevEloRank() {
        return prevEloRank;
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

    public void setPrevEloRank(String prevEloRank) {
        this.prevEloRank = prevEloRank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put(Constants.NODE_ELO, elo);
        result.put(Constants.NODE_PREV, prevEloRank);
        result.put(Constants.NODE_USERNAME, username);

        return result;
    }
}
