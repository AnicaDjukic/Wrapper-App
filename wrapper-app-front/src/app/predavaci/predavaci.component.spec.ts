import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PredavaciComponent } from './predavaci.component';

describe('PredavaciComponent', () => {
  let component: PredavaciComponent;
  let fixture: ComponentFixture<PredavaciComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PredavaciComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PredavaciComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
