package com.example.api_auditeur.repository;

import com.example.api_auditeur.model.Formation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormationRepository extends JpaRepository<Formation, Long> {
}
