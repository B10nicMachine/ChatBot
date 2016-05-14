package com.soarex.bot.modules.discord.commands;

import com.soarex.bot.api.ICommand;
import com.soarex.bot.modules.discord.Discord;
import com.soarex.bot.modules.discord.MsgListener;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.MissingPermissionsException;

/**
 * Created by shumaf on 14.05.16.
 */
public class Debug implements ICommand {
    @Override
    public String description() {
        return "выводит отладочную информацию";
    }

    @Override
    public String usage() {
        return "!debug";
    }

    @Override
    public Permissions permission() {
        return Permissions.BAN;
    }

    @Override
    public void execute(String... args) {
        StringBuilder msg = new StringBuilder("\n__**BOT ID:**__ ");
        msg.append(Discord.discordClient.getOurUser().getID());
        msg.append("\n\n__**Guilds: **__");
        for (IGuild guild : Discord.discordClient.getGuilds()) {
            msg.append("\n" + guild.getName() + "   " + guild.getID());
        }
        msg.append("\n__**Text channels: **__");
        for (IChannel channel : Discord.discordClient.getChannels(false)) {
            msg.append("\n" + channel.getGuild().getName() + "   " + channel.getName() + "   " + channel.getID());
        }
        msg.append("\n__**Voice channels: **__");
        for (IVoiceChannel voiceChannel : Discord.discordClient.getVoiceChannels()) {
            msg.append("\n" + voiceChannel.getGuild().getName() + "   " + voiceChannel.getName() + "   " + voiceChannel.getID());
        }
        try {
            MsgListener.msgChannel.sendMessage(msg.toString());
        } catch (MissingPermissionsException | HTTP429Exception | DiscordException e) {
            e.printStackTrace();
        }
    }
}
