package com.soarex.bot.modules.discord;

import com.soarex.bot.SoarexBot;
import com.soarex.bot.api.IModule;
import com.soarex.bot.CommandDispatcher;
import com.soarex.bot.modules.discord.commands.*;
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
    public static Connection connection;

    public static int maxObscene;
    public static boolean obsceneDelete;
    public static boolean obsceneBan;

    public static String musicChannel;

    static {
        InputStream stream = Discord.class.getClassLoader().getResourceAsStream("SoarexBot.properties");
        Properties properties = new Properties();
        String token;
        try {
            properties.load(stream);
            stream.close();
            token = properties.getProperty("discord.token");
            maxObscene = Integer.parseInt(properties.getProperty("discord.maxObscene"));
            obsceneDelete = Boolean.parseBoolean(properties.getProperty("discord.obsceneDelete"));
            obsceneBan = Boolean.parseBoolean(properties.getProperty("discord.obsceneBan"));
            discordClient = new ClientBuilder().withToken(token).login();

            musicChannel = properties.getProperty("discord.music");
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
        dispatcher.registerListener(new MsgListener());

        registerCommands();
    }

    private void registerCommands() {
        CommandDispatcher.registerCmd("!help", new Help());
        CommandDispatcher.registerCmd("!debug", new Debug());
        CommandDispatcher.registerCmd("!музыку", new VKMusic());
        CommandDispatcher.registerCmd("!громкость", new Vollume());
        CommandDispatcher.registerCmd("!пауза", new Pause());
        CommandDispatcher.registerCmd("!пропустить", new Skip());
        CommandDispatcher.registerCmd("!продолжить", new Resume());
    }
}
