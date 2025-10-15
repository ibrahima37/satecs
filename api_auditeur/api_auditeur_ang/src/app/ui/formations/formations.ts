import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {FormationService } from '../../services/formation.service';
import { Formation } from '../../models/formation';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-formations',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './formations.html',
  styleUrls: ['./formations.css']
})
export class Formations implements OnInit{

public formations: Formation[] = [];

constructor(private formationService: FormationService) {}

  ngOnInit(): void {
    this.formationService.getFormations().subscribe({
      next: (data: Formation[]) => this.formations = data,
      error: (err: any) => console.error(err)
    });
  }
}


