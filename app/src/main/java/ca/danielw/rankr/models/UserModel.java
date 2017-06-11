package ca.danielw.rankr.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class UserModel {
    public String email;
    public String league;
    public String username;

    public UserModel(){

    }

    public UserModel(String Username, String Email, String leagueId){
        email = Email;
        username = Username;
        league = leagueId;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("username", username);

        return result;
    }
}
