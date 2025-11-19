import { Component } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { UtilisateurService } from '../../services/utilisateur.service';
import { AuthResponse } from '../../models/utilisateur';

@Component({
  selector: 'app-register-dialog',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './register-dialog.html',
  styleUrls: ['./register-dialog.css']
})
export class RegisterDialog {

  registerForm: FormGroup;
  loading: boolean = false;
  message: string = '';

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<RegisterDialog>,
    private authService: AuthService,
    private router: Router,
    private utilisateurService: UtilisateurService,
  ) {
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      motDePasse: ['', [Validators.required, Validators.minLength(6)]],
      nom: ['', Validators.required],
      prenom: ['', Validators.required]
    });
  }

  submit(): void {
  if (this.registerForm.valid) {
    const request = this.registerForm.value;

    this.authService.inscription(request).subscribe({
      next: (reponse: AuthResponse) => {
        const utilisateur = reponse.user;
        this.message = `Bienvenue ${utilisateur.nom}, votre compte a été créé !`;
        // Connexion automatique après inscription
        this.authService.connexion({
          email: request.email,
          motDePasse: request.motDePasse
        }).subscribe({
          next: (response: AuthResponse) => {
            console.log('Connexion réussie', response);
            this.loading = false;
            this.authService.handleAuthResponse(response);
            //this.utilisateurService.setUtilisateurConnect(response.utilisateur);
            //localStorage.setItem('token', response.token);
            this.router.navigate(['/admin']);
            this.dialogRef.close();
          },
          error: (err: any) => {
            console.error('Erreur lors de la connexion automatique :', err);
          }
        });
      },
      error: (err: any) => {
        console.error('Erreur lors de l’inscription :', err);
      }
    });
  }
}
}
