package com.example.api_auditeur.repository;

import com.example.api_auditeur.model.Notification;
import com.example.api_auditeur.model.page_enum.CanalNotification;
import com.example.api_auditeur.model.page_enum.TypeFormation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByDestinataire(String destinataire);
    List<Notification> findByCanalNotification(CanalNotification canal);
    List<Notification> findByType(TypeFormation type);
    List<Notification> findByEnvoyee(Boolean envoyee);
    List<Notification> findByDateEnvoiBetween(LocalDate dateDebut, LocalDate dateFin);
}
