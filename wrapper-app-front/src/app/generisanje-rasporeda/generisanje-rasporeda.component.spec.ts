import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GenerisanjeRasporedaComponent } from './generisanje-rasporeda.component';

describe('GenerisanjeRasporedaComponent', () => {
  let component: GenerisanjeRasporedaComponent;
  let fixture: ComponentFixture<GenerisanjeRasporedaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GenerisanjeRasporedaComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GenerisanjeRasporedaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
