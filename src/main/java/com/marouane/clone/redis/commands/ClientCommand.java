package com.marouane.clone.redis.commands;

import java.util.List;

@Command(name = "CLIENT")
public class ClientCommand implements ICommand<String> {
    @Override
    public String execute(List<CommandParameter> params) throws CommandException {
        return "OK";
    }
}
