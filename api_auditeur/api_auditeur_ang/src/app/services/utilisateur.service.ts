import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UtilisateurService {

  private isAuthentificatedSubject = new BehaviorSubject<boolean>(false);
  private currentUtilisateurSubject = new BehaviorSubject<any>(null);

  constructor(private http: HttpClient) {
    const utilisateur = this.getUtilisateurConnect();
    if (utilisateur) {
      this.isAuthentificatedSubject.next(true);
      this.currentUtilisateurSubject.next(utilisateur);
    }
  }

  isLoggedIn(): Observable<boolean> {
    return this.isAuthentificatedSubject.asObservable();
  }

  getCurrentUtilisateur(): Observable<any> {
    return this.currentUtilisateurSubject.asObservable();
  }

  setUtilisateurConnect(utilisateur: any): void{
    const utilisateurData = {
      id: utilisateur.id,
      nom: utilisateur.nom,
      email: utilisateur.email,
      prenom: utilisateur.prenom,
    };

    localStorage.setItem('currentUtilisateur', JSON.stringify(utilisateurData));
    this.isAuthentificatedSubject.next(true);
    this.currentUtilisateurSubject.next(utilisateurData);
  }

  logout(): void {
    localStorage.removeItem('currentUtilisateur');
    this.isAuthentificatedSubject.next(false);
    this.currentUtilisateurSubject.next(null);
  }

  getUtilisateurConnect(): any {
    const user = localStorage.getItem('currentUtilisateur');
    return user ? JSON.parse(user) : null;
  }

  clearUtilisateur(): void {
    localStorage.removeItem('currentUtilisateur');
    this.currentUtilisateurSubject.next(null);
    this.isAuthentificatedSubject.next(false);
  }

  login(email: string, motDePasse: string): Observable<any>{
    return this.http.post('/api/login', {email, motDePasse});
  }

}
