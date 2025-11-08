import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { Utilisateur, AuthResponse, InscriptionRequest, ConnexionRequest } from '../models/utilisateur';

interface LoginResponse{
  token: string;
  user: Utilisateur;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = "http://192.168.1.109:9090/api/utilisateur";

  private readonly TOKEN_KEY = 'token';
  private currentUserSubject = new BehaviorSubject<Utilisateur | null>(this.getStoredUser());
  private currentUser$ = this.currentUserSubject.asObservable();
  public roles: string[] = [];

  constructor(
    private http: HttpClient,
    private router: Router
  ){this.loadUser();}

  private loadUser(): void {
    const userJson = localStorage.getItem('user');
    if (userJson) {
      try {
        const user = JSON.parse(userJson);
        this.currentUserSubject.next(user);
      } catch (e) {
        console.error('Erreur lors du chargement de l\'utilisateur', e);
      }
    }
  }

  inscription(request: InscriptionRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, request).pipe(
      tap(response => this.handleAuthResponse(response))
    );
  }

  connexion(request: ConnexionRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, request).pipe(
      tap(response => this.handleAuthResponse(response))
    );
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  deconnexion(): void {
    this.logout();
  }

  refreshToken(): Observable<AuthResponse> {
    // Si vous avez un endpoint refresh token
    return this.http.post<AuthResponse>(`${this.apiUrl}/refresh-token`, {
      token: this.getToken()
    }).pipe(
      tap(response => this.handleAuthResponse(response))
    );
  }

  private handleAuthResponse(response: AuthResponse): void {
    if (response.token && response.utilisateur) {
      localStorage.setItem('token', response.token);
      localStorage.setItem('user', JSON.stringify(response.utilisateur));
      this.currentUserSubject.next(response.utilisateur);
      this.roles = this.extractRolesFromToken(response.token);
    }
  }

  private extractRolesFromToken(token: string): string[] {
    try {
      // Les JWT sont composés de trois parties séparées par des points : header.payload.signature
      const payloadBase64 = token.split('.')[1];
      if (!payloadBase64) {
        console.warn('Token malformé : aucune partie payload trouvée.');
        return [];
      }

      // Décodage du payload en JSON
      const payloadJson = atob(payloadBase64);
      const payload = JSON.parse(payloadJson);

      // Extraction des rôles depuis le champ 'roles' ou 'role'
      const roles = payload.roles || payload.role || [];

      // Normalisation : s'assurer que c'est un tableau de chaînes
      if (Array.isArray(roles)) {
        return roles.map(role => String(role));
      } else if (typeof roles === 'string') {
        return [roles];
      } else {
        console.warn('Format de rôles inattendu dans le token.');
        return [];
      }
    } catch (error) {
      console.error('Erreur lors de l\'extraction des rôles du token :', error);
      return [];
    }
  }

  private getStoredUser(): Utilisateur | null {
    const userJson = localStorage.getItem('user');
    return userJson ? JSON.parse(userJson) : null;
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  getCurrentUser(): Utilisateur | null {
    return this.currentUserSubject.value;
  }

  // ✅ Méthode publique pour obtenir les rôles
  getUserRoles(): string[] {
    return this.roles;
  }

  // ✅ Vérifier si l'utilisateur a un rôle spécifique
  hasRole(role: string): boolean {
    return this.roles.includes(role);
  }

  // ✅ Vérifier si l'utilisateur a au moins un des rôles
  hasAnyRole(roles: string[]): boolean {
    return roles.some(role => this.roles.includes(role));
  }

}
