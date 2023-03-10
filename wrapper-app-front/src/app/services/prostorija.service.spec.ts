import { TestBed } from '@angular/core/testing';

import { ProstorijaService } from './prostorija.service';

describe('ProstorijaService', () => {
  let service: ProstorijaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProstorijaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
