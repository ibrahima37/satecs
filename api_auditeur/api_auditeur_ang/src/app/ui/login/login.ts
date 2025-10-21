import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UtilisateurService } from '../../services/utilisateur.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class Login {

  loginForm: FormGroup;
  errorMessage: string = '';
  loading: boolean = false;

  constructor (
    private fb: FormBuilder,
    private utilisateurService: UtilisateurService,
    private router: Router
  ){
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      motDePasse: ['', Validators.required],
    });
  }

  onSublit(): void{
    if(this.loginForm.invalid) return;

    this.loading = true;
    const { email, motDePasse } = this.loginForm.value;

    this.utilisateurService.login(email, motDePasse).subscribe({
      next: () => {
        const returnUrl = this.router.parseUrl(this.router.url).queryParams['returnUrl'] || '/formation';
        this.router.navigateByUrl(returnUrl);
      },
      error: (err: any) => {
        this.errorMessage = ' Identifiants incorrects ';
        this.loading = false;
      }
    });
  }

}
