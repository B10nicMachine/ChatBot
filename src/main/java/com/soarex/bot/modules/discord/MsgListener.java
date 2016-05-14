package com.soarex.bot.modules.discord;

import com.soarex.bot.SoarexBot;
import com.soarex.bot.CommandDispatcher;
import com.soarex.bot.utils.ObsceneFilter;
import sx.blah.discord.api.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.MissingPermissionsException;

import static com.soarex.bot.modules.discord.Discord.*;

import java.sql.*;

/**
 * Created by shumaf on 08.05.16.
 */
public class MsgListener {

    public static IChannel msgChannel;
    public static IMessage msg;

    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) {
        if (event.getMessage().getAuthor().getID().equals(discordClient.getOurUser().getID())) return;
        msg = event.getMessage();
        msgChannel = event.getMessage().getChannel();
        String message = msg.getContent();
        for (Object cmd : CommandDispatcher.commands.keySet()) {
            String command = (String) cmd;
            if (message.startsWith(command)) {
                String argsStr = message.replaceFirst(command, "");
                if (!argsStr.isEmpty()) CommandDispatcher.executeCmd(command, argsStr.substring(1, argsStr.length()).split(" "));
                else CommandDispatcher.executeCmd(command, new String[0]);
                break;
            }
        }
        SoarexBot.LOGGER.info("[MSG|{}::{}|{}|{}|{}]  {}", msg.getGuild().getName(), msg.getGuild().getID(), msgChannel, msg.getAuthor(), msg.getTimestamp(), msg.getContent());
        if (!ObsceneFilter.isAllowed(message)) {
            try {
                String author = event.getMessage().getAuthor().getID();

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
                    msgChannel.sendMessage(event.getMessage().getAuthor().mention() + ", не ругайся! В твоем сообщении " + obscene + " мат(ов)");
                    update.setInt(1, buffObscene);
                    update.setString(2, author);
                    update.executeUpdate();
                    connection.commit();
                } else {
                    if (obsceneBan) {
                        msgChannel.sendMessage("Прощай на веки, мой матешиный друг! Ты ругнулся " + buffObscene + " раз(а)");
                        event.getMessage().getGuild().banUser(discordClient.getUserByID(author));
                    } else {
                        msgChannel.sendMessage(event.getMessage().getAuthor().mention() + ", не ругайся! В твоем сообщении " + obscene + " мат(ов)");
                    }
                    update.setInt(1, buffObscene);
                    update.setString(2, author);
                    update.executeUpdate();
                    connection.commit();
                }
                if (obsceneDelete) event.getMessage().delete();
            } catch (MissingPermissionsException e) {
                SoarexBot.LOGGER.error("Insufficient permissions : ", e);
            } catch (HTTP429Exception e) {
                SoarexBot.LOGGER.error("Can't send message, too many requests : ", e);
            } catch (DiscordException | SQLException e) {
                SoarexBot.LOGGER.error("Discord module internal exception : ", e);
            }
        }
    }
}