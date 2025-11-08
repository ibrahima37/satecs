package com.example.api_auditeur.controller;

import com.example.api_auditeur.model.Utilisateur;
import com.example.api_auditeur.model.UtilisateurDetails;
import com.example.api_auditeur.service.StatisticsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@AllArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('AUDITEUR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Map<String, Long>> getStatistics(Authentication authentication) {

        Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();
        Long utilisateurId = utilisateur.getId(); // ✅ accès direct à l’ID

        Map<String, Long> stats = statisticsService.getStatsForUser(utilisateurId);
        return ResponseEntity.ok(stats);
    }

}
