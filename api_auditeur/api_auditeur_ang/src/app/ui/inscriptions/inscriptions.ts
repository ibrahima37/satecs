import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, FormsModule } from '@angular/forms';
import { Formation } from '../../models/formation';
import { Inscription } from '../../models/inscriptions';
import { FormationService } from '../../services/formation.service';
import { InscriptionService } from '../../services/inscription.service';
import { AuthService } from '../../services/auth.service';
import { CreateInscriptionRequest, EtatInscription } from '../../models/inscriptions';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';

@Component({
selector: 'app-inscriptions',
standalone: true,
imports: [
  CommonModule,
  HttpClientModule,
  FormsModule,
  ReactiveFormsModule,
  MatPaginator,
  MatTableModule,
  MatCardModule,
  MatFormFieldModule,
  MatInputModule,
  MatIconModule,
  MatSelectModule,
  MatButtonModule
],
templateUrl: './inscriptions.html',
styleUrls: ['./inscriptions.css']
})
export class Inscriptions implements OnInit {

  inscriptions: Inscription[] = [];
  dataSource = new MatTableDataSource<Inscription>();
  totalInscriptions = 0;
  displayedColumns: string[] = [
    'id',
    'dateInscription',
    'etatInscription',
    'formationId',
    'paiementId',
    'utilisateurId',
    'numeroCni',
    'address',
    'dateNaissance',
    'fichier',
    'numeroTel',
    'actions'
  ];


  @ViewChild(MatPaginator) paginator!: MatPaginator;


  constructor(
    private inscriptionService: InscriptionService,
    public router: Router
  ) {}

  ngOnInit(): void {
    this.loadInscriptions();
  }

  loadInscriptions(): void {
    this.inscriptionService.getAllInscriptions().subscribe({
      next: (data: Inscription[]) => {
        const sortedData = data.sort((a, b) => a.id - b.id);

        this.inscriptions = sortedData;
        this.totalInscriptions = sortedData.length;
        this.dataSource = new MatTableDataSource<Inscription>(sortedData);
        this.dataSource.paginator = this.paginator;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des inscriptions', err);
      }
    });
  }
}
