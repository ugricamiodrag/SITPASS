import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AccountRequest } from '../models/account-request.model';
import { ConfigService } from './config.service';

@Injectable({
  providedIn: 'root'
})
export class RegistrationFormService {
  private headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(private http: HttpClient, private config: ConfigService) {}

  checkEmail(email: string): Observable<any> {
    return this.http.get(this.config.checkEmailUrl(email));
  }

  add(request: AccountRequest): Observable<any> {
    const options = {
      headers: new HttpHeaders({'Content-Type': 'application/json'})
    }
    return this.http.post(this.config.register_url, request, options);
  }
}
