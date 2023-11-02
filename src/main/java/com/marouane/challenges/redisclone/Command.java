package com.marouane.challenges.redisclone;

public interface Command<R> {

    public R execute(Object... args);

}
