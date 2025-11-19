import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Utilisateur } from '../models/utilisateur';

@Injectable({
  providedIn: 'root'
})
export class UtilisateurService {

  private utilisateurConnecte: any = null;
  private apiUrl = "http://192.168.1.248:9090/api/utilisateur";
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

  getUtilisateurById(id: number): Observable<Utilisateur> {
    return this.http.get<Utilisateur>(`${this.apiUrl}/${id}`);
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
    if (this.utilisateurConnecte) {
      return this.utilisateurConnecte;
    }

    try {
      const user = localStorage.getItem('utilisateur');
      if (!user || user === 'undefined') {
        this.utilisateurConnecte = null;
      } else {
        this.utilisateurConnecte = JSON.parse(user);
      }
    } catch (error) {
      console.error('Erreur lors du parsing du token utilisateur :', error);
      this.utilisateurConnecte = null;
    }

    return this.utilisateurConnecte;
  }

  clearUtilisateur(): void {
    localStorage.removeItem('currentUtilisateur');
    this.currentUtilisateurSubject.next(null);
    this.isAuthentificatedSubject.next(false);
  }

  clearUtilisateurConnect(): void {
    this.utilisateurConnecte = null;
    localStorage.removeItem('utlisateur');
  }

  login(email: string, motDePasse: string): Observable<any>{
    return this.http.post('`${this.apiUrl}`/login', {email, motDePasse})
  }

}
