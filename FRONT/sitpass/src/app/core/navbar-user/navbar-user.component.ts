import { Component } from '@angular/core';
import { AuthenticationService } from '../../services/authentication.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar-user',
  templateUrl: './navbar-user.component.html',
  styleUrl: './navbar-user.component.css'
})
export class NavbarUserComponent {

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
