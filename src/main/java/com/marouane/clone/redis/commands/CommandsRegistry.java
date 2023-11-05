package com.marouane.clone.redis.commands;

import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class CommandsRegistry {
    private static final Logger logger = Logger.getLogger(CommandsRegistry.class.getName());
    private final Map<String, Class<? extends ICommand<?>>> commands = new HashMap<>();

    private static volatile CommandsRegistry instance;

    @SuppressWarnings("unchecked")
    private CommandsRegistry() {
        Reflections reflections = new Reflections("com.marouane.challenges.redisclone.commands");
        Set<Class<?>> annotatedCommandClasses = reflections.getTypesAnnotatedWith(Command.class);

        annotatedCommandClasses.forEach(c -> {
            Command a = c.getAnnotation(Command.class);
            if (ICommand.class.isAssignableFrom(c)) {
                commands.put(a.name(), (Class<? extends ICommand<?>>) c);
            } else {
                logger.warning("class ignored as it doesn't implement ICommand: %s".formatted(c.getName()));
            }
        });
    }

    public Map<String, Class<? extends ICommand<?>>> getCommands() {
        return commands;
    }

    public static CommandsRegistry getInstance() {
        if (instance == null) {
            synchronized (CommandsRegistry.class) {
                if (instance == null) {
                    instance = new CommandsRegistry();
                }
            }
        }
        return instance;
    }

    /**
     * given the name return an instance of the command
     *
     * @param name then name of the command
     * @return an instance of the command
     * @throws CommandException if the command is not found, or if there's any errors creating an instance of the command
     */
    public ICommand<?> getCommand(String name) throws CommandException {
        Class<? extends ICommand<?>> commandClass = commands.get(name);
        if (commandClass == null) {
            throw new CommandException("command with name '%s' not found.".formatted(name));
        }

        try {
            return commandClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new CommandException("Unable to instantiate command, error: %s".formatted(e.getMessage()));
        }
    }


}
