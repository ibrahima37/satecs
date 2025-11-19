package com.example.api_auditeur.controller;

import com.example.api_auditeur.dto.FormationDto;
import com.example.api_auditeur.model.page_enum.NiveauFormation;
import com.example.api_auditeur.model.page_enum.TypeFormation;
import com.example.api_auditeur.service.FormationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/formation")
@RestController @AllArgsConstructor @Data
public class FormationController {

    private FormationService formationService;
    // CREATE
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<FormationDto> creerFormation(@Valid @RequestBody FormationDto formationDto) {
        try {
            FormationDto created = formationService.creerFormation(formationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // READ - Get by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('AUDITEUR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<FormationDto> getFormationById(@PathVariable Long id) {
        try {
            FormationDto formation = formationService.getFormationById(id);
            return ResponseEntity.ok(formation);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // READ - Get All
    @GetMapping
    @PreAuthorize("hasAnyAuthority('AUDITEUR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<FormationDto>> getAllFormations() {
        List<FormationDto> formations = formationService.getAllFormations();
        return ResponseEntity.ok(formations);
    }

    // UPDATE
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<FormationDto> modifierFormation(
            @PathVariable Long id,
            @Valid @RequestBody FormationDto formationDto) {
        try {
            FormationDto updated = formationService.modifierFormation(id, formationDto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Void> supprimerFormation(@PathVariable Long id) {
        try {
            formationService.supprimerFormation(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // RECHERCHES AVANCÉES

    // Filtrer par type
    @GetMapping("/type/{type}")
    @PreAuthorize("hasAnyAuthority('AUDITEUR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<FormationDto>> getFormationsByType(@PathVariable TypeFormation type) {
        List<FormationDto> formations = formationService.getFormationsByType(type);
        return ResponseEntity.ok(formations);
    }

    // Filtrer par niveau
    @GetMapping("/niveau/{niveau}")
    @PreAuthorize("hasAnyAuthority('AUDITEUR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<FormationDto>> getFormationsByNiveau(@PathVariable NiveauFormation niveau) {
        List<FormationDto> formations = formationService.getFormationsByNiveau(niveau);
        return ResponseEntity.ok(formations);
    }

    // Formations à venir
    @GetMapping("/a-venir")
    @PreAuthorize("hasAnyAuthority('AUDITEUR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<FormationDto>> getFormationsAVenir() {
        List<FormationDto> formations = formationService.getFormationsAVenir();
        return ResponseEntity.ok(formations);
    }

    // Recherche par mot-clé
    @GetMapping("/recherche")
    @PreAuthorize("hasAnyAuthority('AUDITEUR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<FormationDto>> rechercherFormations(@RequestParam String motCle) {
        List<FormationDto> formations = formationService.rechercherFormations(motCle);
        return ResponseEntity.ok(formations);
    }

    // Filtrer par tarif maximum
    @GetMapping("/tarif-max")
    @PreAuthorize("hasAnyAuthority('AUDITEUR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<FormationDto>> getFormationsByTarifMax(@RequestParam Double tarifMax) {
        List<FormationDto> formations = formationService.getFormationsByTarifMax(tarifMax);
        return ResponseEntity.ok(formations);
    }

   /* @GetMapping
    @PreAuthorize("hasAnyRole('AUDITEUR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<Formation>> liteFormation(){
        List<Formation> formations = formationService.liteFormatins();
        if (formations == null || formations.isEmpty()){
            return ResponseEntity.ok((Collections.emptyList()));
        }
        return ResponseEntity.ok(formations);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('AUDITEUR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Formation> uneFormation(@PathVariable Long id) {
        return ResponseEntity.ok(formationService.afficheUneFormationSpecifique(id));
    }*/
}
