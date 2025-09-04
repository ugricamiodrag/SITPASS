import { Component } from '@angular/core';
import { User } from '../models/user.model';
import { AuthenticationService } from '../services/authentication.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  constructor(private authenticationService: AuthenticationService, private toastr: ToastrService, private router: Router){}

  user = new User('', '');

  onSubmit() {
    const auth = new User('', '');
    auth.email = this.user.email;
    auth.password = this.user.password;

    this.authenticationService.login(auth).subscribe(
        (result: any) => {
            console.log(result);
            this.toastr.success('Successful login!');
            localStorage.setItem('user', JSON.stringify(result)); 
            this.router.navigate(['home']);
        },
        (error: any) => {
            this.toastr.error('An error occurred during login.');
            console.log(error);
        }
    );
}

}
