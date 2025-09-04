import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FacilityFormModalComponent } from './facility-form-modal.component';

describe('FacilityFormModalComponent', () => {
  let component: FacilityFormModalComponent;
  let fixture: ComponentFixture<FacilityFormModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FacilityFormModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FacilityFormModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
