package com.catcher.batch.core.domain;

import com.catcher.batch.core.domain.command.Command;
import org.springframework.stereotype.Component;

@Component
public class CatcherItemExecutor implements CommandExecutor {
    @Override
    public <T> T run(Command<T> command) {
        return command.execute();
    }
}
