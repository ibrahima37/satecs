package com.example.api_auditeur.dto;

import com.example.api_auditeur.model.page_enum.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private Long id;
    @NotNull(message = "L'ID de la formation est obligatoire")
    private String email;
    @NotNull(message = "L'ID de la formation est obligatoire")
    private String nom;
    @NotNull(message = "L'ID de la formation est obligatoire")
    private String prenom;
    @NotNull(message = "L'ID de la formation est obligatoire")
    private String motDePasse;

    private Role role;
}
