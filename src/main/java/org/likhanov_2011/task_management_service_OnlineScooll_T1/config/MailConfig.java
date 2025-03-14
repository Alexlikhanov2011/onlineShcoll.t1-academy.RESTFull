package org.likhanov_2011.task_management_service_OnlineScooll_T1.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mail.notifications")
@Getter
@Setter
public class MailConfig {
    private boolean enabled;
    private String from;
    private String to;
    private String subject;
    private String testContent;
    private String encoding = "UTF-8";
}