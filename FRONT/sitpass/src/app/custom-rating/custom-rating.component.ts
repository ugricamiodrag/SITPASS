import {Component, forwardRef} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from "@angular/forms";
import { ClickEvent } from 'angular-star-rating';

@Component({
  selector: 'app-custom-rating',
  template: `<star-rating [starType]="'svg'" [rating]="rating" (starClickChange)="onRate($event)"></star-rating>`,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => CustomRatingComponent),
      multi: true
    }
  ]
})
export class CustomRatingComponent implements ControlValueAccessor {
  rating: number = 0;

  private onChange: (value: number) => void = () => {};
  private onTouch: () => void = () => {};

  writeValue(value: number): void {
    if (value !== undefined) {
      this.rating = value;
    }
  }

  registerOnChange(fn: (value: number) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouch = fn;
  }

  onRate(event: ClickEvent): void {
    this.rating = event.rating;
    this.onChange(this.rating);
    this.onTouch();
  }
}
