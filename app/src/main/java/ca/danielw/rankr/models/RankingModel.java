package ca.danielw.rankr.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

import ca.danielw.rankr.utils.Constants;

@IgnoreExtraProperties
public class RankingModel {
    private String name;
    private String elo;

    public RankingModel() {

    }

    public RankingModel(String name, String elo){
        this.name = name;
        this.elo = elo;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put(name, elo);

        return result;
    }
}
