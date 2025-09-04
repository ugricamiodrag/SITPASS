import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { AuthenticationService } from '../services/authentication.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrl: './change-password.component.css'
})
export class ChangePasswordComponent {

  oldPassword!: string;
  newPassword!: string;
  repeatNewPassword!: string;

  constructor(private toastr: ToastrService, private service: AuthenticationService) {}
  changePassword() {
    if (this.newPassword !== this.repeatNewPassword) {
      this.toastr.error('New passwords do not match.');
      return;
    }
  
    this.service.changePassword(this.oldPassword, this.newPassword, this.repeatNewPassword).subscribe(
      response => {
        this.toastr.success(response.message);
        this.oldPassword = '';
        this.newPassword = '';
        this.repeatNewPassword = '';
      },
      error => {
        console.log(error);
        this.toastr.error(error.error);
      }
    );
  }
}  