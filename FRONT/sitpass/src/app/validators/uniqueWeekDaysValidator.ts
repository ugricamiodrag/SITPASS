import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export const uniqueWeekDaysValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const workDays = control.value;
  const days = workDays.map((workDay: any) => workDay.day);

  const hasDuplicates = days.some((day: any, index: number) => days.indexOf(day) !== index);
  return hasDuplicates ? { nonUniqueWeekDays: true } : null;
};
