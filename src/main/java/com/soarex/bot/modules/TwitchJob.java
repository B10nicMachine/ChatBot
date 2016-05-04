package com.soarex.bot.modules;

import com.soarex.bot.SoarexBot;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by shumaf on 04.05.16.
 */
public class TwitchJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SoarexBot.LOGGER.warn("WOOOOOOOOOOOOORK!!!");
    }
}
