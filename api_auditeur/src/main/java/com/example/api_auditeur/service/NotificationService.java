package com.example.api_auditeur.service;

import com.example.api_auditeur.dto.CreateNotificationRequest;
import com.example.api_auditeur.dto.NotificationDto;
import com.example.api_auditeur.dto.NotificationMasseRequest;
import com.example.api_auditeur.model.Notification;
import com.example.api_auditeur.model.page_enum.CanalNotification;
import com.example.api_auditeur.model.page_enum.TypeFormation;
import com.example.api_auditeur.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service @AllArgsConstructor @Data
@Slf4j
public class NotificationService {

    private NotificationRepository notificationRepository;

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

        return notificationRepository.save(notification);

    }

    // CREATE - Créer une notification
    @Transactional
    public NotificationDto creerNotification(CreateNotificationRequest request) {
        Notification notification = new Notification();
        notification.setObjet(request.getObjet());
        notification.setContenu(request.getContenu());
        notification.setType(request.getType());
        notification.setDestinataire(request.getDestinataire());
        notification.setCanalNotification(request.getCanalNotification());
        notification.setDateEnvoi(LocalDate.now());
        notification.setEnvoyee(false);

        // Si envoi immédiat
        if (request.getEnvoyerImmediatement()) {
            envoyerNotification(notification);
        }

        Notification saved = notificationRepository.save(notification);
        return convertToDto(saved);
    }

    // CREATE - Créer des notifications en masse
    @Transactional
    public List<NotificationDto> creerNotificationsMasse(NotificationMasseRequest request) {
        List<Notification> notifications = new ArrayList<>();

        for (String destinataire : request.getDestinataires()) {
            Notification notification = new Notification();
            notification.setObjet(request.getObjet());
            notification.setContenu(request.getContenu());
            notification.setType(request.getType());
            notification.setDestinataire(destinataire);
            notification.setCanalNotification(request.getCanalNotification());
            notification.setDateEnvoi(LocalDate.now());
            notification.setEnvoyee(false);

            notifications.add(notification);
        }

        List<Notification> saved = notificationRepository.saveAll(notifications);
        return saved.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // READ - Get by ID
    public NotificationDto getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée avec l'id: " + id));
        return convertToDto(notification);
    }

    // READ - Get All
    public List<NotificationDto> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // READ - Par destinataire
    public List<NotificationDto> getNotificationsByDestinataire(String destinataire) {
        return notificationRepository.findByDestinataire(destinataire).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // READ - Par type
    public List<NotificationDto> getNotificationsByType(TypeFormation type) {
        return notificationRepository.findByType(type).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // READ - Par canal
    public List<NotificationDto> getNotificationsByCanal(CanalNotification canal) {
        return notificationRepository.findByCanalNotification(canal).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // READ - Notifications envoyées
    public List<NotificationDto> getNotificationsEnvoyees() {
        return notificationRepository.findByEnvoyee(true).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // READ - Notifications en attente
    public List<NotificationDto> getNotificationsEnAttente() {
        return notificationRepository.findNotificationsEnAttente().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // READ - Notifications du jour
    public List<NotificationDto> getNotificationsDuJour() {
        return notificationRepository.findNotificationsDuJour().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // READ - Notifications à envoyer aujourd'hui
    public List<NotificationDto> getNotificationsAEnvoyerAujourdhui() {
        return notificationRepository.findNotificationsAEnvoyerAujourdhui().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // UPDATE - Modifier une notification
    @Transactional
    public NotificationDto modifierNotification(Long id, NotificationDto notificationDto) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée avec l'id: " + id));

        // Ne pas modifier une notification déjà envoyée
        if (notification.getEnvoyee()) {
            throw new RuntimeException("Impossible de modifier une notification déjà envoyée");
        }

        notification.setObjet(notificationDto.getObjet());
        notification.setContenu(notificationDto.getContenu());
        notification.setType(notificationDto.getType());
        notification.setDestinataire(notificationDto.getDestinataire());
        notification.setCanalNotification(notificationDto.getCanalNotification());

        Notification updated = notificationRepository.save(notification);
        return convertToDto(updated);
    }

    // UPDATE - Envoyer une notification
    @Transactional
    public NotificationDto envoyerNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée avec l'id: " + id));

        if (notification.getEnvoyee()) {
            throw new RuntimeException("Cette notification a déjà été envoyée");
        }

        envoyerNotification(notification);
        Notification updated = notificationRepository.save(notification);
        return convertToDto(updated);
    }

    // UPDATE - Envoyer toutes les notifications en attente
    @Transactional
    public List<NotificationDto> envoyerNotificationsEnAttente() {
        List<Notification> notifications = notificationRepository.findNotificationsEnAttente();

        for (Notification notification : notifications) {
            envoyerNotification(notification);
        }

        List<Notification> updated = notificationRepository.saveAll(notifications);
        return updated.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // UPDATE - Marquer comme envoyée
    @Transactional
    public NotificationDto marquerCommeEnvoyee(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée avec l'id: " + id));

        notification.setEnvoyee(true);
        notification.setDateEnvoi(LocalDate.now());

        Notification updated = notificationRepository.save(notification);
        return convertToDto(updated);
    }

    // DELETE
    @Transactional
    public void supprimerNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée avec l'id: " + id));

        // Ne pas supprimer une notification envoyée
        if (notification.getEnvoyee()) {
            throw new RuntimeException("Impossible de supprimer une notification déjà envoyée");
        }

        notificationRepository.deleteById(id);
    }

    // STATISTIQUES

    public Long countNotificationsByDestinataire(String destinataire) {
        return notificationRepository.countByDestinataire(destinataire);
    }

    public Long countNotificationsEnvoyees() {
        return notificationRepository.countNotificationsEnvoyees();
    }

    // Méthode privée pour envoyer réellement la notification
    private void envoyerNotification(Notification notification) {
        try {
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
                default:
                    log.warn("Canal de notification non supporté: {}", notification.getCanalNotification());
            }

            notification.setEnvoyee(true);
            notification.setDateEnvoi(LocalDate.now());
            log.info("Notification envoyée avec succès à {} via {}",
                    notification.getDestinataire(), notification.getCanalNotification());

        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de la notification: {}", e.getMessage());
            throw new RuntimeException("Erreur lors de l'envoi de la notification: " + e.getMessage());
        }
    }

    // Simuler l'envoi par email
    private void envoyerParEmail(Notification notification) {
        log.info("Envoi d'un email à {} - Objet: {}",
                notification.getDestinataire(), notification.getObjet());
        // TODO: Intégrer un service d'email (JavaMailSender, SendGrid, etc.)
    }

    // Simuler l'envoi par SMS
    private void envoyerParSMS(Notification notification) {
        log.info("Envoi d'un SMS à {} - Message: {}",
                notification.getDestinataire(), notification.getContenu());
        // TODO: Intégrer un service SMS (Twilio, etc.)
    }

    // Simuler l'envoi par Push
    private void envoyerParPush(Notification notification) {
        log.info("Envoi d'une notification push à {} - Titre: {}",
                notification.getDestinataire(), notification.getObjet());
        // TODO: Intégrer Firebase Cloud Messaging ou autre service push
    }

    // Conversion Entity -> DTO
    private NotificationDto convertToDto(Notification notification) {
        NotificationDto dto = new NotificationDto();
        dto.setId(notification.getId());
        dto.setObjet(notification.getObjet());
        dto.setContenu(notification.getContenu());
        dto.setType(notification.getType());
        dto.setDateEnvoi(notification.getDateEnvoi());
        dto.setEnvoyee(notification.getEnvoyee());
        dto.setDestinataire(notification.getDestinataire());
        dto.setCanalNotification(notification.getCanalNotification());
        return dto;
    }

  /*  public Notification marqueCommeLue(Long notificationId){
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
    }*/
}
