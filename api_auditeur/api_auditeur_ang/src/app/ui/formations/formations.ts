import { Component, OnInit } from '@angular/core';
import { FormationService } from '../../services/formation.service';
import { AuthService } from '../../services/auth.service';
import { Formation } from '../../models/formation';
import { Utilisateur } from '../../models/utilisateur';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { InscriptionDialog } from '../inscription-dialog/inscription-dialog';
import { Observable } from 'rxjs';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-formations',
  standalone: true,
  imports: [CommonModule, RouterModule, MatCardModule, MatIconModule],
  templateUrl: './formations.html',
  styleUrls: ['./formations.css']
})
export class Formations implements OnInit{

  currentUser$!: Observable<Utilisateur | null>;

  constructor(
    private formationService: FormationService,
    private router: Router,
    private authService: AuthService,
    private dialog: MatDialog) {}

    formations: Formation[] = [];
    allFormations: Formation[] = [];
    searchTerm = '';

  ngOnInit() {
    this.loadFormations();
    this.currentUser$ = this.authService.getCurrentUser$();
  }

  hasRequiredRole(user: Utilisateur | null): boolean {
    return user?.role === 'ADMIN' || user?.role === 'SUPER_ADMIN';
  }

  loadFormations() {
    this.formationService.getAllFormations().subscribe({
      next: (data) => {
        this.formations = data;
        this.allFormations = data;
      }
    });
  }

  onSearch() {
    if (this.searchTerm.trim()) {
      this.formationService.rechercherFormations(this.searchTerm).subscribe({
        next: (data) => this.formations = data
      });
    } else {
      this.formations = this.allFormations;
    }
  }

  inscrireFormation(formationId: number): void {
    console.log('Ouverture du dialog pour formation', formationId);
    this.dialog.open(InscriptionDialog, {
      width: '600px',
      data: { formationId }
    });
  }

  inscrire(id: number): void {
    this.router.navigate(['/admin/inscription'], { queryParams: { formationId: id } });
  }
}
