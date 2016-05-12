package com.soarex.bot.modules.discord;

import com.soarex.bot.SoarexBot;
import com.soarex.bot.utils.ObsceneFilter;
import sx.blah.discord.api.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.MissingPermissionsException;

import static com.soarex.bot.modules.discord.Discord.*;

import java.sql.*;

/**
 * Created by shumaf on 08.05.16.
 */
public class ObsceneListener {

    static boolean t = true;

    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) {
        if(event.getMessage().getAuthor().getID().equals(discordClient.getOurUser().getID())) return;
        String msg = event.getMessage().getContent();
        if (!ObsceneFilter.isAllowed(msg))
            try {
                String author = event.getMessage().getAuthor().getID();

                Statement statement = connection.createStatement();
                PreparedStatement select = connection.prepareStatement("SELECT obscene FROM discord WHERE id=?");
                PreparedStatement update = connection.prepareStatement("UPDATE discord SET obscene=? WHERE id=?");
                select.setString(1, author);
                select.execute();
                ResultSet resultSet = select.getResultSet();

                int karma = -1;
                if (resultSet.next()) karma = resultSet.getInt("obscene");
                int obscene = ObsceneFilter.invective.size();
                int buffObscene = karma + obscene;
                if (buffObscene < maxObscene) {
                    event.getMessage().getChannel().sendMessage(event.getMessage().getAuthor().mention() + ", не ругайся! В твоем сообщении " + obscene + " мат(ов)");
                    update.setInt(1, buffObscene);
                    update.setString(2, author);
                    update.executeUpdate();
                    connection.commit();
                } else {
                    if (obsceneBan) {
                        event.getMessage().getChannel().sendMessage("Прощай на веки, мой матешиный друг! Ты ругнулся " + buffObscene + " раз(а)");
                        event.getMessage().getGuild().banUser(discordClient.getUserByID(author));
                    } else {
                        event.getMessage().getChannel().sendMessage(event.getMessage().getAuthor().mention() + ", не ругайся! В твоем сообщении " + obscene + " мат(ов)");
                    }
                    update.setInt(1, buffObscene);
                    update.setString(2, author);
                    update.executeUpdate();
                    connection.commit();
                }
                if (obsceneDelete) event.getMessage().delete();
            } catch (MissingPermissionsException e) {
                SoarexBot.LOGGER.error("Insufficient permissions to edit messages : ", e);
            } catch (HTTP429Exception e) {
                SoarexBot.LOGGER.error("Can't send message, too many requests : ", e);
            } catch (DiscordException | SQLException e) {
                SoarexBot.LOGGER.error("Discord module internal exception : ", e);
            }
    }
}