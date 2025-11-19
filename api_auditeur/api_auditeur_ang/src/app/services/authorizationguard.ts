import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({
providedIn: 'root'
})
export class AuthorizationGuard implements CanActivate {

constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    // 🔐 Vérifie si l'utilisateur est authentifié
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login'], {
        queryParams: { returnUrl: state.url }
      });
      return false;
    }

    // 🔍 Récupère les rôles requis depuis la route
    const requiredRoles: string[] = route.data['roles'] || [];

    // ✅ Si aucun rôle n'est requis, autoriser l'accès
    if (requiredRoles.length === 0) {
      return true;
    }

    // 🔐 Récupère les rôles de l'utilisateur
    const userRoles: string[] = this.authService.getRoles();

    // 🔎 Vérifie si l'utilisateur possède au moins un rôle requis
    const hasRequiredRole = requiredRoles.some((role: string) =>
      userRoles.includes(role)
// ou userRoles.map(r => r.toUpperCase()).includes(role.toUpperCase())
    );

    if (!hasRequiredRole) {
      this.router.navigate(['/access-denied']);
      return false;
    }

    return true;
  }
}
