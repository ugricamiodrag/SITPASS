import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchFacilityComponent } from './search-facility.component';

describe('SearchFacilityComponent', () => {
  let component: SearchFacilityComponent;
  let fixture: ComponentFixture<SearchFacilityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SearchFacilityComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SearchFacilityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
