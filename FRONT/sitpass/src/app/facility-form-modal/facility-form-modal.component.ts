import { Component, Inject, ChangeDetectorRef } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { Facility, WorkDay, Discipline, dayOfWeek } from '../models/facility.model';
import { FacilityService } from '../services/facility.service';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { ImageService } from '../services/image.service';
import { photoCountValidator } from "../validators/photoCount.validator";
import { uniqueWeekDaysValidator } from "../validators/uniqueWeekDaysValidator";
import { HttpClient } from "@angular/common/http";
import { ConfigService } from "../services/config.service";
import { FacilityDocumentFileResponse } from "../models/facility-index.model";

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
  documentFile: File | null = null;
  facility: Facility | null = null;

  constructor(
    public dialogRef: MatDialogRef<FacilityFormModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { facility: Facility | null, isAdmin: boolean },
    private fb: FormBuilder,
    private facilityService: FacilityService,
    private cdr: ChangeDetectorRef,
    private sanitizer: DomSanitizer,
    private imageService: ImageService,
    private http: HttpClient,
    private config: ConfigService
  ) {
    this.isAdmin = data.isAdmin;
    this.facility = data.facility || {} as Facility;

    this.facilityForm = this.fb.group({
      name: [this.facility.name || '', Validators.required],
      description: [this.facility.description || '', Validators.required],
      address: [this.facility.address || '', Validators.required],
      city: [this.facility.city || '', Validators.required],
      workDays: this.fb.array(this.initWorkDays(this.facility.workDays || this.getDefaultWorkDays()), uniqueWeekDaysValidator),
      disciplines: this.fb.array(this.initDisciplines(this.facility.disciplines || [])),
      photos: this.fb.array(this.initPhotos(this.facility.images || []), [photoCountValidator()])
    });

    if (this.facility.images) {
      this.facility.images.forEach((image, index) => this.fetchImage(image, index));
    }

    if (this.facility.id) {
      this.http.get<FacilityDocumentFileResponse>(this.config.getIndexUrl(this.facility.id))
        .subscribe({
          next: response => this.fetchDocument(response.serverFilename),
          error: err => console.warn('Could not fetch document info:', err)
        });
    }
  }

  /** Initialize photos array */
  initPhotos(images: string[]): FormGroup[] {
    return images.map(image => this.fb.group({ path: [image] }));
  }

  /** Accessors */
  get workDays(): FormArray { return this.facilityForm.get('workDays') as FormArray; }
  get disciplines(): FormArray { return this.facilityForm.get('disciplines') as FormArray; }
  get photos(): FormArray { return this.facilityForm.get('photos') as FormArray; }

  /** WorkDay helpers */
  getDefaultWorkDays(): WorkDay[] {
    return this.daysOfWeek.map(day => ({ id: undefined, validFrom: new Date(), day, from: '', until: '' }));
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

  /** Discipline helpers */
  initDisciplines(disciplines: Discipline[]): FormGroup[] {
    return disciplines.map(discipline => this.fb.group({ id: [discipline.id], name: [discipline.name, Validators.required] }));
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

  removeWorkDay(index: number): void { this.workDays.removeAt(index); }

  addDiscipline(): void { this.disciplines.push(this.fb.group({ id: [null], name: ['', Validators.required] })); }

  removeDiscipline(index: number): void { this.disciplines.removeAt(index); }

  /** Photo helpers */
  addPhoto(): void { this.photos.push(this.fb.control('')); this.photoPaths.push(''); }

  removePhoto(index: number): void {
    const photoControl = this.photos.at(index);
    if (photoControl.value && typeof photoControl.value.path === 'string') this.photosToRemove.push(photoControl.value.path);
    this.photos.removeAt(index);
    this.photoPaths.splice(index, 1);
    this.cdr.detectChanges();
  }

  onFileChange(event: Event, index: number): void {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;
    const file = input.files[0];
    this.photos.at(index).setValue(file);
    this.newPhotos[index] = file;

    const reader = new FileReader();
    reader.onload = e => { this.photoPaths[index] = this.sanitizer.bypassSecurityTrustUrl(e.target!.result as string); this.cdr.detectChanges(); };
    reader.readAsDataURL(file);
  }

  fetchImage(path: string, index: number): void {
    console.log("Fetching image from path:", path);
    const filename = path.replace(/^.*[\\\/]/, '');
    this.imageService.getImage(filename).subscribe({
      next: response => {
        console.log("Image received:", response);
        console.log("Blob size:", response.size);
        const url = window.URL.createObjectURL(response);
        this.photoPaths[index] = this.sanitizer.bypassSecurityTrustUrl(url);
        this.cdr.detectChanges();
      },
      error: err => {
        console.error('Error fetching image:', err);
      }
    });
  }


  /** Document helpers */
  private fetchDocument(existingDocumentName: string) {
    if (!existingDocumentName) return;

    this.http.get(this.config.getFileGetter(existingDocumentName), { responseType: 'blob' })
      .subscribe({
        next: blob => {
          if (!blob || blob.size === 0) { console.warn(`Document ${existingDocumentName} is empty.`); return; }
          this.documentFile = new File([blob], existingDocumentName, { type: blob.type });
        },
        error: err => {
          console.error(`Failed to fetch document ${existingDocumentName}:`, err);
          alert(`Could not fetch document "${existingDocumentName}".`);
          this.documentFile = null;
        }
      });
  }

  onDocumentChange(event: any) {
    const file = event.target.files[0];
    if (!file) return;
    const allowedTypes = ['application/pdf'];
    if (!allowedTypes.includes(file.type)) { alert('Only PDF documents are allowed.'); return; }
    this.documentFile = file;
  }

  removeDocument() {
    if (!this.documentFile) return;
    const fileName = this.documentFile.name;
    this.http.delete(this.config.getFileGetter(fileName)).subscribe({
      next: () => { console.log(`Document ${fileName} removed.`); this.documentFile = null;
          this.nullifyDocument(this.facility!.id!)
        },
      error: err => { console.error(`Failed to remove document ${fileName}:`, err); alert('Could not remove document.'); }
    });
  }

  nullifyDocument(id: number) {
    const formData = new FormData();

    // Create an empty "file" to indicate null
    const emptyFile = new Blob([], { type: 'application/pdf' });
    formData.append('file', emptyFile, 'empty.pdf');

    this.http.post(this.config.getIndexUrl(id), formData).subscribe({
      next: () => console.log("Document nullified successfully."),
      error: err => console.error("Failed to nullify document:", err)
    });
  }

  insertIndexDocument(response: Facility) {
    if (!this.documentFile) return;
    const allowedTypes = ['application/pdf'];
    if (!allowedTypes.includes(this.documentFile.type)) { alert('Please upload a valid PDF.'); return; }

    const formData = new FormData();
    formData.append('file', this.documentFile);

    this.http.post(this.config.getIndexUrl(response.id), formData).subscribe({
      next: () => console.log('PDF successfully indexed.'),
      error: err => { console.error('Failed to index PDF', err); alert('Error uploading PDF.'); }
    });
  }

  /** Save & cancel */
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

    this.newPhotos.forEach(photo => { if (photo) formData.append('photos', photo); });

    const saveObservable = this.data.facility?.id
      ? this.facilityService.updateFacility(this.data.facility.id, formData)
      : this.facilityService.createFacility(formData);

    saveObservable.subscribe(response => {
      this.dialogRef.close(response);
      if (this.documentFile) {
        this.insertIndexDocument(response);
      }
      else if (response.id) {
        this.addIndexToStore(response.id);
      }

    });
  }

  onCancel(): void { this.dialogRef.close(); }


  addIndexToStore(facilityId: number): void {
    const formData = new FormData();
    const emptyFile = new Blob([], { type: 'application/pdf' });
    formData.append('file', emptyFile, 'empty.pdf');


    this.http.post(this.config.getIndexUrl(facilityId), formData).subscribe({
      next: () => console.log('PDF successfully indexed.'),
      error: err => { console.error('Failed to index PDF', err); alert('Error uploading PDF.'); }
    });
  }
}
