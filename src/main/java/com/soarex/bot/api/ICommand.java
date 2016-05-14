package com.soarex.bot.api;

import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.Permissions;

/**
 * Created by shumaf on 14.05.16.
 */
public interface ICommand {

    /**
     * Returns descriprion, which displays in help command
     *
     * @return String with description
     */
    String description();

    /**
     * Returns usage, which displays if command args incorrect
     * @return String with usage
     */
    String usage ();

    /**
     * Returns Permission, which need for execute command
     * @return Role, which need for execute command
     */
    Permissions permission();

    /**
     * Main method, which executes in command request
     * @param args
     */
    void execute(String... args);
}
