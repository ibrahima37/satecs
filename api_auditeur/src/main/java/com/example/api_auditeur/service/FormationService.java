package com.example.api_auditeur.service;

import com.example.api_auditeur.dto.FormationDto;
import com.example.api_auditeur.model.Formation;
import com.example.api_auditeur.model.page_enum.NiveauFormation;
import com.example.api_auditeur.model.page_enum.TypeFormation;
import com.example.api_auditeur.repository.FormationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service @AllArgsConstructor
@Data
public class FormationService {

    private FormationRepository formRepo;

    public FormationDto creerFormation(FormationDto formationDto){

        if (formationDto.getDateFin().isBefore(formationDto.getDateDebut())) {
            throw new RuntimeException("La date de fin doit être après la date de début");
        }

        Formation formation = new Formation();

        formation.setTitre(formationDto.getTitre());
        formation.setCapacite(formationDto.getCapacite());
        formation.setNiveauFormation(formationDto.getNiveauFormation());
        formation.setDescription(formationDto.getDescription());
        formation.setDateDebut(formationDto.getDateDebut());
        formation.setDeteFin(formationDto.getDateFin());
        formation.setTarif(formationDto.getTarif());
        formation.setTypeFormation(formationDto.getTypeFormation());
        formation.setDateCreation(formationDto.getDateCreation());
        formation.setDateModification(formationDto.getDateModification());

        Formation saved = formRepo.save(formation);

        return convertToDto(saved);
    }

    // READ - Get by ID
    public FormationDto getFormationById(Long id) {
        Formation formation = formRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Formation non trouvée avec l'id: " + id));
        return convertToDto(formation);
    }

    // READ - Get All
    public List<FormationDto> getAllFormations() {
        return formRepo.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // UPDATE
    @Transactional
    public FormationDto modifierFormation(Long id, FormationDto formationDto) {
        Formation formation = formRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Formation non trouvée avec l'id: " + id));

        // Validation
        if (formationDto.getDateFin().isBefore(formationDto.getDateDebut())) {
            throw new RuntimeException("La date de fin doit être après la date de début");
        }

        formation.setTitre(formationDto.getTitre());
        formation.setDescription(formationDto.getDescription());
        formation.setDateDebut(formationDto.getDateDebut());
        formation.setDeteFin(formationDto.getDateFin());
        formation.setCapacite(formationDto.getCapacite());
        formation.setTarif(formationDto.getTarif());
        formation.setTypeFormation(formationDto.getTypeFormation());
        formation.setNiveauFormation(formationDto.getNiveauFormation());
        formation.setDateModification(LocalDate.now());

        Formation updated = formRepo.save(formation);
        return convertToDto(updated);
    }

    // DELETE
    @Transactional
    public void supprimerFormation(Long id) {
        if (!formRepo.existsById(id)) {
            throw new RuntimeException("Formation non trouvée avec l'id: " + id);
        }
        formRepo.deleteById(id);
    }

    // RECHERCHES AVANCÉES
    public List<FormationDto> getFormationsByType(TypeFormation type) {
        return formRepo.findByTypeFormation(type).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<FormationDto> getFormationsByNiveau(NiveauFormation niveau) {
        return formRepo.findByNiveauFormation(niveau).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<FormationDto> getFormationsAVenir() {
        return formRepo.findFormationsAVenir().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<FormationDto> rechercherFormations(String motCle) {
        return formRepo.rechercherParMotCle(motCle).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<FormationDto> getFormationsByTarifMax(Double tarifMax) {
        return formRepo.findByTarifLessThanEqual(tarifMax).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private FormationDto convertToDto(Formation formation) {
        FormationDto dto = new FormationDto();
        dto.setId(formation.getId());
        dto.setTitre(formation.getTitre());
        dto.setDescription(formation.getDescription());
        dto.setDateDebut(formation.getDateDebut());
        dto.setDateFin(formation.getDeteFin());
        dto.setCapacite(formation.getCapacite());
        dto.setTarif(formation.getTarif());
        dto.setTypeFormation(formation.getTypeFormation());
        dto.setNiveauFormation(formation.getNiveauFormation());
        dto.setDateCreation(formation.getDateCreation());
        dto.setDateModification(formation.getDateModification());

        // Calculer le nombre d'inscrits si la liste existe
        if (formation.getInscriptionList() != null) {
            dto.setNombreInscrits(formation.getInscriptionList().size());
        } else {
            dto.setNombreInscrits(0);
        }

        return dto;
    }

    public List<Formation> liteFormatins(){
        return formRepo.findAll();
    }

    public Formation afficheUneFormationSpecifique(Long id) {
        return  formRepo.findById(id).orElseThrow();
    }
}
