import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Facility, WorkDay } from '../models/facility.model';
import { ReservationService } from '../services/reservation.service';
import { Exercise } from '../models/exercise.model';
import { timeRangeValidator } from '../validators/timeRangeValidator.validator';  // Import the custom validator
import { User } from '../models/user.model';
import {ExerciseToSendDTO} from "../models/dto/exerciseDTO.model";

@Component({
  selector: 'app-reservation-form',
  templateUrl: './reservation-form.component.html',
  styleUrls: ['./reservation-form.component.css']
})
export class ReservationFormComponent implements OnInit {
  reservationForm: FormGroup;
  workDays: WorkDay[];
  selectedWorkDay: WorkDay | null = null;
  dateFilter: (date: Date | null) => boolean;

  constructor(
    public dialogRef: MatDialogRef<ReservationFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { facility: Facility },
    private fb: FormBuilder,
    private reservationService: ReservationService
  ) {
    this.workDays = this.data.facility.workDays;
    this.reservationForm = this.fb.group({
      date: [null, Validators.required],
      day: [null, Validators.required],
      from: ['', Validators.required],
      until: ['', Validators.required]
    });

    this.dateFilter = (date: Date | null) => {
      if (!date || !this.selectedWorkDay) return false;
      const today = new Date();
      today.setHours(0, 0, 0, 0);
      return date >= today && date.getDay() === this.getWeekdayIndex(this.selectedWorkDay.day);
    };
  }

  ngOnInit(): void {
    this.reservationForm.get('day')?.valueChanges.subscribe(day => {
      this.selectedWorkDay = this.workDays.find(wd => wd.day === day) || null;
      this.updateMinMaxTimes();
    });
  }

  updateMinMaxTimes() {
    if (this.selectedWorkDay) {
      const from = this.selectedWorkDay.from;
      const until = this.selectedWorkDay.until;

      this.reservationForm.get('from')?.setValidators([
        Validators.required,
        timeRangeValidator(from, until)
      ]);

      this.reservationForm.get('until')?.setValidators([
        Validators.required,
        timeRangeValidator(from, until)
      ]);

      this.reservationForm.get('from')?.updateValueAndValidity();
      this.reservationForm.get('until')?.updateValueAndValidity();
    }
  }

  parseJwt(token: string): any {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(atob(base64).split('').map(c => {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join(''));

      const parsedToken = JSON.parse(jsonPayload);
      console.log('Decoded Token:', parsedToken);
      return parsedToken;
    } catch (e) {
      console.error('Error parsing JWT', e);
      return null;
    }
  }

  onSave(): void {
    if (this.reservationForm.valid && this.selectedWorkDay) {
      const token = localStorage.getItem('user');
      if (token) {
        const decodedToken = this.parseJwt(token);
        if (decodedToken && decodedToken.sub) {
          this.reservationService.getUserByEmail(decodedToken.sub).subscribe(
            (userFound: User) => {
              const selectedDate = new Date(this.reservationForm.value.date);
              const fromTime = this.reservationForm.value.from;
              const untilTime = this.reservationForm.value.until;


              const [fromHours, fromMinutes] = fromTime.split(':').map(Number);
              const [untilHours, untilMinutes] = untilTime.split(':').map(Number);

              selectedDate.setHours(fromHours, fromMinutes);
              const from = new Date(selectedDate);


              selectedDate.setHours(untilHours, untilMinutes);
              const until = new Date(selectedDate);

              console.log("Sending from which is: " + from + "\nSending until which is: " + until);
              console.log(this.data.facility);

              const reservation: ExerciseToSendDTO = {
                userId: userFound.id!,
                facilityId: this.data.facility.id!,
                from: from,
                until: until
              };

              console.log("This is from: " + reservation.from + "\nThis is until: " + reservation.until);
              if (this.isWithinWorkingHours(this.reservationForm.value.from, this.reservationForm.value.until)) {
                this.reservationService.createReservation(reservation).subscribe(
                  (response: ExerciseToSendDTO) => {
                    this.dialogRef.close(response);
                  },
                  (error: any) => {
                    console.error('Error creating reservation:', error);
                  }
                );
              } else {
                alert('Reservation time is outside of the facility\'s working hours.');
              }
            },
            (error: any) => {
              console.error('Error fetching user by email:', error);
            }
          );
        } else {
          console.error('Invalid token or email not found in token');
        }
      } else {
        console.error('No JWT token found in localStorage');
      }
    }
  }



  onCancel(): void {
    this.dialogRef.close();
  }

  isWithinWorkingHours(from: Date, until: Date): boolean {
    if (!this.selectedWorkDay) return false;
    console.log('From:', from);
    console.log('Until:', until);
    console.log('Selected Work Day From:', this.selectedWorkDay.from);
    console.log('Selected Work Day Until:', this.selectedWorkDay.until);
    const fromDate = from.toString();
    console.log(fromDate);

    return fromDate >= this.selectedWorkDay.from && until.toString() <= this.selectedWorkDay.until;
  }

  private getWeekdayIndex(weekday: string): number {
    const weekdays = ['SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY'];
    return weekdays.indexOf(weekday.toUpperCase());
  }
}
