import { Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
//import { Router } from '@angular/router';
//import { UtilisateurService } from '../../services/utilisateur.service';
//import { Utilisateur } from '../../models/utilisateur';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrls: ['./register.css']
})
export class Register implements OnInit{

  ngOnInit(): void{}

}
