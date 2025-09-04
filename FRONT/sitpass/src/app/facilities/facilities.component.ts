import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Facility } from '../models/facility.model';
import { FacilityService } from '../services/facility.service';
import { FacilityFormModalComponent } from '../facility-form-modal/facility-form-modal.component';
import { ToastrService } from 'ngx-toastr';
import { AuthenticationService } from '../services/authentication.service';

@Component({
  selector: 'app-facilities',
  templateUrl: './facilities.component.html',
  styleUrls: ['./facilities.component.css']
})
export class FacilitiesComponent implements OnInit {
  facilities: Facility[] = [];

  constructor(private facilityService: FacilityService, public dialog: MatDialog, private toastr: ToastrService, private authService: AuthenticationService) {}

  ngOnInit(): void {
    this.loadFacilities();
  }

  loadFacilities() {
    this.facilities = [];
    this.facilityService.getFacilities().subscribe((data) => {

      if (data) {
        this.facilities = data;
      }
      else {
        this.facilities = [];
      }



    });
  }

  onEditFacility(facility: Facility): void {

    const dialogRef = this.dialog.open(FacilityFormModalComponent, {
      width: '55%',
      data: { facility: facility, isAdmin: this.isAdmin()}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const index = this.facilities.findIndex(f => f.id === result.id);
        if (index !== -1) {
          this.facilities[index] = result;
        }
      }
    });
  }

  onDeleteFacility(facilityId: number): void {
    this.facilityService.deleteFacility(facilityId).subscribe(
      () => {
        this.toastr.success("You have successfully deleted the facility.")
        console.log('Facility deleted successfully');
        this.loadFacilities();

      },
      (error) => {

        this.toastr.error(error.error);

      }
    );

}

isAdmin(): boolean {
  return this.authService.hasRole('ROLE_ADMIN');
}


  onHideFacility(facilityId: number): void {
    this.facilityService.hideFacility(facilityId).subscribe(
      () =>{

        this.toastr.success("You have successfully hidden the facility");
        this.loadFacilities();
      },
      (error) => {
        this.toastr.error(error.error);
      }
    )
  }

  onUnhideFacility(facilityId: number): void {
    this.facilityService.hideFacility(facilityId).subscribe(
      () =>{

        this.toastr.success("You have successfully unhidden the facility");
        this.loadFacilities();
      },
      (error) => {
        this.toastr.error(error.error);
      }
    )
  }

  openCreateFacilityModal(): void {
    const dialogRef = this.dialog.open(FacilityFormModalComponent, {
      width: '55%',
      data: {facility : null, isAdmin: this.isAdmin()}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.facilities.push(result);
      }
    });
  }
}
