import { Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators, FormGroup } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { UtilisateurService } from '../../services/utilisateur.service';
import { InscriptionService } from '../../services/inscription.service';
import { FormationService } from '../../services/formation.service';
import { Formation, NiveauFormation } from '../../models/formation';
import { ModePaiement } from '../../models/paiement';
import { EtatInscription } from '../../models/inscriptions';

@Component({
  selector: 'app-inscriptions',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './inscriptions.html',
  styleUrls: ['./inscriptions.css']
})

export class Inscriptions implements OnInit{

  form!: FormGroup;

  etatInscriptionOptions = Object.values(EtatInscription);
  niveauFormationOptions = Object.values(NiveauFormation);
  paiement = Object.values(ModePaiement);

  formation!: Formation;
  errorMessage: string = '';
  successMessage: string = '';
  recapitulatif: any = null;
  isAuthentificated: boolean = false;
  currentUtilisateur: any;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private utilisateurService: UtilisateurService,
    private inscriptionService: InscriptionService,
    private formationService: FormationService
  ){}

  ngOnInit(): void{
    this.checkAutentification();

    this.form = this.fb.group({

    nom: ['', Validators.required],
    prenom: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    telephone: ['', Validators.required],

    formationId: ['', Validators.required],
    utilisateurId: ['', Validators.required],
    dateInscription: [this.getToday(), Validators.required],
    etatInscription: ["VALIDER", Validators.required],
    niveauFormation: ['DEBUTANT', Validators.required],
    paiement: ['', Validators.required]
    });

    const formationId = this.route.snapshot.queryParamMap.get('formationId');
    const utilisateur = this.utilisateurService.getUtilisateurConnect();

    if(formationId){
      this.form.patchValue({ formationId });
      this.formationService.getFormationById(+formationId).subscribe({
        next: (data: Formation) => {
          this.formation = data;
        },
        error: (err: any) => {
          this.errorMessage = 'Erreur : ' + (err?.message ?? 'Erreur inconnue');
        }
      });
    }

    if(utilisateur?.id){
      this.form.patchValue({ utilisateurId: utilisateur.id });
    } else {
      this.errorMessage = 'Utilisateur non connecté';
    }
  }

  checkAutentification(): void{
    this.utilisateurService.isLoggedIn().subscribe(isLoggedIn => {
      this.isAuthentificated = isLoggedIn;
      if(isLoggedIn){
        this.utilisateurService.getCurrentUtilisateur().subscribe(utilisateur =>{
          this.currentUtilisateur = utilisateur;
          this.form.patchValue({ utilisateurId: utilisateur.id});
        });
      }
    });
  }

  //Redirige vers la page connexion
  redirectToLogin(): void{
    this.router.navigate(['/login'], {
      queryParams: {returnUrl: this.router.url}
    });
  }

  getToday(): string{
    const today = new Date();
    return today.toISOString().slice(0, 10);
  }

  soumettre(): void{
    if(this.form.invalid){
      this.errorMessage = "Formulaire Invalide";
      return;
    }
      if (!this.isAuthentificated) {
      this.errorMessage = 'Vous devez être connecté pour vous inscrire.';
      return;
      }

    this.inscriptionService.soumettreInscription(this.form.value).subscribe({
      next: (data: any) => {
        this.successMessage = 'Inscription Enregistrée';
        this.recapitulatif = this.form.value;
      },
      error: (err: any) => {
        this.errorMessage = 'Erreur : ' +(err?.message ?? 'Erreur inconnue');
      }
    });
  }
}
