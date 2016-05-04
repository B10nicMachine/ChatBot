package com.soarex.bot;

import com.soarex.bot.modules.ModuleLoader;
import com.soarex.bot.modules.Twitch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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

    /**
     * SLF4J Instance
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(SoarexBot.class);

    static {
        InputStream stream = SoarexBot.class.getClassLoader().getResourceAsStream("SoarexBot.properties");
        Properties properties = new Properties();
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

        LOGGER.info("{} v{} by {}", NAME, VERSION, AUTHOR);
        LOGGER.info("{}", DESCRIPTION);
    }

    public static void main(String[] args) {
        /*
        try {
            Class.forName(properties.getProperty("jdbcClass"));
            SoarexBot.LOGGER.info("Succesfully loaded DataBase driver");

            Connection connection = DriverManager.getConnection(properties.getProperty("dbUrl"), properties.getProperty("dbUser"), properties.getProperty("dbPassword"));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        */
        loadNativeModules();
    }

    private static void loadNativeModules() {
        ModuleLoader.loadModule(Twitch.class);
    }
}
