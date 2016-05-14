package com.soarex.bot.modules.discord.commands;

import com.soarex.bot.api.ICommand;
import com.soarex.bot.modules.discord.Discord;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;

/**
 * Created by shumaf on 14.05.16.
 */
public class Pause implements ICommand {
    @Override
    public String description() {
        return "ставит текущую аудиозапись на паузу";
    }

    @Override
    public String usage() {
        return "!пауза";
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
                channel.getAudioChannel().pause();
            } catch (DiscordException e) {
                e.printStackTrace();
            }
        }
    }
}
