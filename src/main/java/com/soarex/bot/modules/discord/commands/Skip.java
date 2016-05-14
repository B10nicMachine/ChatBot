package com.soarex.bot.modules.discord.commands;

import com.soarex.bot.api.ICommand;
import com.soarex.bot.modules.discord.Discord;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;

/**
 * Created by shumaf on 14.05.16.
 */
public class Skip implements ICommand {
    @Override
    public String description() {
        return "пропускает текущую аудиозапись";
    }

    @Override
    public String usage() {
        return "!пропустить";
    }

    @Override
    public Permissions permission() {
        return Permissions.SEND_MESSAGES;
    }

    @Override
    public void execute(String... args) {
        IVoiceChannel channel = Discord.discordClient.getVoiceChannelByID(Discord.musicChannel);
        if (channel.isConnected()) {
            try {
                channel.getAudioChannel().skip();
            } catch (DiscordException e) {
                e.printStackTrace();
            }
        }
    }
}
