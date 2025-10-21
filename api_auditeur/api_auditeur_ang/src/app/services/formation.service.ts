import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Formation } from '../models/formation';
import {Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FormationService {

  private apiUrl = "http://192.168.1.184:8087/api/formation";

  constructor(private http: HttpClient){}

  getFormations(): Observable<Formation[]>{
    return this.http.get<Formation[]>(this.apiUrl);
  }

  getFormationById(id: number): Observable<Formation>{
    return this.http.get<Formation>(`${this.apiUrl}/${id}`);
  }

}
