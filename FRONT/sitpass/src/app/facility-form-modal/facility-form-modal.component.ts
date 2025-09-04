import { Component, Inject, ChangeDetectorRef } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { Facility, WorkDay, Discipline, dayOfWeek } from '../models/facility.model';
import { FacilityService } from '../services/facility.service';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { ImageService } from '../services/image.service';
import {photoCountValidator} from "../validators/photoCount.validator";
import {uniqueWeekDaysValidator} from "../validators/uniqueWeekDaysValidator";

@Component({
  selector: 'app-facility-form-modal',
  templateUrl: './facility-form-modal.component.html',
  styleUrls: ['./facility-form-modal.component.css']
})
export class FacilityFormModalComponent {
  facilityForm: FormGroup;
  daysOfWeek = Object.values(dayOfWeek);
  photoPaths: SafeUrl[] = [];
  newPhotos: File[] = [];
  photosToRemove: string[] = [];
  isAdmin: boolean;

  constructor(
    public dialogRef: MatDialogRef<FacilityFormModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { facility: Facility | null, isAdmin: boolean },
    private fb: FormBuilder,
    private facilityService: FacilityService,
    private cdr: ChangeDetectorRef,
    private sanitizer: DomSanitizer,
    private imageService: ImageService
  ) {
    this.isAdmin = data.isAdmin;
    const facility = data.facility || {} as Facility;
    this.facilityForm = this.fb.group({
      name: [facility.name || '', Validators.required],
      description: [facility.description || '', Validators.required],
      address: [facility.address || '', Validators.required],
      city: [facility.city || '', Validators.required],
      workDays: this.fb.array(this.initWorkDays(facility.workDays || this.getDefaultWorkDays()), uniqueWeekDaysValidator),
      disciplines: this.fb.array(this.initDisciplines(facility.disciplines || [])),
      photos: this.fb.array(this.initPhotos(facility.images || []), [photoCountValidator()])
    });

    if (facility.images) {
      facility.images.forEach((image, index) => {
        this.fetchImage(image, index);
      });
    }


  }

  initPhotos(images: string[]): FormGroup[] {
    return images.map(image => this.fb.group({
      path: [image]
    }));
  }

  get workDays(): FormArray {
    return this.facilityForm.get('workDays') as FormArray;
  }

  get disciplines(): FormArray {
    return this.facilityForm.get('disciplines') as FormArray;
  }

  get photos(): FormArray {
    return this.facilityForm.get('photos') as FormArray;
  }

  getDefaultWorkDays(): WorkDay[] {
    console.log(this.isAdmin);
    return this.daysOfWeek.map(day => ({
      id: undefined,
      validFrom: new Date(),
      day: day,
      from: '',
      until: ''
    }));
  }

  initWorkDays(workDays: WorkDay[]): FormGroup[] {
    return workDays.map(day => this.fb.group({
      id: [day.id],
      validFrom: [day.validFrom, Validators.required],
      day: [dayOfWeek[day.day], Validators.required],
      from: [day.from, Validators.required],
      until: [day.until, Validators.required]
    }));
  }

  initDisciplines(disciplines: Discipline[]): FormGroup[] {
    return disciplines.map(discipline => this.fb.group({
      id: [discipline.id],
      name: [discipline.name, Validators.required]
    }));
  }

  addWorkDay(): void {
    this.workDays.push(this.fb.group({
      id: [null],
      validFrom: [new Date().toISOString().split('T')[0], Validators.required],
      day: [null, Validators.required],
      from: ['', Validators.required],
      until: ['', Validators.required]
    }));
  }

  removeWorkDay(index: number): void {
    this.workDays.removeAt(index);
  }

  addDiscipline(): void {
    this.disciplines.push(this.fb.group({
      id: [null],
      name: ['', Validators.required]
    }));
  }

  removeDiscipline(index: number): void {
    this.disciplines.removeAt(index);
  }

  addPhoto(): void {
    this.photos.push(this.fb.control(''));
    this.photoPaths.push('');
  }

  removePhoto(index: number): void {
    const photoControl = this.photos.at(index);
    console.log(photoControl);
    console.log(photoControl.value.path)
    if (photoControl.value && typeof photoControl.value.path === 'string') {
      // If the photo is an existing photo (string path), mark it for deletion
      this.photosToRemove.push(photoControl.value.path);
    }
    this.photos.removeAt(index);
    this.photoPaths.splice(index, 1);
    this.cdr.detectChanges();
  }

  onFileChange(event: Event, index: number): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      this.photos.at(index).setValue(file);
      this.newPhotos[index] = file;
      const reader = new FileReader();
      reader.onload = (e) => {
        this.photoPaths[index] = this.sanitizer.bypassSecurityTrustUrl(e.target!.result as string);
        this.cdr.detectChanges();
      };
      reader.readAsDataURL(file);
    }
  }

  fetchImage(path: string, index: number): void {
    this.imageService.getImage(path).subscribe(response => {
      const url = window.URL.createObjectURL(response);
      this.photoPaths[index] = this.sanitizer.bypassSecurityTrustUrl(url);
      this.cdr.detectChanges();
    }, error => {
      console.error('Error fetching image:', error);
    });
  }

  onSave(): void {
    const formData: FormData = new FormData();
    const facilityData = this.facilityForm.value;

    formData.append('name', facilityData.name);
    formData.append('description', facilityData.description);
    formData.append('address', facilityData.address);
    formData.append('city', facilityData.city);
    formData.append('workDays', JSON.stringify(facilityData.workDays));
    formData.append('disciplines', JSON.stringify(facilityData.disciplines));
    formData.append('photosToRemove', JSON.stringify(this.photosToRemove));

    this.newPhotos.forEach((photo, index) => {
      if (photo) {
        formData.append('photos', photo);
      }
    });

    if (this.data.facility && this.data.facility.id) {
      this.facilityService.updateFacility(this.data.facility.id, formData).subscribe(response => {
        this.dialogRef.close(response);

      });
    } else {
      this.facilityService.createFacility(formData).subscribe(response => {
        this.dialogRef.close(response);

      });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
