import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import {UserService} from "../services/user.service";
import {AuthenticationService} from "../services/authentication.service";
import {User} from "../models/user.model";
import {ToastrService} from "ngx-toastr";
import {Facility} from "../models/facility.model";
import {FacilityService} from "../services/facility.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {
  public role: string | undefined;
  public user!: User;
  public selectedSection!: String;
  visited!: Facility[];
  managed!: Facility[];

  constructor(private router: Router, private userService: UserService,
              private authService: AuthenticationService,
              private facilityService: FacilityService,
              private toastr: ToastrService) { }

  ngOnInit() {
    this.checkRole();
    this.loadUser();
    if (this.role === 'ROLE_USER'){
      this.getVisitedFacilities();
    }
    if (this.role === 'ROLE_MANAGER'){
      this.getManagedFacilities();
    }
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
        console.log(item);
      } else {
        console.error('Role not found in token payload');

      }
    } catch (error) {
      console.error('Error decoding token:', error);

    }
  }

  getVisitedFacilities(): void {
    this.facilityService.getVisitedFacilities().subscribe(value => {
      if (value) {
        this.visited = value;
      }
      else {
        this.visited = [];
      }
    })
  }

  getManagedFacilities() {
    this.facilityService.getManagedFacilities().subscribe(value => {
      this.managed = value;
    })
  }



  loadUser(){
    const email = this.authService.getEmailFromToken();
    if (email){
      this.userService.getUserByEmail(email).subscribe(
        (user: User) => {
          this.user = user;
        },
        error => {
          console.log(error);
          this.router.navigate(['/login']);
        }
      )
    }
    else {
      this.router.navigate(['/login']);
    }
  }


  changeSection(section: string) {
    this.selectedSection = section;
  }
}
