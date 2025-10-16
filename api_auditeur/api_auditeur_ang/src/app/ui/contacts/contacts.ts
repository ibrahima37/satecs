import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import * as L from 'leaflet';
import { AfterViewInit } from '@angular/core';

@Component({
  selector: 'app-contacts',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './contacts.html',
  styleUrls: ['./contacts.css']
})
export class Contacts implements AfterViewInit{
  contactForm: FormGroup;

  constructor(private fb: FormBuilder){
    this.contactForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      subject: ['', Validators.required],
      message: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

  onSubmit(){
    if(this.contactForm.valid){
      console.log('Message envoyé :', this.contactForm.value);
      this.contactForm.reset();
    }
  }
  ngAfterViewInit(): void {
  const mapInstance = L.map('map').setView([14.6928, -17.4467], 13); // Dakar

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors'
  }).addTo(mapInstance);

  L.marker([14.6928, -17.4467])
    .addTo(mapInstance)
    .bindPopup('📍 Nous sommes ici : Dakar, Sénégal')
    .openPopup();
  }

}
