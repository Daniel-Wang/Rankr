package ca.danielw.rankr.utils;

import net.sargue.mailgun.Configuration;

public final class MailGunConfig {
    private static Configuration instance;

    private MailGunConfig(String domain, String apiKey, String from){
        instance = new Configuration()
                .domain(domain).apiKey(apiKey).from("Team Rankr", from);
    }

    public static Configuration getInstance(){
        return instance;
    }
}
