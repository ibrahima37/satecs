package com.example.api_auditeur.model;

import com.example.api_auditeur.model.page_enum.CanalNotification;
import com.example.api_auditeur.model.page_enum.TypeFormation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Notification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String objet;
    private String contenu;

    @Enumerated(EnumType.STRING)
    private TypeFormation type;
    private LocalDate dateEnvoi;
    private Boolean envoyee;
    private String destinataire;

    @Enumerated(EnumType.STRING)
    private CanalNotification canalNotification;
}
