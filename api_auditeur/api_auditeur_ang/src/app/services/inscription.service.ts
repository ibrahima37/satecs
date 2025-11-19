import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { HttpParams } from '@angular/common/http';
import { InscriptionDto } from '../models/inscriptiondto';
import { Inscription, InscriptionAvecPaiementRequest, CreateInscriptionRequest } from '../models/inscriptions';

@Injectable({
  providedIn: 'root'
})
export class InscriptionService {
  private apiUrl = "http://192.168.1.248:9090/api/inscription";
  private apiUrls = "http://192.168.1.248:9090/api/paiement";

  constructor(private http: HttpClient, public auth: AuthService){}

  getAllInscriptions(): Observable<Inscription[]> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    return this.http.get<Inscription[]>(this.apiUrl, { headers });
  }

  inscrire(request: CreateInscriptionRequest): Observable<Inscription> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });

     const formData = new FormData();
      formData.append('formationId', request.formationId.toString());
      formData.append('utilisateurId', request.utilisateurId.toString());
      formData.append('paiementId', request.paiementId?.toString() ?? "0");
      formData.append('etatInscription', request.etatInscription);
      formData.append('numeroCni', request.numeroCni);
      formData.append('dateNaissance', request.dateNaissance);
      formData.append('address', request.address);
      formData.append('numeroTel', request.numeroTel);
      if (request.fichier) {
        formData.append('fichier', request.fichier, request.fichier.name);
      }
      console.log('Payload inscription:', request);
    return this.http.post<Inscription>(this.apiUrl, formData, { headers });
  }

  getInscriptionById(id: number): Observable<InscriptionDto> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    return this.http.get<InscriptionDto>(`${this.apiUrl}/${id}`, { headers });
  }

  updateInscription(id: number, data: FormData): Observable<InscriptionDto> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    return this.http.put<InscriptionDto>(`${this.apiUrl}/${id}`, data, { headers });
  }

  getPaiements(): Observable<any[]> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    return this.http.get<any[]>(`${this.apiUrls}`, { headers });
  }


  creerInscription(request: CreateInscriptionRequest): Observable<Inscription> {
    return this.http.post<Inscription>(this.apiUrl, request);
  }

  creerInscriptionAvecPaiement(request: InscriptionAvecPaiementRequest): Observable<Inscription> {
    return this.http.post<Inscription>(`${this.apiUrl}/avec-paiement`, request);
  }

  confirmerInscription(id: number): Observable<Inscription> {
    return this.http.patch<Inscription>(`${this.apiUrl}/${id}/confirmer`, {});
  }

  annulerInscription(id: number): Observable<Inscription> {
    return this.http.patch<Inscription>(`${this.apiUrl}/${id}/annuler`, {});
  }

  getInscriptionsByUtilisateur(userId: number): Observable<Inscription[]> {
    return this.http.get<Inscription[]>(`${this.apiUrl}/utilisateur/${userId}`);
  }

  getInscriptionsByFormation(formationId: number): Observable<Inscription[]> {
    return this.http.get<Inscription[]>(`${this.apiUrl}/formation/${formationId}`);
  }

  supprimerInscription(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

}
