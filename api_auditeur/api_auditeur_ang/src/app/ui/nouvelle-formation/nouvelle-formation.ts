import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators} from '@angular/forms';
import { FormationService } from '../../services/formation.service';
import { Router } from '@angular/router';
import { Formation } from '../../models/formation';

@Component({
  selector: 'app-nouvelle-formation',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule
],
  templateUrl: './nouvelle-formation.html',
  styleUrls: ['./nouvelle-formation.css']
})
export class NouvelleFormation implements OnInit {

  formationForm!: FormGroup;
  private formatDate(date: string | Date): string {
    return new Date(date).toISOString().slice(0, 10); // yyyy-MM-dd
  }

  constructor(
    private fb: FormBuilder,
    private formationService: FormationService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.formationForm = this.fb.group({
      titre: ['', Validators.required],
      description: ['', Validators.required],
      dateDebut: ['', Validators.required],
      dateFin: ['', Validators.required],
      capacite: [0, [Validators.required, Validators.min(1)]],
      tarif: [0, [Validators.required, Validators.min(0)]],
      typeFormation: ['', Validators.required],
      niveauFormation: ['', Validators.required],
    });
  }

  submit(): void {
    console.log('Formulaire valide ?', this.formationForm.valid);
    if (this.formationForm.valid) {
      const raw = this.formationForm.value;
      const formation: Formation = {
        ...raw,
        dateDebut: this.formatDate(raw.dateDebut),
        dateFin: this.formatDate(raw.dateFin),
        dateCreation: this.formatDate(new Date()),
        dateModification: this.formatDate(new Date())
      };console.log('Payload envoyé :', formation);

      this.formationService.creerFormation(formation).subscribe({
        next: (created) => {
          console.log('Formation créée :', created);
          this.router.navigate(['/admin/formations']);
        },
        error: (err) => {
          console.error('Erreur création formation', err);
        }
      });
    }
  }

}
