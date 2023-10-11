package com.catcher.batch.datasource.config;

import com.catcher.batch.infrastructure.KmsUtils;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {

    /**
     * DB
     */
    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${spring.datasource.username}")
    private String databaseUsername;

    @Value("${spring.datasource.password}")
    private String databasePassword;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    /**
     * SSH
     */
    @Value("${ssh.host}")
    private String sshHost;

    @Value("${ssh.port}")
    private int sshPort;

    @Value("${ssh.username}")
    private String sshUsername;

    @Value("${ssh.password}")
    private String sshPassword;

    @Value("${ssh.datasource.origin}")
    private String originUrl;

    @Value("${ssh.local-port}")
    private int localPort;

    @Bean
    public DataSource dataSource() throws Exception {

        JSch jsch = new JSch();
        Session session = jsch.getSession(
                KmsUtils.decrypt(sshUsername),
                KmsUtils.decrypt(sshHost),
                sshPort
        );
        session.setPassword(KmsUtils.decrypt(sshPassword));
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        int assignedPort = session.setPortForwardingL(0,
                KmsUtils.decrypt(originUrl),
                localPort
        ); // TODO: lport 값(현재 0)은 추후 서버 올릴때는 지정해줘야함

        return DataSourceBuilder.create()
                .url(KmsUtils.decrypt(databaseUrl))
                .username(KmsUtils.decrypt(databaseUsername))
                .password(KmsUtils.decrypt(databasePassword))
                .build();
    }
}
