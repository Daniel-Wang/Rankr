package ca.danielw.rankr.utils;

public final class Constants {

    public static final String PACKAGE_NAME = "ca.danielw.rankr";

    public static final String LEAGUE_NAME = "LEAGUE_NAME";
    public static final String EMAIL = "EMAIL";

    public static final String NODE_LEAGUES = "leagues";
    public static final String NODE_MEMBERS = "members";
    public static final String NODE_EMAIL = "email";
    public static final String NODE_USERNAME = "username";

    public static final String NODE_USERS = "users";
    public static final String NODE_GAMES = "games";
    public static final String NODE_LEAGUE = "league";

    public static final String RANK = "rank";
    public static final String NODE_RANKINGS = "rankings";
    public static final String NODE_ELO = "elo";
    public static final String NODE_PREV = "prevRank";

    public static final String SIGNUP_FRAGMENT = "SIGNUP_FRAGMENT";
    public static final String SIGNIN_FRAGMENT = "SIGNIN_FRAGMENT";
    public static final String CREATE_LEAGUE_FRAGMENT = "CREATE_LEAGUE_FRAGMENT";
    public static final String HOME_FRAGMENT = "HOME_FRAGMENT";
    public static final String PROFILE_FRAGMENT = "PROFILE_FRAGMENT";
    public static final String SETTINGS_FRAGMENT = "SETTINGS_FRAGMENT";

    public static final String SOURCE_LOCATION = "SOURCE_LOCATION";

    public static final String SIGN_IN_INTENT = "SIGN_IN_INTENT";

    public static final int BASE_RATING = 1200;

    public static final String DOMAIN_NAME = "mail.danielw.ca";
    public static final String FROM_DOMAIN = "daniel@" + DOMAIN_NAME;

    public static final String ME_USER = "ME_USER";
    public static final String K_FACTOR = "kFactor";

    public static final String LOSES = "loses";
    public static final String WINS = "wins";

    public static final int KFACTOR = 32;
    public static final int MASTER_KFACTOR = 16;
    public static final int MASTER_ELO = 2200;

    public static final int ENTER_GAME_RESULT = 1000;
    public static final int RESULT_OK = 200;

    public static final String CURRENT_GAME = "CURRENT_GAME";
    public static final String FRAGMENT_RANKING = "FRAGMENT_RANKING";

    public static final String SEND_EMAIL_ENDPOINT = "https://us-central1-rankr-2d74f.cloudfunctions.net/send_emails";

    public static final String EMAILS = "emails";

    private Constants(){
        throw new AssertionError();
    }
}
