package com.marouane.clone.redis.commands;

import java.util.List;

public interface ICommand<R> {

    public R execute(List<CommandParameter> params) throws CommandException;

}
