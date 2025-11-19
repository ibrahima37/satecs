import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { UtilisateurService } from '../../services/utilisateur.service';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../../services/auth.service';
import { ConnexionRequest } from '../../models/utilisateur';
import { MatDialog } from '@angular/material/dialog';
import { RegisterDialog } from '../register-dialog/register-dialog';
import { ResetPasswordDialog } from '../reset-password-dialog/reset-password-dialog';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatCardModule, MatFormFieldModule, MatInputModule, MatIconModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class Login implements OnInit{

  loginForm!: FormGroup;
  errorMessage: string = '';
  loading: boolean = false;
  returnUrl = '/admin';

  constructor (
    private fb: FormBuilder,
    private router: Router,
    private utilisateurService: UtilisateurService,
    private authService: AuthService,
    private route: ActivatedRoute,
    private dialog: MatDialog
  ){}

  openRegister(): void {
    const dialogRef = this.dialog.open(RegisterDialog);

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.router.navigate(['/admin/home']); // ✅ redirection ici
      }
    });
  }

  openResetPassword(): void {
    this.dialog.open(ResetPasswordDialog);
  }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', Validators.required],
      motDePasse: ['', [Validators.required, Validators.minLength(6)]],
    });
    this.router.navigate([this.returnUrl]);
  }

  login(): void{
    console.log('Login déclenché');
    if(this.loginForm.invalid) {
      this.errorMessage = 'Veuillez remplir tous les champs correctement';
      return;
    }

    this.loading = true;
    const credentials: ConnexionRequest = this.loginForm.value;

    this.authService.connexion(credentials).subscribe({
      next: (response) => {
        console.log('Connexion réussie', response);
        this.loading = false;
        this.authService.handleAuthResponse(response);
        //this.utilisateurService.setUtilisateurConnect(response.utilisateur);
        localStorage.setItem('token', response.token);
        this.router.navigate(['/admin']);
      },
      error: (err: any) => {
        this.errorMessage = ' Identifiants incorrects ';
        this.loading = false;
      }
    });
  }
}
