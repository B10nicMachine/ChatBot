package com.soarex.bot.utils;

import com.soarex.bot.SoarexBot;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by shumaf on 10.05.16.
 */
public class Strings {

    private static Properties properties = new Properties();

    static {
        InputStream stream = Strings.class.getClassLoader().getResourceAsStream("Strings.properties");
        try {
            properties.load(stream);
            stream.close();
        } catch (IOException e) {
            SoarexBot.LOGGER.error("Can't get \"Strings.properties\" file", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
