package ca.danielw.rankr.utils;

public final class Constants {

    public static final String NODE_LEAGUES = "leagues";
    public static final String NODE_NAME = "name";
    public static final String NODE_MEMBERS = "members";
    public static final String NODE_EMAIL = "email";
    public static final String NODE_USERNAME = "username";

    public static final String NODE_USERS = "users";
    public static final String NODE_GAMES = "games";
    public static final String NODE_LEAGUE = "league";

    public static final String NODE_RANKINGS = "rankings";
    public static final String NODE_ELO = "elo";
    public static final String NODE_PREV = "prev_elo";

    public static final String SIGNIN_FRAGMENT = "SIGNIN_FRAGMENT";
    public static final String CREATE_LEAGUE_FRAGMENT = "CREATE_LEAGUE_FRAGMENT";
    public static final String HOME_FRAGMENT = "HOME_FRAGMENT";
    public static final String PROFILE_FRAGMENT = "PROFILE_FRAGMENT";
    public static final String SETTINGS_FRAGMENT = "SETTINGS_FRAGMENT";

    public static final int BASE_RATING = 1200;

    public static final String DOMAIN_NAME = "mail.danielw.ca";
    public static final String API_KEY = "key-181e8242f9bfaf5d0726ce4770e37963";
    public static final String FROM_DOMAIN = "daniel@" + DOMAIN_NAME;

    private Constants(){
        throw new AssertionError();
    }
}
