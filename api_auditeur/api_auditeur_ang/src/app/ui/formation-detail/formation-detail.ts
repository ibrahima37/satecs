import { Component, OnInit } from '@angular/core';
import { Formation } from '../../models/formation';
import { FormationService } from '../../services/formation.service';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-formation-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './formation-detail.html',
  styleUrls: ['./formation-detail.css'],
})
export class FormationDetail implements OnInit{
  formation!: Formation;
  isLoading: boolean = true;
  errorMessage: string = '';

  constructor(
    private route: ActivatedRoute,
    private formationService: FormationService,
    private router: Router
    ){}

  ngOnInit(): void{
    const id = Number(this.route.snapshot.paramMap.get('id'));
    console.log('Formation ID :', id);
    this.formationService.getFormationById(id!).subscribe({
      next: (data: any) => {
        console.log('Formation data :', data);
        console.log('Date Fin:', data.dateFin);
        this.formation = data;
        this.isLoading = false;
      },
      error: (err: any)=> {
        console.error('Erreur Complete :', err);
        this.errorMessage = "Erreur lors de chargement de la formation";
        console.error(err);
        this.isLoading = false;
      }
    });
  }
  inscrire(): void{
    this.router.navigate(['/inscriptions'], {queryParams: {formationId: this.formation.id} });
  }

}
