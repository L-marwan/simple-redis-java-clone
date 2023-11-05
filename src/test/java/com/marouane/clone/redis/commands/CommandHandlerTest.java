package com.marouane.clone.redis.commands;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CommandHandlerTest {

    @Test
    void handleCommandWithValidParamsShouldReturnValidResult() throws CommandException {
        var params = List.of("PING");
        assertThat(CommandHandler.handle(params)).isEqualTo("PONG ");
        params = List.of("PING", "OTHER", "PARAM");
        assertThat(CommandHandler.handle(params)).isEqualTo("PONG OTHER PARAM");
        assertThat(CommandHandler.handle("PING")).isEqualTo("PONG ");
        params = List.of("SET", "key", "val");
        assertThat(CommandHandler.handle(params)).isEqualTo("OK");
    }
}