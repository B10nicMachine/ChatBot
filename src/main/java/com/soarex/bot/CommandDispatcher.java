package com.soarex.bot;

import com.soarex.bot.api.ICommand;
import com.soarex.bot.modules.discord.MsgListener;
import sx.blah.discord.handle.obj.IRole;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shumaf on 14.05.16.
 */
public class CommandDispatcher {

    public static Map commands = new HashMap<String, ICommand>();

    public static void registerCmd(String cmd, ICommand obj) {
        if (!cmd.isEmpty() && obj != null)
            commands.put(cmd, obj);
    }

    public static void executeCmd(String cmd, String ...args) {
        if (!cmd.isEmpty()) {
            ICommand obj = (ICommand) commands.get(cmd);
            for (IRole role : MsgListener.msg.getAuthor().getRolesForGuild(MsgListener.msgChannel.getGuild()))
                role.getPermissions().stream().filter(permission -> obj.permission() == permission).forEach(permission -> obj.execute(args));
        }
    }
}
