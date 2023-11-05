package com.marouane.clone.redis.commands;

import com.marouane.clone.redis.CacheManager;

import java.util.List;

@Command(name = "GET", description = "Get the value of key, if it doesn't exist, null is returned")
public class GetCommand implements ICommand<String> {


    @Override
    public String execute(List<CommandParameter> params) throws CommandException {
        if (params.isEmpty())
            throw new CommandException("Set Command needs at least 1 arguments, found 0 arguments.");
        String key;

        try {
            key = params.get(0).getValue(String.class);
        } catch (CommandException e) {
            throw new CommandException("Set command only supports keys and values of type String.");
        }
        var manager = CacheManager.getInstance();
        return manager.get(key);
    }
}
