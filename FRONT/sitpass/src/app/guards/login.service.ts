import { Injectable } from '@angular/core';
import { Router, UrlTree } from '@angular/router';
import { AuthenticationService } from '../services/authentication.service';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginGuard {

  constructor(
    private authService: AuthenticationService,
    private router: Router
  ) {}

  canActivate(): Observable<boolean | UrlTree> {
    if (this.authService.isLoggedIn()) {
      return of(this.router.createUrlTree(['/home']));
    }
    return of(true);
  }
}
