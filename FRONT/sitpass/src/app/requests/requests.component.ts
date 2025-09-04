import { Component, OnInit } from '@angular/core';
import { RequestService } from '../services/request.service';
import { AccountRequest } from '../models/account-request.model';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Router } from '@angular/router';

@Component({
  selector: 'app-requests',
  templateUrl: './requests.component.html',
  styleUrls: ['./requests.component.css']
})
export class RequestsComponent implements OnInit {
  requests: AccountRequest[] = [];
  totalItems: number = 0;
  pageSize: number = 5;
  activePage: number = 0;
  public role: string | undefined;


  constructor(private requestService: RequestService, private router: Router) {}

  ngOnInit(): void {
    this.loadRequests(this.activePage);
    this.checkRole();
  }

  loadRequests(page: number) {
    this.requestService.getRequests(page, this.pageSize).subscribe(response => {
      this.requests = response.requests;
      this.totalItems = response.totalItems;

    });

  }

  onAccept(request: AccountRequest) {
    this.requestService.acceptRequest(request).subscribe(() => {
      this.loadRequests(this.activePage);
    });

  }

  onDecline(event: { request: AccountRequest, reason?: string }) {
    this.requestService.declineRequest(event.request, event.reason).subscribe(() => {
      this.loadRequests(this.activePage);
    });

  }

  onPageSelected(page: number) {
    this.activePage = page;
    this.loadRequests(page);
  }

  checkRole() {
    const item = localStorage.getItem('user');

    if (!item) {
      this.router.navigate(['login']);
      this.role = undefined;
      return;
    }

    const jwt: JwtHelperService = new JwtHelperService();
    try {
      const decodedToken = jwt.decodeToken(item);
      if (decodedToken && decodedToken.role) {
        this.role = decodedToken.role.authority;
        console.log('Role:', this.role);
      } else {
        console.error('Role not found in token payload');

      }
    } catch (error) {
      console.error('Error decoding token:', error);

    }
  }
}
