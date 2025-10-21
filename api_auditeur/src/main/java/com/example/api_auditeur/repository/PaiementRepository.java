package com.example.api_auditeur.repository;

import com.example.api_auditeur.model.Paiement;
import com.example.api_auditeur.model.page_enum.ModePaiement;
import com.example.api_auditeur.model.page_enum.StatutPaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    Optional<Paiement> findByNumTransaction(String numTransaction);
    List<Paiement> findByStatutPaiement(StatutPaiement statut);
    List<Paiement> findByModePaiement(ModePaiement mode);
    List<Paiement> findByDatePaiementBetween(LocalDate dateDebut, LocalDate dateFin);

    @Query("SELECT SUM(p.montant) FROM Paiement p")
    Double calculateTotalMontant();

    @Query("SELECT SUM(p.montant) FROM Paiement p WHERE p.statutPaiement = :statut")
    Double calculateTotalMontantByStatut(StatutPaiement statut);
}
