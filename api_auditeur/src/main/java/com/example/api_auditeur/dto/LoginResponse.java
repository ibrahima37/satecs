package com.example.api_auditeur.dto;

import com.example.api_auditeur.model.Utilisateur;
import com.example.api_auditeur.model.page_enum.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String token;
    private Utilisateur user;

//    public LoginResponse(UserDetails userDetails) {
//        this.user = new Utilisateur();
//        this.user.setEmail(userDetails.getUsername());
//
//        if (!userDetails.getAuthorities().isEmpty()) {
//            this.user.setRole(Role.valueOf(userDetails.getAuthorities().iterator().next().getAuthority()));
//        }
//
//        this.token = null; // ou autre logique
//    }
}

