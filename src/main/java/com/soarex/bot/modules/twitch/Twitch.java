package com.soarex.bot.modules.twitch;

import com.soarex.bot.api.IModule;
import com.soarex.bot.utils.DBUtils;

import java.sql.Connection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by shumaf on 04.05.16.
 */
public class Twitch implements IModule {

    public static Connection connection;
    public static StreamNotifier notifier;

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
        connection = DBUtils.connect();
        notifier = new StreamNotifier();

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay((Runnable) () -> Twitch.notifier.check(), 2, 30, TimeUnit.SECONDS);
    }
}
