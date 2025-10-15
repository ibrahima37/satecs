package com.example.api_auditeur.controller;

import com.example.api_auditeur.model.Formation;
import com.example.api_auditeur.service.FormationService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RequestMapping("/api/formation")
@RestController @AllArgsConstructor @Data
@CrossOrigin("*")
public class FormationController {

    private FormationService formService;

    // @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Formation> crerFormation(@RequestBody Formation formation){
        Formation form = formService.creerFormation(formation);

       // Formation response = new Formation();
        return ResponseEntity.status(HttpStatus.CREATED).body(form);
    }

    @GetMapping
    public ResponseEntity<List<Formation>> liteFormation(){
        List<Formation> formations = formService.liteFormatins();
        if (formations == null || formations.isEmpty()){
            return ResponseEntity.ok((Collections.emptyList()));
        }
        return ResponseEntity.ok(formations);
    }
}
