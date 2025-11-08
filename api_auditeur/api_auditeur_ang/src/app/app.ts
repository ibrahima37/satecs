import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
//import { AdminTemplate } from './ui/admin-template/admin-template';
import { MatToolbarModule } from '@angular/material/toolbar';

@Component({
  selector: 'app-root',
  templateUrl: './app.html',
  standalone: true,
  imports: [ CommonModule, RouterModule, MatToolbarModule],
  styleUrls: ['./app.css']
})
export class App {
  protected readonly title = signal('api-audi-ang');
}
