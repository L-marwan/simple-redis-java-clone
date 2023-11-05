package com.marouane.clone.redis.commands;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommandsRegistryTest {


    @Test
    void commandsAreFetched() throws CommandException {
        var reg = CommandsRegistry.getInstance();
        var commands = reg.getCommands();
        assertThat(commands).hasSize(3);
    }

}