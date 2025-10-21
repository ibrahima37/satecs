package com.example.api_auditeur.service;

import com.example.api_auditeur.model.Formation;
import com.example.api_auditeur.repository.FormationRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @AllArgsConstructor
@Data
public class FormationService {

    private FormationRepository formRepo;

    public Formation creerFormation(Formation form){

        Formation formation = new Formation();

        formation.setTitre(form.getTitre());
        formation.setCapacite(form.getCapacite());
        formation.setNiveauFormation(form.getNiveauFormation());
        formation.setDescription(form.getDescription());
        formation.setDateDebut(form.getDateDebut());
        formation.setDeteFin(form.getDeteFin());
        formation.setTarif(form.getTarif());
        formation.setTypeFormation(form.getTypeFormation());
        formation.setDateCreation(form.getDateCreation());
        formation.setDateModification(form.getDateModification());

        return  formRepo.save(formation);
    }

    public List<Formation> liteFormatins(){
        return formRepo.findAll();
    }

    public Formation afficheUneFormationSpecifique(Long id) {
        return  formRepo.findById(id).orElseThrow();
    }
}
