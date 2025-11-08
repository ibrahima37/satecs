import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { NgChartsModule } from 'ng2-charts';
import { ChartConfiguration, ChartType } from 'chart.js';
import { DashboardService } from '../../services/dashboard.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatCardModule,
    NgChartsModule,
  ],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css']
})
export class Dashboard implements OnInit{

  // Données pour le camembert
  pieLabels: string[] = ['Formations', 'Inscriptions', 'Paiements', 'Notifications'];
  pieChartData: ChartConfiguration<'pie'>['data'] = {
    labels: ['Formations', 'Inscriptions', 'Paiements', 'Notifications'],
    datasets: [
      {
        data: [0, 0, 0, 0],
        backgroundColor: ['#42A5F5', '#66BB6A', '#FFA726', '#AB47BC']
      }
    ]
  };
  pieChartType: ChartType = 'pie';
  percentages: number[] = [0, 0, 0, 0];

// Données pour le graphique en barres
  barChartData: ChartConfiguration<'bar'>['data'] = {
    labels: ['Formations', 'Inscriptions', 'Paiements', 'Notifications'],
    datasets: [
      { data: [0, 0, 0, 0], label: 'Statistiques' }
    ]
  };

  constructor(private dashboardService: DashboardService) {}

  calculatePercentages(data: number[]): number[] {
    const total = data.reduce((sum, val) => sum + val, 0);
    if (total === 0) return [0, 0, 0, 0];
    return data.map(val => Math.round((val / total) * 100));
  }

  ngOnInit(): void {
  this.dashboardService.getStats().subscribe({
    next: (data) => {
      const values = [
        data.formations,
        data.inscriptions,
        data.paiements,
        data.notifications
      ];

      this.pieChartData.datasets[0].data = values;
      this.barChartData.datasets[0].data = values;
      this.percentages = this.calculatePercentages(values);
    },
    error: (err: any) => console.error('Erreur chargement stats', err)
  });
}


}
