import { TestBed } from '@angular/core/testing';

import { PredavacService } from './predavac.service';

describe('PredavacService', () => {
  let service: PredavacService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PredavacService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
