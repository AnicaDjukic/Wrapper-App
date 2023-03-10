import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PredmetDialogComponent } from './predmet-dialog.component';

describe('PredmetDialogComponent', () => {
  let component: PredmetDialogComponent;
  let fixture: ComponentFixture<PredmetDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PredmetDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PredmetDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
