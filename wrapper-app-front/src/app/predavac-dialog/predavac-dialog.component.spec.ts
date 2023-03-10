import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PredavacDialogComponent } from './predavac-dialog.component';

describe('PredavacDialogComponent', () => {
  let component: PredavacDialogComponent;
  let fixture: ComponentFixture<PredavacDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PredavacDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PredavacDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
