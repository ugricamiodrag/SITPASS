import { TestBed } from '@angular/core/testing';

import { CustomStarConfigService } from './custom-star-config.service';

describe('CustomStarConfigService', () => {
  let service: CustomStarConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CustomStarConfigService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
