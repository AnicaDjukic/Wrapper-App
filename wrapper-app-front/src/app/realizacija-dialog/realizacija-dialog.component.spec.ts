import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RealizacijaDialogComponent } from './realizacija-dialog.component';

describe('RealizacijaDialogComponent', () => {
  let component: RealizacijaDialogComponent;
  let fixture: ComponentFixture<RealizacijaDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RealizacijaDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RealizacijaDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
