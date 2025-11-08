import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NouvelleFormation } from './nouvelle-formation';

describe('NouvelleFormation', () => {
  let component: NouvelleFormation;
  let fixture: ComponentFixture<NouvelleFormation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NouvelleFormation]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NouvelleFormation);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
