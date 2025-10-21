import { Component, OnInit } from '@angular/core';
import { NotificationService } from '../../services/notification.service';
import { NotificationApp, CanalNotification } from '../../models/notification';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-notifications',
  standalone: false,
  templateUrl: './notifications.html',
  styleUrls: ['./notifications.css']
})
export class Notifications implements OnInit{

notifications: Notification[] = [];
isLoading = true;
errorMessage = '';

constructor(private notificationService: NotificationService) {}

  ngOnInit(): void {
    this.loadNotifications();
  }

  loadNotifications(): void {
    this.notificationService.getNotification().subscribe({
      next: (data: any) => {
        this.notifications = data;
        this.isLoading = false;
      },
      error: (error: any) => {
        this.errorMessage = error.message;
        this.isLoading = false;
      }
    });
  }

  envoyerNotification(id: number): void {
    this.notificationService.envoyerNotification(id).subscribe({
      next: () => {
        console.log('Notification envoyée');
        this.loadNotifications();
      },
      error: (error) => console.error(error)
    });
  }

}
