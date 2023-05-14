import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NoviSemestarComponent } from './novi-semestar.component';

describe('NoviSemestarComponent', () => {
  let component: NoviSemestarComponent;
  let fixture: ComponentFixture<NoviSemestarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NoviSemestarComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NoviSemestarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
