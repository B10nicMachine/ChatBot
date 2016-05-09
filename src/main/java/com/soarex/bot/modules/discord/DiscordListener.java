package com.soarex.bot.modules.discord;

import com.soarex.bot.SoarexBot;
import com.soarex.bot.utils.ObsceneFilter;
import sx.blah.discord.api.EventSubscriber;
import sx.blah.discord.handle.impl.events.MentionEvent;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.MissingPermissionsException;

/**
 * Created by shumaf on 08.05.16.
 */
public class DiscordListener {

    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) {
        if(event.getMessage().getAuthor().getID().equals(Discord.discordClient.getOurUser().getID())) return;
        String msg = event.getMessage().getContent();
        if (!ObsceneFilter.isAllowed(msg))
            try {
                event.getMessage().getChannel().sendMessage(event.getMessage().getAuthor().mention() + " ,не ругайся! В твоем сообщении " + ObsceneFilter.invective.size() + " мат(ов)");
            } catch (MissingPermissionsException e) {
                SoarexBot.LOGGER.warn("Insufficient permissions to edit messages : ", e);
            } catch (HTTP429Exception e) {
                e.printStackTrace();
            } catch (DiscordException e) {
                e.printStackTrace();
        }
    }
}
