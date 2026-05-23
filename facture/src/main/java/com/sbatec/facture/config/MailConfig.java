package com.sbatec.facture.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {


    private final ApplicationProperties applicationProperties;

    public MailConfig(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Bean(name = "factureSender")
    public JavaMailSender factureSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(applicationProperties.getSmtpHost());
        mailSender.setPort(Integer.parseInt(applicationProperties.getSmtpPort()));
        mailSender.setUsername(applicationProperties.getSmtpUserName());
        mailSender.setPassword(applicationProperties.getSmtpPassword());
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.ssl.trust", applicationProperties.getSmtpHost());
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // OBLIGATOIRE
        props.put("mail.smtp.ssl.enable", "false");
        //props.put("mail.debug", "true"); // utile pour debug
        props.put("spring.mail.properties.mail.smtp.from", applicationProperties.getSmtpUserName());
        return mailSender;
    }
}
