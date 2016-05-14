package com.soarex.bot.modules.discord.commands;

import com.soarex.bot.api.ICommand;
import com.soarex.bot.modules.discord.Discord;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;

/**
 * Created by shumaf on 14.05.16.
 */
public class Vollume implements ICommand {
    @Override
    public String description() {
        return "регулирование громкости аудиовещания бота";
    }

    @Override
    public String usage() {
        return "!громкость [значение от 1 до 100]";
    }

    @Override
    public Permissions permission() {
        return Permissions.SEND_MESSAGES;
    }

    @Override
    public void execute(String... args) {
        float vollume = Float.parseFloat(args[0]);
        if (vollume > 0 && vollume < 100) {
            IVoiceChannel channel = Discord.discordClient.getVoiceChannelByID(Discord.musicChannel);
            if (channel.isConnected()) {
                try {
                    channel.getAudioChannel().setVolume(vollume);
                } catch (DiscordException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
