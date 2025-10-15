package com.example.api_auditeur.controller;

import com.example.api_auditeur.model.Paiement;
import com.example.api_auditeur.service.PaiementService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/paiement")
@AllArgsConstructor @RestController
@CrossOrigin("*")
public class PaiementController {

    private final PaiementService paiementService;

    // ✅ Enregistrer
    @PostMapping("/enregistrer")
    public ResponseEntity<Paiement> enregistrer(@RequestBody Paiement paiement) {
        Paiement saved = paiementService.enregistrerPaiement(paiement);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ✅ Valider
    @PutMapping("/valider/{id}")
    public ResponseEntity<Paiement> valider(@PathVariable Long id) {
        Paiement paiement = paiementService.validerPaiement(id);
        return ResponseEntity.ok(paiement);
    }

    // ✅ Annuler
    @PutMapping("/annuler/{id}")
    public ResponseEntity<Paiement> annuler(@PathVariable Long id) {
        Paiement paiement = paiementService.annulerPaiement(id);
        return ResponseEntity.ok(paiement);
    }

    // ✅ Rembourser
    @PutMapping("/rembourser/{id}")
    public ResponseEntity<Paiement> rembourser(@PathVariable Long id) {
        Paiement paiement = paiementService.rembourserPaiement(id);
        return ResponseEntity.ok(paiement);
    }
}
