// dateRange.validator.ts
import { AbstractControl, ValidatorFn } from '@angular/forms';

export const dateRangeValidator: ValidatorFn = (control: AbstractControl): { [key: string]: any } | null => {
  const startDate = control.get('startDate')?.value;
  const endDate = control.get('endDate')?.value;

  if (startDate && endDate && new Date(startDate) > new Date(endDate)) {
    return { 'dateRangeError': true }; // Return error if startDate is after endDate
  }

  return null; // No error if validation passes
};
