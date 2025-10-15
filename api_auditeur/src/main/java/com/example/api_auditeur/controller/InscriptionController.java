package com.example.api_auditeur.controller;

import com.example.api_auditeur.model.Inscription;
import com.example.api_auditeur.model.Paiement;
import com.example.api_auditeur.service.InscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @AllArgsConstructor
@RequestMapping("/api/inscription")
@CrossOrigin("*")
public class InscriptionController {

    private InscriptionService inscriptionService;

    @PostMapping("/soumettre")
    public ResponseEntity<Inscription> soumettre(
            @RequestParam Long utilisateurId,
            @RequestParam Long formationId,
            @RequestBody Paiement paiement){
        Inscription inscription = inscriptionService.soumettreInscription(utilisateurId, formationId,paiement);
        return ResponseEntity.status(HttpStatus.CREATED).body(inscription);
    }

    @PutMapping("/valider/{id}")
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Inscription> valider(@PathVariable Long id){
        Inscription inscription = inscriptionService.validerInscription(id);
        return ResponseEntity.ok(inscription);
    }

    @PutMapping("/rejeter/{id}")
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Inscription> rejeter(@PathVariable Long id){
        Inscription inscription = inscriptionService.rejeterInscription(id);
        return ResponseEntity.ok(inscription);
    }

    @PostMapping("/mettre-en-attente")
    public ResponseEntity<Inscription> mettreEnAttente(@RequestParam Long utilisateurId,
                                                       @RequestParam Long formationId) {
        Inscription inscription = inscriptionService.mettreEnAttente(utilisateurId, formationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(inscription);
    }

}
