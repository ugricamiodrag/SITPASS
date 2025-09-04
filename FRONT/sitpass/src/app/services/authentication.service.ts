import { Injectable } from '@angular/core';
import { User } from '../models/user.model';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ConfigService } from './config.service';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private jwtHelper = new JwtHelperService();

  constructor(private http: HttpClient, private config: ConfigService, private router: Router) { }

  isLoggedIn() {
    if (!localStorage.getItem('user')) {
      return false;
  }
    return true;
  }

  login(userDTO: User): Observable<any> {
    const options = {
      headers: new HttpHeaders({'Content-Type': 'application/json'})
    }
    return this.http.post(this.config.login_url, userDTO, options);
  }


  logout(): Observable<any> {
		return this.http.get(this.config.logout_url, {headers: new HttpHeaders({'Content-Type': 'application/json'}), responseType: 'text'});
	}

  changePassword(oldPassword: string, newPassword: string, repeatedPassword: string): Observable<any> {

    const passwordChangeDTO = {
      oldPassword: oldPassword,
      newPassword: newPassword,
      repeatPassword: repeatedPassword
    };

    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',

      })
    };
    return this.http.post(this.config.change_password_url, passwordChangeDTO,
    options
  )
  }
  hasRole(role: string): boolean {
    const item = localStorage.getItem('user');
    let theRole: undefined;
    if (!item) {
      this.router.navigate(['login']);
      theRole = undefined;
      return false;
    }

    const jwt: JwtHelperService = new JwtHelperService();
    try {
      const decodedToken = jwt.decodeToken(item);
      if (decodedToken && decodedToken.role) {
        theRole = decodedToken.role.authority;
        if (theRole === role){
          return true;
        }
        else {
          return false;
        }


      } else {
        console.error('Role not found in token payload');
        return false;

      }
    } catch (error) {
      console.error('Error decoding token:', error);
      return false;
    }
  }
  getEmailFromToken(): string | null {
    const token = localStorage.getItem('user');
    if (token) {
      const decodedToken = this.jwtHelper.decodeToken(token);
      return decodedToken ? decodedToken.sub : null;
    }
    return null;
  }


}
