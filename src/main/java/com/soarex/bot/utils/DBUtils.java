package com.soarex.bot.utils;

import com.soarex.bot.SoarexBot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by shumaf on 08.05.16.
 */
public class DBUtils {

    public static Connection connect(String driver, String url, String user, String password) {
        try {
            Class.forName(driver);
            SoarexBot.LOGGER.info("Using {} data base driver", driver);
            SoarexBot.LOGGER.info("Successfully loaded DataBase driver");

            SoarexBot.LOGGER.info("Successfully connected to database in MAIN THREAD \n URL : {} \n USER : {} \n PASSWORD : {}", SoarexBot.DB_URL, SoarexBot.DB_USER, SoarexBot.DB_PASSWORD);
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            SoarexBot.LOGGER.error("Unable to connect to database : ", e);
        }
        return null;
    }

    public static Connection connect() {
        try {
            Class.forName(SoarexBot.DB_DRIVER);
            SoarexBot.LOGGER.info("Using {} data base driver", SoarexBot.DB_DRIVER);
            SoarexBot.LOGGER.info("Successfully loaded DataBase driver");
            SoarexBot.LOGGER.info("Successfully connected to database \n URL : {} \n USER : {} \n PASSWORD : {}", SoarexBot.DB_URL, SoarexBot.DB_USER, SoarexBot.DB_PASSWORD);
            return DriverManager.getConnection(SoarexBot.DB_URL, SoarexBot.DB_USER, SoarexBot.DB_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            SoarexBot.LOGGER.error("Unable to connect to DataBase : ", e);
        }
        return null;
    }
}
