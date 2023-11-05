package com.marouane.challenges.redisclone.commands;


import java.util.List;
import java.util.stream.Collectors;

@Command(name = "PING", description = "Returns pong with the rest of the args ")
public class PingCommand implements ICommand<String> {

    @Override
    public String execute(List<CommandParameter> params) throws CommandException {
        return "PONG " + params.stream().map(p -> {
            try {
                return p.getValue(String.class);
            } catch (CommandException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.joining(" "));
    }
}
