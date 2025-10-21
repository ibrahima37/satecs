package com.example.api_auditeur.controller;

import com.example.api_auditeur.model.Paiement;
import com.example.api_auditeur.model.page_enum.ModePaiement;
import com.example.api_auditeur.model.page_enum.StatutPaiement;
import com.example.api_auditeur.service.PaiementService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    // Récupérer tous les paiements
    @GetMapping
    public ResponseEntity<List<Paiement>> getAllPaiements() {
        List<Paiement> paiements = paiementService.getAllPaiements();
        return ResponseEntity.ok(paiements);
    }

    // Récupérer un paiement par ID
    @GetMapping("/{id}")
    public ResponseEntity<Paiement> getPaiementById(@PathVariable Long id) {
        return paiementService.getPaiementById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Récupérer un paiement par numéro de transaction
    @GetMapping("/transaction/{numTransaction}")
    public ResponseEntity<Paiement> getPaiementByNumTransaction(@PathVariable String numTransaction) {
        return paiementService.getPaiementByNumTransaction(numTransaction)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Créer un nouveau paiement
    @PostMapping
    public ResponseEntity<Paiement> createPaiement(@RequestBody Paiement paiement) {
        Paiement newPaiement = paiementService.createPaiement(paiement);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPaiement);
    }

    // Mettre à jour un paiement
    @PutMapping("/{id}")
    public ResponseEntity<Paiement> updatePaiement(
            @PathVariable Long id,
            @RequestBody Paiement paiementDetails) {
        try {
            Paiement updatedPaiement = paiementService.updatePaiement(id, paiementDetails);
            return ResponseEntity.ok(updatedPaiement);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Mettre à jour le statut d'un paiement
    @PatchMapping("/{id}/statut")
    public ResponseEntity<?> updateStatutPaiement(
            @PathVariable Long id,
            @RequestBody Map<String, StatutPaiement> request) {
        try {
            if (!request.containsKey("statut")) {
                return ResponseEntity.badRequest()
                        .body("Le champ 'statut' est requis");
            }
            StatutPaiement statut = request.get("statut");
            if (statut == null) {
                return ResponseEntity.badRequest()
                        .body("Le statut ne peut pas être null");
            }

            Paiement updatedPaiement = paiementService.updateStatutPaiement(id, statut);
            return ResponseEntity.ok(updatedPaiement);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("Statut invalide: " + e.getMessage());

        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer un paiement
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaiement(@PathVariable Long id) {
        try {
            paiementService.deletePaiement(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Récupérer les paiements par statut
    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<Paiement>> getPaiementsByStatut(@PathVariable StatutPaiement statut) {
        List<Paiement> paiements = paiementService.getPaiementsByStatut(statut);
        return ResponseEntity.ok(paiements);
    }

    // Récupérer les paiements par mode de paiement
    @GetMapping("/mode/{mode}")
    public ResponseEntity<List<Paiement>> getPaiementsByMode(@PathVariable ModePaiement modePaiement) {
        List<Paiement> paiements = paiementService.getPaiementsByMode(modePaiement);
        return ResponseEntity.ok(paiements);
    }

    // Récupérer les paiements par période
    @GetMapping("/periode")
    public ResponseEntity<List<Paiement>> getPaiementsByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        List<Paiement> paiements = paiementService.getPaiementsByPeriode(dateDebut, dateFin);
        return ResponseEntity.ok(paiements);
    }

    // Calculer le montant total des paiements
    @GetMapping("/total")
    public ResponseEntity<Double> getTotalMontant() {
        Double total = paiementService.getTotalMontant();
        return ResponseEntity.ok(total != null ? total : 0.0);
    }

    // Calculer le montant total par statut
    @GetMapping("/total/statut/{statut}")
    public ResponseEntity<Double> getTotalMontantByStatut(@PathVariable StatutPaiement statut) {
        Double total = paiementService.getTotalMontantByStatut(statut);
        return ResponseEntity.ok(total != null ? total : 0.0);
    }

    // Valider un paiement
    @PostMapping("/{id}/valider")
    public ResponseEntity<Paiement> validerPaiement(@PathVariable Long id) {
        try {
            Paiement paiement = paiementService.validerPaiement(id);
            return ResponseEntity.ok(paiement);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Rejeter un paiement
    @PostMapping("/{id}/rejeter")
    public ResponseEntity<Paiement> rejeterPaiement(@PathVariable Long id) {
        try {
            Paiement paiement = paiementService.rejeterPaiement(id);
            return ResponseEntity.ok(paiement);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Rembourser un paiement
    @PostMapping("/{id}/rembourser")
    public ResponseEntity<Paiement> rembourserPaiement(@PathVariable Long id) {
        try {
            Paiement paiement = paiementService.rembourserPaiement(id);
            return ResponseEntity.ok(paiement);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }
}
