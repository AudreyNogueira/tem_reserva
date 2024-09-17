package com.temreserva.backend.temreserva_backend.web.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
    @Bean
    public JavaMailSender configurateMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("temreservaoficial@gmail.com");
        mailSender.setPassword("tCMw^2KZcg0s");

        getProperties(mailSender.getJavaMailProperties());
        return mailSender;
    }

    public Properties getProperties(Properties mailSenderProps) {
        Properties props = mailSenderProps;
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        return props;
    }
}
