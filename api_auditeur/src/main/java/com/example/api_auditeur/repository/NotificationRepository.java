package com.example.api_auditeur.repository;

import com.example.api_auditeur.model.Notification;
import com.example.api_auditeur.model.page_enum.CanalNotification;
import com.example.api_auditeur.model.page_enum.TypeFormation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  /*  List<Notification> findByDestinataire(String destinataire);
    List<Notification> findByCanalNotification(CanalNotification canal);
    List<Notification> findByType(TypeFormation type);
    List<Notification> findByEnvoyee(Boolean envoyee);
    List<Notification> findByDateEnvoiBetween(LocalDate dateDebut, LocalDate dateFin);

    */
    // Recherche par destinataire
    List<Notification> findByDestinataire(String destinataire);

    // Recherche par type
    List<Notification> findByType(TypeFormation type);

    // Recherche par canal
    List<Notification> findByCanalNotification(CanalNotification canal);

    // Notifications envoyées
    List<Notification> findByEnvoyee(Boolean envoyee);

    // Notifications non envoyées
    @Query("SELECT n FROM Notification n WHERE n.envoyee = false")
    List<Notification> findNotificationsEnAttente();

    // Notifications par période
    List<Notification> findByDateEnvoiBetween(LocalDate debut, LocalDate fin);

    // Notifications du jour
    @Query("SELECT n FROM Notification n WHERE n.dateEnvoi = CURRENT_DATE")
    List<Notification> findNotificationsDuJour();

    // Notifications à envoyer aujourd'hui
    @Query("SELECT n FROM Notification n WHERE n.dateEnvoi = CURRENT_DATE AND n.envoyee = false")
    List<Notification> findNotificationsAEnvoyerAujourdhui();

    // Compter par destinataire
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.destinataire = ?1")
    Long countByDestinataire(String destinataire);

    // Compter les notifications envoyées
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.envoyee = true")
    Long countNotificationsEnvoyees();

    long count();

}
