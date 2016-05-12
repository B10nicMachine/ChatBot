package com.soarex.bot;

import com.soarex.bot.modules.discord.Discord;
import com.soarex.bot.modules.ModuleLoader;
import com.soarex.bot.modules.twitch.Twitch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by shumaf on 03.05.16.
 */
public class SoarexBot {

    /**
     * The name of the project
     */
    public static final String NAME;
    /**
     * The version of the api
     */
    public static final String VERSION;
    /**
     * It's me :D
     */
    public static final String AUTHOR;
    /**
     * The api's description
     */
    public static final String DESCRIPTION;

    public static final String DB_URL;
    public static final String DB_USER;
    public static final String DB_PASSWORD;
    public static final String DB_DRIVER;

    /**
     * SLF4J Instance
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(SoarexBot.class);

    public static Properties properties = new Properties();

    static {
        InputStream stream = SoarexBot.class.getClassLoader().getResourceAsStream("SoarexBot.properties");
        try {
            properties.load(stream);
            stream.close();
        } catch (IOException e) {
            SoarexBot.LOGGER.error("SoarexBot Internal Exception", e);
        }
        NAME = properties.getProperty("application.name");
        VERSION = properties.getProperty("application.version");
        AUTHOR = properties.getProperty("application.author");
        DESCRIPTION = properties.getProperty("application.description");

        DB_DRIVER = properties.getProperty("db.class");
        DB_URL = properties.getProperty("db.url");
        DB_USER = properties.getProperty("db.user");
        DB_PASSWORD = properties.getProperty("db.password");

        LOGGER.info("{} v{} by {}", NAME, VERSION, AUTHOR);
        LOGGER.info("{}", DESCRIPTION);
    }

    public static void main(String[] args) {
        loadNativeModules();
    }

    private static void loadNativeModules() {
        ModuleLoader.loadModule(Discord.class);
        //ModuleLoader.loadModule(Twitch.class);
    }

    public static void shutdown() {
        LOGGER.info("Shutting down Soarex::BOT");
        System.exit(0);
    }
}
