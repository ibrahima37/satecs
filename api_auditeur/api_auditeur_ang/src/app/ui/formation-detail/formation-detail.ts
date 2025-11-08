import { Component, OnInit } from '@angular/core';
//import { Formation } from '../../models/formation';
//import { FormationService } from '../../services/formation.service';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
//import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-formation-detail',
  standalone: true,
  imports: [CommonModule, MatCardModule],
  templateUrl: './formation-detail.html',
  styleUrls: ['./formation-detail.css'],
})
export class FormationDetail implements OnInit{

  ngOnInit(): void{}

}
