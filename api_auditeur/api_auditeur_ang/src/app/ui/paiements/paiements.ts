import { Component, OnInit } from '@angular/core';
import { PaiementService } from '../../services/paiement.service';
import { Paiement, StatutPaiement, ModePaiement } from '../../models/paiement';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-paiements',
  standalone: true,
  imports: [ CommonModule, MatCardModule ],
  templateUrl: './paiements.html',
  styleUrls: ['./paiements.css']
})
export class Paiements implements OnInit{

  paiements: Paiement[] = [];

  constructor(private http : HttpClient, private paiementService : PaiementService){}

  ngOnInit(): void {
    this.loadPaiements();
  }

  loadPaiements(): void {
    this.paiementService.getAllPaiements().subscribe({
      next: (data: Paiement[]) => {
        this.paiements = data;
      },
      error: (err: any) => {
        console.error('Erreur chargement paiements', err);
      }
    });
  }
}

