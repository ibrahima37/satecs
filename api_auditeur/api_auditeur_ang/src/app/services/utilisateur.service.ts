import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Utilisateur } from '../models/utilisateur';

@Injectable({
  providedIn: 'root'
})
export class UtilisateurService {

  private utilisateurConnecte: any = null;
  private apiUrl = "http://192.168.1.184:8087/api/utilisateur";
  private isAuthentificatedSubject = new BehaviorSubject<boolean>(false);
  private currentUtilisateurSubject = new BehaviorSubject<any>(null);

  constructor(private http: HttpClient) {
    const utilisateur = this.getUtilisateurConnect();
    if (utilisateur) {
      this.isAuthentificatedSubject.next(true);
      this.currentUtilisateurSubject.next(utilisateur);
    }
  }

  inscription(utilisateur: Utilisateur): Observable<Utilisateur>{
    return this.http.post<Utilisateur>(`${this.apiUrl}`, utilisateur);
  }

  isLoggedIn(): Observable<boolean> {
    return this.isAuthentificatedSubject.asObservable();
  }

  getCurrentUtilisateur(): Observable<any> {
    return this.currentUtilisateurSubject.asObservable();
  }

  setUtilisateurConnect(utilisateur: any): void {
    this.utilisateurConnecte = utilisateur;
    localStorage.setItem('utilisateur', JSON.stringify(utilisateur)); // optionnel
  }

  logout(): void {
    localStorage.removeItem('currentUtilisateur');
    this.isAuthentificatedSubject.next(false);
    this.currentUtilisateurSubject.next(null);
  }

  getUtilisateurConnect(): any {
    if (this.utilisateurConnecte) return this.utilisateurConnecte;
    const user = localStorage.getItem('utilisateur');
    return user ? JSON.parse(user) : null;
  }

  clearUtilisateur(): void {
    localStorage.removeItem('currentUtilisateur');
    this.currentUtilisateurSubject.next(null);
    this.isAuthentificatedSubject.next(false);
  }

  login(email: string, motDePasse: string): Observable<any>{
    return this.http.post('http://192.168.1.184:8087/api/utilisateur/login', {email, motDePasse});
  }

}
