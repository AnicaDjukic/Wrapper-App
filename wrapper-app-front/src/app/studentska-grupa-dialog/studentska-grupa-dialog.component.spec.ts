import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentskaGrupaDialogComponent } from './studentska-grupa-dialog.component';

describe('StudentskaGrupaDialogComponent', () => {
  let component: StudentskaGrupaDialogComponent;
  let fixture: ComponentFixture<StudentskaGrupaDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StudentskaGrupaDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StudentskaGrupaDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
