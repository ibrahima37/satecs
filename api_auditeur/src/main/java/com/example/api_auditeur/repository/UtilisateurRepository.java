package com.example.api_auditeur.repository;

import com.example.api_auditeur.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    @Query("SELECT u FROM Utilisateur u WHERE u.email = :email AND u.motDePasse = :motDePasse")
    Optional<Utilisateur> findByEmailAndMotDePass(@Param("email") String email, @Param("motDePasse") String motDePasse);

    Optional<Utilisateur> findByEmail(String email);
}
