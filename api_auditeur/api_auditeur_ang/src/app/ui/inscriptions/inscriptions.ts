import { Component, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { InscriptionService } from '../../services/inscription.service';
import { FormationService } from '../../services/formation.service';
import { InscriptionRequest } from '../../models/utilisateur';
import { Formation } from '../../models/formation';
import { CreateInscriptionRequest } from '../../models/inscriptions';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';


@Component({
  selector: 'app-inscriptions',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatCardModule, MatSelectModule, MatInputModule ],
  templateUrl: './inscriptions.html',
  styleUrls: ['./inscriptions.css']
})

export class Inscriptions implements OnInit{

  loginForm: FormGroup;
  formations: Formation[] = [];

  constructor(
    private fb: FormBuilder,
    private formationService: FormationService,
    private inscriptionService: InscriptionService,
    private auth: AuthService
  ) {
    this.loginForm = this.fb.group({
      prenom: [''],
      nom: [''],
      tel: [''],
      email: [''],
      motDePasse: [''],
      formationId: [null],
      utilisateurId: [null]
    });
  }

  ngOnInit(): void {
    const utilisateurId = this.auth.getCurrentUser(); // méthode à créer si nécessaire
    this.loginForm.patchValue({ utilisateurId });

    this.formationService.getAllFormations().subscribe({
      next: (data: any) => this.formations = data,
      error: (err: any) => console.error('Erreur chargement formations', err)
    });
  }

  inscrire(): void {
    const inscriptionData = this.loginForm.value;
    this.inscriptionService.inscrire(inscriptionData).subscribe({
      next: () => console.log('Inscription réussie'),
      error: (err: any) => console.error('Erreur inscription', err)
    });
  }
}
