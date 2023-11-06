package com.marouane.clone.redis.commands;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CommandHandler {

    private static Logger logger = Logger.getLogger(CommandHandler.class.getName());

    public static Object handle(Object o) throws CommandException {
        String commandName = getCommandName(o);

        ICommand<?> command = CommandsRegistry.getInstance().getCommand(commandName);
        logger.info("executing command : " + commandName);
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
                logger.severe("Empty message, can't find command name");
                throw new CommandException("Empty message, can't find command name");
            }
            commandName = (String) l.get(0);
        } else {
            logger.severe("unknown type");
            throw new CommandException("unknown type");
        }

        if (StringUtils.isBlank(commandName)) {
            logger.severe("No command name was provided.");
            throw new CommandException("No command name was provided.");
        }
        return commandName;
    }
}
