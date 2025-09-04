import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { RegistrationFormService } from '../services/registration-form.service';
import { AccountRequest, RequestStatus } from '../models/account-request.model';
import { trigger, state, style, transition, animate } from '@angular/animations';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';


@Component({
  selector: 'app-registration-form',
  templateUrl: './registration-form.component.html',
  styleUrls: ['./registration-form.component.css'],
  animations: [
    trigger('flyInOut', [
      state('in', style({ opacity: 1 })),
      transition('void => *', [
        style({ opacity: 0 }),
        animate(300)
      ]),
      transition('* => void', [
        animate(300, style({ opacity: 0 }))
      ])
    ])
  ]  
})
export class RegistrationFormComponent implements OnInit {
  registrationForm!: FormGroup;
 

  constructor(
    private formBuilder: FormBuilder, 
    private registrationService: RegistrationFormService,
    private toaster: ToastrService,
    private router: Router,
  ){}

  ngOnInit(): void {
    this.registrationForm = this.formBuilder.group({
      email: ['', 
        [Validators.required, Validators.email]
      ],
      address: [''] 
    });
  }
  

  onSubmit(): void {
    if (this.registrationForm.valid) {
      const accountRequest: AccountRequest = {
        email: this.registrationForm.get('email')?.value,
        address: this.registrationForm.get('address')?.value,
        status: RequestStatus.PENDING 
      };
  
      this.registrationService.add(accountRequest).subscribe({
        next: (result) => {
          this.toaster.success(result.message);
          this.registrationForm.reset();

        },
        error: (result) => {
          console.log(result.message);
          this.toaster.error('Registration is not successful!');
        }
      });
    } else {
      this.toaster.error('Form is not valid!');
    }
  }
}  