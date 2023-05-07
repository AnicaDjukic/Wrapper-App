import { TestBed } from '@angular/core/testing';

import { OrgJedinicaService } from './org-jedinica.service';

describe('OrgJedinicaService', () => {
  let service: OrgJedinicaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrgJedinicaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
