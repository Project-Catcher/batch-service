package com.catcher.batch.core.domain.command;

public interface Command<T> {
    T execute();
}
