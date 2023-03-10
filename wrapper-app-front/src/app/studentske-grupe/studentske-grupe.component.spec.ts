import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentskeGrupeComponent } from './studentske-grupe.component';

describe('StudentskeGrupeComponent', () => {
  let component: StudentskeGrupeComponent;
  let fixture: ComponentFixture<StudentskeGrupeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StudentskeGrupeComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StudentskeGrupeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
