package com.example.api_auditeur.repository;

import com.example.api_auditeur.model.Formation;
import com.example.api_auditeur.model.page_enum.NiveauFormation;
import com.example.api_auditeur.model.page_enum.TypeFormation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface FormationRepository extends JpaRepository<Formation, Long> {
    List<Formation> findByTypeFormation(TypeFormation typeFormation);
    List<Formation> findByNiveauFormation(NiveauFormation niveauFormation);
    List<Formation> findByDateDebutBetween(LocalDate dateDebut, LocalDate dateFin);
    List<Formation> findByTarifLessThanEqual(Double tarif);

    @Query("SELECT f FROM Formation f WHERE f.dateDebut > CURRENT_DATE ORDER BY f.dateDebut ASC")
    List<Formation> findFormationsAVenir();

    @Query("SELECT f FROM Formation f WHERE f.titre LIKE %?1% OR f.description LIKE %?1%")
    List<Formation> rechercherParMotCle(String motCle);

    long count();
}
