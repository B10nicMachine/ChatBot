package com.soarex.bot.api;

/**
 * Created by shumaf on 03.05.16.
 */
public interface IModule extends Runnable {

    String getName();
    String getAuthor();
    String getVersion();
}
