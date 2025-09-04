import { AbstractControl, FormArray, ValidationErrors, ValidatorFn } from '@angular/forms';

export function photoCountValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const formArray = control as FormArray;
    if (formArray.length === 1) {
      return { invalidPhotoCount: true };
    }
    return null;
  };
}
