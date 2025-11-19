import { Component, OnInit, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { ActivatedRoute } from '@angular/router';
import { InscriptionService } from '../../services/inscription.service';
import { FormationService } from '../../services/formation.service';
import { AuthService } from '../../services/auth.service';
import { UtilisateurService } from '../../services/utilisateur.service';
import { Inscription } from '../../models/inscriptions';
import { Utilisateur } from '../../models/utilisateur';
import { ModePaiement } from '../../models/paiement';
import { InscriptionDto } from '../../models/inscriptiondto';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Observable } from 'rxjs';
import { CreateInscriptionRequest, EtatInscription } from '../../models/inscriptions';
import { Router } from '@angular/router';
import { HttpParams } from '@angular/common/http';

@Component({
  selector: 'app-inscription-detail',
  standalone: true,
  imports:[
    CommonModule,
    MatCardModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSelectModule,
    MatOptionModule],
  templateUrl: './inscription-detail.html',
  styleUrls: ['./inscription-detail.css']
})
export class InscriptionDetail implements OnInit{

  updateForm!: FormGroup;
  inscriptionId!: number;
  inscription!: InscriptionDto;
  utilisateur!: Utilisateur;
  EtatInscription = EtatInscription;
  paiements: any[] = [];

  selectedFile!: File;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private inscriptionService: InscriptionService,
    private utilisateurService: UtilisateurService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.inscriptionId = Number(this.route.snapshot.paramMap.get('id'));

    this.updateForm = this.fb.group({
      numeroCni: [{ value: '', disabled: true }],
      dateNaissance: [{ value: '', disabled: true }],
      address: [{ value: '', disabled: true }],
      numeroTel: [{ value: '', disabled: true }],
      fichier: [{ value: '', disabled: true }],
      etatInscription: ['', Validators.required],
      paiementId: [0, Validators.required]
    });

    this.inscriptionService.getInscriptionById(this.inscriptionId).subscribe(ins => {
      this.inscription = ins;

      this.updateForm.patchValue({
        numeroCni: ins.numeroCni,
        dateNaissance: ins.dateNaissance,
        address: ins.address,
        numeroTel: ins.numeroTel,
        fichier: ins.fichier,
        etatInscription: ins.etatInscription,
        paiementId: ins.paiementId ?? 0
      });

      this.utilisateurService.getUtilisateurById(ins.utilisateurId).subscribe(user => {
        this.utilisateur = user;
      });
    });

    this.inscriptionService.getPaiements().subscribe({
      next: (data) => this.paiements = data,
      error: (err : any) => console.error('Erreur chargement paiements', err)
    });
  }

  onSubmit(): void {
    if (this.updateForm.invalid) return;

    const payload = new FormData();
    payload.append('etatInscription', this.updateForm.value.etatInscription);
    payload.append('paiementId', this.updateForm.value.paiementId);
    payload.append('formationId', this.inscription.formationId.toString());
    payload.append('utilisateurId', this.inscription.utilisateurId.toString());
    payload.append('dateInscription', this.inscription.dateInscription.split('T')[0]);
    payload.append('numeroCni', this.inscription.numeroCni);
    payload.append('dateNaissance', this.inscription.dateNaissance.split('T')[0]);
    payload.append('address', this.inscription.address);
    payload.append('numeroTel', this.inscription.numeroTel);

    // si tu as un vrai fichier (File ou Blob)
    if (this.selectedFile) {
      payload.append('fichier', this.selectedFile);
    }

    this.inscriptionService.updateInscription(this.inscriptionId, payload).subscribe({
      next: (updated) => {
        console.log('Inscription mise à jour avec succès', updated);
        this.router.navigate(['/admin/inscription']);
      },
      error: (err: any) => console.error('Erreur mise à jour', err)
    });
  }
}
