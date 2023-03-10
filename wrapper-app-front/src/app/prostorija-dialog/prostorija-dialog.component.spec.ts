import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProstorijaDialogComponent } from './prostorija-dialog.component';

describe('ProstorijaDialogComponent', () => {
  let component: ProstorijaDialogComponent;
  let fixture: ComponentFixture<ProstorijaDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProstorijaDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProstorijaDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
