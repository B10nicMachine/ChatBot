package com.soarex.bot.modules.twitch;

import static com.soarex.bot.modules.twitch.Twitch.*;

import com.deswaef.twitch.api.streams.domain.StreamCheck;
import com.deswaef.twitch.configuration.Twitch;
import com.soarex.bot.SoarexBot;
import com.soarex.bot.modules.discord.Discord;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by shumaf on 09.05.16.
 */
public class StreamNotifier {

    private static InputStream stream;
    private static Properties properties = new Properties();
    public static String baseUrl, clientId, clientSecret, redirectUrl, discordNotifyChannel = null;

    public StreamNotifier() {
        try {
            stream = StreamNotifier.class.getClassLoader().getResourceAsStream("SoarexBot.properties");
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

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM streamers");
            String query = "UPDATE streamers SET streamId=? WHERE nick=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            MessageBuilder builder = new MessageBuilder(Discord.discordClient).withChannel(discordNotifyChannel);
            while (resultSet.next()) {
                String nick = resultSet.getString("nick");
                String link = resultSet.getString("link");
                Long streamId = resultSet.getLong("streamId");
                Optional<StreamCheck> check = instance.streams().stream(link);
                if (check.get().getStream() != null && !check.get().getStream().getId().equals(streamId)) {
                    RequestBuffer.request(() -> {
                        try {
                            builder.withContent("Прямо сейчас " + nick + " начинает свое вещание. Играем в " + check.get().getStream().getGame() + ". Смотреть здесь : http://www.twitch.tv/" + link).build();
                        } catch (DiscordException | MissingPermissionsException e) {
                            SoarexBot.LOGGER.warn("Unable to send notification about broadcast : ", e);
                        }
                        return null;
                    });
                    streamId = check.get().getStream().getId();
                    preparedStatement.setLong(1, streamId);
                    preparedStatement.setString(2, nick);
                    preparedStatement.executeUpdate();
                    connection.commit();
                }
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
