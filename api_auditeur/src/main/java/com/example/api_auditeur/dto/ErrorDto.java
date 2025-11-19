package com.example.api_auditeur.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Setter @Getter
public class ErrorDto {
    private String titre;
    private String message;
}
