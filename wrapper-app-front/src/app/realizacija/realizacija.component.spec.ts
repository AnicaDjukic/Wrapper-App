import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RealizacijaComponent } from './realizacija.component';

describe('RealizacijaComponent', () => {
  let component: RealizacijaComponent;
  let fixture: ComponentFixture<RealizacijaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RealizacijaComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RealizacijaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
