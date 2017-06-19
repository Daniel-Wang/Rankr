package ca.danielw.rankr.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

import ca.danielw.rankr.utils.Constants;

@IgnoreExtraProperties
public class UserModel {
    private String email;
    private String league;
    private String username;

    public UserModel(){

    }

    public UserModel(String Username, String Email, String League){
        email = Email;
        username = Username;
        league = League;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(Constants.NODE_EMAIL, email);
        result.put(Constants.NODE_USERNAME, username);
        result.put(Constants.NODE_LEAGUE, league);

        return result;
    }
}
