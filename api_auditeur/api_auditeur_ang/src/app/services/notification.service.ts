import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { NotificationApp, CanalNotification, TypeFormation } from '../models/notification';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private apiUrl = 'http://192.168.1.184: 8087/api/notification';

  constructor(private http: HttpClient){}

  getNotification(): Observable<NotificationApp[]>{
    return this.http.get<NotificationApp[]>(this.apiUrl).pipe(
      catchError(this.handleError)
    );
  }

  getNotificationById(id: number): Observable<NotificationApp>{
    return this.http.get<NotificationApp>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  updateNotification(id: number, notification: NotificationApp): Observable<NotificationApp>{
    return this.http.put<NotificationApp>(`${this.apiUrl}/${id}`, notification).pipe(
      catchError(this.handleError)
    );
  }

  deleteNotification(id: number): Observable<void>{
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  envoyerNotification(id: number): Observable<NotificationApp>{
    return this.http.post<NotificationApp>(`${this.apiUrl}/${id}/envoyer`, {}).pipe(
      catchError(this.handleError)
    );
  }

  getNotificationByDestinataire(destinataire: string): Observable<NotificationApp[]>{
    return this.http.get<NotificationApp[]>(`${this.apiUrl}/destinataire/${destinataire}`).pipe(
      catchError(this.handleError)
    );
  }

  getNotificationByCanal(canal: CanalNotification): Observable<NotificationApp[]>{
    return this.http.get<NotificationApp[]>(`${this.apiUrl}/canal/${canal}`).pipe(
      catchError(this.handleError)
    );
  }

  getNotificationByDType(type: TypeFormation): Observable<NotificationApp[]>{
    return this.http.get<NotificationApp[]>(`${this.apiUrl}/type/${type}`).pipe(
      catchError(this.handleError)
    );
  }

  getNotificationsEnvoyee(): Observable<NotificationApp[]>{
    return this.http.get<NotificationApp[]>(`${this.apiUrl}/envoyees`).pipe(
      catchError(this.handleError)
    );
  }

  getNotificationsNonEnvoyee(): Observable<NotificationApp[]>{
    return this.http.get<NotificationApp[]>(`${this.apiUrl}/nonenvoyees`).pipe(
      catchError(this.handleError)
    );
  }

  getNotificationsByPeriode(dateDebut: string, dateFin: string): Observable<NotificationApp[]> {
    return this.http.get<NotificationApp[]>(`${this.apiUrl}/periode`, {
      params: { dateDebut, dateFin }
    }).pipe(
      catchError(this.handleError)
    );
  }

  marquerCommeLue(id: number): Observable<NotificationApp> {
    return this.http.patch<NotificationApp>(`${this.apiUrl}/${id}/lue`, {}).pipe(
      catchError(this.handleError)
    );
  }

  // Envoyer des notifications en masse
  envoyerNotificationsEnMasse(ids: number[]): Observable<NotificationApp[]> {
    return this.http.post<NotificationApp[]>(`${this.apiUrl}/envoyer-masse`, { ids }).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Une erreur est survenue';

    if(error.error instanceof ErrorEvent){
      errorMessage = 'Erreur : ${error.error.message}';
    } else {
        errorMessage = `Code d\'erreur : ${error.status}\nMessage: ${error.message}`;
    }
    console.error(errorMessage);
    return throwError(()=> new Error(errorMessage));
  }
}
