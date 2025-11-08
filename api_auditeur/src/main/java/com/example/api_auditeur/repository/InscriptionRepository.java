package com.example.api_auditeur.repository;

import com.example.api_auditeur.model.Inscription;
import com.example.api_auditeur.model.page_enum.EtatInscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {

    // Recherche par formation
    List<Inscription> findByFormationId(Long formationId);

    // Recherche par utilisateur
    List<Inscription> findByUtilisateurId(Long utilisateurId);

    // Recherche par état
    List<Inscription> findByEtatInscription(EtatInscription etatInscription);

    // Recherche par paiement
    Optional<Inscription> findByPaiementId(Long paiementId);

    // Vérifier si un utilisateur est déjà inscrit à une formation
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM Inscription i WHERE i.formation.id = ?1 AND i.utilisateur.id = ?2")
    boolean existsByFormationIdAndUtilisateurId(Long formationId, Long utilisateurId);

    // Compter les inscriptions d'une formation
    @Query("SELECT COUNT(i) FROM Inscription i WHERE i.formation.id = ?1")
    Long countByFormationId(Long formationId);

    // Inscriptions par période
    List<Inscription> findByDateInscriptionBetween(LocalDate dateDebut, LocalDate dateFin);

    // Inscriptions d'aujourd'hui
    @Query("SELECT i FROM Inscription i WHERE i.dateInscription = CURRENT_DATE")
    List<Inscription> findInscriptionsDuJour();

    // Inscriptions par formation et état
    @Query("SELECT i FROM Inscription i WHERE i.formation.id = ?1 AND i.etatInscription = ?2")
    List<Inscription> findByFormationIdAndEtat(Long formationId, EtatInscription etat);

    long count();
}
