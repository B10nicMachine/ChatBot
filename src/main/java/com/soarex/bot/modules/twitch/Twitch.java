package com.soarex.bot.modules.twitch;

import com.soarex.bot.api.IModule;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Created by shumaf on 04.05.16.
 */
public class Twitch implements IModule {
    @Override
    public String getName() {
        return "Twitch";
    }

    @Override
    public String getAuthor() {
        return "Soarex";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public void run() {
        try {
            JobDetail jobDetail = JobBuilder.newJob(TwitchJob.class).withIdentity("twitch").build();
            Trigger trigger = TriggerBuilder.newTrigger().startNow().withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(30).repeatForever()).build();
            StdSchedulerFactory.getDefaultScheduler().scheduleJob(jobDetail, trigger);
            StdSchedulerFactory.getDefaultScheduler().start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
