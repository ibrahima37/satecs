package com.example.api_auditeur.repository;

import com.example.api_auditeur.model.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {
}
