package com.soarex.bot.modules.discord;

import com.soarex.bot.SoarexBot;
import com.soarex.bot.api.IModule;
import com.soarex.bot.utils.DBUtils;
import com.soarex.bot.utils.ObsceneFilter;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.EventDispatcher;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.function.BooleanSupplier;

import static com.soarex.bot.modules.twitch.Twitch.connection;

/**
 * Created by shumaf on 07.05.16.
 */
public class Discord implements IModule {

    public static IDiscordClient discordClient;
    public static Connection connection;

    public static int maxObscene;
    public static boolean obsceneDelete;
    public static boolean obsceneBan;

    static {
        InputStream stream = Discord.class.getClassLoader().getResourceAsStream("SoarexBot.properties");
        Properties properties = new Properties();
        String token = null;
        try {
            properties.load(stream);
            stream.close();
            token = properties.getProperty("discord.token");
            maxObscene = Integer.parseInt(properties.getProperty("discord.maxObscene"));
            obsceneDelete = Boolean.parseBoolean(properties.getProperty("discord.obsceneDelete"));
            obsceneBan = Boolean.parseBoolean(properties.getProperty("discord.obsceneBan"));
            discordClient = new ClientBuilder().withToken(token).login();
        } catch (IOException | DiscordException e) {
            SoarexBot.LOGGER.error("Discord Module Internal Exception", e);
        }
        connection = DBUtils.connect();
    }

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
        EventDispatcher dispatcher = discordClient.getDispatcher();
        dispatcher.registerListener(new ObsceneListener());
    }
}
