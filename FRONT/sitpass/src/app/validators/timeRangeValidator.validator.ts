import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function timeRangeValidator(minTime: string, maxTime: string): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;
    if (value && (value < minTime || value > maxTime)) {
      return { timeRange: true };
    }
    return null;
  };
}
