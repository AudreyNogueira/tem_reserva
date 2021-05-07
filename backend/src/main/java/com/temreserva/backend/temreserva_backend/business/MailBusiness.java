package com.temreserva.backend.temreserva_backend.business;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.temreserva.backend.temreserva_backend.data.repository.MailTemplateRepository;
import com.temreserva.backend.temreserva_backend.web.config.MailConfig;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailBusiness {
    private final JavaMailSender mailSender;
    private final MailConfig config;
    private final MailTemplateRepository mailTemplateRepository;

    public MailBusiness(MailTemplateRepository mailTemplateRepository) {
        this.mailTemplateRepository = mailTemplateRepository;
        config = new MailConfig();
        mailSender = config.configurateMailSender();
    }

    public void sendMail(String to, String subject, String templateName){
        try {
            String text = mailTemplateRepository.findByDescription(templateName);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(text, true); // Use this or above line.
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("temreservaoficial@gmail.com");
            mailSender.send(mimeMessage);
        } catch (MailException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
