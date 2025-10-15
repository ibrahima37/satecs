package com.example.api_auditeur.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service @AllArgsConstructor
public class EmailService {

    private JavaMailSender mailSender;

    public void envoyerEmail(String destinataire, String objet, String contenu) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinataire);
        message.setSubject(objet);
        message.setText(contenu);
        message.setFrom("ibrahimadia632@gmail.com");
        mailSender.send(message);
    }
}
