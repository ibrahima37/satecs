import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.html',
  standalone: true,
  imports: [CommonModule, RouterModule],
  styleUrls: ['./app.css']
})
export class App {
  protected readonly title = signal('api-audi-ang');
}
