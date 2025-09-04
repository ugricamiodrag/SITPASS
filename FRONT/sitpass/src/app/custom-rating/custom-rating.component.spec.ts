import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomRatingComponent } from './custom-rating.component';

describe('CustomRatingComponent', () => {
  let component: CustomRatingComponent;
  let fixture: ComponentFixture<CustomRatingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CustomRatingComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CustomRatingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
