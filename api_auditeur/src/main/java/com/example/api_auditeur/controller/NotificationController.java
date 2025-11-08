package com.example.api_auditeur.controller;

import com.example.api_auditeur.dto.CreateNotificationRequest;
import com.example.api_auditeur.dto.NotificationDto;
import com.example.api_auditeur.dto.NotificationMasseRequest;
import com.example.api_auditeur.model.Notification;
import com.example.api_auditeur.model.page_enum.CanalNotification;
import com.example.api_auditeur.model.page_enum.TypeFormation;
import com.example.api_auditeur.repository.UtilisateurRepository;
import com.example.api_auditeur.service.EmailService;
import com.example.api_auditeur.service.NotificationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController @AllArgsConstructor
@RequestMapping("/api/notification")
//@CrossOrigin(origins = "http://localhost:4200")
public class NotificationController {

    private NotificationService notificationService;

    private EmailService emailService;

    private UtilisateurRepository ut;

    @PostMapping("/envoyer")
    @PreAuthorize("hasAuthority('ADMIN')")
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

    // CREATE - Créer une notification
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<NotificationDto> creerNotification(@Valid @RequestBody CreateNotificationRequest request) {
        try {
            NotificationDto created = notificationService.creerNotification(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // CREATE - Créer des notifications en masse
    @PostMapping("/masse")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<NotificationDto>> creerNotificationsMasse(
            @Valid @RequestBody NotificationMasseRequest request) {
        try {
            List<NotificationDto> created = notificationService.creerNotificationsMasse(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // READ - Get by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<NotificationDto> getNotificationById(@PathVariable Long id) {
        try {
            NotificationDto notification = notificationService.getNotificationById(id);
            return ResponseEntity.ok(notification);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // READ - Get All
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<NotificationDto>> getAllNotifications() {
        List<NotificationDto> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    // READ - Par destinataire
    @GetMapping("/destinataire/{destinataire}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<NotificationDto>> getNotificationsByDestinataire(@PathVariable String destinataire) {
        List<NotificationDto> notifications = notificationService.getNotificationsByDestinataire(destinataire);
        return ResponseEntity.ok(notifications);
    }

    // READ - Par type
    @GetMapping("/type/{type}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<NotificationDto>> getNotificationsByType(@PathVariable TypeFormation type) {
        List<NotificationDto> notifications = notificationService.getNotificationsByType(type);
        return ResponseEntity.ok(notifications);
    }

    // READ - Par canal
    @GetMapping("/canal/{canal}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<NotificationDto>> getNotificationsByCanal(@PathVariable CanalNotification canal) {
        List<NotificationDto> notifications = notificationService.getNotificationsByCanal(canal);
        return ResponseEntity.ok(notifications);
    }

    // READ - Notifications envoyées
    @GetMapping("/envoyees")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<NotificationDto>> getNotificationsEnvoyees() {
        List<NotificationDto> notifications = notificationService.getNotificationsEnvoyees();
        return ResponseEntity.ok(notifications);
    }

    // READ - Notifications en attente
    @GetMapping("/en-attente")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<NotificationDto>> getNotificationsEnAttente() {
        List<NotificationDto> notifications = notificationService.getNotificationsEnAttente();
        return ResponseEntity.ok(notifications);
    }

    // READ - Notifications du jour
    @GetMapping("/aujourd-hui")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<NotificationDto>> getNotificationsDuJour() {
        List<NotificationDto> notifications = notificationService.getNotificationsDuJour();
        return ResponseEntity.ok(notifications);
    }

    // READ - Notifications à envoyer aujourd'hui
    @GetMapping("/a-envoyer-aujourd-hui")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<NotificationDto>> getNotificationsAEnvoyerAujourdhui() {
        List<NotificationDto> notifications = notificationService.getNotificationsAEnvoyerAujourdhui();
        return ResponseEntity.ok(notifications);
    }

    // UPDATE - Modifier une notification
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<NotificationDto> modifierNotification(
            @PathVariable Long id,
            @Valid @RequestBody NotificationDto notificationDto) {
        try {
            NotificationDto updated = notificationService.modifierNotification(id, notificationDto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // UPDATE - Envoyer une notification
    @PostMapping("/{id}/envoyer")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<NotificationDto> envoyerNotification(@PathVariable Long id) {
        try {
            NotificationDto envoyee = notificationService.envoyerNotificationById(id);
            return ResponseEntity.ok(envoyee);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // UPDATE - Envoyer toutes les notifications en attente
    @PostMapping("/envoyer-attente")
    public ResponseEntity<List<NotificationDto>> envoyerNotificationsEnAttente() {
        try {
            List<NotificationDto> envoyees = notificationService.envoyerNotificationsEnAttente();
            return ResponseEntity.ok(envoyees);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // UPDATE - Marquer comme envoyée (manuel)
    @PatchMapping("/{id}/marquer-envoyee")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<NotificationDto> marquerCommeEnvoyee(@PathVariable Long id) {
        try {
            NotificationDto updated = notificationService.marquerCommeEnvoyee(id);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Void> supprimerNotification(@PathVariable Long id) {
        try {
            notificationService.supprimerNotification(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // STATISTIQUES

    // Compter les notifications d'un destinataire
    @GetMapping("/destinataire/{destinataire}/count")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Map<String, Long>> countNotificationsByDestinataire(@PathVariable String destinataire) {
        Long count = notificationService.countNotificationsByDestinataire(destinataire);
        Map<String, Long> response = new HashMap<>();
        response.put("nombreNotifications", count);
        return ResponseEntity.ok(response);
    }

    // Compter les notifications envoyées
    @GetMapping("/statistiques/envoyees")
    public ResponseEntity<Map<String, Long>> countNotificationsEnvoyees() {
        Long count = notificationService.countNotificationsEnvoyees();
        Map<String, Long> response = new HashMap<>();
        response.put("nombreEnvoyees", count);
        return ResponseEntity.ok(response);
    }

}
