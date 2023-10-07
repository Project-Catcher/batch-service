package com.catcher.batch.job.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.launch.JobLauncher;

@RequiredArgsConstructor
public class CommonJobConfig {

    protected final JobLauncher jobLauncher;
}
