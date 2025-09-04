import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AccountRequest } from '../models/account-request.model';
import { ConfigService } from './config.service';

@Injectable({
  providedIn: 'root'
})
export class RequestService {

  private options = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  }

  constructor(private http: HttpClient, private config: ConfigService) {}

  getRequests(page: number, pageSize: number): Observable<{ requests: AccountRequest[], totalItems: number }> {
    const body = { page, pageSize };
    return this.http.post<{ requests: AccountRequest[], totalItems: number }>(this.config.requests_url, body);
  }
  
  

  acceptRequest(request: AccountRequest): Observable<any> {
    return this.http.put(this.config.accept_url, request, this.options);
  }

  declineRequest(request: AccountRequest, reason?: string): Observable<any> {
    return this.http.put(this.config.decline_url, { request, reason }, this.options);
}

}
