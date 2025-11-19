import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatCardModule } from '@angular/material/card';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { Utilisateur } from '../../models/utilisateur';
import { Observable } from 'rxjs';
import { MatDrawer } from '@angular/material/sidenav';

@Component({
  selector: 'app-admin-template',
  standalone: true,
  imports: [ CommonModule,
      MatToolbarModule,
      MatButtonModule,
      MatIconModule,
      MatMenuModule,
      MatSidenavModule,
      MatListModule,
      RouterModule,
      MatCardModule
  ],
  templateUrl: './admin-template.html',
  styleUrls: ['./admin-template.css']
})
export class AdminTemplate implements OnInit{
  utilisateur: Utilisateur | null = null;
  currentUser$!: Observable<Utilisateur | null>;

@ViewChild('myDrawer') myDrawer!: MatDrawer;

toggleDrawer(): void {
    this.myDrawer.toggle();
  }

  constructor(
    public authService : AuthService,
  ){}

  ngOnInit(): void {
    this.authService.reloadUser();
    this.currentUser$ = this.authService.getCurrentUser$();
    this.authService.getCurrentUser$().subscribe(user => {
      this.utilisateur = user;
      console.log('Utilisateur connecté :', this.utilisateur);
    });
  }

  hasRequiredRole(user: Utilisateur | null): boolean {
    return user?.role === 'ADMIN' || user?.role === 'SUPER_ADMIN';
  }

  logout() {
    this.authService.logout();
  }

}
