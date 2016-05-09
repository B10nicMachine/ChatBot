package com.soarex.bot.modules.discord;

import com.soarex.bot.SoarexBot;
import com.soarex.bot.api.IModule;
import com.soarex.bot.utils.DBUtils;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.EventDispatcher;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * Created by shumaf on 07.05.16.
 */
public class Discord implements IModule {

    public static IDiscordClient discordClient;

    @Override
    public String getName() {
        return "Discord";
    }

    @Override
    public String getAuthor() {
        return "Soarex";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public void run() {
        InputStream stream = Discord.class.getClassLoader().getResourceAsStream("SoarexBot.properties");
        Properties properties = new Properties();
        String token = null;
        try {
            properties.load(stream);
            stream.close();
            token = properties.getProperty("discord.token");
            discordClient = new ClientBuilder().withToken(token).login();
        } catch (IOException | DiscordException e) {
            SoarexBot.LOGGER.error("Discord Module Internal Exception", e);
        }
        EventDispatcher dispatcher = discordClient.getDispatcher();
        dispatcher.registerListener(new DiscordListener());
        //Connection connection = DBUtils.connect();
    }
}
