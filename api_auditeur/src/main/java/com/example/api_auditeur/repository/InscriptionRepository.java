package com.example.api_auditeur.repository;

import com.example.api_auditeur.model.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
}
