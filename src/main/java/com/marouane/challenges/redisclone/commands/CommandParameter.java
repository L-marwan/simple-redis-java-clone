package com.marouane.challenges.redisclone.commands;

public class CommandParameter {

    private final Object value;

    public CommandParameter(Object value) {
        this.value = value;
    }

    public <T> T getValue(Class<T> classz) throws CommandException {
        if (value.getClass().isAssignableFrom(classz)) {
            return (T) value;
        }
        throw new CommandException("Parameter is not of type: " + classz);
    }
}
