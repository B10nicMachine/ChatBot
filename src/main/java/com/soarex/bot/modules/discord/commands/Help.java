package com.soarex.bot.modules.discord.commands;

import com.soarex.bot.api.ICommand;
import com.soarex.bot.CommandDispatcher;
import com.soarex.bot.modules.discord.MsgListener;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.MissingPermissionsException;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by shumaf on 14.05.16.
 */
public class Help implements ICommand {
    @Override
    public String description() {
        return "отображает информацию о командах";
    }

    @Override
    public String usage() {
        return "!help или !help [command]";
    }

    @Override
    public Permissions permission() {
        return Permissions.SEND_MESSAGES;
    }

    @Override
    public void execute(String... args) {
        if (args.length == 0) {
            StringBuilder answer = new StringBuilder("Доступные команды : ");
            Iterator itr = CommandDispatcher.commands.entrySet().iterator();
            while(itr.hasNext()) {
                Map.Entry entry = (Map.Entry) itr.next();
                ICommand value = (ICommand) entry.getValue();
                answer.append("\n**" + value.usage() + "** - " + value.description());
            }
            try {
                MsgListener.msgChannel.sendMessage(answer.toString());
            } catch (MissingPermissionsException | HTTP429Exception | DiscordException e) {
                e.printStackTrace();
            }
        } else if (args.length == 1){
            ICommand value = (ICommand) CommandDispatcher.commands.get(args[0]);
            if (value != null) {
                try {
                    MsgListener.msgChannel.sendMessage("Использование : " + value.usage());
                } catch (MissingPermissionsException | HTTP429Exception | DiscordException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    MsgListener.msgChannel.sendMessage("Такой командый не существует");
                } catch (MissingPermissionsException | HTTP429Exception | DiscordException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
