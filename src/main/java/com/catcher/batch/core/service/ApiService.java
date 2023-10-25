package com.catcher.batch.core.service;

import org.springframework.stereotype.Service;

@Service
public interface ApiService<T> {

    <T> T getData();
}