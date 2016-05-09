package com.soarex.bot.modules.twitch;

import com.deswaef.twitch.api.streams.domain.StreamCheck;
import com.deswaef.twitch.configuration.Twitch;
import com.soarex.bot.SoarexBot;
import com.soarex.bot.modules.discord.Discord;
import com.soarex.bot.utils.DBUtils;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by shumaf on 09.05.16.
 */
public class StreamNotifier {

    private static InputStream stream = StreamNotifier.class.getClassLoader().getResourceAsStream("SoarexBot.properties");
    private static Properties properties = new Properties();
    public static String baseUrl, clientId, clientSecret, redirectUrl, discordNotifyChannel = null;

    public StreamNotifier() {
        try {
            properties.load(stream);
            stream.close();
            baseUrl = properties.getProperty("twitch.url");
            clientId = properties.getProperty("twitch.client");
            clientSecret = properties.getProperty("twitch.secret");
            redirectUrl = properties.getProperty("twitch.redirect");
            discordNotifyChannel = properties.getProperty("twitch.discordNotifyChannel");
        } catch (IOException e) {
            SoarexBot.LOGGER.error("Twitch Module Internal Exception", e);
        }
        Twitch instance = Twitch.newTwitchInstance(baseUrl, clientId, clientSecret, redirectUrl);
        Connection connection = DBUtils.connect();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM streamers");
            while (resultSet.next()) {
                String nick = resultSet.getString("nick");
                String link = resultSet.getString("link");
                String streamId = resultSet.getString("streamId");
                Optional<StreamCheck> check = instance.streams().stream(link);
                if(!check.get().getStream().getId().equals(streamId)) {
                    RequestBuffer.request(() -> {
                        MessageBuilder builder = new MessageBuilder(Discord.discordClient)
                                .withChannel(discordNotifyChannel)
                                .withContent("Прямо сейчас " + nick + " начинает свое вещание. Играем в " + check.get().getStream().getGame() + ". Смотреть здесь : http://www.twitch.tv/" + link);
                        return null;
                    });
                    statement.executeUpdate("UPDATE streamers SET streamId=" + streamId + " WHERE nick=" + nick);
                }
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
