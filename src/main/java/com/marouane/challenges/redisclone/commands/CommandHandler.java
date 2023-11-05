package com.marouane.challenges.redisclone.commands;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler {


    public static Object handle(Object o) throws CommandException {
        String commandName = getCommandName(o);

        ICommand<?> command = CommandsRegistry.getInstance().getCommand(commandName);
        var params = new ArrayList<CommandParameter>();
        if (o instanceof List<?> l) {
            l.subList(1, l.size()).forEach(i -> params.add(new CommandParameter(i)));
        }
        return command.execute(params);
    }

    private static String getCommandName(Object o) throws CommandException {
        String commandName;
        if (o instanceof String s) {
            commandName = s;
        } else if (o instanceof List<?> l) {
            if (l.isEmpty()) {
                throw new CommandException("Empty message, can't find command name");
            }
            commandName = (String) l.get(0);
        } else {
            throw new CommandException("unknown type");
        }

        if (StringUtils.isBlank(commandName)) {
            throw new CommandException("No command name was provided.");
        }
        return commandName;
    }
}
