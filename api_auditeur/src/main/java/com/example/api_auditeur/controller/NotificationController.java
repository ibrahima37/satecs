package com.example.api_auditeur.controller;

import com.example.api_auditeur.model.Notification;
import com.example.api_auditeur.model.page_enum.CanalNotification;
import com.example.api_auditeur.model.page_enum.TypeFormation;
import com.example.api_auditeur.repository.UtilisateurRepository;
import com.example.api_auditeur.service.EmailService;
import com.example.api_auditeur.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController @AllArgsConstructor
@RequestMapping("/api/notification")
@CrossOrigin("*")
public class NotificationController {

    private NotificationService notificationService;

    private EmailService emailService;

    private UtilisateurRepository ut;

    @PostMapping("/envoyer")
    public ResponseEntity<Notification> envoyer(@RequestBody Notification notification){

        ut.findByEmail(notification.getDestinataire())
                .orElseThrow(()-> new RuntimeException("Utilisateur non trouvé"));

        Notification envoyee = notificationService.envoyerNotification(
                notification.getObjet(),
                notification.getContenu(),
                notification.getDestinataire(),
                notification.getDateEnvoi(),
                notification.getEnvoyee(),
                notification.getType(),
                notification.getCanalNotification()
        );
        // Si le canal est EMAIL, envoyer l'e-mail
        if ("EMAIL".equalsIgnoreCase(String.valueOf(notification.getCanalNotification()))) {
            emailService.envoyerEmail(
                    notification.getDestinataire(),
                    notification.getObjet(),
                    notification.getContenu()
            );
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(envoyee);
    }

    @PutMapping("/lue/{id}")
    public ResponseEntity<Notification> marquerCommeLue(@PathVariable Long id){

        Notification notification = notificationService.marqueCommeLue(id);

        return ResponseEntity.ok(notification);
    }

    // Récupérer toutes les notifications
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    // Récupérer une notification par ID
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
        return notificationService.getNotificationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Créer une nouvelle notification
    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        Notification newNotification = notificationService.createNotification(notification);
        return ResponseEntity.status(HttpStatus.CREATED).body(newNotification);
    }

    // Mettre à jour une notification
    @PutMapping("/{id}")
    public ResponseEntity<Notification> updateNotification(
            @PathVariable Long id,
            @RequestBody Notification notificationDetails) {
        try {
            Notification updatedNotification = notificationService.updateNotification(id, notificationDetails);
            return ResponseEntity.ok(updatedNotification);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer une notification
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        try {
            notificationService.deleteNotification(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Envoyer une notification
    @PostMapping("/{id}/envoyer")
    public ResponseEntity<Notification> envoyerNotification(@PathVariable Long id) {
        try {
            Notification notification = notificationService.envoyerNotification(id);
            return ResponseEntity.ok(notification);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Récupérer les notifications par destinataire
    @GetMapping("/destinataire/{destinataire}")
    public ResponseEntity<List<Notification>> getNotificationsByDestinataire(
            @PathVariable String destinataire) {
        List<Notification> notifications = notificationService.getNotificationsByDestinataire(destinataire);
        return ResponseEntity.ok(notifications);
    }

    // Récupérer les notifications par canal
    @GetMapping("/canal/{canal}")
    public ResponseEntity<List<Notification>> getNotificationsByCanal(
            @PathVariable CanalNotification canal) {
        List<Notification> notifications = notificationService.getNotificationsByCanal(canal);
        return ResponseEntity.ok(notifications);
    }

    // Récupérer les notifications par type
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Notification>> getNotificationsByType(
            @PathVariable TypeFormation type) {
        List<Notification> notifications = notificationService.getNotificationsByType(type);
        return ResponseEntity.ok(notifications);
    }

    // Récupérer les notifications envoyées
    @GetMapping("/envoyees")
    public ResponseEntity<List<Notification>> getNotificationsEnvoyees() {
        List<Notification> notifications = notificationService.getNotificationsEnvoyees();
        return ResponseEntity.ok(notifications);
    }

    // Récupérer les notifications non envoyées
    @GetMapping("/non-envoyees")
    public ResponseEntity<List<Notification>> getNotificationsNonEnvoyees() {
        List<Notification> notifications = notificationService.getNotificationsNonEnvoyees();
        return ResponseEntity.ok(notifications);
    }

    // Récupérer les notifications par période
    @GetMapping("/periode")
    public ResponseEntity<List<Notification>> getNotificationsByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        List<Notification> notifications = notificationService.getNotificationsByPeriode(dateDebut, dateFin);
        return ResponseEntity.ok(notifications);
    }

    // Envoyer des notifications en masse
    @PostMapping("/envoyer-masse")
    public ResponseEntity<List<Notification>> envoyerNotificationsEnMasse(
            @RequestBody Map<String, List<Long>> request) {
        List<Long> ids = request.get("ids");
        List<Notification> notifications = notificationService.envoyerNotificationsEnMasse(ids);
        return ResponseEntity.ok(notifications);
    }
}
