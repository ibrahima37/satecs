import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from './auth.service';
import { NotificationApp, CanalNotification, TypeFormation, CreateNotificationRequest } from '../models/notification';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private apiUrl = 'http://192.168.1.109:9090/api/notification';

  constructor(private http: HttpClient, public auth: AuthService){}

  getAllNotifications(): Observable<NotificationApp[]>{
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    return this.http.get<NotificationApp[]>(this.apiUrl, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  getNotificationById(id: number): Observable<NotificationApp>{
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    return this.http.get<NotificationApp>(`${this.apiUrl}/${id}`, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  updateNotification(id: number, notification: NotificationApp): Observable<NotificationApp>{
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    return this.http.put<NotificationApp>(`${this.apiUrl}/${id}`, notification, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  creerNotification(request: CreateNotificationRequest): Observable<Notification> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    return this.http.post<Notification>(this.apiUrl, request, { headers });
  }

  envoyerNotification(id: number): Observable<Notification> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    return this.http.post<Notification>(`${this.apiUrl}/${id}/envoyer`, {}, { headers });
  }

  getNotificationsEnAttente(): Observable<Notification[]> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    return this.http.get<Notification[]>(`${this.apiUrl}/en-attente`, { headers });
  }

  getNotificationsByDestinataire(destinataire: string): Observable<Notification[]> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    return this.http.get<Notification[]>(`${this.apiUrl}/destinataire/${destinataire}`, { headers });
  }

  supprimerNotification(id: number): Observable<void> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers });
  }

  getNotificationByCanal(canal: CanalNotification): Observable<NotificationApp[]>{
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    return this.http.get<NotificationApp[]>(`${this.apiUrl}/canal/${canal}`, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  getNotificationByType(type: TypeFormation): Observable<NotificationApp[]>{
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    return this.http.get<NotificationApp[]>(`${this.apiUrl}/type/${type}`, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  getNotificationsEnvoyee(): Observable<NotificationApp[]>{
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    return this.http.get<NotificationApp[]>(`${this.apiUrl}/envoyees`, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  getNotificationsNonEnvoyee(): Observable<NotificationApp[]>{
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    return this.http.get<NotificationApp[]>(`${this.apiUrl}/nonenvoyees`, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  getNotificationsByPeriode(dateDebut: string, dateFin: string): Observable<NotificationApp[]> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    return this.http.get<NotificationApp[]>(`${this.apiUrl}/periode`, {
      params: { dateDebut, dateFin }
    }).pipe(
      catchError(this.handleError)
    );
  }

  marquerCommeLue(id: number): Observable<NotificationApp> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    return this.http.patch<NotificationApp>(`${this.apiUrl}/${id}/lue`, { headers }, {}).pipe(
      catchError(this.handleError)
    );
  }

  // Envoyer des notifications en masse
  envoyerNotificationsEnMasse(ids: number[]): Observable<NotificationApp[]> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    return this.http.post<NotificationApp[]>(`${this.apiUrl}/envoyer-masse`, { ids }, { headers }).pipe(
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
