import { TestBed } from '@angular/core/testing';

import { RealizacijaService } from './realizacija.service';

describe('RealizacijaService', () => {
  let service: RealizacijaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RealizacijaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
