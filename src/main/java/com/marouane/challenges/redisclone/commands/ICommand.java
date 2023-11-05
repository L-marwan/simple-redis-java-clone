package com.marouane.challenges.redisclone.commands;

import java.util.List;

public interface ICommand<R> {

    public R execute(List<CommandParameter> params) throws CommandException;

}
