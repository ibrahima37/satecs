package com.example.api_auditeur.service;
import com.example.api_auditeur.model.Inscription;
import com.example.api_auditeur.model.Utilisateur;
import com.twilio.rest.api.v2010.account.Message;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service @AllArgsConstructor
public class SmsService {

    private final String FROM_NUMBER = "+221773855062";

    public void envoyerSmsConfirmation(Inscription inscription) {
        Utilisateur utilisateur = inscription.getUtilisateur();
        String numeroTel = inscription.getNumeroTel();

        Message message = Message.creator(
                new com.twilio.type.PhoneNumber(numeroTel),
                new com.twilio.type.PhoneNumber(FROM_NUMBER),
                "Bonjour " + utilisateur.getNom() + " " + utilisateur.getPrenom() +
                        ", votre inscription à la formation " + inscription.getFormation().getTitre() +
                        " a été créée avec succès !"
        ).create();
        System.out.println("SMS envoyé à " + numeroTel + " avec SID : " + message.getSid());
    }

}

