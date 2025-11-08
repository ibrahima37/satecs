import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { Inscriptions, InscriptionAvecPaiementRequest, CreateInscriptionRequest } from '../models/inscriptions';

@Injectable({
  providedIn: 'root'
})
export class InscriptionService {
  private apiUrl = "http://192.168.1.109:9090/api/inscription";

  constructor(private http: HttpClient, public auth: AuthService){}

  getAllInscriptions(): Observable<Inscriptions[]> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    return this.http.get<Inscriptions[]>(this.apiUrl, { headers });
  }

  inscrire(request: CreateInscriptionRequest): Observable<any> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

    return this.http.post<any>(this.apiUrl, request, { headers });
  }

  getInscriptionById(id: number): Observable<Inscriptions> {
    return this.http.get<Inscriptions>(`${this.apiUrl}/${id}`);
  }

  creerInscription(request: CreateInscriptionRequest): Observable<Inscriptions> {
    return this.http.post<Inscriptions>(this.apiUrl, request);
  }

  creerInscriptionAvecPaiement(request: InscriptionAvecPaiementRequest): Observable<Inscriptions> {
    return this.http.post<Inscriptions>(`${this.apiUrl}/avec-paiement`, request);
  }

  confirmerInscription(id: number): Observable<Inscriptions> {
    return this.http.patch<Inscriptions>(`${this.apiUrl}/${id}/confirmer`, {});
  }

  annulerInscription(id: number): Observable<Inscriptions> {
    return this.http.patch<Inscriptions>(`${this.apiUrl}/${id}/annuler`, {});
  }

  getInscriptionsByUtilisateur(userId: number): Observable<Inscriptions[]> {
    return this.http.get<Inscriptions[]>(`${this.apiUrl}/utilisateur/${userId}`);
  }

  getInscriptionsByFormation(formationId: number): Observable<Inscriptions[]> {
    return this.http.get<Inscriptions[]>(`${this.apiUrl}/formation/${formationId}`);
  }

  supprimerInscription(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

}
