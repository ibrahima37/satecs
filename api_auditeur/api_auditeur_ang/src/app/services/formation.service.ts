import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Formation } from '../models/formation';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class FormationService {

  private apiUrl = "http://192.168.1.109:9090/api/formation";

  constructor(private http: HttpClient, public auth: AuthService){}

  getAllFormations(): Observable<Formation[]>{
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    console.log('Token envoyé :', token);
    return this.http.get<Formation[]>(this.apiUrl, { headers });
  }

  getFormationById(id: number): Observable<Formation>{
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    return this.http.get<Formation>(`${this.apiUrl}/${id}`, { headers });
  }

  creerFormation(formation: Formation): Observable<Formation> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    return this.http.post<Formation>(this.apiUrl, formation, { headers });
  }

  modifierFormation(id: number, formation: Formation): Observable<Formation> {
    return this.http.put<Formation>(`${this.apiUrl}/${id}`, formation);
  }

  supprimerFormation(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getFormationsAVenir(): Observable<Formation[]> {
    return this.http.get<Formation[]>(`${this.apiUrl}/a-venir`);
  }

  rechercherFormations(motCle: string): Observable<Formation[]> {
    return this.http.get<Formation[]>(`${this.apiUrl}/recherche?motCle=${motCle}`);
  }

  getFormationsByType(type: string): Observable<Formation[]> {
    return this.http.get<Formation[]>(`${this.apiUrl}/type/${type}`);
  }

}
