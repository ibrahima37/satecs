import { Component, OnInit } from '@angular/core';
import { FormationService } from '../../services/formation.service';
import { Formation } from '../../models/formation';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { Router } from '@angular/router';

@Component({
  selector: 'app-formations',
  standalone: true,
  imports: [CommonModule, RouterModule, MatCardModule],
  templateUrl: './formations.html',
  styleUrls: ['./formations.css']
})
export class Formations implements OnInit{

  constructor(
    private formationService: FormationService,
    private router: Router) {}

  formations: Formation[] = [];
  allFormations: Formation[] = [];
  searchTerm = '';

  ngOnInit() {
    this.loadFormations();
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

  inscrire(id: number): void {
    this.router.navigate(['/admin/inscription'], { queryParams: { formationId: id } });
  }
}
