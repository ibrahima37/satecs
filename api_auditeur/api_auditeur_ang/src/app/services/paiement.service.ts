import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from './auth.service';
import { Paiement, StatutPaiement, ModePaiement, CreatePaiementRequest } from '../models/paiement';

@Injectable({
  providedIn: 'root'
})
export class PaiementService {

private apiUrl = 'http://192.168.1.109:9090/api/paiement';

constructor(private http: HttpClient, public auth: AuthService) {}

  // Récupérer tous les paiements
  getAllPaiements(): Observable<Paiement[]> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    console.log('Token envoyé :', token);
    return this.http.get<Paiement[]>(this.apiUrl, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  // Récupérer un paiement par ID
  getPaiementById(id: number): Observable<Paiement> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    console.log('Token envoyé :', token);
    return this.http.get<Paiement>(`${this.apiUrl}/${id}`, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  // Récupérer un paiement par numéro de transaction
  getPaiementByNumTransaction(numTransaction: string): Observable<Paiement> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    console.log('Token envoyé :', token);
    return this.http.get<Paiement>(`${this.apiUrl}/transaction/${numTransaction}`, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  // Créer un nouveau paiement
  creerPaiement(request: CreatePaiementRequest): Observable<Paiement> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    console.log('Token envoyé :', token);
    return this.http.post<Paiement>(this.apiUrl, request, { headers });
  }

  // Mettre à jour un paiement
  updatePaiement(id: number, paiement: Paiement): Observable<Paiement> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    console.log('Token envoyé :', token);
    return this.http.put<Paiement>(`${this.apiUrl}/${id}`, paiement, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  // Mettre à jour le statut d'un paiement
  updateStatutPaiement(id: number, statut: StatutPaiement): Observable<Paiement> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    console.log('Token envoyé :', token);
    return this.http.patch<Paiement>(`${this.apiUrl}/${id}/statut`, { statut }, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  // Supprimer un paiement
  deletePaiement(id: number): Observable<void> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    console.log('Token envoyé :', token);
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  // Récupérer les paiements par statut
  getPaiementsByStatut(statut: StatutPaiement): Observable<Paiement[]> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    console.log('Token envoyé :', token);
    return this.http.get<Paiement[]>(`${this.apiUrl}/statut/${statut}`, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  // Récupérer les paiements par mode de paiement
  getPaiementsByMode(mode: ModePaiement): Observable<Paiement[]> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    console.log('Token envoyé :', token);
    return this.http.get<Paiement[]>(`${this.apiUrl}/mode/${mode}`, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  // Récupérer les paiements par période
  getPaiementsByPeriode(dateDebut: string, dateFin: string): Observable<Paiement[]> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    console.log('Token envoyé :', token);
    return this.http.get<Paiement[]>(`${this.apiUrl}/periode`, {
      params: { dateDebut, dateFin }
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Calculer le montant total des paiements
  getTotalMontant(): Observable<number> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    console.log('Token envoyé :', token);
    return this.http.get<number>(`${this.apiUrl}/total`, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  // Gestion des erreurs
  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Une erreur est survenue';

    if (error.error instanceof ErrorEvent) {
      // Erreur côté client
      errorMessage = `Erreur: ${error.error.message}`;
    } else {
      // Erreur côté serveur
      errorMessage = `Code d'erreur: ${error.status}\nMessage: ${error.message}`;
    }

    console.error(errorMessage);
    return throwError(() => new Error(errorMessage));
  }

  validerPaiement(id: number): Observable<Paiement> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    console.log('Token envoyé :', token);
    return this.http.patch<Paiement>(`${this.apiUrl}/${id}/valider`, { headers }, {});
  }

  annulerPaiement(id: number, motif?: string): Observable<Paiement> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    console.log('Token envoyé :', token);
    const url = motif ? `${this.apiUrl}/${id}/annuler?motif=${motif}` : `${this.apiUrl}/${id}/annuler`;
    return this.http.patch<Paiement>(url, { headers }, {});
  }

  getTotalPaiementsValides(): Observable<{ totalValides: number }> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    console.log('Token envoyé :', token);
    return this.http.get<{ totalValides: number }>(`${this.apiUrl}/statistiques/total-valides`, { headers });
  }

}
