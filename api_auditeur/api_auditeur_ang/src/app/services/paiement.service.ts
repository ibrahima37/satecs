import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Paiement, StatutPaiement, ModePaiement } from '../models/paiement';

@Injectable({
  providedIn: 'root'
})
export class PaiementService {

private apiUrl = 'http://192.168.1.184:8087/api/paiement';

constructor(private http: HttpClient) {}

  // Récupérer tous les paiements
  getPaiements(): Observable<Paiement[]> {
    return this.http.get<Paiement[]>(this.apiUrl).pipe(
      catchError(this.handleError)
    );
  }

  // Récupérer un paiement par ID
  getPaiementById(id: number): Observable<Paiement> {
    return this.http.get<Paiement>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  // Récupérer un paiement par numéro de transaction
  getPaiementByNumTransaction(numTransaction: string): Observable<Paiement> {
    return this.http.get<Paiement>(`${this.apiUrl}/transaction/${numTransaction}`).pipe(
      catchError(this.handleError)
    );
  }

  // Créer un nouveau paiement
  createPaiement(paiement: Paiement): Observable<Paiement> {
    return this.http.post<Paiement>(this.apiUrl, paiement).pipe(
      catchError(this.handleError)
    );
  }

  // Mettre à jour un paiement
  updatePaiement(id: number, paiement: Paiement): Observable<Paiement> {
    return this.http.put<Paiement>(`${this.apiUrl}/${id}`, paiement).pipe(
      catchError(this.handleError)
    );
  }

  // Mettre à jour le statut d'un paiement
  updateStatutPaiement(id: number, statut: StatutPaiement): Observable<Paiement> {
    return this.http.patch<Paiement>(`${this.apiUrl}/${id}/statut`, { statut }).pipe(
      catchError(this.handleError)
    );
  }

  // Supprimer un paiement
  deletePaiement(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  // Récupérer les paiements par statut
  getPaiementsByStatut(statut: StatutPaiement): Observable<Paiement[]> {
    return this.http.get<Paiement[]>(`${this.apiUrl}/statut/${statut}`).pipe(
      catchError(this.handleError)
    );
  }

  // Récupérer les paiements par mode de paiement
  getPaiementsByMode(mode: ModePaiement): Observable<Paiement[]> {
    return this.http.get<Paiement[]>(`${this.apiUrl}/mode/${mode}`).pipe(
      catchError(this.handleError)
    );
  }

  // Récupérer les paiements par période
  getPaiementsByPeriode(dateDebut: string, dateFin: string): Observable<Paiement[]> {
    return this.http.get<Paiement[]>(`${this.apiUrl}/periode`, {
      params: { dateDebut, dateFin }
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Calculer le montant total des paiements
  getTotalMontant(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/total`).pipe(
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

}
