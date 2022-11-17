package br.com.fiap.emailconsumer.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
    void sendSimpleMessageUsingTemplate(String to, String subject, String ...templateModel);
}
