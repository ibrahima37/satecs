import { Component, OnInit } from '@angular/core';
import { PaiementService } from '../../services/paiement.service';
import { Paiement, StatutPaiement, ModePaiement } from '../../models/paiement';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-paiements',
  standalone: true,
  templateUrl: './paiements.html',
  styleUrls: ['./paiements.css']
})
export class Paiements implements OnInit{

paiements: Paiement[] = [];
isLoading = true;
errorMessage = '';

constructor(private paiementService: PaiementService) {}

  ngOnInit(): void {
    this.loadPaiements();
  }

  loadPaiements(): void {
    this.paiementService.getPaiements().subscribe({
      next: (data) => {
        this.paiements = data;
        this.isLoading = false;
      },
      error: (error) => {
        this.errorMessage = error.message;
        this.isLoading = false;
      }
    });
  }

  validerPaiement(id: number): void {
    this.paiementService.updateStatutPaiement(id, StatutPaiement.VALIDE).subscribe({
      next: () => {
        console.log('Paiement validé');
        this.loadPaiements();
      },
      error: (error) => console.error(error)
    });
  }

}
