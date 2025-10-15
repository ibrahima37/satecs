package com.example.api_auditeur.controller;

import com.example.api_auditeur.model.Notification;
import com.example.api_auditeur.repository.UtilisateurRepository;
import com.example.api_auditeur.service.EmailService;
import com.example.api_auditeur.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
