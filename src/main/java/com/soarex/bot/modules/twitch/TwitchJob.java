package com.soarex.bot.modules.twitch;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by shumaf on 04.05.16.
 */
public class TwitchJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Twitch.notifier.check();
    }

}
