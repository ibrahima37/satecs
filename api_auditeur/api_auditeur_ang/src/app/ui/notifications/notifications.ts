import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { NotificationService } from '../../services/notification.service';
import { NotificationApp } from '../../models/notification';

@Component({
  selector: 'app-notifications',
  standalone: true,
  imports: [ CommonModule, MatCardModule ],
  templateUrl: './notifications.html',
  styleUrls: ['./notifications.css']
})
export class Notifications implements OnInit{

  notifications: NotificationApp[] = [];
  errorMessage = '';

  constructor(private notificationService: NotificationService) {}

  ngOnInit(): void{
    this.notificationService.getAllNotifications().subscribe({
      next: (data) => this.notifications = data,
      error: (err) => this.errorMessage = 'Erreur : ' + (err?.message ?? 'Erreur inconnue')
    });
  }

}
