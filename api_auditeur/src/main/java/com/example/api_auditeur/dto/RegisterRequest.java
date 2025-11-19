package com.example.api_auditeur.dto;

import com.example.api_auditeur.model.page_enum.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private Long id;
    @NotNull(message = "Le mail est obligatoire")
    private String email;
    @NotNull(message = "Le nom est obligatoire")
    private String nom;
    @NotNull(message = "Le prenom est obligatoire")
    private String prenom;
    @NotNull(message = "Le mot de passe est obligatoire")
    private String motDePasse;


    private Role role;
}
