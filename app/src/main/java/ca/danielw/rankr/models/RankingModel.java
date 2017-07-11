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
    private int elo;
    private String username;
    private int kFactor;
    private int wins;
    private int loses;

    private int prevEloRank;
    private int rank;


    public RankingModel() {

    }

    public RankingModel(String id, int elo, String username, int totalGames, int kFactor){
        this.id = id;
        this.elo = elo;
        this.username = username;
        this.kFactor = kFactor;
        wins = totalGames;
        loses = totalGames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RankingModel that = (RankingModel) o;

        if (elo != that.elo) return false;
        if (kFactor != that.kFactor) return false;
        if (wins != that.wins) return false;
        if (loses != that.loses) return false;
        if (prevEloRank != that.prevEloRank) return false;
        if (rank != that.rank) return false;
        if (!id.equals(that.id)) return false;
        return username.equals(that.username);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + elo;
        result = 31 * result + username.hashCode();
        result = 31 * result + kFactor;
        result = 31 * result + wins;
        result = 31 * result + loses;
        result = 31 * result + prevEloRank;
        result = 31 * result + rank;
        return result;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLoses() {
        return loses;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    public int getkFactor() {
        return kFactor;
    }

    public void setkFactor(int kFactor) {
        this.kFactor = kFactor;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getPrevEloRank() {
        return prevEloRank;
    }

    public int getRank(){
        return rank;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPrevEloRank(int prevEloRank) {
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
        result.put(Constants.K_FACTOR, kFactor);
        result.put(Constants.WINS, wins);
        result.put(Constants.LOSES, loses);

        return result;
    }
}
