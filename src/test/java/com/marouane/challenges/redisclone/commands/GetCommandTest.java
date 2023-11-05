package com.marouane.challenges.redisclone.commands;

import com.marouane.challenges.redisclone.CacheManager;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GetCommandTest {


    @Test
    void getReturnsValueWhenItExists() throws CommandException {
        var manager = CacheManager.getInstance();
        manager.put("key1", "val1");
        manager.put("key2", "val2");

        var command = new GetCommand();
        var param = new CommandParameter("key1");
        assertThat(command.execute(List.of(param))).isEqualTo("val1");
    }

}