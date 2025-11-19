import { Component, OnInit, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { InscriptionService } from '../../services/inscription.service';
import { UtilisateurService } from '../../services/utilisateur.service';
import { CreateInscriptionRequest, EtatInscription } from '../../models/inscriptions';
import { Utilisateur } from '../../models/utilisateur';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';

@Component({
  selector: 'app-inscription-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSelectModule,
    MatOptionModule
  ],
  templateUrl: './inscription-dialog.html',
  styleUrls: ['./inscription-dialog.css']
})
export class InscriptionDialog implements OnInit{

  EtatInscription = EtatInscription;

  registerForm!: FormGroup;
  loading: boolean = false;
  message: string = '';

  selectedFormationId!: number;
  uploadedFile?: File;

  currentUser$!: Observable<Utilisateur | null>;

  onFileSelected(event: any): void {
    this.uploadedFile = event.target.files[0];
  }

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<InscriptionDialog>,
    private authService: AuthService,
    private router: Router,
    private inscriptionService: InscriptionService,
    private utilisateurService: UtilisateurService,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {this.selectedFormationId = data.formationId; }

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      numeroCni: ['', Validators.required],
      dateNaissance: ['', Validators.required],
      address: ['', Validators.required],
      numeroTel: ['', Validators.required],
      fichier: [''],
      etatInscription: [EtatInscription.DOCUMENTS_EN_ATTENTE, Validators.required]
    });

    this.currentUser$ = this.authService.getCurrentUser$();
  }

  submitInscription(): void {
    if (this.registerForm.invalid) {
      console.warn('Formulaire invalide, inscription non envoyée');
      return;
    }

    this.currentUser$.subscribe(user => {
      if (!user|| user.id == null) {
        console.error("Utilisateur non connecté");
        return;
      }

      const request: CreateInscriptionRequest = {
        formationId: this.selectedFormationId,
        utilisateurId: user.id,
        paiementId: 0,
        etatInscription: this.registerForm.value.etatInscription,
        numeroCni: this.registerForm.value.numeroCni,
        dateNaissance: this.registerForm.value.dateNaissance,
        address: this.registerForm.value.address,
        numeroTel: this.registerForm.value.numeroTel,
        fichier: this.uploadedFile,
        dateInscription: new Date().toISOString().substring(0, 10)
      };

      console.log('Envoi de l’inscription avec payload :', request);

      this.inscriptionService.inscrire(request).subscribe({
        next: (inscription) => {
          console.log('Inscription créée avec succès', inscription);
          this.dialogRef.close(inscription);
        },
        error: (err: any) => {
          console.error('Erreur lors de la création de l’inscription', err);
        }
      });
    });
  }
}
