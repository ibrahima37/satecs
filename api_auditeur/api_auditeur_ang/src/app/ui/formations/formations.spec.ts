import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Formations } from './formations';

describe('Formations', () => {
  let component: Formations;
  let fixture: ComponentFixture<Formations>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Formations]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Formations);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
