import { Component } from '@angular/core';
import { AuthenticationService } from '../../services/authentication.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { NgOptimizedImage } from '@angular/common'

@Component({
  selector: 'app-navbar-admin',
  templateUrl: './navbar-admin.component.html',
  styleUrl: './navbar-admin.component.css'
})
export class NavbarAdminComponent {

  constructor(private authenticationService: AuthenticationService, private toastr: ToastrService, private router: Router){}

  logout() {
		this.authenticationService.logout().subscribe(
			result => {
				localStorage.removeItem('user');
				this.toastr.success(result);
				this.router.navigate(['login']);
			},
			error => {
				this.toastr.error(error.error);
			}
		);
	}
}
