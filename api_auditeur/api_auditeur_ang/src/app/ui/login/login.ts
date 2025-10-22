import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { UtilisateurService } from '../../services/utilisateur.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class Login implements OnInit{

  loginForm!: FormGroup;
  errorMessage: string = '';
  loading: boolean = false;

  constructor (
    private fb: FormBuilder,
    private utilisateurService: UtilisateurService,
    private router: Router
  ){}
  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      motDePasse: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  onSubmit(): void{
    if(this.loginForm.invalid) {
      this.errorMessage = 'Veuillez remplir tous les champs correctement';
      return;
    }

    this.loading = true;
    const { email, motDePasse } = this.loginForm.value;

    this.utilisateurService.login(email, motDePasse).subscribe({
      next: (response) => {
        this.loading = false;
        this.utilisateurService.setUtilisateurConnect(response);
        const formationId = response.formationId;
        this.router.navigate(['/inscriptions'], { queryParams: { formationId } });
      },
      error: (err: any) => {
        this.errorMessage = ' Identifiants incorrects ';
        this.loading = false;
      }
    });
  }
}
