package com.example.api_auditeur.service;

import com.example.api_auditeur.repository.FormationRepository;
import com.example.api_auditeur.repository.InscriptionRepository;
import com.example.api_auditeur.repository.NotificationRepository;
import com.example.api_auditeur.repository.PaiementRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service @AllArgsConstructor
public class StatisticsService {

    private final FormationRepository formationRepository;
    private final InscriptionRepository inscriptionRepository;
    private final PaiementRepository paiementRepository;
    private final NotificationRepository notificationRepository;

    public Map<String, Long> getStatsForUser(Long utilisateurId) {
        Map<String, Long> stats = new HashMap<>();

        stats.put("formations", formationRepository.count());
        stats.put("inscriptions", inscriptionRepository.count());
        stats.put("paiements", paiementRepository.count());
        stats.put("notifications", notificationRepository.countNotificationsEnvoyees());

        return stats;
    }
}
