package com.marouane.clone.redis.commands;

import com.marouane.clone.redis.CacheManager;

import java.util.List;

@Command(name = "SET", description = "Set key to hold the value, if it already exists, it will be overwritten")
public class SetCommand implements ICommand<String> {

    @Override
    public String execute(List<CommandParameter> params) throws CommandException {
        if (params.size() < 2)
            throw new CommandException("Set Command needs at least 2 arguments, found %d arguments.".formatted(params.size()));
        String key;
        String value;

        try {
            key = params.get(0).getValue(String.class);
            value = params.get(1).getValue(String.class);
        } catch (CommandException e) {
            throw new CommandException("Set command only supports keys and values of type String.");
        }

        CacheManager.getInstance().put(key, value);

        return "OK";
    }
}
