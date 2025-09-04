import { Injectable } from '@angular/core';
import { Router, ActivatedRouteSnapshot, UrlTree } from '@angular/router';
import { AuthenticationService } from '../services/authentication.service';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Observable, of } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class RoleGuard {

  constructor(
    public auth: AuthenticationService,
    public router: Router
  ) { }

  canActivate(route: ActivatedRouteSnapshot): Observable<boolean | UrlTree> {
    const expectedRoles: string = route.data['expectedRoles'];
    const token = localStorage.getItem('user');
    const jwt: JwtHelperService = new JwtHelperService();

    if (!token) {
      return of(this.router.createUrlTree(['/login']));
    }

    try {
      const info = jwt.decodeToken(token);
      const roles: string[] = expectedRoles.split('|');
      const hasRole = roles.some(role => role === info.role[0].authority);

      if (!hasRole) {
        return of(this.router.createUrlTree(['/sitpass']));
      }
      return of(true);
    } catch (error) {
      console.error('Error decoding token', error);
      return of(this.router.createUrlTree(['/login']));
    }
  }
}
