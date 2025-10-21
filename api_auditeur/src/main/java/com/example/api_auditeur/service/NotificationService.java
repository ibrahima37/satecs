package com.example.api_auditeur.service;

import com.example.api_auditeur.model.Notification;
import com.example.api_auditeur.model.page_enum.CanalNotification;
import com.example.api_auditeur.model.page_enum.TypeFormation;
import com.example.api_auditeur.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    // Récupérer toutes les notifications
    public List<Notification> getAllNotifications() {
        return notificationRepo.findAll();
    }

    // Récupérer une notification par ID
    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepo.findById(id);
    }

    // Créer une nouvelle notification
    public Notification createNotification(Notification notification) {
        notification.setEnvoyee(false);
        notification.setDateEnvoi(LocalDate.now());
        return notificationRepo.save(notification);
    }

    // Mettre à jour une notification
    public Notification updateNotification(Long id, Notification notificationDetails) {
        Notification notification = notificationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée avec l'id: " + id));

        notification.setObjet(notificationDetails.getObjet());
        notification.setContenu(notificationDetails.getContenu());
        notification.setType(notificationDetails.getType());
        notification.setDestinataire(notificationDetails.getDestinataire());
        notification.setCanalNotification(notificationDetails.getCanalNotification());

        return notificationRepo.save(notification);
    }

    // Supprimer une notification
    public void deleteNotification(Long id) {
        Notification notification = notificationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée avec l'id: " + id));
        notificationRepo.delete(notification);
    }

    // Envoyer une notification
    public Notification envoyerNotification(Long id) {
        Notification notification = notificationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée avec l'id: " + id));

        // Logique d'envoi selon le canal
        switch (notification.getCanalNotification()) {
            case EMAIL:
                envoyerParEmail(notification);
                break;
            case SMS:
                envoyerParSMS(notification);
                break;
            case PUSH:
                envoyerParPush(notification);
                break;
            case NOTIFICATION_APP:
                envoyerParWhatsApp(notification);
                break;
        }

        notification.setEnvoyee(true);
        notification.setDateEnvoi(LocalDate.now());
        return notificationRepo.save(notification);
    }

    // Récupérer les notifications par destinataire
    public List<Notification> getNotificationsByDestinataire(String destinataire) {
        return notificationRepo.findByDestinataire(destinataire);
    }

    // Récupérer les notifications par canal
    public List<Notification> getNotificationsByCanal(CanalNotification canal) {
        return notificationRepo.findByCanalNotification(canal);
    }

    // Récupérer les notifications par type
    public List<Notification> getNotificationsByType(TypeFormation type) {
        return notificationRepo.findByType(type);
    }

    // Récupérer les notifications envoyées
    public List<Notification> getNotificationsEnvoyees() {
        return notificationRepo.findByEnvoyee(true);
    }

    // Récupérer les notifications non envoyées
    public List<Notification> getNotificationsNonEnvoyees() {
        return notificationRepo.findByEnvoyee(false);
    }

    // Récupérer les notifications par période
    public List<Notification> getNotificationsByPeriode(LocalDate dateDebut, LocalDate dateFin) {
        return notificationRepo.findByDateEnvoiBetween(dateDebut, dateFin);
    }

    // Envoyer des notifications en masse
    public List<Notification> envoyerNotificationsEnMasse(List<Long> ids) {
        List<Notification> notifications = notificationRepo.findAllById(ids);
        for (Notification notification : notifications) {
            envoyerNotification(notification.getId());
        }
        return notifications;
    }

    // Méthodes privées d'envoi (à implémenter selon vos besoins)
    private void envoyerParEmail(Notification notification) {
        // Implémentation de l'envoi par email
        System.out.println("Envoi email à: " + notification.getDestinataire());
    }

    private void envoyerParSMS(Notification notification) {
        // Implémentation de l'envoi par SMS
        System.out.println("Envoi SMS à: " + notification.getDestinataire());
    }

    private void envoyerParPush(Notification notification) {
        // Implémentation de l'envoi par notification push
        System.out.println("Envoi notification push à: " + notification.getDestinataire());
    }

    private void envoyerParWhatsApp(Notification notification) {
        // Implémentation de l'envoi par WhatsApp
        System.out.println("Envoi WhatsApp à: " + notification.getDestinataire());
    }
}
