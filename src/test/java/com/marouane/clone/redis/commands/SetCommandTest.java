package com.marouane.clone.redis.commands;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SetCommandTest {

    @Test
    void executeThrowsExceptionWhenArgumentsAreLessThen2() {
        var command = new SetCommand();

        assertThatThrownBy(() -> command.execute(List.of())).isInstanceOf(CommandException.class)
                .hasMessageContaining("Set Command needs at least 2 arguments");
        assertThatThrownBy(() -> command.execute(List.of(new CommandParameter("val1")))).isInstanceOf(CommandException.class)
                .hasMessageContaining("Set Command needs at least 2 arguments");

    }

    @Test
    void executeThrowsExceptionWhenKeyOrValueAreNotString() {
        var command = new SetCommand();

        var stringParam = new CommandParameter("val1");
        var intParam = new CommandParameter(55);


        String errorMessage = "Set command only supports keys and values of type String.";
        assertThatThrownBy(() -> command.execute(List.of(stringParam, intParam))).isInstanceOf(CommandException.class)
                .hasMessageContaining(errorMessage);
        assertThatThrownBy(() -> command.execute(List.of(intParam, intParam))).isInstanceOf(CommandException.class)
                .hasMessageContaining(errorMessage);
        assertThatThrownBy(() -> command.execute(List.of(intParam, stringParam))).isInstanceOf(CommandException.class)
                .hasMessageContaining(errorMessage);

    }

    @Test
    void executeReturnsOKWhenSuccessful() throws CommandException {
        var command = new SetCommand();
        var key = new CommandParameter("key");
        var value = new CommandParameter("val");
        assertThat(command.execute(List.of(key, value))).isEqualTo("OK");
    }
}