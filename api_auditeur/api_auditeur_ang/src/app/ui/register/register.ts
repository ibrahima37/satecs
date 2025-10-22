import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { UtilisateurService } from '../../services/utilisateur.service';
import { Utilisateur } from '../../models/utilisateur';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrls: ['./register.css']
})
export class Register implements OnInit{

  registerForm!: FormGroup;
  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private fb: FormBuilder,
    private utilisateurService: UtilisateurService,
    private router: Router
  ){}

  ngOnInit(): void{
    this.registerForm = this.fb.group({
      nom: ['', Validators.required],
      prenom: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      motDePasse: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  onSubmit(): void{
    if(this.registerForm.invalid){
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const utilisateur: Utilisateur = this.registerForm.value;

    this.utilisateurService.inscription(utilisateur).subscribe({
      next: (response) => {
        this.loading = false;
        this.successMessage = 'Inscription reussie !';

        setTimeout(() =>{
          this.router.navigate(['/formation']);
        }, 2000)
      },
      error: (error) => {
        this.loading = false;
        this.errorMessage = error.error?.message || 'Erreur lors de l\'inscription';
      }
    });
  }

}
