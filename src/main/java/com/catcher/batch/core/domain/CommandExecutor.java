package com.catcher.batch.core.domain;

import com.catcher.batch.core.domain.command.Command;

public interface CommandExecutor {
    <T> T run(Command<T> command);
}
