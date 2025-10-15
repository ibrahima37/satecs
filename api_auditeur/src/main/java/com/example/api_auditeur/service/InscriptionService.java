package com.example.api_auditeur.service;

import com.example.api_auditeur.model.Formation;
import com.example.api_auditeur.model.Inscription;
import com.example.api_auditeur.model.Paiement;
import com.example.api_auditeur.model.Utilisateur;
import com.example.api_auditeur.model.page_enum.EtatInscription;
import com.example.api_auditeur.model.page_enum.Role;
import com.example.api_auditeur.model.page_enum.StatutPaiement;
import com.example.api_auditeur.repository.FormationRepository;
import com.example.api_auditeur.repository.InscriptionRepository;
import com.example.api_auditeur.repository.PaiementRepository;
import com.example.api_auditeur.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class InscriptionService {

    private InscriptionRepository repository;

    private UtilisateurRepository uRepository;

    private FormationRepository fRepository;

    private PaiementRepository paiementRepository;

    public void sinscrie(
            String nom,
            String prenom,
            String email,
            String motDePasse,
            Long formationId){

        Utilisateur utilisateur= new Utilisateur();
        utilisateur.setNom(nom);
        utilisateur.setPrenom(prenom);
        utilisateur.setEmail(email);
        utilisateur.setRole(Role.ADMIN);
        utilisateur.setMotDePasse(new BCryptPasswordEncoder().encode(motDePasse));
        uRepository.save(utilisateur);

        Formation formation = fRepository.findById(formationId)
                .orElseThrow(()->new RuntimeException("Formation introuvable"));

        Inscription inscription = new Inscription();
        inscription.setUtilisateur(utilisateur);
        inscription.setFormation(formation);
        inscription.setDateInscription(LocalDate.now());
        inscription.setEtatInscription(EtatInscription.ANNULEE);
        //inscription.setPaiement();

        repository.save(inscription);
    }

    public Inscription soumettreInscription(Long utilisateurId, Long formationId, Paiement paiement){
        Utilisateur utilisateur = uRepository.findById(utilisateurId)
                .orElseThrow(()-> new RuntimeException("Utilisateur introuvable"));
        Formation formation = fRepository.findById(formationId)
                .orElseThrow(()-> new RuntimeException("Formation introuvable"));

        Paiement paiementEnregistre = paiementRepository.save(paiement);

        Inscription inscription = new Inscription();
        inscription.setUtilisateur(utilisateur);
        inscription.setFormation(formation);
        inscription.setEtatInscription(EtatInscription.DOCUMENTS_EN_ATTENTE);
        inscription.setDateInscription(LocalDate.now());
        inscription.setPaiement(paiementEnregistre);
        return repository.save(inscription);
    }

    public Inscription validerInscription(Long inscriptionId){
        Inscription inscription = repository.findById(inscriptionId)
                .orElseThrow(()-> new RuntimeException("Inscription introuvable"));
        inscription.setEtatInscription(EtatInscription.VALIDER);
        inscription.setDateInscription(LocalDate.now());

        return repository.save(inscription);
    }

    public Inscription rejeterInscription(Long inscriptionId){
        Inscription inscription = repository.findById(inscriptionId)
                .orElseThrow(()-> new RuntimeException("Inscription introuvable"));
        inscription.setEtatInscription(EtatInscription.VALIDER);
        inscription.setDateInscription(LocalDate.now());

        return repository.save(inscription);
    }

    public Inscription mettreEnAttente(Long utilisateurId, Long formationId){
        Utilisateur utilisateur = uRepository.findById(utilisateurId)
                .orElseThrow(()-> new RuntimeException("Utilisateur introuvable"));
        Formation formation = fRepository.findById(formationId)
                .orElseThrow(()-> new RuntimeException("Formation introuvable"));

        Paiement paiement = new Paiement();
        paiement.setMontant(paiement.getMontant());
        paiement.setDatePaiement(LocalDate.now());
        paiement.setStatutPaiement(StatutPaiement.EN_ATTENTE);

        Inscription inscription = new Inscription();
        inscription.setUtilisateur(utilisateur);
        inscription.setFormation(formation);
        inscription.setEtatInscription(EtatInscription.DOCUMENTS_EN_ATTENTE);
        inscription.setDateInscription(LocalDate.now());
        inscription.setPaiement(paiement);
        return repository.save(inscription);
    }
}
