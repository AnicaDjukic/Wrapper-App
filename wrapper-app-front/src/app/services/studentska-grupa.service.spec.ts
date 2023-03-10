import { TestBed } from '@angular/core/testing';

import { StudentskaGrupaService } from './studentska-grupa.service';

describe('StudentskaGrupaService', () => {
  let service: StudentskaGrupaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StudentskaGrupaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
