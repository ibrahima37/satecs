package com.example.api_auditeur.repository;

import com.example.api_auditeur.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
