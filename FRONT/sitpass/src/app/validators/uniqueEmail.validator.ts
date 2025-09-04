import { AbstractControl, ValidationErrors, ValidatorFn, AsyncValidatorFn } from '@angular/forms';
import { Observable, of } from 'rxjs';
import { map, catchError, debounceTime, take, switchMap } from 'rxjs/operators';
import { RegistrationFormService } from '../services/registration-form.service';

export function uniqueEmailValidator(registrationService: RegistrationFormService): AsyncValidatorFn {
  return (control: AbstractControl): Observable<ValidationErrors | null> => {
    if (!control.value) {
      return of(null); 
    }

    return control.valueChanges.pipe(
      debounceTime(300),  
      take(1),
      switchMap(email =>
        registrationService.checkEmail(email).pipe(
          map(response => {
         
            return response.exists ? { 'emailTaken': true } : null;
          }),
          catchError(() => of(null)) 
        )
      )
    );
  };
}
