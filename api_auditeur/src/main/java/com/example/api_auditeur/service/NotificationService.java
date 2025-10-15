package com.example.api_auditeur.service;

import com.example.api_auditeur.model.Notification;
import com.example.api_auditeur.model.page_enum.CanalNotification;
import com.example.api_auditeur.model.page_enum.TypeFormation;
import com.example.api_auditeur.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service @AllArgsConstructor @Data
public class NotificationService {

    private NotificationRepository notificationRepo;

    public Notification envoyerNotification(
            String objet,
            String contenu,
            String destinataire,
            LocalDate dateEnvoi,
            Boolean envoyee,
            TypeFormation type,
            CanalNotification canal){

        Notification notification = new Notification();
        notification.setObjet(objet);
        notification.setContenu(contenu);
        notification.setDestinataire(destinataire);
        notification.setCanalNotification(canal);
        notification.setDateEnvoi(LocalDate.now());
        notification.setEnvoyee(true);
        notification.setType(type);

        return notificationRepo.save(notification);

    }

    public Notification marqueCommeLue(Long notificationId){
        Notification notification= notificationRepo.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification introuvable"));

        notification.setEnvoyee(true);

        return notificationRepo.save(notification);
    }
}
