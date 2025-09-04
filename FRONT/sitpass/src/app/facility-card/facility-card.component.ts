import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { Facility } from '../models/facility.model';
import { AuthenticationService } from '../services/authentication.service';
import { ReservationFormComponent } from '../reservation-form/reservation-form.component';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import {DomSanitizer, SafeUrl} from "@angular/platform-browser";
import {ImageService} from "../services/image.service";
import {AddManagerComponent} from "../add-manager/add-manager.component";
import {ExerciseService} from "../services/exercise.service";
import {UserService} from "../services/user.service";
import {Observable} from "rxjs";
import {RatingSystemComponent} from "../rating-system/rating-system.component";
import {map} from "rxjs/operators";
import {ReviewsPageComponent} from "../reviews-page/reviews-page.component";
import {AnalyticsComponent} from "../analytics/analytics.component";

@Component({
  selector: 'app-facility-card',
  templateUrl: './facility-card.component.html',
  styleUrls: ['./facility-card.component.css']
})
export class FacilityCardComponent implements OnInit {
  @Input() facility: Facility | null = null;
  @Output() edit = new EventEmitter<Facility>();
  @Output() delete = new EventEmitter<number>();
  @Output() hide = new EventEmitter<number>();
  @Output() unhide = new EventEmitter<number>();
  photoUrls: string[] = []; // To store safe URLs of photos
  hasBeenInFacility$: Observable<boolean> | null = null;
  private userId!: number;
  protected active!: boolean;


  constructor(
    private authService: AuthenticationService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private imageService: ImageService, // Inject ImageService
    private sanitizer: DomSanitizer,
    private exerciseService: ExerciseService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    if (this.facility !== null) {
      this.facility.workDays = this.facility.workDays.sort((a, b) => {
        return this.dayOfWeekIndex(a.day) - this.dayOfWeekIndex(b.day);
      });
      this.loadPhotos(); // Fetch photos from the backend
      this.isActive(this.facility);

    }


    this.checkIfUserHasBeenInFacility();
  }

  private dayOfWeekIndex(day: string): number {
    const daysOfWeek = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];
    return daysOfWeek.indexOf(day);
  }

  loadPhotos(): void {
    if (this.facility?.images) {
      this.facility.images.forEach((imagePath, index) => {
        this.imageService.getImage(imagePath).subscribe(response => {
          const url = window.URL.createObjectURL(response);
          this.photoUrls[index] = <string>this.sanitizer.bypassSecurityTrustUrl(url);
        }, error => {
          console.error('Error fetching image:', error);
        });
      });
    }
  }

  isAdmin(): boolean {
    return this.authService.hasRole('ROLE_ADMIN');
  }

  isManager(): boolean {
    return this.authService.hasRole('ROLE_MANAGER');
  }

  isUser(): boolean {
    return this.authService.hasRole('ROLE_USER');
  }

  isActive(facility: Facility): void {
    this.active = facility.active;
  }

  onEdit() {

    if (this.facility) {

      this.edit.emit(this.facility);
    }
  }



  onMakeReservation() {
    if (this.facility) {
      const dialogRef = this.dialog.open(ReservationFormComponent, {
        width: '400px',
        data: { facility: this.facility }
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result) {

          this.snackBar.open('Reservation successfully made!', 'Close', {
            duration: 3000,
          });
        }
      });
    }
  }

  checkIfUserHasBeenInFacility(): void {
    const email = this.authService.getEmailFromToken();

    if (email && this.facility) {

      this.userService.getUserByEmail(email).subscribe({
        next: user => {

          if (user.id && this.facility) {
            this.userId = user.id;
            this.hasBeenInFacility$ = this.exerciseService.hasBeenInFacility(this.facility, user.id);
          }
        },
        error: err => {
          console.error('Error fetching user:', err);
        }
      });
    }
  }


  onDelete() {
    if (this.facility && this.facility.id) {
      this.delete.emit(this.facility.id);
    }
  }

  onHide() {
    if (this.facility && this.facility.id){
      this.hide.emit(this.facility.id);
    }
  }

  onUnhide(){
    if (this.facility && this.facility.id){
      this.unhide.emit(this.facility.id);
    }
  }

  manage() {

    if (this.facility) {

      const dialogRef = this.dialog.open(AddManagerComponent, {
        width: '400px',
        data: { facility: this.facility, isAdmin: this.isAdmin() }
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result) {

          this.snackBar.open('You have successfully changed the manager!', 'Close', {
            duration: 3000,
          });
        }
      });
    }
  }


  rateIt() {
    const dialogRef = this.dialog.open(RatingSystemComponent, {
      height: "450px",
      width: "350px",
      data: { facilityId: this.facility!.id, userId: this.userId }
    })

    dialogRef.afterClosed().subscribe(value => {
      if (value){
        this.snackBar.open('You have successfully rated this facility!', 'Close', {
          duration: 3000,
        });
      }
    })
  }

  openReviews() {
    const dialogRef = this.dialog.open(ReviewsPageComponent, {
      height: "450px",
      width: "350px",
      data: { facilityId: this.facility!.id , isManager: this.isManager()}
    })
  }

  openAnalytics() {
    const dialogRef = this.dialog.open(AnalyticsComponent, {
      width: '40%',
      height: '70%',
      data: { facilityId: this.facility!.id }
    });
  }

}
