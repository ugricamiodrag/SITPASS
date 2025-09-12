import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import {FacilitySearchCriteria} from "../models/facility-search.model";
import {Facility} from "../models/facility.model";
import {FacilityService} from "../services/facility.service";

import {ConfigService} from "../services/config.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  public role: string | undefined;
  searchResults: Facility[] = [];
  popularFacility: Facility[] = [];
  visitedFacility: Facility[] = [];
  unvisitedFacility: Facility[] = [];
  page: number = 0;
  size: number = 5;
  totalPages: number = 0;
  showNoFacilitiesMessage = false;
  mySet: Set<any> = new Set();

  constructor(private router: Router, private facilityService: FacilityService, private config: ConfigService) { }

  ngOnInit() {
    this.checkRole();
    if (this.role && this.role === 'ROLE_USER'){
      this.getPopularFacilities();
      this.getVisitedFacilities();
      this.getUnvisitedFacilities();

    }
    else {
      this.loadAdminAndManagerFacilities();
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
        console.log(decodedToken);
      } else {
        console.error('Role not found in token payload');

      }
    } catch (error) {
      console.error('Error decoding token:', error);

    }
  }

  onSearchResults(event: Facility[]) {
    this.searchResults = event;
    this.showNoFacilitiesMessage = true;
  }



  loadAdminAndManagerFacilities() {
    this.facilityService.getFacilities().subscribe(value => {
      this.searchResults = value;
    })
  }

  getPopularFacilities(): void {
    this.facilityService.getPopularFacilities().subscribe(value => {
      if (value) {
        this.popularFacility = value;
        this.popularFacility.forEach(item => this.mySet.add(item));
      }
      else {
        this.popularFacility = [];
      }
    })
  }

  getVisitedFacilities(): void {
    this.facilityService.getVisitedFacilities().subscribe(value => {
      if (value) {
        this.visitedFacility = value;
        this.visitedFacility.forEach(item => this.mySet.add(item));
      }
      else {
        this.visitedFacility = [];
      }
    })
  }

  getUnvisitedFacilities(): void {
    this.facilityService.getUnvisitedFacilities(this.page, this.size).subscribe(value => {
      this.unvisitedFacility = this.unvisitedFacility.concat(value.content);
      this.unvisitedFacility.forEach(item => this.mySet.add(item));
      this.totalPages = value.totalPages;
    })
  }

  loadMore() {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.getUnvisitedFacilities();
    }
  }


}
