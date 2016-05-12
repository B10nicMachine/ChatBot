package com.soarex.bot.modules.twitch;

import com.soarex.bot.SoarexBot;
import com.soarex.bot.modules.discord.Discord;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import sx.blah.discord.api.EventSubscriber;
import sx.blah.discord.handle.impl.events.GuildCreateEvent;

/**
 * Created by shumaf on 04.05.16.
 */
public class TwitchJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        StreamNotifier notifier = new StreamNotifier();
    }

    @EventSubscriber
    public void onFix(GuildCreateEvent event) {
        Discord.guild=event.getGuild();
    }
}
